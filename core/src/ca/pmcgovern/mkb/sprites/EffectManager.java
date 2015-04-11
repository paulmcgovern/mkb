/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.sprites;

import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.screens.TaskStore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author mcgovern
 */
public class EffectManager {
    
    
    private static EffectManager instance;
    private boolean isRunning;
    private ParticleEffect inProgressEffect;
    private ParticleEffect dustEffect;
    
    private AssetManager assetMgr;
    
    
    public static final int COLLIDE_DURATION_MS = 1500;
    
    /** time since collision sound last played, MS. */
    private long lastCollisionPlayTime;
    
    private float inProgressEffectScaleDefaultScale;
    private float prevCameraZoom;
    
    // TOOD: dispose?
    /*
    public static synchronized EffectManager getInstance() {
     
        if( instance == null ) {
            instance = new EffectManager();
        }
                
        return instance;
    }
    */
        
            
    public EffectManager( AssetManager assetMgr ) {   
    	
        if( assetMgr == null ) {
            throw new IllegalArgumentException( "Asset manager is null." );
        }
        this.assetMgr = assetMgr;
        this.inProgressEffect = new ParticleEffect();
        // TODO: get these items from AssetManager
        this.inProgressEffect.load(Gdx.files.internal("data/inProgress.p"), Gdx.files.internal("data"));
        
        this.inProgressEffectScaleDefaultScale = this.inProgressEffect.getEmitters().first().getScale().getHighMax();
   
        this.dustEffect = new ParticleEffect();
        this.dustEffect.load(Gdx.files.internal("data/dust.p"), Gdx.files.internal("data"));
   
    }
    
    public void draw( Batch batch, float delta ) {
        
        if( !this.inProgressEffect.isComplete() ) {
            this.inProgressEffect.draw(batch, delta);
        }
        
        if( !this.dustEffect.isComplete() ) {
         
            this.dustEffect.draw( batch, delta );
        }
    }
    
    public void startInProgressEffect( Actor target ) {
        this.isRunning = true;
    //    updateInProgressEffect(target);
        this.inProgressEffect.reset();
        Array<ParticleEmitter> emtrs = this.inProgressEffect.getEmitters();
        for( ParticleEmitter q : emtrs ) {
            q.setContinuous(true);
        }
        this.inProgressEffect.start(); 
    }
    
    
    public void stopInProgressEffect() {
                
        Array<ParticleEmitter> emtrs = this.inProgressEffect.getEmitters();
        for( ParticleEmitter q : emtrs ) {
            q.setContinuous(false);
        }
        this.isRunning = false;
    }
    
    public void updateInProgressEffect( Actor target ) {
        
        if( target == null ) {
            this.stopInProgressEffect();
            return;
        }
        
        
        Stage stg = target.getStage();
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() - (target.getHeight() / 2)));
        this.inProgressEffect.setPosition( effectCenter.x, Gdx.graphics.getHeight()-effectCenter.y );
        
        ParticleEmitter emitter = this.inProgressEffect.getEmitters().first();
      
        float currentZoom = ((OrthographicCamera)stg.getCamera()).zoom;
        
        if( this.prevCameraZoom != currentZoom ) {
        
            this.prevCameraZoom = currentZoom;
            
            float scale = this.inProgressEffectScaleDefaultScale / currentZoom;
            emitter.getScale().setHigh( scale, scale );
        }
       
        if( this.inProgressEffect.isComplete() ) {
            this.startInProgressEffect(target);
        }
    
    }
    
    public void playClick() {
        
        Sound clickSound = this.assetMgr.get( "data/sounds/Click2-Sebastian-759472264.mp3", Sound.class );
        clickSound.play( 0.75f );
    }
    
    
    public void collision( Actor movingActor, Actor stationaryActor ) {

        
        long now = System.currentTimeMillis();
        
        if( now - this.lastCollisionPlayTime > COLLIDE_DURATION_MS ) {
            Sound collideSound = this.assetMgr.get( "data/sounds/Metal Clang-SoundBible.com-19572601.mp3",Sound.class );
            this.lastCollisionPlayTime = now;
            collideSound.play( 0.5f );
           
            Stage stg = movingActor.getStage();
            Vector2 mid = new Vector2( (movingActor.getX() + stationaryActor.getX())/2, (movingActor.getY() + stationaryActor.getY() )/2);
            Vector2 midScreen = stg.stageToScreenCoordinates(mid);
            
            
            this.dustEffect.setPosition( midScreen.x,Gdx.graphics.getHeight() - midScreen.y );
            this.dustEffect.start();
            
          
        }
    }
}

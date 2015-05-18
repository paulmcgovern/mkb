/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.sprites;

import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.screens.TaskStore;
import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.ui.Task.TaskState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mcgovern
 */
public class EffectManager {
    
    
    private static EffectManager instance;
    private boolean isRunning;
    private ParticleEffect inProgressEffect;
    private ParticleEffect dustEffect;
    private ParticleEffect doneEffect;
    
    private AssetManager assetMgr;
   Array<PooledEffect> effects = new Array<PooledEffect>();
    
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
   
        this.doneEffect = new ParticleEffect();
        this.doneEffect.load( Gdx.files.internal( "data/done.p"), Gdx.files.internal("data"));
   
        
    }
    
    public void draw( Batch batch, float delta ) {
        
        if( !this.inProgressEffect.isComplete() ) {
            this.inProgressEffect.draw(batch, delta);
        }
        
        if( !this.dustEffect.isComplete() ) {         
            this.dustEffect.draw( batch, delta );
        }
        
        if( !this.doneEffect.isComplete() ) {
            this.doneEffect.draw( batch, delta );
        }
    }
    
    public void startInProgressEffect( Actor target ) {
        this.isRunning = true;

        this.inProgressEffect.reset();
        Array<ParticleEmitter> emtrs = this.inProgressEffect.getEmitters();
        for( ParticleEmitter q : emtrs ) {
            q.setContinuous(true);
        }
        Stage stg = target.getStage();
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() + (target.getHeight() / 2)));
        this.inProgressEffect.setPosition(effectCenter.x, effectCenter.y );
        this.inProgressEffect.start(); 
    }
    
    
    Actor doneEffectTargetActor = null;
    
    
    private void updateDoneEffect( Stage stg ) {
        
        if( this.doneEffect.isComplete() ) {
            this.doneEffectTargetActor = null;
            return;
        }
        
        if( this.doneEffectTargetActor == null ) {
            return;
        }
   
      //  Stage stg = this.doneEffectTargetActor.getStage();
        float x = this.doneEffectTargetActor.getX();
        float y = this.doneEffectTargetActor.getY();
        float width = this.doneEffectTargetActor.getWidth();
        float height = this.doneEffectTargetActor.getHeight();
    
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( x + (width / 2), y + (height / 2)));

       // Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() + (target.getHeight() / 2)));
     this.doneEffect.setPosition( effectCenter.x, Gdx.graphics.getHeight() - effectCenter.y );
      //this.doneEffect.setPosition( target.getX(), target.getY() );
       
        
        
    //    Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( x + ( width/ 2), y - (height / 2)));
    //    this.doneEffect.setPosition( effectCenter.x, Gdx.graphics.getHeight()-effectCenter.y );
        
        ParticleEmitter emitter = this.doneEffect.getEmitters().first();
     //System.err.println( "DELAY:" + emitter.getDelay().getLowMin() + " "+ emitter.getDelay().isActive() );
        float currentZoom = ((OrthographicCamera)stg.getCamera()).zoom;
     //   this.doneEffect.getEmitters().first().
        if( this.prevCameraZoom != currentZoom ) {
        
            this.prevCameraZoom = currentZoom;
            
            float scale = this.inProgressEffectScaleDefaultScale / currentZoom;
            emitter.getScale().setHigh( scale, scale );
        }
       
        
    }
    
    
    public void startDoneEffect( Actor target ) {
        
        this.doneEffect.reset();         
        this.doneEffectTargetActor = target;
     this.doneEffect.flipY();
        Stage stg = target.getStage();
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() + (target.getHeight() / 2)));
  //  System.err.println( "TARG: "+ target.getX() + " " + target.getY() ); 
   // System.err.println( "screne:" + effectCenter );////Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX(), target.getY() ));
   // System.err.println( Gdx.graphics.getHeight() );   this.doneEffect.setPosition( effectCenter.x, Gdx.graphics.getHeight() - effectCenter.y );
      //this.doneEffect.setPosition( target.getX(), target.getY() );
        ParticleEmitter emitter = this.doneEffect.getEmitters().first();
emitter.getDelay().setActive( true );

emitter.getDelay().setLow( 3, 3);
        float currentZoom = ((OrthographicCamera)stg.getCamera()).zoom;
        
       // if( this.prevCameraZoom != currentZoom ) {
        
       //     this.prevCameraZoom = currentZoom;
            
            float scale = this.inProgressEffectScaleDefaultScale / currentZoom;
            emitter.getScale().setHigh( scale, scale );
       
        //}
              this.doneEffect.start();
     
    }
  /**  
    private void updateEffectPos( ParticleEffect effect, float x, float y, Stage stage ) { 
    
        float zoom = ((OrthographicCamera)stage.getCamera()).zoom;
        
       // Stage stg = target.getStage();
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() - (target.getHeight() / 2)));
        this.doneEffect.setPosition( effectCenter.x, Gdx.graphics.getHeight()-effectCenter.y );
        
        ParticleEmitter emitter = this.doneEffect.getEmitters().first();
      
        float currentZoom = ((OrthographicCamera)stg.getCamera()).zoom;
        
        if( this.prevCameraZoom != currentZoom ) {
        
            this.prevCameraZoom = currentZoom;
            
            float scale = this.inProgressEffectScaleDefaultScale / currentZoom;
            emitter.getScale().setHigh( scale, scale );
        }
       
        effect
    }
    **/
    
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
        
        
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX() + (target.getWidth() / 2), target.getY() + (target.getHeight() / 2)));
        this.inProgressEffect.setPosition(effectCenter.x, Gdx.graphics.getHeight() -  effectCenter.y );
        
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
        
        Sound clickSound = this.assetMgr.get( "data/sounds/click.mp3", Sound.class );
        clickSound.play( 0.75f );
    }
    
    
    public void collision( Stage stg, Vector2 movingActor, Vector2 stationaryActor ) {

        
        long now = System.currentTimeMillis();
        
        if( now - this.lastCollisionPlayTime > COLLIDE_DURATION_MS ) {
            Sound collideSound = this.assetMgr.get( "data/sounds/clang.mp3",Sound.class );
            this.lastCollisionPlayTime = now;
            collideSound.play( 0.5f );
           
           
            Vector2 mid = new Vector2( (movingActor.x + stationaryActor.x)/2, (movingActor.y + stationaryActor.y )/2);
            Vector2 midScreen = stg.stageToScreenCoordinates(mid);
            
            this.dustEffect.setPosition( midScreen.x,Gdx.graphics.getHeight() - midScreen.y );
            this.dustEffect.start();
        }
    }
    
    
    public void updateAll( Stage stage ) {
        
        Array<Actor> allActors = stage.getActors();
        
        if( allActors == null || allActors.size == 0 ) {
            return;
        }
        List<TaskSprite> taskSprites = new ArrayList<TaskSprite>();
        
        List<TaskSprite> inProgress = new ArrayList<TaskSprite>();
        
        for( int i = 0; i < allActors.size; i++ ) {
            
            Actor a = allActors.get( i );
            
            if( a instanceof TaskSprite ) {
                taskSprites.add( (TaskSprite)a );
                Task t = ((TaskSprite)a).getTask();
                
                if( TaskState.IN_PROGRESS == t.getState() ) {
                    inProgress.add( (TaskSprite)a );
                    updateInProgressEffect( a );
                }
            }            
        }
        
       updateDoneEffect( stage );
        
       // this.updateInProgressEffect(null);
    }
}

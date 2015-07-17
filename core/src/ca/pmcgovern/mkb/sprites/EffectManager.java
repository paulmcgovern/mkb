/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;


/**
 *
 * @author mcgovern
 */
public class EffectManager {
    
    public static final String TAG = "EffectManager";
    
  
    private boolean isRunning;
    private ParticleEffect inProgressEffect;
    private ParticleEffect dustEffect;
    private ParticleEffect doneEffect;
    private ParticleEffect deleteEffect;    
    
    public static final String IN_PROGRESS_ACTOR = "IN_PROGRESS_ACTOR";
    public static final String DONE_EFFECT_ACTOR = "DONE_EFFECT_ACTOR";
    public static final String DELETE_EFFECT_ACTOR = "DELETE_EFFECT_ACTOR";    
    public static final String COLLIDE_EFFECT_ACTOR = "COLLIDE_EFFECT_ACTOR";    
    
    private AssetManager assetMgr;
    Array<PooledEffect> effects = new Array<PooledEffect>();
    
    public static final int COLLIDE_DURATION_MS = 1500;
    
    /** time since collision sound last played, MS. */
    private long lastCollisionPlayTime;
              
            
    public EffectManager( AssetManager assetMgr ) {   
    	
        if( assetMgr == null ) {
            throw new IllegalArgumentException( "Asset manager is null." );
        }
        this.assetMgr = assetMgr;
        this.inProgressEffect = new ParticleEffect();
        // TODO: get these items from AssetManager
        this.inProgressEffect.load(Gdx.files.internal("data/inProgress.p"), Gdx.files.internal("data"));
        
      //  this.inProgressEffectScaleDefaultScale = this.inProgressEffect.getEmitters().first().getScale().getHighMax();
   
        this.dustEffect = new ParticleEffect();
        this.dustEffect.load(Gdx.files.internal("data/dust.p"), Gdx.files.internal("data"));
   
        this.doneEffect = new ParticleEffect();
        this.doneEffect.load( Gdx.files.internal( "data/done.p"), Gdx.files.internal("data"));
   
        this.deleteEffect = new ParticleEffect();
        this.deleteEffect.load( Gdx.files.internal( "data/delete.p"), Gdx.files.internal("data"));
        
    }
  
    /**
     * Adds an actor slaved to the target that wraps the emitter.
     * @param target 
     */
    public void startInProgressEffect( Actor target ) {
        
        if( this.isRunning ) {
            return;
        }
        this.isRunning = true;
        
        Actor effectActor = new ParticleEffectSlaveActor( target, this.inProgressEffect, false );
        effectActor.setName( IN_PROGRESS_ACTOR );
        
        target.getStage().addActor( effectActor );
        
        effectActor.setPosition( target.getX(), target.getY() );//, prevCameraZoom);
       
        
        this.inProgressEffect.reset();
        Array<ParticleEmitter> emtrs = this.inProgressEffect.getEmitters();
        
        for( ParticleEmitter q : emtrs ) {
            q.setContinuous(true);
        }      
    }
    
    
    Actor doneEffectTargetActor = null;
    Actor deleteEffectTargetActor = null;
    

    public void startDeleteEffect( Actor target ) {
        
        stopInProgressEffect();
     
        this.doneEffect.reset();  
        
        
        Actor effectActor = new ParticleEffectSlaveActor( target, this.deleteEffect, true );
        effectActor.setName( DELETE_EFFECT_ACTOR );
        
        target.getStage().addActor( effectActor );
        
        effectActor.setPosition( target.getX(), target.getY() );//, prevCameraZoom);
       
                
        ParticleEmitter emitter = this.deleteEffect.getEmitters().first();
        emitter.getDelay().setActive( true );

        emitter.getDelay().setLow( 3, 3);
        this.deleteEffect.start();
    }    
    
    
    public void startDoneEffect( Actor target ) {
        
        stopInProgressEffect();
     
        this.doneEffect.reset();  
        
        
        Actor effectActor = new ParticleEffectSlaveActor( target, this.doneEffect, false );
        effectActor.setName( DONE_EFFECT_ACTOR );
        
        target.getStage().addActor( effectActor );
        
        effectActor.setPosition( target.getX(), target.getY() );//, prevCameraZoom);
       
                
            ParticleEmitter emitter = this.doneEffect.getEmitters().first();
emitter.getDelay().setActive( true );

emitter.getDelay().setLow( 3, 3);

            this.doneEffect.start();
    }
    
    public void stopInProgressEffect() {
                
        Array<ParticleEmitter> emtrs = this.inProgressEffect.getEmitters();
        for( ParticleEmitter q : emtrs ) {
            q.setContinuous(false);
        }
        this.isRunning = false;
    }
    
    public void playClick() {
        
        Sound clickSound = this.assetMgr.get( "data/sounds/click.mp3", Sound.class );
        clickSound.play( 0.75f );
    }
    
    
    public void collision( Actor actor1, Actor actor2 ) {

        long now = System.currentTimeMillis();
        
        if( now - this.lastCollisionPlayTime > COLLIDE_DURATION_MS ) {
            
            Sound collideSound = this.assetMgr.get( "data/sounds/clang.mp3",Sound.class );
            this.lastCollisionPlayTime = now;
            collideSound.play( 0.5f );
                      
            Vector2 centerActor1 = new Vector2();
            centerActor1.x = actor1.getX() + (actor1.getWidth() / 2f);
            centerActor1.y = actor1.getY() + (actor1.getHeight() / 2f);
        
            Vector2 centerActor2 = new Vector2();
            centerActor1.x = actor2.getX() + (actor2.getWidth() / 2f);
            centerActor1.y = actor2.getY() + (actor2.getHeight() / 2f);
                    
            Vector2 mid = new Vector2( (centerActor1.x + centerActor2.x)/2, (centerActor1.y + centerActor2.y )/2);
            
            Actor effectActor = new ParticleEffectSlaveActor( actor1, this.dustEffect, false );
            effectActor.setName( COLLIDE_EFFECT_ACTOR );
                    
            actor1.getStage().addActor( effectActor );
        
            effectActor.setPosition( mid.x, mid.y );
            
            this.dustEffect.start();
            
        }
    }
    
   
    protected void stopEffect( ParticleEffect p ) {
        
        if( this.inProgressEffect.equals(p)) {
            this.stopInProgressEffect();            
        } else if( this.doneEffect.equals(p)) {
           // TODO: force stop
        }
    }
    
    /**
     * Wrapper around effect to bind it to an on-screen actor.
     * Removes itself when actor is removed or effect is complete.
     */
    class ParticleEffectSlaveActor extends Actor {
    
        private ParticleEffect effect;
        private Actor master;
        
        /** Persist on stage AFTER master actor is gone. **/
        private boolean persist;
        
        public ParticleEffectSlaveActor( Actor master, ParticleEffect effect, boolean persist ) {
            this.effect = effect;
            this.master = master;  
            this.persist = persist;
        }
        
        @Override
        public void draw(Batch batch, float parentAlpha) {
            effect.draw(batch);
        }     
        
        @Override
        public void act(float delta) {
            
            super.act(delta);
            
            Vector2 effectCenter = new Vector2(this.master.getX() + (this.master.getWidth() / 2 ), this.master.getY() + (this.master.getHeight() / 2 ));
         
            this.effect.setPosition(effectCenter.x, effectCenter.y);   
            this.effect.update(delta);
           
            if( !this.persist &&( !this.master.isVisible() || this.master.getStage() == null)) {
                
                EffectManager.this.stopEffect( this.effect );    
                this.addAction( Actions.removeActor() );               
                
            } else if( this.effect.isComplete() ) {
                clear();
                this.addAction( Actions.removeActor() );                 
            }
        }
        
        @Override
        public boolean remove() {  
            return super.remove();
        }
    }
    
    private Sound bgMusic;
   
    public void startBgMusic() {
        
      //  if( this.bgMusic == null ) {
      //      this.bgMusic = this.assetMgr.get( "data/sounds/The Typewriter-0-Anderson_The_Typewriter-2000-6835.mp3", Sound.class );
      //      this.bgMusic.loop( 0.25f );
      //  }
    }
    
    public void pauseBgMusic() {
        
        if( this.bgMusic != null ) {
            this.bgMusic.pause();
        }
    }
    
    
    
}

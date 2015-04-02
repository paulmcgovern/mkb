/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.sprites;

import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.screens.TaskStore;
import com.badlogic.gdx.Gdx;
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
    
    private ParticleEffect inProgressEffect;
    
    // TOOD: dispose?
    
    public static synchronized EffectManager getInstance() {
     
        if( instance == null ) {
            instance = new EffectManager();
        }
                
        return instance;
    }
    
        
            
    private EffectManager() {   
    	
        this.inProgressEffect = new ParticleEffect();
        this.inProgressEffect.load(Gdx.files.internal("data/inProgress.p"), Gdx.files.internal("data"));
        
    }
    
    public void draw( Batch batch, float delta ) {
        
        if( !this.inProgressEffect.isComplete() ) {
            this.inProgressEffect.draw(batch, delta);
        }
    }
    
    public void startInProgressEffect( Actor target ) {
        
        updateInProgressEffect(target);
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
    }
    
    public void updateInProgressEffect( Actor target ) {
        Stage stg = target.getStage();
        Vector2 effectCenter = stg.stageToScreenCoordinates( new Vector2( target.getX(), target.getY() ));
        this.inProgressEffect.setPosition( effectCenter.x, effectCenter.y );
        
    }
    
    
}

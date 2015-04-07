/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.sprites.EffectManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 *
 * @author mcgovern
 */
public class PlayClickListener extends ChangeListener {
           
    private EffectManager effectMgr;
    
    public PlayClickListener( EffectManager effectMgr ) {
        this.effectMgr = effectMgr;
    }
    
    @Override
    public void changed( ChangeListener.ChangeEvent event, Actor actor) {
        this.effectMgr.playClick();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.actions;

import ca.pmcgovern.mkb.fwt.TaskSprite;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 *
 * @author mcgovern
 */
public class SetCollideEnableAction extends Action {

    @Override
    public boolean act( float delta ) { 
        
        ((TaskSprite)this.actor).setCollideEnable();
                    
        return true;
    }        
}

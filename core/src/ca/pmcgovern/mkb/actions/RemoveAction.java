package ca.pmcgovern.mkb.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

public class RemoveAction extends Action {

    @Override
    public boolean act( float delta ) {         	
    	this.actor.getStage().getRoot().removeActor( this.actor );                      
        return true;
    }        
}
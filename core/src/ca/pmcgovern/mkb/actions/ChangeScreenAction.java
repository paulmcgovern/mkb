package ca.pmcgovern.mkb.actions;

import ca.pmcgovern.mkb.screens.MkbScreen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;


public class ChangeScreenAction extends Action {

    private MkbScreen.ScreenId nextScreenId; 
    
    
    public ChangeScreenAction( MkbScreen.ScreenId nextScreenId ) {
        this.nextScreenId = nextScreenId;        
    }
    
    @Override
    public boolean act(float delta) {
        
        this.actor.fire( new ChangeScreenEvent() );

        return true;
    }
    
    public final class ChangeScreenEvent extends Event {
      
        public MkbScreen.ScreenId getNextScreenId() {
            return ChangeScreenAction.this.nextScreenId;
        }
    }
    
}

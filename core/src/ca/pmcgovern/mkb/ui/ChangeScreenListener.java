package ca.pmcgovern.mkb.ui;

import ca.pmcgovern.mkb.actions.ChangeScreenAction;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * TODO: make this a singleton
 * @author mcgovern
 *
 */
public class ChangeScreenListener implements EventListener {

    @Override
    public boolean handle(Event event) {

        if( event instanceof ChangeScreenAction.ChangeScreenEvent ) {           
            
            final MkbScreen.ScreenId nextId = ((ChangeScreenAction.ChangeScreenEvent)event).getNextScreenId(); 
            
            
            // Using the post runner prevents dispose() getting called from within
            // the event handling loop. Yields an IllegalStateException otherwise.
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run() {
                  ScreenManager.getInstance().showScreen( nextId );     
                }
            });
            return true;
        }
        
        return false;
    }
}

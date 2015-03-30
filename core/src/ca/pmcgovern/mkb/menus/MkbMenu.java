package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public abstract class MkbMenu extends Table {

    public enum MenuType { MAIN, SETTINGS, HELP, TASK_DETAILS }
    
    
    public MkbMenu( Skin skin ) {
        super( skin );
    }
    
    public MkbMenu() {
        super();
    }
    
    
    /**
     * Listener to drive app to another screen.
     * @author mcgovern
     *
     */
    class WinChangeListener extends ChangeListener {

        MkbScreen.ScreenId nextScreenId;
        
        public WinChangeListener( MkbScreen.ScreenId nextScreenId ) {
            this.nextScreenId = nextScreenId;
        }
        
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          
            ScreenManager.getInstance().showScreen( this.nextScreenId );
        }
    } 
    
          
}

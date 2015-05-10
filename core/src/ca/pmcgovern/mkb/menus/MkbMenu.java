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
    
  
    
          
}

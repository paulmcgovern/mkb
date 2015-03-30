package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.screens.MkbScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class SettingsWindow extends MkbMenu {

    
    public SettingsWindow(String title, Skin skin) {
        
        super( skin );
       
     
        Table buttonsTable = new Table( skin );
        buttonsTable.setBackground( "default-pane" );

        buttonsTable.add( new Label( "Settings1", skin ));
        buttonsTable.row();
        buttonsTable.add( new Label( "Settings2....", skin ));
        buttonsTable.row();
        buttonsTable.add( new Label( "Settings3...", skin ));
        buttonsTable.row();
        buttonsTable.add( new Label( "Settings4...", skin ));        
        buttonsTable.pack();
        
        this.add( buttonsTable );
        this.pack();
    }

    
    @Override
    public String getName() {
        return MkbMenu.MenuType.SETTINGS.name().toLowerCase(); 
    }
   
    
}

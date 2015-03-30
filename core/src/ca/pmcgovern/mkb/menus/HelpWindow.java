package ca.pmcgovern.mkb.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HelpWindow extends MkbMenu {

    
    public HelpWindow(String title, Skin skin) {
        
        super( skin );
       
                
        Table buttonsTable = new Table( skin );
        buttonsTable.setBackground( "default-pane" );

        buttonsTable.add( new Label( "Help", skin ));
        buttonsTable.row();
        buttonsTable.add( new Label( "Help text", skin ));
         
        buttonsTable.pack();
        
        this.add( buttonsTable );
        this.pack();
    }

    
    @Override
    public String getName() {
        return MkbMenu.MenuType.HELP.name().toLowerCase(); 
    }
    
 
      
      
    
}

package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.ui.NinePatchScaler;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainTable extends MkbMenu {


    
    public MainTable( Skin skin ) {
        
    	super( skin );
    
        
        NinePatchScaler patchScaler = new NinePatchScaler( skin );
                
        Table topTable = new Table( skin );
    
      
        topTable.setBackground( patchScaler.getScaledPatch( 0.3f, "speech-top" ));
       
        this.add( topTable ).expandX().fill();
        this.row();
        
  
        topTable.add( new Label( "Add new task", skin ));
        topTable.row();
        topTable.add( new Label( "Help", skin ));
        topTable.row();
 
        
        Button itb = new ImageTextButton( "Add new task", skin, "new-task-save");
        itb.addListener( new WinChangeListener() );
        
        topTable.add( itb );
        
     
        this.row();
                
        Table botTable = new Table( skin );
        botTable.setBackground( patchScaler.getScaledPatch( 0.3f, "speech-bottom" ));        
        
        this.add( botTable ).expandX().fill();

        this.pack();
     
    }

 
    
    @Override
    public String getName() {        
        return MkbMenu.MenuType.MAIN.name().toLowerCase(); 
    }
    
    
    class WinChangeListener extends ChangeListener {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
                        
            // Not necessary to set/clear window on
            // the parent Screen because we're done with it.
       //     MainWindow.this.addAction( Actions.sequence( Actions.fadeOut( 0.5f ), new RemoveAction()));      
            
        //    ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.NEW_SCREEN );
        }
    } 
    
    
    
    
}   


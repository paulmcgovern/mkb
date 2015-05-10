package ca.pmcgovern.mkb.menus.newtask;

import ca.pmcgovern.mkb.menus.*;
import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.ui.ColourPicker;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


public class FormContainer extends BaseTable {

  
    ImageTextButton save;
    ImageTextButton cancel;
    //    private TextField titleInput;
    public FormContainer( AssetManager assetMgr, TextField titleInput, Table taskSelectTable, ColourPicker colourPicker, ImageTextButton save, ImageTextButton cancel ) {
        
        super(assetMgr);
        
        this.save = save;
        this.cancel = cancel;
        
      
      //  Skin skin = assetMgr.get( "data/icons.json", Skin.class );
      
        Table menuTable = new Table();
         
        
   // Save and cancel buttons
        Table buttonTable = new Table();
        
         // Save button
      //  this.save = new ImageTextButton( "Save", skin, "new-task-save");
      //  MkbScreen.layoutButton( this.save );
        
        // Save button is initially inactive
      //  this.save.setColor( 1, 1, 1, 0.25f );
             
      //  this.cancel = new ImageTextButton( "Cancel", skin, "new-task-cancel" );
      //  MkbScreen.layoutButton( this.cancel );
        
        buttonTable.add( this.save );          
        buttonTable.add( this.cancel );
   
        
        
        menuTable.add( titleInput );
        menuTable.row();
        menuTable.add( taskSelectTable );
        menuTable.row();
        menuTable.add( colourPicker );
        menuTable.row();
        menuTable.add( buttonTable );
        setMenu(  menuTable );
        
        
        pack();
        // TODO Auto-generated constructor stub
    }

    
}

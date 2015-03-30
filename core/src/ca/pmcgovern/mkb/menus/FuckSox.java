package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.menus.MainWindow.WinChangeListener;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskStore;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class FuckSox extends BaseTable {

    public FuckSox(AssetManager assetMgr) {
        
        super(assetMgr);
        
        Skin skin = assetMgr.get( "data/icons.json", Skin.class );
      
        TaskStore store = TaskStore.getInstance();
        int count = store.getTaskCount();
        int completedCount = store.getCompletedTaskCount();
        
        
        // TODO: change to active task name
        // TODO: make this a button to select and zoom to current active task
        String activeTaskName = store.getActiveTaskName();
        
        if( activeTaskName == null ) {
            activeTaskName = "No Active Task";
        }
       
        Table menuTable = new Table();
     
        menuTable.add( new Label( activeTaskName, skin )).colspan( 2 );
        menuTable.row();    
        
        menuTable.add( new Label( Integer.toString( count ), skin, "task-count" ));
        menuTable.add( new Label( Integer.toString( completedCount ), skin, "task-count" ));
        menuTable.row();
        menuTable.add( new Label( "Tasks", skin, "tiny" ));
        menuTable.add( new Label( "Complete", skin, "tiny" ));
        menuTable.row();  
        
        Table buttonTable = new Table();
        
        ImageTextButton itb = new ImageTextButton( "Add New", skin, "main-add" );
        MkbScreen.layoutButton( itb );        

        itb.addListener( new WinChangeListener( MkbScreen.ScreenId.NEW_SCREEN ) );        
        buttonTable.add( itb ).padLeft(  10 );
        
        
        itb = new ImageTextButton( "Settings",   skin, "main-tools" );
        itb.addListener( new WinChangeListener( MkbScreen.ScreenId.SETTINGS ) );   
        MkbScreen.layoutButton( itb );
        buttonTable.add( itb ).padLeft( 10 );
        
        itb = new ImageTextButton( "Help",   skin, "main-info" );
        itb.addListener( new WinChangeListener( MkbScreen.ScreenId.HELP ) );        
        
        MkbScreen.layoutButton( itb );
        buttonTable.add( itb ).padLeft( 10 );
        
        menuTable.add( buttonTable ).colspan( 2 );  
        
    
        setMenu(  menuTable );
        
        // TODO Auto-generated constructor stub
    }


    
    @Override
    public String getName() {        
        return MkbMenu.MenuType.MAIN.name().toLowerCase(); 
    }
    
    
  
}

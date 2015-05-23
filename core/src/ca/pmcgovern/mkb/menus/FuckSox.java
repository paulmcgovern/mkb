package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.GameMain;
import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.screens.MkbScreen;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.screens.TaskStore;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class FuckSox extends BaseTable {

    private EffectManager effectMgr;
    
    public FuckSox( int taskCount, int completedCount, String activeTaskDescription, AssetManager assetMgr, EffectManager effectMgr, ChangeListener winChangeListener ) {
        
        super(assetMgr);
        
        
        this.effectMgr = effectMgr;
        PlayClickListener playClick = new PlayClickListener( this.effectMgr );
        CloseListener closeListener = new CloseListener();
        
        Skin skin = assetMgr.get( "data/icons.json", Skin.class );
      
       // TaskStore store = TaskStore.getInstance();
       
        
        // TODO: change to active task name
        // TODO: make this a button to select and zoom to current active task
      // .. String activeTaskName = store.getActiveTaskName();
       
        Table menuTable = new Table();
     
        if( activeTaskDescription == null ) {
            
            menuTable.add( new Label( GameMain.getString( "NO_ACTIVE_TASK"), skin )).colspan( 2 ); 
         
        } else {
            
            menuTable.add( new Label( GameMain.getString( "WORKING_ON" ), skin, "tiny" )).colspan( 2 ); 
            menuTable.row();
            menuTable.add( new Label( activeTaskDescription, skin, "small" )).colspan( 2 );
        }
           
        menuTable.row();
        
        menuTable.add( new Label( Integer.toString( taskCount ), skin, "task-count" ));
        menuTable.add( new Label( Integer.toString( completedCount ), skin, "task-count" ));
        menuTable.row();
        menuTable.add( new Label( GameMain.getString( "TASKS" ), skin, "tiny" ));
        menuTable.add( new Label( GameMain.getString( "COMPLETE" ), skin, "tiny" ));
        menuTable.row();  
        
        Table buttonTable = new Table();
        
        ImageTextButton itb = new ImageTextButton( GameMain.getString( "ADD" ), skin, "main-add" );
        MkbScreen.layoutButton( itb ); 
        itb.setName( "add" );
        itb.addListener( playClick );
        itb.addListener( closeListener);
        itb.addListener( winChangeListener );  
        
     //   itb.addListener( new WinChangeListener( MkbScreen.ScreenId.NEW_SCREEN ) );
        buttonTable.add( itb ).padLeft(  10 );
        
        
        itb = new ImageTextButton( GameMain.getString( "SETTINGS" ),   skin, "main-tools" );
        itb.setName( "settings" );
        itb.addListener( playClick );   
        itb.addListener( closeListener);
        itb.addListener( winChangeListener );        
        
    //    itb.addListener( new WinChangeListener( MkbScreen.ScreenId.SETTINGS ) );   
        

        MkbScreen.layoutButton( itb );
        buttonTable.add( itb ).padLeft( 10 );
        
        itb = new ImageTextButton(  GameMain.getString( "HELP" ),   skin, "main-info" );
        itb.setName( "help" );
        itb.addListener( playClick );
        itb.addListener( closeListener);
        itb.addListener( winChangeListener );        

    //    itb.addListener( new WinChangeListener( MkbScreen.ScreenId.HELP ) );        
        
        MkbScreen.layoutButton( itb );
        buttonTable.add( itb ).padLeft( 10 );
        
        menuTable.add( buttonTable ).colspan( 2 );  
      
    
        setMenu(  menuTable );
        
        // TODO Auto-generated constructor stub
    }

    class CloseListener extends ChangeListener {
        
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            FuckSox.this.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() )  );
        }
    }
}

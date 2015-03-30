package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.actions.ClearMonsterDialogAction;
import ca.pmcgovern.mkb.menus.FuckSox;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.OverviewScreen;
import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MonsterListener extends ClickListener {

    private MkbScreen parentScreen;
    private Skin skin;
        
    public MonsterListener( MkbScreen parentScreen, Skin skin ){
        this.parentScreen = parentScreen;
        this.skin = skin;   
    }
    
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        
        Actor monster = event.getListenerActor();
        monster.addAction( Actions.scaleBy( -0.15f, -0.15f, 0.25f, Interpolation.exp10 )); 
                  
        return true;
    }

    
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          
        Actor monster = event.getListenerActor();
        monster.addAction( Actions.scaleBy( 0.15f, 0.15f, 0.5f, new Interpolation.ElasticOut( 2, 5, 1, 1 )));
                
        // TODO: disable events in parent screen task stage?
        
        Stage stage = monster.getStage();
        Group root = stage.getRoot();
       
        
        Gdx.app.log( "Monster Listener", "Touch up event:" + event );
        
                
      //  if( ((Event)event) instanceof TaskEvent ) {
            
     //       handleTaskEvent( (TaskEvent)(Event)event );
       //     return;
      //  }
       // 
        
        
    /*    
        Window win = (Window)root.findActor( "settings" );
        
        if( win == null ) {
            win = (Window)root.findActor( "help" );            
        }
        
        if( win == null ) {
            win = (Window)root.findActor( "main" );    
        }
        
          */     
     
        
        
        MkbMenu win = (MkbMenu)root.findActor( "settings" );
        
        if( win == null ) {
            win = (MkbMenu)root.findActor( "help" );            
        }
        
        if( win == null ) {
            win = (MkbMenu)root.findActor( "main" );    
        }
        
        if( win == null ) {
            win = (MkbMenu)root.findActor( "task_details" );
        }
        
        if( win == null ) {
            
         //   WindowStyle s = this.skin.get("default", WindowStyle.class );
         //   s.background = new NinePatchDrawable(this.speechNine);
         //   s.background = this.speechNine;
            
   //         win = new MainWindow( "NARD", this.skin );
            
      //      win.setX( stage.getWidth() / 6 );
      //      win.setY( stage.getWidth() / 6 );
               
        //   
        
        //   AssetManager assetMgr = ;
            
            win = new FuckSox( this.parentScreen.getAssetManager() );
            win.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn( 0.3f)));
            win.setX( stage.getWidth() / 6 );
             win.setY( stage.getWidth() / 6 );
           this.parentScreen.setOpenMenu( win );
       
        } else {
            
            // TODO: save state
            // TODO: fade-in new screen
            
            // Remove the window from the parent screen.
            win.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new ClearMonsterDialogAction( this.parentScreen ) ));           
        }
    } 
    
    

    /**
     *  Listen for events fired from TaskSprites.
     *  If Event is not a TaskEvent, call default handling.
     */
    @Override
    public boolean handle( Event event ) {
        
        if( !(event instanceof TaskEvent )) {
            return super.handle( event );
        } 
      
        Gdx.app.log( "MonsterListener", "Handling TaskEvent " + event );
        
        TaskSprite ts = ((TaskEvent)event).getTaskSprite();
         
        this.parentScreen.setOpenMenu( new TaskDetailsMenu( ts,  this.parentScreen.getAssetManager()));
  
        ((OverviewScreen)this.parentScreen).setFocusTask( ts ); 
     
        
        return true;
    }
}

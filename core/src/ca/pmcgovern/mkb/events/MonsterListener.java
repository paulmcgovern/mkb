package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.actions.ClearMonsterDialogAction;
import ca.pmcgovern.mkb.menus.FuckSox;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.OverviewScreen;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.newtask.TaskForm;
import ca.pmcgovern.mkb.screens.MkbScreen.ScreenId;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.ui.TaskPicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MonsterListener extends ClickListener {

    public static final String TAG = "MonsterListener";
    private MkbScreen parentScreen;
    private Skin skin;
    private EffectManager effectMgr;
    private TaskSpriteManager taskManager;
    public static final String MAIN_MENU = "main";
    
    public MonsterListener( TaskSpriteManager taskManager, MkbScreen parentScreen, Skin skin, EffectManager effectMgr ){
        this.parentScreen = parentScreen;
        this.skin = skin;   
        this.effectMgr = effectMgr;
        this.taskManager = taskManager;
    }
    
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        
        Actor monster = event.getListenerActor();
        monster.addAction( Actions.scaleBy( -0.15f, -0.15f, 0.25f, Interpolation.exp10 )); 
                  
        return true;
    }

    
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          
        this.effectMgr.playClick();
        Actor monster = event.getListenerActor();
        monster.addAction( Actions.scaleBy( 0.15f, 0.15f, 0.5f, new Interpolation.ElasticOut( 2, 5, 1, 1 )));
                
        // TODO: disable events in parent screen task stage?
        
        Stage stage = monster.getStage();
        Group root = stage.getRoot();
       
        
       // Gdx.app.log( TAG, "Touch up event:" + event );
        
                
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
     
        
        
        MkbMenu win = null;//(MkbMenu)root.findActor( "settings" );
        
      //  if( win == null ) {
       //     win = (MkbMenu)root.findActor( "help" );            
      //  }
        
        if( win == null ) {
            win = (MkbMenu)root.findActor( MAIN_MENU );    
        }
        
        if( win == null ) {
            win = (MkbMenu)root.findActor( "task_details" );
        }
        
        if( win == null ) {
        
            Gdx.app.log( TAG, "No open window found.");    
       
            int taskCount = this.taskManager.getTaskCount();
            int completedCount = this.taskManager.getTaskCompletedCount();
            String activeTaskDescr = this.taskManager.getActiveTaskDescription();
            
            
            win = new FuckSox( taskCount, completedCount, activeTaskDescr, this.parentScreen.getAssetManager(), this.effectMgr, new WinChangeListener() );
            win.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn( 0.3f)));
            win.setX( stage.getWidth() / 6 );
            win.setY( stage.getWidth() / 6 );
            win.setName( MAIN_MENU );
         
           this.parentScreen.setOpenMenu( win );
       
        } else {
            
            Gdx.app.log( TAG, "Found open window " + win.getName() );
            // TODO: save state
            // TODO: fade-in new screen
            
            // Remove the window from the parent screen.
         //   win.addAction( Actions.sequence(  Actions.fadeOut( 0.3f ),/* Actions.removeActor() , */ new ClearMonsterDialogAction( this.parentScreen ) ));           
            
           this.parentScreen.clearOpenMenu();
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
        EditTaskListener editListener = new EditTaskListener( ts );
      
        TaskDetailsMenu detailsMenu =  new TaskDetailsMenu( ts,  this.parentScreen.getAssetManager(), this.effectMgr, editListener );
        detailsMenu.addListener( editListener );
        
        this.parentScreen.setOpenMenu( detailsMenu );//new TaskDetailsMenu( ts,  this.parentScreen.getAssetManager(), this.effectMgr ));
        
        ((OverviewScreen)this.parentScreen).setFocusTask( ts ); 
     
        
        return true;
    }
    
    class EditTaskListener extends ChangeListener {
   
        private TaskSprite ts;
        
        public EditTaskListener( TaskSprite ts ) {
            
          
            this.ts = ts;
        }
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            
         System.err.println( "ML: editTaskListener" + actor );

            //    if( !(actor instanceof TaskSprite )) {
              //      return;
               // }
         
                AssetManager assetMgr =  MonsterListener.this.parentScreen.getAssetManager();
        
             //  ((OverviewScreen)MonsterListener.this.parentScreen).getFocusTask();
     
                TaskForm form = new TaskForm( assetMgr, MonsterListener.this.effectMgr );
                form.populate( this.ts );
                form.addListener(new TaskSavedListener() );
        
                MonsterListener.this.parentScreen.setOpenMenu( form );
        }        
        
    }
    
    
    class WinChangeListener extends ChangeListener {
        
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
           
            String name = actor.getName();
            
            if( name == null ) {
                return;
            }
            
            if( "add".equals( name )) {
               // MonsterListener.this.parentScreen.setNextScreenId( ScreenId.NEW_SCREEN );
  //               this.parentScreen.setOpenMenu( new TaskDetailsMenu( ts,  this.parentScreen.getAssetManager(), this.effectMgr ));
      //          actor.getParent()
                AssetManager assetMgr =  MonsterListener.this.parentScreen.getAssetManager();
        
                TaskForm form = new TaskForm( assetMgr,  MonsterListener.this.effectMgr );
                form.addListener(new TaskSavedListener() );
                
                MonsterListener.this.parentScreen.setOpenMenu( form );
            //   MonsterListener.this.
            } else if( "settings".equals( name )) {
                MonsterListener.this.parentScreen.setNextScreenId( ScreenId.SETTINGS );                
            } else if( "help".equals( name )) {             
                MonsterListener.this.parentScreen.setNextScreenId( ScreenId.HELP );                
            }
            
            // The screen transition will "close" this open menu.
        }
    } 
    
    class TaskSavedListener implements EventListener {

        @Override
        public boolean handle( Event event ) {

            if( event instanceof TaskSavedEvent ) {                 
                event.getStage().getRoot().findActor("monster").fire( event );         
               return true;
            }
            
            return false;
        }
    }
}
 
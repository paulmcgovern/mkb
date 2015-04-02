package ca.pmcgovern.mkb.menus;


/**
 * http://meatcat.info/wordpress/?p=24
 * @author mcgovern
 *
 */

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.screens.MkbScreen.ScreenId;
import ca.pmcgovern.mkb.sprites.TaskSprite;
import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.ui.Task.TaskState;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

    public class TaskDetailsMenu extends BaseTable {

     
        
       // start done change delete
        
        public TaskDetailsMenu( TaskSprite taskSprite, AssetManager assetMgr ) {            
            
            super(assetMgr);  
            
            if( taskSprite == null ) {
                throw new IllegalArgumentException( "TaskSprite is null" );
            }
            
            Task task = taskSprite.getTask();
            
            int taskId = task.getId();
            
            if( taskId < 1 ) {
                throw new IllegalArgumentException( "Bad task ID " + task );
            }
            
            Skin skin = assetMgr.get( "data/icons.json", Skin.class );
            
            
           
            
            
            Table menuTable = new Table();
            
          
            
            // TODO: wrap title if over certain lenth
            String taskDesc = task.getDescription();
            
            if( taskDesc.length() > 20 ) {
                // TODO: WRAP TEXT
            }
            
            menuTable.add( new Label( taskDesc, skin, "tiny" )).colspan( 2 );
            menuTable.row();   
           
            menuTable.add( new Label( task.getState().toString(), skin, "tiny" )).colspan( 2 );   
            menuTable.row();  
            
            Table buttonTable = new Table();
            
            // State change buttons depend on current state
            ImageTextButton[] stateChageButtons = getStateChangeButtons( task.getState(), skin);
            
            
            ImageTextButton itb = null;
            
            StateChangeButtonListener stateButtonListener = new StateChangeButtonListener( taskSprite );
            
            for( int i = 0; i < stateChageButtons.length; i++ ) {
                itb = stateChageButtons[ i ];
                MkbScreen.layoutButton( itb );  
                itb.addListener( stateButtonListener );
                buttonTable.add( itb ).padLeft( 10 );
            }
            
     
            itb = new ImageTextButton( "Change", skin, "detail-edit" );
            MkbScreen.layoutButton( itb );        

            itb.addListener( new WinChangeListener( MkbScreen.ScreenId.NEW_SCREEN, taskId ) );        
            buttonTable.add( itb ).padLeft( 10 );
            
            
            itb = new ImageTextButton( "Delete", skin, "detail-delete" );
            MkbScreen.layoutButton( itb );        

            ConfirmWinChangeListener c = new ConfirmWinChangeListener( assetMgr, taskSprite );
            itb.addListener( c );        
            buttonTable.add( itb ).padLeft( 10 );
            
            
          //  itb = new ImageTextButton( "Settings",   skin, "main-tools" );
          //  itb.addListener( new WinChangeListener( MkbScreen.ScreenId.SETTINGS ) );   
          //  MkbScreen.layoutButton( itb );
           // buttonTable.add( itb ).padLeft( 10 );
            
           // itb = new ImageTextButton( "Help",   skin, "main-info" );
          //  itb.addListener( new WinChangeListener( MkbScreen.ScreenId.HELP ) );        
            
          //  MkbScreen.layoutButton( itb );
           // buttonTable.add( itb ).padLeft( 10 );
            
            menuTable.add( buttonTable ).colspan( 2 );  
            
         
            setMenu( menuTable );
            
            // TODO Auto-generated constructor stub
        }


        
        @Override
        public String getName() {        
            return MkbMenu.MenuType.TASK_DETAILS.name().toLowerCase(); 
        }
            
        /**
         * Build array of buttons to handle state changes.
         * @param state
         * @param skin
         * @return
         */
        private ImageTextButton[] getStateChangeButtons( TaskState state, Skin skin ) {
            
            TaskState[] nextStates = Task.STATE_TRANSITIONS.get( state );
            
            if( nextStates == null || nextStates.length == 0 ) {
                nextStates = Task.STATE_TRANSITIONS.get( TaskState.NEW );
            }
            
            
            ImageTextButton allButtons[] = new ImageTextButton[ nextStates.length ];
            
            for( int i = 0; i < allButtons.length; i++ ) {
                
                TaskState nextState = nextStates[ i ];
           
                String label;
                String style;
                
                switch( nextState ) {
            
                case COMPLETED:
                    label = "Finish";
                    style= "detail-finish";                    
                    break;               
                case IN_PROGRESS:
                    label = "Start";
                    style= "detail-start";    
                    break;         
                case STOPPED:
                    label = "Stop";
                    style= "detail-stop";  
                    break;
                default:
                    label = "Start";
                    style= "detail-start";  
                    break;
                }                
                
                ImageTextButton stateChangeButton = new ImageTextButton( label, skin, style );
                stateChangeButton.setName( nextState.name() );
                allButtons[ i ] = stateChangeButton;
            }
 
            return allButtons;
        }
      
       
        /**
         * Change task state based on which button was clicked
         * @author mcgovern
         *
         */
        class StateChangeButtonListener extends ChangeListener {

            private TaskSprite ts;
            
            public StateChangeButtonListener( TaskSprite ts ) {
                this.ts = ts;
            }
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            
                TaskState newState = TaskState.valueOf( actor.getName() );
               
                this.ts.getTask().setState( newState );
               
                TaskDetailsMenu.this.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new RemoveActorAction()));
                
                ScreenManager scrMgr = ScreenManager.getInstance();
                scrMgr.clearOpenMenu( ScreenId.OVERVIEW_SCREEN );
             
                TaskManager mgr = TaskManager.getInstance();                
                mgr.save( ts ); 
            }
        }
        
        
        
        
        class WinChangeListener extends ChangeListener {

            private MkbScreen.ScreenId nextScreen;
            private int taskId;
                        
            public WinChangeListener( MkbScreen.ScreenId nextScreenId, int taskId ) {
                this.nextScreen = nextScreenId;
                this.taskId = taskId;
            }
            
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                            
                // Not necessary to set/clear window on
                // the parent Screen because we're done with it.
           //     MainWindow.this.addAction( Actions.sequence( Actions.fadeOut( 0.5f ), new RemoveAction()));      
                
                TaskManager mgr = TaskManager.getInstance();
                mgr.setEditTargetId( this.taskId );                
                ScreenManager.getInstance().showScreen( this.nextScreen );
            }
        } 
        
        
        class ConfirmWinChangeListener extends ChangeListener {
            private AssetManager assetMgr;
            private TaskSprite ts;
            public ConfirmWinChangeListener( AssetManager assetMgr, TaskSprite ts ) {
                this.assetMgr = assetMgr;
                this.ts = ts;
            }
            
            
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                
               Window confirm = new ConfirmWindow( "Confirm", assetMgr, this.ts  );
               Stage uiStage = TaskDetailsMenu.this.getStage();
               
               confirm.addAction(Actions.sequence( Actions.alpha(0), Actions.fadeIn( 0.3f)));
               uiStage.addActor( confirm );
       
               confirm.setX( (uiStage.getWidth() - confirm.getWidth()) / 2 );
               confirm.setY( (uiStage.getHeight() - confirm.getHeight()) / (float)1.5 );
               
               // Remove this menu and pass control to the confirmation dialog.
               // The confirm dialog will clear the spotlight
               TaskDetailsMenu.this.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new RemoveActorAction()));
               
            }
            
            
      
        }
}
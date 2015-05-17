package ca.pmcgovern.mkb.menus;


import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.fwt.Task;
import ca.pmcgovern.mkb.fwt.Task.TaskState;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

    public class TaskDetailsMenu extends BaseTable {
    public static final String EDIT = "EDIT";
    public static final String DELETE = "DELETE";

        
        public TaskDetailsMenu( TaskSprite taskSprite, AssetManager assetMgr, ChangeListener editButtonListener ) {            
            
            super(assetMgr);  
            
            if( taskSprite == null ) {
                throw new IllegalArgumentException( "TaskSprite is null" );
            }
                        
       
    
            Task task = taskSprite.getTask();
            
            int taskId = taskSprite.getTaskId();
            
            if( taskId < 1 ) {
                throw new IllegalArgumentException( "Bad task ID " + taskSprite );
            }
            
            Skin skin = assetMgr.get( "data/icons.json", Skin.class );
            
           
            Table menuTable = new Table();
            
          
            
            // TODO: wrap title if over certain lenth
            String taskDesc = taskSprite.getTaskDescription();
            
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
            
            CloseListener closer = new CloseListener();
            
            ImageTextButton itb = null;
            
             
            for( int i = 0; i < stateChageButtons.length; i++ ) {
                itb = stateChageButtons[ i ];
                MkbScreen.layoutButton( itb );  
              
                itb.addListener( editButtonListener );
                itb.addListener( closer );
                buttonTable.add( itb ).padLeft( 10 );
            }
            
            
            itb = new ImageTextButton( "Change", skin, "detail-edit" );
            MkbScreen.layoutButton( itb );      
            itb.setName( EDIT );
            itb.addListener( editButtonListener );
  
            itb.addListener( closer );
            
            buttonTable.add( itb ).padLeft( 10 );
            
            
            itb = new ImageTextButton( "Delete", skin, "detail-delete" );
            itb.setName( DELETE );
            MkbScreen.layoutButton( itb );        
            itb.addListener( editButtonListener );
            itb.addListener( closer );
            
            buttonTable.add( itb ).padLeft( 10 );
                        
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
            
            TaskState[] nextStates = TaskSpriteManager.getNextStates( state );
            
            
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
    /**    class StateChangeButtonListener extends ChangeListener {

            private TaskSprite ts;
            
            public StateChangeButtonListener( TaskSprite ts ) {
                this.ts = ts;
            }
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            
                TaskDetailsMenu.this.effectMgr.playClick();
                
                TaskState newState = TaskState.valueOf( actor.getName() );// name of button
               
             //   if( TaskState.COMPLETED.equals( newState )) {
             //       TaskDetailsMenu.this.effectMgr.startDoneEffect( this.ts );
             //   }
                
                // Mark task as needing saving.                
                this.ts.getTask().setState( newState );
                this.ts.setDirty();
                
                               
            //    TaskDetailsMenu.this.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new RemoveActorAction()));
                
                ScreenManager scrMgr = ScreenManager.getInstance();
                scrMgr.clearOpenMenu( ScreenId.OVERVIEW_SCREEN );
             
                // TaskSpriteManager mgr = new TaskSpriteManager()TaskManager.getInstance();    
                // TaskSpriteManager mgr = new TaskSpriteManager( this.);
                // mgr.save( ts ); 
            }
        }
        
        
      
        /**
        class WinChangeListener extends ChangeListener {

            private MkbScreen.ScreenId nextScreen;
            private int taskId;
                        
            public WinChangeListener( MkbScreen.ScreenId nextScreenId, int taskId ) {
                this.nextScreen = nextScreenId;
                this.taskId = taskId;
            }
            
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              
                TaskDetailsMenu.this.effectMgr.playClick();
                // Not necessary to set/clear window on
                // the parent Screen because we're done with it.
           //     MainWindow.this.addAction( Actions.sequence( Actions.fadeOut( 0.5f ), new RemoveAction()));      
                
              //  TaskManager mgr = TaskManager.getInstance();
                GameMain.editTargetId = this.taskId;                
                ScreenManager.getInstance().showScreen( this.nextScreen );
            }
        } 
    */
        
        
    class CloseListener extends ChangeListener {
            
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            TaskDetailsMenu.this.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ),Actions.removeActor()));
        }
    }        
}

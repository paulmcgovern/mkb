package ca.pmcgovern.mkb.ui;


//import static ca.pmcgovern.mkb.ui.Task.ICON_NAMES;
import ca.pmcgovern.mkb.actions.ScrollPaneCenterAction;
import ca.pmcgovern.mkb.sprites.TaskSprite;
import ca.pmcgovern.mkb.sprites.TaskSprite.DrawState;
import ca.pmcgovern.mkb.sprites.TaskSpriteFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class TaskPicker extends Table {

    private TaskSprite selectedTask;
    
    private Table iconTable;    
    private ScrollPane selectScroll;
    
    private static final String TAG = "TaskPicker";
    
    Image callSelected;
    
    public TaskPicker( Skin skin, TaskSpriteFactory taskFactory, float btnW, float targWidth ) {
        
        super( skin );
        
        if( taskFactory == null ) {
            throw new IllegalArgumentException( "Null task factory." );
        }
              
        // The table requires spacers on the 
        // left and right to allow a selected
        // item to be centered.
       
        this.iconTable = new Table();
        
        float padding = btnW / 6;
     
        // Expand spacing end cell  
        float endPadding = (targWidth / 2) - (btnW *2) + (padding ); 
        
        this.iconTable.add().width( endPadding ).expandX();

        
        ClickListener iconListener = new IconClickListener();
        
        Task.Type[] allTypes = Task.Type.values();
        
        for( int i = 0; i < allTypes.length; i++ ) {
            
            Task.Type t = allTypes[ i ];
                      
            TaskSprite taskSprt = taskFactory.getTask( t  );
            taskSprt.addListener( iconListener );
                 
            this.iconTable.add( taskSprt ).width( btnW ).height( btnW ).pad( 0, padding, 0, padding );
        }
              
        // Expand spacing end cell       
        this.iconTable.add().width( endPadding ).expandX();
             
        this.selectScroll = new ScrollPane( this.iconTable );
        this.selectScroll.setName( "taskScroll" );
        this.selectScroll.setScrollingDisabled( false,  true );        
     
        this.add( this.selectScroll );
    }
    
  
    public TaskSprite getSelectedTask() {       
        return this.selectedTask;
    }
    
    
    public void setCurrentTask( Task t ) {
        
        SnapshotArray<? extends Actor> allIcons = TaskPicker.this.iconTable.getChildren();
        int count = allIcons.size;
        int targetIdx = -1;
        
        Task.Type targetType = t.getType();
        
        for( int i = 0; i < count; i++ ) {
                           
            TaskSprite icon = (TaskSprite)allIcons.get( i );
      
            if( targetType == icon.getType() ) {
                
                // Set icon colour
                icon.setTaskColor( t.getColour() );
                this.selectedTask = icon;               
                this.selectedTask.setTaskColor( t.getColour() );
                this.selectedTask.setState( DrawState.TASK_PICKER_SELECTED );
                this.selectedTask.setTaskDescription( t.getDescription() );
                
                targetIdx = i;
                break;
            }
        }
        
        
        
        
        Gdx.app.log( TAG,  "Targ icon index " + targetIdx );
        if( targetIdx < 0 ) {
            throw new IllegalArgumentException( "Unable to locate task type " + targetType.name() );
        }
      
        
    }    
    
    public void setSelectedColor( Task.IconColor newColor ) {
    	
        if( this.selectedTask == null ) {
            return;
        }
        
        this.selectedTask.setTaskColor( newColor );
    }
    
    
    
    class IconClickListener extends ClickListener {
        
        public void clicked( InputEvent event, float x, float y ) {
       
            TaskSprite taskIcon = (TaskSprite)event.getListenerActor();
           
            if( taskIcon.equals( TaskPicker.this.selectedTask )) {
                return;
            }

            
            if( TaskPicker.this.selectedTask != null ) {             
                taskIcon.setTaskColor( TaskPicker.this.selectedTask.getTaskColor() );
            } else {            
                taskIcon.setTaskColor( Task.IconColor.WHITE );
            }
            
            taskIcon.setState( DrawState.TASK_PICKER_SELECTED );
           
          
            SnapshotArray<? extends Actor> allIcons = TaskPicker.this.iconTable.getChildren();
            int count = allIcons.size;
            
            for( int i = 0; i < count; i++ ) {
                               
                TaskSprite a = (TaskSprite)allIcons.get( i );
                
                if( taskIcon.equals( a )) {              
                    continue;                    
                }  

                a.setTaskColor( Task.IconColor.NONE );
                a.setState( DrawState.TASK_PICKER_UNSELECTED );  

            }
             
     
            TaskPicker.this.selectedTask = taskIcon;
           
            TaskPicker.this.selectScroll.addAction( new ScrollPaneCenterAction( TaskPicker.this.selectScroll, taskIcon ));
            
            TaskPicker.this.fire( new PickedEvent() );
        }
    } 
    
 
    
    
    
    /**
     * Indicates a new task has been selected.
     * @author mcgovern
     *
     */
    public class PickedEvent extends Event {
        
        public PickedEvent() {
            this.setBubbles(true);
        }
        
    }


    public void setTaskDescription( String descr ) {
    
        if( this.selectedTask == null ) {
            return;
        }
        
        if( descr == null ) {
            this.selectedTask.setTaskDescription( "" );
        } else {
            this.selectedTask.setTaskDescription( descr );
        }        
    }
}

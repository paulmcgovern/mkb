/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.fwt;

import ca.pmcgovern.mkb.fwt.Task.IconColor;
import ca.pmcgovern.mkb.util.EmptyQuadrantFinder;
import java.util.HashMap;
import java.util.Map;
import static  ca.pmcgovern.mkb.fwt.Task.TaskState;
import ca.pmcgovern.mkb.fwt.Task.Type;
import static ca.pmcgovern.mkb.sprites.TaskSprite.FADE_DURATION;
import static ca.pmcgovern.mkb.sprites.TaskSprite.UNSELECTED_ALPHA;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author mcgovern
 */
public class TaskSpriteManager {

    public static final String TAG = "TaskSpriteManager";
    
    public static final Map<TaskState, TaskState[]> STATE_TRANSITIONS = new HashMap<Task.TaskState, TaskState[]>();
    
    static {
        STATE_TRANSITIONS.put( TaskState.NEW,         new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.IN_PROGRESS, new TaskState[]{ TaskState.STOPPED, Task.TaskState.COMPLETED } );
        STATE_TRANSITIONS.put( TaskState.STOPPED,     new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.COMPLETED,   new Task.TaskState[]{ Task.TaskState.IN_PROGRESS } );       
    }
       
     private Label.LabelStyle taskLabelDflt;
  
   
      public static final float FADE_DURATION = 0.25f;
	  
	  public static final float UNSELECTED_ALPHA= 0.25f;
	
    public enum DrawContext { TASK_PICKER, TASK_PICKER_UNSELECTED, TASK_PICKER_SELECTED, OVERVIEW }
    
   // DrawContext context;
    
   EmptyQuadrantFinder spotFinder;
    TaskStore taskDb;
    AssetManager assetMgr;
    Texture doneTexture;
            TaskDrawableFactory drawables ;
            
    public TaskSpriteManager( AssetManager assetMgr) {
        this.taskDb = TaskStore.getInstance();
        this.spotFinder = new EmptyQuadrantFinder( 120 );
        this.assetMgr = assetMgr;
        this.doneTexture = new Texture(Gdx.files.internal("data/done.png"));
   
        Skin skin = this.assetMgr.get( "data/icons.json", Skin.class );        
     
        this.taskLabelDflt = skin.get( "default", Label.LabelStyle.class );
        this.drawables = new TaskDrawableFactory( this.assetMgr.get("data/tasks.atlas", TextureAtlas.class ));

    }
  
    /** task with no label or color **/
    public TaskSprite getTask( Task.Type type ) {
   
        Task t = new Task();
        t.setType( type );
         
        TaskSprite ts = new TaskSprite( t );
        
        TextureRegionDrawable icon = drawables.getDrawable(t.type, DrawContext.TASK_PICKER_UNSELECTED, Task.IconColor.NONE );
        ts.setDrawable( icon );
        ts.setLabel( buildLabel( t )); 
            
        ts.setWidth( icon.getRegion().getRegionWidth() );
        ts.setHeight( icon.getRegion().getRegionHeight() );
      
        return ts;   
    }    
   
    
   
   
   
   
    public static TaskState[] getNextStates( TaskState currentState ) {
            
        TaskState[] nextStates = STATE_TRANSITIONS.get( currentState );
            
        if( nextStates == null || nextStates.length == 0 ) {
            nextStates = STATE_TRANSITIONS.get( TaskState.NEW );
        }
        
        return nextStates;   
    }
    
   
    public void init( TaskSprite ts, DrawContext context ) {
    
            if( ts == null ) {
                Gdx.app.log( TAG, "Attempt to initialize null TaskSprite" );
                return;
            }
                       
            
            Task t = ts.getTask();
            
            if( t == null ) {
                Gdx.app.log( TAG, "Attempt to initialize TaskSprite with null Task" );
                return;                
            }
            
            ts.pack();//TODO: required?          
            TextureRegionDrawable icon = drawables.getDrawable(t.type, context, t.colour );
            ts.setDrawable( icon );
            ts.setLabel( buildLabel( ts.getTask() )); 
            
            ts.setWidth( icon.getRegion().getRegionWidth() );
            ts.setHeight( icon.getRegion().getRegionHeight() );
            
            setState( ts, ts.task.state, context );
                    
    }
    
    
    public List<TaskSprite> init( Rectangle extents, DrawContext context ) {
        
        List<Task> allTasks = this.taskDb.getAllTasks();
        
        if( allTasks == null || allTasks.isEmpty() ) {
            return new ArrayList<TaskSprite>();
        }
        
        int count = allTasks.size();
        
        // Make sure all tasks will fit in extents.
        updatePositionsToFit(allTasks, extents);
        
        
        List<TaskSprite> allTaskSprites = new ArrayList<TaskSprite>();
        
        
        for( int i = 0; i < count; i++ ) {
            
            Task t = allTasks.get( i );
            
            if( t.getState() == null ) {
                t.setState( TaskState.NEW );
            }
            TaskSprite ts = new TaskSprite( t );
           
            ts.pack();//TODO: required?
            ts.setPosition( t.posX, t.posY );
            TextureRegionDrawable icon = drawables.getDrawable(t.type, context, t.colour );
            ts.setDrawable( icon );
            ts.setLabel( buildLabel( t )); 
            
            ts.setWidth( icon.getRegion().getRegionWidth() );
            ts.setHeight( icon.getRegion().getRegionHeight() );
            
            if( ts.task.state == TaskState.COMPLETED ) {
                ts.done = new Image( this.doneTexture ); 
                ts.done.setPosition( ts.getX(), ts.getY() + ts.getHeight() / 2);
            }
            
            
            allTaskSprites.add( ts );
        }
      
        return allTaskSprites;
    }
    
    public void setState( TaskSprite ts, TaskState newState, DrawContext context ) {
    
        if( context == DrawContext.OVERVIEW ) {
            setOverviewState( newState, ts );
        } else {
            ts.setState( newState );
        }
    }
    
   
    
    public void setOverviewState( TaskState newState, TaskSprite ts ) {
        
    	 //   this.changeDrawable();
       // setDrawable( this.drawables.getDrawable( this.task.getType(), this.state, this.task.getColour() ));
        System.err.println( "Setting overview state: " + newState );          	
        
        boolean clearDone = true;
        
        switch( newState ) {

            
            case DELETED:
    
                ts.setState( TaskState.DELETED );            
                ts.addAction( Actions.sequence( /*Actions.fadeOut( FADE_DURATION ),*/ Actions.removeActor() ));
   
                break;
            
            case COMPLETED:
       
                clearDone = false;
                
                if( ts.done == null ) {
                    ts.done = new Image( this.doneTexture );
                //    ts.getStage().addActor( ts.done );            
                }
                       
                ts.done.addAction( Actions.sequence( Actions.alpha(0), Actions.delay(FADE_DURATION), Actions.alpha( 1, FADE_DURATION)));
                ts.done.setPosition( ts.getX(), ts.getY() + ts.getHeight() / 2);
                   System.err.println( "Done at: " + ts.done.getX() + " " + ts.done.getY() );
                break;
                
        case IN_PROGRESS:
              
            break;
            
        case STOPPED:
           
            break;
        
        case NEW:
            break;
            
        default:
            throw new IllegalArgumentException( "Unknown state:" + newState );
          
            
        }    
         
        if( clearDone && ts.done != null ) {
            ts.done.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() ));
            ts.done = null;
        }
        
                
    }
     // this.taskDb = TaskStore.getInstance();
    public void setDrawcontext( DrawContext newContext , TaskSprite ts ) {
       
        if( newContext == DrawContext.TASK_PICKER_UNSELECTED ) {
            ts.addAction( Actions.alpha( UNSELECTED_ALPHA, FADE_DURATION ));             
        } else {
            ts.addAction( Actions.alpha( 1, FADE_DURATION ));        
        }
    }
    
    
    public void setTaskColor( IconColor newColor, TaskSprite ts ) {
      
        if( newColor == null ) {
            throw new IllegalArgumentException( "new color is null" );
        }
        
        if( ts == null ) {
            throw new IllegalArgumentException( "TaskSprite is null");
        }
        
        ts.task.setColour( newColor );
        
        Drawable d = this.drawables.getDrawable(  ts.task.type, DrawContext.TASK_PICKER, newColor );
       
        if( d == null ) {
            throw new IllegalArgumentException( "No icon found for " +  ts.task.type + " " + DrawContext.TASK_PICKER + " " + newColor );
        }
        
    	ts.setDrawable( d );    
    }
    
   protected Label buildLabel( Task task ) {
        
      //  System.err.println( "##Zoom" + zoom );
   /**
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal( "data/Days.ttf" ));
        
        FileHandle f = Gdx.files.internal( "data/Days.ttf" );
        System.err.println("XX:"+ f.exists() );
        System.err.println( "XX:" + f.length() );
        FreeTypeFontParameter singleUseFontParams = new FreeTypeFontParameter();   
        singleUseFontParams.size = (int)Math.floor( 30 / zoom );

        LabelStyle scaledFontStyle = new LabelStyle( this.taskLabelDflt );
        
        scaledFontStyle.font = generator.generateFont( singleUseFontParams );
        
        Label taskLabel = new Label( task.getDescription(), scaledFontStyle );
        
        generator.dispose();
        **/
        
        Label taskLabel = new Label( task.description, this.taskLabelDflt );// scaledFontStyle );
            System.err.println( "Label: " + task.description);
        return taskLabel;
    }  
   
     public static boolean isComplete( TaskSprite ts ) {
        
        if( ts == null ) {
            return false;
        }
    
        Task t = ts.task;
        
        if( t == null ) {
            return false;
        }
        
        if( t.type == null ) {
            return false;
        }
        
        
        if( t.colour == null || t.colour == IconColor.NONE ) {
            return false;
        }
        
        
        if( t.description == null || t.description.isEmpty() ) {
            return false;
        }
        
        return true;        
    }
     
     
    public boolean delete( TaskSprite ts ) {
      
        if( ts == null ) {
            throw new IllegalArgumentException( "TaskSprite is null." );
        }
        
        Task t = ts.getTask();
        
        if( t == null ) {
            throw new IllegalArgumentException( "Task is null." );
        }
        ts.clearDirty();
        return this.taskDb.deleteTask( ts.getTask() );
    }
    
    public int getTaskCount() {
     
        return this.taskDb.getTaskCount();
    }
    
    public int getTaskCompletedCount() {
        return this.taskDb.getCompletedTaskCount();
    }
    
    public String getActiveTaskDescription() {
        return this.taskDb.getActiveTaskName();
    }
    
    public int save( TaskSprite ts ) {
        
        if( !isComplete( ts )) {
            throw new IllegalArgumentException( "Invalid/incomplete task." );
        }  
        
        Gdx.app.log( TAG, "Save TaskSprite " + ts );
        
        // Special case: only one task can be in 
        // progress at a time. If this task is now
        // IN_PROGRESS set any IN_PROGRESS task  
        // to STOPPED
        
        Task t = ts.getTask();
        
       // if( this.editTargetId >= 1 ) {
            
       //     t.setId( this.editTargetId );
            
           // clearEditTargetId();
       // }
        
        if( t.getState() == TaskState.IN_PROGRESS ) {
            
            List<Task> allTasks = this.taskDb.getAllTasks();
            
            if( allTasks != null ) {
                
                int count = allTasks.size();
                
                for( int i = 0; i < count; i++ ) {
                    
                    Task otherTask = allTasks.get( i );
                    
                  //  if( this.editTargetId == otherTask.getId()) {
                  //      continue;
                  //  }
                    
                    if( otherTask.getState() == TaskState.IN_PROGRESS ) {
                        otherTask.setState( TaskState.STOPPED );
                        this.taskDb.saveTask( otherTask );
                    }
                }
            }
        }
        
        ts.clearDirty();
        return this.taskDb.saveTask( ts.getTask() );
        
        
    }
      

     private int save( Task t ) {
       
        Gdx.app.log( TAG, "Save Task " + t );
     
        return this.taskDb.saveTask( t );
      
    }
      

      
    private void updatePositionsToFit( List<Task> allTasks, Rectangle extents ) {
    
        if( allTasks == null || allTasks.isEmpty() ) {
            return;
        }
        
        EmptyQuadrantFinder spotFinder = new EmptyQuadrantFinder( 120 );
        
        List<Vector2> spritePoints = new ArrayList<Vector2>();
       // System.err.println( "Extents: "  + extents);
        List<Task> modifiedTasks = new ArrayList<Task>();
        
        
      //  System.err.println( "Extents in: " + extents );
        // Make the extents containing the visible tasks
        // slightly smaller than the "real" extents.
        extents = new Rectangle( extents );
        extents.width = extents.width * 0.85f;
        extents.height = extents.height * 0.85f;
        extents.setCenter( 0,0 );
                
        System.err.println( "Adjusted: " + extents );
        
        int taskCount = allTasks.size();
        
        // Build Task sprites
        for( int i = 0; i < taskCount; i++ ) { 
          
            Task t = allTasks.get( i );
            
            if( t == null ) {
                Gdx.app.error(TAG, "Found null Task." );
                continue;
            }
            
         //   Gdx.app.log( TAG, "Init stage item:" + i + " " + allTasks.get( i ) );
        //    TaskSprite s =  taskFactory.getTask(allTasks.get( i ), initialZoom );
            
           
            
            Vector2 pos = new Vector2( t.posX, t.posY );
            System.err.println(extents.contains( pos ) + " " + pos );
            
            if( extents.contains( pos )) {
            //    stage.addActor( s );
                spritePoints.add( pos );
            } else {
                pos = spotFinder.find(spritePoints, extents);
                t.posX = pos.x;
                t.posY = pos.y;   /// setPosition(pos.x, pos.y );
                
                System.err.println( "Derived point: "+ pos );
                spritePoints.add( pos );
                modifiedTasks.add( t );
            }
        } 
        
        
        if( !modifiedTasks.isEmpty() ) {
            taskCount = modifiedTasks.size();
            for( int i = 0; i < taskCount; i++ ) {
                Task t = modifiedTasks.get( i );
                this.save(t);
            }
        }
    }
    
}

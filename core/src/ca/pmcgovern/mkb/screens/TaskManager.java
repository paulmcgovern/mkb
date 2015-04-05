package ca.pmcgovern.mkb.screens;

import java.util.List;

import ca.pmcgovern.mkb.events.TaskShowDetailListener;
import ca.pmcgovern.mkb.sprites.TaskSprite;
import ca.pmcgovern.mkb.sprites.TaskSpriteFactory;
import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.ui.Task.IconColor;
import ca.pmcgovern.mkb.ui.Task.TaskState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * Point of contact for all operations on Tasks
 * @author mcgovern
 *
 */
public class TaskManager {

  public static final String TAG = "TaskManager";
   
  
  private Task activeTask;
  
  
    private AssetManager assetManager;
  //  private TextureAtlas spriteAtlas;
  //  private List<TaskSprite> allTasks;
    private TaskShowDetailListener detailListener;
    private TaskLayout layoutMgr;
    private ClickListener clickListener;
    
 //   float prevZoom;
 //   private BitmapFont[] zoomFonts;
    
    private static TaskManager instance;
    
    private int editTargetId; 
    
    private TaskStore taskDb;   
    
    private TaskManager() {   
    	
    	this.taskDb = TaskStore.getInstance();   
    }
    
    public static synchronized TaskManager getInstance() {
     
        if( instance == null ) {
            instance = new TaskManager();
        }
                
        return instance;
    }
    
    
  /**  
    public TaskManager( AssetManager assetManager, TaskShowDetailListener detailListener, ClickListener clickListener ) {
        
        
      //  this.spriteAtlas = spriteAtlas;
        this.allTasks = new ArrayList<TaskSprite>();
        this.layoutMgr = new TaskLayout();
        this.detailListener = detailListener;
        this.clickListener = clickListener;
    }
    **/
    
    public static boolean isComplete( TaskSprite t ) {
        
        if( t == null ) {
            return false;
        }
    
        if( t.getType() == null ) {
            return false;
        }
        
        
        if( t.getTaskColor() == null || t.getTaskColor() == IconColor.NONE ) {
            return false;
        }
        
        
        if( t.getTaskDescription() == null || t.getTaskDescription().isEmpty() ) {
            return false;
        }
        
        return true;        
    }
    
    // TODO
    public boolean isDuplicate( Task t ) {
        return false;
    }
   
    
    public void clearEditTargetId() {
        
        Gdx.app.log( "TaskManager", "Clear edit target ID." );
        
        this.editTargetId = 0;
    }
    
    
    public void setEditTargetId( int id ) {
        
        Gdx.app.log( "TaskManager", "Set edit target: " + id +" " +this );
        
        if( id < 1 ) {
            throw new IllegalArgumentException( "Bad ID:" + id );
        }
        this.editTargetId = id;
    }
    
    
    public int getEditTargetId() {
        Gdx.app.log( "TaskManager", "Get edit target: " + this.editTargetId +" " +this );
        
        return this.editTargetId;
    }    
    
   
    
    public Task getTaskById( int id ) {
        
        if( id < 1 ) {
            throw new IllegalArgumentException( "Bad ID:" + id );
        }
        
        Task t = this.taskDb.getTask( id );
        
        if( t == null ) {
            throw new IllegalArgumentException( "No task found for ID " + id );
        }
        
        return t;       
    }
        
    
    public int save( TaskSprite ts ) {
        
        if( !TaskManager.isComplete( ts )) {
            throw new IllegalArgumentException( "Invalid/incomplete task." );
        }  
        
        Gdx.app.log( TAG, "Save TaskSprite " + ts );
        
        // Special case: only one task can be in 
        // progress at a time. If this task is now
        // IN_PROGRESS set any IN_PROGRESS task  
        // to STOPPED
        
        Task t = ts.getTask();
        
        if( this.editTargetId >= 1 ) {
            
            t.setId( this.editTargetId );
            
           // clearEditTargetId();
        }
        
        if( t.getState() == TaskState.IN_PROGRESS ) {
            
            List<Task> allTasks = this.taskDb.getAllTasks();
            
            if( allTasks != null ) {
                
                int count = allTasks.size();
                
                for( int i = 0; i < count; i++ ) {
                    
                    Task otherTask = allTasks.get( i );
                    
                    if( this.editTargetId == otherTask.getId()) {
                        continue;
                    }
                    
                    if( otherTask.getState() == TaskState.IN_PROGRESS ) {
                        otherTask.setState( TaskState.STOPPED );
                        this.taskDb.saveTask( otherTask );
                    }
                }
            }
        }
        
        return this.taskDb.saveTask( ts.getTask() );
    }
    
    
    public boolean delete( Task task ) {
        
        
        if( task == null ) {
            throw new IllegalArgumentException( "Task is null." );
        }
        
        this.taskDb.deleteTask( task );
        
        return true;
    }
    
    
    
    public void init( TaskSpriteFactory taskFactory, Stage stage, AssetManager assetMgr ) {
        
        float initialZoom = ((OrthographicCamera)stage.getCamera()).zoom;
        
        this.taskDb = TaskStore.getInstance();
                
        List<Task> allTasks = this.taskDb.getAllTasks();
        
        if( allTasks == null || allTasks.isEmpty() ) {
            Gdx.app.log( TAG, "Task count: 0" );            
        } else {
        int count = allTasks.size();        
        Gdx.app.log( TAG, "Task count: " + count );
        
        // Build Task sprites
        for( int i = 0; i < count; i++ ) { 
           
            Gdx.app.log( TAG, "Init stage item:" + i + " " + allTasks.get( i ) );
            TaskSprite s =  taskFactory.getTask(allTasks.get( i ), initialZoom );
       
            stage.addActor( s );
        } 
        }
    }
  
  
}

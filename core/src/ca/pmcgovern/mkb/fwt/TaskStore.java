package ca.pmcgovern.mkb.fwt;

import ca.pmcgovern.mkb.screens.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class TaskStore {

    // TODO: max tasks count
    
  //  private List<Task> allTasks;
    
    private static TaskStore instance;
    
    public static final String DATAFILE_PREFIX = "mkb-tsks-01";


    private Task mostRecent;
    
 //   private TaskStore() {        
  //      this.allTasks = new ArrayList<Task>();
  //  }
    
    public static synchronized TaskStore getInstance() {
     
        if( instance == null ) {
            instance = new TaskStore();
        }
                
        return instance;
    }
    
    
   
    private synchronized int getMaxId( List<Task> allTasks ) {
        
    
        if( allTasks == null || allTasks.isEmpty() ) {
            return 0;
        }
        
        int id = 0;
        
        for( Task t : allTasks ) {
            
            id = Math.max( id,  t.id );
        }
        
        if( id == 0 ) {
            throw new IllegalArgumentException( "Task IDs not set." );
        }
        
        return id;
    }
    
    /*
     * May return null
     */
    public Task getTask( int id ) {
               
        if( id < 1 ) {
            throw new IllegalArgumentException( "Bad ID:" + id );
        }
       
        List<Task> allTasks = this.getAllTasks();
        
        if( allTasks == null || allTasks.size() == 0 ) {
            return null;
        }
        
        Task targTask = null;
        
        for( Task t : allTasks ) {
            
           // TODO: t is null?
           if( t.id == id ) {
               targTask = t;
               break;
           }        
        }
       
       return targTask;
    }
    
    
    /**
     * If task ID is 0, generates and Id and saves. If not zero, does update.
     * Sets ID if 0
     * @param t
     * @return
     */
    public synchronized int saveTask( Task t ) {
       
        Gdx.app.log( "TaskStore", "Save task: " + t );
        
        List<Task> allTasks = this.getAllTasks();
        
        if( allTasks == null ) {
            allTasks = new ArrayList<Task>();
        }
        
        if( t.id < 1 ) {
            
            t.id = getMaxId( allTasks) + 1;
            
        } else {
        
            allTasks = this.getAllTasks();
            
            int count = allTasks.size();
            int targetId = t.id;
            int targetIdx = -1;
            
            for( int i = 0; i < count; i++ ) {
                if( allTasks.get( i ).id == targetId ) {
                    targetIdx = i;
                    break;
                }
            }
            
            if( targetIdx < 0 ) {
                throw new IllegalArgumentException( "No task found in store to update: " + t );
            }
            
            allTasks.remove( targetIdx );  // Remove existing       
            
            Gdx.app.log( "TaskStore", "Updated " + t );
        }
        
        allTasks.add( t );
        
        Gdx.app.log( "TaskStore", "Saved " + t + " " + allTasks.size() );

        this.mostRecent = t;
        
        try {
            
            Persistence p = Persistence.getInstance();
            
            long wrote = p.writeList( DATAFILE_PREFIX, allTasks);
            
            System.err.println( "WROTE: " + wrote  + " " + Gdx.files.getLocalStoragePath());
        } catch( Exception e ) { 
            e.printStackTrace(); }
        return t.id; 
    }
    

    
    public synchronized boolean deleteTask( Task targetTask ) {
        
        Gdx.app.log( "TaskStore", "Delete task: " + targetTask );
        
        List<Task> allTasks = this.getAllTasks();
        
        if( allTasks == null ) {
            allTasks = new ArrayList<Task>();
        }
        
        int targetId = targetTask.id;
        
        boolean foundIt = false;
        
        int count = allTasks.size();
        
        for( int i = 0; i < count; i++ ) {
            
            Task t = allTasks.get( i );
            
            if( t.id == targetId ) {
                allTasks.remove( i );
                foundIt = true;
                break;
            }
        }
         
       
        
        try {
            
            Persistence p = Persistence.getInstance();
       
            // Last one out, turn off the light.
            if( allTasks.size() == 0 ) {
             
                p.deleteFile( DATAFILE_PREFIX );
                
            } else {
            
                long wrote = p.writeList( DATAFILE_PREFIX, allTasks);
                
                System.err.println( "WROTE: " + wrote  + " " + Gdx.files.getLocalStoragePath());
            }
            
           
        } catch( Exception e ) { 
            e.printStackTrace(); 
        }
        return foundIt;
    }
        
    
    
    
    public List<Task> getAllTasks() {  
        
        List<Task> nard = null;
        
        try {
            Persistence p = Persistence.getInstance();
            nard = p.readList( DATAFILE_PREFIX );
         
        } catch( IOException e ) {
            e.printStackTrace();
        }
        
        if( nard == null ) {
            nard = new ArrayList<Task>();
        }
        
        return nard;
    }
    
    
    public int getTaskCount() {        
        
        int count = 0;
        
        try {
            
            Persistence p = Persistence.getInstance();
            List<Task> allTasks = p.readList( DATAFILE_PREFIX );
            
            if( allTasks == null ) {
                count = 0;
            } else {
                count = allTasks.size();
            }
            
        } catch( IOException e ) {
            e.printStackTrace();
        }
        
        return count;
    }
    
    
    public String getActiveTaskName() {
        
        List<Task> allTasks = this.getAllTasks();
        int taskCount = allTasks.size();
        String name = null;
        
        for( int i = 0; i < taskCount; i++ ) {
            
            Task t = allTasks.get( i );
            
            if( t == null ) {
                // TODO: log
                continue;
            }
            
            if( Task.TaskState.IN_PROGRESS == t.state ) {
                name = t.description;
            }            
        }
        
        return name;
    }
    
    
    
    public int getCompletedTaskCount() {
               
        Persistence p = Persistence.getInstance();
        
        if( !p.dataFileExists( DATAFILE_PREFIX )) {
            return 0;
        }
           
        List<Task> allTasks = null;
        
        try {
            allTasks = p.readList( DATAFILE_PREFIX );
        } catch( Exception e ) {
            e.printStackTrace();
          
        }
        
        if( allTasks == null || allTasks.isEmpty() ) {
            return 0;
        }
        
        
       int taskCount = allTasks.size();
       int completedCount = 0;
       
       for( int i = 0; i < taskCount; i++ ) {
           
           Task t = allTasks.get( i );
       System.err.println( "XXXX: " + t + " " + t.getState() )    ;
           if( t == null ) {
               
               // TODO: log
               continue;
           }
           
           if( Task.TaskState.COMPLETED == t.state ) {
               completedCount++;
           }
       }
       
       return completedCount;
    }
    
    
    public Task getMostRecentTask() {
        return this.mostRecent;
    }
    
   

    
    
}

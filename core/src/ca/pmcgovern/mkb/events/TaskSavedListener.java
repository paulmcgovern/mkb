/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.fwt.Task.TaskState;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.newtask.TaskForm;
import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.util.EmptyQuadrantFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mcgovern
 */
public class TaskSavedListener implements EventListener {

    public static final String TAG = "TaskSavedListener";
    
    private TaskSpriteManager taskMgr;
    private Rectangle extents;
    private Stage taskStage;
        
        public TaskSavedListener( TaskSpriteManager taskMgr, Rectangle extents, Stage taskStage ) {
            this.taskMgr = taskMgr;
            this.extents = extents;
            this.taskStage = taskStage;
        }
        
        
        @Override
        public boolean handle( Event event ) {

           
            if( !(event instanceof TaskSavedEvent )) { 
                return false;
            }
            
            Gdx.app.log(TAG, "Task saved: " + event );
             
            TaskSprite ts = ((TaskSavedEvent)event).getTaskSprite();
            
            if( ts == null ) {
                Gdx.app.error(TAG, "Attempt to save null task." );
                return true;
            }
            
            if( !TaskSpriteManager.isComplete( ts )) {
                Gdx.app.error(TAG, "Attempt to save incomplete task." );
                return true;                
            }
            
            
            
            TaskSavedEvent.Type type = ((TaskSavedEvent)event).getType();
          
            Gdx.app.log(TAG, "Task: " + ts  + " Update type: " + type );
          
            
            if( type == TaskSavedEvent.Type.EDIT ) {
            
                update( ts );
                
            } else {
                
                saveNew( ts );
            }
            
            return false;
        }

        private void saveNew( TaskSprite ts ) {
           
            List<Vector2> spritePoints = new ArrayList<Vector2>();
            
            Array<Actor> allSprites = this.taskStage.getActors();
            
            for( int i = 0; i < allSprites.size; i++ ) {
                Actor a = allSprites.get( i );
                
                if( a instanceof TaskSprite ) {
                    spritePoints.add( new Vector2( a.getX(), a.getY() ));
                }
            }
            // TODO: use curren zoom to calculate extents
            EmptyQuadrantFinder spotFinder = new EmptyQuadrantFinder( 120 );
            Vector2 newPoint = spotFinder.find( spritePoints, this.extents );
        
            ts.setPosition( newPoint.x, newPoint.y );

            //ts.setWidth( ts.getDrawable().getMinWidth() );
            //ts.setHeight( ts.getDrawable().getMinWidth() ); 
            ts.setState( TaskState.NEW );
            ts.setDirty();
            
            this.taskStage.addActor( ts );           
            //this.taskMgr.save(ts);
        }
        
        private void update( TaskSprite ts ) {
            
            System.err.println( "#####\n## TODO: update exsiting TaskSprite " + ts );
            this.taskMgr.save( ts );
        }
 
}  
    


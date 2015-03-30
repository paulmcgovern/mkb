package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.sprites.MonsterSprite;
import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;



public class TaskGestureListener extends ActorGestureListener {

    private Stage targetStage;
  
    public TaskGestureListener( Stage uiStage ) {
        
        super( 30.0f, 1.0f, 1.0f, 1.0f );
        
        if( uiStage == null ) {
            throw new IllegalArgumentException( "Target stage is null." );
        }
        
        this.targetStage = uiStage;
    }
    
    
    /** 
     * Fire an event at the Monster to open a Menu 
     */
    @Override        
    public void tap( InputEvent event, float x, float y, int count, int button ) {
    
        Actor a = event.getListenerActor();
        
        if( !(a instanceof TaskSprite)) {
            Gdx.app.log( "TaskGestureListener", "Event not from a TaskSprite: " + a );            
            return;
        }
        
        
        TaskEvent te = new TaskEvent( (TaskSprite)a, TaskEvent.Type.TASK_SHOW_DETAILS );
        
        Actor monster = this.targetStage.getRoot().findActor( MonsterSprite.MONSTER );
        
        if( monster == null ) {
            throw new NullPointerException( "Failed to find monster sprite in target stage." );
        }
        
        Gdx.app.log( "TaskGestureListener", "Sending event to Monster " + te );
        monster.fire( te );
        
    }
    
    
    
    
    @Override
    public void pan ( InputEvent event, float x, float y, float deltaX, float deltaY ) {
        
        Actor actor = event.getListenerActor();
        
        actor.setPosition( actor.getX() + deltaX, actor.getY() + deltaY );

            // TODO: check for collisions
            
    }
        
  //  @Override
  //  public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
  //      TaskSprite.this.task.setActive( true );
  //  }

  //  @Override
  //  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
  //      TaskSprite.this.task.setActive( false );
   // }   
}

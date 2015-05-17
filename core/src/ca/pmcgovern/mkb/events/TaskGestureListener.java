package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import ca.pmcgovern.mkb.screens.OverviewScreen;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.MonsterSprite;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;



public class TaskGestureListener extends ActorGestureListener {

    private Stage targetStage;
    private Rectangle stageExtents;
    private EffectManager effectMgr;
    
    public TaskGestureListener( Stage uiStage, Rectangle extents, EffectManager effectMgr ) {
        
        super( 30.0f, 1.0f, 1.0f, 1.0f );
        
        if( uiStage == null ) {
            throw new IllegalArgumentException( "Target stage is null." );
        }
        
        if( extents == null ) {
            throw new IllegalArgumentException( "Stage extens are null." );
        }
        
        if( effectMgr == null ) {
            throw new IllegalArgumentException( "Effect manager is null.");                    
        }
        
        this.targetStage = uiStage;
        this.stageExtents = extents;
        this.effectMgr = effectMgr;
    }
    
    
  /****
    @Override        
    public void tap( InputEvent event, float x, float y, int count, int button ) {
    
        Actor a = event.getListenerActor();
        
        if( !(a instanceof TaskSprite)) {
            Gdx.app.log( "TaskGestureListener", "Event not from a TaskSprite: " + a );            
            return;
        }
        
        
        TaskEvent te = new TaskEvent( (TaskSprite)a, TaskEvent.Type.TASK_SHOW_DETAILS );
        
        

        TaskDetailsMenu detailsMenu = new TaskDetailsMenu(ts, this.parentScreen.getAssetManager(), this.effectMgr, editListener);

        this.parentScreen.setOpenMenu(detailsMenu);//new TaskDetailsMenu( ts,  this.parentScreen.getAssetManager(), this.effectMgr ));

        ((OverviewScreen) this.parentScreen).setFocusTask(ts);
        
      //  Actor monster = this.targetStage.getRoot().findActor( MonsterSprite.MONSTER );
        
      //  if( monster == null ) {
      //      throw new NullPointerException( "Failed to find monster sprite in target stage." );
      //  }
        
        effectMgr.playClick();
        
      //  Gdx.app.log( "TaskGestureListener", "Sending event to Monster " + te );
      //  monster.fire( te );
        
        
    }
    
    ***/
    
    
    @Override
    public void pan ( InputEvent event, float x, float y, float deltaX, float deltaY ) {
        
        Actor actor = event.getListenerActor();
        float actorW = actor.getWidth();
        float actorH = actor.getHeight();
        
        Rectangle bound = new Rectangle( 0, 0, actor.getWidth(), actor.getHeight() );
        bound.setCenter( actor.getX() + deltaX + actorW/2, actor.getY() + deltaY + actorH/2 );
        
        // TODO: if task collides with side of viewport, move viewport to follow.
        if( this.stageExtents.contains( bound )) {
            
            boolean collisionFound = false;
            
            // Check collisions with 
            // other tasks.
            Stage taskStage = actor.getStage();
           
            Array<Actor> allActors = taskStage.getActors();
            
            Circle otherCircle = new Circle();
            Circle thisCircle = new Circle( actor.getX() + deltaX, actor.getY()+deltaY, 2 +actor.getWidth()/2 );
            
            for( int i = 0; i < allActors.size; i++ ) {
                
                Actor a = allActors.get( i );
                
                if( a == actor ) {
                    continue;
                }
                
                if( a instanceof TaskSprite ) {
                    otherCircle.set( a.getX(), a.getY(), a.getWidth()/2 );
                    if( thisCircle.overlaps(otherCircle)) {
                        collisionFound = true;                     
                        break;
                    }
                }
            }
            
            if( !collisionFound ) {
                actor.setPosition( actor.getX() + deltaX, actor.getY() + deltaY );
            } 
        }          
            
    }
        
  
}

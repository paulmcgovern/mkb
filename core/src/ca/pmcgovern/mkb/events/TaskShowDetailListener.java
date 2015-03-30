package ca.pmcgovern.mkb.events;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class TaskShowDetailListener extends ActorGestureListener {

    Stage targetStage;
    Skin skin;
    
    
    /**
     * Shape renderer is set as an argument in hopes it gets disposed of properly at a higher level. 
     * @param targetStage
     * @param skin
     * @param renderer
     */
    public TaskShowDetailListener( Stage targetStage, Skin skin ) {
        
        super( 30.0f, 1.0f, 1.0f, 1.0f );        
        this.targetStage = targetStage;
        this.skin = skin;       
    }
    
    

    
    @Override
    public boolean longPress( Actor actor, float x, float y ) {
                
        
        System.err.println( "Long press..." );
      //  TaskDetailsWindow details = new TaskDetailsWindow( (TaskSprite)actor, this.polyBatch, this.pointerPoly, skin );
      //  details.setVisible( true );
        
     //   Stage srcStage = actor.getStage();
      
     //   Vector2 srcPos = new Vector2( actor.getX(), actor.getY() );        
     //   Vector2 destPos = this.targetStage.screenToStageCoordinates( srcStage.stageToScreenCoordinates( srcPos ));
     
     //   details.setPosition( destPos.x, destPos.y );
     //   this.targetStage.addActor( details );
        
        return true;
    }
}

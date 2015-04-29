package ca.pmcgovern.mkb.sprites;


import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.fwt.Task.IconColor;
import ca.pmcgovern.mkb.ui.Task.TaskState;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.HashMap;
import java.util.Map;

public class TaskSprite extends Image {

	  public static final float FADE_DURATION = 0.25f;
	  
	  public static final float UNSELECTED_ALPHA= 0.25f;
	  
    public enum DrawState { TASK_PICKER, TASK_PICKER_UNSELECTED, TASK_PICKER_SELECTED, OVERVIEW, OVERVIEW_COMPLETE, DELETED }
    
    private Task task;
   
    private boolean hasMoved;
 
   /** Start life in the task picker. **/
   private DrawState state = DrawState.TASK_PICKER;
   
  
   private TaskDrawableFactory drawables;
   private Drawable d;
 private Image done;
 
   private Label label;
   
  public static final Map<TaskState, TaskState[]> STATE_TRANSITIONS = new HashMap<TaskState, TaskState[]>();
    
    static {
        STATE_TRANSITIONS.put( TaskState.NEW,         new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.IN_PROGRESS, new TaskState[]{ TaskState.STOPPED, TaskState.COMPLETED } );
        STATE_TRANSITIONS.put( TaskState.STOPPED,     new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.COMPLETED,   new TaskState[]{ TaskState.IN_PROGRESS } );       
    }
           
   
   public TaskSprite( Task task, TaskDrawableFactory drawables ) {
                
       super();// super( t );  
       this.drawables = drawables;
       this.task = task;    
     //  this.setDrawable( this.drawables.getDrawable( task.getType(), DrawState.TASK_PICKER, null )); //task.getColour() ));
         
       pack();
   }
   
   
   @Override
   public void draw( Batch batch, float parentAlpha ) {
           
       super.draw(batch, parentAlpha);
             
       if( this.label != null ) {       
           this.label.draw( batch,  parentAlpha );
       }       
     
       if( this.done != null ) {         
           this.done.draw(batch, parentAlpha );
       }       
   }
   
  /*
   @Override
   public void act( float delta ) {
       
       if( this.task.getState() == TaskState.COMPLETED && this.done == null ) {
           
           Image i = new Image( this.drawables.getDoneDrawable() );
           i.setPosition( this.getX(), this.getY() );
           this.getStage().addActor( i );
           this.done = i;
       
       }
   }
   */
   
   @Override
   public void positionChanged() {
       
       this.hasMoved = true;
       
       if( this.label != null ) {
           this.label.setPosition( this.getX(), this.getY() );
       }
       
        if( this.done != null ) {
           this.done.setPosition( this.getX(), this.getY() );
       }
   }
   
   
   public final boolean isHasMoved() {
    return hasMoved;
}


public void setLabel( Label label ) {
       
       this.label = label;
   }
 

   public void setState( DrawState newState ) {

        this.state = newState;
     boolean clearDoneIcon = false; 
	 //   this.changeDrawable();
    //    setDrawable( this.drawables.getDrawable( this.task.getType(), this.state, null )); //this.task.getColour() ));
        System.err.println( "Setting state: " + newState )     ;          	

        switch( newState ) {

        case TASK_PICKER:
            
            clearDoneIcon = true;
           
        //    setDrawable( this.taskPickerDrawable );
            break;
            
        case TASK_PICKER_SELECTED:
        	
        	addAction( Actions.alpha( 1, FADE_DURATION )); 
           clearDoneIcon = true;  
         //   setDrawable( this.selectedDrawable );
            break;
            
        case TASK_PICKER_UNSELECTED:
        	clearDoneIcon = true;
            addAction( Actions.alpha( UNSELECTED_ALPHA, FADE_DURATION )); 
        	break;
            
        case DELETED:
    
            this.task.setState( TaskState.DELETED );
            
            addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() ));
          
            if( this.done != null ) {
                this.done.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() ));
                this.done = null;
            }
            break;
            
        case OVERVIEW:
            
            
            break;
            
        case OVERVIEW_COMPLETE:
       
            if( this.done == null ) {
                this.done = new Image( this.drawables.getDoneDrawable() );    
                this.getStage().addActor( this.done );            
            }
                       
            this.done.addAction( Actions.sequence( Actions.alpha(0), Actions.delay(FADE_DURATION), Actions.alpha( 1, FADE_DURATION)));
            this.done.setPosition( this.getX(), this.getY() + this.getHeight() / 2);
                   
            break;
        default:
            throw new IllegalArgumentException( "Unknown state:" + newState );
          
            
        }    
        
        if( clearDoneIcon && this.done != null ) {    
            this.done.remove();
            this.done = null;            
        }
    }
    
   
    public int getTaskId() {
        return this.task.getId();
    }

    public TaskSprite(Drawable drawable) {
        super(drawable);
    }

     
    public String getTaskDescription() {
        return this.task.getDescription();
    }
    public Task getTask() {
        return this.task;       
    }
 
   
    
    @Override 
    public void setPosition( float x, float y ) {
        
        this.setX( x );
        this.setY( y );
        
        this.task.setPosX( x );
        this.task.setPosY( y );
    }

    public boolean isActive() {
        return this.task.isActive();
    }
       
    public Task.Type getType() {
        return this.task.getType();
    }
    
    public void setTaskColor( IconColor color ) {
  //  	this.task.setColour( color );   
  //  	setDrawable( this.drawables.getDrawable( this.task.getType(), this.state, null ));//this.task.getColour() ));
    }
 

    public IconColor getTaskColor() {
        return null;//this.task.getColour();
    }
    
    public void setTaskDescription( String descr ) {
        this.task.setDescription( descr );
    }
    
    public final void setCollide(boolean collide) {
   //  this.collide = collide;
    }


    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append( "[TaskSprite " );
        buff.append( this.state );
        buff.append( this.task );
        buff.append( "]" );
        return buff.toString();
    }
   
    public boolean collides( TaskSprite t ) {
      
        if( t == null ) { 
            return false;
        }
        
        Rectangle localBounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        Rectangle otherBounds = new Rectangle((int)t.getX(), (int)t.getY(), (int)t.getWidth(), (int)t.getHeight());
        
       return localBounds.overlaps( otherBounds );        
    }    
        
    
    



}


   


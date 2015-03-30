package ca.pmcgovern.mkb.sprites;


import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.ui.Task.IconColor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

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
   
   private Label label;
   
   
   public TaskSprite( Task task, TaskDrawableFactory drawables ) {
                
       super();// super( t );  
       this.drawables = drawables;
       this.task = task;    
       this.setDrawable( this.drawables.getDrawable( task.getType(), DrawState.TASK_PICKER, task.getColour() ));
         
       pack();
   }
   
   
   @Override
   public void draw( Batch batch, float parentAlpha ) {
           
       super.draw(batch, parentAlpha);
       
      
       if( this.label != null ) {       
           this.label.draw( batch,  parentAlpha );
       }
   }
  
   
   @Override
   public void positionChanged() {
       
       this.hasMoved = true;
       
       if( this.label != null ) {
           this.label.setPosition( this.getX(), this.getY() );
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
      
	 //   this.changeDrawable();
        setDrawable( this.drawables.getDrawable( this.task.getType(), this.state, this.task.getColour() ));
          	
        switch( newState ) {
        
        case TASK_PICKER:
            
          
        //    setDrawable( this.taskPickerDrawable );
            break;
            
        case TASK_PICKER_SELECTED:
        	
        	addAction( Actions.alpha( 1, FADE_DURATION )); 
             
         //   setDrawable( this.selectedDrawable );
            break;
            
        case TASK_PICKER_UNSELECTED:
        	
            addAction( Actions.alpha( UNSELECTED_ALPHA, FADE_DURATION )); 
        	break;
            
        case DELETED:
        
            addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() ));
            break;
            
        case OVERVIEW:
            break;
        case OVERVIEW_COMPLETE:
            break;
        default:
            throw new IllegalArgumentException( "Unknown state:" + newState );
          
            
        }    
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
    	this.task.setColour( color );   
    	setDrawable( this.drawables.getDrawable( this.task.getType(), this.state, this.task.getColour() ));
    }
 

    public IconColor getTaskColor() {
        return this.task.getColour();
    }
    
    public void setTaskDescription( String descr ) {
        this.task.setDescription( descr );
    }
    
    public final void setCollide(boolean collide) {
   //  this.collide = collide;
    }

    public String getTaskDescription() {
        return this.task.getDescription();
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


   


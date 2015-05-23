/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.fwt;

import ca.pmcgovern.mkb.fwt.Task.TaskState;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 *
 * @author mcgovern
 */
public class TaskSprite extends Image {

  
    protected Image done;
 
    protected Label label;  
   
    protected Task task;
    
    private boolean hasMoved;
    
    private boolean dirty;
    
    private boolean collides = true;
    
    
    public TaskSprite( Task t ) {
        this.task = t;
    }
    
  public void clearCollideEnable() {
      this.collides = false;
  }
  public void setCollideEnable() {
      this.collides = true;
  }
  public boolean getCollideEnable() {
      return this.collides;
  }
    @Override 
    public void setPosition( float x, float y ) {
        
        this.setX( x );
        this.setY( y );

        this.task.posX = x;
        this.task.posY = y;
        
        if( this.label != null ) {
            this.label.setPosition(x, y);
        }
        
        if( this.done != null ) {
            float offset = getHeight() / 2;
            this.done.setPosition(x, y + offset);
        }
        
        this.hasMoved = true;
    }  
    
    @Override
    public void setX( float x ) {

        super.setX( x );
        this.task.posX = x;  

        if( this.label != null ) {
            this.label.setX( x );
        }
        
        if( this.done != null ) {
            this.done.setX( x );
        }
        this.hasMoved = true;        
    }
    
    @Override
    public void setY( float y ) {
   
        super.setY( y );
        this.task.posY = y;
        
        if( this.label != null ) {
            this.label.setY( y );
        }
        
        if( this.done != null ) {
            float offset = getHeight() / 2;
            this.done.setY( y + offset );
        }        
        this.hasMoved = true;        
    }
    
  
   
    public void setState( TaskState state ) {
      
        if( state == null ) {
            throw new IllegalArgumentException( "New state is null" );
        }

        this.task.state = state;     
    }

    public Image getDone() {
        return done;
    }

    public void setDone(Image done) {
        this.done = done;
       // getStage().addActor( done );
    }

    protected Label getLabel() {
        return label;
        
        
    }

    protected void setLabel(Label label) {
        
        if( label == null ) {
            throw new IllegalArgumentException( "Label is null");            
        }
        
        this.label = label;
        this.label.setX( this.getX() );
        this.label.setY( this.getY() );
        
       // this.getStage().addActor( label );
        
    }

    public String getTaskDescription() {
        return this.task.description;
    }
    
    public void setTaskDescription( String newDescr ) {
        if( newDescr == null ) {
            newDescr = "";
        }
        this.task.description = newDescr;
    }
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    public int getTaskId() {
        return this.task.id;
    }
    
    public boolean isHasMoved() {
        return this.hasMoved;
    }
    @Override
    public void draw( Batch batch, float parentAlpha ) {
     
       super.draw(batch, parentAlpha);
             
       if( this.label != null ) {       
           this.label.draw( batch, parentAlpha );
       }       
     
       if( this.done != null ) {         
           this.done.draw(batch, parentAlpha );
       } 
    }
   
    
    public void setDirty() {
        this.dirty = true;
    }
    
    
    public void clearDirty() {
        this.dirty = false;
    }
    
    
    public boolean isDirty() {
        return this.dirty;
    }
    
}

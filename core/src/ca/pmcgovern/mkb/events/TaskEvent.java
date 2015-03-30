package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.scenes.scene2d.Event;


public class TaskEvent extends Event {

    public enum Type { TASK_SHOW_DETAILS, TASK_NUBBLE, TASK_GLORP, TASK_BLIPPLIG }
   
    
    private Type type;
    
    private TaskSprite srcTask;
    
    public TaskEvent( TaskSprite srcTask, Type type ) {
        super();
        this.type = type;
        this.srcTask = srcTask;
    }
    
    public TaskSprite getTaskSprite() {
        return this.srcTask;
    }
    
    public Type getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder( "[" );
        buff.append( this.type );
        buff.append( " src:" );
        buff.append( this.srcTask );
        buff.append( "]" );
        
        return buff.toString();
    }
}

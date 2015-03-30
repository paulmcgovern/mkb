package ca.pmcgovern.mkb.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class Task implements Serializable {

    public static enum Type { CALL, CUT, FIND, GLUE, PAINT, READ, SEW, WRITE };
       
    public static enum IconColor { BLUE, WHITE, GREEN, ORANGE, RED, YELLOW, NONE };
        
    public static enum TaskState { NEW, IN_PROGRESS, STOPPED, COMPLETED, DELETED };      
  
    public static Map<TaskState, TaskState[]> STATE_TRANSITIONS = new HashMap<TaskState, TaskState[]>();
    
    static {
        STATE_TRANSITIONS.put( TaskState.NEW,         new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.IN_PROGRESS, new TaskState[]{ TaskState.STOPPED, TaskState.COMPLETED } );
        STATE_TRANSITIONS.put( TaskState.STOPPED,     new TaskState[]{ TaskState.IN_PROGRESS } );
        STATE_TRANSITIONS.put( TaskState.COMPLETED,   new TaskState[]{ TaskState.IN_PROGRESS } );       
    }
            
    private int id;
    
    private boolean active;

    private Type type;
    
    private float posX;
    
    private float posY;
    
    
    
    /** Default color: "none" */
    private IconColor colour =  IconColor.NONE;
    
    /** Default state: new */
    private TaskState state = TaskState.NEW;
    
    private String description;
       
    
    public final void setColour( IconColor color ) {
        this.colour = color;
    }
    public final IconColor getColour() {
        return this.colour;
    }
    
    public final boolean isActive() {
        return active;
    }

    public final void setActive(boolean active) {
        this.active = active;
    }

    public final Type getType() {
        return this.type;
    }

    public final void setType(Type type) {
        this.type = type;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {        
        this.description = description;
    }
    
	public TaskState getState() {
		return state;
	}
	public void setState(TaskState state) {
		this.state = state;
	}
    
	public final int getId() {
        return id;
    }
	
    public final void setId(int id) {
        this.id = id;
    }
    
    
    
    public final float getPosX() {
        return posX;
    }
    public final void setPosX(float posX) {
        this.posX = posX;
    }
    public final float getPosY() {
        return posY;
    }
    public final void setPosY(float posY) {
        this.posY = posY;
    }
    public String toString() {
	    StringBuilder buff = new StringBuilder( "[Task " );
	    buff.append( this.id );
	    buff.append( " " );
	    buff.append( this.type );
	    buff.append( " " );
	    buff.append( this.colour );   
	    buff.append( "]" );
	    return buff.toString();
	    
	}
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colour == null) ? 0 : colour.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    
    /** 
     * Compares type, description, and colour
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        if (colour != other.colour)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    
}

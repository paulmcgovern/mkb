/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.fwt;

/**
 *
 * @author mcgovern
 */
public class Task {

    public static enum Type { CALL, CUT, FIND, GLUE, PAINT, READ, SEW, WRITE };
       
    public static enum IconColor { BLUE, WHITE, GREEN, ORANGE, RED, YELLOW, NONE };
        
    public static enum TaskState { NEW, IN_PROGRESS, STOPPED, COMPLETED, DELETED };      
  
    float posX;
    float posY;
    TaskState state;
    IconColor colour;
    Type type;
    int id;
    String description;

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public IconColor getColour() {
        return colour;
    }

    public void setColour(IconColor colour) {
        this.colour = colour;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}

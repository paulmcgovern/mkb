/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.fwt.TaskSprite;
import com.badlogic.gdx.scenes.scene2d.Event;

/**
 *
 * @author mcgovern
 */
public class TaskSavedEvent extends Event {
    	
        public static enum Type { EDIT, NEW }
        
        private TaskSprite ts;
        private Type type;
        
        public TaskSavedEvent( TaskSprite ts, Type type ) {
            this.ts = ts;
            this.type = type;
            this.setBubbles( true );
        } 
        
        public Type getType() {
            return this.type;
        }
        
        public TaskSprite getTaskSprite() {
            return this.ts;
        }
    }        
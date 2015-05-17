/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 *
 * @author mcgovern
 */
public class MenuEvent extends Event {

    public static enum Type {
        OPEN, CLOSE
    }

    private Type t;

    public MenuEvent(Type t) {
        this.t = t;
        this.setBubbles(true);
    }

    public Type getType() {
        return this.t;
    }

}

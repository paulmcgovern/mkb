/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.events;

import ca.pmcgovern.mkb.ui.ZoomControl;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 *
 * @author mcgovern
 */
public class ExtentsListener extends ChangeListener {

    private ZoomControl zoomControl;
    private float maxZoom;
    
    public ExtentsListener(ZoomControl zoomControl, float maxZoom ) {
        this.zoomControl = zoomControl;
        this.maxZoom = maxZoom;
    }
    
    @Override
    public void changed(ChangeEvent event, Actor actor) {
    
        this.zoomControl.setValue( this.maxZoom );
    }
    
    
    
}

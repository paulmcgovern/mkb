package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ZoomControl extends Slider {

    
    public ZoomControl( final OrthographicCamera camera, Skin skin, float maxZoomOut ) {
        
        super( camera.zoom, camera.zoom * maxZoomOut, 0.05f, true, skin );
       
        addListener( new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                
                float val = getValue();
                camera.zoom = val;
                camera.update();    
      
            }
        });
        
     
        
    }
    
    
    

}

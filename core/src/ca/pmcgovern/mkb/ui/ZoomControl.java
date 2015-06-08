package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.math.Rectangle;

public class ZoomControl extends Slider {

    public static final String ZOOM_SLIDER = "ZOOM_SLIDER";
    public static final float STEP_SIZE = 0.05f;
    
    public ZoomControl( final OrthographicCamera camera, Skin skin, float maxZoomOut, Rectangle extents ) {
        
        super( camera.zoom, camera.zoom * maxZoomOut, STEP_SIZE, true, skin );
       
        if( extents == null ) {
            throw new IllegalArgumentException( "Extents is null." );
        }
        
        addListener( new ZoomChangeListener( camera, extents )); 
    }
    
    @Override
    public String getName() {
        return ZOOM_SLIDER;
    }
    
    class ZoomChangeListener extends ChangeListener {
        
        private float prevZoom;
        private final OrthographicCamera camera;
        private final Rectangle extents;
        
        public ZoomChangeListener( OrthographicCamera camera, Rectangle extents ) {
            this.camera = camera;
            this.prevZoom = camera.zoom;
            this.extents = extents;
        }
        
        
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
            
            float sliderValue = getValue();
            
            if( sliderValue > this.prevZoom ) {
                           
                float effectiveViewportWidth = camera.viewportWidth * sliderValue; // i.e. next zoom
                float effectiveViewportHeight = camera.viewportHeight * sliderValue;
          
                Rectangle vp = new Rectangle( 0,0, effectiveViewportWidth, effectiveViewportHeight );
                vp.setCenter( camera.position.x, camera.position.y );

                if( !extents.contains( vp )) {
                
                    float vpX = vp.getX();
                    float extX = extents.getX();
                
                    if( vpX < extX ) {
                        
                        camera.position.x = camera.position.x + Math.abs( vpX - extX );
                        
                    } else if( (vpX + vp.width) > (extX + extents.getWidth()) ) {
           
                        camera.position.x = camera.position.x - (Math.abs(vpX + vp.width) - (extX + extents.getWidth()) );
                    }
                
                    float vpY = vp.getY();
                    float extY = extents.getY();
                
               
                    if( vpY < extY ) {                        
                  
                        camera.position.y = camera.position.y + Math.abs( vpY - extY );
                        
                    } else if( (vpY + vp.height) > (extY + extents.getHeight())  ) {
                        
                        camera.position.y = camera.position.y - Math.abs( (vpY + vp.height) - (extY + extents.getHeight()) );
                    }                           
                }
            } 
            
            this.camera.zoom = sliderValue;
            this.camera.update();            
            this.prevZoom = sliderValue;            
        }
    }
    

}

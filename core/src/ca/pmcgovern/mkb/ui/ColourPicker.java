package ca.pmcgovern.mkb.ui;

import static ca.pmcgovern.mkb.ui.Task.IconColor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ColourPicker extends Table {

 //   public static final float FADE_DURATION = 0.25f;
    public static final int NOT_SET = -1;
    
    private Task.IconColor currentColor = Task.IconColor.NONE;
    
    public ColourPicker( TextureAtlas colourIcons, Skin skin, int width, int height, float iconDmtr ) {
   
        super( skin );
       
        ClickListener iconListener = new IconClickListener();
        
        Task.IconColor[] allColors = Task.IconColor.values();
       
        
        
        for( int i = 0; i < allColors.length; i++ ) {
         
        	if( Task.IconColor.NONE == allColors[ i ] ) {
        		continue;
        	}
      
        	
          	if( Task.IconColor.WHITE == allColors[ i ] ) {
          		System.err.println( "FIXME: white circle missing");
        		continue;
        	}
        	
            Image img = new Image( colourIcons.createSprite( allColors[ i ].name().toLowerCase() ));
      
            float imgWidth = img.getWidth();
            
            img.setOrigin( imgWidth / 2, imgWidth / 2 );
            
            float scale = 0;
            
            if( imgWidth > iconDmtr ) {
                scale = -1 * ( 1 - (iconDmtr / img.getWidth()));
            } else {
                scale = img.getWidth() / iconDmtr;
            }
                       
            img.scaleBy( scale );  
            img.setName( Integer.toString( i ));
            img.addListener( iconListener );    
            this.add( img );
            
          
        }    
     
        this.pack();
     
    }
    
   
    public Task.IconColor getCurrentColor() {
        return this.currentColor;
    }    
    
    
    public boolean isColourPicked() {
        return this.currentColor != Task.IconColor.NONE;
    }
    
    
    class IconClickListener extends ClickListener {
        
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
           
            event.getListenerActor().addAction( Actions.scaleBy( 0.15f, 0.15f, 0.25f, Interpolation.exp10 ));
                      
            return true;
        }
  
        
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
         
            Image colorIcon = (Image)event.getListenerActor();                  
            colorIcon.addAction( Actions.scaleBy( -0.15f, -0.15f, 0.5f, new Interpolation.ElasticOut( 2, 5, 1, 1 )));
 
            if( isOver() ) {
              
            	Task.IconColor prevColor = ColourPicker.this.currentColor;
            	 
                try {
                    
                	Task.IconColor[] allColors = Task.IconColor.values();
                    
                    int colorIdx = Integer.parseInt( colorIcon.getName() );               
                    ColourPicker.this.currentColor = allColors[ colorIdx ];
                    ColourPicker.this.fire( new PickedEvent( allColors[ colorIdx ] ) );
                    
                } catch( NumberFormatException e ) {
                    ColourPicker.this.currentColor = prevColor;
                }
            }   
        } 
    }     

 
    /**
     * Indicates a new task has been selected.
     * @author mcgovern
     *
     */
    public class PickedEvent extends Event {
    	
        private Task.IconColor colour;
        
        public PickedEvent( Task.IconColor colour ) {
            this.colour = colour;
            this.setBubbles( true );
        } 
        
        public Task.IconColor getColour() {
            return this.colour;
        }
    }
}

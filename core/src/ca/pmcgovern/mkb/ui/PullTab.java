package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PullTab extends Image {

    public PullTab( Sprite t ) {
         
        super( t ); 
	    setTouchable( Touchable.enabled );
	    setVisible( true );
	    setBounds( 0, 0, getWidth()/2, getHeight() );
    } 
	
  
    
}

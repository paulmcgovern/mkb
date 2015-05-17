package ca.pmcgovern.mkb.keyboard;

//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class FunctionalKey extends TextButton {

    
    protected String main;
   
    
    public FunctionalKey( int prefWidth, String main, TextButtonStyle keyStyle ) {

        super( main, keyStyle );
       
        this.main = main;
        
     //   TextBounds tb = new TextBounds();
     //   tb.width = prefWidth;     
         
       // getLabel().getTextBounds().set( tb );
       // getLabelCell().pad( 8, 2, 5, 2);        // t l b r  
       
              
         
    }
    
  
   
}

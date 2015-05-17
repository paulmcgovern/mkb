package ca.pmcgovern.mkb.keyboard;

//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class Key extends TextButton {

    
    protected String main;
    protected String alternate;
    protected Label altLabel;
    protected Label shift;
    
    public Key( int prefWidth, String main, String alternate, TextButtonStyle keyStyle, LabelStyle alternateStyle) {

        super( main, keyStyle );
       
        this.main = main;
        this.alternate = alternate;
        
      //  TextBounds tb = new TextBounds();
     //   tb.width = prefWidth;     
         
     //   getLabel().getTextBounds().set( tb );
        getLabelCell().pad( 8, 2, 5, 2);        // t l b r  
       
        this.altLabel = new Label( alternate, alternateStyle );
        this.altLabel.setTouchable( Touchable.disabled );  
        this.getLabel().setTouchable( Touchable.disabled );  
        addActor( this.altLabel );
               
        altLabel.setY( altLabel.getY() + 10 );
        altLabel.setX( altLabel.getX() + 5 );        
    }
    
    public void toUpper() {
        
       // this.getLabelCell().free();
        this.getLabel().setText( this.alternate );
        
    }
   
}

package ca.pmcgovern.mkb.keyboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * LibGDX doesn't play nice with alternate keyboards
 * and kids may need something simpler than what 
 * is provided by default.
 * 
 *  0 means "no character"
 *  Special functional keys are given values less than zero.
 * Uses TextButtons throughout and a font generated on the fly.
 * @author mcgovern
 *
 */
public class Keyboard extends Group implements OnscreenKeyboard {

    private static int LAYOUT_ENTER_KEY = -1;
    private static int LAYOUT_DELETE_KEY = -2;
    private static int LAYOUT_SHIFT_KEY = -3;
    private static int LAYOUT_SPACE_KEY = -4;
    private static int LAYOUT_SYMBOLS_KEY = -5;
    
    public static final int[] ALT_0 = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')' };
    public static final int[] CHR_0 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };   

    public static final int[] CHR_1 = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p' };
    public static final int[] ALT_1 = { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P' };
    
    public static final int[] ALT_2 = { 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 0  }; 
    public static final int[] CHR_2 = { 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', LAYOUT_DELETE_KEY  };

    public static final int[] ALT_3 = { 0,                'Z', 'X', 'C', 'V', 'B', 'N', 'M', 0 }; 
    public static final int[] CHR_3 = { LAYOUT_SHIFT_KEY, 'z', 'x', 'c', 'v', 'b', 'n', 'm', LAYOUT_ENTER_KEY };

    public static final int[] ALT_4 = { 0,               0,     0, 'V', 'B', 'N', 'M', 0 }; 
    public static final int[] CHR_4 = { '!', LAYOUT_SYMBOLS_KEY,      LAYOUT_SPACE_KEY, 'v', 'b', 'n', 'm' };
   
   // public static final String ROW_2 = "ASDFGHJKL";
   // public static final String ROW_3 = "ZXCVBNM";      
  //  protected KeyChangeListener keyListener = new KeyChangeListener();
  //  protected KeyClickListener keyListener = new KeyClickListener();
    
    protected  KeyClickListener keyListener = new KeyClickListener();
    
    protected BitmapFont big;
    
    protected BitmapFont small;
    protected InputListener keyInputListener;
    
    protected Table main;
    protected Table alternate;
   
    protected TextButton currentLetter;
    
    public Keyboard( BitmapFont smallFont, BitmapFont bigFont, Skin skin ) {
       
        
        this.big = bigFont;
        this.small = smallFont;
        this.keyInputListener = new KeyInputListener();
        TextButtonStyle keyStyle = skin.get( "toggle", TextButtonStyle.class );
        keyStyle.font = bigFont;

        TextButtonStyle alternateStyle = skin.get( TextButtonStyle.class );
        alternateStyle.font = smallFont;
        
        
        this.main = new Table( skin );
        this.main.debug();
        this.main.setWidth( this.getWidth());
        
        addKeyRow( CHR_0, ALT_0, keyStyle, alternateStyle, this.main );
        this.main.row();       
        addKeyRow( CHR_1, ALT_1, keyStyle, alternateStyle, this.main );      
        this.main.row();
        addKeyRow( CHR_2, ALT_2, keyStyle, alternateStyle, this.main );
        this.main.row();
        addKeyRow( CHR_3, ALT_3, keyStyle, alternateStyle, this.main );  
        this.main.row();        
       addKeyRow( CHR_4, ALT_4, keyStyle, alternateStyle, this.main );  
        this.main.pack();
        
        this.addActor( this.main );
        
        this.alternate = new Table( skin );         
     //   addKeyRow( ALT_0, CHR_0, keyStyle, alternateStyle, this.alternate );        
        this.alternate.row();        
     //   addKeyRow( ALT_1, CHR_1, keyStyle, alternateStyle, this.alternate );
        this.alternate.row();        
        addKeyRow( ALT_2, CHR_2, keyStyle, alternateStyle, this.alternate );
        this.alternate.row();        
     //   addKeyRow( ALT_3, CHR_3, keyStyle, alternateStyle, this.alternate );
      //  this.alternate.row();        
     //   addKeyRow( ALT_4, CHR_4, keyStyle, alternateStyle, this.alternate );      
        this.alternate.pack();
        
      //  this.addActor( this.alternate );
        
        this.currentLetter = new TextButton( "M", keyStyle );
        this.currentLetter.setTouchable( Touchable.disabled );
        this.main.debug();
        this.alternate.debug();
        

    }
    
    protected void clearCurrentLetter() {
        this.currentLetter.addAction( Actions.alpha( 0, 0.25f ));
    }
    
    protected void setCurrentLetter( CharSequence text, float xPos ) {
        
        this.currentLetter.clearActions();
        this.currentLetter.setText( text.toString() );
        
       // TextBounds tb = new TextBounds();
       // tb.width = 40;     
         
        //this.currentLetter.getTextBounds().set( tb );
        //getLabelCell().pad( 8, 2, 5, 2);   
        
       //this.currentLetter.get
        this.currentLetter.addAction( Actions.alpha( 1, 0.1f ));
       this.currentLetter.setPosition( xPos,  this.main.getHeight() + 10 );
               
       // System.err.println( "H " + this.main() );
        this.getStage().addActor( this.currentLetter );
    }
    
    private void addKeyRow( int[] chs, int[] alt, TextButtonStyle keyStyle, TextButtonStyle alternateStyle, Table parentTable ) {
     
        int maxWidth = Gdx.graphics.getWidth();
        
        int keyWidth = (maxWidth / 14);
     
        LabelStyle altStyle = new LabelStyle();
        
        altStyle.font = this.small;
        altStyle.fontColor = Color.BLACK;
    
        keyStyle.font = this.big;
        System.err.println( maxWidth );
        String main = null;
        String alternate = null;
        int ch = 0;
        int colspan = 1;
        
        for( int i = 0; i < chs.length; i++ ) {
         
            ch = chs[ i ];
            
            if( ch < 0 ) {
                
               if( ch == LAYOUT_SPACE_KEY ) {
                   colspan = 4;                   
               } else if ( ch == LAYOUT_ENTER_KEY) {
                   colspan = 2;                             
               } else {
                   colspan = 1;
               }
               
               parentTable.add( makeFunctionalKey( ch, keyWidth * colspan, keyStyle ) ).colspan( colspan ).center();             
               continue;
               
            } else {
                main = Character.toString( (char)chs[ i ] );     
            }
                   
            ch = (char)alt[ i ];
            
            if( ch <= 0 ) {
                alternate = "";
            } else {
                alternate = Character.toString( (char)alt[ i ] );     
            }
            
            System.err.println( "M :'" + main + "' ALT: '" + alternate + "'" );
            
            Key k = new Key( keyWidth, main, alternate, keyStyle, altStyle );   
            
        
            //k.addListener( this.keyListener );
           k.addListener( this.keyInputListener );
            parentTable.add( k );
        }
    }

    
    
    protected FunctionalKey makeFunctionalKey( int keyTypeId, int keyWidth, TextButtonStyle keyStyle ) {
   
        System.err.println( "FN KEY" );
        FunctionalKey k = new FunctionalKey( keyWidth, "@", keyStyle );         
        k.addListener( this.keyListener );
        return k;
    }
    


    /** 
     * Onscreenkeyboard method
     */
    @Override
    public void show( boolean visible ) {
      
        
    }

     /*
    class KeyChangeListener extends ChangeListener {

               
        @Override
        public void changed( ChangeEvent event, Actor actor ) {            
          TextButton t =  (TextButton)actor;
          System.err.println( t.getText() );
        }
    }*/

   /*
    class KeyClickListener extends ClickListener {

        public boolean  touchDown( InputEvent event, float x, float y, int pointer, int button) {
            System.err.println( "TDn " + event.getListenerActor());
            return false;
        }
        
        public void    touchDragged(InputEvent event, float x, float y, int pointer) {
            System.err.println( "TDg " + event.getListenerActor());            
        }
      
        public void    touchUp(InputEvent event, float x, float y, int pointer, int button) {
            System.err.println( "TUp " + event.getListenerActor());
        }
    } */
    
    class KeyClickListener extends ActorGestureListener {

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.err.println( "TDn " + event.getListenerActor());
            
           
            Keyboard.this.setCurrentLetter( ((Key)event.getListenerActor()).getText(),  event.getListenerActor().getX() );
           
            // TODO Auto-generated method stub
            super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            System.err.println( "TUp " + event.getListenerActor());
            // TODO Auto-generated method stub
             Keyboard.this.clearCurrentLetter();
             
            Key m = (Key)event.getListenerActor();
        //    m.toUpper();
      /**   
            if( Keyboard.this.main.isVisible() ) {
         
                Keyboard.this.main.setVisible( false );
                float kx = Keyboard.this.main.getX();
                float ky = Keyboard.this.main.getY();
              
                Keyboard.this.main.remove();
                Keyboard.this.addActor( Keyboard.this.alternate );
                Keyboard.this.alternate.setVisible( true ); 
                Keyboard.this.alternate.setX( kx );
                Keyboard.this.alternate.setY( ky );         
            } else {
                System.err.println( "alt -> main");       
                Keyboard.this.alternate.setVisible( false );
                float kx = Keyboard.this.main.getX();
                float ky = Keyboard.this.main.getY();
                                
                Keyboard.this.alternate.remove();
                Keyboard.this.addActor( Keyboard.this.main );
                Keyboard.this.main.setVisible( true );
                Keyboard.this.main.setX( kx );
                Keyboard.this.main.setY( ky );    
            }
          **/  
            
       //   SnapshotArray<Actor> aa = Keyboard.this.getChildren();          
        //  for( Actor a : aa ) {
        //      a.setVisible( !a.isVisible() );
        //  }          
        //  Keyboard.this.setY( Keyboard.this.getY() + Keyboard.this.getPrefHeight() / 2 );
          
          
            System.err.println( m.getText() );
            super.touchUp(event, x, y, pointer, button);
            
         //   TextField t = (TextField)m.getStage().getRoot().findActor( "text" );
            
          // t.setText( t.getText() + m.getText() );
          //  t.setCursorPosition( t.getText().length() );
            
        }

        @Override
        public void tap(InputEvent event, float x, float y, int count,
                int button) {
            System.err.println( "Tap " + event.getListenerActor());
            
            
            
            // TODO Auto-generated method stub
            super.tap(event, x, y, count, button);
        }

        @Override
        public boolean longPress(Actor actor, float x, float y) {
            System.err.println( "Long " + actor );
            // TODO Auto-generated method stub
            return super.longPress(actor, x, y);
        }


        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            System.err.println( "Pan " + ((Key)event.getListenerActor()).getText());
            
            // TODO Auto-generated method stub
            
            super.pan(event, x, y, deltaX, deltaY);
        }
        
    }
    
    
    class KeyInputListener extends InputListener {

        private boolean isPressed;
       
        public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            
            System.err.println( "From actor: " + fromActor );
            ((Key)event.getListenerActor()).toggle();
            
        }
        
        public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
           
            
            System.err.println( "To actor: " + toActor );
            
            ((Key)event.getListenerActor()).toggle();
            
           // if( this.isPressed ) {
               
               Keyboard.this.setCurrentLetter( ((Key)event.getListenerActor()).getText(),  event.getListenerActor().getX() ); 
             
          //     System.err.println( "CK:" + ((Key)event.getListenerActor()).isChecked() );
         //      ((Key)event.getListenerActor()).toggle();
               
             //  ((Key)event.getListenerActor()).
              // TextButtonStyle s = new TextButtonStyle( ((Key)event.getListenerActor()).getStyle() );
              // s.up = s.down;               
              //(Key)event.getListenerActor()).setStyle( s );               
              // Drawable d= ((Key)event.getListenerActor()).getStyle().down;           
              // ((Key)event.getListenerActor()).getStyle().up = d;
           // }
       
      //     ((Key)event.getListenerActor()).getClickListener().touchDown(event, 1, 1, pointer, 1);
            System.err.println( "ENTER" ) ;           
           
        }
    
        
       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
           
           this.isPressed = true;
           System.err.println( "TOUCHDOWN")  ; 
        //   this.isPressed = true;
           
           Keyboard.this.setCurrentLetter( ((Key)event.getListenerActor()).getText(), event.getListenerActor().getX() );
          
          return true;
       }
      
     
    
       public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        this.isPressed = false;
        Keyboard.this.clearCurrentLetter();
          System.err.println(((Key)event.getListenerActor()).getText() );
           System.err.println( "TOUCHUP")  ;  
       }
    }
}

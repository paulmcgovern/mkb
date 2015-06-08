package ca.pmcgovern.mkb.ui;

import ca.pmcgovern.mkb.actions.ChangeScreenAction;
import ca.pmcgovern.mkb.screens.MkbScreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuTable extends Table {

	public static final int STATE_OPEN = 2;
	public static final int STATE_CLOSED = 1;
	public static final int STATE_FORCED_CLOSE = 4;
    
	public static final int MAX_BOUNCES = 4;
	protected float homeX;
	
	//protected float openThreshold;
	//protected float closeThreshold;
	
	protected float hiddenWidth;
	
	protected int pullState = STATE_CLOSED;
	
	public MenuTable( int width, int height, TextureAtlas spriteAtlas, Skin skin) {
		
		super();
		this.debug();
		
					
		Table buttonsTable = new Table( skin );
		buttonsTable.setBackground( "default-pane" );
		
		buttonsTable.add( new Label( "Line One of Two", skin ));
		buttonsTable.row();
		buttonsTable.add( new Label( "Line Two of Two", skin ));
		buttonsTable.row();
	//	buttonsTable.add( buildActionButton( "Add New Task", skin, MkbScreen.ScreenId.NEW_SCREEN ));
		buttonsTable.row();
		buttonsTable.add( buildActionButton( "Settings", skin, MkbScreen.ScreenId.SETTINGS ));
        buttonsTable.row();
        buttonsTable.add( buildActionButton( "About", skin, MkbScreen.ScreenId.HELP ));
        buttonsTable.row();
        
		buttonsTable.pack();
		
		this.add( buttonsTable ).height( height );
	
		//PullTab tab = new PullTab( spriteAtlas.createSprite( "tab" ));		
		//float tabWidth = tab.getWidth();
		
		
		//Table tabTable = new Table();
		//tabTable.add( tab );
		//tabTable.pack();
		
		//this.add( tabTable );
		//this.pack();
		
		//this.addListener( new PullTabGestureListener() );
		System.err.println( "pt:" + this.getWidth() + "," + this.getHeight() );
		//System.err.println( "tab w: " + tabWidth );
		this.pack();
		this.homeX = -1 * this.getWidth();		
		this.setX( this.homeX );
		
		this.hiddenWidth =  this.getWidth();
		//this.openThreshold = -1 * this.hiddenWidth );
		//this.closeThreshold = -1 * tabWidth;
		
		System.err.println( "MT: " + this.getX() + " " + this.getY()  + " w:" + this.getWidth() );
	}

	public void toggle() {
	    
	    if( this.pullState == STATE_FORCED_CLOSE ) {
	        return;
	    }
	    
	    if( this.pullState == STATE_OPEN) {
	        close();
	    } else {
	        open();
	    }
	}
	
	public void open() {	   
	    this.clearActions();     
        this.slide( 0, MAX_BOUNCES, 0.5f, STATE_OPEN );	    
	}
	
	public void close() {
        this.clearActions();     
        this.slide( this.homeX, MAX_BOUNCES, 0.5f, STATE_CLOSED );    	    
	}
	
	
	private Button buildActionButton( String label, Skin skin, MkbScreen.ScreenId nextScreenId )  {

	  //  ImageTextButtonStyle itbs = new ImageTextButtonStyle();
      //  itbs.font = generatedFont;
      //  itbs.fontColor = Color.LIGHT_GRAY;
      //  itbs.downFontColor = Color.BLACK;
      //  itbs.overFontColor = Color.WHITE;
      //  itbs.imageUp = new TextureRegionDrawable( atlas.findRegion( upImg ));
      //  itbs.imageDown = new TextureRegionDrawable( atlas.findRegion( overImg ));
      //  itbs.imageOver = new TextureRegionDrawable( atlas.findRegion( downImg ));
       
        ImageTextButton itb = new ImageTextButton( label, skin );

        // Put image on the right.
    //    itb.clear();
     //   itb.add( itb.getLabel() ).padRight( 20 );
     //   itb.add( itb.getImage() );
        
    //    itb.addListener( new ButtonChangeListener( nextScreenId ) );
    
        return itb;
	}
	/*
    class PullTabGestureListener extends ActorGestureListener {

        public PullTabGestureListener() {
            super( 30.0f, 1.0f, 1.0f, 1.0f );
        }
        
 
        
        @Override
        public void pan ( InputEvent event, float x, float y, float deltaX, float deltaY ) {
        	
        	 float newPos = MenuTable.this.getX() + deltaX;
        	 float newY =  MenuTable.this.getY();
        	 
        	 if( deltaX > 0 ) {
        	 
        		 if( newPos >= 0 ) {        			
        			 MenuTable.this.setPosition( 0, newY );
        			 MenuTable.this.pullState = MenuTable.STATE_OPEN;
        			 return;
        		 }
        
        	 } else {        		 
        		 
        		 if( newPos <= MenuTable.this.homeX ) {
        			 MenuTable.this.setPosition( MenuTable.this.homeX, newY ); 
        			 MenuTable.this.pullState = MenuTable.STATE_CLOSED;
        			 return;
        		 }
        	 }
    
        	 MenuTable.this.setPosition( newPos, newY );
        }
            
        
        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        	
        	Actor actor = event.getListenerActor();  
        	actor.clearActions();
        }

        @Override
        /**
         * TODO: make number of bounces relative to distance of travel. No bounce on small distance.
         * Does nothing if pull state is STATE_FORCED_CLOSE.
         */
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            
            // Don't interfere with a forced open or closed.
            if(  MenuTable.this.pullState == STATE_FORCED_CLOSE ) {
                return;
            }
            
            MenuTable.this.toggle();
            
       	 //	float menuX =  MenuTable.this.getX();
     
       	//	int numBounces = MAX_BOUNCES;
         // 	 float endX = 0;
       	 	
       	 //	int endState = 0;
       	 //	float travelTime = 0.5f;
       	 	
       	 	//if( MenuTable.this.pullState == MenuTable.STATE_CLOSED ) {
       	     
       	 	//	if( menuX > MenuTable.this.openThreshold ) {
       	 			
       	 	//		endState = MenuTable.STATE_OPEN;
       	 			
       	 	//	} else {
       	 			
       	 	//		endState = MenuTable.STATE_CLOSED;
       	 	//		endX = MenuTable.this.homeX;
       	 	//		travelTime = 0.25f;
       	 	//		numBounces = 0;
       	 	//	}
       	 		
       	 //	} else {
       	 		
       	 		//if( menuX < MenuTable.this.closeThreshold ) {
       	 			
       	 		//	endState = MenuTable.STATE_CLOSED;
       	 		//	endX = MenuTable.this.homeX;
       	 		//} else {
       	 			
       	 		//	endState = MenuTable.STATE_OPEN;
       	 		//	travelTime = 0.25f;
       	 		//	numBounces = 0;
       	 		//}
       	 	//}
       
       	 	// TODO: vary time with distance
       	 	//	double distance = (MenuTable.this.hiddenWidth - Math.abs( menuX )) / MenuTable.this.hiddenWidth;       	 	
       	 	//	if( distance < 0 ) {
       	 	//		distance = 0;
       	 	//	} else if( endState == MenuTable.STATE_OPEN ) {
       	 	//		distance = 1.0 - distance; 
       	 	//	}
       	 	
       	 //	MenuTable.this.slide( endX, numBounces, travelTime, endState );
      //  }   
    }
   
   
    private void slide( float destX, int numBounces, float travelTime, int endState, Action... actions ) {
     
        Interpolation mvmtEffect = null;
        
        if( numBounces < 2 ) {
            mvmtEffect = Interpolation.exp10Out;          
        } else {
            mvmtEffect = new Interpolation.BounceOut( numBounces );
        }
    
        Action movementAction = Actions.moveTo( destX, MenuTable.this.getY(), travelTime, mvmtEffect );
                
        SequenceAction seq = new SequenceAction( movementAction, new EndStateAction( endState ));
        
        for( Action a : actions ) {
            seq.addAction( a );
        }
        
        MenuTable.this.addAction( seq );  
    }
    
   
    class EndStateAction extends Action {
    	
    	int newState;
    	
    	public EndStateAction( int newState ) {
    		super();
    		this.newState = newState;
    	}
    	
    	@Override
    	public boolean act( float delta ) {
    	    System.err.println( ((MenuTable)this.actor).pullState + " -> " + newState );
    		((MenuTable)this.actor).pullState = newState;      
    		return true;
    	}
    }
    
    
    /**
     * Sets pull state STATE_FORCED_CLOSE and slides to home position.     * 
     * @author mcgovern
     *
     */
    class ButtonChangeListener extends ChangeListener {

        private MkbScreen.ScreenId  nextScreenId;
        
        public ButtonChangeListener( MkbScreen.ScreenId nextScreenId ) {
            this.nextScreenId = nextScreenId;
        }
        
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
          System.err.println( "CHANGES");  
            // Let other listeners know they should 
            // not interfere with movement.
            MenuTable.this.pullState = STATE_FORCED_CLOSE;
            event.handle();
            MenuTable.this.clearActions();
            
            MenuTable.this.slide( MenuTable.this.homeX, 2, 0.5f, STATE_CLOSED,  new ChangeScreenAction( nextScreenId ) );
        }
    }
}

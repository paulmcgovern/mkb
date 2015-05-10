package ca.pmcgovern.mkb.screens;


import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.MonsterSprite;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.ui.ColourPicker;
import ca.pmcgovern.mkb.ui.NinePatchScaler;
import ca.pmcgovern.mkb.fwt.Task;
import ca.pmcgovern.mkb.menus.newtask.FormContainer;
import ca.pmcgovern.mkb.ui.TaskPicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


public class NewTaskScreen2 extends MkbScreen {

    public static final float FADE_DURATION = 0.25f;
    
    private MonsterSprite monster;
    
    private int editTargetId;
    
    private Stage uiStage;

    private OrthographicCamera uiCamera;
    
    private Skin skin;
    
    private TextureAtlas taskSprites;
    private TextureAtlas iconSprites;
    
  
    private TaskPicker taskSelectTable;

    private ColourPicker colourSelectTable;
        
   
    
    private ShaderProgram dropShadowShader;
    private ShaderProgram passthroughShader;
    private ShaderProgram vignetteShader;        
    private FrameBuffer shadowBuff;
    private FrameBuffer screenBuff;
   
 //   private Group background;
     private TaskSpriteManager taskManager;
    
    private ImageTextButton save; 
            
    private ImageTextButton cancel;
    
    
    private TextField titleInput;
    
   
    public static final String TAG = "NewTaskScreen";
   
   
    public NewTaskScreen2(AssetManager assetMgr, EffectManager effectMgr ) {
          
        super( assetMgr, effectMgr );
                
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;
    
        // Render drop shadows
        this.dropShadowShader = new ShaderProgram( Gdx.files.internal("data/shaders/monochrome.vert"),  Gdx.files.internal("data/shaders/monochrome.frag"));

        // Do-nothing shader
        this.passthroughShader = new ShaderProgram(Gdx.files.internal("data/shaders/passthrough.vert"),  Gdx.files.internal("data/shaders/passthrough.frag"));
        
        // Vignette around active task shader
        this.vignetteShader = new ShaderProgram( Gdx.files.internal("data/shaders/vignette.vert"),  Gdx.files.internal("data/shaders/vignette.frag"));
         
        
        
    }
    
    
    @Override 
    public MkbScreen.ScreenId getId() {
        return MkbScreen.ScreenId.NEW_SCREEN;
    }
    
    @Override
    public void hide() { 
    //    this.selectedTaskIcon = null;
  //      this.background.clear();        
    }    
    
    @Override
    public void setOpenMenu( MkbMenu w ) {        
    }
  
    @Override
    public void clearOpenMenu() {        
    }
    
    private void updateTaskDescription() {
       
        String descrText = this.titleInput.getText();
        
        if( descrText == null ) {
            return;
        }
        
        this.taskSelectTable.setTaskDescription( descrText.trim() );
    }
    

    
    
    
    /**
    protected TextureRegion drawToShadow( Batch spriteBatch, FrameBuffer shadowBuff, Actor[] allActors, int start, int count ) {
         
        shadowBuff.begin();
        
        Gdx.gl.glClearColor(0, 0, 0, 0 );       
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      
        Gdx.gl.glEnable(GL20.GL_BLEND );
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
     
        
        spriteBatch.setShader( this.dropShadowShader );     
        
        
        spriteBatch.begin();  
        
       
        
        for( int i = start; i < (start + count); i++ ) {
         
            Actor a = allActors[ i ];
            a.draw( spriteBatch,  1 );
        }
            
        spriteBatch.end();      
        shadowBuff.end();
       
        TextureRegion shadow = new TextureRegion( this.shadowBuff.getColorBufferTexture() );  
        shadow.flip( false, true );
    
        return shadow;
    }
         
    */
   
    
    @Override
    public void render( float delta ) {

        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);       
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
      
        this.uiStage.act();
  
        FrameBuffer screenBuff = null;
        
        if( this.nextScreenId != null ) {          
            screenBuff = new FrameBuffer( Pixmap.Format.RGBA8888, this.width, this.height, false );  
            screenBuff.begin();            
        } 
    
        // There is a single SpriteBatch, it happens
        // to be part of the task stage.        
        Batch bspriteBatch = this.uiStage.getBatch();
     
         
      //  Actor[] uiActors = new Actor[ 10 ];        
       // Actor titleInput = this.uiStage.getRoot().findActor( "titleInput" );           
       // uiActors[ 0 ] = this.uiStage.getRoot().findActor( "container" );
       // uiActors[ 1 ] = this.uiStage.getRoot().findActor( "textBanner" );
       // uiActors[ 2 ] = this.uiStage.getRoot().findActor( "bannerFg" );             
    //  TextureRegion shadow = drawToShadow( bspriteBatch, shadowBuff, uiActors, 0, 1 );
        

  //     bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        

   //   bspriteBatch.begin();    
    //   bspriteBatch.setShader( this.passthroughShader );
       
   //    drawBackground(bspriteBatch);
    //    bspriteBatch.end();
      //  Table.drawDebug( this.uiStage );
     //   bspriteBatch.begin();
     //     bspriteBatch.setShader( this.passthroughShader );
      //  drawBackground( bspriteBatch );
    //    bspriteBatch.end();
      this.uiStage.draw();  
     // ;
       /***
       bspriteBatch.draw( shadow, 10, -10, this.width, this.height ); 
       bspriteBatch.end();   
       
       // Draw main table
       bspriteBatch.begin();
       bspriteBatch.setShader( this.passthroughShader );
       uiActors[ 0 ].draw( bspriteBatch, 1f );
       bspriteBatch.end();      
       
       
       // Text banner    
     //  Actor textBannerTable = this.uiStage.getRoot().findActor( "textBanner" );     
     //  Actor bannerFg = this.uiStage.getRoot().findActor( "bannerFg" );
              
       
       shadow = drawToShadow( bspriteBatch, shadowBuff, uiActors, 1, 2 );
    
       bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        

       bspriteBatch.begin();       
       bspriteBatch.setShader( this.passthroughShader );
       bspriteBatch.draw( shadow, 10, -10, this.width, this.height ); 
       uiActors[ 1 ].draw( bspriteBatch, 1 );
       bspriteBatch.end();         
       
       
       // Draw shadow cast by banner foreground on lower banner
       shadow = drawToShadow( bspriteBatch, shadowBuff, uiActors, 2, 1 );
              
     
       bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        

       bspriteBatch.begin();       
       bspriteBatch.setShader( this.passthroughShader );
       bspriteBatch.draw( shadow, 7, -10, this.width, this.height ); // X displacement slightly less
       
       uiActors[ 2 ].draw( bspriteBatch,  1 );
        
       titleInput.draw( bspriteBatch, 1 );
       this.monster.draw( bspriteBatch,  1 );
      
       bspriteBatch.end();         
            
       
       
   
       
       
     
       **/
      
       if( screenBuff != null && this.nextScreenId != null ) {
            
            screenBuff.end();
            
            ScreenManager s = ScreenManager.getInstance();
             
            TextureRegion t = new TextureRegion(screenBuff.getColorBufferTexture() );
            t.flip( false, true );
            s.setLastScreenImg( t );        
            s.showScreen( this.nextScreenId );
        }        
    }
    
    
    
    
  
    @Override
    public void resize(int width, int height) {
        
        Gdx.app.log( TAG,  "resize..." );
        this.nextScreenId = null;
        this.width = width;
        this.height = height;
        
      //  setBackground();
        
        
        
        
        
        this.taskManager = new TaskSpriteManager(assetMgr);
        // Format.ALPHA not supported HTC Desire C ?
      //  this.shadowBuff = new FrameBuffer( Format.RGBA8888, (int)(this.width * 0.65 ), (int)(this.height * 0.65 ), false );
        
      //  this.screenBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );    
        
       
        float aspect = (float) width / (float) height;
      
        this.uiCamera = new OrthographicCamera( width, height );
        this.uiCamera.setToOrtho( false,  1.0f, 1.0f / aspect ); 
        this.uiCamera.update();
    
        this.uiStage.getViewport().update( width, height, true);
        
        
        
        PlayClickListener playClick = new PlayClickListener( this.effectMgr );
       
        
        // Set background to previous screen      
      Image k = fadeIn( this.uiStage );
       k.clearActions();
      
        
        
        
        
        
        this.monster = new MonsterSprite( this.skin.getSprite( "monster" ));

        this.uiStage.addActor( this.monster );
        
        // Space between icons in the picker elements
        float iconDmtr = this.width * 0.065f;   
      
      //  NinePatchScaler patchScaler = new NinePatchScaler( this.skin );
        this.colourSelectTable = new ColourPicker( this.iconSprites, this.skin, (int)Math.floor(this.width * 0.6f) , (int)Math.floor( this.height * 0.6f ), iconDmtr );
        this.taskSelectTable = new TaskPicker( this.skin, this.taskManager, iconDmtr, this.width * 0.7f ); //topTable.getWidth() );
        
        
          
        PickedListener p = new PickedListener();
        
        this.taskSelectTable.addListener( p );        
        this.colourSelectTable.addListener( p );        
    
        this.titleInput = new TextField( "New task name", this.skin, "new-task" );
        this.titleInput.setName( "titleInput" );      
        this.titleInput.addListener( new TextFocusListener() );
        this.titleInput.addListener( new TextInputListener() );
        
        
             // Save button
        this.save = new ImageTextButton( "Save", skin, "new-task-save");
        MkbScreen.layoutButton( this.save );
        // Save button is initially inactive
        this.save.setColor( 1, 1, 1, 0.25f );
        this.save.addListener( playClick );
        
        
        this.cancel = new ImageTextButton( "Cancel", skin, "new-task-cancel" );
        MkbScreen.layoutButton( this.cancel );
        this.cancel.addListener( playClick );
                
       
        this.save.addListener( new SaveButtonListener() );        
        this.cancel.addListener( new CancelButtonListener() );
     
       
        // Put everything into One Big Table        
       FormContainer c = new FormContainer(this.assetMgr, this.titleInput, this.taskSelectTable, this.colourSelectTable, this.save, this.cancel );
       c.setWidth( this.width * 0.60f );
       c.setX( this.monster.getWidth() );
       
      c.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn(0.5f)));
       
       this.uiStage.addActor( c );
       
      
        if( this.editTargetId >= 1 ) {
            populateForm();
        }
        
       
        
   
     }
    
    
    private void populateForm() {
    
        TaskManager mgr = TaskManager.getInstance();
   /** required    
    * Task t = mgr.getTaskById( this.editTargetId ); 
        
        if( t == null ) {
            Gdx.app.log( TAG, "No task found for " + this.editTargetId );
            return;
        }
      
        
        this.titleInput.setText( t.getDescription() );
      
        this.taskSelectTable.setCurrentTask( t );
     **/   
       
    }
   
   
    
    
    /**
     * Activate save button depending on state.
     * 
     */
    private void updateSaveButton() {
   
        float newAlpha = 0.25f;
        
        TaskSprite t = this.taskSelectTable.getSelectedTask();
   
        if( t != null && TaskSpriteManager.isComplete( t )) {           
            newAlpha = 1f;
        }
        
        this.save.addAction( Actions.alpha( newAlpha, FADE_DURATION ));  
    }

    
   

    @Override
    public void show() {
        
        Gdx.app.log( TAG,  "show...");
       this.nextScreenId = null;
        TaskManager mgr = TaskManager.getInstance();
        this.editTargetId = mgr.getEditTargetId();
        
        Gdx.app.log( TAG,  "Edit target:" +this.editTargetId );
        
        if( this.editTargetId >=1 ){
            mgr.clearEditTargetId();
        }
       
        this.uiStage =  new Stage(new ExtendViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(  this.uiStage );
                
     //   this.background = new Group();     
     //   this.background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  
     //   this.uiStage.addActor( this.background);

        this.taskSprites = this.assetMgr.get( "data/tasks.atlas", TextureAtlas.class );
        this.iconSprites = this.assetMgr.get( "data/icons.atlas", TextureAtlas.class );
        
        this.skin = assetMgr.get( "data/icons.json", Skin.class );
    }
    
    
    @Override
    public void dispose() {
        Gdx.app.log( TAG,  "dispose...");
        // TODO: fonts?
 //       this.taskSprites.dispose(); 
  //      this.iconSprites.dispose();        
        this.uiStage.dispose();   
     
    }
    
    // TODO: key-code 66: Done     


    /**
     * Listen for when a task or colour is picked.
     * @author mcgovern
     *
     */
    class PickedListener implements EventListener {

        @Override
        public boolean handle( Event event ) {

            if( event instanceof TaskPicker.PickedEvent ) {
   System.err.println( "NTS: Task Picked event: " + event ); 
                NewTaskScreen2.this.effectMgr.playClick();
                NewTaskScreen2.this.updateSaveButton();       
           
                return true;
                
            } else if( event instanceof ColourPicker.PickedEvent ) {
   System.err.println( "NTS: Color Picked event: " + event ); 
            	Task.IconColor newColor = ((ColourPicker.PickedEvent)event).getColour(); 
                NewTaskScreen2.this.updateSaveButton();
                NewTaskScreen2.this.effectMgr.playClick();                
               NewTaskScreen2.this.taskSelectTable.setSelectedColor( newColor );
                return true;
            }

            return false;
        }      
    }
    
   
  
    /**
     * Saves the currently selected task, and returns to overview screen
     * @author mcgovern
     *
     */
    class SaveButtonListener extends ChangeListener {
              
        
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
       
            TaskSprite ts = NewTaskScreen2.this.taskSelectTable.getSelectedTask();
            
           // TaskSprite newTask = NewTaskScreen.this.taskSelectTable.getSelectedTask();
                        
            if( TaskSpriteManager.isComplete( ts )) {
            	
                // Item doesn't have a position yet.
                // One will be assigned in the overview screen.
                ts.setPosition( -100000, -100000 );
               
                NewTaskScreen2.this.taskManager.save(ts);
            //	TaskManager mgr = TaskManager.getInstance(); 
//System.err.println( "NEW X:"+ newTask.getX() + " vs " + newTask.getTask().getPosX() )  ;
   //         	mgr.save( newTask );               
            } //*/
              
            NewTaskScreen2.this.setNextScreenId( MkbScreen.ScreenId.OVERVIEW_SCREEN );
         //   ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
        }
    }
  
   
    
    
    /**
     * Cancel button. Go back to overview screen.
     * Clear current edit target ID.
     * @author mcgovern
     *
     */
    class CancelButtonListener extends ChangeListener {
                      
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
                                   
            TaskManager.getInstance().clearEditTargetId();
            
                  NewTaskScreen2.this.setNextScreenId( MkbScreen.ScreenId.OVERVIEW_SCREEN );
                  //ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
        }
    }
    
    
    
    class TextFocusListener extends FocusListener {
  
        boolean dirty;
        
        @Override
        public void keyboardFocusChanged( FocusListener.FocusEvent event, Actor actor, boolean focused ) {
        
            TextField textField = (TextField)actor;
            
            if( !dirty ) {
                textField.setText( "" );
                this.dirty = true;
            } else {
                textField.setCursorPosition( textField.getText().length() );
            }
            
            NewTaskScreen2.this.effectMgr.playClick();
           // NewTaskScreen2.this.uiStage.getRoot().addAction( Actions.moveTo( 0, actor.getStage().getHeight() - actor.getY()- actor.getHeight(), 0.5f, Interpolation.exp10 ));
            NewTaskScreen2.this.updateSaveButton();
        }   
    }
    

    /**
     * Return form elements to original position on ENTER key.
     * @author mcgovern
     *
     */
    class TextInputListener extends InputListener {
   
        public boolean keyUp( InputEvent event, int keycode)    {
            
            NewTaskScreen2.this.updateTaskDescription();
            NewTaskScreen2.this.updateSaveButton();          
            
            if( keycode == Keys.ENTER ) { 
                NewTaskScreen2.this.uiStage.unfocusAll();
                NewTaskScreen2.this.uiStage.getRoot().addAction( Actions.moveTo( 0, 0, 0.5f, Interpolation.exp10 ));
                  
            }
            NewTaskScreen2.this.effectMgr.playClick();
            return super.keyUp(event, keycode);           
        }
    }
}
    



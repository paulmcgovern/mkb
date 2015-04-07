package ca.pmcgovern.mkb.screens;


import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.MonsterSprite;
import ca.pmcgovern.mkb.sprites.TaskSprite;
import ca.pmcgovern.mkb.sprites.TaskSpriteFactory;
import ca.pmcgovern.mkb.ui.ColourPicker;
import ca.pmcgovern.mkb.ui.NinePatchScaler;
import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.ui.TaskPicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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


public class NewTaskScreen extends MkbScreen {

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
   
    private Group background;
     
    
    private ImageTextButton save; 
            
    private ImageTextButton cancel;
    
    
    private TextField titleInput;
    
   
    public static final String TAG = "NewTaskScreen";
   
   
    public NewTaskScreen(AssetManager assetMgr, EffectManager effectMgr ) {
          
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
        this.background.clear();        
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
    
   /* 
    private void updateTaskIconLabel() {
        
        String newName = getTaskName();
      
        if( newName == null ) {
            return;            
        }
    
       this.selectedTaskName.setText( newName );
    }
    */
    
    
    // TODO: to super
    protected void setBackground( Batch spriteBatch ) {    
        
        spriteBatch.setShader( this.passthroughShader );            
        spriteBatch.begin();          
        spriteBatch.draw( this.assetMgr.get( "data/Yellow_notebook_paper.jpg", Texture.class ), 0, 0 );
        spriteBatch.end();
    }
    
    
    
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
         
    
   
    
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);       
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
      
        this.uiStage.act();
   
        // There is a single SpriteBatch, it happens
        // to be part of the task stage.        
        Batch bspriteBatch = this.uiStage.getBatch();
     
        
        // Background
        setBackground( bspriteBatch );
     
        Actor[] uiActors = new Actor[ 10 ];
        
        Actor titleInput = this.uiStage.getRoot().findActor( "titleInput" );           
        uiActors[ 0 ] = this.uiStage.getRoot().findActor( "container" );
        uiActors[ 1 ] = this.uiStage.getRoot().findActor( "textBanner" );
        uiActors[ 2 ] = this.uiStage.getRoot().findActor( "bannerFg" );
             
        TextureRegion shadow = drawToShadow( bspriteBatch, shadowBuff, uiActors, 0, 1 );
        
 
       bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        

       bspriteBatch.begin();    
       bspriteBatch.setShader( this.passthroughShader );
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
            
       
       
   
       
       
     //  Table.drawDebug( this.uiStage );
       
    //   this.uiStage.draw();       
       
        
    }
    
    
    
    
  
    @Override
    public void resize(int width, int height) {
        
        Gdx.app.log( TAG,  "resize..." );

        
      
        this.width = width;
        this.height = height;

        // Format.ALPHA not supported HTC Desire C ?
        this.shadowBuff = new FrameBuffer( Format.RGBA8888, (int)(this.width * 0.65 ), (int)(this.height * 0.65 ), false );
        
        this.screenBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );    
        
       
        float aspect = (float) width / (float) height;
      
        this.uiCamera = new OrthographicCamera( width, height );
        this.uiCamera.setToOrtho( false,  1.0f, 1.0f / aspect ); 
        this.uiCamera.update();
    
        this.uiStage.getViewport().update( width, height, true);
        
        
        this.background.setBounds( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );       
    
    
     //   this.selectedTaskName = new Label( "WORKS!", skin );
        
         PlayClickListener playClick = new PlayClickListener( this.effectMgr );
       
        
        this.monster = new MonsterSprite( this.skin.getSprite( "monster" ));
        
        float scaledMonsterHeight = this.height * 0.2f;  
        float scaledMonsterWidth  = this.width * 0.11f;  
        
        float scale = 0;
        
        if( this.monster.getHeight() > scaledMonsterHeight ) {   
            scale = -1 * ( 1 - (scaledMonsterHeight /  this.monster.getHeight()));
        } else {
            scale = 1 - this.monster.getHeight() / scaledMonsterHeight;           
        }
        
        this.monster.scaleBy( scale );  
        this.uiStage.addActor( this.monster );
        
        
         
        NinePatchScaler patchScaler = new NinePatchScaler( this.skin );
        
       
        // Put everything into One Big Table        
        Table container = new Table();           
            
        container.setFillParent( true );
        container.setName( "container" );     
        this.uiStage.addActor( container );
        
      
     
        
        float leftGutter =  this.width * 0.114f;
        float rightGutter = this.width * 0.1f;
        float iconDmtr = this.width * 0.065f;   
        
       
        Table topTable = new Table();
        
        topTable.setBackground( patchScaler.getScaledPatch( 0.65f, "speech-top") );
        
        topTable.setX( scaledMonsterWidth );
        topTable.setY( scaledMonsterHeight );        
        topTable.setWidth( this.width - topTable.getX() );
        topTable.setHeight( this.height - topTable.getY() );
        
        
        // No listener will be attached to task sprites
        this.taskSelectTable = new TaskPicker( this.skin, new TaskSpriteFactory( this.taskSprites ), iconDmtr, topTable.getWidth() );
     
        this.taskSelectTable.setX( 0f );
        topTable.add( this.taskSelectTable ).padTop( iconDmtr * 0.8f ).padBottom( iconDmtr * 0.53f ).center();
        
      
        Table middleTable = new Table();
     //   middleTable.debug();

        this.skin.getDrawable( "speech-middle");
        middleTable.setBackground( patchScaler.getScaledPatch( 0.65f, "speech-middle" ));
        
        this.colourSelectTable = new ColourPicker( this.iconSprites, this.skin, (int)Math.floor(this.width * 0.6f) , (int)Math.floor( this.height * 0.6f ), iconDmtr );
 
        middleTable.add( this.colourSelectTable ).padTop( iconDmtr * 0.43f );
        middleTable.row();

        Table buttonTable = new Table();
         
        // Save button
        this.save = new ImageTextButton( "Save", this.skin, "new-task-save");
        MkbScreen.layoutButton( this.save );

        
        // Save button is initially inactive
        this.save.setColor( 1, 1, 1, 0.25f );
        
     
        this.cancel = new ImageTextButton( "Cancel", this.skin, "new-task-cancel" );
        MkbScreen.layoutButton( this.cancel );
     
        
        
        buttonTable.add( this.save ).padRight( this.height * 0.07f );           
        buttonTable.add( this.cancel ).padLeft( this.height * 0.07f );;
   
        middleTable.add( buttonTable ).center();  
   
        // Bottom of speech-bubble thingy.
        Table bottom = new Table();     
        // TODO: calc 9patch scale 
        bottom.setBackground( new NinePatchDrawable( patchScaler.getScaledPatch( 0.65f, "speech-bottom")));

  
        

        // Add left-hand spacing        
        container.add( new Container() ).padLeft( leftGutter );
        container.add( topTable ); 
        container.add( new Container() ).padRight( rightGutter );   
                
        container.row(); 
        

        // Add left-hand spacing        
        container.add( new Container() ).padLeft( leftGutter );
        container.add( middleTable ).fill(); 
        container.add( new Container() ).padRight( rightGutter );   
        
        container.row();

        // Add left-hand spacing     
        container.add( new Container() ).padLeft( leftGutter );
        container.add( bottom ).fillX();
        container.add( new Container() ).padRight( rightGutter );   
        
        container.validate();
        
        // Text input/banner thingy
        Table textBannerTable = new Table(); 
      //  textBannerTable.debug();
        textBannerTable.setName( "textBanner" );
        textBannerTable.setWidth( this.width * 0.46f ); 
        textBannerTable.setHeight( this.height * 0.06f ); 
        
        Image bannerLeft = new Image( this.skin.getDrawable( "banner-left" ));
        Image bannerMiddle = new Image( this.skin.getDrawable( "banner-middle" ));
        Image bannerRight = new Image( this.skin.getDrawable( "banner-right" ));
        Image bannerFg = new Image( this.skin.getDrawable( "ltblue_bg" ));
        
        
        
     //   bannerFg.setWidth( this.height * 0.31f );
     //   bannerFg.setName( "bannerFg" );
     
        
        textBannerTable.add( bannerLeft );
        
         
        textBannerTable.add( bannerMiddle ).expand().fill();
        textBannerTable.add( bannerRight );
        
        textBannerTable.setY( topTable.getTop() - (topTable.getPadTop() / 2 )- ( textBannerTable.getHeight() / 2) );
        textBannerTable.setX((this.width - textBannerTable.getWidth()) / 2 );
           
        this.uiStage.addActor( textBannerTable );
                
        container.validate();
        
        Container bannerFgHolder = new Container( bannerFg );
        bannerFgHolder.setName( "bannerFg" );
        bannerFgHolder.fill();        
        bannerFgHolder.setWidth( this.width * 0.31f );
        bannerFgHolder.setHeight( this.height * 0.09f );
        
        bannerFgHolder.setX( (this.width - bannerFgHolder.getWidth()) / 2f );
        bannerFgHolder.setY( textBannerTable.getY() - (bannerFgHolder.getHeight() - textBannerTable.getHeight() / 2f) + (bannerFgHolder.getHeight() /2f));
        
        this.uiStage.addActor( bannerFgHolder );
   
        
   
        this.titleInput = new TextField( "New task name", this.skin, "new-task" );
        titleInput.setName( "titleInput" );
        titleInput.setWidth( bannerFgHolder.getWidth() );
        titleInput.setHeight( bannerFgHolder.getHeight() );
        titleInput.setPosition( bannerFgHolder.getX() + 1, bannerFgHolder.getY() );
        
        this.uiStage.addActor( titleInput );
      
        
        this.titleInput.addListener( new TextFocusListener() );
        this.titleInput.addListener( new TextInputListener() );
        
        PickedListener p = new PickedListener();
        
        this.taskSelectTable.addListener( p );        
        this.colourSelectTable.addListener( p );        
    
        this.save.addListener( playClick );  
        this.save.addListener( new SaveButtonListener() );
        this.cancel.addListener( playClick );
        this.cancel.addListener( new CancelButtonListener() );
     /**   
        TextField detailText = new TextField("Default text", this.skin );
        detailText.setName( "text" );
        detailText.addListener( new TextFocusListener());   
        detailText.addListener( new TextInputListener());     
               
        container.add( detailText ).colspan( 2 );
        container.row();
        
        Table buttonsTable = new Table( skin );
   //     buttonsTable.setBackground( "default-pane" );
    
        // Save button
        Button btn =  buildActionButton( "Save", skin ); // TODO: icon over
        btn.setName( "save" );
        btn.addListener( new SaveButtonListener() );
   
    //    btn.setVisible( false );
        
        buttonsTable.add( btn );
        
        // Cancel button
        // Go back to Overview screen.
        btn =  buildActionButton( "Cancel", skin );  // TODO: icon over
        btn.addListener( new ChangeListener() {
            public void changed( ChangeEvent event, Actor actor ) {
                ScreenManager.getInstance().showScreen( MkbScreen.OVERVIEW_SCREEN );
            }} );
        
        btn.addListener( new ClickListener() {
         
            public void clicked(InputEvent event, float x, float y) {
                System.err.println( "Clicked: " + event );
            }
        });
        
    
        
        buttonsTable.add( btn ); 
        
        buttonsTable.pack();
        
       // btn.setZIndex( 10000); 
        container.add( buttonsTable ).colspan( 2 );    
        this.updateSaveButton();
        */
        
        
       
        if( this.editTargetId >= 1 ) {
            populateForm();
        }
    }
    
    
    private void populateForm() {
    
        TaskManager mgr = TaskManager.getInstance();
        Task t = mgr.getTaskById( this.editTargetId );
        
        if( t == null ) {
            Gdx.app.log( TAG, "No task found for " + this.editTargetId );
            return;
        }
        
        
        this.titleInput.setText( t.getDescription() );
        
        this.taskSelectTable.setCurrentTask( t );
        
       
    }
   
   
    
    
    /**
     * Activate save button depending on state.
     * 
     */
    private void updateSaveButton() {
   
        float newAlpha = 0.25f;
        
        TaskSprite t = this.taskSelectTable.getSelectedTask();
    
        if( t != null && TaskManager.isComplete( t )) {           
            newAlpha = 1f;
        }
        
        this.save.addAction( Actions.alpha( newAlpha, FADE_DURATION ));  
    }

    
   

    @Override
    public void show() {
        
        Gdx.app.log( TAG,  "show...");
       
        TaskManager mgr = TaskManager.getInstance();
        this.editTargetId = mgr.getEditTargetId();
        
        Gdx.app.log( TAG,  "Edit target:" +this.editTargetId );
        
        if( this.editTargetId >=1 ){
            mgr.clearEditTargetId();
        }
       
        this.uiStage =  new Stage(new ExtendViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(  this.uiStage );
                
        this.background = new Group();     
        this.background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  
        this.uiStage.addActor( this.background);

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
 
                NewTaskScreen.this.effectMgr.playClick();
                NewTaskScreen.this.updateSaveButton();       
           
                return true;
                
            } else if( event instanceof ColourPicker.PickedEvent ) {

            	Task.IconColor newColor = ((ColourPicker.PickedEvent)event).getColour(); 
                NewTaskScreen.this.updateSaveButton();
                NewTaskScreen.this.effectMgr.playClick();                
                NewTaskScreen.this.taskSelectTable.setSelectedColor( newColor );
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
       
            TaskSprite newTask = NewTaskScreen.this.taskSelectTable.getSelectedTask();
                                   
            if( TaskManager.isComplete( newTask )) {
            	
            	TaskManager mgr = TaskManager.getInstance();            	
            	mgr.save( newTask );               
            }
                 
            ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
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
            
            ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
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
            
            NewTaskScreen.this.effectMgr.playClick();
            NewTaskScreen.this.uiStage.getRoot().addAction( Actions.moveTo( 0, actor.getStage().getHeight() - actor.getY()- actor.getHeight(), 0.5f, Interpolation.exp10 ));
            NewTaskScreen.this.updateSaveButton();
        }   
    }
    

    /**
     * Return form elements to original position on ENTER key.
     * @author mcgovern
     *
     */
    class TextInputListener extends InputListener {
   
        public boolean keyUp( InputEvent event, int keycode)    {
            
            NewTaskScreen.this.updateTaskDescription();
            NewTaskScreen.this.updateSaveButton();          
            
            if( keycode == Keys.ENTER ) { 
                NewTaskScreen.this.uiStage.unfocusAll();
                NewTaskScreen.this.uiStage.getRoot().addAction( Actions.moveTo( 0, 0, 0.5f, Interpolation.exp10 ));
                  
            }
            NewTaskScreen.this.effectMgr.playClick();
            return super.keyUp(event, keycode);           
        }
    }
}
    



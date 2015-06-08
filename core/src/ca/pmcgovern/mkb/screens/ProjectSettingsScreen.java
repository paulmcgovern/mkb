package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.ConfirmClearAllWindow;
import ca.pmcgovern.mkb.menus.MkbMenu;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.sprites.EffectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ProjectSettingsScreen extends MkbScreen {

    public static final String TAG = "SettingsScreen";
    private Stage uiStage;
   
    private ShapeRenderer shapeRenderer;
    
    private Skin skin;
  
    private OrthographicCamera uiCamera;

    private ShaderProgram passthroughShader ;

    Color top;
    Color bottom;
    TaskSpriteManager taskMgr;
    
    public ProjectSettingsScreen(AssetManager assetMgr,EffectManager effectMgr ) {
          
        super( assetMgr, effectMgr );
        
          // Do-nothing shader
        this.passthroughShader = new ShaderProgram(Gdx.files.internal("data/shaders/passthrough.vert"),  Gdx.files.internal("data/shaders/passthrough.frag"));
       
        this.shapeRenderer = new ShapeRenderer();
        top = new Color( 0.7f,0,0.7f, 1 );
        bottom =  new Color( 0.25f, 0, 0.25f, 1 );

    
       
    }

    
    @Override 
    public MkbScreen.ScreenId getId() {
        return MkbScreen.ScreenId.SETTINGS;
    }
    
    @Override
    public void render(float delta) {
     
                 
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
       
        
        FrameBuffer screenBuff = null;
        
        if( this.nextScreenId != null ) {
          
            screenBuff = new FrameBuffer( Pixmap.Format.RGBA8888, this.width, this.height, false );  
            screenBuff.begin();
            
        }
        
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
       
        // Background
        // TODO: scale up a 2x2 texture
        this.shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );
        this.shapeRenderer.rect( 0, 0, this.width, this.height, bottom, bottom, top, top );          
        this.shapeRenderer.end();
     
          
        
        
        Batch bspriteBatch = this.uiStage.getBatch();
        bspriteBatch.begin();
        bspriteBatch.setShader( this.passthroughShader );
          
       // drawBackground( bspriteBatch );
        
       
        
       // fontBig.draw( bspriteBatch,  "Homework Monster",  fontBig.getSpaceWidth(), this.height - (4f * fontBig.getAscent() ) );
       // fontBig.setColor(1, .5f, .25f, 1);
        
    
        bspriteBatch.end();
     
        this.uiStage.act();       
        this.uiStage.draw();  
       
        
        if( screenBuff != null ) {
            
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

        Gdx.app.log( TAG,  "resize...");
       
        this.taskMgr = new TaskSpriteManager( this.assetMgr );
        this.uiStage.clear();
       
       
        
        this.nextScreenId = null;
        this.width = width;
        this.height = height;
        
        // Set VP to fill screen.
        this.uiStage.getViewport().update( width, height, true);
   
        PlayClickListener playClick = new PlayClickListener( this.effectMgr );
        
        Gdx.input.setInputProcessor(this.uiStage);
     /**   
 Table t = new Table();
        t.add( new Container() ).width(10).height(10);
        t.debug();
        t.setX( 200 );
        t.setY( 200 );
        this.uiStage.addActor( t );
      **/  
     
        Table contentTable = new Table( this.skin );
  
        
        ButtonGroup langRadioGrp = new ButtonGroup();
       
        langRadioGrp.setMaxCheckCount(1);
        langRadioGrp.setMinCheckCount(1);        
        langRadioGrp.setUncheckLast( true );
       
        
        TextButton enButton = new TextButton( "English", skin, "toggleLang");   
        enButton.setName( "EN" );
        enButton.addListener(playClick);
        
        TextButton frButton = new TextButton( "Fran�ais", skin, "toggleLang");
        frButton.setName( "FR" );
        frButton.addListener(playClick);
        
        langRadioGrp.add( enButton );
        langRadioGrp.add( frButton );
      
                  
        final Label text2 = new Label( "Settings", skin, "huge");
       
        text2.setWrap(true);
        contentTable.add( text2 ).colspan( 3 ).align( Align.left );
        contentTable.row();
        
        contentTable.add( new Label( "Language:", skin )).pad( 10 ).align( Align.left );
        contentTable.add( enButton ).pad( 10 );
        contentTable.add( frButton ).pad( 10 );
        
        contentTable.row();
  
        ImageTextButton clearAll = new ImageTextButton( "Clear All", skin, "settings-clear" );        
        MkbScreen.layoutButton( clearAll );
        
        clearAll.addListener( playClick );
        clearAll.addListener( new ClearAllListener( taskMgr.getTaskCount() ) );
        contentTable.add( clearAll ).colspan( 3 ).pad( 10 );
        
        contentTable.row();
        
        
        ImageTextButton ok = new ImageTextButton( "OK", skin, "settings-done" );
        MkbScreen.layoutButton( ok );  
        
        ImageTextButton cancel = new ImageTextButton( "Cancel", skin, "settings-cancel" );
        MkbScreen.layoutButton( cancel );  
         
        ok.addListener(playClick);
        ok.addListener( new SaveSettingsListener() );
        ok.addListener( new ChangeScreenListener() );
        // TODO: save new settings.
        
        cancel.addListener( playClick );
        cancel.addListener(  new ChangeScreenListener() );      
        
        Table buttonTable = new Table();
   
        buttonTable.add( ok ).pad( 10 );
        buttonTable.add( cancel ).pad( 10 );  
      
        contentTable.row();
        contentTable.add( buttonTable ).colspan( 3 ).align(Align.center);
      
        
       
        
        final ScrollPane scroller = new ScrollPane( contentTable );

        final Table table = new Table();
       
        table.setFillParent(true);
     
        table.add(scroller).fill().expand();
        this.setBackground();    
    
        this.uiStage.addActor(table);

        fadeIn( this.uiStage );
            
    }
    
    

    @Override
    public void show() {

        Gdx.app.log( TAG, "show..." );
        

        float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        ExtendViewport uiViewport = new ExtendViewport( UI_WORLD_HEIGHT * aspectRatio, UI_WORLD_HEIGHT );
     
        this.uiStage = new Stage( uiViewport );
           
        this.skin = this.assetMgr.get( "data/icons.json", Skin.class );        
        
        this.nextScreenId = null;
        
      
        
       // this.spriteBatch = new SpriteBatch();
     //   this.taskStage.getBatch().setShader( this.shader );
      //  this.spriteBatch.setShader( this.shader );

     //  this.gsdt = new GestDtct();
     //   GestureDetector gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, gsdt);
      
     
     //   Gdx.input.setInputProcessor( new InputMultiplexer( this.uiStage, this.taskStage, gestureDetector ));
     
      
    }
    

   
    
    @Override
    public void setOpenMenu( MkbMenu w ) {        
    }
  
    @Override
    public void clearOpenMenu() {        
    }
    
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "Dispose..." );
        if( this.uiStage != null )        {
            this.uiStage.dispose();        
        }
    }

    class SaveSettingsListener extends ChangeListener {    

        @Override
        public void changed( ChangeEvent event, Actor actor ) { 
           // Find current selected language
            
            Button b = (Button)ProjectSettingsScreen.this.uiStage.getRoot().findActor( "FR" );
            System.err.println( "FR:" + b.isChecked() );
        }
    }
    
    class ClearAllListener extends ChangeListener {        
         
        private int taskCount;
        
        public ClearAllListener( int taskCount ) {
            super();
            this.taskCount = taskCount;
        }
        
        @Override
        public void changed( ChangeEvent event, Actor actor ) { 
            ConfirmClearAllWindow win = new ConfirmClearAllWindow( "Clear All", ProjectSettingsScreen.this.assetMgr, this.taskCount, new InvokeClearListener() );
            
            win.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn( FADE_DURATION )));
           
            float x = ProjectSettingsScreen.this.uiStage.getViewport().getWorldWidth() / 2;
            x -= win.getWidth() / 2;
            
            float y = ProjectSettingsScreen.this.uiStage.getViewport().getWorldWidth() / 2;
            y -= win.getHeight() / 2;
            
            win.setX( x );
            win.setY( y );
            
            ProjectSettingsScreen.this.uiStage.addActor( win );
        }
    }      
    
    
    class InvokeClearListener extends ChangeListener {
 
        @Override
        public void changed( ChangeEvent event, Actor actor ) { 
        
            if( !ConfirmClearAllWindow.CLEAR_ALL_CONFIRM.equals( actor.getName())) {
                return;
            }
          
            int initialCount = ProjectSettingsScreen.this.taskMgr.getTaskCount();
            
            int deletedCount = ProjectSettingsScreen.this.taskMgr.deleteAll();
            
            if( deletedCount != initialCount ) {
                Gdx.app.error( TAG, "Failed to delete all tasks: " +initialCount + " vs. " + deletedCount );
            }
            
              // Change button to incative...
            
        }
    }
    
    
    class ChangeScreenListener extends ChangeListener{          
            
        @Override
        public void changed(ChangeEvent event, Actor actor) {     
            ProjectSettingsScreen.this.nextScreenId = MkbScreen.ScreenId.OVERVIEW_SCREEN;                   
        }
    }   
}

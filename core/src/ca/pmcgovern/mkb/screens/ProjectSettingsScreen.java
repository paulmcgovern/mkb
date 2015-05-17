package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.menus.MkbMenu;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.sprites.EffectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ProjectSettingsScreen extends MkbScreen {

    public static final String TAG = "SettingsScreen";
    private Stage uiStage;
   
    private Skin skin;
  
    private OrthographicCamera uiCamera;

    private ShaderProgram passthroughShader ;

   
    public ProjectSettingsScreen(AssetManager assetMgr,EffectManager effectMgr ) {
          
        super( assetMgr, effectMgr );
        
          // Do-nothing shader
        this.passthroughShader = new ShaderProgram(Gdx.files.internal("data/shaders/passthrough.vert"),  Gdx.files.internal("data/shaders/passthrough.frag"));
       
    }

    
    @Override 
    public MkbScreen.ScreenId getId() {
        return MkbScreen.ScreenId.HELP;
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
        
        Batch bspriteBatch = this.uiStage.getBatch();
        bspriteBatch.begin();
        bspriteBatch.setShader( this.passthroughShader );
          
        drawBackground( bspriteBatch );
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
       

        this.nextScreenId = null;
        this.width = width;
        this.height = height;

        
           
                
        this.uiStage.getViewport().update(width, height, true);
        
        
        
        PlayClickListener playClick = new PlayClickListener( this.effectMgr );
        
        Gdx.input.setInputProcessor(this.uiStage);
        
        
        ButtonGroup langRadioGrp = new ButtonGroup();
     
        langRadioGrp.setMaxCheckCount(1);
        langRadioGrp.setMinCheckCount(0);        
        langRadioGrp.setUncheckLast( true );
        
        
        TextButton enButton = new TextButton( "English", skin, "toggle");
        enButton.addListener(playClick);
        TextButton frButton = new TextButton( "Fran�ais", skin, "toggle");
        frButton.addListener(playClick);
        
        langRadioGrp.add( enButton );
        langRadioGrp.add( frButton );
      
        Table contentTable = new Table( this.skin );
                  
        final Label text2 = new Label( "Settings", skin);
  //      text2.setAlignment(Align.center);        
        text2.setWrap(true);
        contentTable.add( text2 ).colspan( 2 );
        contentTable.row();
        
        contentTable.add( enButton );
        contentTable.add( frButton );
        
        
        contentTable.row();
  
        ImageTextButton ok = new ImageTextButton( "OK", skin, "help-done" );
        MkbScreen.layoutButton( ok );  
        
        ImageTextButton cancel = new ImageTextButton( "Cancel", skin, "help-done" );
        MkbScreen.layoutButton( cancel );  
         
        ok.addListener(playClick);
        
        // TODO: save new settings.
        
        cancel.addListener( playClick );
        cancel.addListener( new ChangeListener() {          
            
                @Override
                public void changed(ChangeEvent event, Actor actor) {     
                    ProjectSettingsScreen.this.nextScreenId = MkbScreen.ScreenId.OVERVIEW_SCREEN;                   
                }
            } );        
        
        contentTable.add( ok );
        contentTable.add( cancel );
        contentTable.row();
        
       
        
        final ScrollPane scroller = new ScrollPane( contentTable );

        final Table table = new Table();
       
        table.setFillParent(true);
       // table.setColor(1,1,1,0);
        table.add(scroller).fill().expand();
        this.setBackground();    
      //  table.setBackground( new TextureRegionDrawable( new TextureRegion( this.bg )));

        this.uiStage.addActor(table);

        fadeIn( this.uiStage );
            
    }
    
    

    @Override
    public void show() {

        Gdx.app.log( TAG, "show..." );

        this.uiStage = new Stage(new ExtendViewport(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight() ));        
      
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
        this.uiStage.dispose();        
    }

    
    
}

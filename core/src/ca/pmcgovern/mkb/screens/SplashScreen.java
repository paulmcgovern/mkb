package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.GameMain;
import ca.pmcgovern.mkb.GamePreferences;
import java.util.HashMap;
import java.util.Map;

import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.ui.LoadingBar;
import ca.pmcgovern.mkb.ui.MagicSkinLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.Locale;


/**
 * Splash screen. Load resources.
 * @author mcgovern
 *
 */
public class SplashScreen extends MkbScreen {

    private ShapeRenderer shapeRenderer;
    
    private Stage stage;
    
    private static String TAG = "Splash";
    
  //  private Batch spriteBatch;
    private LoadingBar bar;
    
    /** Minimum amount of time to display this screen. */
    public static final float MIN_DELAY_SEC = 0.3f;
    
    /** Total time elapsed since first render. */
    private float totalElapsedSec;
    
    
    private Map<String,BitmapFont> fontsByName;
  
    private Color top;
    private Color bottom;
    
    public SplashScreen( AssetManager assetMgr, EffectManager effectMgr ) {

        super( assetMgr, effectMgr );    
              
        GamePreferences prefs = GamePreferences.getInstance(); 
        GameMain.loadStrings( "fr" );// prefs.getLanguage() );
        
        this.assetMgr.load( "data/tile2.png", Texture.class );     
        this.assetMgr.load( "data/tasks.atlas",  TextureAtlas.class );
      //  this.assetMgr.load( "data/uiskin.atlas", TextureAtlas.class );
      //  this.assetMgr.load( "data/uiskin.json", Skin.class );
        this.assetMgr.load( "data/icons.atlas",  TextureAtlas.class );
        this.assetMgr.load( "data/icons.json", Skin.class );        
        this.assetMgr.load( "data/colours.pack",  TextureAtlas.class ); 
      //  this.assetMgr.load( "data/Yellow_notebook_paper.jpg", Texture.class );
        this.assetMgr.load( "data/lined_paper.png", Texture.class );
        this.assetMgr.load( "data/extents.png", Texture.class );
        this.assetMgr.load( "data/tables.pack", TextureAtlas.class );
        this.assetMgr.load( "data/sounds/clang.mp3", Sound.class );
        this.assetMgr.load( "data/sounds/click.mp3", Sound.class );
        this.assetMgr.load( "data/done.png", Texture.class  );
      //  this.assetMgr.load( "data/sounds/The Typewriter-0-Anderson_The_Typewriter-2000-6835.mp3", Sound.class );
        top = new Color( 0.7f,0,0.7f, 1 );
        bottom =  new Color( 0.25f, 0, 0.25f, 1 );

    }
    
    
    @Override 
    public MkbScreen.ScreenId getId() {
        return ScreenId.SPLASH_SCREEN;     
    }
    
    
    @Override
    public void setOpenMenu( MkbMenu w ) {        
    }
  
    @Override
    public void clearOpenMenu() {        
    }  
    
    @Override
    public void show() {
       
        Gdx.app.log( TAG, "Show..." ); 
        
        this.fontsByName = initFonts();             
        SkinLoader m = new MagicSkinLoader( new InternalFileHandleResolver(), this.fontsByName );
        this.assetMgr.setLoader( Skin.class, m );
        
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage();     
       // this.spriteBatch = new SpriteBatch();
       
    }
    
    
    @Override
    public void render(float delta) { 
        
        this.totalElapsedSec += Gdx.graphics.getDeltaTime();
    
        if( this.screenBuff != null ) {
            System.err.println( "Start sceen cap");
            this.screenBuff.begin();
             
        }
        
        
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
       
        // Background
        // TODO: scale up a 2x2 texture
        this.shapeRenderer.begin( ShapeType.Filled );
        this.shapeRenderer.rect( 0, 0, this.width, this.height, bottom, bottom, top, top );          
        this.shapeRenderer.end();
       
        // Title  
       
        Batch spriteBatch = this.stage.getBatch();
        
        spriteBatch.begin();
        BitmapFont fontBig = this.fontsByName.get( "big-font" );
        BitmapFont fontTiny = this.fontsByName.get( "tiny-font" );     
        
        fontBig.draw( spriteBatch, "Homework Monster", fontBig.getSpaceWidth(), this.height - (4f * fontBig.getAscent() ) );
        fontBig.setColor(1, .5f, .25f, 1);
     
         fontBig.draw( spriteBatch,  Gdx.graphics.getHeight() + " " + Gdx.graphics.getWidth() + " " + Gdx.graphics.getDensity() + " " + Gdx.graphics.getPpiX(),  fontBig.getSpaceWidth(), this.height - (8f * fontBig.getAscent() ) );
        fontBig.setColor(1, .5f, .25f, 1);
     
        
        
        fontTiny.draw( spriteBatch, GameMain.getString("LOADING"), this.bar.getX(), this.bar.getY() - (1.5f * fontTiny.getAscent())  );
        spriteBatch.end();
        
        // Progress bar
        this.stage.act( delta );
        this.stage.draw(); 
       
        
        if( this.screenBuff != null ) {            
          
            this.screenBuff.end();
          
            TextureRegion t = new TextureRegion( this.screenBuff.getColorBufferTexture() );
            t.flip( false, true );
            ScreenManager scrMgr = ScreenManager.getInstance();
            scrMgr.setLastScreenImg( t );
            ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
            
            //this.screenBuff.dispose();
        }
        
        if( this.assetMgr.getProgress() < 1 ) {
            
            this.assetMgr.update();
    
        } else {
             
            if( totalElapsedSec >  MIN_DELAY_SEC ) {  
              
                 this.screenBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
     
           
               //  ScreenManager.getInstance().showScreen( MkbScreen.NEW_SCREEN );
            }
        }
    }
 
    private FrameBuffer screenBuff;
    
    
    
    @Override
    public void resize(int width, int height) {

        this.width = width;
        this.height = height;
    
        this.stage.getViewport().update( (int)this.width, (int)this.height, false );
       
        this.bar = new LoadingBar( this.assetMgr, this.width * 0.25f, this.height / 10, new Color( 255, 102, 0, 1) );      
        this.stage.addActor( this.bar ); 
        this.bar.setPosition( this.width / 10, (this.height / 4) + 2 );     
    }
    

    /**
     * The app never returns to the splash screen so stage and batches are disposed here.
     */
    @Override
    public void hide() {

        Gdx.app.log( TAG, "Hide..." ); 
        this.fontsByName = null;
        this.stage.dispose();       
        this.shapeRenderer.dispose();
       
    }


    @Override
    public void dispose() {
        Gdx.app.log( TAG, "Dispose..." );     
        this.screenBuff.dispose();
            
    }

    
    
    
    
    protected Map<String,BitmapFont>  initFonts() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Days.ttf"));

        Map<String,BitmapFont> fontsByName = new HashMap<String,BitmapFont>();
        FreeTypeFontParameter p = new FreeTypeFontParameter();
        p.kerning = true;
        p.genMipMaps = true;
        p.minFilter = Texture.TextureFilter.MipMapNearestNearest;
        p.magFilter = Texture.TextureFilter.MipMapLinearLinear;
       
        p.size = (int)Math.floor( UI_WORLD_HEIGHT / 13 );
        fontsByName.put( "huge-font", generator.generateFont( p ));
        p.size = (int)Math.floor( UI_WORLD_HEIGHT / 14 );
        fontsByName.put( "big-font", generator.generateFont( p ));
        p.size = (int)Math.floor( UI_WORLD_HEIGHT / 15 );
        fontsByName.put( "small-font", generator.generateFont( p ));
        p.size = (int)Math.floor( UI_WORLD_HEIGHT / 16 );    
        fontsByName.put( "tiny-font", generator.generateFont( p ));
        
        generator.dispose();
        
        return fontsByName;        
    }
  
}

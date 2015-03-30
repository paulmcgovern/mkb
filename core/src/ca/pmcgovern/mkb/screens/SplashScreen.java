package ca.pmcgovern.mkb.screens;

import java.util.HashMap;
import java.util.Map;

import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.ui.LoadingBar;
import ca.pmcgovern.mkb.ui.MagicSkinLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;


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
    
    
    private float width;
    private float height;
    
    private Map<String,BitmapFont> fontsByName;
  
    public SplashScreen( AssetManager assetMgr ) {
        
        super( assetMgr );
      
        this.assetMgr.load( "data/tile2.png", Texture.class );     
        this.assetMgr.load( "data/tasks.atlas",  TextureAtlas.class );
        this.assetMgr.load( "data/uiskin.atlas", TextureAtlas.class );
        this.assetMgr.load( "data/uiskin.json", Skin.class );
        this.assetMgr.load( "data/icons.atlas",  TextureAtlas.class );
        this.assetMgr.load( "data/icons.json", Skin.class );        
        this.assetMgr.load( "data/colours.pack",  TextureAtlas.class ); 
        this.assetMgr.load( "data/Yellow_notebook_paper.jpg", Texture.class );
        this.assetMgr.load( "data/tables.pack", TextureAtlas.class );
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
        this.bar = new LoadingBar( this.assetMgr );      
        this.stage.addActor( this.bar ); 
    }
    
    
    @Override
    public void render(float delta) { 
        
        this.totalElapsedSec += Gdx.graphics.getDeltaTime();
    
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
        Color top = Color.LIGHT_GRAY;
        Color bottom = Color.DARK_GRAY;

        // Background
        // TODO: scale up a 2x2 texture
        this.shapeRenderer.begin( ShapeType.Filled );
        this.shapeRenderer.rect( 0, 0, this.width, this.height, bottom, bottom, top, top );          
        this.shapeRenderer.end();
       
        // Title  
       
        Batch spriteBatch = this.stage.getBatch();
        
        spriteBatch.begin();
        BitmapFont fontBig = this.fontsByName.get( "big-font" );
        BitmapFont fontSmall = this.fontsByName.get( "small-font" );     
        
        fontBig.draw( spriteBatch,  "Lorem ipsum",  10,  150 );
        fontBig.setColor(1, .5f, .25f, 1);
        fontSmall.draw( spriteBatch,  "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",  10,  100 );
        spriteBatch.end();
        
        // Progress bar
        this.stage.act( delta );
        this.stage.draw(); 
        
        if( this.assetMgr.getProgress() < 1 ) {
            
            this.assetMgr.update();
    
        } else {
             
            if( totalElapsedSec >  MIN_DELAY_SEC ) {  
                        
                ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
               //  ScreenManager.getInstance().showScreen( MkbScreen.NEW_SCREEN );
            }
        }
    }
 
 
    @Override
    public void resize(int width, int height) {

        Gdx.app.log( TAG, "Resize... "  + width + " " + height ); 
   
        this.width = width;
        this.height = height;
    
        this.stage.getViewport().update( (int)this.width, (int)this.height, false );
       
        this.bar.setPosition( 100, (this.height / 3) + 2 );     
    }
    

    /**
     * The app never returns to the splash screen so stage and batches are disposed here.
     */
    @Override
    public void hide() {

        Gdx.app.log( TAG, "Hide..." ); 
        this.fontsByName = null;
      //  this.spriteBatch.dispose();
        this.stage.dispose();       
        this.shapeRenderer.dispose();
    }


    @Override
    public void dispose() {
        
        Gdx.app.log( TAG, "Dispose..." );            
    }

    
    
    
    
    protected Map<String,BitmapFont>  initFonts() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Days.ttf"));

        Map<String,BitmapFont> fontsByName = new HashMap<String,BitmapFont>();
        FreeTypeFontParameter p = new FreeTypeFontParameter();

        float ppi = Gdx.graphics.getPpiY();        
        
        System.err.println( "PPI:" + ppi );
        
        p.size = Math.round( ppi / 2);
        fontsByName.put( "huge-font", generator.generateFont( p ));
        p.size = Math.round( ppi / 3);
        fontsByName.put( "big-font", generator.generateFont( p ));
       // p.size = Math.round( ppi / 4);  
        p.size = (int)Math.ceil( Gdx.graphics.getHeight() * 0.038 );

        fontsByName.put( "small-font", generator.generateFont( p ));
        p.size = Math.round( ppi / 5);      
        fontsByName.put( "tiny-font", generator.generateFont( p ));
        
        generator.dispose();
        
        return fontsByName;        
    }
  
}

package ca.pmcgovern.mkb;

import ca.pmcgovern.mkb.screens.MagicAssetManager;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.io.FileInputStream;
import java.util.Properties;

public class GameMain extends Game {

    protected AssetManager assetMgr;
    
    private ScreenManager screenMgr;
  
    public static final int NOT_SET = -1;
    
    public static int editTargetId = NOT_SET;
    
    public static final String TAG = "GameMain";
   // private TaskManager taskMgr;
    
    @Override
    public void create() {
    	
        //Texture.setEnforcePotImages(false);
        this.assetMgr = new MagicAssetManager();
        
        Gdx.app.log( "Main", "start." );
        this.screenMgr = ScreenManager.getInstance();        
        this.screenMgr.init( this, this.assetMgr );
    
        this.screenMgr.showScreen( MkbScreen.ScreenId.SPLASH_SCREEN );    
        
    }
    
    @Override
    public void dispose() {
        Gdx.app.log( "Main", "dispose..." );
        
       // this.screenMgr.dispose();
        
        
        Array<String> all = this.assetMgr.getAssetNames();
        
      //  for( String name : all ) {
       //     Gdx.app.log( TAG, "K:" + name + ": " +this.assetMgr.get(name ));
      //      this.assetMgr.unload(name);
      //  }
        
        this.assetMgr.dispose();
     
    }

    @Override
   public void render() {
        super.render();
    }
    
   
   private static Properties strings;
   
   public static void loadStrings( String langPrefix ) {

       FileHandle f = Gdx.files.internal( "data/strings/" + langPrefix + ".properties" );
       strings = new Properties();
      
       try {
        strings.load( new FileInputStream( f.file() ));
       } catch( Exception e ) {
           Gdx.app.error( TAG, "Failed to load language file. " + e.getMessage() );
       }
   }
   
   public static String getString( String key ) {
       
       return strings.getProperty(key, key);
   }
   
}

   
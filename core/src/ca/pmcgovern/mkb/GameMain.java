package ca.pmcgovern.mkb;

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.FileInputStream;
import java.util.Properties;

public class GameMain extends Game {

    private ScreenManager screenMgr;
  
    public static final int NOT_SET = -1;
    
    public static int editTargetId = NOT_SET;
    
    public static final String TAG = "GameMain";
   // private TaskManager taskMgr;
    
    @Override
    public void create() {
    	
        //Texture.setEnforcePotImages(false);

        Gdx.app.log( "Main", "start." );
        this.screenMgr = ScreenManager.getInstance();        
        this.screenMgr.init( this );
    
        this.screenMgr.showScreen( MkbScreen.ScreenId.SPLASH_SCREEN );    
        
    }
    
    @Override
    public void dispose() {
        this.screenMgr.dispose();
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

   
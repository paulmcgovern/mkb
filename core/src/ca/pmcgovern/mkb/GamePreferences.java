package ca.pmcgovern.mkb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import java.util.Locale;

public class GamePreferences {

    public static final String PREFS_NAME = "ca.pmcgovern.mkb.GamePreferences";
    
    public enum Keys { OVERVIEW_CAM_ZOOM, OVERVIEW_CAM_X, OVERVIEW_CAM_Y, LANGUAGE }
    
    private static GamePreferences singleton;
   
    private Preferences prefs;
    
    private GamePreferences() {
        this.prefs = Gdx.app.getPreferences( PREFS_NAME );
    }
    
   
    public static final GamePreferences getInstance() {
        
        if( singleton == null ) {
            singleton = init();
        }
        
        return singleton;
    }
    
    private static synchronized GamePreferences init() {        
        return new GamePreferences();        
    }
    
    
  

    /** Restore camera state. Defaults to zoom 1 */
    public void restoreCameraState( OrthographicCamera cam ) {
        
        if( cam == null ) {
            throw new IllegalArgumentException( "Camera is null." );
        } 
     
        cam.zoom = this.prefs.getFloat( Keys.OVERVIEW_CAM_ZOOM.name(), 1 );    
        
        cam.position.x = this.prefs.getFloat( Keys.OVERVIEW_CAM_X.name(), 0 );
        cam.position.y = this.prefs.getFloat( Keys.OVERVIEW_CAM_Y.name(), 0 ); 
    }    
    
    public String getLanguage() {
        
        String lang = this.prefs.getString( Keys.LANGUAGE.name(), "" );
        
        if( lang == null || lang.isEmpty() ) {

            String dfltLang = Locale.getDefault().getLanguage();
        
            if( "fr".equals( dfltLang )) {
                GameMain.loadStrings( "fr");
            } else {
                GameMain.loadStrings( "en" );
            }            
            
            saveLanguage( dfltLang );
            lang = dfltLang;
        }
        
        return lang;
    }
    
    
    public void saveLanguage( String language ) {
        
        if( language == null || language.length() != 2 ) {
            throw new IllegalArgumentException( "Bad language: " + language );
        }
        
        this.prefs.putString( Keys.LANGUAGE.name(), language.toLowerCase() );        
        this.prefs.flush(); 
    }
    
    

    /** Only saves X and Y. Drops Z. */
    public void saveCameraState( OrthographicCamera cam ) {
        
        if( cam == null ) {
            throw new IllegalArgumentException( "Camera is null." );
        }
 
        this.prefs.putFloat( Keys.OVERVIEW_CAM_ZOOM.name(), cam.zoom );
                
        Vector3 pos = cam.position;
        this.prefs.putFloat( Keys.OVERVIEW_CAM_X.name(),  pos.x );
        this.prefs.putFloat( Keys.OVERVIEW_CAM_Y.name(),  pos.y );
        
        this.prefs.flush();        
    }
    


    
    
    public void flush() {
        this.prefs.flush();
    }
    
}

package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.GameMain;
import ca.pmcgovern.mkb.screens.MkbScreen.ScreenId;
import ca.pmcgovern.mkb.sprites.EffectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *  http://bioboblog.blogspot.ca/2012/08/libgdx-screen-management.html
 *  
 *  TODO: dispose properly.
 *  
 * @author mcgovern
 *
 */

public class ScreenManager {

        private static ScreenManager instance;
        
        private GameMain game;
        
        private MkbScreen[] allScreens;
         
        private EffectManager effectManager;
        
        private ScreenManager() {};
        
        
        public static ScreenManager getInstance() {
            if( instance == null ) {
               synchronized( ScreenManager.class ) {
                   instance = new ScreenManager();
               }
            }
            
            return instance;
        }
        
        public void init( GameMain game, AssetManager assetMgr ) {
                       
            if( game == null ) {
                throw new IllegalArgumentException( "Game is null." );
            }
            
            if( assetMgr == null ) {
                throw new IllegalArgumentException( "Null asset manager" );
            }
            
            this.game = game;

           
            this.effectManager = new EffectManager( assetMgr );
            
            this.allScreens = new MkbScreen[ 4 ];
                      
            this.allScreens[ MkbScreen.ScreenId.SPLASH_SCREEN.ordinal() ] = new SplashScreen( assetMgr, this.effectManager );
            this.allScreens[ MkbScreen.ScreenId.SETTINGS.ordinal() ] = new ProjectSettingsScreen( assetMgr, this.effectManager );  
            this.allScreens[ MkbScreen.ScreenId.OVERVIEW_SCREEN.ordinal() ] = new OverviewScreen( assetMgr, this.effectManager );        
            this.allScreens[ MkbScreen.ScreenId.HELP.ordinal() ] = new HelpScreen( assetMgr, this.effectManager );
        }
        
        
        public void clearOpenMenu( MkbScreen.ScreenId targetScreen ) {
        
            this.allScreens[ targetScreen.ordinal() ].clearOpenMenu();
        }
        
        
        public void showScreen( MkbScreen.ScreenId screenId ) {
            
         //   if( screenId < 0 || screenId > this.allScreens.length - 1 ) {
         //       throw new IllegalArgumentException( "Bad screen ID " + screenId );
          //  }
            Gdx.app.log( "ScreenMgr", "show " + screenId  );
          
        
            this.game.setScreen( this.allScreens[ screenId.ordinal() ] );
     
        }
    
        
      /*
        public void dispose( MkbScreen screen ) {
            
            if( screen == null ) {
                throw new IllegalArgumentException( "Screen is null" );
            }
            
            screen.dispose();
            
         //   int targetId = screen.getId();
            
      //      this.allScreens[ i ].dispose();
            
        //    for( int i = 0; i < this.allScreens.length; i++ ) {
                
        //        if( this.allScreens[ i ].getId() == targetId ) {
                   
        //        }
        //    }
           
        }
        */
        public void dispose() {
            
            Gdx.app.log( "ScreenMgr", "dispose..." );
           
            for( int i = 0; i < this.allScreens.length; i++ ) {
                if( this.allScreens[ i ] != null ) {
                    this.allScreens[ i ].dispose();
                    this.allScreens[ i ] = null;
                }
            }            
        }

    private TextureRegion lastScreenImage;
    
    
    
    void setLastScreenImg(TextureRegion t) {
       this.lastScreenImage = t;
    }
    
    TextureRegion getLastSceenImg() { return this.lastScreenImage; }
        
}

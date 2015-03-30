package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.GameMain;
import ca.pmcgovern.mkb.screens.MkbScreen.ScreenId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

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
        
        private AssetManager assetMgr;
        
        
        private ScreenManager() {};
        
        
        public static ScreenManager getInstance() {
            if( instance == null ) {
               synchronized( ScreenManager.class ) {
                   instance = new ScreenManager();
               }
            }
            
            return instance;
        }
        
        public void init( GameMain game ) {
                       
            if( game == null ) {
                throw new IllegalArgumentException( "Game is null." );
            }
            
            
            this.game = game;

            this.assetMgr = new MagicAssetManager();
                       
            this.allScreens = new MkbScreen[ 5 ];
                      
            this.allScreens[ MkbScreen.ScreenId.SPLASH_SCREEN.ordinal() ] = new SplashScreen( this.assetMgr );
            this.allScreens[ MkbScreen.ScreenId.SETTINGS.ordinal() ] = new ProjectSettingsScreen( this.assetMgr );  
            this.allScreens[ MkbScreen.ScreenId.OVERVIEW_SCREEN.ordinal() ] = new OverviewScreen( this.assetMgr );
            this.allScreens[ MkbScreen.ScreenId.NEW_SCREEN.ordinal() ] = new NewTaskScreen( this.assetMgr );
            this.allScreens[ MkbScreen.ScreenId.HELP.ordinal() ] = new HelpScreen( this.assetMgr );
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
            
            this.assetMgr.dispose();
            
            for( int i = 0; i < this.allScreens.length; i++ ) {
                if( this.allScreens[ i ] != null ) {
                    this.allScreens[ i ].dispose();
                    this.allScreens[ i ] = null;
                }
            }            
        }
        
}

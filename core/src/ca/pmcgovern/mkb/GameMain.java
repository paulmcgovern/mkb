package ca.pmcgovern.mkb;

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameMain extends Game {

    private ScreenManager screenMgr;
  
    public static final int NOT_SET = -1;
    public static int editTargetId = NOT_SET;
    
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
    
}

   
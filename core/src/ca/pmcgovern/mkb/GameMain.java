package ca.pmcgovern.mkb;

import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameMain extends Game {

    private ScreenManager screenMgr;
  
    private TaskManager taskMgr;
    
    @Override
    public void create() {
    	
        //Texture.setEnforcePotImages(false);

        Gdx.app.log( "Main", "start." );
        this.screenMgr = ScreenManager.getInstance();        
        this.screenMgr.init( this );
        
        this.taskMgr = TaskManager.getInstance();
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

   
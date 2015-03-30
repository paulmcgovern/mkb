package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.menus.MkbMenu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class MkbScreen implements Screen {
    
    public static enum ScreenId { OVERVIEW_SCREEN, SETTINGS, SPLASH_SCREEN, NEW_SCREEN, HELP };

    
    protected AssetManager assetMgr;
    
    public abstract MkbScreen.ScreenId getId();

    protected int width;
    
    protected int height;
    
    public MkbScreen( AssetManager assetMgr ) {
        
        if( assetMgr == null ) {
            throw new IllegalArgumentException( "Asset manager is null." );
        }
        
        this.assetMgr = assetMgr;
    }
    
    public abstract void setOpenMenu( MkbMenu w );

    
    public abstract void clearOpenMenu();

   
    public AssetManager getAssetManager() {
        return assetMgr;
    }
    
    
    @Override
    public void show() {
    }

    @Override
    public void hide() {      
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

   
    
    /** Utility method for changing ImageTextButton layout from horizontal to vertical */
    public static void layoutButton( ImageTextButton button ) {
        
        Image i = button.getImage();
        Label l = button.getLabel();
        button.clearChildren();
        
    //  System.err.println( "XXX:"+  l.getStyle().font.getAscent() + " " + l.getHeight());  
      
      //  l.getStyle().font.getAscent();
        
        button.add( i ).size( (int)Math.floor(l.getHeight() * 1.5 ));
        button.row();
        button.add( l );
    }
    
        
   
}

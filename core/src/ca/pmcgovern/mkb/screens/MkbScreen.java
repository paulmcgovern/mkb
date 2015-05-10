package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.menus.MkbMenu;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.sprites.EffectManager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class MkbScreen implements Screen {
    
    public static enum ScreenId { OVERVIEW_SCREEN, SETTINGS, SPLASH_SCREEN, NEW_SCREEN, HELP };

    
    protected AssetManager assetMgr;
    protected EffectManager effectMgr;
    
    public abstract MkbScreen.ScreenId getId();

    protected int width;
    
    protected int height;
    
    public MkbScreen( AssetManager assetMgr, EffectManager effectMgr ) {
        
        if( assetMgr == null ) {
            throw new IllegalArgumentException( "Asset manager is null." );
        }
        
        this.assetMgr = assetMgr;
        
        if( effectMgr == null ) {
            throw new IllegalArgumentException( "Effect manager is null." );
        }
        
        this.effectMgr = effectMgr;
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
    
    protected Texture bg;
    protected void setBackground() {
        this.bg = this.assetMgr.get( "data/lined_paper.png", Texture.class ); //Yellow_notebook_paper.jpg", Texture.class ); // lined_paper.png" ); 
        this.bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
    
    }
    
    protected void drawBackground( Batch spriteBatch ) {
        
        //   this.bg = this.assetMgr.get( "data/lined_paper.png", Texture.class ); //Yellow_notebook_paper.jpg", Texture.class ); // lined_paper.png" ); 
       // this.bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
   // System.err.println( "BG: " + this.bg.getWidth() );
         // Background will repeat over whole screen.
     
        spriteBatch.draw( this.bg, 0,0, 0,0, this.width, this.height );
    }
        
    ScreenId nextScreenId;
    public void setNextScreenId( ScreenId nextId ) {
        this.nextScreenId = nextId;
    }
    
    protected Image fadeIn( Stage uiStage ) {
           // Fade in
        ScreenManager s = ScreenManager.getInstance();
        TextureRegion lastScreen = s.getLastSceenImg();
        if( lastScreen != null ) {
            s.setLastScreenImg( null );
            Image lastScreenImage = new Image( lastScreen );            
            lastScreenImage.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ), Actions.removeActor() ));
            uiStage.addActor( lastScreenImage );
            return lastScreenImage;
        }                
        return null;
    }
   
}

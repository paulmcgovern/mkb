package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class LoadingBar extends Actor {
 
    private AssetManager assetMgr;
    private TextureRegion pixmaptex;    
       
    
    public LoadingBar( AssetManager assetMgr ) {
       
        this.assetMgr = assetMgr;
        
        Pixmap pixmap = new Pixmap( 2, 2, Format.RGBA8888 );
        
        pixmap.setColor( 0, 1, 0, 1 );        
        pixmap.fillRectangle(0, 0, 2, 2 );
       
        this.pixmaptex = new TextureRegion( new Texture( pixmap ));
        pixmap.dispose();  
    }
    

    @Override
    public void draw( Batch batch, float parentAlpha) {
       
        batch.draw( this.pixmaptex, this.getX(), this.getY(), 0, 0, 2, 2,  this.assetMgr.getProgress() * 100, 1, 0 );   
    } 
}

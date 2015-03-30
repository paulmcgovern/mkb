package ca.pmcgovern.mkb.menus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class BaseTable extends MkbMenu {

    private Container<Actor> payload;
 
    
    public BaseTable( AssetManager assetMgr ) {
        
        super();
      
        TextureAtlas tableAtlas = assetMgr.get( "data/tables.pack", TextureAtlas.class );
                    
    
        Container<Actor> c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top_left" )));
        
        this.add( c ).fill().expand();
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top" )));
        
        this.add( c ).fill().colspan(2).expand();
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top_right" )));
        
        this.add( c ).fill().expand();
        
        this.row();
       
        
        // Middle row
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_left" )));
        
        this.add( c ).fill().expand();
        
                
        // Payload cell  
        // Will be populated later
        this.payload = new Container<Actor>();
        this.payload.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "bg" )));        
               
        this.add( this.payload ).colspan(2).fill().expand();
        
        
        
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_right" )));      
        this.add( c ).fill().expand();
                   
        this.row();
        
                
        // Bottom row
        
        c = new Container<Actor>();    
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom_left" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
        
        
        c = new Container<Actor>();    
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_carrot" )));
        this.add( c ).fill();
       
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom_right" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
                
        this.pack();       
    }
    
    

    
    public void setMenu( Actor content ) {
    
        this.invalidateHierarchy();      
        this.payload.setActor( content );    
        this.pack();
    }
}

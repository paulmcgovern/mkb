package ca.pmcgovern.mkb.sprites;

import ca.pmcgovern.mkb.sprites.TaskSprite.DrawState;
import ca.pmcgovern.mkb.ui.Task.IconColor;
import ca.pmcgovern.mkb.ui.Task.Type;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TaskDrawableFactory {

    private TextureAtlas taskAtlas;
    
    public TaskDrawableFactory( TextureAtlas taskAtlas ) {
   
        this.taskAtlas = taskAtlas;
    }    
    
    public Drawable getDrawable( Type type, DrawState state, IconColor color ) {
   
    	TextureRegion tr = null;
		StringBuilder buff = new StringBuilder( type.toString() );  
		
		
    	if( color != null && IconColor.NONE != color ) {

    		buff.append( '-' );
    		buff.append( "selected" );  	
    		buff.append( '-' );    		
    		buff.append( color.toString() );
    	}    	
    
        tr = this.taskAtlas.findRegion( buff.toString().toLowerCase() );
    
        if( tr == null ) {
    		throw new IllegalArgumentException( "No drawable found for '" + buff + "'" );
    	}
        
        return new TextureRegionDrawable( tr );
    }
}

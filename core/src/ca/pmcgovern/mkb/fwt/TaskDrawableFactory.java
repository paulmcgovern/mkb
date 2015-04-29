/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.fwt;

import ca.pmcgovern.mkb.fwt.Task.IconColor;
import ca.pmcgovern.mkb.fwt.Task.Type;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager.DrawContext;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author mcgovern
 */
public class TaskDrawableFactory {
    
    public TaskDrawableFactory( TextureAtlas taskAtlas ) {
        this.taskAtlas = taskAtlas;
    }
    
      private TextureAtlas taskAtlas;
    private Texture done;
    
  
    public TextureRegionDrawable getDrawable( Type type, DrawContext context, IconColor color ) {
   
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

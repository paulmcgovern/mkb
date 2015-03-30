package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class TaskBackground extends Image {

    @Override
    public void draw( Batch batch, float parentAlpha ) {
    
        Color c = batch.getColor();
        
        batch.setColor( Color.MAGENTA );
        
        super.draw( batch, parentAlpha );
        
        batch.setColor( c );
    }
    
}

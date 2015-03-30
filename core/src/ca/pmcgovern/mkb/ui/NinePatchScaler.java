package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class NinePatchScaler {

    private Skin skin;
    
    public NinePatchScaler( Skin skin ) {
        this.skin = skin;
    }
    
    public NinePatchDrawable getScaledPatch( float scale, String name ) {
      
        if( name == null || name.isEmpty() ) {
            throw new IllegalArgumentException( "Patch name is null or zero-length" );
        }
scale = 1;
        NinePatch patch = this.skin.getPatch( name );

        patch.setLeftWidth( (float)Math.ceil( patch.getLeftWidth() * scale ));
        patch.setRightWidth( (float)Math.ceil(patch.getRightWidth() * scale ));        
        patch.setTopHeight( (float)Math.ceil(patch.getTopHeight() * scale ));
        patch.setBottomHeight( (float)Math.ceil(patch.getBottomHeight() * scale ));  
       
        return new NinePatchDrawable( patch );
    }
}

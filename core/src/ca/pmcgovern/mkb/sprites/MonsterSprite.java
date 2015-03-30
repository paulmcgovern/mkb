package ca.pmcgovern.mkb.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MonsterSprite extends Image {

    public static final String MONSTER = "monster";
    
    public MonsterSprite( Sprite t ) {
        super( t );
    }
    
    @Override
    public String getName() {
      return MONSTER;  
    }
}

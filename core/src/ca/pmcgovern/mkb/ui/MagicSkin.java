package ca.pmcgovern.mkb.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MagicSkin extends Skin {

    
    public MagicSkin() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MagicSkin(FileHandle skinFile, TextureAtlas atlas) {
        super(skinFile, atlas);
        // TODO Auto-generated constructor stub
    }

    public MagicSkin(FileHandle skinFile) {
        super(skinFile);
        // TODO Auto-generated constructor stub
    }

    public MagicSkin(TextureAtlas atlas) {
        super(atlas);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void load(FileHandle skinFile) {
        
        System.out.println( "MagicSkin: " + skinFile.file() );
        // TODO Auto-generated method stub
        super.load(skinFile);
    }

}

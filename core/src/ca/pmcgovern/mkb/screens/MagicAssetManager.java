package ca.pmcgovern.mkb.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MagicAssetManager extends AssetManager {

    
    
    public MagicAssetManager() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MagicAssetManager(FileHandleResolver resolver) {
        super(resolver);
        // TODO Auto-generated constructor stub
    }

    public void addShiznit(String name, BitmapFont f ) {
        this.addAsset(name,  BitmapFont.class,  f );        
    }
}

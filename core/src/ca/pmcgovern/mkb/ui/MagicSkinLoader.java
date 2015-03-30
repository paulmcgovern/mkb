package ca.pmcgovern.mkb.ui;

import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class MagicSkinLoader extends SkinLoader {

    private Map<String,BitmapFont> fontsByName;
    
    public MagicSkinLoader(FileHandleResolver resolver, Map<String,BitmapFont> fontsByName ) {
        super(resolver); 
        
        if( fontsByName == null ) {
            throw new NullPointerException( "Font map is null." );
        }
        
        this.fontsByName = fontsByName;
    }

    @Override
    public Skin loadSync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
 
        String textureAtlasPath;
        ObjectMap<String, Object> resources;
        
        if (parameter == null) {
            textureAtlasPath = file.pathWithoutExtension() + ".atlas";
            resources = null;
        } else {
            textureAtlasPath = parameter.textureAtlasPath;
            resources = parameter.resources;
        }
        
        TextureAtlas atlas = manager.get(textureAtlasPath, TextureAtlas.class);
        Skin skin = new Skin(atlas);
        
        for( Map.Entry<String,BitmapFont> kv : this.fontsByName.entrySet() ) {
          
            skin.add( kv.getKey(), kv.getValue() );
        }
        
       
        if (resources != null) {
            for (Entry<String, Object> entry : resources.entries()) {
                skin.add(entry.key, entry.value);
            }
        }
        skin.load(file);
        return skin;
    }
  
}

package ca.pmcgovern.mkb.screens;

import java.util.Map;
import java.util.WeakHashMap;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

public class GeneratedFontLoader extends SynchronousAssetLoader<BitmapFont, BitmapFontLoader.BitmapFontParameter> {
    
        WeakHashMap<String, BitmapFont> fontsByName;
        BitmapFontLoader defaultLoader;
        public GeneratedFontLoader( BitmapFontLoader defaultLoader, FileHandleResolver resolver, Map<String, BitmapFont> fontsByName ) {
            super(resolver);  
            this.fontsByName = new WeakHashMap<String, BitmapFont>();
            this.fontsByName.putAll( fontsByName );
            this.defaultLoader = defaultLoader;
            System.out.println( "Generated file loader()" );
        }

        
     

        @Override
        public BitmapFont load (AssetManager assetManager, String fileName, FileHandle file, BitmapFontLoader.BitmapFontParameter parameter) {
                   
            
            System.out.println( "LOAD:" + fileName + " " + file );
            if( this.fontsByName.containsKey( fileName )) {
                return this.fontsByName.get( fileName );
            } else {
              
               return this.defaultLoader.loadSync(assetManager, fileName, file,  parameter );
              
             //   return this.defaultLoader.
            }
      //  for (Page page : data.getPages()) {
      //  Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
      //  page.texture = texture;
      //  }
//return null;
       // return new TextureAtlas(data);
        }

        @Override
        public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, BitmapFontLoader.BitmapFontParameter parameter) {
            System.out.println( "DEPS:" + fileName );
            
            return this.getDependencies(fileName, file, parameter);
   //     return new Array<AssetDescriptor>();//    this.defaultLoader.getDependencies(fileName, file,  new BitmapFontParameter());
       // FileHandle imgDir = atlasFile.parent();

      //  if (parameter != null)
      //  data = new TextureAtlasData(atlasFile, imgDir, parameter.flip);
      //  else {
      //  data = new TextureAtlasData(atlasFile, imgDir, false);
      //  }

       // Array<AssetDescriptor> dependencies = new Array();
        //for (Page page : data.getPages()) {
        //TextureParameter params = new TextureParameter();
       // params.format = page.format;
       // params.genMipMaps = page.useMipMaps;
        //params.minFilter = page.minFilter;
        //params.magFilter = page.magFilter;
        //dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
        //}
       // return dependencies;
         //   return null;
        }
        
        
     //   static public class BitmapFontParameter extends AssetLoaderParameters<BitmapFont> {
        
     //   public BitmapFontParameter() {
     //   }

       
    //    }
}//


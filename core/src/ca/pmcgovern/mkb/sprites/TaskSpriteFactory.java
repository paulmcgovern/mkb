package ca.pmcgovern.mkb.sprites;

import ca.pmcgovern.mkb.ui.Task;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class TaskSpriteFactory {
    
   
    private TaskDrawableFactory drawables;
  
    private EventListener[] listeners;
    
    private LabelStyle taskLabelDflt;
    
    public TaskSpriteFactory( TextureAtlas taskAtlas ) {
        
        this.drawables = new TaskDrawableFactory( taskAtlas );  
    }
    
    public TaskSpriteFactory( TextureAtlas taskAtlas, LabelStyle taskLabelDflt, EventListener ... listeners ) {
        
        this.drawables = new TaskDrawableFactory( taskAtlas );     
        this.listeners = listeners;
        this.taskLabelDflt = taskLabelDflt;
        
    }
        
    
    public TaskSprite getTask( Task.Type type ) {
   
        Task t = new Task();
        t.setType( type );
        
        return getTaskNoLabel( t );
        
    }
    
    
    public TaskSprite getTaskNoLabel( Task task ) {
         
        TaskSprite s = new TaskSprite( task, this.drawables );  
        
        if( this.listeners != null ) {
            
            for( int i = 0; i < this.listeners.length; i++ ) {
                s.addListener( this.listeners[ i ] );
            }
            
        }
 
        return s;     
    }
    
    
    
    public TaskSprite getTask( Task task, float zoom ) {
        
        TaskSprite s = getTaskNoLabel( task );
        
        s.setLabel( buildLabel( task, zoom )); 
        
        s.setX( task.getPosX() );
        s.setY( task.getPosY() );
        
        return s;
    }
   
    
    
    
    protected Label buildLabel( Task task, float zoom ) {
        
        System.err.println( "##Zoom" + zoom );
   /**
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal( "data/Days.ttf" ));
        
        FileHandle f = Gdx.files.internal( "data/Days.ttf" );
        System.err.println("XX:"+ f.exists() );
        System.err.println( "XX:" + f.length() );
        FreeTypeFontParameter singleUseFontParams = new FreeTypeFontParameter();   
        singleUseFontParams.size = (int)Math.floor( 30 / zoom );

        LabelStyle scaledFontStyle = new LabelStyle( this.taskLabelDflt );
        
        scaledFontStyle.font = generator.generateFont( singleUseFontParams );
        
        Label taskLabel = new Label( task.getDescription(), scaledFontStyle );
        
        generator.dispose();
        **/
        
        Label taskLabel = new Label( task.getDescription(), this.taskLabelDflt );// scaledFontStyle );
            
        return taskLabel;
    }
   
}

package ca.pmcgovern.mkb.ui;

import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;



public class TaskLabelManager extends ActorGestureListener {

    private Stage taskStage;
    private Group labelGroup;
    private LabelStyle defaultStyle;
    
    public TaskLabelManager( Stage taskStage, Group labelGroup, LabelStyle defaultStyle ) {
            
        this.taskStage = taskStage;
        this.labelGroup = labelGroup;
        this.defaultStyle = defaultStyle;
    }
    
    
    /**
     * Remove all current labels and create new ones with scaled fonts with NO fade-in.
     */
    public void init() {
 System.err.println( "INIT() LG");   
      this.labelGroup.clearActions();
      this.labelGroup.clearChildren(); 

      buildAllLabels( this.taskStage.getActors() ); 
        
    //  this.labelGroup.setColor( 0, 0, 0, 1 );
    //  this.labelGroup.setVisible( true );
      
      
      this.labelGroup.clearChildren();    
      buildAllLabels( this.taskStage.getActors() );       
      showLabelGroup( this.labelGroup, 0.3f );      
      
      
     // showLabelGroup( this.labelGroup, 0.3f );         
      
    }
    

    
    @Override        
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
           
        hideLabelGroup( this.labelGroup, 0.15f );
    }   
    
    /**
     * Remove all current labels and create new ones with scaled fonts with fade-in.
     */    
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

        this.labelGroup.clearChildren();    
        buildAllLabels( this.taskStage.getActors() );       
        showLabelGroup( this.labelGroup, 0.3f );         
      
    }
    

    public void buildAllLabels( Array<Actor> allActors ) {

        int count = allActors.size;
      System.err.println( "Tcount:" + count );
        if( count == 0 ) {
            return;
        }
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal( "data/Days.ttf" ));

        FreeTypeFontParameter singleUseFontParams = new FreeTypeFontParameter();   
        singleUseFontParams.size = (int)Math.floor( 30 / ((OrthographicCamera)this.taskStage.getCamera()).zoom);

        LabelStyle scaledFontStyle = new LabelStyle( this.defaultStyle );
        
        scaledFontStyle.font = generator.generateFont( singleUseFontParams );
            
        float labelOffset = this.labelGroup.getY() + singleUseFontParams.size;
        
               
        for( int i = 0; i < count; i++ ) {
            System.err.println( "Build label:" + i + " " + allActors.get( i ));
            buildLabel( (TaskSprite)allActors.get( i ), scaledFontStyle, labelOffset );
        }
        
        generator.dispose();
    }
    
    private void buildLabel( TaskSprite taskActor, LabelStyle style, float yOffset ) {
     
        Stage targetStage = this.labelGroup.getStage();
        
        Vector2 srcPos = new Vector2( taskActor.getX(), taskActor.getY() );  
        Vector2 srcScreenPos = this.taskStage.stageToScreenCoordinates( srcPos );
        Vector2 destPos = targetStage.screenToStageCoordinates(  srcScreenPos );
       
        Label taskLabel = new Label( taskActor.getTaskDescription(), style );

        taskLabel.setPosition( destPos.x - this.labelGroup.getX(), (destPos.y - yOffset ));
       
        this.labelGroup.addActor( taskLabel );
    }
    
    
    
    public void showLabelGroup( Group allLabels, float duration ) {
        toggleLabelGroup( allLabels, 1, duration );
    }
    
    public void hideLabelGroup( Group allLabels, float duration ) {
        toggleLabelGroup( allLabels, 0, duration );        
    }
    
    
    private void toggleLabelGroup( Group allActors, float newAlpha, float duration ) {      
        allActors.clearActions();
        allActors.addAction( Actions.alpha( newAlpha, duration ) );
    }
         
    
}

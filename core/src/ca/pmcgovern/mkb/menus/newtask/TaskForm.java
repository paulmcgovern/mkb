/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.menus.newtask;

import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.events.TaskSavedEvent;
import ca.pmcgovern.mkb.fwt.Task;
import ca.pmcgovern.mkb.fwt.Task.IconColor;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.BaseTable;
import ca.pmcgovern.mkb.screens.MkbScreen;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.ui.ColourPicker;
import ca.pmcgovern.mkb.ui.TaskPicker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;


  
/**
 *
 * @author mcgovern
 */
public class TaskForm extends BaseTable {

    private ImageTextButton save;
    private ImageTextButton cancel;
    
    private TextField titleInput;    
    private TaskPicker taskSelectTable;

    private ColourPicker colourSelectTable;
        
    private TaskSpriteManager taskManager;
    
    private EffectManager effectMgr;
    
    public TaskForm( AssetManager assetMgr, EffectManager effectMgr ) {
    
        super( assetMgr );
        this.taskManager = new TaskSpriteManager(assetMgr);
        this.effectMgr = effectMgr;
         
     //   TextureAtlas taskSprites = assetMgr.get( "data/tasks.atlas", TextureAtlas.class );
        TextureAtlas iconSprites = assetMgr.get( "data/icons.atlas", TextureAtlas.class );
         
        Table menuTable = new Table();
          Skin skin = assetMgr.get( "data/icons.json", Skin.class );
        
         this.titleInput = new TextField( "New task name", skin, "new-task" );
        this.titleInput.setName( "titleInput" );      
        this.titleInput.addListener( new TextFocusListener() );
        this.titleInput.addListener( new TextInputListener() );
    
              // Space between icons in the picker elements
        float iconDmtr = Gdx.graphics.getWidth() * 0.065f;   
      
        this.colourSelectTable = new ColourPicker( iconSprites, skin, (int)Math.floor(Gdx.graphics.getWidth() * 0.6f) , (int)Math.floor( Gdx.graphics.getHeight() * 0.6f ), iconDmtr );
 
        this.taskSelectTable = new TaskPicker( skin, taskManager, iconDmtr, Gdx.graphics.getWidth() * 0.6f ); 
      
        PickedListener p = new PickedListener();
        
        this.taskSelectTable.addListener( p );        
        this.colourSelectTable.addListener( p );        
   
        
        // Save and cancel buttons
        Table buttonTable = new Table();
        
         // Save button
        this.save = new ImageTextButton( "Save", skin, "new-task-save");
        MkbScreen.layoutButton( this.save );
        
        // Save button is initially inactive
        this.save.setColor( 1, 1, 1, 0.25f );
        this.save.setTouchable( Touchable.disabled );
        
        this.cancel = new ImageTextButton( "Cancel", skin, "new-task-cancel" );
        MkbScreen.layoutButton( this.cancel );
        
        
       //         this.cancel.addListener( playClick );
                
         PlayClickListener playClick = new PlayClickListener( this.effectMgr );
      
        this.save.addListener( new SaveButtonListener() ); 
        this.save.addListener( playClick );
        this.cancel.addListener( new CancelButtonListener() );
        this.cancel.addListener( playClick );
                
        buttonTable.add( this.save );          
        buttonTable.add( this.cancel );
        
        float tableWidth = (float)Math.floor( Gdx.graphics.getWidth() * 0.7f );
        
        menuTable.add( this.titleInput );
        menuTable.row();
        menuTable.add( this.taskSelectTable ).width( tableWidth );
        menuTable.row();
        menuTable.add( this.colourSelectTable  ).width( tableWidth );
        menuTable.row(); 
        menuTable.add( buttonTable );
        
        
        setMenu( menuTable );
        
        pack();
     }
    
    
    public void populate( TaskSprite ts ) {
        if( ts == null ) {
            throw new IllegalArgumentException( "Attempt to populate form with null TaskSprite");
        }
        
        Task t = ts.getTask();
        
        if( t == null ) {
            throw new IllegalArgumentException( "Attempt to populate form with null TaskSprite");
        }
        
        
        this.titleInput.setText( t.getDescription() );
        this.taskSelectTable.setCurrentTask( t );
        
        System.err.println( "TS populate" + ts );
    }

    /**
     * Listen for when a task or colour is picked.
     * @author mcgovern
     *
     */
    class PickedListener implements EventListener {

        @Override
        public boolean handle( Event event ) {

            if( event instanceof TaskPicker.PickedEvent ) {
   //System.err.println( "NTS: Task Picked event: " + event ); 
                TaskForm.this.effectMgr.playClick();
                TaskForm.this.updateSaveButton();       
           
                return true;
                
            } else if( event instanceof ColourPicker.PickedEvent ) {
  // System.err.println( "NTS: Color Picked event: " + event ); 
            	Task.IconColor newColor = ((ColourPicker.PickedEvent)event).getColour(); 
              //  TaskForm.this.updateSaveButton();
                TaskForm.this.effectMgr.playClick();                
               TaskForm.this.taskSelectTable.setSelectedColor( newColor );
                return true;
            }

            return false;
        }      
    }

    
    
    class TextFocusListener extends FocusListener {
  
        boolean dirty;
        
        @Override
        public void keyboardFocusChanged( FocusListener.FocusEvent event, Actor actor, boolean focused ) {
        
            TextField textField = (TextField)actor;
            
            if( !dirty ) {
                textField.setText( "" );
                this.dirty = true;
            } else {
                textField.setCursorPosition( textField.getText().length() );
            }
            
           TaskForm.this.effectMgr.playClick();
           // NewTaskScreen2.this.uiStage.getRoot().addAction( Actions.moveTo( 0, actor.getStage().getHeight() - actor.getY()- actor.getHeight(), 0.5f, Interpolation.exp10 ));
          //  NewTaskScreen2.this.updateSaveButton();
        }   
    }
    

    /**
     * Return form elements to original position on ENTER key.
     * @author mcgovern
     *
     */
    class TextInputListener extends InputListener {
   
        @Override
        public boolean keyUp( InputEvent event, int keycode)    {
            
            TaskForm.this.updateSaveButton();
        /**    
            NewTaskScreen2.this.updateTaskDescription();
            NewTaskScreen2.this.updateSaveButton();          
          
            if( keycode == Keys.ENTER ) { 
                NewTaskScreen2.this.uiStage.unfocusAll();
                NewTaskScreen2.this.uiStage.getRoot().addAction( Actions.moveTo( 0, 0, 0.5f, Interpolation.exp10 ));
                  
            } */
            TaskForm.this.effectMgr.playClick();
            return super.keyUp(event, keycode);           
        }
    }
    
    
    /**
     * Activate save button depending on state.
     * 
     */
    private void updateSaveButton() {
     
        TaskSprite t = this.taskSelectTable.getSelectedTask();
        IconColor c = this.colourSelectTable.getCurrentColor();
        String d = this.titleInput.getText();
        
      
        if( t != null && c != null && d != null && !d.isEmpty()) {    
            
            if( !this.save.isTouchable() ) {
            
                this.save.setTouchable( Touchable.enabled );  
                this.save.addAction( Actions.alpha( 1, FADE_DURATION )); 
            }
            
        } else {
            
            if( this.save.isTouchable() ) {

                this.save.setTouchable( Touchable.disabled );                  
                this.save.addAction( Actions.alpha( 0.25f, FADE_DURATION ));                  
            }
        }
    }    
    
    
    
      class CancelButtonListener extends ChangeListener {
                      
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
                                   
            TaskManager.getInstance().clearEditTargetId();
            
            TaskForm.this.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ),Actions.removeActor()));
                 // NewTaskScreen2.this.setNextScreenId( MkbScreen.ScreenId.OVERVIEW_SCREEN );
                  //ScreenManager.getInstance().showScreen( MkbScreen.ScreenId.OVERVIEW_SCREEN );
        }
    }
    
  class SaveButtonListener extends ChangeListener {
              
        
        @Override
        public void changed( ChangeEvent event, Actor actor ) {
            
            
       System.err.println( "SAVED");
       
            TaskSprite ts =   TaskForm.this.taskSelectTable.getSelectedTask();
            IconColor color = TaskForm.this.colourSelectTable.getCurrentColor();
            String descr =    TaskForm.this.titleInput.getText();
            
            if( ts == null || color == null || descr == null ) {
                return;
            }
            
            descr = descr.trim();
            
            if( descr.isEmpty() ) {
                return;
            }
            
            ts.setTaskDescription(descr);
            ts.getTask().setColour( color );
           
            if( !TaskSpriteManager.isComplete( ts )) {
                Gdx.app.log( "TaskForm", "Attempt to save incomplete task:"+ ts );
                return;
            }
            
            ts.setPosition( -100000, -100000 );
             
            TaskForm.this.fire( new TaskSavedEvent( ts, TaskSavedEvent.Type.NEW ));
          
            TaskForm.this.addAction( Actions.sequence( Actions.fadeOut(FADE_DURATION), Actions.removeActor() ));
                  
            } 
        }
  
  
    
}

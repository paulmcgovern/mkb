package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.screens.MkbScreen.ScreenId;
import ca.pmcgovern.mkb.screens.ScreenManager;
import ca.pmcgovern.mkb.screens.TaskManager;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.TaskSprite;
import ca.pmcgovern.mkb.sprites.TaskSprite.DrawState;
import ca.pmcgovern.mkb.ui.Task;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class ConfirmWindow extends Window {

    private Container<Actor> payload;
    
    public ConfirmWindow(String title, AssetManager assetMgr, EffectManager effectMgr, TaskSprite taskSprite ) {
        
        
        super( "", assetMgr.get( "data/icons.json", Skin.class ), "delete" );
        
        
        Skin skin = assetMgr.get( "data/icons.json", Skin.class );
        
        
        
        Task task = taskSprite.getTask();
      
        if( task == null ) {
            throw new IllegalArgumentException( "TaskSprite contains null Task." );
        }
        
        
        
        TextureAtlas tableAtlas = assetMgr.get( "data/tables.pack", TextureAtlas.class );
        
        Container<Actor> c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top_left" )));
        
        this.add( c ).fill().expand();
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top" )));
        
       // this.add( c ).fill().colspan(2).expand();
        this.add( c ).fill().expand();
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top_right" )));
        
        this.add( c ).fill().expand();
        
        this.row();
               
        // Middle row
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_left" )));
        
        this.add( c ).fill().expand();
                        
        // Payload cell  
        // Will be populated later
        this.payload = new Container<Actor>();
        
        //this.payload = new Container<Actor>();
        this.payload.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "bg" )));        
               
        //this.add( this.payload ).colspan(2).fill().expand();
        this.add( this.payload ).fill().expand();
                         
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_right" )));      
        this.add( c ).fill().expand();
                   
        this.row();
                        
        // Bottom row        
        c = new Container<Actor>();    
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom_left" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
                
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_bottom_right" )));
        
        this.add( c ).fill().expand().padBottom( 23 );
                
        this.pack();       
        
   
        // TODO: wrap title if over certain lenth
        String taskDesc = task.getDescription();
        
       // if( taskDesc.length() > 20 ) {
            // TODO: WRAP
      //  }
        
       // this.add( new Label( taskDesc, skin, "tiny" )).colspan( 2 );
       // this.row();    
        
        ChangeListener clearOpenMenus = new ClearMenusListener();
        
        Table buttonTable = new Table();  
        
        buttonTable.add( new Label( "Are you sure?", skin, "big" )).colspan( 2 );
        buttonTable.row();   
        
        buttonTable.add( new Label( "Delete " + taskDesc, skin, "tiny" )).colspan( 2 );
        buttonTable.row();  
        
        PlayClickListener playClick = new PlayClickListener( effectMgr );
        
        ImageTextButton itb = new ImageTextButton( "Cancel", skin, "detail-cancel" );
        MkbScreen.layoutButton( itb ); 
        buttonTable.add( itb ).padLeft( 10 );
        itb.addListener( playClick );
        itb.addListener( clearOpenMenus );
        
        itb = new ImageTextButton( "Delete", skin, "detail-delete" );
        MkbScreen.layoutButton( itb );  
        buttonTable.add( itb ).padLeft( 10 );
        itb.addListener( playClick );
        itb.addListener( new DeleteListener( taskSprite ) );
        itb.addListener( clearOpenMenus );
        
        this.payload.setActor( buttonTable );       
   
        this.pack();
    }  

    
    
    class DeleteListener extends ChangeListener {       
        
        private TaskSprite taskSprite;
        
        public DeleteListener( TaskSprite taskSprite ) {
            this.taskSprite = taskSprite;
        }
        
        @Override
        public void changed(ChangeEvent event, Actor actor) {        
           ConfirmWindow.this.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new RemoveActorAction()));  
         
           TaskManager taskMgr = TaskManager.getInstance();
           ((TaskManager) taskMgr).delete( taskSprite.getTask() );
           
           taskSprite.setState( DrawState.DELETED );
           
           // TODO: remove from parent stage.....
        }
    }
        
    
    
    
    
    /**
     * Close current window and clear active menus on overview screen
     */    
    class ClearMenusListener extends ChangeListener {
        
        public void changed( ChangeEvent event, Actor actor ) {
            
            ConfirmWindow.this.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), new RemoveActorAction())); 
            
            ScreenManager mgr = ScreenManager.getInstance();
            mgr.clearOpenMenu( ScreenId.OVERVIEW_SCREEN );
        }
    }
    
    
    
}

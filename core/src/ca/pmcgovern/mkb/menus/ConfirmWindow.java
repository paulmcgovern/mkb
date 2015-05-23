package ca.pmcgovern.mkb.menus;

import ca.pmcgovern.mkb.GameMain;
import ca.pmcgovern.mkb.screens.MkbScreen;
import ca.pmcgovern.mkb.fwt.TaskSprite;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    
    public static final String DELETE_CONFIRM = "DELETE_CONFIRM";
    
    public ConfirmWindow(String title, AssetManager assetMgr, TaskSprite taskSprite, ChangeListener deleteTaskListener ) {
        
        
        super( "", assetMgr.get( "data/icons.json", Skin.class ), "delete" );
        
        
        Skin skin = assetMgr.get( "data/icons.json", Skin.class );
                
        
        if( taskSprite == null ) {
            throw new IllegalArgumentException( "TaskSprite is null." );
        }
        
        
        
        TextureAtlas tableAtlas = assetMgr.get( "data/tables.pack", TextureAtlas.class );
        
        Container<Actor> c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top_left" )));
        
        this.add( c ).fill().expand();
        
        c = new Container<Actor>();        
        c.setBackground( new TextureRegionDrawable( tableAtlas.findRegion( "table_top" )));

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
        String taskDesc = taskSprite.getTaskDescription();
        
        
        Table buttonTable = new Table();  
        
        buttonTable.add( new Label( GameMain.getString("ARE_YOU_SURE" ), skin, "big" )).colspan( 2 );
        buttonTable.row();   
        
        buttonTable.add( new Label( GameMain.getString( "DELETE" ) + " " + taskDesc, skin, "tiny" )).colspan( 2 );
        buttonTable.row();  
        
       // PlayClickListener playClick = new PlayClickListener( effectMgr );
        CloseListener closer = new CloseListener();
        
        ImageTextButton itb = new ImageTextButton( GameMain.getString( "CANCEL" ), skin, "detail-cancel" );
        MkbScreen.layoutButton( itb ); 
        buttonTable.add( itb ).padLeft( 10 );
        itb.setName( "CANCEL" );
        itb.addListener( closer );
        itb.addListener( deleteTaskListener );        
        
        itb = new ImageTextButton( GameMain.getString( "DELETE" ), skin, "detail-delete" );
        MkbScreen.layoutButton( itb ); 
        itb.setName( DELETE_CONFIRM );
        buttonTable.add( itb ).padLeft( 10 );
        itb.addListener( closer ); 
        itb.addListener( deleteTaskListener );
          
        this.payload.setActor( buttonTable );       
   
        this.pack();
    }  

        
    class CloseListener extends ChangeListener {
            
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            ConfirmWindow.this.addAction( Actions.sequence( Actions.fadeOut( FADE_DURATION ),Actions.removeActor()));
        }
    }     
    
}

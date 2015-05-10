package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.events.PlayClickListener;
import ca.pmcgovern.mkb.menus.MkbMenu;
import static ca.pmcgovern.mkb.screens.OverviewScreen.FADE_DURATION;
import ca.pmcgovern.mkb.sprites.EffectManager;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class HelpScreen extends MkbScreen {

    public static final String TAG = "HelpScreen";
    private Stage uiStage;

    
    private Skin skin;
  
    private OrthographicCamera uiCamera;

   
    
    public HelpScreen(AssetManager assetMgr, EffectManager effectMgr ) {
          
        super( assetMgr, effectMgr );
    }

    
    @Override 
    public MkbScreen.ScreenId getId() {
        return MkbScreen.ScreenId.HELP;
    }
    
    @Override
    public void render(float delta) {
     
        Gdx.gl.glClearColor( 0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
        
   
        FrameBuffer screenBuff = null;
        
        if( this.nextScreenId != null ) {
          
            screenBuff = new FrameBuffer( Pixmap.Format.RGBA8888, this.width, this.height, false );  
            screenBuff.begin();            
        }   
        
        this.uiStage.act();       
        this.uiStage.draw();  
       
        
        if( screenBuff != null ) {
            
            screenBuff.end();
            
            ScreenManager s = ScreenManager.getInstance();
             
            TextureRegion t = new TextureRegion(screenBuff.getColorBufferTexture() );
            t.flip( false, true );
            s.setLastScreenImg( t );        
            s.showScreen( this.nextScreenId );
        }        
    }
    
    @Override
    public void resize(int width, int height) {
        this.nextScreenId = null;
        Gdx.app.log( TAG,  "resize...");

        PlayClickListener playClick = new PlayClickListener( this.effectMgr );
        this.width = width;
        this.height = height;
        
        this.uiStage.getViewport().update(width, height, true);
        
        
        Table contentTable = new Table( this.skin );
        
        
        Gdx.input.setInputProcessor(this.uiStage);
        
      
        String reallyLongString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus convallis, mi et suscipit fringilla, turpis nibh maximus elit, eget ultricies arcu nisi sed felis. Donec et malesuada sem. Suspendisse potenti. Maecenas sit amet aliquam tortor. Integer aliquam porta dapibus. Aliquam finibus non nisl id commodo. Donec sollicitudin velit tellus, ac bibendum enim egestas viverra. Quisque finibus leo aliquet, iaculis est a, ultricies elit. Sed maximus justo at purus malesuada gravida. Sed eu dictum lorem. Morbi neque risus, lacinia eu hendrerit sollicitudin, lobortis id magna. Maecenas venenatis laoreet leo, a mattis nibh malesuada in. Aliquam enim nisi, posuere quis dolor et, tempus blandit nisl.";

        reallyLongString += "\n\n\nPellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse lacinia libero enim, nec tincidunt quam molestie id. Nullam ullamcorper porttitor lacus eget finibus. Mauris elementum ligula eget congue iaculis. Sed consectetur eget augue et egestas. Suspendisse potenti. Etiam in felis sed mauris interdum eleifend eu nec mi. Donec convallis mi sed dolor feugiat accumsan. Phasellus tempus auctor mauris at vehicula. Etiam quis accumsan enim. Interdum et malesuada fames ac ante ipsum primis in faucibus. Duis malesuada lobortis turpis, vitae finibus ipsum volutpat non. Proin tempor, leo ut euismod hendrerit, arcu elit suscipit mi, eu lobortis nunc sem tempor risus. Aliquam eleifend condimentum mattis.";


        reallyLongString += "\n\n\nEtiam blandit risus risus. Nam egestas odio eget ultrices aliquet. Morbi in ipsum pretium, aliquet orci eu, fermentum eros. Etiam pharetra sapien ut ex eleifend aliquet non ac mi. Sed tincidunt augue erat, quis pulvinar quam pellentesque convallis. Cras non erat pellentesque, lacinia justo at, cursus urna. Nulla nulla velit, ultrices lobortis pulvinar vitae, dictum sed mauris. Nunc efficitur ipsum ultrices sodales dignissim. Nunc commodo porttitor semper. Suspendisse potenti. Aenean blandit accumsan cursus. Nam faucibus ac lectus eget tincidunt. Pellentesque consectetur malesuada est nec efficitur. Aliquam luctus massa ipsum, sed facilisis risus commodo molestie. Pellentesque non tincidunt ante, sed accumsan sem. Maecenas euismod libero orci, sed efficitur turpis pretium vehicula.";

        reallyLongString += "\n\n\nMorbi id est a justo egestas vestibulum ut at odio. Aenean nisl dolor, pretium at tempor non, imperdiet eget lacus. Duis at vestibulum nulla, ut dictum risus. Praesent enim sem, volutpat eu tempor a, vestibulum vitae eros. Ut egestas at risus ac ornare. Cras placerat pharetra libero in tempus. Donec ut nisl elit. ";
        
        final Label text = new Label(reallyLongString, skin);
        text.setAlignment(Align.center);
        text.setWrap(true);
        final Label text2 = new Label("This is a short string!", skin);
        text2.setAlignment(Align.center);
        text2.setWrap(true);
       
        contentTable.add( text ).width( this.width );
        contentTable.row();
        contentTable.add( text2 );
        contentTable.row();
        
        ImageTextButton itb = new ImageTextButton( "Done", skin, "help-done" );
        MkbScreen.layoutButton( itb );        
        itb.addListener(playClick);
        itb.addListener( new ChangeListener() {          
            
                @Override
                public void changed(ChangeEvent event, Actor actor) {  
                    HelpScreen.this.nextScreenId = MkbScreen.ScreenId.OVERVIEW_SCREEN;                    
                }
            } );        
        
        contentTable.add( itb );
        
        final ScrollPane scroller = new ScrollPane( contentTable );

        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand();

        this.uiStage.addActor(table);
        
        fadeIn( this.uiStage );
              
         
       
      
   //     this.taskLabelManager =  new TaskLabelManager(this.taskStage, this.labelGroup, this.skin.get( "default", LabelStyle.class ));
     // this.vignetteShader.begin();
      //  this.vignetteShader.setUniformf( "u_center", 100, 200 );
      //  this.vignetteShader.end();
    }
    

    @Override
    public void show() {
        this.nextScreenId = null;
        Gdx.app.log( TAG, "show..." );

        this.uiStage = new Stage(new ExtendViewport(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight() ));        
      
        this.skin = this.assetMgr.get( "data/icons.json", Skin.class );        
        
      
        
       // this.spriteBatch = new SpriteBatch();
     //   this.taskStage.getBatch().setShader( this.shader );
      //  this.spriteBatch.setShader( this.shader );

     //  this.gsdt = new GestDtct();
     //   GestureDetector gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, gsdt);
      
     
     //   Gdx.input.setInputProcessor( new InputMultiplexer( this.uiStage, this.taskStage, gestureDetector ));
     
      
    }
    

   
    
    @Override
    public void setOpenMenu( MkbMenu w ) {        
    }
  
    @Override
    public void clearOpenMenu() {        
    }
    
    @Override
    public void dispose() {
        this.uiStage.dispose();        
    }

    /**
     * Listener to drive app to another screen.
     * @author mcgovern
     *
     */
    class WinChangeListener extends ChangeListener {

        MkbScreen.ScreenId nextScreenId;
        
        public WinChangeListener( MkbScreen.ScreenId nextScreenId ) {
            this.nextScreenId = nextScreenId;
        }
        
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          
            ScreenManager.getInstance().showScreen( this.nextScreenId );
        }
    } 
    
    
}

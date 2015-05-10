package ca.pmcgovern.mkb.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Sine;
import ca.pmcgovern.mkb.GameMain;

import ca.pmcgovern.mkb.GamePreferences;
import ca.pmcgovern.mkb.actions.RemoveAction;
import ca.pmcgovern.mkb.events.MonsterListener;
import ca.pmcgovern.mkb.events.TaskGestureListener;
import ca.pmcgovern.mkb.events.TaskSavedListener;

import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.MonsterSprite;
import ca.pmcgovern.mkb.fwt.TaskSprite;

import ca.pmcgovern.mkb.ui.CameraTweenAccessor;
import ca.pmcgovern.mkb.ui.MenuTable;
import ca.pmcgovern.mkb.fwt.Task.TaskState;
import ca.pmcgovern.mkb.menus.newtask.TaskForm;
import ca.pmcgovern.mkb.ui.VignetteRadiusTweenAccessor;
import ca.pmcgovern.mkb.ui.ZoomControl;
import ca.pmcgovern.mkb.util.EmptyQuadrantFinder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.ArrayList;
import java.util.List;
//import ca.pmcgovern.mkb.events.TaskGestureListener;


/*
 * Borrows from:
 * http://www.gamefromscratch.com/post/2013/12/18/LibGDX-Tutorial-9-Scene2D-Part-3-UI-Skins.aspx
 * //private Mesh squareMesh;
    //   private TextureRegion foobarTex;
    // http://www.saschahlusiak.de/2012/10/opengl-antialiasing-in-android-with-transparent-textures/
    // http://www.acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
    // http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3915

 * @author mcgovern
 *
 */

// TODO: limit max items (20)
// TODO: colissions
// TODO: particle effects pooling
// TODO: particle effects zooming

// TODO: dispose of particle effects
// TODO: sound!
// TODO: all done message. 



public class OverviewScreen extends MkbScreen {

        
private Vector2 gorp = new Vector2(0,0);

    private Rectangle extents;
    public static final float FADE_DURATION = 0.35f;
    
    private Image lastScreenImage;
      
    public static final float MAX_ZOOM = 4;
    
    private static final int SHADOW_OFFSET_Y = 30;

    private static final int SHADOW_OFFSET_X = 20;

    public static final String TAG = "OverviewScreen";
    
    private TweenManager cameraTweenMgr;

    static final int ASSUMED_WIDTH  = 512;
    static final int ASSUMED_HEIGHT = 512;
   
  //  private TaskLabelManager taskLabelManager;

    private MonsterSprite monster;

    private static final float ASSUMED_ASPECT_RATIO = (float)ASSUMED_WIDTH/(float)ASSUMED_HEIGHT;
  
    private ShaderProgram dropShadowShader;
    private ShaderProgram passthroughShader;
    private ShaderProgram vignetteShader;    
    
    private Stage taskStage;
    
    private Stage uiStage;

    private Skin skin;
  
  //  private Group labelGroup;
    
  //  private Image bg;
  //  private Texture bg;
    //private float bg
    private TextureAtlas taskSprites;
  
 
    private FrameBuffer shadowBuff;
    private FrameBuffer screenBuff;
   
    private  GestDtct gsdt;
    private MkbMenu currentMenu;
    
    private TaskSprite focusTask;
    private TaskSprite activeTask;
    
//    ParticleEffect effect;
 
    
    @Override 
    public MkbScreen.ScreenId getId() {
        return MkbScreen.ScreenId.OVERVIEW_SCREEN;
    }

    private MenuTable mt;
    
    private TaskSpriteManager taskManager;

    private OrthographicCamera taskCamera;
 
   // private OrthographicCamera uiCamera;
   
  
   private ClickListener taskClickListener;
    
    public OverviewScreen(AssetManager assetMgr, EffectManager effectMgr ) {
          
        super( assetMgr, effectMgr );
        this.cameraTweenMgr= new TweenManager();
        Tween.registerAccessor(OrthographicCamera.class, new CameraTweenAccessor());
        Tween.registerAccessor( Vector2.class, new VignetteRadiusTweenAccessor() );
        
      
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;
        
   
        // Do-nothing shader
        this.passthroughShader = new ShaderProgram(Gdx.files.internal("data/shaders/passthrough.vert"),  Gdx.files.internal("data/shaders/passthrough.frag"));
        
        // Render drop shadows
        this.dropShadowShader = new ShaderProgram( Gdx.files.internal("data/shaders/monochrome.vert"),  Gdx.files.internal("data/shaders/monochrome.frag"));
        
        // Vignette around active task shader
        this.vignetteShader = new ShaderProgram( Gdx.files.internal("data/shaders/vignette.vert"),  Gdx.files.internal("data/shaders/vignette.frag"));
        
        if (!vignetteShader.isCompiled()) {
        System.err.println(vignetteShader.getLog());
        System.exit(0);
        }
        if (vignetteShader.getLog().length()!=0)
        System.out.println(vignetteShader.getLog());
        
                   
        if (!dropShadowShader.isCompiled()) {
        System.err.println(dropShadowShader.getLog());
        System.exit(0);
        }
        if (dropShadowShader.getLog().length()!=0)
        System.out.println(dropShadowShader.getLog());
                    
      
    }

    
    public void setFocusTask( TaskSprite ts ) {
        this.focusTask = ts;
    
        Gdx.app.log( TAG, "set focus task: " + ts );
        
        if( this.focusTask != null ) {
            
            // Setup vignette params
            this.blub = true;
            
            // Reset vignette radius
            this.gorp.set( 1, 1 );
        
            Tween.to( this.gorp, CameraTweenAccessor.XY_AXIS, 1 )
            .target( MIN_VIGNETTE_RADIUS )
            .ease( Expo.IN )        
            .start(this.cameraTweenMgr );
      
            this.pushCameraState();            
            
            float targetX = ts.getX();
            float targetY = ts.getY();
            OrthographicCamera cam = (OrthographicCamera)ts.getStage().getCamera();
            
            targetX = targetX - (cam.zoom * (ts.getWidth() / 2)); 
            targetY = targetY + (cam.zoom * (ts.getHeight() / 2)); 
       
            centerOn( targetX, targetY );
        }
    }
    
    
    public void clearFocusTask() {
        
        if( this.focusTask != null ) {
            this.focusTask = null;
            this.blub = true; 
              // Reset vignette radius
            this.gorp.set( MIN_VIGNETTE_RADIUS, MIN_VIGNETTE_RADIUS );
        
            Tween.to( this.gorp, CameraTweenAccessor.XY_AXIS, 1 )
            .target( 1 )
            .ease( Expo.IN )        
            .start(this.cameraTweenMgr );
     
           
        }
     
        Gdx.app.log( TAG, "Clear focus task." + this.taskFocusCameraPos );
        if( this.taskFocusCameraPos != null ) {
            this.popCameraState();
        }
    }

    
    
    
    
    // TODO: these don't belong here.. . Move to camera tween mgr?
    private Vector3 taskFocusCameraPos;
    
    // TODO: track zoom as well
    private void popCameraState() {
        
        if( this.taskFocusCameraPos != null ) {
            centerOn( this.taskFocusCameraPos.x, this.taskFocusCameraPos.y );
        }
        
        Gdx.app.log( TAG, "pop camera state"  + this.taskFocusCameraPos );
                
        this.taskFocusCameraPos = null;
    }
    
    private void pushCameraState() {
        
        Camera cam = this.taskStage.getCamera();
        this.taskFocusCameraPos = new Vector3( cam.position );
        
        Gdx.app.log( TAG, "push camera state: " + this.taskFocusCameraPos );
    }
    
    
    
    
    
    
    
    /**
     *  Center camera and vignette shader on the given task. 
     *  
     * @param ts
     */
    private static final float MIN_VIGNETTE_RADIUS = 0.1f;
    
    private void centerOn( float targX, float targY ) {
           
     
        OrthographicCamera camera = (OrthographicCamera)this.taskStage.getCamera();
        this.cameraTweenMgr.killTarget( camera );         
        Vector3 targetPos = new Vector3( targX, targY, 0 );
     
        // TODO calc offset better. Set it halfway between menu and edge?
      //  targetPos.x = targetPos.x - (camera.zoom * (ts.getWidth() / 2));
      //  targetPos.y = targetPos.y + (camera.zoom * (ts.getHeight() / 2));
        
        Gdx.app.log( TAG,  "Center camera to: " + targetPos );
      
        // Reset vignette radius
        /**
        this.gorp.set( 1, 1 );
        
        Tween.to( this.gorp, CameraTweenAccessor.XY_AXIS, 1 )
        .target( MIN_VIGNETTE_RADIUS )
        .ease( Expo.IN )        
        .start(this.cameraTweenMgr );
     **/
        Tween.to( camera, CameraTweenAccessor.XY_AXIS, 1 )                
        .target(targetPos.x, targetPos.y, 0 )
        .ease( Expo.INOUT)            
        .start( this.cameraTweenMgr ); 
    }

    
    private void writeDirty() {
        
        Array<Actor> allTasks = this.taskStage.getActors();
        
        if( allTasks == null || allTasks.size == 0 ){
            return;
        }
        
        for( int i = 0; i < allTasks.size; i++ ) {
            
            Actor a = allTasks.get( i );
            if( a instanceof TaskSprite && ((TaskSprite)a).isDirty() ) {
                
                if( TaskState.DELETED == ((TaskSprite)a).getTask().getState() ) {
                    
                    this.taskManager.delete( (TaskSprite)a );                    
                    a.addAction( Actions.sequence( Actions.fadeOut( 0.75f ), Actions.removeActor()) );
                    
                } else if( TaskState.COMPLETED == ((TaskSprite)a).getTask().getState() ) {
                                      
                    this.effectMgr.startDoneEffect( a );
                    this.taskManager.save( (TaskSprite)a );    
                    
                } else {
                    
                    this.taskManager.setState( (TaskSprite)a, ((TaskSprite)a).getTask().getState(), TaskSpriteManager.DrawContext.OVERVIEW);
                    this.taskManager.save( (TaskSprite)a );  
                }
            }
        }
    }
    
    private void checkCollisions() {
        
        boolean collisionFound = false;
        
        Array<Actor> allActors = this.taskStage.getActors();
        
        if( allActors == null || allActors.size < 2 ) {
            return;
        }
        
        List<Circle> collisionBounds = new ArrayList<Circle>();
           
        for( int i = 0; i < allActors.size; i++ ) {
                        
            Actor a = allActors.get( i );
            
            if(!(a instanceof TaskSprite )) {
                continue;
            }
                                  
            collisionBounds.add( new Circle( a.getX(), a.getY(), 2 + a.getWidth()/2  ));
        }
        
        int count = collisionBounds.size();
        
        Circle c1 = null;
        Circle c2 = null;
        
        for( int i = 0; i < count; i++ ) {
            
            c1 = collisionBounds.get( i );
            
            for( int j = 0; j < count; j++ ) {                
                
                if( j == i ) {
                    continue;
                }
                 
                c2 = collisionBounds.get( j );
                
                if( c1.overlaps(c2)) {
                    collisionFound = true;
                    break;
                }
            }
            
            if( collisionFound ) {
                break;
            }
        }
        
        if( collisionFound ) {
             this.effectMgr.collision( this.taskStage, new Vector2( c1.x, c2.y), new Vector2( c1.x, c2.y ));
        }       
    }
           
    @Override
    public void render(float delta) {
       
        FrameBuffer transitionBuff = null;
        
        if( this.nextScreenId != null ) {
              transitionBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
              transitionBuff.begin();
        }
        
        
        // Persist any state changes to Tasks
        writeDirty();
        

        
        this.taskStage.act( delta );        
        this.uiStage.act( delta );  

        
        checkCollisions();
                
                
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
       
        
        this.cameraTweenMgr.update( delta ); 
        
        // There is a single SpriteBatch, it happens
        // to be part of the task stage.        
        Batch bspriteBatch = this.taskStage.getBatch();
        
        // Move the camera to render 
        // the drop shadow into the FBO.
        float cx = this.taskCamera.position.x;
        float cy = this.taskCamera.position.y;
        
        this.taskCamera.position.x = cx - SHADOW_OFFSET_X;
        this.taskCamera.position.y = cy + SHADOW_OFFSET_Y;        
        this.taskCamera.update();       
        
        bspriteBatch.setShader( this.dropShadowShader );
        
        // Render shadows for all tasks.          
        this.shadowBuff.begin();  
    
        Gdx.gl.glClearColor( 0f, 0f, 0f, 0f );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT );
         
        Gdx.gl.glEnable(GL20.GL_BLEND );
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        
        this.taskStage.draw();         
        this.shadowBuff.end();
                         
        // Restore camera to
        // regular position
        this.taskCamera.position.x = cx;
        this.taskCamera.position.y = cy;     
        this.taskCamera.update();  
               
                
        TextureRegion shadowRegion = new TextureRegion( this.shadowBuff.getColorBufferTexture() );   
        shadowRegion.flip( false, true );
       
        bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        
     
        this.screenBuff.begin();
           
        bspriteBatch.begin();
        bspriteBatch.setShader( this.passthroughShader );
        
       drawBackground( bspriteBatch );
        // Background will repeat over whole screen.
    //    bspriteBatch.draw( this.bg, 0,0, 0,0, this.width, this.height );

        // Draw task icon shadows      
        bspriteBatch.draw( shadowRegion, 0, 0, this.width, this.height );    
        bspriteBatch.end();
     
        this.effectMgr.updateAll( this.taskStage );
        
        // Draw fancy effect around active task, etc.              
       // this.effectMgr.updateInProgressEffect( getActiveTask() );
        
        bspriteBatch.begin();
        this.effectMgr.draw(bspriteBatch, delta);
        bspriteBatch.end();
        this.taskStage.draw();  
          
        this.screenBuff.end();
     //  System.err.println( "blub " + this.blub )         ;
        // Apply vignette shader if
        // there is a focused task
        // Vignette centered on task
        //if( this.focusTask != null ) {
         if( this.blub )    {
             
            bspriteBatch.setShader( this.vignetteShader );            
            
            //if( this.gorp.x == 1 || this.gorp.x == MIN_VIGNETTE_RADIUS ) {                
            //    this.blub = false;
            //}
            
            if( this.focusTask != null ) {
   //   this.cameraTweenMgr.
            
           // this.gorp.x
                    
            Vector2 vignetteCenter = this.taskStage.stageToScreenCoordinates(
                    new Vector2(this.focusTask.getX() + (this.focusTask.getWidth()/2f), 
                    (this.focusTask.getY() + (this.focusTask.getHeight()/2f) )));
            
            vignetteCenter.y = this.height - vignetteCenter.y;
            this.vignetteShader.begin();
        //    System.err.println( this.height + ":"+  Gdx.graphics.getHeight() + " "  + this.focusTask.getX() + " " + this.focusTask.getY() + " c:"+  vignetteCenter );
            this.vignetteShader.setUniformf( "u_center", vignetteCenter.x, vignetteCenter.y );
            this.vignetteShader.setUniformf( "u_innerRadius", this.gorp.x );
            this.vignetteShader.setUniformf( "u_outerRadius", this.gorp.x * 4 );   
            this.vignetteShader.end();
            }
        }
        
        
        TextureRegion vignetted = new TextureRegion( this.screenBuff.getColorBufferTexture() );  
        vignetted.flip( false, true );
        bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        

        
        bspriteBatch.begin();        
        bspriteBatch.draw( vignetted, 0, 0, this.width, this.height ); 
        bspriteBatch.end(); 
        
        
        
        // Render tasks           
        this.taskStage.draw();  

     //       this.uiStage.addActor( this.lastScreenImage );
     //   }    
        
        // No shaders for UI elements
        bspriteBatch.setShader( this.passthroughShader );
        this.uiStage.draw();

        
      //  if( this.lastScreenImage != null ) {
      //      bspriteBatch.begin();
      //      bspriteBatch.draw( this.lastScreenImage, 0,0 );
       //     bspriteBatch.end();
       // }
        
        
        if( this.nextScreenId != null && transitionBuff != null ) {
           
              transitionBuff.end();
 ScreenManager s = ScreenManager.getInstance();
             
            TextureRegion t = new TextureRegion(screenBuff.getColorBufferTexture() );
            t.flip( false, true );
            s.setLastScreenImg( t );        
            s.showScreen( this.nextScreenId );              
        }
    
    }

    
    private TaskSprite getActiveTask() {
 
        Array<Actor> tasks = this.taskStage.getActors();
  
        if( tasks == null || tasks.size == 0 ) {         
            return null;
        }
      
        TaskSprite ts = null; 
        for( int i = 0; i < tasks.size; i++ ) {
            Actor a = tasks.get( i );
      
            if( a!= null && a instanceof TaskSprite ) {
          
                if( ((TaskSprite)a).getTask().getState() == TaskState.IN_PROGRESS ) {                    
              
                    ts = (TaskSprite)a;
                   
                    break;
                }
            }
        }

        return ts;
                
    }
  //  private boolean vignetteTarget
/*
    private void updateActiveTaskDecoration() {
        
        Array<Actor> tasks = this.taskStage.getActors();
  
        if( tasks == null || tasks.size == 0 ) {         
            return;
        }
        Vector2 effectCenter = null;
  
        for( int i = 0; i < tasks.size; i++ ) {
            Actor a = tasks.get( i );
      
            if( a!= null && a instanceof TaskSprite ) {
          
                if( ((TaskSprite)a).getTask().getState() == TaskState.IN_PROGRESS ) {                    
              
                    effectCenter = this.taskStage.stageToScreenCoordinates( new Vector2(a.getX(), a.getY() ));
                    System.err.println( "EFC:" + effectCenter );
                    break;
                }
            }
        }

        if( effectCenter == null ) {
            return;
        }
        
        this.effect.setPosition( effectCenter.x,  Gdx.graphics.getHeight() - effectCenter.y);
    }
    
  */
    /*  
    private void decorateActiveTask() {
              
        Array<Actor> tasks = this.taskStage.getActors();
        
        if( tasks == null || tasks.size == 0 ) {
            return;
        }
      
        boolean hasActiveTask = false;
        
        for( int i = 0; i < tasks.size; i++ ) {
            Actor a = tasks.get( i );
            
            if( a!= null && a instanceof TaskSprite ) {
                
                if( ((TaskSprite)a).getTask().getState() == TaskState.IN_PROGRESS ) {                    
                    
                    hasActiveTask = true;
                   break;
                }
            }
        }
        
        if( hasActiveTask) {
        
            this.effect.start();
        }
    }
*/
 
  
    
    @Override
    public void resize(int width, int height) {

        Gdx.app.log( TAG,  "resize...");
       
        this.width = width;
        this.height = height;
         
        this.nextScreenId = null;
        
      // if( lastScreen != null ){
          
         //  this.lastScreenImage = new TextureRegion( lastScreen );
        //   Image lastScreenImage = new Image( lastscreen );
        //   this.ui
          // this.lastScreenImage = new Image( lastscreen);
       //    this.lastScreenImage.
           // lastScreenRegion.flip( false, true );
       //}
      
      
        
        
        //System.err.println( ((OrthographicCamera)this.taskCamera).viewportHeight );
        // Extents       
        float maxX = width  * MAX_ZOOM;
        float maxY = height * MAX_ZOOM;
     //  this.taskStage.getViewport().
        this.extents = new Rectangle( 0, 0, maxX, maxY);
        this.extents.setCenter(0,0);
        
   //     System.err.println( "Extents X: " + this.extents.getX() );        
   //     System.err.println( "Our extents:" + this.extents);
        
        Table k = new Table();
        k.add( new Container() ).width( this.extents.getWidth() - 5 ).height( this.extents.getHeight() ); //extents.width -5).height( extents.height - 5);
        k.debug();
                this.taskStage.addActor( k );
       // k.setX( -1590 );
        
      //  k.setY( -790);

        
        Table ct = new Table();
        ct.add( new Container()).width( 40 ).height( 40 );
        ct.setX(0 );
        ct.setY( 0 );
        ct.debug();
        this.taskStage.addActor( ct );
        
     //   System.err.println( "Debug K " + k.getX() + " " + k.getY() );
        // TODO: zoom. The extents will be the max out
        
        Vector2 cc=new Vector2(1,1);
        this.extents.getCenter(cc);
   //     System.err.println( "CC:"+this.extents.getCenter(cc) );
    
        float aspect = (float) width / (float) height;
           
        this.taskStage.getViewport().update(width, height, true);
        this.uiStage.getViewport().update(width, height, true);
 
        this.taskCamera.viewportHeight = height;
        this.taskCamera.viewportWidth = width;
        this.taskCamera.position.x = 0;
        this.taskCamera.position.y = 0;
        
                
        ZoomControl zoomControl = new ZoomControl( (OrthographicCamera)this.taskStage.getCamera(), skin, MAX_ZOOM, this.extents );
        zoomControl.setPosition( this.width - 30, 0 );
  
        this.setBackground();
        
      //  this.bg = this.assetMgr.get( "data/lined_paper.png", Texture.class ); //Yellow_notebook_paper.jpg", Texture.class ); // lined_paper.png" ); 
      //  this.bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
    

        this.uiStage.addActor( zoomControl );
        
        this.vignetteShader.begin();
        this.vignetteShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.vignetteShader.end();
        
        // Format.ALPHA not supported HTC Desire C ?
        this.shadowBuff = new FrameBuffer( Format.RGBA8888, (int)(this.width * 0.65 ), (int)(this.height * 0.65 ), false );

        this.screenBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
        
        
        this.taskManager = new TaskSpriteManager( this.assetMgr );//askManager.getInstance();
       
                 
        this.monster = new MonsterSprite( this.skin.getSprite( "monster" ));
     
        this.monster.addListener( new MonsterListener( this.taskManager, this, this.skin, this.effectMgr ) );
        
        // get notified when a new task is ready.
        this.monster.addListener(new TaskSavedListener( this.taskManager, this.extents, this.taskStage ) );
        this.uiStage.addActor( this.monster );
         
          
        
     //   TaskShowDetailListener taskClickListener = new TaskShowDetailListener( this.uiStage, this.skin );
        TaskGestureListener tgl = new TaskGestureListener( this.uiStage, this.extents, this.effectMgr );
        
        
        LabelStyle taskLabelDflt = this.skin.get( "default", LabelStyle.class );
   
        TaskSpriteManager taskManager = new TaskSpriteManager( this.assetMgr );
        
        List<ca.pmcgovern.mkb.fwt.TaskSprite> allTaskSprites = taskManager.init( this.extents, TaskSpriteManager.DrawContext.OVERVIEW );
        
        int count = allTaskSprites.size();
        
        for( int i = 0; i < count; i++ ) {
            
            ca.pmcgovern.mkb.fwt.TaskSprite t = allTaskSprites.get(i);
         
            t.setWidth( t.getWidth() );
            t.setHeight( t.getHeight() );
           // t.addListener( taskClickListener );
            
            t.addListener( tgl );
            
            this.taskStage.addActor( t );
        }
        
     //this.taskClickListener = new MoveCameraClickListener( this.cameraTweenMgr );

        
    //    this.taskManager.init( new TaskSpriteFactory( this.taskSprites, taskLabelDflt, taskClickListener, tgl ), this.taskStage, this.assetMgr );
    /**
     * this.taskManager.init( new TaskSpriteFactory( this.taskSprites, taskLabelDflt, tgl ), this.taskStage, this.assetMgr, this.extents );
     * *
     * *
     * */
   //     this.taskLabelManager.init();
  
       // this.vignetteShader.begin();
      //  this.vignetteShader.setUniformf( "u_center", 100, 200 );
      //  this.vignetteShader.end();
        
        
        // Restore camera position and zoom control state
        GamePreferences prefs = GamePreferences.getInstance();    
        prefs.restoreCameraState( this.taskCamera );
        this.taskCamera.update();  
        zoomControl.setValue( this.taskCamera.zoom );
      
        
        fadeIn( this.uiStage );
        
    }
    

    private void disableTaskEvents() {
        Array<Actor> allActors = this.taskStage.getActors();
        if( allActors == null ) {
            return;
        }
        int count = allActors.size;
        for( int i = 0; i < count; i++ ) {
            allActors.items[ i ].setTouchable( Touchable.disabled );
        }
    }
    
    private void enableTaskEvents() {
        Array<Actor> allActors = this.taskStage.getActors();
        if( allActors == null ) {
            return;
        }
        int count = allActors.size;
        for( int i = 0; i < count; i++ ) {
            allActors.items[ i ].setTouchable( Touchable.enabled );
        }        
    }
    
    @Override
    public void show() {

        Gdx.app.log( TAG, "show..." );
       this.nextScreenId = null;
       
        clearFocusTask();
        GameMain.editTargetId = GameMain.NOT_SET;
        
        this.taskStage = new Stage(new ExtendViewport(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight() ));

        this.uiStage = new Stage(new ExtendViewport(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight() ), this.taskStage.getBatch() );        
     
        
        this.taskSprites = this.assetMgr.get( "data/tasks.atlas", TextureAtlas.class );
       
        this.skin = this.assetMgr.get( "data/icons.json", Skin.class );        
        

        // Initialize zoom  
       
      
        
       
       
 //       ((OrthographicCamera)this.taskStage.getCamera()).update();
        
       // this.spriteBatch = new SpriteBatch();
     //   this.taskStage.getBatch().setShader( this.shader );
      //  this.spriteBatch.setShader( this.shader );

       this.gsdt = new GestDtct();
        GestureDetector gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, gsdt);
      
     
        Gdx.input.setInputProcessor( new InputMultiplexer( this.uiStage, this.taskStage, gestureDetector ));
     
        
     //   this.labelGroup = new Group();    
 //       this.uiStage.addActor( this.labelGroup );
        
        // IS this required?
     //   this.uiStage.addListener( new ChangeScreenListener() );        
        
        this.taskCamera = (OrthographicCamera)this.taskStage.getCamera();
        
       // this.effectMgr = EffectManager.getInstance();
        
     //   this.effect = new ParticleEffect();
     //   this.effect.load(Gdx.files.internal("data/inProgress.p"), Gdx.files.internal("data"));
      
    }
    

    
    @Override
    public void setOpenMenu( MkbMenu m ) {
      
        Gdx.app.log( TAG, "Set open menu: " + m );
                
       // Table k = new MainTable( this.skin );
        this.currentMenu = m;
        
        this.currentMenu.setX( this.uiStage.getWidth() / 6 );
        this.currentMenu.setY( this.uiStage.getHeight() / 6 );     
        this.currentMenu.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn( 0.3f)));        
        
        this.uiStage.addActor( this.currentMenu );      
        this.gsdt.disable();
        this.disableTaskEvents();
       
    }
   
    private boolean blub;

    /** Clear menu and set the currently focused task to Null.
     * Also enable gesture detect.
     */
    @Override
    public void clearOpenMenu() { 
        
        Gdx.app.log( TAG, "Clear open menu: " +  this.currentMenu );
     
        if( this.currentMenu != null ) {
            this.currentMenu.addAction( Actions.sequence( Actions.fadeOut( 0.3f ), Actions.removeActor() ));            
        }
        
        this.currentMenu = null;
        
        if( this.focusTask != null ) {
            this.focusTask = null;
            this.blub = false;
        }
        
        this.popCameraState();
        this.gsdt.enable();
        this.enableTaskEvents();
    }
    
    
    
    @Override
    public void hide() {  
        Gdx.app.log( TAG,  "hide...");
        Gdx.input.setInputProcessor( null );   

        GamePreferences prefs = GamePreferences.getInstance();
           
        // Save camera state
        prefs.saveCameraState( this.taskCamera );
        
        // Save task positions.
        Array<Actor> tasks = this.taskStage.getActors();
              
       
        // Only update tasks that hve moved.
        if( tasks != null && tasks.size > 0 ) {
            
            for( int i = 0; i < tasks.size; i++ ) {
                
                Actor a = tasks.get( i );
                
                if( a instanceof TaskSprite ) {
                    if( ((TaskSprite)a).isHasMoved() || ((TaskSprite)a).isDirty() ) {               
                        this.taskManager.save( (TaskSprite)a ); 
                    }
                }
               // TaskSprite ts = (TaskSprite)tasks.get( i );
                
                
            }
        }
    }
    
    
    @Override
    public void dispose() {
        Gdx.app.log( TAG,  "dispose...");
        // TODO: fonts?
      //  this.spriteAtlas.dispose();       
        this.taskStage.dispose();   
        this.uiStage.dispose();   
  // TODO: do I have to dispose() FBO?
    }   
   

    class GestDtct implements GestureListener {

        float velX, velY;
        boolean flinging = false;
        boolean enabled = true;
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void enable() {
            this.enabled = true; 
        }
        
        public void disable() {
            this.enabled = false;
        }
                
        

        public boolean touchDown (float x, float y, int pointer, int button) {
            flinging = false;          
            return false;
        }

        @Override
        public boolean tap (float x, float y, int count, int button) {
            Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
        //    OverviewScreen.this.vignetteShader.begin();
        //    OverviewScreen.this.vignetteShader.setUniformf( "u_center", x, Gdx.graphics.getHeight() - y );
       //     OverviewScreen.this.vignetteShader.end();
            return false;
        }

        @Override
        public boolean longPress (float x, float y) {
            Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
            flinging = true;
     //       velX = taskCamera.zoom * velocityX * 0.5f;
    //        velY = taskCamera.zoom * velocityY * 0.5f;
            return false;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            
            if( !this.enabled ) {
                return true;
            }
            
            Gdx.app.log("GestureDetectorTest", "pan :" + deltaX + ", " + deltaY + " " + taskCamera.position + " " + extents + " " +width + "x"+height + " z:"+ taskCamera.zoom );
    
            float effectiveViewportWidth = taskCamera.viewportWidth * taskCamera.zoom;
            float effectiveViewportHeight = taskCamera.viewportHeight * taskCamera.zoom;
          
            // Describe the *next* viewport and compare with extents.
            Rectangle vp = new Rectangle( 0,0, effectiveViewportWidth, effectiveViewportHeight );
            vp.setCenter( taskCamera.position.x -(deltaX * taskCamera.zoom), taskCamera.position.y + (deltaY * taskCamera.zoom));
           
            if( extents.contains(vp)) {
                   
              OverviewScreen.this.taskCamera.position.add(-deltaX * taskCamera.zoom, deltaY * taskCamera.zoom, 0);
          
            } else {
                Gdx.app.log( "GestureDetectorTest", "Extents exceeded." );
            }
            
            
            return false;
        }

      //  @Override
     //   public boolean panStop (float x, float y, int pointer, int button) {
     //       Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
     //       return false;
     //   }

        @Override
        public boolean zoom (float originalDistance, float currentDistance) {
            float ratio = originalDistance / currentDistance;
            //    camera.zoom = initialScale * ratio;
            //   System.out.println(camera.zoom);
            return false;
        }

        @Override
        public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
            return false;
        }

        public void update () {
            if (flinging) {
                velX *= 0.98f;
                velY *= 0.98f;
                OverviewScreen.this.taskCamera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
                if (Math.abs(velX) < 0.01f) velX = 0;
                if (Math.abs(velY) < 0.01f) velY = 0;
            }
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            // TODO Auto-generated method stub
            return false;
        }

    }
    
    
    
    

}

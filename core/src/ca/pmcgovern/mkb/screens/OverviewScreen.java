package ca.pmcgovern.mkb.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Expo;
import ca.pmcgovern.mkb.GameMain;

import ca.pmcgovern.mkb.GamePreferences;
import ca.pmcgovern.mkb.actions.SetCollideEnableAction;
import ca.pmcgovern.mkb.events.MonsterListener;
import ca.pmcgovern.mkb.events.OverviewGestureListener;
import ca.pmcgovern.mkb.events.TaskGestureListener;
import ca.pmcgovern.mkb.events.TaskSavedListener;
import ca.pmcgovern.mkb.fwt.Task;
import ca.pmcgovern.mkb.fwt.TaskSpriteManager;
import ca.pmcgovern.mkb.menus.MkbMenu;
import ca.pmcgovern.mkb.sprites.EffectManager;
import ca.pmcgovern.mkb.sprites.MonsterSprite;
import ca.pmcgovern.mkb.fwt.TaskSprite;

import ca.pmcgovern.mkb.ui.CameraTweenAccessor;
import ca.pmcgovern.mkb.ui.MenuTable;
import ca.pmcgovern.mkb.fwt.Task.TaskState;
import ca.pmcgovern.mkb.menus.ConfirmWindow;
import static ca.pmcgovern.mkb.menus.ConfirmWindow.DELETE_CONFIRM;
import ca.pmcgovern.mkb.menus.TaskDetailsMenu;
import static ca.pmcgovern.mkb.menus.TaskDetailsMenu.DELETE;
import static ca.pmcgovern.mkb.menus.TaskDetailsMenu.EDIT;
import ca.pmcgovern.mkb.menus.newtask.TaskForm;
import ca.pmcgovern.mkb.ui.VignetteRadiusTweenAccessor;
import ca.pmcgovern.mkb.ui.ZoomControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
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
    private ShaderProgram bgShader;
    
    private FrameBuffer bgBuff;
   
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
   
   private  OverviewGestureListener gsdt;
    private MkbMenu currentMenu;
    
    private TaskSprite focusTask;
    private TaskSprite activeTask;
    
    private TaskSavedListener taskSavedListener;
    private TaskGestureListener tgl;
    private TaskDetailMenuOpener tdm;
      
    
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
       
        this.bgShader = new  ShaderProgram( Gdx.files.internal("data/shaders/bgVignette.vert"),  Gdx.files.internal("data/shaders/bgVignette.frag"));
        
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
            
            if( a == null ) {
                continue;
            }
            
            if( a instanceof TaskSprite && ((TaskSprite)a).isDirty() ) {
                
                TaskSprite ts = (TaskSprite)a;
                
                Task t = ts.getTask();
                
                if( t == null ) {                
                    continue;
                }
                
                TaskState state = t.getState();
                
                if( TaskState.DELETED == state ) {
                    
                    this.taskManager.delete( ts );  
                    ts.clearCollideEnable();
                    ts.addAction( Actions.sequence( Actions.moveTo( ts.getX(), this.extents.y + 200, 1.5f, Interpolation.bounceIn ), Actions.removeActor() ));
                    
                } else if( TaskState.COMPLETED == state ) {
                                      
                    this.effectMgr.startDoneEffect( ts );
                    this.taskManager.setState( ts, state, TaskSpriteManager.DrawContext.OVERVIEW );
                    this.taskManager.save( ts );    
                    
                } else if( TaskState.NEW == ((TaskSprite)a).getTask().getState() ) {
                  
                    this.taskManager.init( ts, TaskSpriteManager.DrawContext.OVERVIEW );
                    ts.addListener( this.tdm );
                    ts.addListener( this.tgl );            
                    this.taskManager.save( ts ); 
                    ts.clearCollideEnable();
                    float destY = ts.getY();
                    ts.setY( this.extents.height + 200 );                 
                    ts.addAction( Actions.sequence( Actions.moveTo(ts.getX(), destY, 1.5f, Interpolation.bounceOut ), new SetCollideEnableAction() ));
                    
                } else {
                    
                    this.taskManager.setState( ts, state, TaskSpriteManager.DrawContext.OVERVIEW );
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
           
            if( !((TaskSprite)a).getCollideEnable() ) {
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
        
        // Persist any state changes to Tasks
        writeDirty();
                
        FrameBuffer transitionBuff = null;
        
        if( this.nextScreenId != null ) {
              transitionBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
              transitionBuff.begin();
        }
        
        
        this.taskStage.act( delta );        
        this.uiStage.act( delta );  
     
        checkCollisions();
        this.cameraTweenMgr.update( delta ); 
             
       
        // There is a single SpriteBatch, it happens
        // to be part of the task stage.        
        Batch bspriteBatch = this.taskStage.getBatch();
      
        this.bgShader.begin();      
        this.bgShader.setUniformf( "resolution", this.width, this.height );
        this.bgShader.end();        
        
        Gdx.gl.glFlush();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );  
   
        Gdx.gl.glEnable(GL20.GL_BLEND );
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
     
        bspriteBatch.getProjectionMatrix().setToOrtho2D( 0, 0, this.width, this.height );        
     
        
        
        
        // Render vignette background
        this.bgBuff.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );  
   
        if( this.blub )    {
             
            bspriteBatch.setShader( this.vignetteShader );            
                        
            if( this.focusTask != null ) {
        
                Vector2 vignetteCenter = this.taskStage.stageToScreenCoordinates(
                    new Vector2(this.focusTask.getX() + (this.focusTask.getWidth()/2f), 
                    (this.focusTask.getY() + (this.focusTask.getHeight()/2f) )));
            
                vignetteCenter.y = this.height - vignetteCenter.y;
                this.vignetteShader.begin();
        
                this.vignetteShader.setUniformf( "u_center", vignetteCenter.x, vignetteCenter.y );
                this.vignetteShader.setUniformf( "u_innerRadius", this.gorp.x );
                this.vignetteShader.setUniformf( "u_outerRadius", this.gorp.x * 4 );   
                this.vignetteShader.end();
            
             
            } else {
                bspriteBatch.setShader( this.passthroughShader );
            }
        }
        
        bspriteBatch.begin();
    // ..  bspriteBatch.setShader( this.vignetteShader );//bgShader);
        drawBackground( bspriteBatch );
        bspriteBatch.end();
        
        this.bgBuff.end();
        
        TextureRegion bgRegion = new TextureRegion( this.bgBuff.getColorBufferTexture() );   
        bgRegion.flip( false, true );
        
        
        
        bspriteBatch.begin();
        bspriteBatch.setShader( this.bgShader );        
        bspriteBatch.draw(bgRegion, 0,0);
        bspriteBatch.end();
        
     
        
        
        // Move the camera to render 
        // the drop shadow into the FBO.
        float cx = this.taskCamera.position.x;
        float cy = this.taskCamera.position.y;
        
        this.taskCamera.position.x = cx - SHADOW_OFFSET_X;
        this.taskCamera.position.y = cy + SHADOW_OFFSET_Y;        
        this.taskCamera.update();       
        
       
        bspriteBatch.setShader( this.dropShadowShader );
       
        this.taskStage.draw();         
  //      this.shadowBuff.end();
                                 
        // Restore camera to
        // regular position
        this.taskCamera.position.x = cx;
        this.taskCamera.position.y = cy;     
        this.taskCamera.update();  
    
   
        if( this.blub  ) {
             bspriteBatch.setShader( this.vignetteShader );
        } else {
            bspriteBatch.setShader( this.passthroughShader );
        }
         this.taskStage.draw();
         
         
        
        // No shaders for UI elements
        bspriteBatch.setShader( this.passthroughShader );
        this.uiStage.draw();

       
        
        
        if( this.nextScreenId != null && transitionBuff != null ) {
           
            transitionBuff.end();
            ScreenManager s = ScreenManager.getInstance();
             
            TextureRegion t = new TextureRegion(transitionBuff.getColorBufferTexture() );
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
     
    @Override
    public void resize(int width, int height) {

        Gdx.app.log( TAG,  "resize...");
       
        this.width = width;
        this.height = height;
         
        this.nextScreenId = null;
        
        // Extents       
        float maxX = width  * MAX_ZOOM;
        float maxY = height * MAX_ZOOM;
     //  this.taskStage.getViewport().
        this.extents = new Rectangle( 0, 0, maxX, maxY);
        this.extents.setCenter(0,0);
        
   //     System.err.println( "Extents X: " + this.extents.getX() );        
   //     System.err.println( "Our extents:" + this.extents);
        
            this.gsdt = new OverviewGestureListener( this.taskCamera, this.extents );
        GestureDetector gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, gsdt);
     
        Gdx.input.setInputProcessor( new InputMultiplexer( this.uiStage, this.taskStage, gestureDetector ));
     
        // TODO: zoom. The extents will be the max out
        
    //    Vector2 cc=new Vector2(1,1);
    //    this.extents.getCenter(cc);
   
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

        
        this.bgShader.begin();
        this.bgShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.bgShader.setUniformf( "u_center", this.width / 2 , this.height / 2  );
        this.bgShader.setUniformf( "u_innerRadius", 0.9f );
        this.bgShader.setUniformf( "u_outerRadius", 1f);           
        this.bgShader.end();        
        
        // Format.ALPHA not supported HTC Desire C ?
       this.shadowBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );// (int)(this.width * 0.65 ), (int)(this.height * 0.65 ), false );

        this.screenBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
           this.bgBuff = new FrameBuffer( Format.RGBA8888, this.width, this.height, false );
     
        
        this.taskManager = new TaskSpriteManager( this.assetMgr );
       
                 
        this.monster = new MonsterSprite( this.skin.getSprite( "monster" ));
     
        
        // get notified when a new task is ready.
        this.taskSavedListener = new TaskSavedListener( this.taskManager, this.extents, this.taskStage );
                
        this.monster.addListener( new MonsterListener( this.taskManager, this, this.skin, this.effectMgr, this.taskSavedListener ));
        
        this.uiStage.addActor( this.monster );
         
        
     //   TaskShowDetailListener taskClickListener = new TaskShowDetailListener( this.uiStage, this.skin );
        this.tgl = new TaskGestureListener( this.uiStage, this.extents, this.effectMgr );
        this.tdm = new TaskDetailMenuOpener();
         
      
        
        
        
        LabelStyle taskLabelDflt = this.skin.get( "default", LabelStyle.class );
   
  //      TaskSpriteManager taskManager = new TaskSpriteManager( this.assetMgr );
        
        List<ca.pmcgovern.mkb.fwt.TaskSprite> allTaskSprites = this.taskManager.init( this.extents, TaskSpriteManager.DrawContext.OVERVIEW );
        
        int count = allTaskSprites.size();
        
        for( int i = 0; i < count; i++ ) {
            
            ca.pmcgovern.mkb.fwt.TaskSprite t = allTaskSprites.get(i);
         
            t.setWidth( t.getWidth() );
            t.setHeight( t.getHeight() );
           // t.addListener( taskClickListener );
            t.addListener( tdm );
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

        this.uiStage = new Stage(new ExtendViewport(  Gdx.graphics.getWidth(), Gdx.graphics.getHeight() )); ///, this.taskStage.getBatch() );        
     
        
        this.taskSprites = this.assetMgr.get( "data/tasks.atlas", TextureAtlas.class );
       
        this.skin = this.assetMgr.get( "data/icons.json", Skin.class );        
        

        // Initialize zoom  
       
      
        
       
       
 //       ((OrthographicCamera)this.taskStage.getCamera()).update();
        
       // this.spriteBatch = new SpriteBatch();
     //   this.taskStage.getBatch().setShader( this.shader );
      //  this.spriteBatch.setShader( this.shader );
     this.taskCamera = (OrthographicCamera)this.taskStage.getCamera();
   
   
        
     //   this.labelGroup = new Group();    
 //       this.uiStage.addActor( this.labelGroup );
        
        // IS this required?
     //   this.uiStage.addListener( new ChangeScreenListener() );        
        
        
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
   

    
    /** Open the task detail menu  **/
    /** set up edit Listener       **/
    public class TaskDetailMenuOpener extends ActorGestureListener {

  
        @Override        
        public void tap( InputEvent event, float x, float y, int count, int button ) {
    
            Actor a = event.getListenerActor();
        
            if( !(a instanceof TaskSprite)) {
                Gdx.app.log( "TaskGestureListener", "Event not from a TaskSprite: " + a );            
                return;
            }
       
            // Pass an instance of a local inner class
            // that will listen for button events
            TaskDetailsMenu detailsMenu = new TaskDetailsMenu( (TaskSprite)a, OverviewScreen.this.getAssetManager(), new EditTaskListener() );

            OverviewScreen.this.setOpenMenu(detailsMenu);  
            OverviewScreen.this.setFocusTask((TaskSprite)a);
      
            OverviewScreen.this.effectMgr.playClick();      
        }
    }

    
    
    
    /**
     * List for confirmation to delete 
     */
    class DeleteTaskListener extends ChangeListener {
  
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                       
            if( DELETE_CONFIRM.equalsIgnoreCase(actor.getName() )) {
                
                if( OverviewScreen.this.focusTask != null ) {
                    OverviewScreen.this.focusTask.setState(TaskState.DELETED);
                    OverviewScreen.this.focusTask.setDirty();
                }
            }
            
            OverviewScreen.this.clearOpenMenu();            
            OverviewScreen.this.effectMgr.playClick();        
        }        
    }
    
    
    /**
     * Listen for actions from task details menu
     */
    
    class EditTaskListener extends ChangeListener {
     
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {         
      
            OverviewScreen.this.effectMgr.playClick();
            String srcButtonName = actor.getName();
            
            if( EDIT.equalsIgnoreCase( srcButtonName )) {
            
                TaskForm form = new TaskForm(assetMgr, OverviewScreen.this.effectMgr);
                form.populate( OverviewScreen.this.focusTask );
                form.addListener( OverviewScreen.this.taskSavedListener );
                   
                OverviewScreen.this.setOpenMenu( form );
             
            } else if ( DELETE.equalsIgnoreCase( srcButtonName )) {
                
               Window confirm = new ConfirmWindow( "Confirm", OverviewScreen.this.assetMgr, OverviewScreen.this.focusTask, new DeleteTaskListener() );
               confirm.setX( (uiStage.getWidth() - confirm.getWidth()) / 2 );
               confirm.setY( (uiStage.getHeight() - confirm.getHeight()) / (float)1.5 );
               
               confirm.addAction(Actions.sequence( Actions.alpha(0), Actions.fadeIn( FADE_DURATION )));
               OverviewScreen.this.uiStage.addActor( confirm );  
                             
            } else {
                              
                TaskState newState = null;
                
                try {
                    newState = TaskState.valueOf( actor.getName() );
                } catch( Exception e ) {
                    newState = TaskState.IN_PROGRESS;
                }
                
                 // Mark task as needing saving. 
                if( OverviewScreen.this.focusTask != null ) {
                    OverviewScreen.this.focusTask.getTask().setState( newState );
                    OverviewScreen.this.focusTask.setDirty();
                }
                 
                OverviewScreen.this.clearOpenMenu(); 
            }            
        }
    } 
}

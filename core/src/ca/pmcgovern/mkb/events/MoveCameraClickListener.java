package ca.pmcgovern.mkb.events;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Expo;
import ca.pmcgovern.mkb.ui.CameraTweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MoveCameraClickListener extends ClickListener {

    private TweenManager tweenMgr;
    
    public MoveCameraClickListener( TweenManager tweenMgr ) {
        this.tweenMgr = tweenMgr;
    }
    
    @Override
    public void clicked( InputEvent event, float x, float y )  {    
        System.err.println( "TODO: disabled" );
/***
        this.tweenMgr.killAll();
        Actor a = event.getListenerActor();

        OrthographicCamera camera = (OrthographicCamera)a.getStage().getCamera();
   
        Vector3 touchPos = new Vector3( Gdx.input.getX(), Gdx.input.getY(), 0 );

        camera.unproject( touchPos );

        Tween.to( camera, CameraTweenAccessor.XY_AXIS, 1 )
        .target(touchPos.x, touchPos.y, 0 )
        .ease( Expo.INOUT)            
        .start( this.tweenMgr );
        **/
    }
}

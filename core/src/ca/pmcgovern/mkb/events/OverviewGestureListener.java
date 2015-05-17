
package ca.pmcgovern.mkb.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author mcgovern
 */
public class OverviewGestureListener implements GestureDetector.GestureListener {

    private OrthographicCamera taskCamera;
    private Rectangle extents;

    float velX, velY;
    boolean flinging = false;
    boolean enabled = true;

    public OverviewGestureListener(OrthographicCamera taskCamera, Rectangle extents) {

        this.taskCamera = taskCamera;
        this.extents = extents;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        flinging = false;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
      //      Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
        //    OverviewScreen.this.vignetteShader.begin();
        //    OverviewScreen.this.vignetteShader.setUniformf( "u_center", x, Gdx.graphics.getHeight() - y );
        //     OverviewScreen.this.vignetteShader.end();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        //        Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //       Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
        flinging = true;
     //       velX = taskCamera.zoom * velocityX * 0.5f;
        //        velY = taskCamera.zoom * velocityY * 0.5f;
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        if (!this.enabled) {
            return true;
        }

       //     Gdx.app.log("GestureDetectorTest", "pan :" + deltaX + ", " + deltaY + " " + taskCamera.position + " " + extents + " " +width + "x"+height + " z:"+ taskCamera.zoom );
        float effectiveViewportWidth = taskCamera.viewportWidth * taskCamera.zoom;
        float effectiveViewportHeight = taskCamera.viewportHeight * taskCamera.zoom;

        // Describe the *next* viewport and compare with extents.
        Rectangle vp = new Rectangle(0, 0, effectiveViewportWidth, effectiveViewportHeight);
        vp.setCenter(taskCamera.position.x - (deltaX * taskCamera.zoom), taskCamera.position.y + (deltaY * taskCamera.zoom));

        if (extents.contains(vp)) {

            this.taskCamera.position.add(-deltaX * taskCamera.zoom, deltaY * taskCamera.zoom, 0);

        } else {
            Gdx.app.log("GestureDetectorTest", "Extents exceeded.");
        }

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        //       Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean zoom(float originalDistance, float currentDistance) {
        float ratio = originalDistance / currentDistance;
            //    camera.zoom = initialScale * ratio;
        //   System.out.println(camera.zoom);
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
        return false;
    }

    public void update() {
        if (flinging) {
            velX *= 0.98f;
            velY *= 0.98f;
            this.taskCamera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
            if (Math.abs(velX) < 0.01f) {
                velX = 0;
            }
            if (Math.abs(velY) < 0.01f) {
                velY = 0;
            }
        }
    }

}

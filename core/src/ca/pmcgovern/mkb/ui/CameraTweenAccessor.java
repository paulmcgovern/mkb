package ca.pmcgovern.mkb.ui;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraTweenAccessor implements TweenAccessor<OrthographicCamera> {

    public static final int X_AXIS = 1;
    public static final int Y_AXIS = 2;
    public static final int XY_AXIS = 3;
    
    @Override
    public int getValues(OrthographicCamera target, int tweenType, float[] returnValues) {

        
        
        switch (tweenType) {
        case X_AXIS: returnValues[0] = target.position.x; return 1;
        case Y_AXIS: returnValues[0] = target.position.y; return 1;
        case XY_AXIS:
            returnValues[0] = target.position.x;
            returnValues[1] = target.position.y;
            return 2;
        default: assert false; return 0;
    }
    }

    @Override
    public void setValues(OrthographicCamera target, int tweenType, float[] newValues) {
 
       
        switch (tweenType) {
        case X_AXIS: target.position.set(newValues[0], target.position.y, target.position.z ); break;
        
        case Y_AXIS: target.position.set( target.position.x, newValues[1], target.position.z); break;
   
        case XY_AXIS:
            
            Vector3 newPos = new Vector3( newValues[0], newValues[1], 0 );
        target.position.set(newPos );
        target.update();
        break;
                
    default: assert false; break;
        }
        
        

        
    }

}

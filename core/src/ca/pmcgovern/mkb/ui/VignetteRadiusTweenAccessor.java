/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.ui;

import aurelienribon.tweenengine.TweenAccessor;
import static ca.pmcgovern.mkb.ui.CameraTweenAccessor.XY_AXIS;
import static ca.pmcgovern.mkb.ui.CameraTweenAccessor.X_AXIS;
import static ca.pmcgovern.mkb.ui.CameraTweenAccessor.Y_AXIS;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author mcgovern
 */
public class VignetteRadiusTweenAccessor implements TweenAccessor<Vector2> {

    @Override
    public int getValues(Vector2 target, int tweenType, float[] returnValues) {

        switch (tweenType) {
        case X_AXIS: returnValues[0] = target.x; return 1;
        case Y_AXIS: returnValues[0] = target.y; return 1;
        case XY_AXIS:
            returnValues[0] = target.x;
            returnValues[1] = target.y;
            return 2;
        default: assert false; 
        
        return 0;      
    }
    }

    @Override
    public void setValues(Vector2 target, int tweenType, float[] newValues) {
       switch (tweenType) {
        case X_AXIS: 
            target.x = newValues[0]; 
        break;
        
        case Y_AXIS: 
            target.y = newValues[1]; 
        break;
   
        case XY_AXIS:
            target.x = newValues[0];
            target.y = newValues[1];
        break;     
        
                     
        default: 
            assert false; 
        break;
        }  
    }
    
}


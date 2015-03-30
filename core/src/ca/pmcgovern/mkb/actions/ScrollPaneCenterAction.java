package ca.pmcgovern.mkb.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class ScrollPaneCenterAction extends Action {

	private ScrollPane sp;	
	private Interpolation terp;
	private float duration = 0.5f; // TODO: change duration with distance.

	private float time;	
	private float startScrollX;
	private float endScrollX;
	
	private float direction;
	
	public ScrollPaneCenterAction( ScrollPane sp, Actor actor ) {
		
		this.terp = Interpolation.linear;
		this.sp = sp;

		float visibleWidth = sp.getWidget().getParent().getWidth();
		float btnScreenPos = sp.getWidget().localToStageCoordinates( new Vector2( actor.getX(), actor.getY() ) ).x;
		float spScreenPos = sp.localToStageCoordinates( new Vector2( 0f,0f )).x;
        float spScreenMid = spScreenPos + (visibleWidth / 2 );
	
		if( btnScreenPos <= spScreenMid ) {
			this.direction = -1;
		} else {
		    this.direction = 1;
		}
		
		this.endScrollX =  Math.abs( btnScreenPos - spScreenMid );        
		this.startScrollX = sp.getScrollX();  		
	}
	
	
	
	@Override
	public boolean act( float delta ) {
	
		this.time += delta;
		
		float newXPos = this.terp.apply( 0, this.endScrollX, ( this.time / this.duration ));
		
		if( newXPos >= this.endScrollX ) {
			return true;
		}

		this.sp.setScrollX( (newXPos  * this.direction) + this.startScrollX );	
		return false;
	}
	 
}

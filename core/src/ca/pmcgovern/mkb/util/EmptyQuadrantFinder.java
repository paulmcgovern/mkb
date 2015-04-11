/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.pmcgovern.mkb.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mcgovern
 */
public class EmptyQuadrantFinder {
    
    float minSize;

    
    public EmptyQuadrantFinder(float minSize ) {
        if( minSize < 1 ) {
            throw new IllegalArgumentException( "Bad minimum size:" + minSize );
        }
        
        this.minSize = minSize;       
    }
    
    
    private float getDiagonalDist( Rectangle rect ) {
        Vector2 corner1 = new Vector2( rect.x, rect.y );
        Vector2 corner2 = new Vector2( rect.x + rect.width, rect.y + rect.height );
        
        return corner1.dst(corner2);
    }
    
    
    public Vector2 find( List<Vector2> points, Rectangle  extents ) {
        
       
        if( extents == null ) {
            throw new IllegalArgumentException( "Extents is null" );
        }
                
        if( getDiagonalDist(extents) < this.minSize ) {
            throw new IllegalArgumentException( "Extents too small for min. size." );
        }
        
        float quadWidth = extents.width / 2;
        float quadHeight = extents.height / 2;
        
        if( points == null || points.isEmpty() ) {
            return new Vector2( extents.x + quadWidth, extents.y + quadHeight);
        }
        
        
        
        Rectangle ne = new Rectangle( extents.x + quadWidth, extents.y + quadHeight, quadWidth, quadHeight );
        Rectangle se = new Rectangle( extents.x + quadWidth, extents.y, quadWidth, quadHeight );
        Rectangle sw = new Rectangle( extents.x, extents.y, quadWidth, quadHeight );
        Rectangle nw = new Rectangle( extents.x, extents.y + quadHeight, quadWidth, quadHeight );
        
        
        List<Vector2> nePoints = new ArrayList<Vector2>();
        List<Vector2> sePoints = new ArrayList<Vector2>();
        List<Vector2> swPoints = new ArrayList<Vector2>();
        List<Vector2> nwPoints = new ArrayList<Vector2>();
        
        int count = points.size();
        
        for( int i = 0; i < count; i++ ) {
            
            Vector2 pt = points.get( i );
            
            if( ne.contains(pt)) {
                nePoints.add(pt);
            } else if ( se.contains( pt )) {
                sePoints.add(pt);
            } else if ( sw.contains( pt )) {
                swPoints.add(pt);
            } else if ( nw.contains( pt )) {
                nwPoints.add(pt);
            } else {
                throw new IllegalArgumentException( "Point lies outside extents: " + pt );
            }           
        }
        
        int minLength = nePoints.size();
        List<Vector2> shortest = nePoints;
        
        Rectangle quadrant = ne;
        
        if( minLength > sePoints.size() ) {
            minLength = sePoints.size();
            shortest = sePoints;
            quadrant = se;
        } 
        
        if ( minLength > swPoints.size() ) {            
            minLength = swPoints.size();
            shortest = swPoints;
            quadrant = sw;
        } 
        
        if ( minLength > nwPoints.size() ) {
            minLength = nwPoints.size();
            shortest = nwPoints;
            quadrant = nw;
        }
        
        if( minLength == 0 ) {
            return new Vector2( quadrant.x + (quadrant.width/2), quadrant.y + (quadrant.height/2) );
        }
        
        return find(shortest, quadrant ); 
    }
    
   
}

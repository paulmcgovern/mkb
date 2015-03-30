package ca.pmcgovern.mkb.screens;

import ca.pmcgovern.mkb.sprites.TaskSprite;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class TaskLayout {

    
       
    public static final int MAX_ROUNDS = 10;
    
    /**
     * Finds a spot near (x,y) where a new task might fit on-screen. 
     * @param x
     * @param y
     * @return
     */
    public Vector2 findOpenSlot( float x, float y, Actor placementSubject, Array<Actor> allActors ) {
        
        float searchRadius = 100f;
        float twoPi = (float)(2 *Math.PI);
    
        
        
        int taskCount = allActors.size;
        int slots = 12;
        
        float actorHeight = placementSubject.getHeight();
        float actorWidth = placementSubject.getWidth();
        
        Rectangle[] taskPositions = new Rectangle[ taskCount ];
       
        
        for( int i = 0; i < taskCount; i++ ) {            
            TaskSprite t = (TaskSprite)allActors.get( i );
            taskPositions[ i ] = new Rectangle( t.getX(), t.getY(), t.getWidth(), t.getHeight() );    
        }
        
        Vector2 openCoord = null;
        
        for( int round = 0; round < MAX_ROUNDS; round++ ) {
        
            // Expand the search area 
            searchRadius += (round + 1) * searchRadius;
            slots += (round + 1) * slots;
            
            
            boolean[] filledPositions = new boolean[ slots ];
            Rectangle[] openPositions = new Rectangle[ slots ];  
            
            for( int i = 0; i < slots; i++ ) {
            
                float mx = (float)( searchRadius * Math.cos( i * ( twoPi / slots )));
                float my = (float)( searchRadius * Math.sin( i * ( twoPi / slots ))); 
            
                Rectangle localBounds = new Rectangle( mx, my, actorHeight, actorWidth );
                openPositions[ i ] = localBounds;
            
                for( int j = 0; j < taskCount; j++ ) {
                
                    Rectangle otherBounds = taskPositions[ j ];  
                 
                    if( localBounds.overlaps( otherBounds )) {              
                        filledPositions[ i ] = true; 
                    }                
                }           
            }           
        
            for( int i = 0; i < filledPositions.length; i++ ) {
            
                if( !filledPositions[ i ] ) {
                    Rectangle openPos =  openPositions[ i ];   
                    openCoord = new Vector2( openPos.x, openPos.y );
                    break;
                }
            } 
            
            if( openCoord != null ) {
                break;
            }
        }
        
        return openCoord;
    }
}

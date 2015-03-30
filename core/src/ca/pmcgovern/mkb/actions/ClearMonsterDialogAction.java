package ca.pmcgovern.mkb.actions;

import ca.pmcgovern.mkb.screens.MkbScreen;

import com.badlogic.gdx.scenes.scene2d.Action;

public class ClearMonsterDialogAction extends Action {

    private MkbScreen parentScreen;
    
    public ClearMonsterDialogAction( MkbScreen parentScreen ) {
    
        this.parentScreen = parentScreen;
    }
    
    
    @Override
    public boolean act( float delta ) {         	
    	
    	this.parentScreen.clearOpenMenu();
    	
        return true;
    }        
}
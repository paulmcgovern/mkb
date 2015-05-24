package ca.pmcgovern;

import java.util.Random;

import ca.pmcgovern.mkb.GameMain;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "game1";		
	//	cfg.width = 1280;
	//	cfg.height = 720;
	//	cfg.width = 480;
	//	cfg.height = 320;
	  //  cfg.width = 800;
	  //    cfg.height = 480;
		
		Random r = new Random( System.currentTimeMillis() );
	
		
	
	//	cfg.height = 400 + r.nextInt( 320 );
       // cfg.width = 800 + r.nextInt( 400 );		
	cfg.width=800;
        cfg.height=400;
       // System.err.println( "Default samples:"  + cfg.samples );
        cfg.samples = 5 ;
		new LwjglApplication(new GameMain(), cfg);
	}
}

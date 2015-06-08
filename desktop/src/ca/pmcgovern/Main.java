package ca.pmcgovern;

import java.util.Random;

import ca.pmcgovern.mkb.GameMain;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "game1";		
	
                Vector2[] arr = {
                  new Vector2( 320, 480 ),
                    new Vector2(480, 800),
                    new Vector2(480, 854),
                    new Vector2(540, 960),
                    new Vector2(1280, 720),
                   new Vector2(1920, 1080)
                };

                
		Random r = new Random( System.currentTimeMillis() );
                int i = r.nextInt( arr.length );
		//240x320
	
	//	cfg.height = 400 + r.nextInt( 320 );
       // cfg.width = 800 + r.nextInt( 400 );		
	cfg.width=(int)arr[i].y;
        cfg.height=(int)arr[i].x;
        System.err.println( "SIZE:" + arr[i]);
       // System.err.println( "Default samples:"  + cfg.samples );
        cfg.samples = 5 ;
		new LwjglApplication(new GameMain(), cfg);
	}
}

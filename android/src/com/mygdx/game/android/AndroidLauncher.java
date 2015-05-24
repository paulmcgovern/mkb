package com.mygdx.game.android;


import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.mygdx.game.MyGdxGame;
import ca.pmcgovern.mkb.GameMain;
import com.badlogic.gdx.Gdx;
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
  
		initialize(new GameMain(), config);
	}
}

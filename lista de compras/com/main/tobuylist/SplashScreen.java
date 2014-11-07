package com.main.tobuylist;


import com.main.listadecompras.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity implements Runnable {

	Animation animShowLogo;
	ImageView Logo;
	private final int TIME_DELAY = 3000;

	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        loadViews();
        loadAnimations();

        Handler handler = new Handler();
        handler.postDelayed(this, TIME_DELAY);

        Logo.startAnimation(animShowLogo);

    }

	@Override
    public void run() {
            startActivity(new Intent(this, MainActivity.class));
            finish();
    }

	/**
	 * Load all the views in the activity
	 */
	private void loadViews(){
		Logo = (ImageView)findViewById(R.id.logo);
	}

	/**
	 * Load all the animations in the activity
	 */
	public void loadAnimations(){
		animShowLogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.showlogo);
	}
}

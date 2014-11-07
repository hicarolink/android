package main.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity implements Runnable {

	Animation animShowlogo;
	ImageView logo;
	private final int TIME_DELAY = 3000;

	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        loadViews();
        loadAnimations();

        Handler handler = new Handler();
        handler.postDelayed(this, TIME_DELAY);

        logo.startAnimation(animShowlogo);

    }

	@Override
    public void run() {
            startActivity(new Intent(this, OpeningGame.class));
            finish();
    }

	/**
	 * Load all the views in the activity
	 */
	private void loadViews(){
		logo = (ImageView)findViewById(R.id.logo);
	}

	/**
	 * Load all the animations in the activity
	 */
	public void loadAnimations(){
		animShowlogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.showlogo);
	}
}

package com.example.thomas.familyheroes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Thomas on 14/11/2014.
 */
public class LoaderScreen extends Activity {
    private CountDownTimer lTimer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loader_screen); // Contains only an LinearLayout with backround as image drawable

        ImageView myImageView= (ImageView)findViewById(R.id.imageView);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView

        lTimer = new CountDownTimer(4000, 1000) {
            public void onFinish() {
                closeScreen();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }
        }.start();
    }

    private void closeScreen() {
        Intent lIntent = new Intent();
        lIntent.setClass(this, LoginActivity.class);
        startActivity(lIntent);
        finish();
    }
}
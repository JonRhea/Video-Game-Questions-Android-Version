package com.jrhea.videogamequestions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

/**
 * @author Jonathan Rhea
 * @since 2-1-21
 *
 * MainActivity - The start of the application.
 * Not much is here besides the header for the app and the start button.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }//end onCreate

    /**
     * This method simple starts the game by loading QuestionsActivity
     * @param view The button that is being pressed, which is start in this case.
     */
    public void start(View view) {

        startActivity(new Intent(this, QuestionsActivity.class));
    }//end start
}//end class

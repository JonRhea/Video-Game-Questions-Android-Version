package com.jrhea.videogamequestions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.security.SecureRandom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author Jonathan Rhea
 * @since 2-1-21
 *
 * QuestionsActivity - This is where the main game is played.
 * The big picture is that the app picks a random question, shuffles the responses, and displays the answers in a random order.
 * See below for more in depth descriptions.
 * This is also all one activity. One activity for all questions.
 */
public class QuestionsActivity extends AppCompatActivity {

    //Here are most of the variables used in this class
    private String questionLine;
    private String questionSelected;
    private String[] lineArray = new String[50];
    private String[] tempLineArray = new String[50];
    private String[] tokens;
    private String[] answers = new String[4];
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;
    private String result;

    private TextView questionText;
    private TextView resultText;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button returnToStartButton;

    MediaPlayer player;

    int randomNumber = 0;
    private int looper = 0;
    private int correctCounter = 0;
    private int totalQuestions = 49;
    SecureRandom random = new SecureRandom();

    private BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        ConstraintLayout constraintLayout = findViewById(R.id.questions_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //assign the resources to their corresponding variables
        questionText = (TextView)findViewById(R.id.question_text);
        resultText = (TextView)findViewById(R.id.result_text);
        answer1Button = (Button)findViewById(R.id.answer_button_1);
        answer2Button = (Button)findViewById(R.id.answer_button_2);
        answer3Button = (Button)findViewById(R.id.answer_button_3);
        answer4Button = (Button)findViewById(R.id.answer_button_4);
        returnToStartButton = (Button)findViewById(R.id.return_to_start_button);

        returnToStartButton.setVisibility(View.GONE);


        //execute the game and display the questions and answers
        readQuestion();
        getQuestion();
        setTexts();

        playMusic();
    }//end onCreate


    /**
     * readQuestions - The app reads a random line from questions.txt
     */
    public void readQuestion() {

        try {
            InputStream file = getAssets().open("questions.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            //questionLine = reader.readLine();

            looper = 0;
            //while loop to put all the lines from the txt file into an array.
            //if totalQuestions = 0, then all questions are answered
            while (looper < totalQuestions) {

                questionLine = reader.readLine();
                lineArray[looper] = questionLine;
                looper++;
            }//end while

        }//end try
        //required catch
        catch (IOException ioException) {
            ioException.printStackTrace();
        }//end catch
    }//end readQuestion()

    /**
     * getQuestion() - The app picks a random element from lineArray[], parses it, and puts the
     * pieces in their correct places. More information in the method.
     */
    public void getQuestion(){
        //random number to pick the question
        if(totalQuestions - 1 != 0) {
            random = new SecureRandom();
            randomNumber = random.nextInt(totalQuestions - 1);
        }//end id
        else{
            randomNumber = 0;
        }//end else

        //sub 1 so random numbers outside of index range won't happen when lineArray[] is trimmed
        totalQuestions--;
        questionSelected = lineArray[randomNumber];
        trimArray();


        //split the question and answers by commas
        tokens = questionSelected.split(",");

        //index 0 is always the question
        question = tokens[0];
        //the first answer (index 1) is always the correct answer (the second string parsed (is parsed a word?))
        correctAnswer = tokens[1];

        //store the 4 answers into the answers array
        for(int i = 0; i < 4; i++) {
            answers[i] = tokens[i + 1];
        }//end for

        //convert the array to a list, shuffle it, then put it back to an array
        List<String> stringList = Arrays.asList(answers);
        Collections.shuffle(stringList);
        stringList.toArray(answers);

        //assign the shuffled answers to their corresponding button texts
        answer1 = answers[0];
        answer2 = answers[1];
        answer3 = answers[2];
        answer4 = answers[3];

    }//end getQuestion

    /**
     * setTexts - This sets the strings of the question and answers to their correct places.
     * Result is also set to nothing so that previous results do not carry over.
     */
    public void setTexts() {
        questionText.setText(question);
        resultText.setText(" ");
        answer1Button.setText(answer1);
        answer2Button.setText(answer2);
        answer3Button.setText(answer3);
        answer4Button.setText(answer4);

    }//end setTexts

    //**The following 4 methods are the exact same. They just correspond to a different button.**

    /**
     * answer1Pressed - onClick for when the top answer button is pressed
     * @param view The button being pressed
     */
    public void answer1Pressed(View view) {
        //disable the buttons after an answer has been picked
        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
        answer4Button.setEnabled(false);

        if(answer1Button.getText().toString().equals(correctAnswer)){

            result = "Correct!";
            resultText.setTextColor(Color.GREEN);
            resultText.setText(result);
            correctCounter++;

            //three second timer after an answer is selected.
            //if correct, load the next question
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    if(correctCounter != 50) {
                        answer1Button.setEnabled(true);
                        answer2Button.setEnabled(true);
                        answer3Button.setEnabled(true);
                        answer4Button.setEnabled(true);

                        //select a new question
                        getQuestion();
                        setTexts();
                    }//end if
                    else{
                        resultText.setText("You got all the questions right! Nice job!");
                    }//end else
                }
            }.start();
        }//end if
        else{
            result = "Incorrect!";
            resultText.setTextColor(Color.RED);
            resultText.setText(result);

            //three second timer after an answer is selected.
            //if incorrect, display the score
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    resultText.setTextColor(Color.GREEN);
                    resultText.setText("You got " + correctCounter + " correct!");
                    returnToStartButton.setVisibility(View.VISIBLE);
                }
            }.start();
        }//end else

    }//end answer1Pressed

    public void answer2Pressed(View view) {
        //disable the buttons after an answer has been picked
        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
        answer4Button.setEnabled(false);

        if(answer2Button.getText().toString().equals(correctAnswer)){

            result = "Correct!";
            resultText.setTextColor(Color.GREEN);
            resultText.setText(result);
            correctCounter++;

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    if(correctCounter != 50) {
                        answer1Button.setEnabled(true);
                        answer2Button.setEnabled(true);
                        answer3Button.setEnabled(true);
                        answer4Button.setEnabled(true);
                        getQuestion();
                        setTexts();
                    }//end if
                    else{
                        resultText.setText("You got all the questions right! Nice job!");
                    }//end else
                }
            }.start();

        }//end if
        else{
            result = "Incorrect!";
            resultText.setTextColor(Color.RED);
            resultText.setText(result);

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    resultText.setTextColor(Color.GREEN);
                    resultText.setText("You got " + correctCounter + " correct!");
                    returnToStartButton.setVisibility(View.VISIBLE);
                }
            }.start();

        }//end else

    }//end answer2Pressed

    public void answer3Pressed(View view) {
        //disable the buttons after an answer has been picked
        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
        answer4Button.setEnabled(false);

        if(answer3Button.getText().toString().equals(correctAnswer)){

            result = "Correct!";
            resultText.setTextColor(Color.GREEN);
            resultText.setText(result);
            correctCounter++;

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    if(correctCounter != 50) {
                        answer1Button.setEnabled(true);
                        answer2Button.setEnabled(true);
                        answer3Button.setEnabled(true);
                        answer4Button.setEnabled(true);
                        getQuestion();
                        setTexts();
                    }//end if
                    else{
                        resultText.setText("You got all the questions right! Nice job!");
                    }//end else
                }
            }.start();

        }//end if
        else{
            result = "Incorrect!";
            resultText.setTextColor(Color.RED);
            resultText.setText(result);

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    resultText.setTextColor(Color.GREEN);
                    resultText.setText("You got " + correctCounter + " correct!");
                    returnToStartButton.setVisibility(View.VISIBLE);
                }
            }.start();

        }//end else

    }//end answer3Pressed

    public void answer4Pressed(View view) {
        //disable the buttons after an answer has been picked
        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
        answer4Button.setEnabled(false);

        if(answer4Button.getText().toString().equals(correctAnswer)){

            result = "Correct!";
            resultText.setTextColor(Color.GREEN);
            resultText.setText(result);
            correctCounter++;

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    if(correctCounter != 50) {
                        answer1Button.setEnabled(true);
                        answer2Button.setEnabled(true);
                        answer3Button.setEnabled(true);
                        answer4Button.setEnabled(true);
                        getQuestion();
                        setTexts();
                    }//end if
                    else{
                        resultText.setText("You got all the questions right! Nice job!");
                    }//end else
                }
            }.start();

        }//end if
        else{
            result = "Incorrect!";
            resultText.setTextColor(Color.RED);
            resultText.setText(result);

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do nothing
                }

                @Override
                public void onFinish() {
                    resultText.setTextColor(Color.GREEN);
                    resultText.setText("You got " + correctCounter + " correct!");
                    returnToStartButton.setVisibility(View.VISIBLE);
                }
            }.start();

        }//end else

    }//end answer4Pressed

    public void returnToStart(View view) {

        stopPlayer();
        startActivity(new Intent(this, MainActivity.class));
    }//end returnToStart

    /**
     * playMusic plays Allegro from Phoenix Wright: Ace Attorney (2001)
     * The music will continue to loop until stopPlayer is called, which is called when the return
     * to start button is pressed OR the app is stopped, paused, or destroyed.
     */
    public void playMusic() {

        if (player == null) {
            player = MediaPlayer.create(this, R.raw.allegro);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.start();
                }//end onCompletion
            });
        }//end if
        player.start();
    }//end play

    /**
     * stopPlayer simply stops the music when return to start is hit or
     * app is stopped, paused, or destroyed.
     */
    public void stopPlayer() {

        if (player != null) {
            player.release();
            player = null;
        }//end if
    }//end stopPlayer

    /**
     * trimArray - trims lineArray[] so that the app can do two things.
     * 1) Remove the current question from lineArray[] so it can't be used again
     * 2) Trim the array size by 1 so there is no null indexes
     */
    public void trimArray(){

        int i = 0;
        while(i < randomNumber) {

            tempLineArray[i] = lineArray[i];
            i++;
        }//end while
        //skip the current question

        while(i <= totalQuestions){
            tempLineArray[i] = lineArray[i + 1];
            i++;
        }//end while

        lineArray = tempLineArray;
    }//end trimArray

    //Lifecycle Events to stop music

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseApp(){

        stopPlayer();
    }//end pauseApp


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroyApp(){

        stopPlayer();
    }//end destroyApp

    //resume music when app is resumed
    @Override
    protected void onResume(){
        super.onResume();
        if(player == null){
            playMusic();
        }//end if
    }//end onResume

    //destroys app when back is pressed (change in the future to prevent accidental hits?)
    @Override
    public void onBackPressed(){

        destroyApp();
        super.onBackPressed();
    }//end backPressed

    @Override
    public void onUserLeaveHint(){

        pauseApp();
        super.onUserLeaveHint();
    }//end onUserLeaveHint
}//end class
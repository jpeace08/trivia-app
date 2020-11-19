package com.jpeace.triviaapp.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jpeace.triviaapp.R;
import com.jpeace.triviaapp.controller.AppController;
import com.jpeace.triviaapp.model.Question;
import com.jpeace.triviaapp.util.Util;

import java.util.ArrayList;
import java.util.Random;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ToggleButton[] toggleButtons;
    private TextView m_current_score, m_countdown, m_current_question_index, m_question_content;

    private ArrayList<Question> mQuestions;
    private Random mRandom;
    private Handler mHandler;

    private int mCurrentQuestionIndex;
    private int mCurrentScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        initAndAddEvents();
        getDataFromIntent();
        getSavedState();
        updateUI(mCurrentQuestionIndex, mCurrentScore);
    }

    private void getSavedState() {
        mCurrentQuestionIndex= AppController.getInstance().get(AppController.CURRENT_INDEX, Integer.class, 0);
        mCurrentScore=AppController.getInstance().get(AppController.CURRENT_SCORE, Integer.class, 0);
        if (mCurrentQuestionIndex>49) {
            mCurrentQuestionIndex=0;
            mCurrentScore=0;
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateUI(int index, int score) {
        resetStateToggleButton();
        m_current_score.setText("Your score: "+ score);
        Question mCurrentQuestion = mQuestions.get(index);

        m_current_question_index.setText(String.format("Question %d", (index+1)));
        m_question_content.setText(mCurrentQuestion.getQuestion());

        int i = mRandom.nextInt(4);
        setResToggleButton(i, mCurrentQuestion.getCorrectAnswer(), true);
        int in=0;
        for (int j=0; j<toggleButtons.length; j++) {
            if (j != i) {
                setResToggleButton(j, mCurrentQuestion.getIncorrectAnswer()[in], false);
                in++;
            }
        }
    }

    private void setResToggleButton(int index, String content, boolean tag){
        toggleButtons[index].setText(decode(content));
        toggleButtons[index].setTextOn(decode(content));
        toggleButtons[index].setTextOff(decode(content));
        toggleButtons[index].setTag(tag);
    }

    private String decode(String content){
        return content.replace("&amp;", "&");
    }

    private void getDataFromIntent() {
        mQuestions= (ArrayList<Question>) getIntent().getBundleExtra(Util.MY_KEY).getSerializable(Util.MY_KEY);
    }

    private void initAndAddEvents() {
        //init
        Button m_check_ans = findViewById(R.id.m_check_ans);
        m_countdown=findViewById(R.id.m_countdown);
        m_current_question_index=findViewById(R.id.m_current_question_index);
        m_current_score=findViewById(R.id.m_current_score);
        m_question_content=findViewById(R.id.m_question_content);

        toggleButtons=new ToggleButton[Util.NUMBER_ANS];
        mRandom=new Random();

        //add events:
        for (int i=0; i<toggleButtons.length; i++){
            toggleButtons[i]=findViewById(Util.ANS_IDs[i]);
            toggleButtons[i].setOnClickListener(this);
            toggleButtons[i].setOnCheckedChangeListener(this);
        }
        m_check_ans.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.m_check_ans){
            if (checkStateCheckedButton()){
                checkQuestion();
                mCurrentQuestionIndex=++mCurrentQuestionIndex;

                if (mCurrentQuestionIndex>Util.AMOUNT-1) {
                    //TODO: end quiz

                }
                else {
                    mHandler=new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message message) {
                            updateUI(mCurrentQuestionIndex, mCurrentScore);
                            return false;
                        }
                    });
                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                mHandler.sendEmptyMessage(0);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            super.run();
                        }
                    };
                    mThread.start();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Choose your answer!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        for (View v : toggleButtons) {
            if (v.getId() != view.getId()) {
                ((ToggleButton) v).setChecked(false);
            }
        }
    }

    private boolean checkStateCheckedButton() {
        for (ToggleButton tg:toggleButtons) {
            if (tg.isChecked()) return true;
        }
        return false;
    }

    private void checkQuestion() {
        for (ToggleButton tg:toggleButtons) {
            if (tg.isChecked()){
                if (Boolean.parseBoolean(tg.getTag().toString())) {
                    mCurrentScore+=10;
                    tg.setBackgroundResource(R.drawable.dr_bg_true);
                }
                else {
                    tg.setBackgroundResource(R.drawable.dr_bg_false);
                    if (mCurrentScore <= 5) {
                        mCurrentScore = 0;
                    } else {
                        mCurrentScore -= 5;
                    }
                }
            }
            else if (Boolean.parseBoolean(tg.getTag().toString())){
                tg.setBackgroundResource(R.drawable.dr_bg_true);
            }
        }
    }

    private void resetStateToggleButton(){
        for (ToggleButton toggleButton : toggleButtons) {
            toggleButton.setChecked(false);
            toggleButton.setBackgroundResource(R.drawable.dr_bg_toggle);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton instanceof ToggleButton){
            if (b) compoundButton.setTextColor(Color.WHITE);
            else compoundButton.setTextColor(R.color.grey_white);
        }
    }

    @Override
    protected void onPause() {

        AppController.getInstance().put(AppController.CURRENT_SCORE, mCurrentScore);
        if (mCurrentQuestionIndex>=50) mCurrentQuestionIndex=0;
        AppController.getInstance().put(AppController.CURRENT_INDEX, mCurrentQuestionIndex+1);

        AppController.getInstance().clearRequest(AppController.REQ_TAG);
        super.onPause();
    }
}

package com.jpeace.triviaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jpeace.triviaapp.data.QuestionBank;
import com.jpeace.triviaapp.model.OnGetDataListener;
import com.jpeace.triviaapp.model.Question;
import com.jpeace.triviaapp.ui.TriviaActivity;
import com.jpeace.triviaapp.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout m_mask;
    private ImageButton m_play;
    private ProgressBar m_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        addEvents();
        hideSystemUI();
    }

    private void addEvents() {
        m_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingAndSwitchLayout();
            }
        });
    }

    private void showLoadingAndSwitchLayout() {
        m_mask.setVisibility(View.VISIBLE);
        m_loading.setVisibility(View.VISIBLE);

        getData();

    }

    private void hideSystemUI(){
        View decoView=getWindow().getDecorView();
        decoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void getData() {
        QuestionBank.getQuestions(Util.DIF_EASY, Util.GEOGRAPHY, Util.AMOUNT, Util.TYPE_MULTIPLE, new OnGetDataListener() {
            @Override
            public void onDone(ArrayList<Question> questions) {

                Intent intent=new Intent(getApplicationContext(), TriviaActivity.class);
                Bundle data=new Bundle();
                data.putSerializable(Util.MY_KEY, questions);
                intent.putExtra(Util.MY_KEY, data);
                startActivity(intent);
                MainActivity.this.finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Vui long thu lai!", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
    }

    private void init() {
        m_mask=findViewById(R.id.m_mask);
        m_loading=findViewById(R.id.m_loading);
        m_play=findViewById(R.id.m_play);
    }
}
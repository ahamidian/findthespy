package com.findthespy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

public class TimerActivity extends FullscreenActivity{
    private int time;
    private long timeCountInMilliSeconds = 1 * 60000;
    private int spyId;
    private String answer;
    private String categoryName;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;
    private Button finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);
        super.mContentView = findViewById(R.id.fullscreen_content);

        // method call to initialize the views
        initViews();
        Intent intent = getIntent();
        time = intent.getExtras().getInt("time");
        spyId = intent.getExtras().getInt("spyName");
        answer = intent.getExtras().getString("answer");
        categoryName = intent.getExtras().getString("categoryName");

        timeCountInMilliSeconds = time * 1000 * 60;

        ProgressBar CDT = findViewById(R.id.progressBarCircle);
        CDT.setMax(time * 60);

        finishButton = findViewById(R.id.manualFinishButton);
        finishButton.setOnClickListener(FinishClickListener());

        startCountDownTimer();

    }

    private View.OnClickListener FinishClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerActivity.this, ShowSpyActivity.class);
                Bundle b = new Bundle();
                b.putInt("spyName", spyId);
                b.putString("answer", answer);
                b.putString("categoryName", categoryName);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        };
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
//        textViewTime.setText(time);
    }


    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

            }

        }.start();
        countDownTimer.start();
    }


    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }
}

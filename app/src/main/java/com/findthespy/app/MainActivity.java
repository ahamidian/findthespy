package com.findthespy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.ramotion.fluidslider.FluidSlider;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends FullscreenActivity {

    private int numberOfPeople;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        super.mContentView = findViewById(R.id.fullscreen_content);

        initializePeopleSlider();
        initializeTimeSlider();
        initializeExitButton();
        initializeInstructionsButton();
        initializeStartButton();
        initializeLocationsButton();

        setTime(3);
        setNumberOfPeople(5);

    }

    private void initializeTimeSlider() {
        final CircularSliderRange timeSlider = findViewById(R.id.time_slider);
        final int max = 15;
        final int min = 0;
        final int total = max - min;

        final double oneMin = 360 / (double)total;

        timeSlider.setStartAngle(270);
        timeSlider.setEndAngle(270 + 3 * 360 / (double)total);

//        peopleSlider.setPosition(0.3f);
//        peopleSlider.setTextSize(40);
//        peopleSlider.setStartText(String.valueOf(min));
//        peopleSlider.setEndText(String.valueOf(max));

        timeSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            String TAG = "TAG";
            @Override
            public void onStartSliderMoved(double pos) {
//                Log.d(TAG, "onStartSliderMoved:" + pos);
            }

            @Override
            public void onEndSliderMoved(double pos) {
                Log.d(TAG, "onEndSliderMoved:" + pos);

                double position = pos;

                if (position >= 270){
                    if (position > 270 + oneMin / 2) {
                        if (position < 270 + oneMin) {
                            timeSlider.setEndAngle(270 + oneMin);
                            position = oneMin;
                            setTime(getRound(position));
                            return;
                        }
                    }
                    else {
                        timeSlider.setEndAngle(269);
                        position = 90 + 269;
                        setTime(getRound(position));
                        return;
                    }
                }

                if (position > 270){
                    position = position - 270;
                }
                else {
                    position = 90 + position;
                }

                setTime(getRound(position));
            }

            private int getRound(double position) {
                return (int)Math.round(min + (total  * position / 360));
            }

            @Override
            public void onStartSliderEvent(ThumbEvent event) {
//                Log.d(TAG, "onStartSliderEvent:" + event);
            }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                Log.d(TAG, "onEndSliderEvent:" + event);
            }
        });
    }

    private void setTime(final int newTime) {
        this.time = newTime;
        runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                TextView timeText = findViewById(R.id.time_text);
                timeText.setText(String.format("%d MIN", newTime));
            }
        });
    }

    private void initializeLocationsButton() {
        Button locationsButton = findViewById(R.id.locations_button);
        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoriesListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeStartButton() {
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartGameActivity.class);
                Bundle b = new Bundle();
                b.putInt("numberOfPeople", numberOfPeople);
                b.putInt("time", time);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void initializeInstructionsButton() {
        Button instructionsButton = findViewById(R.id.instructions_button);
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void initializePeopleSlider() {
        final FluidSlider peopleSlider = findViewById(R.id.people_slider);

        int max = 10;
        final int min = 3;
        final int total = max - min;

        peopleSlider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float pos) {
                int numOfPeople = Math.round(min + (total  * pos));
                setNumberOfPeople(numOfPeople);
                peopleSlider.setBubbleText(String.valueOf(numberOfPeople));

                return Unit.INSTANCE;
            }
        });
        peopleSlider.setPosition(0.3f);
        peopleSlider.setTextSize(40);
        peopleSlider.setStartText(String.valueOf(min));
        peopleSlider.setEndText(String.valueOf(max));


//        peopleSlider.setBeginTrackingListener(new Function0<Unit>() {
//            @Override
//            public Unit invoke() {
//                Log.d("D", "setBeginTrackingListener");
//                return Unit.INSTANCE;
//            }
//        });
//
//        peopleSlider.setEndTrackingListener(new Function0<Unit>() {
//            @Override
//            public Unit invoke() {
//                Log.d("D", "setEndTrackingListener");
//                return Unit.INSTANCE;
//            }
//        });
    }

    private void setNumberOfPeople(final int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
        runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                TextView peopleText = findViewById(R.id.people_text);
                peopleText.setText(String.format("%d People", numberOfPeople));
            }
        });

    }

    private void initializeExitButton() {
        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.show(getSupportFragmentManager(), "dialog");
                hide();
            }
        });
    }


}

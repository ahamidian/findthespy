package com.findthespy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.ramotion.fluidslider.FluidSlider;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private int numberOfPeople;
    private int time;

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        initializePeopleSlider();
        initializeTimeSlider();
        initializeExitButton();
        initializeInstructionsButton();
        initializeStartButton();
        initializeLocationsButton();

        setTime(3);
        setNumberOfPeople(5);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
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
                Intent intent = new Intent(FullscreenActivity.this, StartGameActivity.class);
                Bundle b = new Bundle();
                b.putInt("numberOfPeople", numberOfPeople);
                b.putInt("time", time);
                intent.putExtras(b);
                startActivity(intent);
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();

        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

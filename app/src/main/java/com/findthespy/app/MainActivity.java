package com.findthespy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.ramotion.fluidslider.FluidSlider;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends FullscreenActivity {

    private int numberOfPeople;
    private int time;
    private String chosenCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        super.mContentView = findViewById(R.id.fullscreen_content);

        initilizeWholeScreen();

        createDefaultCategory();
        initializePeopleSlider();
        initializeTimeSlider();
        initializeExitButton();
        initializeInstructionsButton();
        initializeStartButton();
        initializeLocationsButton();
        initializeCategoryChoose();

        setTime(3);
        setNumberOfPeople(5);

    }

    private void initilizeWholeScreen() {
        FrameLayout whole = findViewById(R.id.whole_screen);
        whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    private void initializeCategoryChoose() {
        TinyDB db = new TinyDB(getApplicationContext());
        final ArrayList<String> categories = db.getListString("categories");
        final ArrayList<String> categoriesToRemove = new ArrayList<>();
        for (String category : categories) {
            if (db.getListString("category#" + category) == null || db.getListString("category#" + category).isEmpty()) {
                categoriesToRemove.add(category);
            }
        }
        categories.removeAll(categoriesToRemove);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_dropdown_item, categories);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hide();
                chosenCategory = categories.get(position);
                hide();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hide();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeCategoryChoose();
    }

    private void createDefaultCategory() {
        TinyDB db = new TinyDB(getApplicationContext());
        ArrayList<String> categories = db.getListString("categories");
        if (categories == null || categories.isEmpty()) {
            categories = new ArrayList<>();
            categories.add("Locations");
            ArrayList<String> items = new ArrayList<>();
            items.add("Airplane");
            items.add("Bank");
            items.add("Beach");
            items.add("Hospital");
            items.add("Hotel");
            items.add("Movie Studio");
            items.add("School");
            items.add("Supermarket");
            items.add("Theater");
            items.add("University");
            db.putListString("categories", categories);
            db.putListString("category#Locations", items);
        }
        chosenCategory = categories.get(0);
    }

    private void initializeTimeSlider() {
        final CircularSliderRange timeSlider = findViewById(R.id.time_slider);
        final int max = 15;
        final int min = 0;
        final int total = max - min;

        final double oneMin = 360 / (double) total;

        timeSlider.setStartAngle(270);
        timeSlider.setEndAngle(270 + 3 * 360 / (double) total);

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

                if (position >= 270) {
                    if (position > 270 + oneMin / 2) {
                        if (position < 270 + oneMin) {
                            timeSlider.setEndAngle(270 + oneMin);
                            position = oneMin;
                            setTime(getRound(position));
                            return;
                        }
                    } else {
                        timeSlider.setEndAngle(269);
                        position = 90 + 269;
                        setTime(getRound(position));
                        return;
                    }
                }

                if (position > 270) {
                    position = position - 270;
                } else {
                    position = 90 + position;
                }

                setTime(getRound(position));
            }

            private int getRound(double position) {
                return (int) Math.round(min + (total * position / 360));
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
                b.putString("categoryName", "category#" + chosenCategory);
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
                int numOfPeople = Math.round(min + (total * pos));
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

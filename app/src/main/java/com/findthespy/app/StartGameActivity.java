package com.findthespy.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class StartGameActivity extends FullscreenActivity {

    TextView roleTextView;
    TextView roleIndexTextView;
    Button seeRoleButton;

    private int round = 1;
    private String answer = "جواب";
    private int roleNum;
    private int spyIndex;
    private int answerIndex;
    private int playerIndex = 1;
    private int time;
    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int startGameActivityIndex = R.layout.start_game_activity;
        setContentView(startGameActivityIndex);
        super.mContentView = findViewById(R.id.fullscreen_content);

        Random randomGen = new Random();


        Intent intent = getIntent();
        categoryName = intent.getExtras().getString("categoryName");
//        final String categoryName = "category#" + "Locations";
        final TinyDB db = new TinyDB(getApplicationContext());
        ArrayList<String> items = db.getListString(categoryName);
        answerIndex = randomGen.nextInt(items.size());
        answer = items.get(answerIndex);

        roleNum = intent.getExtras().getInt("numberOfPeople");
        time = intent.getExtras().getInt("time");
        spyIndex = randomGen.nextInt(roleNum) + 1;

        this.roleTextView = (TextView) findViewById(R.id.role_txt);
        this.roleTextView.setText("سلام");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.roleTextView.setPadding(35, 35, 35, 35);

        seeRoleButton = findViewById(R.id.show_role);
        seeRoleButton.setText(getString(R.string.view_role));
        seeRoleButton.setOnClickListener(getRoleButtonOnClickListener);

        this.roleIndexTextView = findViewById(R.id.role_index_txt);
        this.roleIndexTextView.setText(getString(R.string.player) + "1");

    }

    private View.OnClickListener gotItButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playerIndex >= roleNum) {
                Intent intent = new Intent(StartGameActivity.this, TimerActivity.class);
                Bundle b = new Bundle();
                b.putInt("time", time);
                b.putInt("spyName", spyIndex);
                b.putString("answer", answer);
                b.putString("categoryName", categoryName);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            } else {
                playerIndex += 1;
                seeRoleButton.setText(getString(R.string.view_role));
                roleIndexTextView.setText(getString(R.string.player) + (playerIndex));
                seeRoleButton.setOnClickListener(getRoleButtonOnClickListener);
                roleTextView.setText(" ");
            }
        }

    };

    private View.OnClickListener getRoleButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playerIndex != spyIndex)
                roleTextView.setText(getString(R.string.answer) + answer);
            else
                roleTextView.setText(getString(R.string.spySimple));
            if (playerIndex < roleNum) {
                seeRoleButton.setText(getString(R.string.got_it));
            } else {
                seeRoleButton.setText(getString(R.string.start));
            }
            seeRoleButton.setOnClickListener(gotItButtonOnClickListener);
        }
    };

}

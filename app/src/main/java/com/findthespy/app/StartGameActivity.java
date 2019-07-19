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

import java.util.Random;

public class StartGameActivity extends Activity {

    TextView roleTextView;
    TextView roleIndexTextView;
    Button seeRoleButton;

    private int round = 1;
    private String answer = "جواب";
    private int roleNum;
    private int spyIndex;
    private int playerIndex = 1;
    private int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int startGameActivityIndex = R.layout.start_game_activity;
        setContentView(startGameActivityIndex);


        Intent intent = getIntent();
        roleNum = intent.getExtras().getInt("numberOfPeople");
        time = intent.getExtras().getInt("time");
        Random randomGen = new Random();
        spyIndex = randomGen.nextInt(roleNum);

        this.roleTextView = (TextView) findViewById(R.id.role_txt);
        this.roleTextView.setText("سلام");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.roleTextView.setTextColor(Color.WHITE);
        this.roleTextView.setPadding(35, 35, 35, 35);
        this.roleTextView.setTextSize(18);

        seeRoleButton = findViewById(R.id.show_role);
        seeRoleButton.setText("دیدن نقش");
        seeRoleButton.setOnClickListener(getRoleButtonOnClickListener);

        this.roleIndexTextView = findViewById(R.id.role_index_txt);
        this.roleIndexTextView.setText("بازیکن 1");

    }

    private View.OnClickListener gotItButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playerIndex += 1;
            if (playerIndex >= roleNum) {
                Intent intent = new Intent(StartGameActivity.this, TimerActivity.class);
                Bundle b = new Bundle();
                b.putInt("time", time);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
            seeRoleButton.setText("دیدن نقش");
            roleIndexTextView.setText("بازیکن " + (playerIndex));
            seeRoleButton.setOnClickListener(getRoleButtonOnClickListener);
            roleTextView.setText(" ");
        }

    };

    private View.OnClickListener getRoleButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playerIndex != spyIndex)
                roleTextView.setText("جواب: " + answer);
            else
                roleTextView.setText("آشغال Spy");
            if (playerIndex < roleNum) {
                seeRoleButton.setText("فهمیدم");
                seeRoleButton.setOnClickListener(gotItButtonOnClickListener);
            } else {
                seeRoleButton.setText("شروع بازی");
            }
        }
    };

}

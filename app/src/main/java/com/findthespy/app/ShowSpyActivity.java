package com.findthespy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class ShowSpyActivity extends FullscreenActivity {

    private int spyId;
    private Button showAnswerButton;
    private String answer;
    private String categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_spy_layout);
        super.mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();
        spyId = intent.getExtras().getInt("spyName");
        answer = intent.getExtras().getString("answer");
        categoryName = intent.getExtras().getString("categoryName");

        TextView spyIdText = findViewById(R.id.spy_name);
        spyIdText.setText(getString(R.string.player) + spyId);

        showAnswerButton = findViewById(R.id.view_answer);
        showAnswerButton.setOnClickListener(getViewAnswerClickListener());

        Button showItemsButton = findViewById(R.id.view_item_list);
        showItemsButton.setOnClickListener(getViewItemsListClickListener());
    }

    private View.OnClickListener getViewItemsListClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowSpyActivity.this, ShowingItemsActivity.class);
                Bundle b = new Bundle();
                b.putString("categoryName", categoryName);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        };
    }

    private View.OnClickListener getViewAnswerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerTV = findViewById(R.id.answer);
                answerTV.setText(answer);
                answerTV.setVisibility(View.VISIBLE);

                TextView answerTxtTV = findViewById(R.id.answerTxt);
                answerTxtTV.setVisibility(View.VISIBLE);

                Button backToMainMenu = findViewById(R.id.backToMainMenu);
                backToMainMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShowSpyActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                backToMainMenu.setVisibility(View.VISIBLE);
            }
        };
    }
}

package com.findthespy.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartGameActivity extends Activity {

    TextView roleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game_activity);

        this.roleTextView = (TextView) findViewById(R.id.role_txt);
        this.roleTextView.setText("سلام");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.roleTextView.setTextColor(Color.WHITE);
        this.roleTextView.setPadding(35, 35, 35, 35);
        this.roleTextView.setTextSize(14);

        Button showRoleButton = findViewById(R.id.show_role);
        showRoleButton.setText("نقش: ");
        showRoleButton.setOnClickListener(getRoleButtonClickListener());


    }

    private View.OnClickListener getRoleButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roleTextView.setText("دیدن نقش: ");
            }
        };
    }
}

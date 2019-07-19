package com.findthespy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class ShowSpyActivity extends FullscreenActivity {

    private int spyId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_spy_layout);
        super.mContentView = findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();
        spyId = intent.getExtras().getInt("spyName");

        TextView spyIdText = findViewById(R.id.spy_name);
        spyIdText.setText(getString(R.string.player) + spyId);
    }
}

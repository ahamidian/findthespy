package com.findthespy.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryActivity extends FullscreenActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<String> items = new ArrayList<>();
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        super.mContentView = findViewById(R.id.fullscreen_content);

        final EditText nameEditText = findViewById(R.id.category_name);
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        final String categoryName = "category#" + getIntent().getExtras().getString("categoryName");

        final TinyDB db = new TinyDB(getApplicationContext());
        items = db.getListString(categoryName);
        recyclerView = findViewById(R.id.grid);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                gridLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new CategoryRecyclerViewAdapter(getApplicationContext(), items, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        findViewById(R.id.add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = nameEditText.getText().toString();
                if (items.contains(input)) {
                    Toast.makeText(getApplicationContext(), "Item can not be duplicated!", Toast.LENGTH_LONG).show();
                } else if (input.equals("")) {
                    Toast.makeText(getApplicationContext(), "Item can not be empty!", Toast.LENGTH_LONG).show();
                } else {
                    items.add(input);
                    db.putListString(categoryName, items);
                    nameEditText.setText("");
                    nameEditText.clearFocus();
                    closeKeyBoard();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    private void closeKeyBoard() {
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    @Override
    public void onItemClicked(String category) {
        Toast.makeText(getApplicationContext(), category, Toast.LENGTH_LONG).show();
    }
}


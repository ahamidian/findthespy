package com.findthespy.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesListActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        final EditText nameEditText = findViewById(R.id.category_name);


        final TinyDB db = new TinyDB(getApplicationContext());
        categories = db.getListString("categories");
        recyclerView = findViewById(R.id.grid);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                gridLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new CategoryRecyclerViewAdapter(getApplicationContext(), categories, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        findViewById(R.id.add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories.add(nameEditText.getText().toString());
                db.putListString("categories", categories);
                nameEditText.setText("");
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClicked(String categoryName) {
        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
    }
}


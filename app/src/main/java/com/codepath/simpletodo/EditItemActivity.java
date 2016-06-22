package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {
    private int position;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        text = getIntent().getStringExtra("text");
        position = getIntent().getIntExtra("position", 0);
//        int code = getIntent().getIntExtra("code", 0);

        setContentView(R.layout.activity_edit_item);

        EditText etEditItem = (EditText) findViewById(R.id.etToDoItem);
        etEditItem.append(text);

    }

    public void onSubmit(View v) {
        EditText etText = (EditText) findViewById(R.id.etToDoItem);
        String etNewText = etText.getText().toString();
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("newtext", etNewText);
        data.putExtra("position", position);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}

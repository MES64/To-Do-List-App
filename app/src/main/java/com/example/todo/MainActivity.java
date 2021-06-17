// ToDo Git and GitHub
// ToDo Customise text added to look nicer
// ToDo Scroll down if goes beyond the bottom of the screen
// ToDo Remove button
// ToDo Add checkbox
// ToDo Set importance and order by it
// ToDo Same with deadline (urgency)

package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addItem(View v) {
        // Create new text view
        TextView newItem = new TextView(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        newItem.setLayoutParams(layout);

        // Set text
        TextView inputItem = findViewById(R.id.editActivity);
        CharSequence itemName = inputItem.getText();
        newItem.setText(itemName);

        // Add to Linear Layout (to do list)
        LinearLayout toDoList = findViewById(R.id.toDoList);
        toDoList.addView(newItem);
    }
}
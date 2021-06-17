// ToDo Set importance and order by it
// ToDo Same with deadline (urgency)

// Importance:
// Either important or not important; put into separate sections

// Urgency:
// Order in each section by how much time there is left to do it
// Move along "array" until find the place to insert
// Then insert using addView() with the second argument: index

package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // On-Click Listener Methods ///////////////////////////////////////////////////////////////////

    public void addItem(View v) {
        CheckBox checkBox = createCheckBox();

        Button removeButton = createRemoveButton();

        LinearLayout item = createItemRow();

        item.addView(checkBox);
        item.addView(removeButton);

        // Add to Linear Layout (to do list)
        LinearLayout toDoList = findViewById(R.id.toDoList);
        toDoList.addView(item);
    }

    private void removeItem(View v) {
        LinearLayout item = (LinearLayout)v.getParent();

        LinearLayout toDoList = findViewById(R.id.toDoList);

        toDoList.removeView(item);
    }

    // Item Creation Methods: //////////////////////////////////////////////////////////////////////

    private CheckBox createCheckBox() {
        // Create new check box
        CheckBox checkBox = new CheckBox(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        checkBox.setLayoutParams(layoutParams);

        // Set text
        TextView inputName = findViewById(R.id.editActivity);
        CharSequence itemText = inputName.getText();
        checkBox.setText(itemText);

        // Set design attributes
        checkBox.setTextColor(Color.parseColor("#000000"));
        checkBox.setTextSize(20);
        //itemName.setBackgroundColor(Color.parseColor("#DDDDDD"));

        // Set text view padding
        //itemName.setPadding(0, 50, 0, 50);

        return checkBox;
    }

    private Button createRemoveButton() {
        // Create remove button
        Button removeButton = new Button(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        removeButton.setLayoutParams(layoutParams);

        // Set Text
        removeButton.setText(R.string.remove);

        // Set on-click listener
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeItem(v);
            }
        });

        return removeButton;
    }

    private LinearLayout createItemRow() {
        // Create horizontal layout
        LinearLayout item = new LinearLayout(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        item.setLayoutParams(layoutParams);

        // Set to horizontal (defaults to it anyway, but good to know)
        //item.setOrientation(LinearLayout.HORIZONTAL);

        // Set design attributes
        //item.setBackgroundColor(Color.parseColor("#0000DD"));

        return item;
    }
}
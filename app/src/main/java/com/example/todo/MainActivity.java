// ToDo Edit existing items
// ToDo Click on items to see extra details
// ToDo Keep track of non-overdue items to not keep changing the color to red

package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean important = false;
    private ArrayList<String> timeSetImportant = new ArrayList<String>();
    private ArrayList<String> timeSetUnimportant = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event listener to check importance
        Switch importanceSwitch = findViewById(R.id.importanceSwitch);
        importanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                important = isChecked ? true : false;
            }
        });

        // Get current time
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String defaultDeadline = dateFormat.format(currentDate);

        //Date currentPlusHour = Date.from(currentDate.toInstant().plus(Duration.ofHours(1)));

        //int hourPlusOne = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
        //int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        //String defaultDeadline = hourPlusOne + ":" + currentMinute;

        // Set initial text of deadline input
        TextView deadlineInput = findViewById(R.id.deadline);
        deadlineInput.setText(defaultDeadline);

        // Set a timer to schedule checking if any items are overdue every second
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {  // Needs to run on UI thread to access views
                    @Override
                    public void run() {
                        checkOverdue();
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000L, 1000L);
    }

    // On-Click Listener Methods ///////////////////////////////////////////////////////////////////

    public void addItem(View v) {
        TextView inputName = findViewById(R.id.editActivity);
        CharSequence itemText = inputName.getText();

        TextView inputDeadline = findViewById(R.id.deadline);
        CharSequence deadlineText = inputDeadline.getText();

        addItem(itemText, deadlineText, important, false);
    }

    private void addItem(CharSequence itemText, CharSequence deadlineText, boolean thisImportant, boolean isChecked) {
        // Create Item and Views Within
        CheckBox checkBox = createCheckBox(itemText, isChecked);
        TextView deadline = createDeadline(deadlineText);
        Button removeButton = createRemoveButton();
        LinearLayout item = createItemRow();

        item.addView(checkBox);
        item.addView(deadline);
        item.addView(removeButton);

        // Get correct to-do list
        LinearLayout toDoList;
        if (thisImportant) toDoList = findViewById(R.id.toDoImportant);
        else           toDoList = findViewById(R.id.toDoUnimportant);

        // Add deadline to timeSet ArrayList
        int index = addTimeLeft(deadlineText.toString(), thisImportant, isChecked);

        // Add item to correct Linear Layout (to do list)
        toDoList.addView(item, index);
    }

    private void removeItem(View v) {
        LinearLayout item = (LinearLayout)v.getParent();

        //LinearLayout toDoList = findViewById(R.id.toDoImportant);
        LinearLayout toDoList = (LinearLayout)item.getParent();

        // Find correct timeSet (important/unimportant) and remove at the correct index
        int index = toDoList.indexOfChild(item);
        if (toDoList.getTag().toString().equals("important")) {
            timeSetImportant.remove(index);

            Log.d("Array Length", Integer.toString(timeSetImportant.size()));
            Log.d("Array", "Start");
            for (int i=0; i < timeSetImportant.size(); i++) Log.d("Array", timeSetImportant.get(i));
            Log.d("Array", "End");
        }
        else {
            timeSetUnimportant.remove(index);

            Log.d("Array Length", Integer.toString(timeSetUnimportant.size()));
            Log.d("Array", "Start");
            for (int i=0; i < timeSetUnimportant.size(); i++) Log.d("Array", timeSetUnimportant.get(i));
            Log.d("Array", "End");
        }

        toDoList.removeView(item);
    }

    private void checkedPositionChange(CheckBox checkBox, boolean isChecked) {
        // Get Parents
        LinearLayout item = (LinearLayout)checkBox.getParent();
        LinearLayout toDoList = (LinearLayout)item.getParent();

        TextView currentDeadlineView = (TextView)item.getChildAt(1);

        CharSequence itemText = checkBox.getText();
        CharSequence deadlineText = currentDeadlineView.getText();
        boolean thisImportant = toDoList.getTag().toString().equals("important");

        // Update to-do list and array list
        removeItem(checkBox);
        addItem(itemText, deadlineText, thisImportant, isChecked);
    }

    // Item Creation Methods: //////////////////////////////////////////////////////////////////////

    private CheckBox createCheckBox(CharSequence itemText, boolean isChecked) {
        // Create new check box
        CheckBox checkBox = new CheckBox(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(512,
                //LinearLayout.LayoutParams.WRAP_CONTENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        checkBox.setLayoutParams(layoutParams);

        // Set text
        //TextView inputName = findViewById(R.id.editActivity);
        //CharSequence itemText = inputName.getText();
        checkBox.setText(itemText);

        // Set design attributes
        checkBox.setTextColor(Color.parseColor("#000000"));
        checkBox.setTextSize(20);
        //itemName.setBackgroundColor(Color.parseColor("#DDDDDD"));

        // Set text view padding
        //checkBox.setPadding(0, 0, 50, 0);

        // Set it to checked if needed
        checkBox.setChecked(isChecked);

        // Add event listener for the check box
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedPositionChange(checkBox, isChecked);
            }
        });

        return checkBox;
    }

    private TextView createDeadline(CharSequence deadlineText) {
        // Create deadline text view
        TextView deadline = new TextView(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(256,
                //LinearLayout.LayoutParams.WRAP_CONTENT,   // Width
                LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        deadline.setLayoutParams(layoutParams);

        // Set text
        //TextView inputDeadline = findViewById(R.id.deadline);
        //CharSequence deadlineText = inputDeadline.getText();

        // Check if the deadline is a valid time
        if (validTime(deadlineText.toString())) deadline.setText(deadlineText);
        else                                    deadline.setText("None");

        // Set design attributes
        deadline.setTextColor(Color.parseColor("#444444"));
        deadline.setTextSize(15);
        //itemName.setBackgroundColor(Color.parseColor("#DDDDDD"));

        // Set text view padding
        //deadline.setPadding(100, 0, 100, 0);

        return deadline;
    }

    private Button createRemoveButton() {
        // Create remove button
        Button removeButton = new Button(getApplicationContext());

        // Set Layout Params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                //LinearLayout.LayoutParams.MATCH_PARENT,   // Width
                //LinearLayout.LayoutParams.WRAP_CONTENT);  // Height

        removeButton.setLayoutParams(layoutParams);

        // Set Text
        removeButton.setText(R.string.remove);

        // Set button attributes
        removeButton.setTextColor(Color.parseColor("#000000"));
        removeButton.setTextSize(15);
        //removeButton.setBackgroundColor(Color.parseColor("#DD0000"));

        // Set button padding
        //removeButton.setPadding(50, 0, 0, 0);

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

// Other Methods ///////////////////////////////////////////////////////////////////////////////////

    private boolean validTime(String deadlineText) {
        // Note: Letters impossible to add!

        String[] hrsMins = deadlineText.split(":");

        if (hrsMins.length != 2) return false;

        // Check hours is a 2-digit number
        String hrs = hrsMins[0];
        if (hrs.length() != 2) return false;

        // Check mins is a 2-digit number
        String mins = hrsMins[1];
        if (mins.length() != 2) return false;

        // Check hours/mins is between 0-23/0-59 inclusive; cannot be negative...
        if (Integer.parseInt(hrs) > 23) return false;
        if (Integer.parseInt(mins) > 59) return false;

        return true;
    }

     private int addTimeLeft(String deadlineText, boolean thisImportant, boolean isChecked) {
         // Get input deadline as text
         //TextView inputDeadline = findViewById(R.id.deadline);
         //String deadlineText = inputDeadline.getText().toString();

         if (!validTime(deadlineText)) deadlineText = "24:00";  // Adds to the end of the unfinished items
         if (isChecked) {
             // Get deadline hour
             String[] hrsMins = deadlineText.split(":");
             int deadlineHour = Integer.parseInt(hrsMins[0]);

             // Add 25 hours to move the checked group of items below the unchecked ones
             String checkedDeadlineHour = Integer.toString(deadlineHour + 25);
             deadlineText = checkedDeadlineHour + ":" + hrsMins[1];
         }

         int thisMinutesLeft = getTimeLeft(deadlineText);

         // Get correct timeSet for the importance
         ArrayList<String> timeSet = thisImportant ? timeSetImportant : timeSetUnimportant;

         // Insert totalMinutesLeft in the correct place (in ascending order)
         int index = 0;
         if (!timeSet.isEmpty()) {
             int thatMinutesLeft = getTimeLeft(timeSet.get(index));
             while (thisMinutesLeft > thatMinutesLeft) {
                 index++;
                 if (index == timeSet.size()) break;
                 thatMinutesLeft = getTimeLeft(timeSet.get(index));
             }
         }
         //timeLeft.add(index, totalMinutesLeft);
         timeSet.add(index, deadlineText);

         Log.d("Array Length", Integer.toString(timeSet.size()));
         Log.d("Array", "Start");
         for (int i=0; i < timeSet.size(); i++) Log.d("Array", timeSet.get(i));
         Log.d("Array", "End");

         return index;
     }

     private int getTimeLeft(String deadlineText) {
         // ToDo Check for invalid input; throw exception

         // Extract deadline time
         String[] hrsMins = deadlineText.split(":");
         int deadlineHour = Integer.parseInt(hrsMins[0]);
         int deadlineMinute = Integer.parseInt(hrsMins[1]);

         // Get current time
         int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
         int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

         // Get time difference
         int hoursLeft = deadlineHour - currentHour;
         int minutesLeft = deadlineMinute - currentMinute;

         // Calculate the number of total minutes left and return
         return 60 * hoursLeft + minutesLeft;
     }

     private void checkOverdue() {
         // Important to-do list
         LinearLayout toDoImportant = findViewById(R.id.toDoImportant);
         for (int i=0; i<timeSetImportant.size(); i++) {
             String deadlineText = timeSetImportant.get(i);
             int timeLeft = getTimeLeft(deadlineText);

             if (timeLeft < 0) {  // if overdue
                 LinearLayout item = (LinearLayout)toDoImportant.getChildAt(i);
                 CheckBox checkBox = (CheckBox)item.getChildAt(0);

                 if (!checkBox.isChecked()) item.setBackgroundColor(Color.parseColor("#DF8781"));  // red
             }
         }

         // Unimportant to-do list
         LinearLayout toDoUnimportant = findViewById(R.id.toDoUnimportant);
         for (int i=0; i<timeSetUnimportant.size(); i++) {
             String deadlineText = timeSetUnimportant.get(i);
             int timeLeft = getTimeLeft(deadlineText);

             if (timeLeft < 0) {  // if overdue
                 LinearLayout item = (LinearLayout)toDoUnimportant.getChildAt(i);
                 CheckBox checkBox = (CheckBox)item.getChildAt(0);

                 if (!checkBox.isChecked()) item.setBackgroundColor(Color.parseColor("#DF8781"));  // red
             }
         }
     }
 }
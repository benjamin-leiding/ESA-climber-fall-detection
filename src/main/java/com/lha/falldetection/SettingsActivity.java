package com.lha.falldetection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private EditText threshold;
    private EditText fallDuration;
    private EditText fallenDistance;
    private EditText distanceToAlarm;
    private RadioButton algo1;
    private RadioButton algo2;
    private CheckBox deleteMainDB;
    private CheckBox deleteAccAltDB;
    private CheckBox createNewDB;
    private DatabaseHelper mHelper;
    //private EditText timerDuration;
    public Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        errorToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);

        EditText fallenDurance = (EditText)findViewById(R.id.editTextFD);
        fallenDurance.setTag(fallenDurance.getKeyListener());
        fallenDurance.setKeyListener(null);

        EditText fallenDistance = (EditText)findViewById(R.id.editTextFDist);
        fallenDistance.setTag(fallenDistance.getKeyListener());
        fallenDistance.setKeyListener(null);

        algo1 = (RadioButton)findViewById(R.id.radioButton1);
        algo2 = (RadioButton)findViewById(R.id.radioButton2);
        if(MainActivity.fallDetectionAlgo == 1) {
            System.out.println("MainActivity.fallDetectionAlgo == 1: " + MainActivity.fallDetectionAlgo);
            algo1.toggle();
            radioButton1Selected();
        } else if(MainActivity.fallDetectionAlgo == 2) {
            System.out.println("MainActivity.fallDetectionAlgo == 2: " + MainActivity.fallDetectionAlgo);
            algo2.toggle();
            radioButton2Selected();
        }

        distanceToAlarm = (EditText)findViewById(R.id.editTextDistToAlarm);
        System.out.println(MainActivity.distanceToAlarm);
        String distToAlarm = String.valueOf(MainActivity.distanceToAlarm);
        distanceToAlarm.setText(distToAlarm);

        fallDuration = (EditText)findViewById(R.id.editTextFD);
        fallDuration.setText(String.valueOf(MainActivity.fallDuration));
        fallenDistance = (EditText)findViewById(R.id.editTextFDist);
        fallenDistance.setText(String.valueOf(MainActivity.fallenDistance));




        //timerDuration = (EditText)findViewById(R.id.editTextTimer);
        //timerDuration.setText(String.valueOf(MainActivity.timerDuration));
        mHelper = new DatabaseHelper(this);


    }

    public void onRadioButtonClicked(View v){
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioButtonGroup);
        RadioButton rb_algo1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton rb_algo2 = (RadioButton) findViewById(R.id.radioButton2);

        // Is the current Radio Button checked?
        boolean checked = ((RadioButton) v).isChecked();

        switch(v.getId()){
            case R.id.radioButton1:
                if(checked)
                    radioButton1Selected();
                    break;

            case R.id.radioButton2:
                if(checked)
                    radioButton2Selected();
                    break;
        }
    }

    public void radioButton1Selected() {
        //Saving previous state of threshold
        threshold = (EditText)findViewById(R.id.editTextTHR);
        distanceToAlarm= (EditText)findViewById(R.id.editTextDistToAlarm);
        System.out.println("Threshold string: " + threshold.getText());
        if(threshold.getText().toString().equals("") || distanceToAlarm.getText().toString().equals("")){
            //Just do nothing
            System.out.println("Threshold string if: " + threshold.getText());
        } else {
            MainActivity.threshold_DetectingFallEvents = Double.parseDouble(threshold.getText().toString());
            MainActivity.distanceToAlarm = Double.parseDouble(distanceToAlarm.getText().toString());
        }

        RadioButton rb_algo1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton rb_algo2 = (RadioButton) findViewById(R.id.radioButton2);
        rb_algo1.setTextColor(Color.RED);
        rb_algo2.setTextColor(Color.GRAY);

        TextView thresholdTextView = (TextView)findViewById(R.id.textView3);
        thresholdTextView.setText("Threshold (m/s^2):");

        threshold = (EditText)findViewById(R.id.editTextTHR);
        System.out.println(MainActivity.threshold);
        String thrHold = String.valueOf(MainActivity.threshold);
        threshold.setText(thrHold);

        EditText thr = (EditText)findViewById(R.id.editTextTHR);
        thr.setText(String.valueOf(MainActivity.threshold));

        TextView distToAlarm = (TextView)findViewById(R.id.textView6);
        distToAlarm.setText("Distance to Alarm (m):");
    }

    public void radioButton2Selected() {
        //Saving previous state of threshold
        threshold = (EditText)findViewById(R.id.editTextTHR);
        distanceToAlarm= (EditText)findViewById(R.id.editTextDistToAlarm);
        System.out.println("Threshold string: " + threshold.getText());
        if(threshold.getText().toString().equals("") || distanceToAlarm.getText().toString().equals("")){
            //Just do nothing
            System.out.println("Threshold string if: " + threshold.getText());
        } else {
            MainActivity.threshold = Double.parseDouble(threshold.getText().toString());
            MainActivity.distanceToAlarm = Double.parseDouble(distanceToAlarm.getText().toString());  
        }

        RadioButton rb_algo1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton rb_algo2 = (RadioButton) findViewById(R.id.radioButton2);
        rb_algo2.setTextColor(Color.RED);
        rb_algo1.setTextColor(Color.GRAY);

        TextView thresholdTextView = (TextView)findViewById(R.id.textView3);
        thresholdTextView.setText("Threshold (m):");

        threshold = (EditText)findViewById(R.id.editTextTHR);
        System.out.println(MainActivity.threshold_DetectingFallEvents);
        String thrHold = String.valueOf(MainActivity.threshold_DetectingFallEvents);
        threshold.setText(thrHold);

        EditText thr = (EditText)findViewById(R.id.editTextTHR);
        thr.setText(String.valueOf(MainActivity.threshold_DetectingFallEvents));

        TextView distToAlarm = (TextView)findViewById(R.id.textView6);
        distToAlarm.setText("Distance to Alarm (m):");
    }

    public void backToMainBtnTap(View v) {
        System.out.println("backToMainBtnTap");
        double newTHR;
        double newDistToAlarm;
        int selectedAlgo = 0;
        double newTimer;
        boolean checkBox1;
        boolean checkBox2;
        boolean checkBox3;

        try {
            newTHR = Double.parseDouble(String.valueOf(threshold.getText()));
            newDistToAlarm = Double.parseDouble(String.valueOf(distanceToAlarm.getText()));
            if(algo1.isChecked()) {
                selectedAlgo = 1;
            } else if(algo2.isChecked()) {
                selectedAlgo = 2;
            }
            //checkBox1 = deleteMainDB.isChecked();
            //checkBox2 = deleteAccAltDB.isChecked();
            //checkBox3 = createNewDB.isChecked();
            //newTimer = Double.parseDouble(String.valueOf(timerDuration.getText()));
        } catch (NumberFormatException e) {
            errorToast.setText("Threshold or DistToAlarm value error.");
            errorToast.show();
            return;
        }

        if(selectedAlgo == 1) {
            MainActivity.threshold = newTHR;
        } else if (selectedAlgo == 2) {
            MainActivity.threshold_DetectingFallEvents = newTHR;
        }

        MainActivity.distanceToAlarm = newDistToAlarm;
        MainActivity.fallDetectionAlgo = selectedAlgo;

        Intent backToMain = new Intent (SettingsActivity.this, MainActivity.class);
        startActivity(backToMain);
    }


    public void createDB (View v) {
        System.out.println("Creating a new DB!");


        String[] dbName = getLatestDBNameFromNoteOnSD(getApplicationContext(), "databaseNames.txt").split("_");//mHelper.DATABASE_NAME.split("_");
        int newDBNr = Integer.parseInt(dbName[1]) + 1;

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        mHelper.DATABASE_NAME = "Fall_" + newDBNr + "_" + fDate + ".db";
        generateNoteOnSD(getApplicationContext(), "databaseNames.txt", mHelper.DATABASE_NAME+  ",");
    }

    public void chooseDB (View v) {
        System.out.println("Choose DB to check ");


        Intent dbList = new Intent (SettingsActivity.this, DBNamesListActivity.class);
        String[] dbNamesArray = getDBNotesAsStringArray();
        System.out.println("dbNames as String Array Length: " + dbNamesArray.length);
        dbList.putExtra("dbNames", dbNamesArray);
        startActivity(dbList);

    }

    public void updateDB (boolean mainDB, boolean AccAltDB, boolean createNewDB) {
        if (mainDB) {
            mHelper.getWritableDatabase().delete("fall_table",null,null);
        }
        if (AccAltDB) {
            mHelper.getWritableDatabase().delete("Acceleration",null,null);
            mHelper.getWritableDatabase().delete("Altitude",null,null);
            mHelper.getWritableDatabase().delete("KalmanAltitude",null,null);

        }
        if (createNewDB){
            System.out.println("Creating a new DB!");
            generateNoteOnSD(getApplicationContext(), "databaseNames.txt", mHelper.DATABASE_NAME);
            String[] dbName = mHelper.DATABASE_NAME.split("_");
            int newDBNr = Integer.parseInt(dbName[1]) + 1;

            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
            mHelper.DATABASE_NAME = "Fall_" + newDBNr + "_" + fDate + ".db";

        }
    }

    public String getLatestDBNameFromNoteOnSD(Context context, String sFileName) {
        try {
            File root = this.getFilesDir();
            //File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
            }
            FileInputStream fis = new FileInputStream(gpxfile);
            byte[] data = new byte[(int) gpxfile.length()];
            fis.read(data);
            fis.close();

            String str = new String(data, "UTF-8");
            String latestDBNameInList = str.split(",")[str.split(",").length - 1];

            System.out.println("This is the string from the note: " + latestDBNameInList);
            return latestDBNameInList;
        } catch (IOException e) {
            e.printStackTrace();
        }
            return "0";
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = this.getFilesDir();
            //File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
            }
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(sBody);
            writer.flush();
            writer.close();
            System.out.println("Before toast in txt generator");
            String dbName = sBody.substring(0, sBody.length() - 1);
            Toast.makeText(context, "Stored: " + dbName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getDBNotesAsStringArray() {
        try {
            File root = this.getFilesDir();
            File gpxfile = new File(root, "databaseNames.txt");
            if (gpxfile.exists()) {
                FileInputStream is = new FileInputStream(gpxfile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                String [] firstLineArray = line.split(",");
                for(int i = 0; i < firstLineArray.length; i++) {
                    System.out.println("line db Item: " + i + ". : " + firstLineArray[i]);
                }
                String lastDBName = firstLineArray[firstLineArray.length - 1];
                System.out.println("Length of Line Array: " + firstLineArray.length);
                System.out.println("databaseNames: " +  line);
                System.out.println("lastDBName: " +  lastDBName);
                return firstLineArray;
            } else{
                String[] dbName = new String[] {mHelper.DATABASE_NAME};
                System.out.println("dbName: " +  dbName);

                return dbName;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] dbName = new String[] {""};
        System.out.println("dbName: " +  dbName);

        return dbName;
    }
}

package com.lha.falldetection;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

        distanceToAlarm = (EditText)findViewById(R.id.editTextDistToAlarm);
        System.out.println(MainActivity.distanceToAlarm);
        String distToAlarm = String.valueOf(MainActivity.distanceToAlarm);
        distanceToAlarm.setText(distToAlarm);

        threshold = (EditText)findViewById(R.id.editTextTHR);
        System.out.println(MainActivity.threshold);
        String thr = String.valueOf(MainActivity.threshold);
        threshold.setText(thr);

        fallDuration = (EditText)findViewById(R.id.editTextFD);
        fallDuration.setText(String.valueOf(MainActivity.fallDuration));
        fallenDistance = (EditText)findViewById(R.id.editTextFDist);
        fallenDistance.setText(String.valueOf(MainActivity.fallenDistance));
        //timerDuration = (EditText)findViewById(R.id.editTextTimer);
        //timerDuration.setText(String.valueOf(MainActivity.timerDuration));
        mHelper = new DatabaseHelper(this);


    }


    public void backToMainBtnTap(View v) {
        System.out.println("backToMainBtnTap");
        double newTHR;
        double newDistToAlarm;
        double newTimer;
        boolean checkBox1;
        boolean checkBox2;
        boolean checkBox3;

        //deleteMainDB = findViewById(R.id.checkBox1);
        //deleteAccAltDB = findViewById(R.id.checkBox2);
        //createNewDB = findViewById(R.id.checkBox3);

        try {
            newTHR = Double.parseDouble(String.valueOf(threshold.getText()));
            newDistToAlarm = Double.parseDouble(String.valueOf(distanceToAlarm.getText()));
            //checkBox1 = deleteMainDB.isChecked();
            //checkBox2 = deleteAccAltDB.isChecked();
            //checkBox3 = createNewDB.isChecked();
            //newTimer = Double.parseDouble(String.valueOf(timerDuration.getText()));
        } catch (NumberFormatException e) {
            errorToast.setText("Threshold or DistToAlarm value error.");
            errorToast.show();
            return;
        }
        MainActivity.threshold = newTHR;
        MainActivity.distanceToAlarm = newDistToAlarm;
        //MainActivity.timerDuration = newTimer;

        /**if(checkBox1 || checkBox2 || checkBox3) {
         System.out.println("Check the boxes: " + checkBox1 + checkBox2 + checkBox3);
         updateDB(checkBox1, checkBox2, checkBox3);
         }**/

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

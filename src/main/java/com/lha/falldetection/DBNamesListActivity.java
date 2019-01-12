package com.lha.falldetection;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

public class DBNamesListActivity extends ListActivity {

    private static String[] dbNames = new String[] {

            "Sample", "Data"
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] dbNames = extras.getStringArray("dbNames");
            for (int i = 0; i < dbNames.length; i++) {
                System.out.println("dbNames[" + i + "]: " +dbNames[i]);
            }
            this.dbNames = dbNames;
            //The key argument here must match that used in the other activity
        }


        setListAdapter(new ArrayAdapter< String >(this,
                android.R.layout.simple_list_item_1, this.dbNames));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        /**new AlertDialog.Builder(this)
                .setTitle("Hello")
                .setMessage("from " + getListView().getItemAtPosition(position))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                .show();**/


        System.out.println("ListView: " + l.toString() + "\n" +
                "View: " + v.toString() + "\n" +
                "position: " + String.valueOf(position) + "\n" +
                "id: " + String.valueOf(id));
        TextView dbNameView = (TextView)v;
        String selectedDB = dbNameView.getText().toString();
        System.out.println("Clicked item: " + selectedDB);
        Toast.makeText(DBNamesListActivity.this,
                "Selected DB: " + selectedDB,
                Toast.LENGTH_LONG).show();

        Intent settingsActivity = new Intent (DBNamesListActivity.this, MainActivity.class);
        settingsActivity.putExtra("selectedDBName", selectedDB);
        DatabaseHelper.DATABASE_NAME = selectedDB;
        startActivity(settingsActivity);
    }

}

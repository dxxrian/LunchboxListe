package com.example.lunchboxliste;

import static java.lang.Integer.parseInt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public com.example.lunchboxliste.DBHandler dbHandler;
    ArrayList<String> databasearr;
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new com.example.lunchboxliste.DBHandler(MainActivity.this);
        drawLV();
    }

    public void onSwitch(View v) {
        Intent intent = new Intent(this, MainActivity2.class);
        MainActivity2Result.launch(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        drawLV();
    }

    ActivityResultLauncher<Intent> MainActivity2Result = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        dbHandler.add_lunchbox(data.getStringExtra("date"), data.getStringExtra("lunch"), data.getStringExtra("amount"), data.getStringExtra("fach"));
                    }
                    adapter.notifyDataSetChanged();
                    drawLV();
                }
            });

    public void drawLV() {
        lv = (ListView) findViewById(R.id.lv);
        databasearr = dbHandler.readLunchbox();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, databasearr);
        //Log.w("dorian",databasearr.get(1));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = (String) parent.getItemAtPosition(position);
               String name = selectedItem;
               name = name.substring(name.indexOf(": ")+1);
               name = name.substring(0, name.indexOf("\nMenge: "));
               new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Gericht entnehmen")
                        .setMessage("Wollen Sie" + name + " wirklich entnehmen?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String[] selectedItemSplit = selectedItem.split(" ");
                                if(parseInt(selectedItemSplit[selectedItemSplit.length - 9]) <= 1) {
                                    dbHandler.delete(parseInt(selectedItemSplit[selectedItemSplit.length - 1]));
                                } else {
                                    dbHandler.updateAmount(parseInt(selectedItemSplit[selectedItemSplit.length - 1]), parseInt(selectedItemSplit[selectedItemSplit.length - 9]) - 1);
                                }
                                drawLV();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }

        });
    }

}



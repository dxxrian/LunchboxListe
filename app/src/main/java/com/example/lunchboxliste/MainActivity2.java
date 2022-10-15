package com.example.lunchboxliste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    EditText editTextDatePicker;
    DatePickerDialog picker;
    SimpleDateFormat simpleDateFormat;
    public com.example.lunchboxliste.DBHandler dbHandler;
    ArrayList<String> databasearr;
    Spinner spinner;
    Spinner spinner_fach;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dbHandler = new com.example.lunchboxliste.DBHandler(MainActivity2.this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(Calendar.getInstance().getTime()).toString();
        editTextDatePicker=(EditText) findViewById(R.id.editTextDate);
        editTextDatePicker.setText(currentDate);
        editTextDatePicker.setInputType(InputType.TYPE_NULL);
        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if(dayOfMonth < 10 && (monthOfYear + 1) < 10) {
                                    editTextDatePicker.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                } else if(dayOfMonth < 10 && (monthOfYear + 1) >= 10) {
                                    editTextDatePicker.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                } else if(dayOfMonth >= 10 && (monthOfYear + 1) < 10) {
                                    editTextDatePicker.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                } else {
                                    editTextDatePicker.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        spinner = findViewById(R.id.spinner);
        databasearr = dbHandler.readGerichte();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, databasearr);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem() != null) {
                    EditText editTextGericht = (EditText) findViewById(R.id.editTextGericht);
                    editTextGericht.setText(spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_fach = findViewById(R.id.spinner2);
        List<Integer> faecher = new ArrayList<Integer>();
        for(int i=1; i<=8; i++) {
            faecher.add(i);
        }
        ArrayAdapter<Integer> faecherAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, faecher);
        faecherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fach.setAdapter(faecherAdapter);

    }


        public void onSubmit(View v){
        EditText editTextGericht = (EditText)findViewById(R.id.editTextGericht);
        EditText editTextAnzahl = (EditText)findViewById(R.id.editTextNumberDecimal);
        if (editTextAnzahl.getText().toString().matches("") || editTextGericht.getText().toString().matches("")) {
            Toast.makeText(MainActivity2.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("date",editTextDatePicker.getText().toString());
        resultIntent.putExtra("lunch",editTextGericht.getText().toString());
        resultIntent.putExtra("amount",editTextAnzahl.getText().toString());
        resultIntent.putExtra("fach", spinner_fach.getSelectedItem().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
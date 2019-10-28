package com.example.happybank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SettingsActivity extends AppCompatActivity {

    public static final String nameID = "NAME_ID";
    public static final String bedtimeID = "BED_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Prefill the settings edit texts with current settings
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        if(preferences.contains(nameID)) {
            EditText nameEdit = findViewById(R.id.nameEditText);
            nameEdit.setText(preferences.getString(nameID, getString(R.string.default_name)));
        }

        final EditText bedEdit = findViewById(R.id.bedtimeEditText);
        bedEdit.setShowSoftInputOnFocus(false);
        final Activity parent = this;
        bedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                    //Make sure the soft keyboard is not visible
                    InputMethodManager imm = (InputMethodManager) parent.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = parent.getCurrentFocus();
                    if (view == null) {
                        view = new View(parent);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    showTimePickerDialog(bedEdit);
                }
            }
        });
        if(preferences.contains(bedtimeID)) {
            bedEdit.setText(preferences.getString(bedtimeID, getString(R.string.default_bedtime)));
        }

        //Set the deposit count
        try {
            File depositsFile = new File(getFilesDir(), MainActivity.DEPOSIT_FILENAME);
            Scanner depositsInput = new Scanner(depositsFile);
            Integer depositCount = 0;
            while(depositsInput.hasNextLine()) {
                depositsInput.nextLine();
                depositCount++;
            }
            depositsInput.close();

            TextView countText = findViewById(R.id.depositCountNumber);
            countText.setText(depositCount.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Button saveButton = (Button) findViewById(R.id.settingsSaveButton);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdit = findViewById(R.id.nameEditText);
                EditText bedEdit = findViewById(R.id.bedtimeEditText);

                editor.putString(nameID, nameEdit.getText().toString());
                editor.putString(bedtimeID, bedEdit.getText().toString());

                editor.apply();
                setResult(RESULT_OK);
                parent.finish();
            }
        });

    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}

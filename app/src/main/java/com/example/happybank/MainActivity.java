package com.example.happybank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String DEPOSIT_FILENAME = "DepositFile.txt";
    public final static int DEPOSIT_CODE = 1;
    public final static int SETTINGS_CODE = 2;

    private AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity parent = this;
        Button depositButton = (Button) findViewById(R.id.mainDepositButton);
        depositButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the Deposit Activity
                Intent intent = new Intent(parent, DepositActivity.class);
                startActivityForResult(intent, DEPOSIT_CODE);
            }
        });

        Button withdrawButton = (Button) findViewById(R.id.mainWithdrawButton);
        withdrawButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the Withdraw Activity
                Intent intent = new Intent(parent, WithdrawActivity.class);
                startActivity(intent);
            }
        });

        Button aboutButton = (Button) findViewById(R.id.mainAboutButton);
        aboutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the Settings Activity
                Intent intent = new Intent(parent, AboutActivity.class);
                startActivity(intent);
            }
        });

        Button settingsButton = (Button) findViewById(R.id.mainSettingsButton);
        settingsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the Settings Activity
                Intent intent = new Intent(parent, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_CODE);
            }
        });

        alarmService = new AlarmService(this);
        alarmService.setAlarms();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DEPOSIT_CODE) {
            Toast.makeText(this, "Deposited " + resultCode + " things into your Happy Bank.", Toast.LENGTH_LONG).show();
        } else if(requestCode == SETTINGS_CODE) {
            if(resultCode == RESULT_OK) {
                alarmService.setAlarms();
                Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.happybank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DepositActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
    }

    public void makeDeposit(View view) {

        int numDeposits = 0;

        try {
            //Open or create the DepositOutput file
            File depositOutputFile = new File(getFilesDir(), MainActivity.DEPOSIT_FILENAME);
            if(!depositOutputFile.exists()) {
                depositOutputFile.createNewFile();
            }
            FileOutputStream depositOutputStream = new FileOutputStream(depositOutputFile, true);

            //Append the new deposits to the file
            ArrayList<EditText> editTexts = new ArrayList<EditText>();
            editTexts.add((EditText) findViewById(R.id.depositInput1));
            editTexts.add((EditText) findViewById(R.id.depositInput2));
            editTexts.add((EditText) findViewById(R.id.depositInput3));

            for(EditText editText : editTexts) {
                String depositMessage = editText.getText().toString();
                //Strip any newlines from message as this is the delimiter used in the output file
                //Replace curly braces as these denote the start and end of the message
                depositMessage = depositMessage.replace("\n", " ");
                depositMessage = depositMessage.replace("{", "[");
                depositMessage = depositMessage.replace("}", "]");
                depositMessage = depositMessage.trim();

                if(depositMessage.length() > 0) {
                    String outputString = "{" + depositMessage + "}0\n";
                    depositOutputStream.write(outputString.getBytes());
                    numDeposits++;
                }
            }

            depositOutputStream.close();
        } catch (Exception e) {
             e.printStackTrace();
        }

        //Close the activity and return to main activity
        setResult(numDeposits);
        finish();

    }
}

package com.example.happybank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WithdrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        //Display something from the happy bank
        try {
            File depositsFile = new File(getFilesDir(), MainActivity.DEPOSIT_FILENAME);
            Scanner depositsInput = new Scanner(depositsFile);
            ArrayList<String> allMessages = new ArrayList<String>();
            ArrayList<Integer> allShowCounts = new ArrayList<Integer>();


            //Read in all the messages
            //Check the lowest show count
            int lowestCount = Integer.MAX_VALUE;
            while(depositsInput.hasNextLine()) {
                String nextLine = depositsInput.nextLine();
                String message = nextLine.substring(nextLine.indexOf('{') + 1, nextLine.indexOf('}'));
                Integer showCount = Integer.parseInt(nextLine.substring(nextLine.indexOf('}') + 1));
                allMessages.add(message);
                allShowCounts.add(showCount);
                if(showCount < lowestCount) {
                    lowestCount = showCount;
                }
            }

            depositsInput.close();

            //If there were any messages choose one of the ones with the lowest count to show
            //Or if the favour recent field is not set in the preferences file show any message
            if(lowestCount < Integer.MAX_VALUE) {

                //Check the favour recent field
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), Context.MODE_PRIVATE);
                boolean favourRecent = preferences.getBoolean("RECENT_ID", false);

                ArrayList<Integer> possibleMessageIndices = new ArrayList<Integer>();

                if(favourRecent) {
                    for(int i=0; i < allShowCounts.size(); i++) {
                        if(allShowCounts.get(i).intValue() == lowestCount) {
                            possibleMessageIndices.add(i);
                        }
                    }
                } else {
                    for(int i=0; i < allShowCounts.size(); i++) {
                        possibleMessageIndices.add(i);
                    }
                }

                Random rand = new Random();
                int randomIndex = rand.nextInt(possibleMessageIndices.size());
                int chosenMessageIndex = possibleMessageIndices.get(randomIndex);

                //Show that message in the textView
                TextView messageTextView = findViewById(R.id.withdrawTextView);
                messageTextView.setText(allMessages.get(chosenMessageIndex));

                //Update the show count for the shown message
                allShowCounts.set(chosenMessageIndex, allShowCounts.get(chosenMessageIndex) + 1);

                //Rewrite the DepositFile
                FileOutputStream depositOutputStream = new FileOutputStream(depositsFile);
                for(int i=0; i < allShowCounts.size(); i++) {
                    String outputString = "{" + allMessages.get(i) + "}" + allShowCounts.get(i) + "\n";
                    depositOutputStream.write(outputString.getBytes());
                }

                depositOutputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set up the back button
        final Activity parent = this;
        Button withdrawBackButton = (Button) findViewById(R.id.withdrawBackButton);
        withdrawBackButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Finish the activity
                parent.finish();
            }
        });
    }
}

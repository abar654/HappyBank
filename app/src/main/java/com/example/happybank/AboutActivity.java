package com.example.happybank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Set up the back button
        final Activity parent = this;
        Button backButton = (Button) findViewById(R.id.aboutBackButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Finish the activity
                parent.finish();
            }
        });

    }
}

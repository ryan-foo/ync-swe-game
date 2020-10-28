package com.example.bomberkong.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import com.example.bomberkong.R;

public class LoadActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton;
    Button howToPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        howToPlayButton = findViewById(R.id.howToPlayButton);
        howToPlayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startButton){
            openGameActivity();
        } else {
            openHowToPlayActivity();
        }

    }

    public void openGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void openHowToPlayActivity(){
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }


}
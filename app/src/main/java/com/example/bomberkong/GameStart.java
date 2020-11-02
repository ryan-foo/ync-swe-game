package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game_start);

        Button single_player = (Button) findViewById(R.id.button);
        // Currently the code from 'EXIT' button
        single_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameStart.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Currently the code from 'SETTINGS' button
        Button multi_player = (Button) findViewById(R.id.button2);
        multi_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameStart.this, HowToPlay.class);
                startActivity(intent);
            }
        });

        Button how_to_play = (Button) findViewById(R.id.button3);
        how_to_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameStart.this, HowToPlay.class);
                startActivity(intent);
            }
        });
    }



}
package com.example.bomberkong.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bomberkong.R;

public class HowToPlayActivity extends AppCompatActivity {

    private Button joinGame;
    private Button createGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        Button joinGame = (Button) findViewById(R.id.howto_joingame);
        Button createGame = (Button) findViewById(R.id.howto_creategame);

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeListOfRooms = new Intent(HowToPlayActivity.this, SelectPlayerActivity.class);
                startActivity(seeListOfRooms);
            }
        });

        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewRoom = new Intent(HowToPlayActivity.this, SelectPlayerActivity.class);
                startActivity(createNewRoom);
            }
        });

    }
}
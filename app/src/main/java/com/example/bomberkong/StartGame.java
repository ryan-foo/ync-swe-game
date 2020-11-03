package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartGame extends AppCompatActivity {

    private Button single_player_game;
    private Button multi_player_game;
    private Button how_to_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Button single_player_game = (Button) findViewById(R.id.single_player);
        Button multi_player_game = (Button) findViewById(R.id.multi_player);
        Button how_to_play = (Button) findViewById(R.id.howtoplay);

        single_player_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playerActivity = new Intent(StartGame.this, SelectPlayerActivity.class);
                playerActivity.putExtra("playerID", "1");
                startActivity(playerActivity);
            }
        });

        multi_player_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playerActivity = new Intent(StartGame.this, SelectPlayerActivity.class);
                playerActivity.putExtra("playerID", "1");
                startActivity(playerActivity);
            }
        });

        how_to_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playerActivity = new Intent(StartGame.this, SelectPlayerActivity.class);
                playerActivity.putExtra("playerID", "1");
                startActivity(playerActivity);
            }
        });
    }
}

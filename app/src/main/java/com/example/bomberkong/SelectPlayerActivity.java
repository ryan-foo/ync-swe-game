package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectPlayerActivity extends AppCompatActivity {
    private Button player1Button;
    private Button player2Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);

        //Button buttonClick = (Button)rootView.findViewById(R.id.button);
        //buttonClick.setOnClickListener(new View.OnClickListener() {
        player1Button = (Button)findViewById(R.id.buttonP1);
        player2Button = (Button)findViewById(R.id.buttonP2);
        final Intent playerActivity = new Intent(getApplicationContext(), MainActivity.class);

        player1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerActivity.putExtra("playerID", "1");
                startActivity(playerActivity);
            }
        });

        player2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerActivity.putExtra("playerID", "2");
                startActivity(playerActivity);
            }
        });

    }
}
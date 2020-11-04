package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WaitingRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        // Add Event Listener... As second player enters room... Start the Game!

        // Intent from previous screen contains EXTRA_ROOM_MESSAGE plus the nameOfRoom
    }
}
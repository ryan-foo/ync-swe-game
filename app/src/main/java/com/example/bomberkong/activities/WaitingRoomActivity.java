package com.example.bomberkong.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bomberkong.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is a waiting room for the user who creates a room
 * while a second player a second player joins the room.
 */
public class WaitingRoomActivity extends AppCompatActivity {

    /**
     * Instance of firebase to communicate with the database
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public String nameOfRoom;

    /**
     * Creates the Waiting Room activity
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        String nameOfRoom = getIntent().getStringExtra("RoomName");

        // TODO: Intent from previous screen contains EXTRA_ROOM_MESSAGE plus the nameOfRoom
        // Done?
        DatabaseReference _roomNameRefPlayer1 = database.getReference("rooms/"+nameOfRoom+"/player1");
        _roomNameRefPlayer1.setValue(true);

        DatabaseReference _roomNameRefPlayer2 = database.getReference("rooms/"+nameOfRoom+"/player2");
        _roomNameRefPlayer2.setValue(false);

        DatabaseReference _roomListener = database.getReference("rooms/"+nameOfRoom);
        _roomListener.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("player2").exists()) {
                    boolean P2Existence = snapshot.child("player2").getValue(boolean.class);
                    if (P2Existence) {
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        intent.putExtra("playerID", "1");
                        startActivity(intent);
                    }
                }
                /*
                //try{
                    boolean P2Existence = snapshot.getValue(boolean.class);
                    if (P2Existence) {
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        intent.putExtra("playerID", "1");
                        startActivity(intent);
                    }
                //} catch (NullPointerException e) {
                //    System.err.println("Null pointer exception");
                //}
                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // TODO: Add Event Listener... As second player enters room... Start the Game!
    // Done?
    /*
    private void addRoomEventListener() {
        DatabaseReference _roomListener = database.getReference("rooms/"+nameOfRoom+"/player2");
        _roomListener.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    boolean P2Existence = snapshot.getValue(boolean.class);
                    if (P2Existence) {
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        intent.putExtra("playerID", "1");
                        startActivity(intent);
                    }
                } catch (NullPointerException e) {
                    System.err.println("Null pointer exception");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */
}


package com.example.bomberkong.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bomberkong.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity creates a screen with a list of all available game rooms
 * By clicking on a room, you can join the game.
 */
public class JoinRoomActivity extends AppCompatActivity {

    /**
     * Instance of firebase to communicate with the database
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Displays the list of all available game rooms
     */
    public ListView listView;

    /**
     * Contains the list of all available game rooms
     */
    public List<String> listOfAvailableRooms;

    /**
     * Instantiates a string that will contain which room the player will join
     */
    String joiningRoom = "";

    /**
     * Creates the activity
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        final ListView listView = (ListView) findViewById(R.id.listOfRooms);
        // instantiate list of available rooms
        listOfAvailableRooms = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(JoinRoomActivity.this, android.R.layout.simple_list_item_1,listOfAvailableRooms);
        listView.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference _roomsRef = database.getReference("rooms");
        _roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfAvailableRooms.clear();
                for (DataSnapshot children : snapshot.getChildren()) {
                    listOfAvailableRooms.add(children.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //addListOfRoomsListener();

        /*
        DatabaseReference roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //try {
                    listOfAvailableRooms.clear();
                    Iterable<DataSnapshot> rooms = snapshot.getChildren();
                    for (DataSnapshot child : rooms) {
                        listOfAvailableRooms.add(child.getKey());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(JoinRoomActivity.this,
                            android.R.layout.simple_list_item_1, listOfAvailableRooms);
                    //listView.setAdapter(adapter);
                //} catch (NullPointerException e) {
                    //System.err.println("Null pointer exception (outer).");
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        // Add function for clicking on list to join a room
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the name of room to join
                joiningRoom = listOfAvailableRooms.get(position);

                // Add yourself to room on Firebase
                //DatabaseReference _P1Exists = database.getReference("rooms/"+joiningRoom+"/player1");
                DatabaseReference _P2Exists = database.getReference("rooms/"+joiningRoom+"/player2");
                _P2Exists.setValue(true);

                // Join game as Player Two
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                intent.putExtra("playerID", "2");
                startActivity(intent);
            }
        });

        // Add function for retrieving list of available rooms from Firebase
    }

    //private void addListOfRoomsListener() {
    //}
}
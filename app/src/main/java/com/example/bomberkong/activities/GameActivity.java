package com.example.bomberkong.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.example.bomberkong.World;

// credits for framework: John Horton

/**
 * This activity starts the main game grid
 */
public class GameActivity extends Activity {
    /**
     * The world stores instances such as the main game grid, players, and soundPools. It acts as the
     * main game loop, where the run method keeps track of updates and the onTouchEvent handles player
     * inputs.
     */
    private World mWorld;

    /**
     * Instantiates the game grid Activity
     *
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets the player number that is being controlled - 1 or 2.
        String playerNumControlled = getIntent().getStringExtra("playerID");

        // To get the display and display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Log.e("GameActivity",getApplicationContext().toString());
        // Instantiates the World
        mWorld = new World(getApplicationContext(), size.x, size.y, playerNumControlled);
        setContentView(mWorld);
    }

    /**
     * Resumes the game
     */
    @Override
    protected void onResume() {
        super.onResume();
        mWorld.resume();
    }

    /**
     * Pauses the game
     */
    @Override
    protected void onPause() {
        super.onPause();
        mWorld.pause();
    }
}
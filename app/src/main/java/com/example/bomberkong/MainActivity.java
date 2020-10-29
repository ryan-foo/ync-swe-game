package com.example.bomberkong;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

// credits for framework: John Horton

public class MainActivity extends Activity {
    private World mWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // to get the display and display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mWorld = new World(this, size.x, size.y);
        setContentView(mWorld);
    }

    // Resume and Pause
    @Override
    protected void onResume() {
        super.onResume();

        mWorld.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To do

        mWorld.pause();
    }
}
package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceView;

import com.example.bomberkong.model.Grid;

public class MainActivity extends AppCompatActivity {
    private World mWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sv = null;
        Grid grid = null;

        // to get the display and display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

//        setContentView(R.layout.activity_main);
        mWorld = new World(this, size.x, size.y);
        setContentView(mWorld);
//        grid = mWorld.returnGrid();

        // Game loop happens here: it refreshes everytime there is a change to the grid
//        GridRenderer gridRenderer = new GridRenderer(grid);
//        grid.addCallBack(gridRenderer);
//
//        // Phone resolution
//        sv = (SurfaceView) findViewById(R.id.surfaceView);
//        sv.setWillNotDraw(false);
//        sv.getHolder().addCallback(gridRenderer);

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
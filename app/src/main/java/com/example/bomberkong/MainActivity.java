package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import com.example.bomberkong.model.Grid;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SurfaceView sv           = null;
        Grid grid                 = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        World tw = new World();
        grid = tw.returnGrid();

        // Game loop happens here: it refreshes everytime there is a change to the grid
        GridRenderer gridRenderer = new GridRenderer(grid);
        grid.addCallBack(gridRenderer);

        // Phone resolution
        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sv.setWillNotDraw(false);
        sv.getHolder().addCallback(gridRenderer);

    }

    // Resume and Pause
    @Override
    protected void onResume() {
        super.onResume();
        // To do
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To do
    }
}
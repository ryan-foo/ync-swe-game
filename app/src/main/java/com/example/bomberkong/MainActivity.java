package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import com.example.bomberkong.model.Grid;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SurfaceView sv = null;
        Grid grid = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        World world = new World();
        grid = world.returnGrid();

        GridRenderer gridRenderer = new GridRenderer(grid, this);
        grid.addCallBack(gridRenderer);

        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sv.setWillNotDraw(false);
        sv.getHolder().addCallback(gridRenderer);

    }
}
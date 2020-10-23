package com.example.bomberkong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceView;

import com.example.bomberkong.model.Grid;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SurfaceView sv = null;
        Grid grid = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Gets the size of the display
         */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        /**
         * We pass the size of the display into world
         */
        World world = new World(this, size.x, size.y);

        grid = world.returnGrid();

        Renderer renderer = new Renderer(world, this);
        grid.addCallBack(renderer);

        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sv.setWillNotDraw(false);
        sv.getHolder().addCallback(renderer);

    }
}
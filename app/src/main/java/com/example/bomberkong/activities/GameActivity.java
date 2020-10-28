package com.example.bomberkong.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;

import com.example.bomberkong.InputHandler;
import com.example.bomberkong.InputListener;
import com.example.bomberkong.R;
import com.example.bomberkong.Renderer;
import com.example.bomberkong.World;
import com.example.bomberkong.model.Grid;

public class GameActivity extends AppCompatActivity {

    private final static String TAG = "GameActivity";
    private World world;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SurfaceView sv = null;
        Grid grid = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        /**
         * Gets the size of the display
         */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        /**
         * We pass the size of the display into world
         */
        world = new World(this, size.x, size.y);

        grid = world.returnGrid();

        Renderer renderer = new Renderer(world, this);
        grid.addCallBack(renderer);

        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sv.setWillNotDraw(false);
        sv.getHolder().addCallback(renderer);

        Log.d("TAG", "test");
        InputListener inputListener = new InputListener(20, 10, size.x, size.y);
        sv.setOnTouchListener(inputListener);

        InputHandler inputHandler = new InputHandler(grid);
        inputListener.setCallback(inputHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();

        world.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To do

        world.pause();
    }
}
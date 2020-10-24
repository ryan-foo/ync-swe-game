package com.example.bomberkong;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.bomberkong.util.Float2;
import com.example.bomberkong.util.Int2;

public class InputListener implements View.OnTouchListener {
    private String TAG = "InputListener";

    //the absolute position of the touch
    private Float2 click_pos;

    private Callback callback;

    //passed on through constructor
    private int xCount;
    private int yCount;
    private int gridWidth;
    private int gridHeight;

    public InputListener(int xCount, int yCount, int gridWidth, int gridHeight) {
        this.xCount = xCount;
        this.yCount = yCount;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Float2 dim = new Float2(view.getWidth(), view.getHeight());
        Float2 current = new Float2(motionEvent.getX(), motionEvent.getY());

        Log.d(TAG, "ontouch created");
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                click_pos = new Float2(motionEvent.getX(), motionEvent.getY());
                Int2 grid_pos = absoluteToGridPos(click_pos.getX(), click_pos.getY(), xCount, yCount, gridWidth, gridHeight);
                //pass the grid_pos into the InputHandler to handle the the input.
                callback.onClick(grid_pos);
                break;
        }
        return true;
    }

    Int2 absoluteToGridPos(float absX, float absY, int xCount, int yCount, int gridWidth, int gridHeight) {

        // number of cells width wise
        // number of cells height wise
        int xGrid = -1;
        int yGrid = -1;

        for (int nx = 0; nx < xCount; nx++) {
            for (int ny = 0; ny < yCount; ny++) {

                float xpos1 = nx * gridWidth / xCount;
                float ypos1 = ny * gridHeight / yCount;
                float xpos2 = (nx + 1) * gridWidth / xCount;
                float ypos2 = (ny + 1) * gridHeight / yCount;

                // We have found the x pos on the grid
                if (xpos1 < absX && absX < xpos2) {
                    xGrid = nx;
                }

                // We have found the y pos on the grid
                if (ypos1 < absY && absY < ypos2) {
                    yGrid = ny;
                }
            }
        }

        return new Int2(xGrid, yGrid);
    }

    /**
     * Set the callback object to call
     * when touch actions occur.
     * @param cb The callback object
     */
    public void setCallback(Callback cb) {
        this.callback = cb;
    }

    public interface Callback {
        void onClick(Int2 grid_pos);
    }
}

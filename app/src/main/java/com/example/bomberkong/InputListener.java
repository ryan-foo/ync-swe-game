package com.example.bomberkong;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.bomberkong.util.Float2;
import com.example.bomberkong.util.Int2;

public class InputListener implements View.OnTouchListener {
    private String TAG = "Input";

    //the absolute position of the touch
    private Float2 down_pos;

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
        Float2 dim     = new Float2(view.getWidth(), view.getHeight());
        Float2 current = new Float2(motionEvent.getX(), motionEvent.getY());

        Log.d(TAG, "ontouch created");
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "pressed down");
                down_pos = new Float2(motionEvent.getX(), motionEvent.getY());
                Log.d(TAG, String.valueOf(down_pos));
                Int2 gridPos = absoluteToGridPos(down_pos.getX(), down_pos.getY(), xCount, yCount, gridWidth, gridHeight);
                Log.d(TAG, (gridPos.getX()) + "," + gridPos.getY());
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

}

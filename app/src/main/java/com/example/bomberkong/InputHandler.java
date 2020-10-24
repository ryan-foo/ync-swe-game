package com.example.bomberkong;

import android.util.Log;

import com.example.bomberkong.model.Grid;
import com.example.bomberkong.util.Int2;

public class InputHandler implements InputListener.Callback  {

    private Grid grid;
    private String TAG = "InputHandler";

    public InputHandler(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void onClick(Int2 grid_pos) {
        Log.d(TAG, "we're at InputHandler");
        Log.d(TAG, grid_pos.getX() + "," + grid_pos.getY());
    }
}

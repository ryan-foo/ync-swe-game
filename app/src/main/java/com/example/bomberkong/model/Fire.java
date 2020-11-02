package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.bomberkong.util.Int2;

import static com.example.bomberkong.R.drawable.fire;

public class Fire implements Cell
{
    public static Int2 position;
    private final Context context;
    private final Grid grid;
    private int cellWidth;
    private int cellHeight;
    private Bitmap mBitmapFire;
    public int ticksToFade = 10; // our current game is 10 fps, so we will have 3 seconds before it explodes.

    // todo: continue from here

    public Fire(Context context, Grid grid, Int2 position, Int2 cellSize) {
        this.grid = grid;
        this.context = context;
        this.position = position;
        cellWidth = cellSize.x;
        cellHeight = cellSize.y;

        mBitmapFire = BitmapFactory.decodeResource(context.getResources(), fire);
        // Resize the bitmap
        // todo: gridPosToAbsolute
        mBitmapFire = Bitmap.createScaledBitmap(mBitmapFire, cellWidth, cellHeight, false);
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public Int2 getGridPosition(){
        return this.position;
    }

    public void remove() {
        grid.setCell(getGridPosition(), CellStatus.EMPTY);
    }
}

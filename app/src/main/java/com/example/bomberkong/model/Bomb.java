package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.bomberkong.R;
import com.example.bomberkong.util.Int2;

public class Bomb implements Cell
{
    public static Int2 position;
    private int cellWidth;
    private int cellHeight;
    private Bitmap mBitmapBomb;
    public int ticksToExplode = 200; // our current game is 10 fps, so we will have 3 seconds before it explodes.

    // todo: continue from here

    public Bomb(Context context, Int2 position, Int2 cellSize) {
        this.position = position;
        cellWidth = cellSize.x;
        cellHeight = cellSize.y;

        mBitmapBomb =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey);
        // Resize the bitmap
        // todo: gridPosToAbsolute
        mBitmapBomb =
                Bitmap.createScaledBitmap(mBitmapBomb, cellWidth, cellHeight, false);
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

    public void explode(Grid grid){
        // todo: create a neighbors function, change all neighbors that qualify into fire
        grid.setCell(getGridPosition(), CellStatus.EMPTY);
        // change empty tiles and food around into Fire
        // check if tiles next to the bomb are empty
        // if so, create fire on them
        // create Fire objects with lifetime
        // lifetime of fire objects should tick down
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapBomb,
                position.getX() * cellWidth, position.getY() * cellHeight, paint);
    }
}

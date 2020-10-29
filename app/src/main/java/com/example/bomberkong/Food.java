package com.example.bomberkong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.bomberkong.R;
import com.example.bomberkong.util.Int2;

import java.util.ArrayList;
import java.util.Random;


public class Food {
    public static Int2 location;
    // currently, this will hold the max values for horizontal / vert positions
    private Int2 mSpawnRange;

    // size of pixels of food: which is equal to a single block on the grid
    private int mSize;
    private Bitmap mBitmapFood;

    // todo: continue from here

    public Food(Context context, Int2 sr, int s) {
        mSpawnRange = sr;
        mSize = s; // Cell size.

        location.x = -10; // hide offscreen until game starts.

        // we can spawn food, and then move it around everytime player eats it.
        // todo: if needed, we can refactor this to spawn more and more instances of food over time in an arraylist. (complex)
        mBitmapFood =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.banana);
        // Resize the bitmap
        // todo: gridPosToAbsolute
        mBitmapFood =
                Bitmap.createScaledBitmap(mBitmapFood, s, s, false);
    }

    /**
     * spawn takes a list of all candidates and generates a new position for the food.
     * @param empty (ArrayList containing all empty spaces)
     * @param spawnRangeX (int, size of Grid)
     * @param spawnRangeY (int, size of Grid)
     * @return location New location of fruit
     */

    public static Int2 spawn(ArrayList<Int2> empty, int spawnRangeX, int spawnRangeY) {
        // Choose two random values, validate that its empty, then place the food
        Random random = new Random();

        int candidateX = random.nextInt(spawnRangeX) + 1;
        int candidateY = random.nextInt(spawnRangeY) + 1;

        // todo: while we haven't gotten a good candidate, keep running and find a new area to place the apple
        while (!(empty.contains(new Int2(candidateX, candidateY)))) {
            candidateX = random.nextInt(spawnRangeX) + 1;
            candidateY = random.nextInt(spawnRangeY) + 1;
        }
        // after exiting loop...
        location.x = candidateX;
        location.y = candidateY;

        return location;
    }

    // allow World to know where the food is
    public Int2 getLocation() {
        return location;
    }

    // the game objects will handle drawing themselves
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapFood,
                location.getX() * mSize, location.getY() * mSize, paint);
    }
}


package com.example.bomberkong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.bomberkong.R;

import java.util.Random;

public class Food {
    private Point location = new Point();

    // todo: this should check whether the cell itself is food, or something else before going on to spawn the fruit there
    // currently, this will hold the max values for horizontal / vert positions
    private Point mSpawnRange;

    // size of pixels of food: which is equal to a single block on the grid
    private int mSize;
    private Bitmap mBitmapFood;

    public Food(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;

        location.x = -10; // hide offscreen until game starts.

        // we can spawn food, and then move it around everytime player eats it.
        // todo: if needed, we can refactor this to spawn more and more instances of food over time in an arraylist. (complex)
        mBitmapFood =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.banana);
        // Resize the bitmap
        mBitmapFood =
                Bitmap.createScaledBitmap(mBitmapFood, s, s, false);

    }

    public void spawn() {
        // Choose two random values, validate that its empty, then place the food
        Random random = new Random();

        // todo: while we haven't gotten a good candidate, keep running and find a new area to place the apple
//        while (CELLSTATUS of candidateX, candidateY != empty){
//            int candidateX = random.nextInt(mSpawnRange.x) + 1;
//            int candidateY = random.nextInt(mSpawnRange.y) + 1;
//        }

        int candidateX = random.nextInt(mSpawnRange.x) + 1;
        int candidateY = random.nextInt(mSpawnRange.y) + 1;

        // after exiting loop...
        location.x = candidateX;
        location.y = candidateY;
    }

    // allow World to know where the food is

    Point getLocation() {
        return location;
    }

    // the game objects will handle drawing themselves
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapFood,
                location.x * mSize, location.y * mSize, paint);
    }
}

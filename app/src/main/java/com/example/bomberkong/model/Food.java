package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.bomberkong.util.Int2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.bomberkong.R;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.in;

public class Food {
    private Int2 location;

    // todo: this should check whether the cell itself is food, or something else before going on to spawn the fruit there
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
        mBitmapFood =
                Bitmap.createScaledBitmap(mBitmapFood, s, s, false);

    }

    /**
     * spawn takes a list of all candidates and generates a new position for the food.
     * @param empty (ArrayList containing all walls)
     * @return New location of fruit
     */

    public Int2 spawn(ArrayList<Int2> empty) {
        // Choose two random values, validate that its empty, then place the food
        Random random = new Random();

        int candidateX = random.nextInt(mSpawnRange.x) + 1;
        int candidateY = random.nextInt(mSpawnRange.y) + 1;

        // todo: while we haven't gotten a good candidate, keep running and find a new area to place the apple
        while boolean b = !(new Int2(candidateX, candidateY in empty)); {
            candidateX = random.nextInt(mSpawnRange.x) + 1;
            candidateY = random.nextInt(mSpawnRange.y) + 1;
        }
        // after exiting loop...
        location.x = candidateX;
        location.y = candidateY;

        return location;
    }

    // allow World to know where the food is
    Int2 getLocation() {
        return location;
    }

        // the game objects will handle drawing themselves
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawBitmap(mBitmapFood,
                    location.getX() * mSize, location.getY() * mSize, paint);
        }
    }

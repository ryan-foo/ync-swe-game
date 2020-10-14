package com.example.bomberkong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.example.bomberkong.util.Int2;
import com.example.bomberkong.R;

public class Player implements Cell
{

     private Point mMoveRange;

    // What direction is the player facing?
    private enum Heading {
        UP, DOWN, LEFT, RIGHT
    }

    private Heading heading = Heading.DOWN;

    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // Location of player on the Grid
    private Point mLocation = new Point();

    // these will be a function of the size of screen
    private float mPlayerWidth;
    private float mPlayerHeight;

    private boolean destroyable = true;
    private boolean collidable = true;
    private Int2 position;

    /**
     * Constructor for objects of class Player
     */
    public Player(Context context, Point mr, int ss) {
        mMoveRange = mr;

        // todo: take different resource based on Player Number

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.monkey);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.monkey);

        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.monkey);

        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.monkey);

        mBitmapHeadUp = Bitmap.createScaledBitmap(mBitmapHeadUp, ss, ss, false);
        mBitmapHeadDown = Bitmap.createScaledBitmap(mBitmapHeadDown, ss, ss, false);
        mBitmapHeadLeft = Bitmap.createScaledBitmap(mBitmapHeadLeft, ss, ss, false);
        mBitmapHeadRight = Bitmap.createScaledBitmap(mBitmapHeadRight, ss, ss, false);
    }

    void reset(int w, int h) {
        // Reset Heading
        heading = heading.DOWN;

        // todo: Reset starting point based on whether Player 1 or Player 2
        // Reset player position
        // Start in top left, or bottom right
    }

    // todo: movement needs to be conditional and check if the cell above is collidable or not.

    void move() {
        switch (heading) {
            case UP:
                mLocation.y -= 10;
                break;

            case RIGHT:
                mLocation.x += 10;
                break;

            case DOWN:
                mLocation.y += 10;
                break;

            case LEFT:
                mLocation.x -= 10;
                break;
        }
    }

    boolean detectDeath() {
        // has the player died?
        boolean dead = false;

        // todo: touches a fire, then player dies.
//        if (( player location.x, player location.y).CELLSTATUS = FIRE) {
//            dead = true;
//    }
        return dead;
    }

    boolean checkPickup(Point l) {
        // Check if the food has the same coordinates as Player.
        // if so, increment score etc.
        if (mLocation.x == l.x && mLocation.y == l.y) {
            return true;
        }

        return false;
    }

    void draw(Canvas canvas, Paint paint) {
        switch (heading) {
            case UP:
                canvas.drawBitmap(mBitmapHeadUp, mLocation.x, mLocation.y, paint);
                break;

            case DOWN:
                canvas.drawBitmap(mBitmapHeadDown, mLocation.x, mLocation.y, paint);
                break;

            case LEFT:
                canvas.drawBitmap(mBitmapHeadLeft, mLocation.x, mLocation.y, paint);
                break;

            case RIGHT:
                canvas.drawBitmap(mBitmapHeadRight, mLocation.x, mLocation.y, paint);
                break;
        }
    }

    // Handle changing direction given a movement
    // todo: have they pressed a button, d-pad instead of this movement scheme.
    // preliminary: if they tap somewhere above the location of their player, then they will move that way
    // todo: the logic here is pretty complex, but it should work as intended. might be worth refactoring.
    // as Prof Bruno explained:
    // if they tap somewhere below their player, they will move that way, etc.
    // if they tap in between, then it will try for the direction with the least distance.
    // todo: and finally, if they tap on the monkey itself, it will call the place bomb method
    void switchHeading(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        // todo: tapping on the monkey itself should call place bomb method. this should be the first if condition.
        // todo: you might both turn and place a bomb at the same time. that's pretty disastrous, we will need to fix this somehow.

        // todo: movement should also validate if the desired grid position is non-collidable!
        // todo: movement should be seamless (i.e, if we hold down, we should keep moving / turning, and we should control how many times in a frame a player can move.

        // consider between moving right or up or down depending on which is higher or lower displacement
        if (x >= mLocation.x) {
            // moving towards bottom right
            if (y >= mLocation.y && (x - mLocation.x > y - mLocation.y)) {
                heading = Heading.RIGHT;
            }

            // moving towards bottom right
            if (y >= mLocation.y && (y - mLocation.y > x - mLocation.x)) {
                heading = Heading.DOWN;
            }

            // moving towards top right
            if (mLocation.y >= y && (x - mLocation.x > y - mLocation.y)) {
                heading = Heading.UP;
            }

            // moving towards top right
            if (mLocation.y >= y && (y - mLocation.y > x - mLocation.x)) {
                heading = Heading.RIGHT;
            }
        }

        // then left
        // mLocation.x > x
        else {

            // moving towards bottom left
            if (y >= mLocation.y && (mLocation.x - x > y - mLocation.y)) {
                heading = Heading.LEFT;
            }

            // moving towards bottom left
            if (y >= mLocation.y && (y - mLocation.y > mLocation.x - x)) {
                heading = Heading.DOWN;
            }

            // moving towards top left
            if (mLocation.y >= y && (mLocation.x - x > y - mLocation.y)) {
                heading = Heading.LEFT;
            }

            // moving towards top left
            if (mLocation.y >= y && (y - mLocation.y > mLocation.x - x)) {
                heading = Heading.UP;
            }
        }

        // and then move after changing direction
        move();
    }

    /**
     * Returns whether player is destroyable
     */
    public boolean isDestroyable(){
        return this.destroyable;
    }

    public boolean isCollidable(){
        return this.collidable;
    }

    /**
     * Returns the position of the player
     */
    public Int2 getPosition(){
        return this.position;
    }

    /**
     * Moves the player up by one unit. Currently goes over walls.
     */
    public Grid moveUp(Grid grid){
        Int2 newpos = position.addReturn(new Int2(0, -1));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY)){
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveDown(Grid grid){
        Int2 newpos = position.addReturn(new Int2(0, 1));
        if (grid.getCellStatus(newpos) == CellStatus.EMPTY){
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveRight(Grid grid){
        Int2 newpos = position.addReturn(new Int2(1, 0));
        if (grid.getCellStatus(newpos) == CellStatus.EMPTY){
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveLeft(Grid grid){
        Int2 newpos = position.addReturn(new Int2(-1, 0));
        if (grid.getCellStatus(newpos) == CellStatus.EMPTY){
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid spawnBomb(Grid grid){
        Int2 spawnpos = position.addReturn(new Int2(0, 1));
        Bomb bomb = new Bomb (spawnpos);
        grid.setCell(spawnpos, CellStatus.BOMB);
        return grid;
    }
}


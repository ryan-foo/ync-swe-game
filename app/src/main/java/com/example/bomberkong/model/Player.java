package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.example.bomberkong.R;
import com.example.bomberkong.util.Int2;

public class Player implements Cell
{

     private Point mMoveRange;

    // What direction is the player facing?
    private enum Heading {
        UP, DOWN, LEFT, RIGHT
    }

    private Grid grid;
    private int playerNum;
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
    public Player(Context context, Grid grid, Int2 position, int playerNum, Int2 cellSize) {
        this.position = position;
        this.playerNum = playerNum;
        this.grid = grid;
        int cellWidth = cellSize.x;
        int cellHeight = cellSize.y;


        // todo: take different resource based on Player Number

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.oneup);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.onedown);

        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.oneleft);

        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.oneright);

        // todo: load different resource based on whether P1 or P2
        mBitmapHeadUp = Bitmap.createScaledBitmap(mBitmapHeadUp, cellWidth, cellHeight, false);
        mBitmapHeadDown = Bitmap.createScaledBitmap(mBitmapHeadDown, cellWidth, cellHeight, false);
        mBitmapHeadLeft = Bitmap.createScaledBitmap(mBitmapHeadLeft, cellWidth, cellHeight, false);
        mBitmapHeadRight = Bitmap.createScaledBitmap(mBitmapHeadRight, cellWidth, cellHeight, false);
    }

    public void reset(int w, int h) {
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
                moveUp(grid);
                break;

            case RIGHT:
                moveRight(grid);
                break;

            case DOWN:
                moveDown(grid);
                break;

            case LEFT:
                moveLeft(grid);
                break;
        }
    }

    public boolean detectDeath() {
        boolean dead = false;

        if (grid.getCellStatus(position) == CellStatus.FIRE) {
            dead = true;
        }

        return dead;
    }

    public boolean checkPickup(Int2 foodPosition) {
        // Check if the food has the same coordinates as Player.
        // if so, increment score etc.
        if (position.x == foodPosition.x && position.y == foodPosition.y) {
            return true;
        }

        return false;
    }


    // todo: gridToAbsolute, we should draw the bitmap based on the gridToAbsolute(position.x), gridToAbsolute(position.y)
    // how do I get the notion of scale, and cellSize into the render? look @ drawScaledBitmap method
    public void draw(Canvas canvas, Paint paint) {
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

    /**
     * Handles changing direction given a movement
     * @param motionEvent
     */

    // Handle changing direction given a movement
    // todo: have they pressed a button, d-pad instead of this movement scheme.
    // preliminary: if they tap somewhere above the location of their player, then they will move that way
    // todo: the logic here is pretty complex, but it should work as intended. might be worth refactoring.
    // as Prof Bruno explained:
    // if they tap somewhere below their player, they will move that way, etc.
    // if they tap in between, then it will try for the direction with the least distance.
    // todo: and finally, if they tap on the monkey itself, it will call the place bomb method
    public void switchHeading(MotionEvent motionEvent) {
        Log.d("switchHeading", "motion event recieved");
        float touch_x = motionEvent.getX();
        float touch_y = motionEvent.getY();

        /**
         * Grid positions
         * */

        Int2 gridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());

        int x = gridPosition.getX();
        int y = gridPosition.getY();

        // todo: tapping on the monkey itself should call place bomb method. this should be the first if condition.
        // todo: you might both turn and place a bomb at the same time. that's pretty disastrous, we will need to fix this somehow.

        // todo: movement should also validate if the desired grid position is non-collidable!
        // todo: movement should be seamless (i.e, if we hold down, we should keep moving / turning, and we should control how many times in a frame a player can move.

        // consider between moving right or up or down depending on which is higher or lower displacement

        if (x >= position.getX()) {
            // moving towards bottom right
            if (y >= position.getY() && (x - position.getX() > y - position.getY())) {
                heading = Heading.RIGHT;
            }

            // moving towards bottom right
            if (y >= position.getY() && (y - position.getY() > x - position.getX())) {
                heading = Heading.DOWN;
            }

            // moving towards top right
            if (position.getY() >= y && (x - position.getX() > y - position.getY())) {
                heading = Heading.UP;
            }

            // moving towards top right
            if (position.getY() >= y && (y - position.getY() > x - position.getX())) {
                heading = Heading.RIGHT;
            }
        }

        // then left
        // position.getX > x
        else {

            // moving towards bottom left
            if (y >= position.getY() && (position.getX() - x > y - position.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards bottom left
            if (y >= position.getY() && (y - position.getY() > position.getX() - x)) {
                heading = Heading.DOWN;
            }

            // moving towards top left
            if (position.getY() >= y && (position.getX() - x > y - position.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards top left
            if (position.getY() >= y && (y - position.getY() > position.getX() - x)) {
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
     * Moves the player up by one unit.
     * Each of these take a reference to the Grid to validate the movement.
     * Everytime we move, if it is a valid move, we set the cell
     */
    public Grid moveUp(Grid grid){
        Int2 newpos = position.addReturn(new Int2(0, -1));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveDown(Grid grid){
        Int2 newpos = position.addReturn(new Int2(0, 1));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveRight(Grid grid){
        Int2 newpos = position.addReturn(new Int2(1, 0));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            position = newpos;
        }
        grid.setCell(newpos, CellStatus.PLAYER);
        return grid;
    }

    public Grid moveLeft(Grid grid){
        Int2 newpos = position.addReturn(new Int2(-1, 0));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
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


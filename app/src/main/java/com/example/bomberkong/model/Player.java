package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.example.bomberkong.R;
import com.example.bomberkong.util.Int2;

import java.util.ArrayList;

public class Player implements Cell
{
    // What direction is the player facing?
    private enum Heading {
        UP, DOWN, LEFT, RIGHT
    }

    private int cellWidth;
    private int cellHeight;

    private Context context;
    private Grid grid;
    private int playerNum;
    private Heading heading = Heading.DOWN;

    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    private boolean destroyable = true;
    private boolean collidable = true;
    private Int2 gridPosition;

    /**
     * Constructor for objects of class Player
     */
    public Player(Context context, Grid grid, Int2 gridPosition, int playerNum, Int2 cellSize) {
        this.context = context;
        this.gridPosition = gridPosition;
        this.playerNum = playerNum;
        this.grid = grid;
        cellWidth = cellSize.x;
        cellHeight = cellSize.y;

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

        if (grid.getCellStatus(gridPosition) == CellStatus.FIRE) {
            dead = true;
        }

        return dead;
    }

    public boolean checkPickup(Int2 foodPosition) {
        // Check if the food has the same coordinates as Player.
        // if so, increment score etc.
        if (gridPosition.x == foodPosition.x && gridPosition.y == foodPosition.y) {
            return true;
        }
        return false;
    }


    // todo: gridToAbsolute, we should draw the bitmap based on the gridToAbsolute(position.x), gridToAbsolute(position.y)
    // how do I get the notion of scale, and cellSize into the render? look @ drawScaledBitmap method
    public void draw(Canvas canvas, Paint paint) {
        switch (heading) {
            case UP:
                canvas.drawBitmap(mBitmapHeadUp, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                break;

            case DOWN:
                canvas.drawBitmap(mBitmapHeadDown, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                break;

            case LEFT:
                canvas.drawBitmap(mBitmapHeadLeft, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                break;

            case RIGHT:
                canvas.drawBitmap(mBitmapHeadRight, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
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
        Log.d("switchHeading", "motion event received");
        float touch_x = motionEvent.getX();
        float touch_y = motionEvent.getY();

        /**
         * Grid positions
         * */

        // We need to handle the place bomb function here too -- if x > 3/4 of screen, then call place bomb, otherwise handle as per normal

        Int2 gridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());

        int x = gridPosition.getX();
        int y = gridPosition.getY();

        // todo: tapping on the monkey itself should call place bomb method. this should be the first if condition.
        // todo: you might both turn and place a bomb at the same time. that's pretty disastrous, we will need to fix this somehow.

        // todo: movement should also validate if the desired grid position is non-collidable!
        // todo: movement should be seamless (i.e, if we hold down, we should keep moving / turning, and we should control how many times in a frame a player can move.

        // consider between moving right or up or down depending on which is higher or lower displacement

        if (x >= this.gridPosition.getX()) {
            // moving towards bottom right
            if (y >= this.gridPosition.getY() && (x - this.gridPosition.getX() > y - this.gridPosition.getY())) {
                heading = Heading.RIGHT;
            }

            // moving towards bottom right
            if (y >= this.gridPosition.getY() && (y - this.gridPosition.getY() > x - this.gridPosition.getX())) {
                heading = Heading.DOWN;
            }

            // moving towards top right
            if (this.gridPosition.getY() >= y && (x - this.gridPosition.getX() > y - this.gridPosition.getY())) {
                heading = Heading.UP;
            }

            // moving towards top right
            if (this.gridPosition.getY() >= y && (y - this.gridPosition.getY() > x - this.gridPosition.getX())) {
                heading = Heading.RIGHT;
            }
        }

        else {
            // moving towards bottom left
            if (y >= this.gridPosition.getY() && (this.gridPosition.getX() - x > y - this.gridPosition.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards bottom left
            if (y >= this.gridPosition.getY() && (y - this.gridPosition.getY() > this.gridPosition.getX() - x)) {
                heading = Heading.DOWN;
            }

            // moving towards top left
            if (this.gridPosition.getY() >= y && (this.gridPosition.getX() - x > y - this.gridPosition.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards top left
            if (this.gridPosition.getY() >= y && (y - this.gridPosition.getY() > this.gridPosition.getX() - x)) {
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
    public Int2 getGridPosition(){
        return this.gridPosition;
    }

    /**
     * Moves the player up by one unit.
     * Each of these take a reference to the Grid to validate the movement.
     * Everytime we move, if it is a valid move, we set the cell
     */
    // todo: check if the player model "lingers" after changing position
    public Grid moveUp(Grid grid){
        Int2 newpos = gridPosition.addReturn(new Int2(0, -1));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            gridPosition = newpos;
            grid.setCell(newpos, CellStatus.PLAYER);
        }
        return grid;
    }

    public Grid moveDown(Grid grid){
        Int2 newpos = gridPosition.addReturn(new Int2(0, 1));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            gridPosition = newpos;
            grid.setCell(newpos, CellStatus.PLAYER);
        }
        return grid;
    }

    public Grid moveRight(Grid grid){
        Int2 newpos = gridPosition.addReturn(new Int2(1, 0));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            gridPosition = newpos;
            grid.setCell(newpos, CellStatus.PLAYER);
        }
        return grid;
    }

    public Grid moveLeft(Grid grid){
        Int2 newpos = gridPosition.addReturn(new Int2(-1, 0));
        if (grid.getCellStatus(newpos) == (CellStatus.EMPTY) ||
                grid.getCellStatus(newpos) == (CellStatus.FIRE) ||
                grid.getCellStatus(newpos) == (CellStatus.FOOD)
        )
        {
            gridPosition = newpos;
            grid.setCell(newpos, CellStatus.PLAYER);
        }
        return grid;
    }

    /**
     * spawnBomb takes context, grid, cellSize, and the current list of bombs on the map.
     * This list is maintained in World. When player clicks, they will call spawnBomb
     * @param context
     * @param grid
     * @param cellSize
     * @param bombList
     * @return
     */

    public ArrayList<Bomb> spawnBomb(Context context, Grid grid, Int2 cellSize, ArrayList<Bomb> bombList) {
        Int2 spawnpos = gridPosition; // current position of player
        // switch based on position you're facing
        switch (heading) {
            case UP:
                spawnpos = gridPosition.addReturn(new Int2(0, -1));
                break;

            case DOWN:
                spawnpos = gridPosition.addReturn(new Int2(0, 1));
                break;

            case LEFT:
                spawnpos = gridPosition.addReturn(new Int2(-1, 0));
                break;

            case RIGHT:
                spawnpos = gridPosition.addReturn(new Int2(1, 0));
                break;
        }

        if (grid.getCellStatus(spawnpos) == (CellStatus.EMPTY)) {
            bombList.add(new Bomb(context, spawnpos, cellSize));
        }
        grid.setCell(spawnpos, CellStatus.BOMB);
        return bombList;
    }
}

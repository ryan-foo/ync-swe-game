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

public class Player implements Cell
{
    // What direction is the player facing?
    private enum Heading {
        UP, DOWN, LEFT, RIGHT, NEUTRAL;
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
    private Bitmap mBitmapNeutral;

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
        mBitmapHeadUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneup);
        mBitmapHeadDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.onedown);
        mBitmapHeadLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneleft);
        mBitmapHeadRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneright);
        mBitmapNeutral = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey);

        // todo: load different resource based on whether P1 or P2
        mBitmapHeadUp = Bitmap.createScaledBitmap(mBitmapHeadUp, cellWidth, cellHeight, false);
        mBitmapHeadDown = Bitmap.createScaledBitmap(mBitmapHeadDown, cellWidth, cellHeight, false);
        mBitmapHeadLeft = Bitmap.createScaledBitmap(mBitmapHeadLeft, cellWidth, cellHeight, false);
        mBitmapHeadRight = Bitmap.createScaledBitmap(mBitmapHeadRight, cellWidth, cellHeight, false);
        mBitmapNeutral = Bitmap.createScaledBitmap(mBitmapNeutral, cellWidth, cellHeight, false);
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

            case NEUTRAL:
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

            case NEUTRAL:
                canvas.drawBitmap(mBitmapNeutral, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                break;
        }
    }

    /**
     * Handles changing direction given a movement
     * @param motionEvent
     */
    // todo: the logic here is pretty complex, but it should work as intended. might be worth refactoring.
    // todo: and finally, if they tap on the monkey itself, it will call the place bomb method
    public void switchHeading(MotionEvent motionEvent) {
<<<<<<< HEAD
=======
        Log.d("switchHeading", "motion event received");
>>>>>>> 0c3ffa75642234770eb829910743d451a0803aa8
        float touch_x = motionEvent.getX();
        float touch_y = motionEvent.getY();

        /**
         * Grid positions
         * */
        Int2 touchGridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());

<<<<<<< HEAD
        int touchGridPositionX = touchGridPosition.getX();
        int touchGridPositionY = touchGridPosition.getY();
=======
        // We need to handle the place bomb function here too -- if x > 3/4 of screen, then call place bomb, otherwise handle as per normal

        Int2 gridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());
>>>>>>> 0c3ffa75642234770eb829910743d451a0803aa8

        Log.d("currentpos", String.valueOf(gridPosition.x) + "," + String.valueOf(gridPosition.y));
        Log.d("touchpos", String.valueOf(touchGridPositionX) + "," + String.valueOf(touchGridPositionY));

        // todo: movement should also validate if the desired grid position is non-collidable!
        // todo: movement should be seamless (i.e, if we hold down, we should keep moving / turning, and we should control how many times in a frame a player can move.

        if (touchGridPositionX == this.gridPosition.getX() && touchGridPositionY == this.gridPosition.getY()){
            heading = Heading.NEUTRAL;
            // todo: tapping on the monkey itself should call place bomb method. this should be the first if condition.
        }

        // consider between moving right or up or down depending on which is higher or lower displacement
        else if (touchGridPositionX >= this.gridPosition.getX()) {
            // moving towards bottom right
            if (touchGridPositionY >= this.gridPosition.getY() && (touchGridPositionX - this.gridPosition.getX() > touchGridPositionY - this.gridPosition.getY())) {
                heading = Heading.RIGHT;
            }

            // moving towards bottom right
            else if (touchGridPositionY >= this.gridPosition.getY() && (touchGridPositionY - this.gridPosition.getY() > touchGridPositionX - this.gridPosition.getX())) {
                heading = Heading.DOWN;
            }

            // moving towards top right
            else if (this.gridPosition.getY() >= touchGridPositionY && (touchGridPositionX - this.gridPosition.getX() > touchGridPositionY - this.gridPosition.getY())) {
                heading = Heading.UP;
            }

            // moving towards top right
            else if (this.gridPosition.getY() >= touchGridPositionY && (touchGridPositionY - this.gridPosition.getY() > touchGridPositionX - this.gridPosition.getX())) {
                heading = Heading.RIGHT;
            }
        }

        else {
            // moving towards bottom left
            if (touchGridPositionY >= this.gridPosition.getY() && (this.gridPosition.getX() - touchGridPositionX > touchGridPositionY - this.gridPosition.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards bottom left
            else if (touchGridPositionY >= this.gridPosition.getY() && (touchGridPositionY - this.gridPosition.getY() > this.gridPosition.getX() - touchGridPositionX)) {
                heading = Heading.DOWN;
            }

            // moving towards top left
            else if (this.gridPosition.getY() >= touchGridPositionY && (this.gridPosition.getX() - touchGridPositionX > touchGridPositionY - this.gridPosition.getY())) {
                heading = Heading.LEFT;
            }

            // moving towards top left
            if (this.gridPosition.getY() >= touchGridPositionY && (touchGridPositionY - this.gridPosition.getY() > this.gridPosition.getX() - touchGridPositionX)) {
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

    public Grid spawnBomb(Grid grid){
        Int2 spawnpos = gridPosition;
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
            Bomb bomb = new Bomb(context, spawnpos);
        }
        grid.setCell(spawnpos, CellStatus.BOMB);
        return grid;
    }
}

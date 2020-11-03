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
    private final String playerNumControlled;
    private final int playerNum;

    // What direction is the player facing?
    private enum Heading {
        UP, DOWN, LEFT, RIGHT, NEUTRAL;
    }

    private int cellWidth;
    private int cellHeight;

    private Context context;
    private Grid grid;
    private Bomb bomb;
    private Int2 cellSize;
    private ArrayList<Bomb> bombList;
    private Heading heading = Heading.DOWN;

    private Bitmap mBitmapHeadRightOne;
    private Bitmap mBitmapHeadLeftOne;
    private Bitmap mBitmapHeadUpOne;
    private Bitmap mBitmapHeadDownOne;
    private Bitmap mBitmapHeadRightTwo;
    private Bitmap mBitmapHeadLeftTwo;
    private Bitmap mBitmapHeadUpTwo;
    private Bitmap mBitmapHeadDownTwo;
    private Bitmap mBitmapNeutral;

    private boolean destroyable = true;
    private boolean collidable = true;
    public Int2 gridPosition;

    /**
     * Constructor for objects of class Player
     */
    public Player(Context context, Grid grid, Int2 gridPosition, int playerNum, Int2 cellSize, ArrayList<Bomb> bombList, String playerNumControlled) {
        this.playerNum = playerNum;
        this.playerNumControlled = playerNumControlled;
        this.context = context;
        this.gridPosition = gridPosition;
        this.grid = grid;
        this.cellSize = cellSize;
        this.bombList = bombList;
        cellWidth = cellSize.x;
        cellHeight = cellSize.y;

        grid.setCell(gridPosition, CellStatus.PLAYER);

        // todo: take different resource based on Player Number
        mBitmapHeadUpOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneup);
        mBitmapHeadDownOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.onedown);
        mBitmapHeadLeftOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneleft);
        mBitmapHeadRightOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.oneright);
        mBitmapHeadUpTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.twoup);
        mBitmapHeadDownTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.twodown);
        mBitmapHeadLeftTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.twoleft);
        mBitmapHeadRightTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.tworight);
        mBitmapNeutral = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey);

        // todo: load different resource based on whether P1 or P2
        mBitmapHeadUpOne = Bitmap.createScaledBitmap(mBitmapHeadUpOne, cellWidth, cellHeight, false);
        mBitmapHeadDownOne = Bitmap.createScaledBitmap(mBitmapHeadDownOne, cellWidth, cellHeight, false);
        mBitmapHeadLeftOne = Bitmap.createScaledBitmap(mBitmapHeadLeftOne, cellWidth, cellHeight, false);
        mBitmapHeadRightOne = Bitmap.createScaledBitmap(mBitmapHeadRightOne, cellWidth, cellHeight, false);
        mBitmapHeadUpTwo = Bitmap.createScaledBitmap(mBitmapHeadUpTwo, cellWidth, cellHeight, false);
        mBitmapHeadDownTwo = Bitmap.createScaledBitmap(mBitmapHeadDownTwo, cellWidth, cellHeight, false);
        mBitmapHeadLeftTwo = Bitmap.createScaledBitmap(mBitmapHeadLeftTwo, cellWidth, cellHeight, false);
        mBitmapHeadRightTwo = Bitmap.createScaledBitmap(mBitmapHeadRightTwo, cellWidth, cellHeight, false);
        mBitmapNeutral = Bitmap.createScaledBitmap(mBitmapNeutral, cellWidth, cellHeight, false);
    }

    public void reset(int w, int h) {
        // Reset Heading
        heading = heading.DOWN;
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
                if (playerNum == 1){
                    canvas.drawBitmap(mBitmapHeadUpOne, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                } else {
                    canvas.drawBitmap(mBitmapHeadUpTwo, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                }
                break;

            case DOWN:
                if (playerNum == 1){
                    canvas.drawBitmap(mBitmapHeadDownOne, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                } else {
                    canvas.drawBitmap(mBitmapHeadDownTwo, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                }
                break;

            case LEFT:
                if (playerNum == 1){
                    canvas.drawBitmap(mBitmapHeadLeftOne, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                } else {
                    canvas.drawBitmap(mBitmapHeadLeftTwo, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                }
                break;

            case RIGHT:
                if (playerNum == 1){
                    canvas.drawBitmap(mBitmapHeadRightOne, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                } else {
                    canvas.drawBitmap(mBitmapHeadRightTwo, gridPosition.x * cellWidth, gridPosition.y * cellHeight, paint);
                }
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
    public ArrayList<Bomb> switchHeading(MotionEvent motionEvent) {
        float touch_x = motionEvent.getX();
        float touch_y = motionEvent.getY();

        /**
         * Grid positions
         * */
        Int2 touchGridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());

        int touchGridPositionX = touchGridPosition.getX();
        int touchGridPositionY = touchGridPosition.getY();
        // We need to handle the place bomb function here too -- if x > 3/4 of screen, then call place bomb, otherwise handle as per normal

        Int2 gridPosition = grid.absoluteToGridPos(touch_x, touch_y, grid.getNumCellsWide(), grid.getNumCellsHigh(), grid.getActualViewWidth(), grid.getActualViewHigh());

        // todo: movement should also validate if the desired grid position is non-collidable!
        // todo: movement should be seamless (i.e, if we hold down, we should keep moving / turning, and we should control how many times in a frame a player can move.
        if (touchGridPositionX == this.gridPosition.getX() && touchGridPositionY == this.gridPosition.getY()){
            bombList = spawnBomb(context, grid, cellSize, bombList);
            heading = Heading.NEUTRAL;
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
        return bombList;
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
            grid.setCell(gridPosition, CellStatus.EMPTY);
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
            grid.setCell(gridPosition, CellStatus.EMPTY);
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
            grid.setCell(gridPosition, CellStatus.EMPTY);
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
            grid.setCell(gridPosition, CellStatus.EMPTY);
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
     * @return bombList
     */

    public ArrayList<Bomb> spawnBomb(Context context, Grid grid, Int2 cellSize, ArrayList<Bomb> bombList) {
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
            bomb = new Bomb(context, spawnpos, cellSize);
            bombList.add(bomb);
            grid.setCell(spawnpos, CellStatus.BOMB);
        }
        return bombList;
    }
}

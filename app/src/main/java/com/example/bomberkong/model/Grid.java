package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.bomberkong.R;
import com.example.bomberkong.util.Int2;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Grid
{
    private Map<Int2, CellStatus> gridMap;
    private int numCellsWide;
    private int numCellsHigh;
    private int actualViewWidth;
    private int actualViewHigh;
    private Bitmap mBitmapWall;
    private Bitmap mBitmapEmpty;
    private Bitmap mBitmapBomb;
    private Bitmap mBitmapFire;

    public Grid(Context context, int numCellsWide, int numCellsHigh, int actualViewWidth, int actualViewHeight){
        this.numCellsWide = numCellsWide; // width of the grid in grid blocks
        this.numCellsHigh = numCellsHigh; // height of the grid in grid blocks
        this.actualViewWidth = actualViewWidth; // width of the grid in absolute x
        this.actualViewHigh = actualViewHeight ; // width of the grid in absolute y
        this.gridMap = new HashMap<Int2, CellStatus>();
        this.mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        this.mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, actualViewWidth/numCellsWide, actualViewHeight/numCellsHigh, false);
        this.mBitmapEmpty = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
        this.mBitmapEmpty = Bitmap.createScaledBitmap(mBitmapEmpty, actualViewWidth/numCellsWide, actualViewHeight/numCellsHigh, false);
        this.mBitmapWall = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        this.mBitmapWall = Bitmap.createScaledBitmap(mBitmapWall, actualViewWidth/numCellsWide, actualViewHeight/numCellsHigh, false);
        this.mBitmapFire = BitmapFactory.decodeResource(context.getResources(), R.drawable.fire);
        this.mBitmapFire = Bitmap.createScaledBitmap(mBitmapFire, actualViewWidth/numCellsWide, actualViewHeight/numCellsHigh, false);
        reset();
    }

    public int getNumCellsWide(){
        return numCellsWide;
    }
    public int getNumCellsHigh(){
        return numCellsHigh;
    }
    public int getActualViewWidth(){
        return actualViewWidth;
    }
    public int getActualViewHigh(){
        return actualViewHigh;
    }

    public void setCell(Int2 pos, CellStatus status) {
        gridMap.put(pos, status);
    }

    public CellStatus getCellStatus(Int2 pos) {
        return gridMap.get(pos);
    }

    /**
     * The draw method in Grid is responsible for drawing both the Grid (the lines), empty cells, fire and bombs. All other objects
     * draw themselves, and are called in World. This is because Grid contains overarching information about the game model.
     * @param canvas
     */

    public void draw(Canvas canvas) {
        if (canvas == null) return;

        canvas.drawARGB(255, 255, 255, 255);
        Paint gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(10);
        gridPaint.setARGB(135, 0, 0, 0);

        int xcount = getNumCellsWide();
        int ycount = getNumCellsHigh();

        // drawing objects first

        for (int nx = 0; nx < xcount; nx++) {
            for (int ny = 0; ny < ycount; ny++) {

                float xpos1 = nx * canvas.getWidth() / xcount;
                float ypos1 = ny * canvas.getHeight() / ycount;
                Int2 pos = new Int2(nx, ny);
                CellStatus status = getCellStatus(pos);

                switch (status) {
                    case EMPTY:
                        canvas.drawBitmap(mBitmapEmpty, xpos1, ypos1, null);
                        break;
                    case BOMB:
                        canvas.drawBitmap(mBitmapBomb, xpos1, ypos1, null);
                        break;
                    case FIRE:
                        canvas.drawBitmap(mBitmapFire, xpos1, ypos1, null);
                        break;
                    case WALL:
                        canvas.drawBitmap(mBitmapWall, xpos1, ypos1, null);
                        break;
                }
            }
        }

        // Drawing the lines
        for (int n = 0; n < xcount; n++) {
            float xpos = n * canvas.getWidth() / xcount;
            canvas.drawLine(canvas.getWidth() - xpos, 0, canvas.getWidth() - xpos, canvas.getHeight(), gridPaint);
        }
        for (int n = 0; n < ycount; n++) {
            float ypos = n * canvas.getHeight() / ycount;
            canvas.drawLine(canvas.getWidth() - 0, ypos, canvas.getWidth() - canvas.getWidth(), ypos, gridPaint);
        }

    }


    /**
     * This will be called by Food to determine all the possible candidates for Food to spawn in.
     * @return ArrayList<Int2> emptyCells
     */
    public ArrayList<Int2> getEmpty() {
        ArrayList<Int2> emptyCells = new ArrayList<Int2>();
        for (int x = 0; x < numCellsWide; x++) {
            for (int y = 0; y < numCellsHigh; y++) {
                CellStatus status = getCellStatus(new Int2(x, y));
                if (status == CellStatus.EMPTY) {
                    emptyCells.add(new Int2(x, y));
                }
            }
        }
        return emptyCells;
    }

    /**
     * absoluteToGridPos takes a position and returns its location on a grid.
     * @param absX Absolute value of X (screen position in pixels)
     * @param absY Absolute value of Y (screen position in pixels)
     * @param xCount How many cells in grid?
     * @param yCount How many cells in grid?
     * @param gridWidth Width of the grid in pixels
     * @param gridHeight Height of the grid in pixels
     * @return Int2 xGrid and yGrid position
     */
    public Int2 absoluteToGridPos(float absX, float absY, int xCount, int yCount, int gridWidth, int gridHeight) {
        // number of cells width wise
        // number of cells height wise
        int xGrid = -1;
        int yGrid = -1;

        for (int nx = 0; nx < xCount; nx++) {
            for (int ny = 0; ny < yCount; ny++) {

                float xpos1 = nx * gridWidth / xCount;
                float ypos1 = ny * gridHeight / yCount;
                float xpos2 = (nx + 1) * gridWidth / xCount;
                float ypos2 = (ny + 1) * gridHeight / yCount;

                // We have found the x pos on the grid
                if (xpos1 < absX && absX < xpos2) {
                    xGrid = nx;
                }

                // We have found the y pos on the grid
                if (ypos1 < absY && absY < ypos2) {
                    yGrid = ny;
                }
            }
        }

        return new Int2(xGrid, yGrid);
    }

    /**
     * Sets the walls to borders.
     * The walls are being set in Grid.
     */

    public void reset() {
        for (int x = 0; x < getNumCellsWide(); x ++){
            for (int y = 0; y < getNumCellsHigh(); y ++){
                this.setCell(new Int2 (x, y), CellStatus.EMPTY);
            }
        }
        //setting borders
        for (int x = 0; x < getNumCellsWide(); x ++){
            this.setCell(new Int2 (x, 0), CellStatus.WALL);
            this.setCell(new Int2 (x, getNumCellsHigh() - 1), CellStatus.WALL);
        }
        for (int y = 1; y < getNumCellsHigh(); y ++) {
            this.setCell(new Int2 (0, y), CellStatus.WALL);
            this.setCell(new Int2 (getNumCellsWide() - 1, y), CellStatus.WALL);
        }

        //setting in-game walls
        int[] yList = {2, 7};
        int[] xList = {4, 7, 8, 11, 12, 15};
        for (int y : yList){
            for (int x : xList){
                this.setCell(new Int2 (x, y), CellStatus.WALL);
            }
        }

        int[] yListTwo = {3, 6};
        int[] xListTwo = {3, 16};
        for (int y : yListTwo){
            for (int x : xListTwo){
                this.setCell(new Int2 (x, y), CellStatus.WALL);
            }
        }
    }
}

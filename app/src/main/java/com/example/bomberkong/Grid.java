package com.example.bomberkong;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.bomberkong.util.Int2;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Grid
{
    private Map<Int2, CellStatus> gridMap;
    private int w;
    private int h;
    private int x;
    private int y;

    public Grid(int w, int h, int x, int y){
        this.w = w; // width of the grid in grid blocks
        this.h = h; // height of the grid in grid blocks
        this.x = x; // width of the grid in absolute x
        this.y = y; // width of the grid in absolute y
        this.gridMap = new HashMap<Int2, CellStatus>();
        reset();
    }

    public Map getMap(){
        return gridMap;
    }

    public int getW(){
        return w;
    }
    public int getH(){
        return h;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setCell(Int2 pos, CellStatus status) {
        gridMap.put(pos, status);
    }

    public CellStatus getCellStatus(Int2 pos) {
        return gridMap.get(pos);
    }

    /**
     * The draw method in Grid is responsible for drawing both the Grid, empty cells and the walls. All other objects
     * draw themselves, and are called in World. This is because Grid contains overarching information about the game model / walls.
     * @param canvas
     * @param paint
     */

    void draw(Canvas canvas, Paint paint) {
        if (canvas == null) return;

        canvas.drawARGB(255, 255, 255, 255);

        Paint gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(10);
        gridPaint.setARGB(135, 0, 0, 0);

        int xcount = getW();
        int ycount = getH();

        for (int n = 0; n < xcount; n++) {
            float xpos = n * canvas.getWidth() / xcount;
            canvas.drawLine(canvas.getWidth() - xpos, 0, canvas.getWidth() - xpos, canvas.getHeight(), gridPaint);
        }
        // Can you draw inside the grid? Draw BitMap
        // If CellStatus BitMap
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
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
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
        for (int x = 0; x < getW(); x ++){
            for (int y = 0; y < getH(); y ++){
                this.setCell(new Int2 (x, y), CellStatus.EMPTY);
            }
        }
        for(int x = 0; x < getW(); x ++){
            this.setCell(new Int2 (x, 0), CellStatus.WALL);
            this.setCell(new Int2 (x, getH() - 1), CellStatus.WALL);
        }
        for (int y = 0; y < getH(); y ++) {
            this.setCell(new Int2 (0, y), CellStatus.WALL);
            this.setCell(new Int2 (getW() - 1, y), CellStatus.WALL);
        }
    }
}

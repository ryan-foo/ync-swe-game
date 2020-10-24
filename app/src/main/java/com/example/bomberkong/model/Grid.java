package com.example.bomberkong.model;

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
    private Callback callback = null;


    public Grid(int w, int h, int x, int y){
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
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

    public CellStatus getCellStatus(Int2 pos){
        return gridMap.get(pos);
    }

    public String getCellString(Int2 pos){
        CellStatus temp = gridMap.get(pos);
        if (temp == CellStatus.WALL){
            return "WALL";
        } else if (temp == CellStatus.PLAYER){
            return "PLAYER";
        } else if (temp == CellStatus.EMPTY){
            return "EMPTY";
        } else {
            return "NULL";
        }
    }

    // Callback when grid changes
    public interface Callback {void gridChanged ( Grid grid ) ; }
    public void setCallBack(Callback c) { callback = c; }
    public void addCallBack (Callback c )  { this.callback = c; }

    /**
     * This function is used by the Grid to callback
     * its observer (GridRenderer so far)
     */
    protected void castChanges() {
        if (callback != null) {callback.gridChanged(this);}
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

    Int2 absoluteToGridPos(float absX, float absY, int xCount, int yCount, int gridWidth, int gridHeight) {

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

    public void reset(){
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

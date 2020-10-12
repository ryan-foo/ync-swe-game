package com.example.bomberkong.model;

import com.example.bomberkong.util.Int2;

import java.util.Map;
import java.util.HashMap;

public class Grid
{
    private Map<Int2, CellStatus> gridMap;
    private int w;
    private int h;

    public Grid(int w, int h){
        this.w = w;
        this.h = h;
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

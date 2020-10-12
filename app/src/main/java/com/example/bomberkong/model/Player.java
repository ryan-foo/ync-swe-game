package com.example.bomberkong.model;

import com.example.bomberkong.util.Int2;

public class Player implements Cell
{
    private boolean destroyable = true;
    private boolean collidable = true;
    private Int2 position;

    /**
     * Constructor for objects of class Player
     */
    public Player(Int2 position){
        this.position = position;
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


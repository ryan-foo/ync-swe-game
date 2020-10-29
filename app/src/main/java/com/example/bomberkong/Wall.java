package com.example.bomberkong;

import com.example.bomberkong.util.Int2;

public class Wall implements Cell
{
    private boolean destroyable = false;
    private boolean collidable = true;
    private Int2 position;

    //Constructor class that take and Int2 position
    public Wall(Int2 position){
        this.position = position;
    }

    //Returns whether the cell is destroyable
    public boolean isDestroyable(){
        return this.destroyable;
    }

    public boolean isCollidable(){
        return this.collidable;
    }
    //Returns the Int2 position of the player
    public Int2 getPosition(){
        return this.position;
    }
}


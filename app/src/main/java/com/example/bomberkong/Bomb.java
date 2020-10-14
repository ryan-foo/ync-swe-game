package com.example.bomberkong;

import com.example.bomberkong.util.Int2;

public class Bomb implements Cell
{
    private boolean destroyable = true;
    private boolean collidable = false;
    private Int2 position;

    /**
     * Constructor for objects of class Fire
     */
    public Bomb(Int2 position){
        this.position = position;
    }

    /**
     * Returns whether fire is destroyable
     */
    public boolean isDestroyable(){
        return this.destroyable;
    }

    public boolean isCollidable(){
        return this.collidable;
    }

    /**
     * Returns the position of the fire
     */
    public Int2 getPosition(){
        return this.position;
    }

    public void Explode(Grid grid){

    }
}

package com.example.bomberkong.model;

import android.content.Context;

import com.example.bomberkong.util.Int2;

public class Bomb implements Cell
{
    private boolean destroyable = true;
    private boolean collidable = false;
    private Int2 position;
    private int ticksToExplode = 30; // our current game is 10 fps, so we will have 3 seconds before it explodes.

    /**
     * Constructor for objects of class Fire
     */
    public Bomb(Context context, Int2 position){
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

    public Int2 getGridPosition(){
        return this.position;
    }

    public void tickDown() {
        ticksToExplode -= 1;
    }

    /**
     * Returns the position of the fire
     */

    public void explode(Grid grid){

    }
}

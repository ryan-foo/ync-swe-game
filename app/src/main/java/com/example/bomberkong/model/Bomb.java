package com.example.bomberkong.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.bomberkong.R;
import com.example.bomberkong.World;
import com.example.bomberkong.util.Int2;

import java.util.ArrayList;

public class Bomb implements Cell
{
    public static Int2 position;
    private final Context context;
    private int cellWidth;
    private int cellHeight;
    private Int2 cellSize;
    private Bitmap mBitmapBomb;
    public int ticksToExplode = 30; // our current game is 10 fps, so we will have 3 seconds before it explodes.

    // todo: continue from here

    public Bomb(Context context, Int2 position, Int2 cellSize) {
        this.context = context;
        this.position = position;
        this.cellSize = cellSize;
        cellWidth = cellSize.x;
        cellHeight = cellSize.y;

        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, cellWidth, cellHeight, false);
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public Int2 getGridPosition(){
        return this.position;
    }

    public void explode(World world, ArrayList<Fire> fireList){
        Grid grid = world.getGrid();
        // todo: create a neighbors function, change all neighbors that qualify into fire
        grid.setCell(getGridPosition(), CellStatus.EMPTY);

        // Getting the CellStatus of surroundings
        Int2 posLeft = position.addReturn(new Int2(-1, 0));
        Int2 posRight = position.addReturn(new Int2(1, 0));
        Int2 posUp = position.addReturn(new Int2(0, -1));
        Int2 posDown = position.addReturn(new Int2(0, 1));
        CellStatus left = grid.getCellStatus(posLeft);
        CellStatus right = grid.getCellStatus(posRight);
        CellStatus up = grid.getCellStatus(posUp);
        CellStatus down = grid.getCellStatus(posDown);

        if (left == CellStatus.EMPTY || left == CellStatus.PLAYER){
            Fire fire = new Fire(context, grid,  posLeft, cellSize);
            fireList.add(fire);
            grid.setCell(posLeft, CellStatus.FIRE);
        }
        if (right == CellStatus.EMPTY || right == CellStatus.PLAYER){
            grid.setCell(posRight, CellStatus.FIRE);
            Fire fire = new Fire(context, grid, posRight, cellSize);
            fireList.add(fire);
        }
        if (up == CellStatus.EMPTY || up == CellStatus.PLAYER){
            grid.setCell(posUp, CellStatus.FIRE);
            Fire fire = new Fire(context, grid, posUp, cellSize);
            fireList.add(fire);
        }
        if (down == CellStatus.EMPTY || down == CellStatus.PLAYER){
            grid.setCell(posDown, CellStatus.FIRE);
            Fire fire = new Fire(context, grid, posDown, cellSize);
            fireList.add(fire);
        }
    }
}

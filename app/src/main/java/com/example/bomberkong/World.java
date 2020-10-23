package com.example.bomberkong;

import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;
import com.example.bomberkong.util.Int2;

public class World
{
    private Grid grid;
    private Player player1;

    public World(){
        grid = new Grid(20, 10);
        player1 = new Player(new Int2(2, 2));
        grid.setCell(player1.getPosition(), CellStatus.PLAYER);
    }

    public Grid returnGrid() {
        return this.grid;
    }

    public void update(){
        this.grid.reset();
        this.grid.setCell(player1.getPosition(), CellStatus.PLAYER);
    }
}


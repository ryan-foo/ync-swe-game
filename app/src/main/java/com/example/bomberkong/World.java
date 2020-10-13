package com.example.bomberkong;


import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;
import com.example.bomberkong.util.Int2;

public class World
{
    private final boolean DEBUGGING = true;

    // Objects for drawing

    private Grid grid;
    private Player player;
    public World(){
        grid = new Grid(5, 5);
        player = new Player(new Int2(2, 2));
    }

    public Grid returnGrid() {
        return this.grid;
    }

    public void createGame(){
        this.grid.reset();
        this.grid.setCell(player.getPosition(), CellStatus.PLAYER);
        generateGame();
    }

    public void generateGame(){
        for (int y = 0; y < grid.getH(); y ++){
            for (int x = 0; x < grid.getW(); x ++){
                CellStatus cellstatus = grid.getCellStatus(new Int2(x, y));
                if (cellstatus == CellStatus.WALL){
                    System.out.print("_ ");
                } else if (cellstatus == CellStatus.PLAYER){
                    System.out.print("P ");
                } else if (cellstatus == CellStatus.BOMB){
                    System.out.print("B ");
                }else{
                    System.out.print("..");
                }
            }
            System.out.println();
        }
    }

    public void movePlayer(String dir){
        if (dir == "w"){
            grid = player.moveUp(grid);
        } else if (dir == "s"){
            grid = player.moveDown(grid);
        } else if (dir == "a"){
            grid = player.moveLeft(grid);
        } else if (dir == "d"){
            grid = player.moveRight(grid);
        }
        update();
    }

    public void update(){
        this.grid.reset();
        this.grid.setCell(player.getPosition(), CellStatus.PLAYER);
        generateGame();
    }

    public void bomb(){
        this.grid = player.spawnBomb(grid);
        generateGame();
    }
}


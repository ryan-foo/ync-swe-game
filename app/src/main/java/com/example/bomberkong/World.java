package com.example.bomberkong;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;
import com.example.bomberkong.util.Int2;

public class World extends SurfaceView implements Runnable
{
    private final boolean DEBUGGING = true;

    // Objects for drawing
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    // For smooth movement
    private long mFPS;
    private final int MILLI_IN_SECONDS = 1000;

    // These should be passed into World from the constructor
    public int mScreenX; // Vertical size of the screen
    public int mScreenY; // Horizontal size of the screen

    // These will be initialized based on screen size in pixels
    public int mFontSize;
    public int mFontMargin;

    public int mScoreP1 = 0;
    public int mScoreP2 = 0;

    private Grid grid;
    private Player player;

    // Threads and control variables
    private Thread mGameThread = null;

    // volatile can be accessed from outside and inside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    public World(Context context, int x, int y){
        super(context);

        grid = new Grid(5, 5);
        player = new Player(new Int2(2, 2));

        // Initialize with values passed in as params
        mScreenX = x;
        mScreenY = y;

        // 5% of width
        mFontSize = mScreenX / 20;
        // 1.5% of width
        mFontMargin = mScreenX / 75;

        // Initialize objects for drawing
        mHolder = getHolder();
        mPaint = new Paint();

        startNewGame();
    }

    // When we start the thread with:
    // mGameThread.start();
    // the run method is continuously called by Android // because we implemented the Runnable interface
    // Calling mGameThread.join();
    // will stop the thread
    @Override
    public void run() {
        while (mPlaying) {
            // What time is it at the start of the game loop?
            long frameStartTime = System.currentTimeMillis();

            // if game isn't paused, update
            if (!mPaused) {
                update();
                // Todo: detectCollisions (to 'kill player', or to 'destroy wall')
            }

            // after update, we can draw
            draw();

            // How long was this frame?
            long timeThisFrame =
                    System.currentTimeMillis() - frameStartTime;

            // Dividing by 0 will crash game
            if (timeThisFrame > 0) {
                // Store the current frame in mFPS and pass it to
                // update methods of the grid next frame/loop
                mFPS = MILLI_IN_SECONDS / timeThisFrame;

            // we will use mSpeed / mFPS to determine how fast players move on the screen
            }

        }
    }

    public void startNewGame(){
        this.grid.reset();
        // Todo: reset to player original positions depending on player number
        this.grid.setCell(player.getPosition(), CellStatus.PLAYER);

        // Reset score
        mScoreP1 = 0;
        mScoreP2 = 0;
        generateGame();
    }

    void draw() {
            if (mHolder.getSurface().isValid()) {
                // Lock Canvas to draw
                mCanvas = mHolder.lockCanvas();

                // Fill screen with solid colour, Todo: replace with grid drawing
                mCanvas.drawColor(Color.argb(255, 26, 128, 182));

                // Color to paint with
                mPaint.setColor(Color.argb(255, 255, 255, 255));

                // Draw Grid

                // Draw player, bombs, fire, walls

                // Choose font size
                mPaint.setTextSize(mFontSize);

                // Draw HUD
                mCanvas.drawText("Score P1: " + mScoreP1 + "Score P2: " + mScoreP2,
                         mFontMargin, mFontSize, mPaint);

                if(DEBUGGING) {
                    printDebuggingText();
                }

                // Display drawing on screen
                // unlockCanvasAndPost is a method of SurfaceView
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    public void update(){
        this.grid.reset();
        this.grid.setCell(player.getPosition(), CellStatus.PLAYER);
        generateGame();
    }



    //  Debugs, shows current FPS, we can show other things too
    private void printDebuggingText() {
        int debugSize = mFontSize / 2;
        int debugStart = 150; // just a guess at where is a good position vertically to place the text
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS,
                10, debugStart + debugSize, mPaint);
    }

    public Grid returnGrid() {
        return this.grid;
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

    public void bomb() {
        this.grid = player.spawnBomb(grid);
        generateGame();
    }

    public void pause() {
        mPlaying = false;
        try {
            // stop thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    private void detectCollisions() {
        // has player come into contact with fire? (if so, kill him!)

        // has the player come into contact with a banana? (if so, increase points and destroy the banana)

        // has player hit a grid he is not allowed to enter

    }

}


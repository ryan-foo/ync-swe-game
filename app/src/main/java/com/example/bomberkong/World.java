package com.example.bomberkong;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberkong.util.Int2;

import java.io.IOException;

// credits for framework: John Horton

public class World extends SurfaceView implements Runnable
{
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
    private Food food;

    // Sound
    private SoundPool mSP;
    private int mSpawn_ID = -1; // sound when fruit is spawned
    private int mEat_ID = -1; // sound when fruit is picked
    private int mBombID = -1; // sound when bomb explodes
    private int mDeathID = -1; // sound when player dies

    // Size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 20;
    private int mNumBlocksHigh;

    // Threads and control variables
    private Thread mGameThread = null;

    // volatile can be accessed from outside and inside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    private long mNextFrameTime;

    public World(Context context, int x, int y){
        super(context);

        // work out how many pixels each block in the grid is
        int blockSize = x / NUM_BLOCKS_WIDE;
        // How many blocks of same size will fit into height
        // Todo: This cannot be dynamic -- it has to be static since the grid itself is shared b/w two phones.
        // Todo: What needs to be done is to determine the _aspect ratio_ and then talk about the block height accordingly
        mNumBlocksHigh = y / blockSize;

        // initialize SoundPool
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes =
                    new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes
                    .CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare sounds in memory
            // pickup food sound
            descriptor = assetManager.openFd("get_food.ogg");
            mEat_ID = mSP.load(descriptor, 0);
            // bomb explode sound
            descriptor = assetManager.openFd("bomb.ogg");
            mBombID =  mSP.load(descriptor, 0);
            //death sound
            descriptor = assetManager.openFd("chimchar.ogg");
            mDeathID = mSP.load(descriptor, 0);
        } catch (IOException e) {
            // Error
        }

        // todo: grid should take blocksize
        grid = new Grid(5, 5);

        food = new Food(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
        blockSize);

        player = new Player(context,
        new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),
        blockSize);

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

            // if game isn't paused, update 10 times a second
            if (!mPaused) {
                if (updateRequired()) {
                    update();
                }
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
        // reset grid
        this.grid.reset();
        // Todo: reset to player original positions depending on player number
        player.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // banana should be spawned
        food.spawn();

        // Reset score
        mScoreP1 = 0;
        mScoreP2 = 0;

        // setup next frame time
        mNextFrameTime = System.currentTimeMillis();

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

            // Draw food, player
            food.draw(mCanvas, mPaint);
            player.draw(mCanvas, mPaint);

            // Draw bombs, fire, walls

            // Choose font size
            mPaint.setTextSize(mFontSize);

            // Draw score
            mCanvas.drawText("Score P1: " + mScoreP1 + " Score P2: " + mScoreP2,
                    mFontMargin, mFontSize, mPaint);

            if (mPaused) {
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(250);

                mCanvas.drawText("Tap to begin!", 200, 700, mPaint);
            }

            // Display drawing on screen
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void update() {
//        this.grid.reset();
//        this.grid.setCell(player.getPosition(), CellStatus.PLAYER);

        // todo: Move player and update Grid if there is input
        // todo: also, have a cooldown based on FPS how many fast a player can move... (use
        

        // Did player eat the food?
        if (player.checkPickup(food.getLocation())) {
            // todo: multi-player. for now, all pickups will be given to player 1.
            food.spawn();
            mScoreP1 = mScoreP1 + 1;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        // Did player die? (todo: player cannot die yet.)
        if (player.detectDeath()) {
            mSP.play(mDeathID, 1, 1, 0, 0, 1);
        }


        generateGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    startNewGame();
                    return true;
                }

                player.switchHeading(motionEvent);
                break;
            default:
                break;
        }
        return true;
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

    // this creates the blocky movement we desire
    public boolean updateRequired() {
        // Run at 10 fps
        final long TARGET_FPS = 10;
        // 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // are we due to update the frame?
        if (mNextFrameTime <= System.currentTimeMillis()) {
            // 1 tenth of a second has passed
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;
            return true;
        }
        return false;

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


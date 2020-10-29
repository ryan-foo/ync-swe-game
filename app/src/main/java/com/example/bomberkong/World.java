package com.example.bomberkong;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Food;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;
import com.example.bomberkong.util.Int2;

import java.io.IOException;
import java.util.ArrayList;

// credits for framework: John Horton

public class World extends SurfaceView implements Runnable
{
    private static final String TAG = "World";
    // Objects for drawing
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint scorePaint;

    // Instances of objects that will last throughout
    private Grid grid;
    private Player playerOne;
    private Player playerTwo;
    private Food food;
    private int actualViewWidth;
    private int actualViewHeight;
    private int numCellsWide;
    private int numCellsHigh;

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

    // Sound
    private SoundPool mSP;
    private int mSpawn_ID = -1; // sound when fruit is spawned
    private int mEat_ID = -1; // sound when fruit is picked
    private int mBombID = -1; // sound when bomb explodes
    private int mDeathID = -1; // sound when player dies

    // Threads and control variables
    private Thread mGameThread = null;

    // volatile can be accessed from outside and inside the thread
    private volatile boolean mPlaying = true;
    private boolean mPaused = false;
    private long mNextFrameTime;

    /**
     * This is the constructor method for World, which acts as the game engine
     * @param context is passed from MainActivity
     * @param actualViewWidth represents the actual width of the entire view
     * @param actualViewHeight represents the actual height of the entire view
     */

    public World(Context context, int actualViewWidth, int actualViewHeight) {
        super(context);

        // Actual width/height of cells
        this.actualViewWidth = actualViewWidth;
        this.actualViewHeight = actualViewHeight;

        // Number of horizontal/vertical cells
        numCellsWide = 20;
        numCellsHigh = 10;

        /**
         * Initialize soundpool
         */
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
        }
        catch (IOException e) {
            // Error
        }

        /**
         * Initialize grid and players
         */
        // todo: we should instantiate playerOne and playerTwo with cellsize from gridToAbsolute
        // resolved: see below

        grid = new Grid(context, numCellsWide, numCellsHigh, actualViewWidth, actualViewHeight);
        Int2 cellResolution = new Int2(actualViewWidth/numCellsWide, actualViewHeight/numCellsHigh);
        playerOne = new Player(context, grid, new Int2(2, 2), 1, cellResolution);
        playerTwo = new Player(context, grid, new Int2(4, 4), 2, cellResolution);
        food = new Food(context, grid, new Int2(3, 3), cellResolution);
        grid.setCell(playerOne.getGridPosition(), CellStatus.PLAYER);
        grid.setCell(playerTwo.getGridPosition(), CellStatus.PLAYER);

        // Drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        scorePaint = new Paint();

        // Initialize with values passed in as params
        mScreenX = actualViewWidth;
        mScreenY = actualViewHeight;

        // 5% of width
        mFontSize = mScreenX / 20;
        // 1.5% of width
        mFontMargin = mScreenX / 75;

        startNewGame();
    }

    // When we start the thread with:
    // mGameThread.start();
    // the run method is continuously called by Android // because we implemented the Runnable interface
    // Calling mGameThread.join();
    // will stop the thread

    @Override
    public void run() {
        Log.d("Run", "I am running");
        while (mPlaying) {
            // What time is it at the start of the game loop?
            long frameStartTime = System.currentTimeMillis();

            // if game isn't paused, update 10 times a second
            if (!mPaused) {
                if (updateRequired()) {
<<<<<<< HEAD
=======
                    timesUpdated = timesUpdated + 1;
>>>>>>> 1c6d8263cd622f0b26063ab263c32d4c2ec33d5f
                    update();
                }
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
        playerOne.reset(numCellsWide, numCellsHigh);
        playerTwo.reset(numCellsWide, numCellsHigh);

        // banana should be spawned
        ArrayList<Int2> emptyCells = grid.getEmpty();
        food.spawn(emptyCells, numCellsWide, numCellsHigh);

        // Reset score
        mScoreP1 = 0;
        mScoreP2 = 0;

        // setup next frame time
        mNextFrameTime = System.currentTimeMillis();
    }

    void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            // Lock Canvas to draw
            mCanvas = mSurfaceHolder.lockCanvas();

            // Fill screen with solid colour, Todo: replace with grid drawing
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            // Color to paint with
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // Draw Grid
            grid.draw(mCanvas, mPaint);

            // Draw food, player
            food.draw(mCanvas, mPaint);
            playerOne.draw(mCanvas, mPaint);
            playerTwo.draw(mCanvas, mPaint);

            // Draw bombs, fire

            // Choose font size
            mPaint.setTextSize(mFontSize);

            // Paint Color for score
            scorePaint.setColor(Color.argb(255, 0, 0, 0));
            scorePaint.setTextSize(mFontSize);

            // Draw score
            mCanvas.drawText("Score P1: " + mScoreP1 + " Score P2: " + mScoreP2,
                    mFontMargin, mFontSize, scorePaint);

            if (mPaused) {
                mPaint.setColor(Color.argb(255, 0, 0, 0));
                mPaint.setTextSize(250);

                mCanvas.drawText("Tap to begin!", 200, 700, mPaint);
            }

            // Display drawing on screen
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void update() {
        // Did player eat food?
        if (playerOne.checkPickup(food.getLocation())) {
            ArrayList<Int2> emptyCells = grid.getEmpty();
            food.spawn(emptyCells, numCellsWide, numCellsHigh);
            mScoreP1 = mScoreP1 + 1;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        if (playerTwo.checkPickup(food.getLocation())) {
            ArrayList<Int2> emptyCells = grid.getEmpty();
            food.spawn(emptyCells, numCellsWide, numCellsHigh);
            mScoreP2 = mScoreP2 + 1;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        // Did player die?
        if (playerOne.detectDeath()) {
            mSP.play(mDeathID, 1, 1, 0, 0, 1);
            pause();
            Log.d("World", "Player one dies");
            // Say player 2 wins, timeout, then start new game in 5 secs
            startNewGame();
        }

        if (playerTwo.detectDeath()) {
            mSP.play(mDeathID, 1, 1, 0, 0, 1);
            pause();
            Log.d("World", "Player two dies");
            // Say player 1 wins, timeout, then start new game in 5 secs
            startNewGame();
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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    Log.d("Pausing", "Unpaused in World");
                    mPaused = false;
                    startNewGame();
                    return true;
                }
                Log.d("Touch","onTouchEvent in World");
                // todo: Check if motion event is coming from Player 1 or 2, and handle accordingly
                playerOne.switchHeading(motionEvent);
                break;
            default:
                break;
        }
        return true;
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
}


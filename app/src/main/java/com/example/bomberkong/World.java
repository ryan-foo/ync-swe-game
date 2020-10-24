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
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;
import com.example.bomberkong.util.Int2;

import java.io.IOException;

/**
 * This class is our game engine. It holds all the other classes and handles scoring,
 * sound, etc.
 */

public class World extends SurfaceView {

    private final boolean DEBUGGING = true;
    private Grid grid;
    private Player playerOne;
    private Player playerTwo;
    private int x; // World width
    private int y; // World height

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // Sound
    private SoundPool mSP;
    private int mSpawn_ID = -1; // sound when fruit is spawned
    private int mEat_ID = -1; // sound when fruit is picked
    private int mBombID = -1; // sound when bomb explodes
    private int mDeathID = -1; // sound when player dies

    private boolean mPaused = true;
    private long mNextFrameTime;

    // These will be initialized based on screen size in pixels
    public int mFontSize;
    public int mFontMargin;

    // Threads and control variables
    private Thread mGameThread = null;

    // Size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 20;
    private final int NUM_BLOCKS_HIGH = 10;

    // Score
    public int mScoreP1 = 0;
    public int mScoreP2 = 0;

    // For smooth movement
    private long mFPS;
    private final int MILLI_IN_SECONDS = 1000;

    //testing
    private String LOG = "World";

    public World(Context context, int x, int y) {
        super(context);

        this.x = x;
        this.y = y;

        /**
         * Initialize grid and players
         */
        grid = new Grid(NUM_BLOCKS_WIDE, NUM_BLOCKS_HIGH, x, y);
        playerOne = new Player(context, grid, new Int2(2, 2), 1);
        playerTwo = new Player(context, grid, new Int2(4, 4), 2);
        grid.setCell(playerOne.getPosition(), CellStatus.PLAYER);
        grid.setCell(playerTwo.getPosition(), CellStatus.PLAYER);

        // Drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

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
        } else {
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
            mBombID = mSP.load(descriptor, 0);
            //death sound
            descriptor = assetManager.openFd("chimchar.ogg");
            mDeathID = mSP.load(descriptor, 0);
        } catch (IOException e) {
            // Error
        }

        // 5% of width
        mFontSize = x / 20;
        // 1.5% of width
        mFontMargin = x / 75;
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
                Log.d(LOG,"touched");
                // todo: Check if motion event is coming from Player 1 or 2, and handle accordingly
                playerOne.switchHeading(motionEvent);
                break;
            default:
                break;
        }
        return true;
    }

    void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            // Lock the canvas (graphics memory) ready to draw mCanvas = mOurHolder.lockCanvas();
            mPaint.setColor(Color.argb
                    (255, 255, 255, 255));

            if (DEBUGGING) {
                printDebuggingText();
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void printDebuggingText () {
            int debugSize = mFontSize / 2;
            int debugStart = 150;
            mPaint.setTextSize(debugSize);
            mCanvas.drawText("absX: " + "x",
                    10, debugStart + debugSize, mPaint);
            mCanvas.drawText("absY: " + "y",
                    50, debugStart + debugSize, mPaint);
        }

        public Grid returnGrid () {
            return this.grid;
        }

        public Player returnPlayerOne () {
            return this.playerOne;
        }

        public Player returnPlayerTwo () {
            return this.playerTwo;
        }

        public void startNewGame () {
            // reset grid
            this.grid.reset();
            // Todo: reset to player original positions depending on player number

            // Food should be spawned

            // Reset score
            mScoreP1 = 0;
            mScoreP2 = 0;

            // setup next frame time
            mNextFrameTime = System.currentTimeMillis();
        }

        public void update () {
            this.grid.reset();
            this.grid.setCell(playerOne.getPosition(), CellStatus.PLAYER);
            this.grid.setCell(playerTwo.getPosition(), CellStatus.PLAYER);
        }
    }

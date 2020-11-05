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

import androidx.annotation.NonNull;

import com.example.bomberkong.model.Bomb;
import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Heading;
import com.example.bomberkong.model.Fire;
import com.example.bomberkong.model.Food;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.model.Player;

import com.example.bomberkong.util.Int2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The world stores instances such as the main game grid, players, and soundPools. It acts as the
 * main game loop, where the run method keeps track of updates and the onTouchEvent handles player
 * inputs.
 * credits for framework: John Horton
 */
public class World extends SurfaceView implements Runnable {
    
    private static final String TAG = "World";
    private final String playerNumControlled;

    // Connect to Firebase database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Objects for drawing
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint scorePaint;

    // Instances of objects that will last throughout
    private Context context;
    private Grid grid;
    private Player playerOne;
    private Player playerTwo;
    private Food food;

    // Every fire spawned will be added to fireList, and we can get their pos and time to tick down
    private ArrayList<Fire> fireList = new ArrayList<Fire>();
    private int actualViewWidth;
    private int actualViewHeight;
    private int numCellsWide;
    private int numCellsHigh;
    public Int2 cellResolution;

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
    private int mSpawn_ID = -1; // sound when fire is gone
    private int mEat_ID = -1; // sound when fruit is picked
    private int mBombID = -1; // sound when bomb explodes
    private int mDeathID = -1; // sound when player dies

    // Threads and control variables
    private Thread mGameThread = null;

    // volatile can be accessed from outside and inside the thread
    private volatile boolean mPlaying = true;
    private boolean mPaused = false;
    // start game with neither player having won
    private boolean playerOneWin = false;
    private boolean playerTwoWin = false;
    private long mNextFrameTime;
    private long p1NextMoveTime;
    private long p2NextMoveTime;

    private final Int2 p1StartPos = new Int2(2, 2);
    private final Int2 p2StartPos = new Int2(17, 7);

    /**
     * This is the constructor method for World, which acts as the game engine
     * @param context          is passed from GameActivity
     * @param actualViewWidth  represents the actual width of the entire view
     * @param actualViewHeight represents the actual height of the entire view
     * @param playerNumControlled is the number of the player that is currently being controlled
     */
    public World(Context context, int actualViewWidth, int actualViewHeight, String playerNumControlled) {
        super(context);
        this.playerNumControlled = playerNumControlled;
        Log.d(TAG, "Player controlled" + playerNumControlled);

        // Actual width/height of cells
        this.actualViewWidth = actualViewWidth;
        this.actualViewHeight = actualViewHeight;

        // Number of horizontal/vertical cells
        numCellsWide = 20;
        numCellsHigh = 10;

        /*
          Initialize soundpool
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

        /**
         * Initialize grid and players
         */

        grid = new Grid(context, numCellsWide, numCellsHigh, actualViewWidth, actualViewHeight);
        cellResolution = new Int2(actualViewWidth / numCellsWide, actualViewHeight / numCellsHigh);
        playerOne = new  Player(context, grid, p1StartPos, 1, cellResolution, playerNumControlled);
        playerTwo = new Player(context, grid, p2StartPos, 2, cellResolution, playerNumControlled);
        food = new Food(context, new Int2(3, 3), cellResolution);
        fireList = new ArrayList<Fire>();
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
        mFontSize = mScreenX / 21;
        // 1.5% of width
        mFontMargin = mScreenX / 75;

        // add database listeners to World to read from Firebase and update the world realtime
        addScoreListener();
        addFoodListener();
        addPlayerPositionListener();
        addPlayerHeadingListener();
        addPlayerDeathListener();
        addBombListener();
        startNewGame();
    }

    private void addScoreListener() {
        DatabaseReference _score1Ref = database.getReference("player1/score");
        _score1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("2")) {
                    mScoreP1 = snapshot.getValue(int.class);
//                    mCanvas.drawText("Score P1: " + mScoreP1 + " Score P2: " + mScoreP2,
//                            mFontMargin, mFontSize, scorePaint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference _score2Ref = database.getReference("player2/score");
        _score2Ref.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("2")) {
                    mScoreP2 = snapshot.getValue(int.class);
//                    mCanvas.drawText("Score P1: " + mScoreP1 + " Score P2: " + mScoreP2,
//                            mFontMargin, mFontSize, scorePaint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    private void addBombListener() {
        DatabaseReference _bomb1Ref = database.getReference("player1/bomb");
        _bomb1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("2")) {
                    Bomb bombOne = snapshot.getValue(Bomb.class);
                    playerOne.setBomb(bombOne);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference _bomb2Ref = database.getReference("player2/bomb");
        _bomb2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("1")) {
                    Bomb bombTwo = snapshot.getValue(Bomb.class);
                    playerTwo.setBomb(bombTwo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFoodListener() {
        DatabaseReference _foodRef = database.getReference("food");
        _foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Int2 foodLoc = snapshot.getValue(Int2.class);
                food.setLocation(foodLoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPlayerPositionListener() {
        DatabaseReference _pos1Ref = database.getReference("player1/position");
        _pos1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("2")) {
                    Int2 pos1 = snapshot.getValue(Int2.class);
                    playerOne.setGridPosition(pos1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference _pos2Ref = database.getReference("player2/position");
        _pos2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("1")) {
                    Int2 pos2 = snapshot.getValue(Int2.class);
                    playerTwo.setGridPosition(pos2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPlayerHeadingListener() {
        DatabaseReference _head1Ref = database.getReference("player1/heading");
        _head1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("2")) {
                    Heading heading1 = snapshot.getValue(Heading.class);
                    playerOne.setHeading(heading1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference _head2Ref = database.getReference("player2/heading");
        _head2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerNumControlled.equals("1")) {
                    Heading heading2 = snapshot.getValue(Heading.class);
                    playerTwo.setHeading(heading2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPlayerDeathListener() {
        DatabaseReference _death1Ref = database.getReference("player1/death");
        _death1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean dead1 = snapshot.getValue(boolean.class);
                playerOne.setDead(dead1);
                playerOneWin = false;
                playerTwoWin = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference _death2Ref = database.getReference("player2/death");
        _death2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean dead2 = snapshot.getValue(boolean.class);
                playerTwo.setDead(dead2);
                playerTwoWin = false;
                playerOneWin = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    /**
     * Start new game is called when we want to start a new game. We start a new game when the game first boots, and also 5 seconds after the game has ended.
     * This resets the grid, and resets the positions of the players, and the score.
     */

    public void startNewGame() {
        // reset grid
        this.grid.reset();
        // Todo: reset to player original positions depending on player number
        mPaused = false; // game is running.
        playerOne.reset(p1StartPos);
        playerTwo.reset(p2StartPos);

        playerOneWin = false;
        playerTwoWin = false;
        playerOne.setDead(false);
        playerTwo.setDead(false);

        // banana should be spawned
        // ArrayList<Int2> emptyCells = grid.getEmpty();
        // food.spawn(emptyCells, numCellsWide, numCellsHigh);

        // place food at center so that both players start with same food location
        food.setLocation(new Int2(9,5));

        DatabaseReference _foodRef = database.getReference("food");
        _foodRef.setValue(food.getLocation());

        // Reset score
        mScoreP1 = 0;
        mScoreP2 = 0;
        DatabaseReference _score1Ref = database.getReference("player1/score");
        _score1Ref.setValue(mScoreP1);
        DatabaseReference _score2Ref = database.getReference("player2/score");
        _score2Ref.setValue(mScoreP2);

        // setup time
        mNextFrameTime = System.currentTimeMillis();
        p1NextMoveTime = System.currentTimeMillis();
        p2NextMoveTime = System.currentTimeMillis();
    }

    void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            // Lock Canvas to draw
            mCanvas = mSurfaceHolder.lockCanvas();

            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            // Color to paint with
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // Draw Grid
            grid.draw(mCanvas);

            // Draw food, player
            food.draw(mCanvas, mPaint);

            //todo: checking if grid uses draw, or drawElements
            playerOne.draw(mCanvas, mPaint);
            playerTwo.draw(mCanvas, mPaint);

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
                if (playerOneWin) {
                    mCanvas.drawText("Player One Wins!", 200, 700, mPaint);
                }
                if (playerTwoWin) {
                    mCanvas.drawText("Player Two Wins!", 200, 700, mPaint);
                }
            }

            // Display drawing on screen
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void update() {
        // Did player eat food?
        if (playerOne.checkPickup(food.getLocation())) {
            mScoreP1 += 1;
            DatabaseReference _score1Ref = database.getReference("player1/score");
            _score1Ref.setValue(mScoreP1);
            ArrayList<Int2> emptyCells = grid.getEmpty();
            food.spawn(emptyCells, numCellsWide, numCellsHigh);
            DatabaseReference _foodRef = database.getReference("food");
            _foodRef.setValue(food.getLocation());
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        if (playerTwo.checkPickup(food.getLocation())) {
            mScoreP2 += 1;
            DatabaseReference _score2Ref = database.getReference("player2/score");
            _score2Ref.setValue(mScoreP2);
            ArrayList<Int2> emptyCells = grid.getEmpty();
            food.spawn(emptyCells, numCellsWide, numCellsHigh);
            DatabaseReference _foodRef = database.getReference("food");
            _foodRef.setValue(food.getLocation());
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        // Did player die?
        if (playerOne.getGrid().getCellStatus(playerOne.getGridPosition()) == CellStatus.FIRE) {
            playerOne.setDead(true);
        }

        if (playerOne.getDead()) {
            mSP.play(mDeathID, 1, 1, 0, 0, 1);
            Log.d("World", "Player one dies");
            mPaused = true;
            playerTwoWin = true;
            DatabaseReference _death1Ref = database.getReference("player1/death");
            _death1Ref.setValue(playerOne.getDead());
            draw();
            // Say player 2 wins, timeout, then start new game in 5 secs
            int gameEndCounter = 5;
            while (gameEndCounter != 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameEndCounter -= 1;
            }

            startNewGame();
        }

        if (playerTwo.getGrid().getCellStatus(playerTwo.getGridPosition()) == CellStatus.FIRE) {
            playerTwo.setDead(true);
        }

        if (playerTwo.getDead()) {
            mSP.play(mDeathID, 1, 1, 0, 0, 1);
            Log.d("World", "Player two dies");
            mPaused = true;
            playerOneWin = true;
            DatabaseReference _death2Ref = database.getReference("player2/death");
            _death2Ref.setValue(playerTwo.getDead());
            draw();
            // Say player 1 wins, timeout, then start new game in 5 secs
            int gameEndCounter = 5;
            while (gameEndCounter != 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameEndCounter -= 1;
            }

            startNewGame();
        }


        /**
         * Iterators allow us to remove elements while iterating through a list.
         * In this case, we use iterators to handle both the ticking down of bombs and the ticking down of fires before removing them from the game.
         */

        if (playerOne.getBomb() != null) {
            if (playerNumControlled.equals("1")) {
                Bomb bombOne = playerOne.getBomb();
                DatabaseReference _bomb1Ref = database.getReference("player1/bomb");
                _bomb1Ref.setValue(bombOne);
                bombOne.ticksToExplode -= 1;
                if (bombOne.ticksToExplode == 0) {
                    bombOne.explode(this, fireList);
                    mSP.play(mBombID, 1, 1, 0, 0, 1);
                    playerOne.resetBomb();

                }
            }
        }

        if (playerTwo.getBomb() != null) {
            if (playerNumControlled.equals("2")){
                Bomb bombTwo = playerTwo.getBomb();
                DatabaseReference _bomb2Ref = database.getReference("player2/bomb");
                _bomb2Ref.setValue(bombTwo);
                bombTwo.ticksToExplode -= 1;
                if (bombTwo.ticksToExplode == 0) {
                    bombTwo.explode(this, fireList);
                    mSP.play(mBombID, 1, 1, 0, 0, 1);
                    playerTwo.resetBomb();
                }
            }
        }

        // todo: there seems to be a bug where the screen does not clear!

        Iterator<Fire> fitr = fireList.iterator();
        while (fitr.hasNext()) {
            Fire fire = fitr.next();
            fire.ticksToFade -= 1;
            if (fire.ticksToFade == 0) {
                // todo: something to do with drawing empty?
                grid.setCell(fire.getGridPosition(), CellStatus.EMPTY);
            }
        }
    }

    public Grid getGrid(){
        return this.grid;
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

    /**
     * p1movementAllowed allows us to rate limit the amount of input we can take for each player, respectively. It returns a boolean, which when true, allows us to process the input of the player.
     * 
     * which 
     * @return
     */

    public boolean p1MovementAllowed() {
        // Run at 4 moves per second
        final long MOVES_PER_SECOND = 4;
        // 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // are we due to update the frame?
        if (p1NextMoveTime <= System.currentTimeMillis()) {
            // 1/2 a second has passed
            p1NextMoveTime = System.currentTimeMillis() + MILLIS_PER_SECOND / MOVES_PER_SECOND;
            return true;
        }
        return false;
    }


    public boolean p2MovementAllowed() {
        // Run at 4 moves per second
        final long MOVES_PER_SECOND = 4;
        // 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // are we due to update the frame?
        if (p2NextMoveTime <= System.currentTimeMillis()) {
            // 1/2 a second has passed
            p2NextMoveTime = System.currentTimeMillis() + MILLIS_PER_SECOND / MOVES_PER_SECOND;
            return true;
        }
        return false;
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

                if (playerNumControlled.equals("1")){
                    if (p1MovementAllowed()) {
                        Log.d(TAG, "move p1");
                        playerOne.switchHeading(motionEvent);
                    }
                } else {
                    if (p2MovementAllowed()) {
                        Log.d(TAG, "move p2");
                        playerTwo.switchHeading(motionEvent);
                    }
                }
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


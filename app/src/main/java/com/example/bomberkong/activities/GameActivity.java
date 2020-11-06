package com.example.bomberkong.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.example.bomberkong.MusicService;
import com.example.bomberkong.World;

// credits for framework: John Horton

public class GameActivity extends Activity {
    private World mWorld;
    private boolean mIsBound = false;
    private MusicService mServ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // gets the player number that is being controlled.
        String playerNumControlled = getIntent().getStringExtra("playerID");

        // to get the display and display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Log.e("GameActivity",getApplicationContext().toString());
        mWorld = new World(getApplicationContext(), size.x, size.y, playerNumControlled);
        setContentView(mWorld);

        // Music
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

    }

    // Playing music
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }



    // Resume and Pause
    @Override
    protected void onResume() {
        super.onResume();
        mWorld.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mWorld.pause();
    }
}
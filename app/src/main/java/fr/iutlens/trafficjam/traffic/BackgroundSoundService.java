package fr.iutlens.trafficjam.traffic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import fr.iutlens.trafficjam.R;

public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer playermusic;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        playermusic = MediaPlayer.create(this, R.raw.music);
        playermusic.setLooping(true); // Set looping
        playermusic.start();
        playermusic.setVolume(80,80);


    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        playermusic.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
        playermusic.stop();
    }
    public void onPause() {
        playermusic.pause();
    }
    @Override
    public void onDestroy() {
        playermusic.stop();
        playermusic.release();
    }
}
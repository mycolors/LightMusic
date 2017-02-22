package com.fengniao.lightmusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fengniao.lightmusic.PlayMusic;
import com.fengniao.lightmusic.model.MusicInfo;

import java.io.IOException;
import java.util.List;


public class PlayMusicService extends Service implements PlayMusic {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private PlayMusicBinder mBinder = new PlayMusicBinder();

    public class PlayMusicBinder extends Binder implements PlayMusic {


        @Override
        public void play(String path) {
            PlayMusicService.this.play(path);
        }

        @Override
        public void pause() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void next() {

        }

        @Override
        public void last() {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "onCreate");
    }

    @Override
    public void play(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.i("tag", "service");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void next() {

    }

    @Override
    public void last() {

    }
}

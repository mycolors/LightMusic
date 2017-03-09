package com.fengniao.lightmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.fengniao.lightmusic.MusicManager;
import com.fengniao.lightmusic.PlayMusic;
import com.fengniao.lightmusic.model.Music;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class PlayService extends Service implements PlayMusic {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Music music;
    private boolean isFirstPlay = true;

    private final MusicManager.Stub mBinder = new MusicManager.Stub() {

        @Override
        public void setMusic(Music music) throws RemoteException {
            PlayService.this.music = music;
            isFirstPlay = true;
        }

        @Override
        public void play() throws RemoteException {
            PlayService.this.play();
        }

        @Override
        public void pause() throws RemoteException {
            PlayService.this.pause();
        }

        @Override
        public void stop() throws RemoteException {
            PlayService.this.stop();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return PlayService.this.isPlaying();
        }

        @Override
        public boolean isLooping() throws RemoteException {
            return PlayService.this.isLooping();
        }

        @Override
        public void seekTo(int time) throws RemoteException {
            PlayService.this.seekTo(time);
        }

        @Override
        public int getDuration() throws RemoteException {
            return PlayService.this.getDuration();
        }

        @Override
        public int getCurrentTime() throws RemoteException {
            return PlayService.this.getCurrentTime();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void initMediaPlayer() {
        if (music == null) return;
        try {
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer.isPlaying();
    }

    public boolean isLooping() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        return mediaPlayer.isLooping();
    }

    @Override
    public void play() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        if (isFirstPlay) {
            isFirstPlay = false;
            initMediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } else {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.pause();
    }

    @Override
    public void stop() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        isFirstPlay = true;
        mediaPlayer.reset();
    }

    @Override
    public void next() {

    }

    @Override
    public void last() {

    }

    @Override
    public void seekTo(int time) {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.seekTo(time);
    }

    @Override
    public int getDuration() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentTime() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        return mediaPlayer.getCurrentPosition();
    }
}

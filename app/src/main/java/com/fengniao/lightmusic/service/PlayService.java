package com.fengniao.lightmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.fengniao.lightmusic.PlayMusic;
import com.fengniao.lightmusic.model.MusicInfo;

import java.io.IOException;
import java.util.List;


public class PlayService extends Service implements PlayMusic {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private PlayMusicBinder mBinder = new PlayMusicBinder();
    private List<MusicInfo> mList;
    private int currentPositon;

    public class PlayMusicBinder extends Binder implements PlayMusic {


        @Override
        public void play() {
            PlayService.this.play();
        }

        @Override
        public void pause() {
            PlayService.this.pause();
        }

        @Override
        public void stop() {
            PlayService.this.stop();
        }

        @Override
        public void next() {
            PlayService.this.next();
        }

        @Override
        public void last() {
            PlayService.this.last();
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void setMusicList(List<MusicInfo> list) {
            PlayService.this.mList = list;
        }

        public void setPositon(int position) {
            currentPositon = position;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void play() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mList.get(currentPositon).getPath());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void stop() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.stop();
    }

    @Override
    public void next() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        if (currentPositon < (mList.size() - 1)) {
            currentPositon++;
            play();
        } else {
            Toast.makeText(getApplicationContext(), "已经是最后一首了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void last() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        if (currentPositon == 0) {
            Toast.makeText(getApplicationContext(), "已经是第一首了", Toast.LENGTH_SHORT).show();
            return;
        }
        currentPositon--;
        play();
    }
}

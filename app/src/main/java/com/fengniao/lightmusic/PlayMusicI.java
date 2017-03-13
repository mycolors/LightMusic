package com.fengniao.lightmusic;

/**
 * Created by a1 on 2017/2/22.
 */

public interface PlayMusicI {
    void play();

    void pause();

    void stop();

    void next();

    void last();

    void seekTo(int time);

    int getDuration();

    int getCurrentTime();
}

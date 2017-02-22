package com.fengniao.lightmusic;

/**
 * Created by a1 on 2017/2/22.
 */

public interface PlayMusic {
    void play(String path);

    void pause();

    void stop();

    void next();

    void last();
}

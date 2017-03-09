// MusicManager.aidl
package com.fengniao.lightmusic;
import com.fengniao.lightmusic.model.Music;
// Declare any non-default types here with import statements

interface MusicManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void setMusic(in Music music);

     void play();

     void pause();

     void stop();

     boolean isPlaying();

     boolean isLooping();

     void seekTo(int time);

     int getDuration();

     int getCurrentTime();
}

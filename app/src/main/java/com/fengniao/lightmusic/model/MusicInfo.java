package com.fengniao.lightmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by a1 on 2017/2/22.
 */

public class MusicInfo implements Parcelable {
    private long id;
    private String title;
    private String album;
    private int duration;
    private long size;
    private String artist;
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeInt(duration);
        dest.writeLong(size);
        dest.writeString(artist);
        dest.writeString(path);
    }

    public MusicInfo() {
    }

    public MusicInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.album = in.readString();
        this.duration = in.readInt();
        this.size = in.readLong();
        this.artist = in.readString();
        this.path = in.readString();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };


}

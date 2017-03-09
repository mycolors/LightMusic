package com.fengniao.lightmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {
    private long id;
    private String title;
    private String album;
    private int duration;
    private long size;
    private String artist;
    private String path;
    private long albumId;

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

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
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
        dest.writeLong(albumId);
    }

    public Music() {
    }

    public Music(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.album = in.readString();
        this.duration = in.readInt();
        this.size = in.readLong();
        this.artist = in.readString();
        this.path = in.readString();
        this.albumId = in.readLong();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };


}

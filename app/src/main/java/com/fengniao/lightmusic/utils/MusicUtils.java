package com.fengniao.lightmusic.utils;

/**
 * Created by a1 on 2017/2/22.
 */


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.fengniao.lightmusic.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐工具类,
 */
public class MusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<Music> getMusicData(Context context) {
        List<Music> list = new ArrayList<Music>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Music info = new Music();
                info.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                info.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                info.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));

//                if (song.size > 1000 * 800) {
//                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                    if (song.song.contains("-")) {
//                        String[] str = song.song.split("-");
//                        song.singer = str[0];
//                        song.song = str[1];
//                    }
                list.add(info);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }
}
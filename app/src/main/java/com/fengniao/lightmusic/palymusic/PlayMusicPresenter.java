package com.fengniao.lightmusic.palymusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.fengniao.lightmusic.MusicManager;
import com.fengniao.lightmusic.model.Music;
import com.fengniao.lightmusic.service.PlayService;
import com.fengniao.lightmusic.utils.MusicUtils;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;


public class PlayMusicPresenter implements PlayMusic.Presenter {
    private MusicManager mMusicManager;

    private final PlayMusic.View mView;

    private List<Music> mList;

    private int currentPosition;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicManager = MusicManager.Stub.asInterface(service);
            try {
                mMusicManager.setMusic(mList.get(currentPosition));
                mView.selectMusic(currentPosition);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            updateSchedule();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public PlayMusicPresenter(PlayMusic.View mView) {
        this.mView = mView;
        mMusicManager = mView.getMusicManager();
        mView.setPresenter(this);
        start();
    }

    @Override
    public void start() {
        mList = getMusicList();
        Intent intent = new Intent(getActivity(), PlayService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void setMusic(Music music) {
        try {
            mMusicManager.setMusic(music);
            mMusicManager.stop();
            mMusicManager.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playOrPause() {
        try {
            if (mMusicManager.isPlaying()) {
                mView.setPlayStatus("播放");
                mMusicManager.pause();
            } else {
                mView.setPlayStatus("暂停");
                mMusicManager.play();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void next() {
        if (currentPosition + 1 == mList.size()) {
            mView.showMusicEndToast();
            return;
        }
        currentPosition++;
        mView.selectMusic(currentPosition);
        setMusic(mList.get(currentPosition));

    }

    @Override
    public void stop() {
        try {
            mMusicManager.stop();
            mView.setPlayStatus("播放");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Music> getMusicList() {
        return MusicUtils.getMusicData(getActivity());
    }


    android.os.Handler mHandler = new android.os.Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable, 500);
            try {
                if (mMusicManager.getDuration() >= 0) {
                    int progress = (int) ((float) mMusicManager.getCurrentTime() /
                            (float) mList.get(currentPosition).getDuration() * 100);
                    if (mMusicManager.isPlaying()) {
                        mView.setPlayStatus("暂停");
                        mView.showMusicProgress(progress);
                    } else {
                        mView.setPlayStatus("播放");
                        if (progress >= 99) {
                            mView.showMusicProgress(0);
                        }
                    }
                } else {
                    mView.showMusicProgress(0);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void updateSchedule() {
        mHandler.postDelayed(runnable, 500);
    }

    @Override
    public void setMusicPic(int position) {
        String album = getAlbumArt((int) mList.get(position).getAlbumId());
        Bitmap bm = BitmapFactory.decodeFile(album);
        mView.showMusicPic(bm);
    }

    @Override
    public void itemClick(int position) {
        mView.setPlayStatus("暂停");
        if (currentPosition == position) {
            try {
                mMusicManager.play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
        currentPosition = position;
        mView.selectMusic(currentPosition);
        setMusic(mList.get(currentPosition));
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(connection);
        mHandler.removeCallbacks(runnable);
        mHandler = null;
    }

    @Override
    public Activity getActivity() {
        return mView.getActivity();
    }


    /**
     * 功能 通过album_id查找 album_art 如果找不到返回null
     *
     * @param album_id
     * @return album_art
     */
    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = getActivity().getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        return album_art;
    }
}

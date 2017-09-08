package com.fengniao.lightmusic.playmusic;


import android.app.Activity;
import android.graphics.Bitmap;

import com.fengniao.lightmusic.base.BasePresenter;
import com.fengniao.lightmusic.base.BaseView;
import com.fengniao.lightmusic.model.Music;

import java.util.List;

public interface PlayMusic {
    interface View extends BaseView<Presenter> {
        void setPlayStatus(String text);

        void showMusicProgress(int schedule);

        void showMusicPic(Bitmap bm);

        void showHeaderPic(Bitmap bm);

        void showMusicEndToast();

        void selectMusic(int position);

        Activity getActivity();
    }

    interface Presenter extends BasePresenter {

        void setMusic(Music music);

        void playOrPause();

        void next();

        void stop();

        List<Music> getMusicList();

        void updateSchedule();

        void setMusicPic(int id);

        void itemClick(int position);

        void onDestroy();

        Activity getActivity();
    }

}

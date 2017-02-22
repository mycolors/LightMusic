package com.fengniao.lightmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fengniao.lightmusic.adapter.MusicListAdapter;
import com.fengniao.lightmusic.model.MusicInfo;
import com.fengniao.lightmusic.service.PlayMusicService;
import com.fengniao.lightmusic.utils.MusicUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.music_list)
    RecyclerView musicList;
    private List<MusicInfo> mList;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private PlayMusicService.PlayMusicBinder mBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (PlayMusicService.PlayMusicBinder) service;
            Log.i("tag", "bind");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PlayMusicService.class);
        startService(intent);
//        bindService(intent, connection, BIND_AUTO_CREATE);
        ButterKnife.bind(this);
        initData();
    }


    public void initData() {
        mList = MusicUtils.getMusicData(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        MusicListAdapter adapter = new MusicListAdapter(this, mList);
        musicList.setLayoutManager(manager);
        musicList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                initMediaPlayer(mList.get(position).getPath());
//                mBinder.play(mList.get(position).getPath());

            }
        });
    }

    private void initMediaPlayer(String path) {

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}

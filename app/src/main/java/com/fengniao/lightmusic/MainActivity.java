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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengniao.lightmusic.adapter.MusicListAdapter;
import com.fengniao.lightmusic.model.MusicInfo;
import com.fengniao.lightmusic.service.PlayService;
import com.fengniao.lightmusic.utils.MusicUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.music_list)
    RecyclerView musicList;
    @BindView(R.id.img_music)
    ImageView imgMusic;
    @BindView(R.id.text_music)
    TextView textMusic;
    @BindView(R.id.text_next)
    TextView textNext;
    @BindView(R.id.text_play)
    TextView textPlay;
    @BindView(R.id.text_stop)
    TextView textStop;
    private List<MusicInfo> mList;
    private PlayService.PlayMusicBinder mBinder;
    //当前模仿位置，默认为0
    private int currentPositon;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (PlayService.PlayMusicBinder) service;
            Log.i("tag", "tag");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PlayService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        initView();
        initData();
    }

    public void initView() {
        ButterKnife.bind(this);
//        if (mBinder.isPlaying())
//            textPlay.setText("暂停");
//        else
//            textPlay.setText("播放");
    }


    public void initData() {
        mList = MusicUtils.getMusicData(this);
//        mBinder.setMusicList(mList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        MusicListAdapter adapter = new MusicListAdapter(this, mList);
        musicList.setLayoutManager(manager);
        musicList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (currentPositon == position) {
                    return;
                }
                mBinder.setPositon(position);
                mBinder.play();
                textMusic.setText(mList.get(position).getTitle());
                currentPositon = position;
            }
        });
    }

    @OnClick(R.id.text_play)
    public void playOrPause(View view) {
        if (mBinder.isPlaying()) {
            mBinder.pause();
            textPlay.setText("播放");
        } else {
            mBinder.play();
            textPlay.setText("暂停");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}

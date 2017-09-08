package com.fengniao.lightmusic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fengniao.lightmusic.activity.BaseActivity;
import com.fengniao.lightmusic.adapter.MusicListAdapter;
import com.fengniao.lightmusic.model.Music;
import com.fengniao.lightmusic.playmusic.PlayMusic;
import com.fengniao.lightmusic.playmusic.PlayMusicPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements PlayMusic.View {
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
    @BindView(R.id.pro_music)
    ProgressBar progressBar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    ImageView imgMusicHeader;
    TextView textMusicHeader;
    private List<Music> mList;
    private MusicListAdapter adapter;
    private PlayMusic.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new PlayMusicPresenter(this);
        initView();
    }

    public void initView() {
        imgMusicHeader = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_music_header);
        textMusicHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_music_header);
        mList = mPresenter.getMusicList();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new MusicListAdapter(this, mList);
        musicList.setLayoutManager(manager);
        musicList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.itemClick(position);
            }
        });
    }

    @Override
    public void selectMusic(int position) {
        mPresenter.setMusicPic(position);
        textMusicHeader.setText(mList.get(position).getTitle());
        textMusic.setText(mList.get(position).getTitle());
        adapter.selectItem(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setPlayStatus(String text) {
        textPlay.setText(text);
    }

    @Override
    public void showMusicProgress(int schedule) {
        progressBar.setProgress(schedule);
    }

    @Override
    public void showMusicPic(Bitmap bm) {
        imgMusic.setImageBitmap(bm);
    }

    @Override
    public void showHeaderPic(Bitmap bm) {
        imgMusicHeader.setImageBitmap(bm);
    }


    @Override
    public void showMusicEndToast() {
        Toast.makeText(this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.text_play)
    public void playOrPause(View view) {
        mPresenter.playOrPause();
    }

    @OnClick(R.id.text_stop)
    public void stop(View view) {
        mPresenter.stop();
    }


    @OnClick(R.id.text_next)
    public void next(View view) {
        mPresenter.next();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void setPresenter(PlayMusic.Presenter presenter) {
        mPresenter = presenter;
    }
}

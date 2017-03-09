package com.fengniao.lightmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fengniao.lightmusic.adapter.MusicListAdapter;
import com.fengniao.lightmusic.model.Music;
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
    @BindView(R.id.pro_music)
    ProgressBar progressBar;
    private List<Music> mList;
    private MusicManager mMusicManager;
    //当前播放位置，默认为0
    private int currentPositon;
    private MusicListAdapter adapter;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicManager = MusicManager.Stub.asInterface(service);
            try {
                mMusicManager.setMusic(mList.get(currentPositon));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            showMusicProgress();
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
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        initView();
        initData();
    }

    public void initView() {
        ButterKnife.bind(this);
        mList = MusicUtils.getMusicData(this);
        //显示歌曲封面图片
        showAlbumPic((int) mList.get(currentPositon).getAlbumId());
    }

    //显示歌曲封面图片
    private void showAlbumPic(int album_id) {
        String album = getAlbumArt(album_id);
        Bitmap bm = BitmapFactory.decodeFile(album);
        imgMusic.setImageBitmap(bm);
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
        Cursor cur = this.getContentResolver().query(
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

    public void initData() {
        textMusic.setText(mList.get(currentPositon).getTitle());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new MusicListAdapter(this, mList);
        musicList.setLayoutManager(manager);
        musicList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                textPlay.setText("暂停");
                if (currentPositon == position) {
                    try {
                        mMusicManager.play();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                currentPositon = position;
                selectMusic(position);
                setMusic(mList.get(currentPositon));
            }
        });
    }

    public void selectMusic(int position) {
        showAlbumPic((int) mList.get(currentPositon).getAlbumId());
        textMusic.setText(mList.get(position).getTitle());
        adapter.selectItem(position);
        adapter.notifyDataSetChanged();
    }

    public void setMusic(Music music) {
        try {
            mMusicManager.setMusic(music);
            mMusicManager.stop();
            mMusicManager.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.text_play)
    public void playOrPause(View view) {
        try {
            if (mMusicManager.isPlaying()) {
                textPlay.setText("播放");
                mMusicManager.pause();
                textPlay.setFocusable(false);
            } else {
                textPlay.setText("暂停");
                textPlay.setFocusable(true);
                mMusicManager.play();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //播放音乐时显示音乐进度
    public void showMusicProgress() {
        mHandler.postDelayed(runnable, 1000);
    }


    android.os.Handler mHandler = new android.os.Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable, 1000);
            try {
                if (mMusicManager.getDuration() >= 0) {
                    int progress = (int) ((float) mMusicManager.getCurrentTime() /
                            (float) mList.get(currentPositon).getDuration() * 100);
                    if (mMusicManager.isPlaying()) {
                        textPlay.setText("暂停");
                        progressBar.setProgress(progress);
                    } else {
                        textPlay.setText("播放");
                        if (progress >= 99) {
                            progressBar.setProgress(0);
                        }
                    }
                } else {
                    progressBar.setProgress(0);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @OnClick(R.id.text_stop)
    public void stop(View view) {
        try {
            mMusicManager.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.text_next)
    public void next(View view) {
        if (currentPositon + 1 == mList.size()) {
            Toast.makeText(this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
            return;
        }
        currentPositon++;
        selectMusic(currentPositon);
        setMusic(mList.get(currentPositon));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mHandler.getLooper().quit();
        mHandler = null;
    }
}

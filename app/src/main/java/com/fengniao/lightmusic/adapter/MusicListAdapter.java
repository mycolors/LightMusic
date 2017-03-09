package com.fengniao.lightmusic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengniao.lightmusic.R;
import com.fengniao.lightmusic.model.Music;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {
    private Context context;
    private List<Music> list;
    private OnItemClickListener mOnItemClickListener;
    private int selectPositon;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MusicListAdapter(Context context, List<Music> list) {
        this.context = context;
        this.list = list;
    }

    public void selectItem(int position) {
        this.selectPositon = position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_list_music, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.musicName.setText(list.get(position).getTitle());
        if (position == selectPositon) {
            holder.musicName.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.musicName.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView musicName;

        public MyViewHolder(View itemView) {
            super(itemView);
            musicName = (TextView) itemView.findViewById(R.id.text_music);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}


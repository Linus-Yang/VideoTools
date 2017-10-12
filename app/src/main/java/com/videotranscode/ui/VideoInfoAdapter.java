package com.videotranscode.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videotranscode.entity.VideoInfo;

import java.security.Key;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by linus on 2017/10/10.
 */

public class VideoInfoAdapter extends RecyclerView.Adapter {

    private HashMap<String, LinkedList<VideoInfo>> mVideoInfoHashMap;

    public void setData(HashMap<String, LinkedList<VideoInfo>> videoInfoHashMap) {
        this.mVideoInfoHashMap = videoInfoHashMap;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoViewHolder(new GirdImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InfoViewHolder ivHolder = (InfoViewHolder) holder;
        Set<String> keySet = mVideoInfoHashMap.keySet();
    }

    @Override
    public int getItemCount() {
        return mVideoInfoHashMap != null && mVideoInfoHashMap.size() != 0 ? mVideoInfoHashMap.size() : 0;
    }


    public class InfoViewHolder extends RecyclerView.ViewHolder {

        private GirdImageView gridImageView;

        public InfoViewHolder(View itemView) {
            super(itemView);
            gridImageView = (GirdImageView) itemView;
        }
    }
}

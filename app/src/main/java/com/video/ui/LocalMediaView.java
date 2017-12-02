package com.video.ui;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by linus on 2017/12/2.
 */

public class LocalMediaView extends RecyclerView {

    public interface OnClickItemListener {
        void onClickItem(String path, String name,int position);
    }

    private MyAdapter            mAdapter;
    private LinearLayoutManager  mLayoutManager;
    private List<VideoInfo>      mVideoInfoList;
    private LocalMediaQuery      mMediaQuery;
    private OnClickItemListener  mOnClickItemListener;

    public LocalMediaView(Context context) {
        super(context);
    }

    public LocalMediaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalMediaView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.mOnClickItemListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAdapter = new MyAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        mVideoInfoList = new ArrayList<>();
        mMediaQuery = new LocalMediaQuery(getContext().getContentResolver(), mVideoInfoList, mAdapter);
        mMediaQuery.startQuery();

        setLayoutManager(mLayoutManager);
        setAdapter(mAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(25);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 256);
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundColor(Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
            return new MyViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            VideoInfo info = mVideoInfoList.get(position);
            viewHolder.mTextView.setText(info.getName());
        }

        @Override
        public int getItemCount() {
            return mVideoInfoList != null ? mVideoInfoList.size() : 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView mTextView;

        public MyViewHolder(View itemView ) {
            super(itemView);
            mTextView = (TextView) itemView;
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnClickItemListener != null) {
                int position = getAdapterPosition();
                VideoInfo videoInfo = mVideoInfoList.get(position);
                mOnClickItemListener.onClickItem(videoInfo.getPath(), videoInfo.getName(), position);
            }
        }
    }

}

class LocalMediaQuery extends AsyncQueryHandler {

    private List<VideoInfo> mVideoInfos;
    private RecyclerView.Adapter mAdapter;

    public LocalMediaQuery(ContentResolver cr, List<VideoInfo> list, RecyclerView.Adapter adapter) {
        super(cr);
        mVideoInfos = list;
        mAdapter = adapter;
    }

    public void startQuery() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        startQuery(0, null, uri, null, null, null, null);
    }


    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        if(cursor != null && cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                VideoInfo videoInfo = new VideoInfo();
                videoInfo.setPath(path);
                videoInfo.setName(name);
                mVideoInfos.add(videoInfo);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


}

class VideoInfo {
    String path;
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
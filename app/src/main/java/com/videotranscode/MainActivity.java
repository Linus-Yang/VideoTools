package com.videotranscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Space;
import android.widget.TextView;

import com.videotranscode.entity.VideoInfo;
import com.videotranscode.ui.VideoInfoAdapter;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");

    }

    private RecyclerView mRlView;
    private VideoInfoAdapter mVideoInfoAdapter;
    private HashMap<String, LinkedList<VideoInfo>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRlView = (RecyclerView) findViewById(R.id.rlView);
        mRlView.setLayoutManager(new LinearLayoutManager(this));
        mVideoInfoAdapter = new VideoInfoAdapter();
        map = new HashMap<>();

        LinkedList<VideoInfo> mVideoInfos = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            VideoInfo m = new VideoInfo();
            m.setName("video"+i);
            mVideoInfos.add(i, m);
        }

        map.put("mp4", mVideoInfos);
        mVideoInfoAdapter.setData(map);

        mRlView.setAdapter(mVideoInfoAdapter);

    }


    public native String stringFromJNI();
}

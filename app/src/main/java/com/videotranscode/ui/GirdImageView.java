package com.videotranscode.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.videotranscode.entity.VideoInfo;

import java.util.List;

/**
 * Created by linus on 2017/10/12.
 */

public class GirdImageView extends android.support.v7.widget.AppCompatImageView {

    public GirdImageView(Context context) {
        super(context);
    }

    public GirdImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GirdImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDataSource(String formatName, List<VideoInfo> videoInfos) {

    }


}

package com.video.transCode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.video.ui.LocalMediaView;
import com.videotranscode.R;

public class TransCodeActivity extends AppCompatActivity {

    private LocalMediaView mLocalMediaView;
    private TextView mInputText;
    private EditText mOutPathEt;
    private Button mStartBtn;
    private String mInputPath;
    private LocalMediaView.OnClickItemListener mInClickItemListener = new LocalMediaView.OnClickItemListener() {
        @Override
        public void onClickItem(String path, String name, int position) {
            mInputPath = path;
            mInputText.setText(path);
        }
    };
    private View.OnClickListener mInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] sPath = mInputPath.split("/");
            String gPath = sPath[sPath.length - 1];
            int index = mInputPath.indexOf(gPath);
            gPath = mInputPath.substring(0, index);
            String outPath = gPath + mOutPathEt.getText().toString();
            startTransCode(mInputPath , outPath);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcode);
        mLocalMediaView = (LocalMediaView) findViewById(R.id.lm_view);
        mInputText = (TextView) findViewById(R.id.tv_input);
        mOutPathEt = (EditText) findViewById(R.id.ed_out);
        mStartBtn = (Button) findViewById(R.id.bt_start);
        mStartBtn.setOnClickListener(mInClickListener);
        mLocalMediaView.setOnClickItemListener(mInClickItemListener);
        initFFMPEG();
    }

    public native void initFFMPEG();
    public native void startTransCode(String inputPath, String outPath);
}

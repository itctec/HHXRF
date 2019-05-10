package itc.ink.hhxrf.launching;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.VideoView;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.StatusBarUtil;

/**
 * Created by yangwenjiang on 2018/9/25.
 */

public class LaunchActivity extends BaseActivity {
    private final String LOG_TAG ="LaunchActivity";

    private VideoView launchVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_launch);

        launchVideoView=findViewById(R.id.launch_Activity_Video_VideoView);
        launchVideoView.setOnCompletionListener(new VideoCompletionListener());
        Uri launchVideoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.launch_video);
        launchVideoView.setVideoURI(launchVideoUri);
        launchVideoView.requestFocus();
        //launchVideoView.start();

        Intent intent=new Intent();
        intent.setClass(LaunchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }


    class VideoCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Intent intent=new Intent();
            intent.setClass(LaunchActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

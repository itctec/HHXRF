package itc.ink.hhxrf.home_fragment;

import android.os.Bundle;
import android.view.View;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class CameraActivity extends BaseActivity {
    CameraPreview cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.cameraView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume(this);
    }

    @Override
    protected void onPause() {
        cameraView.onPause();
        super.onPause();
    }

    public void onBackBtnClick(View view){
        finish();
    }

}

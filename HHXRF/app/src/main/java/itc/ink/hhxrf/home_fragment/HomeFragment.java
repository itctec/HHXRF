package itc.ink.hhxrf.home_fragment;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.DataCallBack;
import itc.ink.hhxrf.hardware.HardwareBroadCastReceiver;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class HomeFragment extends Fragment {
    private ImageView showLeftDrawerBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction("xray.Query");
        //getContext().registerReceiver(mDeviceReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        showLeftDrawerBtn=rootView.findViewById(R.id.home_Fragment_Top_Navigation_Show_Left_Drawer_Btn);

        MainActivity.hardwareBroadCastReceiver.addCallBack(new DataCallBack() {
            @Override
            public void onDataChanged(String s) {
                System.out.println("状态数据-》"+s);
                if(s.startsWith("B_")){
                    Intent intentTest=new Intent();
                    intentTest.setClass(getContext(), OnTestingActivity.class);
                    startActivityForResult(intentTest, MainActivity.TESTING_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.hardwareBroadCastReceiver.removeCallBack();
    }

    private final BroadcastReceiver mDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("扣动扳机");
            if ("xray.Query".equals(action)&&intent.getStringExtra("MachineStatus").startsWith("B_")) {
                Intent intentTest=new Intent();
                intentTest.setClass(getContext(), OnTestingActivity.class);
                startActivityForResult(intent, MainActivity.TESTING_ACTIVITY_REQUEST_CODE);
            }
        }
    };
}

package itc.ink.hhxrf.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HardwareBroadCastReceiver extends BroadcastReceiver {
    private String rcvString;
    public static DataCallBack mDataCallBack;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action_name = intent.getAction();
        rcvString= intent.getStringExtra("MachineStatus");
        //  Log.v("广播接收", "事件名称："+action_name);
        if(action_name.equals("xray.Answer")&&mDataCallBack!=null) {
            mDataCallBack.onDataChanged(rcvString);
        }
    }

    public void addCallBack(DataCallBack myCallBack){
        if(myCallBack!=null){
            mDataCallBack = myCallBack;
        }
    }

    public void removeCallBack(){
        mDataCallBack=null;
    }
}

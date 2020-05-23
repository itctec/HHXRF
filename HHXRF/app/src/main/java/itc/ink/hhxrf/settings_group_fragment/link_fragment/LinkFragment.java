package itc.ink.hhxrf.settings_group_fragment.link_fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.HistoryDBDataMode;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.HistoryDbDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.printer.IPrinterOpertion;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.printer.bluetooth.BluetoothOperation;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.permission.DynamicPermission;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class LinkFragment extends Fragment {
    private TextView bluetoothSwitchStateTv;
    private Switch bluetoothSwitch;
    private RecyclerView bluetoothDeviceRecyclerView;

    private List<LinkBluetoothDataMode> bluetoothDataModes=new ArrayList<>();
    private LinkBluetoothDataAdapter bluetoothDataAdapter;

    private Context context;
    public static boolean isConnected;
    public static PrinterInstance mPrinter;
    protected static IPrinterOpertion myOpertion;
    public static final int ENABLE_BT = 2;                  //启动蓝牙
    private ProgressDialog dialog;

    // Member fields
    BluetoothAdapter mBtAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);

        myOpertion = new BluetoothOperation(context, mHandler);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothDataAdapter=new LinkBluetoothDataAdapter(getContext(),bluetoothDataModes,myOpertion);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_link, container, false);

        bluetoothSwitchStateTv = rootView.findViewById(R.id.link_Fragment_Bt_Switch_State);
        bluetoothSwitch = rootView.findViewById(R.id.link_Fragment_Bt_Switch);
        bluetoothSwitch.setOnCheckedChangeListener(new BluetoothSwitchCheckChangeListener());

        bluetoothDeviceRecyclerView=rootView.findViewById(R.id.link_Fragment_Bluetooth_Device_List);
        bluetoothDeviceRecyclerView.setAdapter(bluetoothDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        bluetoothDeviceRecyclerView.setLayoutManager(contentRvLayoutManager);

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                LinkBluetoothDataMode bluetoothDataMode=new LinkBluetoothDataMode(device.getName()
                        + " ( "
                        + getResources()
                        .getText(
                                device.getBondState() == BluetoothDevice.BOND_BONDED ? R.string.has_paired
                                        : R.string.not_paired) + " )",device.getAddress());
                bluetoothDataModes.add(bluetoothDataMode);
                bluetoothDataAdapter.notifyDataSetChanged();
            }
        }

        if (mBtAdapter.isEnabled()) {
            bluetoothSwitch.setChecked(true);
            if (isConnected){
                bluetoothSwitchStateTv.setText("已连接");
            }
        }
        return rootView;
    }

    private void initDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Connecting");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void openConn() {
        myOpertion = new BluetoothOperation(context, mHandler);
        myOpertion.btAutoConn(context, mHandler);
    }

    private void doDiscovery() {
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, ENABLE_BT);
        } else {
            // Indicate scanning in the title
            getActivity().setProgressBarIndeterminateVisibility(true);

            // If we're already discovering, stop it
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }

            bluetoothDataModes.clear();
            // Request discover from BluetoothAdapter
            mBtAdapter.startDiscovery();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            System.out.println("message:" + info);
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.length() - 17);
            System.out.println("name:" + name);
            final Intent data = new Intent();
            data.putExtra(LinkFragment.EXTRA_DEVICE_ADDRESS, address);
            data.putExtra(LinkFragment.EXTRA_DEVICE_NAME, name);
            new Thread(new Runnable() {
                public void run() {
                    myOpertion.open(data);
                }
            }).start();
        }
    };

    private AdapterView.OnItemLongClickListener mDeviceLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // if return true, don't call method onCreateContextMenu
            return false;
        }
    };

    class BluetoothSwitchCheckChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                DynamicPermission dynamicPermission = new DynamicPermission();

                if (MainActivity.obtainAllPermissionSuccess == false && MainActivity.deniedPermissionList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    bluetoothSwitch.setChecked(false);
                    bluetoothSwitchStateTv.setText("未连接");
                    dynamicPermission.outService.showMissingPermissionDialog(getActivity(), "需要SD卡访问权限");
                } else if (MainActivity.obtainAllPermissionSuccess == false && MainActivity.deniedPermissionList.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    bluetoothSwitch.setChecked(false);
                    bluetoothSwitchStateTv.setText("未连接");
                    dynamicPermission.outService.showMissingPermissionDialog(getActivity(), "需要获取地理位置");
                } else {
                    initDialog();
                    if (!mBtAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, ENABLE_BT);
                    }else{
                        doDiscovery();
                    }
                }
            } else {
                if (isConnected) {        //如果已经连接了, 则断开
                    myOpertion.close();
                    myOpertion = null;
                    mPrinter = null;
                }
                if (mBtAdapter.isEnabled()) {
                    mBtAdapter.disable();
                }
            }
        }
    }

    //用于接受连接状态消息的 Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    bluetoothSwitch.setChecked(true);
                    bluetoothSwitchStateTv.setText("已连接");
                    mPrinter = myOpertion.getPrinter();
                    java.util.Timer timer = new Timer();
                    Toast.makeText(context, R.string.yesconn, Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    bluetoothSwitch.setChecked(true);
                    bluetoothSwitchStateTv.setText("未连接");
                    Toast.makeText(context, R.string.conn_failed, Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    bluetoothSwitch.setChecked(false);
                    bluetoothSwitchStateTv.setText("未连接");
                    Toast.makeText(context, R.string.conn_closed, Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    bluetoothSwitch.setChecked(true);
                    bluetoothSwitchStateTv.setText("未连接");
                    Toast.makeText(context, R.string.conn_no, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)&&isAdded()) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LinkBluetoothDataMode bluetoothDataMode=new LinkBluetoothDataMode(device.getName()
                        + " ( "
                        + getResources()
                        .getText(
                                device.getBondState() == BluetoothDevice.BOND_BONDED ? R.string.has_paired
                                        : R.string.not_paired) + " )",device.getAddress());
                bluetoothDataModes.add(bluetoothDataMode);
                bluetoothDataAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)&& isAdded()) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (bluetoothDataModes.size() == 0) {
                    LinkBluetoothDataMode bluetoothDataMode=new LinkBluetoothDataMode("未发现设备","");
                    bluetoothDataModes.add(bluetoothDataMode);
                    bluetoothDataAdapter.notifyDataSetChanged();
                }
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)&&isAdded()) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF://蓝牙已关闭
                        bluetoothSwitchStateTv.setText("未连接");
                        bluetoothDataModes.clear();
                        bluetoothDataAdapter.notifyDataSetChanged();
                        break;
                    case BluetoothAdapter.STATE_ON://蓝牙已开启
                        doDiscovery();
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.str_message)
                                .setMessage(R.string.str_connlast)
                                .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        openConn();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭
                        break;
                    default:
                        Log.e("BlueToothError", "蓝牙状态未知");
                }
            }
        }
    };

}

package itc.ink.hhxrf;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.hardware.HardwareBroadCastReceiver;
import itc.ink.hhxrf.home_fragment.CameraActivity;
import itc.ink.hhxrf.home_fragment.HomeFragment;
import itc.ink.hhxrf.home_fragment.OnTestingActivity;
import itc.ink.hhxrf.home_fragment.last_report.LastReportFragment;
import itc.ink.hhxrf.settings_group_fragment.SettingsGroupFragment;
import itc.ink.hhxrf.left_drawer.adapter.LeftDrawerWrapperDataAdapter;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerWrapperDataMode;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.LinkBluetoothDataMode;
import itc.ink.hhxrf.utils.StatusBarUtil;
import itc.ink.hhxrf.utils.permission.DynamicPermission;

public class MainActivity extends BaseActivity{
    private final String LOG_TAG = "MainActivity";
    public static boolean commandFromLeftDrawer=false;
    public static int currentSubFragmentID=0;
    public static final int FRAGMENT_ID_TEST_WAY=11;
    public static final int FRAGMENT_ID_EDIT_REPORT=12;
    public static final int FRAGMENT_ID_FORMAT=13;
    public static final int FRAGMENT_ID_ELEMENT=14;
    public static final int FRAGMENT_ID_COMPOUND=15;
    public static final int FRAGMENT_ID_UNIT=16;
    public static final int FRAGMENT_ID_DECIMAL_POINT=17;

    public static final int FRAGMENT_ID_HISTORY_DB=21;
    public static final int FRAGMENT_ID_CALIBRATION=22;
    public static final int FRAGMENT_ID_MARK_DB=23;

    public static final int FRAGMENT_ID_TEST_TIME=31;
    public static final int FRAGMENT_ID_PULL_TIME=32;
    public static final int FRAGMENT_ID_LANGUAGE=33;
    public static final int FRAGMENT_ID_LINK=34;
    public static final int FRAGMENT_ID_SAFE=35;
    public static final int FRAGMENT_ID_ABOUT=36;
    public static final int FRAGMENT_ID_HARDWARE_TEST=37;

    private DrawerLayout mainDrawerLayout;
    private RecyclerView leftDrawerContentRV;

    public static final int TESTING_ACTIVITY_REQUEST_CODE=0X01;
    public static final int TESTING_ACTIVITY_RESULT_CODE_OK=0X02;

    private TextView navigationHomeBtn;
    private TextView navigationResultBtn;
    private TextView navigationSettingsBtn;
    private TextView navigationDeviceBtn;

    public static final int TAB_HOME=0;
    public static final int TAB_RESULT=1;
    public static final int TAB_OPERATE=2;
    public static final int TAB_DEVICE=3;
    public static int currentTab=TAB_HOME;

    private DynamicPermission dynamicPermission;
    private String[] permissionsNeeded = null;
    private final int PERMISSION_REQUEST_CODE = 0X001;
    public static boolean obtainAllPermissionSuccess = false;
    public static ArrayList<String> deniedPermissionList = new ArrayList<>();

    public static HardwareBroadCastReceiver hardwareBroadCastReceiver=new HardwareBroadCastReceiver();
    public static Handler mHandler;

    static{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        mHandler=new MyHandler();

        setContentView(R.layout.activity_main);

        //Dynamic Apply Permission
        permissionsNeeded = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION};
        dynamicPermission = new DynamicPermission();
        obtainAllPermissionSuccess = dynamicPermission.outService.requestPermissions(MainActivity.this, PERMISSION_REQUEST_CODE, permissionsNeeded);
        if (obtainAllPermissionSuccess) {
            Log.d(LOG_TAG, "本页面已无权限需求限制");
        }

        mainDrawerLayout=findViewById(R.id.main_Drawer_Layout);


        //Left Drawer
        leftDrawerContentRV = findViewById(R.id.left_Drawer_Content_RecyclerView);
        List<LeftDrawerWrapperDataMode> leftDrawerData=prepareDrawerData(MainActivity.this);
        ItemClickCallbackIMP itemClickCallbackIMP=new ItemClickCallbackIMP();
        LeftDrawerWrapperDataAdapter leftDrawerDataAdapter = new LeftDrawerWrapperDataAdapter(MainActivity.this, leftDrawerData,itemClickCallbackIMP);
        leftDrawerContentRV.setAdapter(leftDrawerDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(MainActivity.this);
        leftDrawerContentRV.setLayoutManager(contentRvLayoutManager);

        //Main Content
        navigationHomeBtn=findViewById(R.id.bottom_Navigation_Home_Btn);
        navigationHomeBtn.setOnClickListener(new NavigationHomeBtnClickListener());
        navigationResultBtn=findViewById(R.id.bottom_Navigation_Result_Btn);
        navigationResultBtn.setOnClickListener(new NavigationResultBtnClickListener());
        navigationSettingsBtn=findViewById(R.id.bottom_Navigation_Settings_Btn);
        navigationSettingsBtn.setOnClickListener(new NavigationSettingsBtnClickListener());
        navigationDeviceBtn=findViewById(R.id.bottom_Navigation_Device_Setting_Btn);
        navigationDeviceBtn.setOnClickListener(new NavigationDeviceSettingBtnClickListener());

        HomeFragment homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, homeFragment).commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dynamicPermission.outService.hasPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            deniedPermissionList.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (dynamicPermission.outService.hasPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            deniedPermissionList.remove(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            deniedPermissionList.clear();
            obtainAllPermissionSuccess = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissionList.add(permissions[i]);
                    obtainAllPermissionSuccess = false;
                }
            }

            if (obtainAllPermissionSuccess) {
                Log.d(LOG_TAG, "已动态获取所有权限");
            } else {
                Log.d(LOG_TAG, "以下权限获取失败：\n" + deniedPermissionList.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("执行结束1");
        if(TESTING_ACTIVITY_REQUEST_CODE==requestCode&&TESTING_ACTIVITY_RESULT_CODE_OK==resultCode){
            System.out.println("执行结束2");
            LastReportFragment lastReportFragment = new LastReportFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, lastReportFragment).commit();
        }
    }

    public List<LeftDrawerWrapperDataMode> prepareDrawerData(Context mContext) {

        //Result settings data
        LeftDrawerSubDataMode test_way=new LeftDrawerSubDataMode(FRAGMENT_ID_TEST_WAY,getResources().getString(R.string.test_way),R.drawable.sub_item_test_way_icon_sel,R.drawable.sub_item_test_way_icon_unsel,1);
        LeftDrawerSubDataMode edit_report=new LeftDrawerSubDataMode(FRAGMENT_ID_EDIT_REPORT,getResources().getString(R.string.edit_report),R.drawable.sub_item_edit_report_icon_sel,R.drawable.sub_item_edit_report_icon_unsel,2);
        LeftDrawerSubDataMode format=new LeftDrawerSubDataMode(FRAGMENT_ID_FORMAT,getResources().getString(R.string.format),R.drawable.sub_item_format_icon_sel,R.drawable.sub_item_format_icon_unsel,3);
        LeftDrawerSubDataMode element=new LeftDrawerSubDataMode(FRAGMENT_ID_ELEMENT,getResources().getString(R.string.element),R.drawable.sub_item_element_icon_sel,R.drawable.sub_item_element_icon_unsel,4);
        LeftDrawerSubDataMode compound=new LeftDrawerSubDataMode(FRAGMENT_ID_COMPOUND,getResources().getString(R.string.compound),R.drawable.sub_item_compound_icon_sel,R.drawable.sub_item_compound_icon_unsel,5);
        LeftDrawerSubDataMode unit=new LeftDrawerSubDataMode(FRAGMENT_ID_UNIT,getResources().getString(R.string.unit),R.drawable.sub_item_unit_icon_sel,R.drawable.sub_item_unit_icon_unsel,6);
        LeftDrawerSubDataMode decimal_point=new LeftDrawerSubDataMode(FRAGMENT_ID_DECIMAL_POINT,getResources().getString(R.string.decimal_point),R.drawable.sub_item_decimal_point_icon_sel,R.drawable.sub_item_decimal_point_icon_unsel,7);
        List<LeftDrawerSubDataMode> resultSettingSubItemArray=new ArrayList<>();
        resultSettingSubItemArray.add(test_way);
        resultSettingSubItemArray.add(edit_report);
        resultSettingSubItemArray.add(format);
        resultSettingSubItemArray.add(element);
        resultSettingSubItemArray.add(compound);
        resultSettingSubItemArray.add(unit);
        resultSettingSubItemArray.add(decimal_point);
        LeftDrawerWrapperDataMode resultSettings=new LeftDrawerWrapperDataMode(getResources().getString(R.string.result_settings),resultSettingSubItemArray);

        LeftDrawerSubDataMode history_db=new LeftDrawerSubDataMode(FRAGMENT_ID_HISTORY_DB,getResources().getString(R.string.history_db),R.drawable.sub_item_history_icon_sel,R.drawable.sub_item_history_icon_unsel,1);
        LeftDrawerSubDataMode calibration=new LeftDrawerSubDataMode(FRAGMENT_ID_CALIBRATION,getResources().getString(R.string.calibration),R.drawable.sub_item_calibration_icon_sel,R.drawable.sub_item_calibration_icon_unsel,2);
        LeftDrawerSubDataMode mark_db=new LeftDrawerSubDataMode(FRAGMENT_ID_MARK_DB,getResources().getString(R.string.mark_db),R.drawable.sub_item_mark_icon_sel,R.drawable.sub_item_mark_icon_unsel,3);
        List<LeftDrawerSubDataMode> operateSettingSubItemArray=new ArrayList<>();
        operateSettingSubItemArray.add(history_db);
        operateSettingSubItemArray.add(calibration);
        operateSettingSubItemArray.add(mark_db);
        LeftDrawerWrapperDataMode operateSettings=new LeftDrawerWrapperDataMode(getResources().getString(R.string.operate_settings),operateSettingSubItemArray);

        LeftDrawerSubDataMode test_time=new LeftDrawerSubDataMode(FRAGMENT_ID_TEST_TIME,getResources().getString(R.string.test_time),R.drawable.sub_item_test_time_icon_sel,R.drawable.sub_item_test_time_icon_unsel,1);
        LeftDrawerSubDataMode pull_time=new LeftDrawerSubDataMode(FRAGMENT_ID_PULL_TIME,getResources().getString(R.string.pull_time),R.drawable.sub_item_pull_time_icon_sel,R.drawable.sub_item_pull_time_icon_unsel,2);
        LeftDrawerSubDataMode language=new LeftDrawerSubDataMode(FRAGMENT_ID_LANGUAGE,getResources().getString(R.string.language),R.drawable.sub_item_language_icon_unsel,R.drawable.sub_item_language_icon_unsel,3);
        LeftDrawerSubDataMode link=new LeftDrawerSubDataMode(FRAGMENT_ID_LINK,getResources().getString(R.string.link),R.drawable.sub_item_link_icon_sel,R.drawable.sub_item_link_icon_unsel,4);
        LeftDrawerSubDataMode safe=new LeftDrawerSubDataMode(FRAGMENT_ID_SAFE,getResources().getString(R.string.safe),R.drawable.sub_item_safe_icon_sel,R.drawable.sub_item_safe_icon_unsel,5);
        LeftDrawerSubDataMode about=new LeftDrawerSubDataMode(FRAGMENT_ID_ABOUT,getResources().getString(R.string.about),R.drawable.sub_item_state_icon_sel,R.drawable.sub_item_safe_icon_unsel,6);
        LeftDrawerSubDataMode instrument_debug=new LeftDrawerSubDataMode(FRAGMENT_ID_HARDWARE_TEST,getResources().getString(R.string.instrument_test),R.drawable.sub_item_debug_icon_sel,R.drawable.sub_item_debug_icon_unsel,7);
        List<LeftDrawerSubDataMode> systemSettingSubItemArray=new ArrayList<>();
        systemSettingSubItemArray.add(test_time);
        systemSettingSubItemArray.add(pull_time);
        systemSettingSubItemArray.add(language);
        systemSettingSubItemArray.add(link);
        systemSettingSubItemArray.add(safe);
        systemSettingSubItemArray.add(about);
        systemSettingSubItemArray.add(instrument_debug);
        LeftDrawerWrapperDataMode systemSettings=new LeftDrawerWrapperDataMode(getResources().getString(R.string.device_settings),systemSettingSubItemArray);

        //Drawer data
        List<LeftDrawerWrapperDataMode> drawerData=new ArrayList<>();
        drawerData.add(resultSettings);
        drawerData.add(operateSettings);
        drawerData.add(systemSettings);

        return drawerData;
    }

    private void updateBtnState(View currentActiveBtn) {
        navigationHomeBtn.setAlpha(0.5F);
        navigationHomeBtn.setBackground(null);
        navigationResultBtn.setAlpha(0.5F);
        navigationResultBtn.setBackground(null);
        navigationSettingsBtn.setAlpha(0.5F);
        navigationSettingsBtn.setBackground(null);
        navigationDeviceBtn.setAlpha(0.5F);
        navigationDeviceBtn.setBackground(null);

        currentActiveBtn.setAlpha(1F);
        currentActiveBtn.setBackgroundResource(R.drawable.navigation_icon_back);
    }

    class NavigationHomeBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            commandFromLeftDrawer=false;
            currentTab=TAB_HOME;
            HomeFragment homeFragment = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, homeFragment).commit();
            updateBtnState(view);
        }
    }

    class NavigationResultBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            commandFromLeftDrawer=false;
            currentTab=TAB_RESULT;
            SettingsGroupFragment settingsGroupFragment = new SettingsGroupFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, settingsGroupFragment).commit();
            updateBtnState(view);
        }
    }

    class NavigationSettingsBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            commandFromLeftDrawer=false;
            currentTab=TAB_OPERATE;
            SettingsGroupFragment settingsGroupFragment = new SettingsGroupFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, settingsGroupFragment).commit();
            updateBtnState(view);
        }
    }

    class NavigationDeviceSettingBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            commandFromLeftDrawer=false;
            currentTab=TAB_DEVICE;
            SettingsGroupFragment settingsGroupFragment = new SettingsGroupFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, settingsGroupFragment).commit();
            updateBtnState(view);
        }
    }


    public void onLeftDrawerShowBtnClick(View view){
        leftDrawerContentRV.scrollToPosition(0);
        mainDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void onLastReportBtnClick(View view){
        LastReportFragment lastReportFragment = new LastReportFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, lastReportFragment).commit();
    }

    public void onCameraBtnClick(View view){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    public void onStartNativeAnalyseBtnClick(View view){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, OnTestingActivity.class);
        startActivityForResult(intent,TESTING_ACTIVITY_REQUEST_CODE);
    }

    public void onLastReportBackBtnClick(View view){
        HomeFragment homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, homeFragment).commit();
    }

    class ItemClickCallbackIMP implements LeftDrawerWrapperDataAdapter.ItemClickCallback{
        @Override
        public void onItemClick(int fragmentID) {
            mainDrawerLayout.closeDrawer(Gravity.LEFT);
            commandFromLeftDrawer=true;
            currentSubFragmentID=fragmentID;
            SettingsGroupFragment settingsGroupFragment = new SettingsGroupFragment();
            switch (fragmentID/10){
                case 1:
                    currentTab=TAB_RESULT;
                    updateBtnState(navigationResultBtn);
                    break;
                case 2:
                    currentTab=TAB_OPERATE;
                    updateBtnState(navigationSettingsBtn);
                    break;
                case 3:
                    currentTab=TAB_DEVICE;
                    updateBtnState(navigationDeviceBtn);
                    break;
            }

            getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, settingsGroupFragment).commit();
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x01){
                Intent intentTest=new Intent();
                intentTest.setClass(MainActivity.this,OnTestingActivity.class);
                startActivityForResult(intentTest, MainActivity.TESTING_ACTIVITY_REQUEST_CODE);
            }else if(msg.what==0x02){
                updateBtnState(navigationHomeBtn);
            }
        }
    }

}

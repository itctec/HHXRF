package itc.ink.hhxrf;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import itc.ink.hhxrf.home_fragment.HomeFragment;
import itc.ink.hhxrf.home_fragment.last_report.LastReportFragment;
import itc.ink.hhxrf.left_drawer.adapter.LeftDrawerSubDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.SettingsGroupFragment;
import itc.ink.hhxrf.left_drawer.adapter.LeftDrawerWrapperDataAdapter;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerWrapperDataMode;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class MainActivity extends BaseActivity {
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
    public static final int FRAGMENT_ID_LANGUAGE=31;
    public static final int FRAGMENT_ID_PULL_TIME=32;
    public static final int FRAGMENT_ID_SAFE=33;
    public static final int FRAGMENT_ID_STATE=34;
    public static final int FRAGMENT_ID_DEBUG=35;
    public static final int FRAGMENT_ID_TEST_TIME=36;
    private DrawerLayout mainDrawerLayout;
    private RecyclerView leftDrawerContentRV;

    private TextView navigationHomeBtn;
    private TextView navigationResultBtn;
    private TextView navigationSettingsBtn;
    private TextView navigationDeviceBtn;

    public static final int TAB_HOME=0;
    public static final int TAB_RESULT=1;
    public static final int TAB_OPERATE=2;
    public static final int TAB_DEVICE=3;
    public static int currentTab=TAB_HOME;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_main);

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

    public List<LeftDrawerWrapperDataMode> prepareDrawerData(Context mContext) {

        //Result settings data
        LeftDrawerSubDataMode test_time=new LeftDrawerSubDataMode(FRAGMENT_ID_TEST_WAY,getResources().getString(R.string.test_way),R.drawable.sub_item_test_way_icon_sel,R.drawable.sub_item_test_way_icon_unsel,1);
        LeftDrawerSubDataMode test_way=new LeftDrawerSubDataMode(FRAGMENT_ID_EDIT_REPORT,getResources().getString(R.string.edit_report),R.drawable.sub_item_edit_report_icon_sel,R.drawable.sub_item_edit_report_icon_unsel,2);
        LeftDrawerSubDataMode unit=new LeftDrawerSubDataMode(FRAGMENT_ID_FORMAT,getResources().getString(R.string.format),R.drawable.sub_item_format_icon_sel,R.drawable.sub_item_format_icon_unsel,3);
        LeftDrawerSubDataMode edit_report=new LeftDrawerSubDataMode(FRAGMENT_ID_ELEMENT,getResources().getString(R.string.element),R.drawable.sub_item_element_icon_sel,R.drawable.sub_item_element_icon_unsel,4);
        LeftDrawerSubDataMode element=new LeftDrawerSubDataMode(FRAGMENT_ID_COMPOUND,getResources().getString(R.string.compound),R.drawable.sub_item_compound_icon_sel,R.drawable.sub_item_compound_icon_unsel,5);
        LeftDrawerSubDataMode compound=new LeftDrawerSubDataMode(FRAGMENT_ID_UNIT,getResources().getString(R.string.unit),R.drawable.sub_item_unit_icon_sel,R.drawable.sub_item_unit_icon_unsel,6);
        LeftDrawerSubDataMode format=new LeftDrawerSubDataMode(FRAGMENT_ID_DECIMAL_POINT,getResources().getString(R.string.decimal_point),R.drawable.sub_item_decimal_point_icon_sel,R.drawable.sub_item_decimal_point_icon_unsel,7);
        List<LeftDrawerSubDataMode> resultSettingSubItemArray=new ArrayList<>();
        resultSettingSubItemArray.add(test_time);
        resultSettingSubItemArray.add(test_way);
        resultSettingSubItemArray.add(unit);
        resultSettingSubItemArray.add(edit_report);
        resultSettingSubItemArray.add(element);
        resultSettingSubItemArray.add(compound);
        resultSettingSubItemArray.add(format);
        LeftDrawerWrapperDataMode resultSettings=new LeftDrawerWrapperDataMode(getResources().getString(R.string.result_settings),resultSettingSubItemArray);

        LeftDrawerSubDataMode history_db=new LeftDrawerSubDataMode(FRAGMENT_ID_HISTORY_DB,getResources().getString(R.string.history_db),R.drawable.sub_item_history_icon_sel,R.drawable.sub_item_history_icon_unsel,1);
        LeftDrawerSubDataMode calibration=new LeftDrawerSubDataMode(FRAGMENT_ID_CALIBRATION,getResources().getString(R.string.calibration),R.drawable.sub_item_calibration_icon_sel,R.drawable.sub_item_calibration_icon_unsel,2);
        LeftDrawerSubDataMode mark_db=new LeftDrawerSubDataMode(FRAGMENT_ID_MARK_DB,getResources().getString(R.string.mark_db),R.drawable.sub_item_mark_icon_sel,R.drawable.sub_item_mark_icon_unsel,3);
        List<LeftDrawerSubDataMode> operateSettingSubItemArray=new ArrayList<>();
        operateSettingSubItemArray.add(history_db);
        operateSettingSubItemArray.add(calibration);
        operateSettingSubItemArray.add(mark_db);
        LeftDrawerWrapperDataMode operateSettings=new LeftDrawerWrapperDataMode(getResources().getString(R.string.operate_settings),operateSettingSubItemArray);

        LeftDrawerSubDataMode language=new LeftDrawerSubDataMode(FRAGMENT_ID_LANGUAGE,getResources().getString(R.string.language),R.drawable.sub_item_language_icon_unsel,R.drawable.sub_item_language_icon_unsel,1);
        LeftDrawerSubDataMode pull_time=new LeftDrawerSubDataMode(FRAGMENT_ID_PULL_TIME,getResources().getString(R.string.pull_time),R.drawable.sub_item_pull_time_icon_sel,R.drawable.sub_item_pull_time_icon_unsel,2);
        LeftDrawerSubDataMode safe=new LeftDrawerSubDataMode(FRAGMENT_ID_SAFE,getResources().getString(R.string.safe),R.drawable.sub_item_safe_icon_sel,R.drawable.sub_item_safe_icon_unsel,3);
        LeftDrawerSubDataMode power_off_time=new LeftDrawerSubDataMode(FRAGMENT_ID_STATE,getResources().getString(R.string.state),R.drawable.sub_item_state_icon_sel,R.drawable.sub_item_safe_icon_unsel,4);
        LeftDrawerSubDataMode instrument_debug=new LeftDrawerSubDataMode(FRAGMENT_ID_DEBUG,getResources().getString(R.string.instrument_debug),R.drawable.sub_item_debug_icon_sel,R.drawable.sub_item_debug_icon_unsel,5);
        LeftDrawerSubDataMode decimal_point=new LeftDrawerSubDataMode(FRAGMENT_ID_TEST_TIME,getResources().getString(R.string.test_time),R.drawable.sub_item_test_time_icon_sel,R.drawable.sub_item_test_time_icon_unsel,6);
        List<LeftDrawerSubDataMode> systemSettingSubItemArray=new ArrayList<>();
        systemSettingSubItemArray.add(language);
        systemSettingSubItemArray.add(pull_time);
        systemSettingSubItemArray.add(power_off_time);
        systemSettingSubItemArray.add(safe);
        systemSettingSubItemArray.add(instrument_debug);
        systemSettingSubItemArray.add(decimal_point);
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
        mainDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void onLastReportBtnClick(View view){
        LastReportFragment lastReportFragment = new LastReportFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_Activity_Fragment_Container, lastReportFragment).commit();
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
}

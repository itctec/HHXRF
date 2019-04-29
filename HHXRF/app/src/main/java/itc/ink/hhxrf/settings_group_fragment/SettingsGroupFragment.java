package itc.ink.hhxrf.settings_group_fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.settings_group_fragment.adapter.SettingsGroupIndicatorDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.calibration.CalibrationFragment;
import itc.ink.hhxrf.settings_group_fragment.compound_fragment.CompoundFragment;
import itc.ink.hhxrf.settings_group_fragment.decimal_point_fragment.DecimalPointFragment;
import itc.ink.hhxrf.settings_group_fragment.edit_report_fragment.EditReportFragment;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementFragment;
import itc.ink.hhxrf.settings_group_fragment.format_fragment.FormatFragment;
import itc.ink.hhxrf.settings_group_fragment.history_db.HistoryFragment;
import itc.ink.hhxrf.settings_group_fragment.test_time.TestTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.settings_group_fragment.unit.UnitFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.FragmentIndicatorSimpleItemTouchCallback;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class SettingsGroupFragment extends Fragment{
    private RecyclerView settingsIndicatorRecyclerView;
    private SettingsGroupIndicatorDataAdapter mSettingsGroupDataAdapter;
    private List<LeftDrawerSubDataMode> mSettingsGroupListData;
    private ItemTouchHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.currentTab==MainActivity.TAB_RESULT){
            mSettingsGroupListData=prepareResultSettingsData();
        }else if(MainActivity.currentTab==MainActivity.TAB_OPERATE){
            mSettingsGroupListData=prepareOperateSettingsData();
        }else if(MainActivity.currentTab==MainActivity.TAB_DEVICE){
            mSettingsGroupListData=prepareSystemSettingsData();
        }

        ItemClickCallbackIMP itemClickCallbackIMP=new ItemClickCallbackIMP();
        mSettingsGroupDataAdapter=new SettingsGroupIndicatorDataAdapter(getContext(), mSettingsGroupListData,itemClickCallbackIMP);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_group, container, false);

        TextView tabTitle=rootView.findViewById(R.id.settings_Group_Top_Navigation_Tab_Title);
        if(MainActivity.currentTab==MainActivity.TAB_RESULT){
            tabTitle.setText(getText(R.string.result_settings));
        }else if(MainActivity.currentTab==MainActivity.TAB_OPERATE){
            tabTitle.setText(getText(R.string.operate_settings));
        }else if(MainActivity.currentTab==MainActivity.TAB_DEVICE){
            tabTitle.setText(getText(R.string.device_settings));
        }

        settingsIndicatorRecyclerView = rootView.findViewById(R.id.settings_Indicator_RecyclerView);
        settingsIndicatorRecyclerView.setAdapter(mSettingsGroupDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        settingsIndicatorRecyclerView.setLayoutManager(contentRvLayoutManager);
        mHelper = new ItemTouchHelper(new FragmentIndicatorSimpleItemTouchCallback(getContext(),mSettingsGroupDataAdapter, mSettingsGroupListData));
        mHelper.attachToRecyclerView(settingsIndicatorRecyclerView);

        if(MainActivity.commandFromLeftDrawer){
            changeFragment(MainActivity.currentSubFragmentID);
        }else{
            changeFragment(mSettingsGroupListData.get(0).getItem_id());
        }

        return rootView;
    }


    public int checkItemRank(int item_id){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select rank_num from tb_fragment_rank_info where item_id=?";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{item_id+""});
        cursor.moveToNext();
        return Integer.parseInt(cursor.getString(0));
    }

    public List<LeftDrawerSubDataMode> prepareResultSettingsData() {
        LeftDrawerSubDataMode test_time=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_TEST_WAY,getResources().getString(R.string.test_way),R.drawable.sub_item_test_way_icon_sel,R.drawable.sub_item_test_way_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_TEST_WAY));
        LeftDrawerSubDataMode test_way=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_EDIT_REPORT,getResources().getString(R.string.edit_report),R.drawable.sub_item_edit_report_icon_sel,R.drawable.sub_item_edit_report_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_EDIT_REPORT));
        LeftDrawerSubDataMode unit=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_FORMAT,getResources().getString(R.string.format),R.drawable.sub_item_format_icon_sel,R.drawable.sub_item_format_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_FORMAT));
        LeftDrawerSubDataMode edit_report=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_ELEMENT,getResources().getString(R.string.element),R.drawable.sub_item_element_icon_sel,R.drawable.sub_item_element_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_ELEMENT));
        LeftDrawerSubDataMode element=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_COMPOUND,getResources().getString(R.string.compound),R.drawable.sub_item_compound_icon_sel,R.drawable.sub_item_compound_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_COMPOUND));
        LeftDrawerSubDataMode compound=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_UNIT,getResources().getString(R.string.unit),R.drawable.sub_item_unit_icon_sel,R.drawable.sub_item_unit_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_UNIT));
        LeftDrawerSubDataMode format=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_DECIMAL_POINT,getResources().getString(R.string.decimal_point),R.drawable.sub_item_decimal_point_icon_sel,R.drawable.sub_item_decimal_point_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_DECIMAL_POINT));
        LeftDrawerSubDataMode decimal_point=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_TEST_TIME,getResources().getString(R.string.test_time),R.drawable.sub_item_test_time_icon_sel,R.drawable.sub_item_test_time_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_TEST_TIME));
        List<LeftDrawerSubDataMode> resultSettingSubItemArray=new ArrayList<>();
        resultSettingSubItemArray.add(test_time);
        resultSettingSubItemArray.add(test_way);
        resultSettingSubItemArray.add(unit);
        resultSettingSubItemArray.add(edit_report);
        resultSettingSubItemArray.add(element);
        resultSettingSubItemArray.add(compound);
        resultSettingSubItemArray.add(format);
        resultSettingSubItemArray.add(decimal_point);

        Collections.sort(resultSettingSubItemArray, new Comparator<LeftDrawerSubDataMode>() {

            @Override
            public int compare(LeftDrawerSubDataMode o1, LeftDrawerSubDataMode o2) {
                if(o1.getRank_num()>o2.getRank_num()){
                    return 1;
                }else if(o1.getRank_num()==o2.getRank_num()){
                    return 0;
                }else{
                    return -1;
                }
            }

        });

        return resultSettingSubItemArray;
    }

    public List<LeftDrawerSubDataMode> prepareOperateSettingsData() {
        LeftDrawerSubDataMode history_db=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_HISTORY_DB,getResources().getString(R.string.history_db),R.drawable.sub_item_history_icon_sel,R.drawable.sub_item_history_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_HISTORY_DB));
        LeftDrawerSubDataMode calibration=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_CALIBRATION,getResources().getString(R.string.calibration),R.drawable.sub_item_calibration_icon_sel,R.drawable.sub_item_calibration_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_CALIBRATION));
        LeftDrawerSubDataMode mark_db=new LeftDrawerSubDataMode(MainActivity.FRAGMENT_ID_MARK_DB,getResources().getString(R.string.mark_db),R.drawable.sub_item_mark_icon_sel,R.drawable.sub_item_mark_icon_unsel,checkItemRank(MainActivity.FRAGMENT_ID_MARK_DB));
        List<LeftDrawerSubDataMode> operateSettingSubItemArray=new ArrayList<>();
        operateSettingSubItemArray.add(history_db);
        operateSettingSubItemArray.add(calibration);
        operateSettingSubItemArray.add(mark_db);

        Collections.sort(operateSettingSubItemArray, new Comparator<LeftDrawerSubDataMode>() {

            @Override
            public int compare(LeftDrawerSubDataMode o1, LeftDrawerSubDataMode o2) {
                if(o1.getRank_num()>o2.getRank_num()){
                    return 1;
                }else if(o1.getRank_num()==o2.getRank_num()){
                    return 0;
                }else{
                    return -1;
                }
            }

        });

        return operateSettingSubItemArray;
    }

    public List<LeftDrawerSubDataMode> prepareSystemSettingsData() {
        LeftDrawerSubDataMode language=new LeftDrawerSubDataMode(31,getResources().getString(R.string.language),R.drawable.vector_drawable_recommend,R.drawable.vector_drawable_recommend,checkItemRank(31));
        LeftDrawerSubDataMode pull_time=new LeftDrawerSubDataMode(32,getResources().getString(R.string.pull_time),R.drawable.vector_drawable_recommend,R.drawable.vector_drawable_recommend,checkItemRank(32));
        LeftDrawerSubDataMode power_off_time=new LeftDrawerSubDataMode(33,getResources().getString(R.string.power_off_time),R.drawable.vector_drawable_recommend,R.drawable.vector_drawable_recommend,checkItemRank(33));
        LeftDrawerSubDataMode safe=new LeftDrawerSubDataMode(34,getResources().getString(R.string.safe),R.drawable.vector_drawable_recommend,R.drawable.vector_drawable_recommend,checkItemRank(34));
        LeftDrawerSubDataMode instrument_debug=new LeftDrawerSubDataMode(35,getResources().getString(R.string.instrument_debug),R.drawable.vector_drawable_recommend,R.drawable.vector_drawable_recommend,checkItemRank(35));
        List<LeftDrawerSubDataMode> systemSettingSubItemArray=new ArrayList<>();
        systemSettingSubItemArray.add(language);
        systemSettingSubItemArray.add(pull_time);
        systemSettingSubItemArray.add(power_off_time);
        systemSettingSubItemArray.add(safe);
        systemSettingSubItemArray.add(instrument_debug);

        Collections.sort(systemSettingSubItemArray, new Comparator<LeftDrawerSubDataMode>() {

            @Override
            public int compare(LeftDrawerSubDataMode o1, LeftDrawerSubDataMode o2) {
                if(o1.getRank_num()>o2.getRank_num()){
                    return 1;
                }else if(o1.getRank_num()==o2.getRank_num()){
                    return 0;
                }else{
                    return -1;
                }
            }

        });

        return systemSettingSubItemArray;
    }

    class ItemClickCallbackIMP implements SettingsGroupIndicatorDataAdapter.ItemClickCallback{
        @Override
        public void onItemClick(int fragmentID) {
            changeFragment(fragmentID);
        }
    }

    public void changeFragment(int fragmentID){
        int itemRankPosition=checkItemRank(fragmentID)%10-1;
        settingsIndicatorRecyclerView.scrollToPosition(itemRankPosition);

        switch (fragmentID){
            case MainActivity.FRAGMENT_ID_TEST_WAY:
                TestWayFragment testWayFragment = new TestWayFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, testWayFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_EDIT_REPORT:
                EditReportFragment editReportFragment = new EditReportFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, editReportFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_FORMAT:
                FormatFragment formatFragment = new FormatFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, formatFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_ELEMENT:
                ElementFragment elementFragment=new ElementFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, elementFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_COMPOUND:
                CompoundFragment compoundFragment=new CompoundFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, compoundFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_UNIT:
                UnitFragment unitFragment=new UnitFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, unitFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_DECIMAL_POINT:
                DecimalPointFragment decimalPointFragment=new DecimalPointFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, decimalPointFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_TEST_TIME:
                TestTimeFragment testTimeFragment=new TestTimeFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, testTimeFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_HISTORY_DB:
                HistoryFragment historyFragment=new HistoryFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, historyFragment).commit();
                break;
            case MainActivity.FRAGMENT_ID_CALIBRATION:
                CalibrationFragment calibrationFragment=new CalibrationFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.settings_Fragment_Container, calibrationFragment).commit();
                break;
        }
    }

}


package itc.ink.hhxrf.settings_group_fragment.test_way_fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.pull_time_fragment.PullTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_time_fragment.TestTimeFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class TestWayFragment extends Fragment {
    public static final String TEST_WAY_KEY="test_way";
    public static final String TEST_WAY_VALUE_METAL="Metal";
    public static final String TEST_WAY_VALUE_GROUND="Soil";

    private ImageView testWayItemBack;
    private TextView testWayMetalBtnTitle;
    private TextView testWayMetalBtnSelectedTip;
    private TextView testWayGroundBtnTitle;
    private TextView testWayGroundBtnSelectedTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_way, container, false);

        ConstraintLayout testWaySwitchBtn=rootView.findViewById(R.id.test_Way_Fragment_Item_Switch_Layout_Btn);
        testWaySwitchBtn.setOnClickListener(new TestWaySwitchBtnClickListener());

        testWayItemBack=rootView.findViewById(R.id.test_Way_Fragment_Item_Btn_Back);
        testWayMetalBtnTitle=rootView.findViewById(R.id.test_Way_Fragment_Metal_Btn_Title);
        testWayMetalBtnSelectedTip=rootView.findViewById(R.id.test_Way_Fragment_Metal_Btn_Selected_Tip);
        testWayGroundBtnTitle=rootView.findViewById(R.id.test_Way_Fragment_Ground_Btn_Title);
        testWayGroundBtnSelectedTip=rootView.findViewById(R.id.test_Way_Fragment_Ground_Btn_Selected_Tip);

        if(SharedPreferenceUtil.getString(TEST_WAY_KEY,TEST_WAY_VALUE_METAL).equals(TEST_WAY_VALUE_METAL)){
            ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
            lp.leftToLeft= ConstraintLayout.LayoutParams.PARENT_ID;
            lp.rightToRight=ConstraintLayout.LayoutParams.UNSET;
            testWayItemBack.setLayoutParams(lp);
            testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
            testWayMetalBtnSelectedTip.setVisibility(View.VISIBLE);
            testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
            testWayGroundBtnSelectedTip.setVisibility(View.GONE);
        }else{
            ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
            lp.rightToRight= ConstraintLayout.LayoutParams.PARENT_ID;
            lp.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
            testWayItemBack.setLayoutParams(lp);
            testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
            testWayMetalBtnSelectedTip.setVisibility(View.GONE);
            testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
            testWayGroundBtnSelectedTip.setVisibility(View.VISIBLE);
        }
        Intent intent = new Intent();
        intent.putExtra("TrigMode",SharedPreferenceUtil.getString(PullTimeFragment.PULL_TIME_KEY,PullTimeFragment.PULL_TIME_VALUE_SHORT));//短按Short   长按Long
        intent.putExtra("period",SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15));
        intent.putExtra("MaterialType",SharedPreferenceUtil.getString(TEST_WAY_KEY,TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
        intent.setAction("xray.information");
        intent.setComponent(new ComponentName("com.example.androidjnitest","com.example.androidjnitest.BroadcastReceiver1"));
        getContext().sendBroadcast(intent);

        return rootView;
    }

    class TestWaySwitchBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(SharedPreferenceUtil.getString(TEST_WAY_KEY,TEST_WAY_VALUE_METAL).equals(TEST_WAY_VALUE_METAL)){
                SharedPreferenceUtil.putString(TEST_WAY_KEY,TEST_WAY_VALUE_GROUND);
                ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
                lp.rightToRight= ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
                testWayItemBack.setLayoutParams(lp);
                testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
                testWayMetalBtnSelectedTip.setVisibility(View.GONE);
                testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
                testWayGroundBtnSelectedTip.setVisibility(View.VISIBLE);
            }else{
                SharedPreferenceUtil.putString(TEST_WAY_KEY,TEST_WAY_VALUE_METAL);
                ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
                lp.leftToLeft= ConstraintLayout.LayoutParams.PARENT_ID;
                lp.rightToRight=ConstraintLayout.LayoutParams.UNSET;
                testWayItemBack.setLayoutParams(lp);
                testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
                testWayMetalBtnSelectedTip.setVisibility(View.VISIBLE);
                testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
                testWayGroundBtnSelectedTip.setVisibility(View.GONE);
            }
            Intent intent = new Intent();
            intent.putExtra("TrigMode",SharedPreferenceUtil.getString(PullTimeFragment.PULL_TIME_KEY,PullTimeFragment.PULL_TIME_VALUE_SHORT));//短按Short   长按Long
            intent.putExtra("period",SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15));
            intent.putExtra("MaterialType",SharedPreferenceUtil.getString(TEST_WAY_KEY,TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
            intent.setAction("xray.information");
            intent.setComponent(new ComponentName("com.example.androidjnitest","com.example.androidjnitest.BroadcastReceiver1"));
            getContext().sendBroadcast(intent);
        }
    }

}

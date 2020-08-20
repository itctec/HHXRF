package itc.ink.hhxrf.settings_group_fragment.pull_time_fragment;

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
import itc.ink.hhxrf.settings_group_fragment.test_time_fragment.TestTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class PullTimeFragment extends Fragment {
    public static final String PULL_TIME_KEY = "PULL_TIME";
    public static final String PULL_TIME_VALUE_SHORT = "Short";
    public static final String PULL_TIME_VALUE_LONG = "Long";

    private ImageView pull_TimeItemBack;
    private TextView pull_TimeShortBtnTitle;
    private TextView pull_TimeShortBtnSelectedTip;
    private TextView pull_TimeLongBtnTitle;
    private TextView pull_TimeLongBtnSelectedTip;

    private View itemOneBtn;
    private View itemTwoBtn;
    private boolean ready=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pull_time, container, false);

        /*ConstraintLayout pull_TimeSwitchBtn = rootView.findViewById(R.id.pull_Time_Fragment_Item_Switch_Layout_Btn);
        pull_TimeSwitchBtn.setOnClickListener(new TestWaySwitchBtnClickListener());*/

        pull_TimeItemBack = rootView.findViewById(R.id.pull_Time_Fragment_Item_Btn_Back);
        pull_TimeShortBtnTitle = rootView.findViewById(R.id.pull_Time_Fragment_Short_Btn_Title);
        pull_TimeShortBtnSelectedTip = rootView.findViewById(R.id.pull_Time_Fragment_Short_Btn_Selected_Tip);
        pull_TimeLongBtnTitle = rootView.findViewById(R.id.pull_Time_Fragment_Long_Btn_Title);
        pull_TimeLongBtnSelectedTip = rootView.findViewById(R.id.pull_Time_Fragment_Long_Btn_Selected_Tip);

        PullTimeSwitchBtnClickListener pullTimeSwitchBtnClickListener=new PullTimeSwitchBtnClickListener();

        itemOneBtn=rootView.findViewById(R.id.pull_Time_Fragment_Item_One_Btn);
        itemOneBtn.setOnClickListener(pullTimeSwitchBtnClickListener);
        itemTwoBtn=rootView.findViewById(R.id.pull_Time_Fragment_Item_Two_Btn);
        itemTwoBtn.setOnClickListener(pullTimeSwitchBtnClickListener);

        if (SharedPreferenceUtil.getString(PULL_TIME_KEY,PULL_TIME_VALUE_SHORT) == PULL_TIME_VALUE_SHORT) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
            lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp.rightToRight = ConstraintLayout.LayoutParams.UNSET;
            pull_TimeItemBack.setLayoutParams(lp);
            pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
            pull_TimeShortBtnSelectedTip.setVisibility(View.VISIBLE);
            pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
            pull_TimeLongBtnSelectedTip.setVisibility(View.GONE);
        } else {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
            lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            lp.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
            pull_TimeItemBack.setLayoutParams(lp);
            pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
            pull_TimeShortBtnSelectedTip.setVisibility(View.GONE);
            pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
            pull_TimeLongBtnSelectedTip.setVisibility(View.VISIBLE);
        }
        Intent intent = new Intent();
        intent.putExtra("TrigMode",SharedPreferenceUtil.getString(PULL_TIME_KEY,PULL_TIME_VALUE_SHORT));//短按Short   长按Long
        intent.putExtra("period",SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15));
        intent.putExtra("MaterialType",SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY,TestWayFragment.TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
        intent.setAction("xray.information");
        intent.setComponent(new ComponentName("com.example.androidjnitest","com.example.androidjnitest.BroadcastReceiver1"));
       // getContext().sendBroadcast(intent);

        return rootView;
    }

    class PullTimeSwitchBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (ready) {
                ready = false;
                if (view.equals(itemTwoBtn) && !SharedPreferenceUtil.getString(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT).equals(PULL_TIME_VALUE_LONG)) {
                    SharedPreferenceUtil.putString(PULL_TIME_KEY, PULL_TIME_VALUE_LONG);
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
                    lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                    lp.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                    pull_TimeItemBack.setLayoutParams(lp);
                    pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                    pull_TimeShortBtnSelectedTip.setVisibility(View.GONE);
                    pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                    pull_TimeLongBtnSelectedTip.setVisibility(View.VISIBLE);

                    Intent intent = new Intent();
                    intent.putExtra("TrigMode", SharedPreferenceUtil.getString(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT));
                    intent.putExtra("period", SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY, 15));
                    intent.putExtra("MaterialType", SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY, TestWayFragment.TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
                    intent.setAction("xray.information");
                    intent.setComponent(new ComponentName("com.example.androidjnitest", "com.example.androidjnitest.BroadcastReceiver1"));
                    getContext().sendBroadcast(intent);
                } else if (view.equals(itemOneBtn) && !SharedPreferenceUtil.getString(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT).equals(PULL_TIME_VALUE_SHORT)) {
                    SharedPreferenceUtil.putString(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT);
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
                    lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                    lp.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                    pull_TimeItemBack.setLayoutParams(lp);
                    pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                    pull_TimeShortBtnSelectedTip.setVisibility(View.VISIBLE);
                    pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                    pull_TimeLongBtnSelectedTip.setVisibility(View.GONE);

                    Intent intent = new Intent();
                    intent.putExtra("TrigMode", SharedPreferenceUtil.getString(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT));
                    intent.putExtra("period", SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY, 15));
                    intent.putExtra("MaterialType", SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY, TestWayFragment.TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
                    intent.setAction("xray.information");
                    intent.setComponent(new ComponentName("com.example.androidjnitest", "com.example.androidjnitest.BroadcastReceiver1"));
                    getContext().sendBroadcast(intent);
                }
                new Thread(SetValueThread).start();
            }
        }
    }


    Thread SetValueThread =new Thread(){

        @Override
        public void run() {
            super.run();
            try{
                Thread.sleep(1000);
                ready=true;
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }
    };

}

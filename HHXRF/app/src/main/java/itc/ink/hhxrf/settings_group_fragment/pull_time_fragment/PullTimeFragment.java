package itc.ink.hhxrf.settings_group_fragment.pull_time_fragment;

import android.app.Fragment;
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
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class PullTimeFragment extends Fragment {
    public static final String PULL_TIME_KEY = "PULL_TIME";
    public static final int PULL_TIME_VALUE_SHORT = 0;
    public static final int PULL_TIME_VALUE_LONG = 1;

    private ImageView pull_TimeItemBack;
    private TextView pull_TimeShortBtnTitle;
    private TextView pull_TimeShortBtnSelectedTip;
    private TextView pull_TimeLongBtnTitle;
    private TextView pull_TimeLongBtnSelectedTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pull_time, container, false);

        ConstraintLayout pull_TimeSwitchBtn = rootView.findViewById(R.id.pull_Time_Fragment_Item_Switch_Layout_Btn);
        pull_TimeSwitchBtn.setOnClickListener(new TestWaySwitchBtnClickListener());

        pull_TimeItemBack = rootView.findViewById(R.id.pull_Time_Fragment_Item_Btn_Back);
        pull_TimeShortBtnTitle = rootView.findViewById(R.id.pull_Time_Fragment_Short_Btn_Title);
        pull_TimeShortBtnSelectedTip = rootView.findViewById(R.id.pull_Time_Fragment_Short_Btn_Selected_Tip);
        pull_TimeLongBtnTitle = rootView.findViewById(R.id.pull_Time_Fragment_Long_Btn_Title);
        pull_TimeLongBtnSelectedTip = rootView.findViewById(R.id.pull_Time_Fragment_Long_Btn_Selected_Tip);


        if (SharedPreferenceUtil.getInt(PULL_TIME_KEY) == PULL_TIME_VALUE_SHORT) {
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

        return rootView;
    }

    class TestWaySwitchBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (SharedPreferenceUtil.getInt(PULL_TIME_KEY) == PULL_TIME_VALUE_SHORT) {
                SharedPreferenceUtil.putInt(PULL_TIME_KEY, PULL_TIME_VALUE_LONG);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
                lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                pull_TimeItemBack.setLayoutParams(lp);
                pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                pull_TimeShortBtnSelectedTip.setVisibility(View.GONE);
                pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                pull_TimeLongBtnSelectedTip.setVisibility(View.VISIBLE);
            } else {
                SharedPreferenceUtil.putInt(PULL_TIME_KEY, PULL_TIME_VALUE_SHORT);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) pull_TimeItemBack.getLayoutParams();
                lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                pull_TimeItemBack.setLayoutParams(lp);
                pull_TimeShortBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                pull_TimeShortBtnSelectedTip.setVisibility(View.VISIBLE);
                pull_TimeLongBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                pull_TimeLongBtnSelectedTip.setVisibility(View.GONE);
            }
        }
    }

}

package itc.ink.hhxrf.settings_group_fragment.test_way_fragment;

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

public class TestWayFragment extends Fragment {
    public static final String TEST_WAY_KEY="test_way";
    public static final int TEST_WAY_VALUE_METAL=0;
    public static final int TEST_WAY_VALUE_GROUND=1;

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


        if(SharedPreferenceUtil.getInt(TEST_WAY_KEY)==TEST_WAY_VALUE_METAL){
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

        return rootView;
    }

    class TestWaySwitchBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(SharedPreferenceUtil.getInt(TEST_WAY_KEY)==TEST_WAY_VALUE_METAL){
                SharedPreferenceUtil.putInt(TEST_WAY_KEY,TEST_WAY_VALUE_GROUND);
                ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
                lp.rightToRight= ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
                testWayItemBack.setLayoutParams(lp);
                testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
                testWayMetalBtnSelectedTip.setVisibility(View.GONE);
                testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
                testWayGroundBtnSelectedTip.setVisibility(View.VISIBLE);
            }else{
                SharedPreferenceUtil.putInt(TEST_WAY_KEY,TEST_WAY_VALUE_METAL);
                ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)testWayItemBack.getLayoutParams();
                lp.leftToLeft= ConstraintLayout.LayoutParams.PARENT_ID;
                lp.rightToRight=ConstraintLayout.LayoutParams.UNSET;
                testWayItemBack.setLayoutParams(lp);
                testWayMetalBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black,null));
                testWayMetalBtnSelectedTip.setVisibility(View.VISIBLE);
                testWayGroundBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white,null));
                testWayGroundBtnSelectedTip.setVisibility(View.GONE);
            }
        }
    }

}

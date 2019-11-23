package itc.ink.hhxrf.settings_group_fragment.edit_report_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class EditReportFragment extends Fragment {
    private ConstraintLayout templateOneLayoutBtn;
    private TextView templateOneLayoutBtnTitle;
    private TextView templateOneLayoutBtnSelTip;
    private ConstraintLayout templateTwoLayoutBtn;
    private TextView templateTwoLayoutBtnTitle;
    private TextView templateTwoLayoutBtnSelTip;
    private ConstraintLayout templateThreeLayoutBtn;
    private TextView templateThreeLayoutBtnTitle;
    private TextView templateThreeLayoutBtnSelTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_report, container, false);

        templateOneLayoutBtn=rootView.findViewById(R.id.edit_Report_Fragment_Template_One_Layout_Btn);
        templateOneLayoutBtn.setOnClickListener(new TemplateOneLayoutBtnClickListener());
        templateOneLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_One_Title);
        templateOneLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_One_Selected_Tip);

        templateTwoLayoutBtn=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Layout_Btn);
        templateTwoLayoutBtn.setOnClickListener(new TemplateTwoLayoutBtnClickListener());
        templateTwoLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Title);
        templateTwoLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Selected_Tip);

        templateThreeLayoutBtn=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Layout_Btn);
        templateThreeLayoutBtn.setOnClickListener(new TemplateThreeLayoutBtnClickListener());
        templateThreeLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Title);
        templateThreeLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Selected_Tip);

        return rootView;
    }

    class TemplateOneLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            templateOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            templateOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            templateOneLayoutBtnSelTip.setVisibility(View.VISIBLE);

            templateTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateTwoLayoutBtnSelTip.setVisibility(View.GONE);

            templateThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateThreeLayoutBtnSelTip.setVisibility(View.GONE);
        }
    }

    class TemplateTwoLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            templateOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateOneLayoutBtnSelTip.setVisibility(View.GONE);

            templateTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            templateTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            templateTwoLayoutBtnSelTip.setVisibility(View.VISIBLE);

            templateThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateThreeLayoutBtnSelTip.setVisibility(View.GONE);
        }
    }

    class TemplateThreeLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            templateOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateOneLayoutBtnSelTip.setVisibility(View.GONE);

            templateTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            templateTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            templateTwoLayoutBtnSelTip.setVisibility(View.GONE);

            templateThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            templateThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            templateThreeLayoutBtnSelTip.setVisibility(View.VISIBLE);
        }
    }




}

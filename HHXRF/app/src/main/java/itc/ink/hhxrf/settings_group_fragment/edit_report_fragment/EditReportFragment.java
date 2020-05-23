package itc.ink.hhxrf.settings_group_fragment.edit_report_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class EditReportFragment extends Fragment {
    public static final String REPORT_TEMPLATE_KEY="report_template";
    public static final String REPORT_TEMPLATE_VALUE_ONE="TEMPLATE_ONE";
    public static final String REPORT_TEMPLATE_VALUE_TWO="TEMPLATE_TWO";
    public static final String REPORT_TEMPLATE_VALUE_THREE="TEMPLATE_THREE";

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
        templateOneLayoutBtn.setOnLongClickListener(new TemplateOneLayoutBtnLongClickListener());
        templateOneLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_One_Title);
        templateOneLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_One_Selected_Tip);

        templateTwoLayoutBtn=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Layout_Btn);
        templateTwoLayoutBtn.setOnClickListener(new TemplateTwoLayoutBtnClickListener());
        templateTwoLayoutBtn.setOnLongClickListener(new TemplateTwoLayoutBtnLongClickListener());
        templateTwoLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Title);
        templateTwoLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_Two_Selected_Tip);

        templateThreeLayoutBtn=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Layout_Btn);
        templateThreeLayoutBtn.setOnClickListener(new TemplateThreeLayoutBtnClickListener());
        templateThreeLayoutBtn.setOnLongClickListener(new TemplateThreeLayoutBtnLongClickListener());
        templateThreeLayoutBtnTitle=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Title);
        templateThreeLayoutBtnSelTip=rootView.findViewById(R.id.edit_Report_Fragment_Template_Three_Selected_Tip);


        if(SharedPreferenceUtil.getString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_ONE).equals(REPORT_TEMPLATE_VALUE_ONE)){
            selectTemplate(1);
        }else if(SharedPreferenceUtil.getString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_ONE).equals(REPORT_TEMPLATE_VALUE_TWO)){
            selectTemplate(2);
        }else if(SharedPreferenceUtil.getString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_ONE).equals(REPORT_TEMPLATE_VALUE_THREE)){
            selectTemplate(3);
        }

        return rootView;
    }

    private void selectTemplate(int temNum){
        templateOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        templateOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        templateOneLayoutBtnSelTip.setVisibility(View.GONE);
        templateTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        templateTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        templateTwoLayoutBtnSelTip.setVisibility(View.GONE);
        templateThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        templateThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        templateThreeLayoutBtnSelTip.setVisibility(View.GONE);
        switch (temNum){
            case 1:
                templateOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                templateOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                templateOneLayoutBtnSelTip.setVisibility(View.VISIBLE);
                break;
            case 2:
                templateTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                templateTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                templateTwoLayoutBtnSelTip.setVisibility(View.VISIBLE);
                break;
            case 3:
                templateThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                templateThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                templateThreeLayoutBtnSelTip.setVisibility(View.VISIBLE);
                break;
        }
    }

    class TemplateOneLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectTemplate(1);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_ONE);
        }
    }

    class TemplateOneLayoutBtnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            selectTemplate(1);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_ONE);
            Intent intent=new Intent();
            intent.setClass(getContext(),EditReportTemplateActivity.class);
            intent.putExtra("TEMPLATE_NAME",REPORT_TEMPLATE_VALUE_ONE);
            startActivity(intent);
            return false;
        }
    }

    class TemplateTwoLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectTemplate(2);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_TWO);
        }
    }

    class TemplateTwoLayoutBtnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            selectTemplate(2);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_TWO);
            Intent intent=new Intent();
            intent.setClass(getContext(),EditReportTemplateActivity.class);
            intent.putExtra("TEMPLATE_NAME",REPORT_TEMPLATE_VALUE_TWO);
            startActivity(intent);
            return false;
        }
    }

    class TemplateThreeLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectTemplate(3);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_THREE);
        }
    }

    class TemplateThreeLayoutBtnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            selectTemplate(3);
            SharedPreferenceUtil.putString(REPORT_TEMPLATE_KEY,REPORT_TEMPLATE_VALUE_THREE);
            Intent intent=new Intent();
            intent.setClass(getContext(),EditReportTemplateActivity.class);
            intent.putExtra("TEMPLATE_NAME",REPORT_TEMPLATE_VALUE_THREE);
            startActivity(intent);
            return false;
        }
    }


}

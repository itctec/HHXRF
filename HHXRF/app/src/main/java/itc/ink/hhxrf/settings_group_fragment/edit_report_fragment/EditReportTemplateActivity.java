package itc.ink.hhxrf.settings_group_fragment.edit_report_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.calibration_fragment.OnEnergyCalibrationActivity;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class EditReportTemplateActivity extends BaseActivity {
    private String templateName;
    private TextView activityTitleTxt;
    private EditText templateHeaderEdit;
    private EditText templateFooterEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_edit_report_template);

        Intent intent=getIntent();
        templateName=intent.getStringExtra("TEMPLATE_NAME");
        templateHeaderEdit=findViewById(R.id.edit_Report_Template_Header_Edit);
        templateFooterEdit=findViewById(R.id.edit_Report_Template_Footer_Edit);

        activityTitleTxt=findViewById(R.id.edit_Report_Template_Title);
        if (templateName.equals(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE)){
            activityTitleTxt.setText(R.string.edit_report_fragment_template_one);
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_HEADER","").equals("")){
                templateHeaderEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_HEADER",""));
            }
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_FOOTER","").equals("")){
                templateFooterEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_FOOTER",""));
            }
        }else if (templateName.equals(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO)){
            activityTitleTxt.setText(R.string.edit_report_fragment_template_two);
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_HEADER","").equals("")){
                templateHeaderEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_HEADER",""));
            }
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_FOOTER","").equals("")){
                templateFooterEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_FOOTER",""));
            }
        }else if (templateName.equals(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE)){
            activityTitleTxt.setText(R.string.edit_report_fragment_template_three);
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_HEADER","").equals("")){
                templateHeaderEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_HEADER",""));
            }
            if(!SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_FOOTER","").equals("")){
                templateFooterEdit.setText(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_FOOTER",""));
            }
        }

    }


    public void onBackBtnClick(View view){
        SharedPreferenceUtil.putString(templateName+"_HEADER",templateHeaderEdit.getText().toString());
        SharedPreferenceUtil.putString(templateName+"_FOOTER",templateFooterEdit.getText().toString());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            SharedPreferenceUtil.putString(templateName+"_HEADER",templateHeaderEdit.getText().toString());
            SharedPreferenceUtil.putString(templateName+"_FOOTER",templateFooterEdit.getText().toString());
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

package itc.ink.hhxrf.home_fragment.last_report;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.calibration_fragment.TypeCalibrationActivity;
import itc.ink.hhxrf.settings_group_fragment.decimal_point_fragment.DecimalPointFragment;
import itc.ink.hhxrf.settings_group_fragment.edit_report_fragment.EditReportFragment;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.LinkFragment;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.printer.util.PrintUtils;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.settings_group_fragment.unit.UnitFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;
import itc.ink.hhxrf.view.McaLineView;

public class ReportActivity extends BaseActivity {
    private ImageView backBtn;
    private EditText topNavigationSampleName;
    private String sampleOlderName="";
    private ImageView reportShowType;
    private ImageView reportChangeColumnBtn;
    private TextView elementNameLabel;
    private TextView operationOneLabel;
    private TextView operationTwoLabel;
    private RecyclerView reportDataRV;
    private int reportShowAsList=0;
    private LastReportDataListAdapter lastReportDataAdapter;
    private LastReportDataGridAdapter lastReportDataGridAdapter;
    private List<LastReportDataMode> lastReportDataArray=new ArrayList<LastReportDataMode>();
    private McaLineView reportMcaLineView;
    private TextView showMoreBtn;
    private boolean isShowAll=false;

    private ConstraintLayout printReportLayout;
    private ConstraintLayout sendReportLayout;
    private String reportDateTime="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LastReportFragment.showForthColumn=false;

        Intent intent=getIntent();
        String sampleName=intent.getStringExtra("SAMPLE_NAME");

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.fragment_home_last_report);

        topNavigationSampleName=findViewById(R.id.last_Report_Fragment_Top_Navigation_Sample_Name);
        topNavigationSampleName.setEnabled(false);
        topNavigationSampleName.setOnEditorActionListener(new TopNavigationSampleNameEditorActionListener());
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(ReportActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_history_data where sample_name='"+sampleName+"'";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);

        TextView calibrationName=findViewById(R.id.last_Report_Fragment_Calibration_Name);

        TextView testType=findViewById(R.id.last_Report_Fragment_Test_Type);

        if(cursor.moveToNext()){
            topNavigationSampleName.setText(cursor.getString(cursor.getColumnIndex("sample_name")));
            sampleOlderName=cursor.getString(cursor.getColumnIndex("sample_name"));
            reportDateTime=cursor.getString(cursor.getColumnIndex("test_datetime"));

            if(cursor.getString(cursor.getColumnIndex("calibration_type")).equals(TypeCalibrationActivity.CA_NONE_VALUE)){
                calibrationName.setText("");
            }else{
                calibrationName.setText(cursor.getString(cursor.getColumnIndex("calibration_type")));
            }

            if(cursor.getString(cursor.getColumnIndex("test_way")).equals(TestWayFragment.TEST_WAY_VALUE_METAL)){
                testType.setText(R.string.test_way_fragment_metal);
            }else{
                testType.setText(R.string.test_way_fragment_ground);
            }

        }else{
            topNavigationSampleName.setText(getResources().getString(R.string.home_last_report_top_navigation_label));
            topNavigationSampleName.setEnabled(false);

            calibrationName.setText("");
            testType.setText("--");
        }

        initListData(false,sampleName);

        lastReportDataAdapter=new LastReportDataListAdapter(this,lastReportDataArray);
        lastReportDataGridAdapter=new LastReportDataGridAdapter(this,lastReportDataArray);

        backBtn=findViewById(R.id.last_Report_Fragment_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        reportShowType=findViewById(R.id.last_Report_Fragment_Report_Show_Type);
        reportShowType.setOnClickListener(new ReportShowTypeClickListener());

        reportChangeColumnBtn=findViewById(R.id.last_Report_Fragment_Report_Change_Column_Btn);
        reportChangeColumnBtn.setOnClickListener(new ReportChangeColumnRightBtnClickListener());

        elementNameLabel=findViewById(R.id.last_Report_Fragment_Element_Name);
        operationOneLabel=findViewById(R.id.last_Report_Fragment_Element_Operation_One_Label);
        operationOneLabel.setText(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%"));
        operationTwoLabel=findViewById(R.id.last_Report_Fragment_Element_Operation_Two_Label);

        reportDataRV=findViewById(R.id.last_Report_Fragment_Report_Data_RV);
        reportDataRV.setAdapter(lastReportDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this);
        reportDataRV.setLayoutManager(contentRvLayoutManager);

        reportMcaLineView=findViewById(R.id.last_Report_Fragment_Report_Data_Mca_Line_View);
        reportMcaLineView.setMcaData(initMcaData());

        showMoreBtn=findViewById(R.id.last_Report_Fragment_Report_Data_Show_More_Btn);
        showMoreBtn.setOnClickListener(new ShowMoreBtnClickListener());

        printReportLayout=findViewById(R.id.last_Report_Fragment_Print_Report_Layout);
        printReportLayout.setOnClickListener(new PrintReportLayoutClickListener());

        sendReportLayout=findViewById(R.id.last_Report_Fragment_Send_Report_Layout);
        sendReportLayout.setOnClickListener(new SendReportLayoutClickListener());
        
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(topNavigationSampleName.getWindowToken(), 0);
    }

    public List<LastReportDataMode> initListData(boolean isShowAll,String sampleName){

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(ReportActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr;
        if(isShowAll){
            sqlStr = "select * from tb_history_data_content where sample_name='"+sampleName+"' order by element_concentration desc" ;
        }else{
            sqlStr = "select * from tb_history_data_content where sample_name='"+sampleName+"' order by element_concentration desc LIMIT 4" ;
        }

        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        DecimalFormat decimalFormat=new DecimalFormat(SharedPreferenceUtil.getString(DecimalPointFragment.DECIMAL_POINT_KEY,"0.00"));
        while(cursor.moveToNext()){
            LastReportDataMode reportItem=new LastReportDataMode(cursor.getString(cursor.getColumnIndex("element_name")),
                    decimalFormat.format(Float.parseFloat(cursor.getString(cursor.getColumnIndex("element_concentration")))),
                    cursor.getString(cursor.getColumnIndex("element_range")),
                    cursor.getString(cursor.getColumnIndex("element_average")));
            lastReportDataArray.add(reportItem);
        }

        return lastReportDataArray;
    }

    public List<Integer> initMcaData(){
        List<Integer> mcaDataList=new ArrayList<>();
        File csv = new File("data/XRS/Data_T/_Gaussfit_CC1.mca");
        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine();
            String line = "";
            boolean startTag=false;
            while ((line = br.readLine()) != null) {
                if(startTag){
                    if(line.equals("<<END>>")){
                        break;
                    }else{
                        int data=Integer.parseInt(line);
                        mcaDataList.add(data);
                    }
                }
                if(line.equals("<<DATA>>")){
                    startTag=true;
                }
            }
            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return mcaDataList;
    }

    class TopNavigationSampleNameEditorActionListener implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                topNavigationSampleName.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(topNavigationSampleName.getWindowToken(), 0);

                SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(ReportActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
                SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

                String checkNameSqlStr="select sample_name from tb_history_data where sample_name='"+topNavigationSampleName.getText().toString()+"'";
                Cursor cursor = sqLiteDatabase.rawQuery(checkNameSqlStr, null);

                if(cursor.moveToNext()){
                    Toast.makeText(ReportActivity.this,R.string.home_last_report_sameple_rename_faild_tip,Toast.LENGTH_LONG).show();
                }else{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("sample_name", topNavigationSampleName.getText().toString());
                    sqLiteDatabase.update("tb_history_data", contentValues, "sample_name=?", new String[]{sampleOlderName});
                    sampleOlderName=topNavigationSampleName.getText().toString();
                }
            }
            return false;
        }
    }

    class ReportShowTypeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            reportShowAsList=(++reportShowAsList)%2;

            if(0==reportShowAsList){
                reportMcaLineView.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.VISIBLE);
                reportShowType.setImageResource(R.drawable.report_show_type_grid_icon);
                reportDataRV.setAdapter(lastReportDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(ReportActivity.this);
                reportDataRV.setLayoutManager(contentRvLayoutManager);
                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=0;
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.VISIBLE);
                operationOneLabel.setVisibility(View.VISIBLE);
                operationTwoLabel.setVisibility(View.VISIBLE);
                reportChangeColumnBtn.setVisibility(View.VISIBLE);
                showMoreBtn.setVisibility(View.VISIBLE);
            }else if(1==reportShowAsList){
                reportMcaLineView.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.VISIBLE);
                reportShowType.setImageResource(R.drawable.report_show_type_list_icon);
                reportDataRV.setAdapter(lastReportDataGridAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(ReportActivity.this, 3);
                reportDataRV.setLayoutManager(contentRvLayoutManager);

                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=(int)dp2px(15);
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.GONE);
                operationOneLabel.setVisibility(View.GONE);
                operationTwoLabel.setVisibility(View.GONE);
                reportChangeColumnBtn.setVisibility(View.GONE);
                showMoreBtn.setVisibility(View.GONE);
            }/*else if(2==reportShowAsList){
                reportShowType.setImageResource(R.drawable.report_show_type_list_icon);
                elementNameLabel.setVisibility(View.GONE);
                operationOneLabel.setVisibility(View.GONE);
                operationTwoLabel.setVisibility(View.GONE);
                reportChangeColumnBtn.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.GONE);
                reportMcaLineView.setVisibility(View.VISIBLE);
                showMoreBtn.setVisibility(View.GONE);
            }*/
        }

        private float dp2px(int dp){
            return Resources.getSystem().getDisplayMetrics().density *dp;
        }
    }

    class ReportChangeColumnRightBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            LastReportFragment.showForthColumn=!LastReportFragment.showForthColumn;
            lastReportDataAdapter.notifyDataSetChanged();

            if(LastReportFragment.showForthColumn){
                reportChangeColumnBtn.setImageResource(R.drawable.vector_drawable_triangle_left);
                operationOneLabel.setText(getResources().getString(R.string.home_last_report_column_range));
                operationTwoLabel.setText(getResources().getString(R.string.home_last_report_column_mean_value));
            }else{
                reportChangeColumnBtn.setImageResource(R.drawable.vector_drawable_triangle_right);
                operationOneLabel.setText(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%"));
                operationTwoLabel.setText(getResources().getString(R.string.home_last_report_column_range));
            }
        }
    }

    class ShowMoreBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isShowAll){
                Drawable drawable= getResources().getDrawable(R.drawable.expand_more,null);
                showMoreBtn.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                showMoreBtn.setText(R.string.home_last_report_show_more);
                isShowAll=false;
            }else{
                Drawable drawable= getResources().getDrawable(R.drawable.expand_less,null);
                showMoreBtn.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                showMoreBtn.setText(R.string.home_last_report_show_less);
                isShowAll=true;
            }

            lastReportDataArray.clear();
            initListData(isShowAll,sampleOlderName);
            lastReportDataAdapter.notifyDataSetChanged();
        }
    }

    class PrintReportLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(!LinkFragment.isConnected && LinkFragment.mPrinter == null) {
                Toast.makeText(ReportActivity.this,"打印机未连接，请先连接打印机",Toast.LENGTH_LONG).show();
            }else{
                new Thread(){
                    @Override
                    public void run() {
                        LinkFragment.mPrinter.init();

                        String printHeaderText="";
                        if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE)){
                            printHeaderText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_HEADER","");
                        }else if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO)){
                            printHeaderText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_HEADER","");
                        }else if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE)){
                            printHeaderText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_HEADER","");
                        }

                        String printTableTitleText="样品名称："+topNavigationSampleName.getText().toString()+"\n测试时间："+reportDateTime;
                        String printTableText="元素\t%\t均值\n";
                        for(LastReportDataMode reportItem :lastReportDataArray){
                            printTableText+=reportItem.getElement_name()+"\t"+
                                    reportItem.getElement_percent()+"\t"+
                                    reportItem.getElement_mean_value()+"\n";
                        }

                        String printFooterText="";
                        if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE)){
                            printFooterText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_ONE+"_FOOTER","");
                        }else if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO)){
                            printFooterText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_TWO+"_FOOTER","");
                        }else if(SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_KEY,EditReportFragment.REPORT_TEMPLATE_VALUE_ONE).equals(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE)){
                            printFooterText=SharedPreferenceUtil.getString(EditReportFragment.REPORT_TEMPLATE_VALUE_THREE+"_FOOTER","");
                        }

                        LinkFragment.mPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
                        LinkFragment.mPrinter.setFont(0, 0, 1, 0);
                        LinkFragment.mPrinter.printText(printHeaderText);
                        LinkFragment.mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
                        LinkFragment.mPrinter.printText(printTableTitleText);
                        LinkFragment.mPrinter.setFont(0, 0, 0, 0);
                        LinkFragment.mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);
                        LinkFragment.mPrinter.printText(printTableText);
                        LinkFragment.mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);
                        LinkFragment.mPrinter.printText(printFooterText);
                        LinkFragment.mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
                    }
                }.start();
            }
        }
    }

    class SendReportLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
            intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "分享"));
        }
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}

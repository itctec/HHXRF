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

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

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
    private boolean reportShowAsList=true;
    private LastReportDataListAdapter lastReportDataAdapter;
    private LastReportDataGridAdapter lastReportDataGridAdapter;
    private List<LastReportDataMode> lastReportDataArray=new ArrayList<LastReportDataMode>();
    public static boolean showForthColumn=false;
    private TextView showMoreBtn;
    private boolean isShowAll=false;

    private ConstraintLayout sendReportLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        TextView testType=findViewById(R.id.last_Report_Fragment_Test_Type);

        if(cursor.moveToNext()){
            topNavigationSampleName.setText(cursor.getString(cursor.getColumnIndex("sample_name")));
            sampleOlderName=cursor.getString(cursor.getColumnIndex("sample_name"));

            if(cursor.getString(cursor.getColumnIndex("test_way")).equals(TestWayFragment.TEST_WAY_VALUE_METAL)){
                testType.setText(R.string.test_way_fragment_metal);
            }else{
                testType.setText(R.string.test_way_fragment_ground);
            }

        }else{
            topNavigationSampleName.setText(getResources().getString(R.string.home_last_report_top_navigation_label));
            topNavigationSampleName.setEnabled(false);

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
        operationTwoLabel=findViewById(R.id.last_Report_Fragment_Element_Operation_Two_Label);

        reportDataRV=findViewById(R.id.last_Report_Fragment_Report_Data_RV);
        reportDataRV.setAdapter(lastReportDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this);
        reportDataRV.setLayoutManager(contentRvLayoutManager);

        showMoreBtn=findViewById(R.id.last_Report_Fragment_Report_Data_Show_More_Btn);
        showMoreBtn.setOnClickListener(new ShowMoreBtnClickListener());

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
        while(cursor.moveToNext()){
            LastReportDataMode reportItem=new LastReportDataMode(cursor.getString(cursor.getColumnIndex("element_name")),
                    cursor.getString(cursor.getColumnIndex("element_concentration")).substring(0,5),
                    cursor.getString(cursor.getColumnIndex("element_range")),
                    cursor.getString(cursor.getColumnIndex("element_average")));
            lastReportDataArray.add(reportItem);
        }

        return lastReportDataArray;
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
            reportShowAsList=!reportShowAsList;

            if(reportShowAsList){
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
            }else{
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
            }
        }

        private float dp2px(int dp){
            return Resources.getSystem().getDisplayMetrics().density *dp;
        }
    }

    class ReportChangeColumnRightBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            showForthColumn=!showForthColumn;
            lastReportDataAdapter.notifyDataSetChanged();

            if(showForthColumn){
                reportChangeColumnBtn.setImageResource(R.drawable.vector_drawable_triangle_left);
                operationOneLabel.setText(getResources().getString(R.string.home_last_report_column_range));
                operationTwoLabel.setText(getResources().getString(R.string.home_last_report_column_mean_value));
            }else{
                reportChangeColumnBtn.setImageResource(R.drawable.vector_drawable_triangle_right);
                operationOneLabel.setText(getResources().getString(R.string.home_last_report_column_percent_symble));
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

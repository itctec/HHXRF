package itc.ink.hhxrf.home_fragment.last_report;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import itc.ink.hhxrf.BuildConfig;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.calibration_fragment.TypeCalibrationActivity;
import itc.ink.hhxrf.settings_group_fragment.compound_fragment.CompoundLibDataMode;
import itc.ink.hhxrf.settings_group_fragment.decimal_point_fragment.DecimalPointFragment;
import itc.ink.hhxrf.settings_group_fragment.edit_report_fragment.EditReportFragment;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.CompareDataActivity;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.HistoryDBDataMode;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.LinkFragment;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.printer.util.PrintUtils;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.settings_group_fragment.unit.UnitFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.view.McaLineView;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class LastReportFragment extends Fragment {
    private EditText topNavigationSampleName;
    private String sampleOlderName="";
    private TextView gradeName;
    private TextView conformity_Value;
    private ImageView reportShowType;
    private ImageView reportChangeColumnBtn;
    private TextView elementNameLabel;
    private TextView operationOneLabel;
    private TextView operationOneLabelSmall;
    private TextView operationTwoLabel;
    private RecyclerView reportDataRV;
    private int reportShowAsList=0;
    private LastReportDataListAdapter lastReportDataAdapter;
    private LastReportDataGridAdapter lastReportDataGridAdapter;
    private List<LastReportDataMode> lastReportDataArray=new ArrayList<LastReportDataMode>();
    public static boolean showForthColumn=false;
    private McaLineView reportMcaLineView;
    private TextView showMoreBtn;
    private boolean isShowAll=false;

    private ConstraintLayout printReportLayout;
    private ConstraintLayout sendReportLayout;
    private String reportDateTime="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showForthColumn=false;

        initListData(false);

        lastReportDataAdapter=new LastReportDataListAdapter(getContext(),lastReportDataArray);
        lastReportDataGridAdapter=new LastReportDataGridAdapter(getContext(),lastReportDataArray);
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(topNavigationSampleName.getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_last_report, container, false);

        topNavigationSampleName=rootView.findViewById(R.id.last_Report_Fragment_Top_Navigation_Sample_Name);
        topNavigationSampleName.setOnEditorActionListener(new TopNavigationSampleNameEditorActionListener());
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_history_data order by test_datetime desc LIMIT 1";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);

        TextView calibrationName=rootView.findViewById(R.id.last_Report_Fragment_Calibration_Name);

        TextView testType=rootView.findViewById(R.id.last_Report_Fragment_Test_Type);

        ReportShowTypeClickListener reportShowTypeClickListener= new ReportShowTypeClickListener();
        gradeName=rootView.findViewById(R.id.last_Report_Fragment_Grade_Name);
        gradeName.setOnClickListener(reportShowTypeClickListener);

        reportShowType=rootView.findViewById(R.id.last_Report_Fragment_Report_Show_Type);
        reportShowType.setOnClickListener(reportShowTypeClickListener);

        conformity_Value=rootView.findViewById(R.id.last_Report_Fragment_Conformity_Value);

        if(cursor.moveToNext()){
            gradeName.setText(cursor.getString(cursor.getColumnIndex("mark_name")));
            if(cursor.getString(cursor.getColumnIndex("mark_suit_value"))!=null&&
                    cursor.getString(cursor.getColumnIndex("mark_suit_value")).equals("SUIT_FULL")){
                conformity_Value.setText("匹配");
            }else if(cursor.getString(cursor.getColumnIndex("mark_suit_value"))!=null&&
                    cursor.getString(cursor.getColumnIndex("mark_suit_value")).equals("SUIT_PART")){
                conformity_Value.setText("部分匹配");
            }
            else if(cursor.getString(cursor.getColumnIndex("mark_suit_value"))!=null&&
                    cursor.getString(cursor.getColumnIndex("mark_suit_value")).equals("SUIT_NULL")){
                conformity_Value.setText("不匹配");
            }
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



        ReportChangeColumnRightBtnClickListener reportChangeColumnRightBtnClickListener=new ReportChangeColumnRightBtnClickListener();
        reportChangeColumnBtn=rootView.findViewById(R.id.last_Report_Fragment_Report_Change_Column_Btn);
        reportChangeColumnBtn.setOnClickListener(reportChangeColumnRightBtnClickListener);

        elementNameLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Name);
        operationOneLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Operation_One_Label);
        operationOneLabel.setText(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%"));
        operationOneLabelSmall=rootView.findViewById(R.id.last_Report_Fragment_Element_Operation_One_Label_Small);
        if(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%").equals("mg/cm")){
            operationOneLabelSmall.setVisibility(View.VISIBLE);
        }else{
            operationOneLabelSmall.setVisibility(View.GONE);
        }
        operationTwoLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Operation_Two_Label);
        operationTwoLabel.setOnClickListener(reportChangeColumnRightBtnClickListener);

        reportDataRV=rootView.findViewById(R.id.last_Report_Fragment_Report_Data_RV);
        reportDataRV.setAdapter(lastReportDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
        reportDataRV.setLayoutManager(contentRvLayoutManager);

        reportMcaLineView=rootView.findViewById(R.id.last_Report_Fragment_Report_Data_Mca_Line_View);
        reportMcaLineView.setMcaData(initMcaData());

        showMoreBtn=rootView.findViewById(R.id.last_Report_Fragment_Report_Data_Show_More_Btn);
        showMoreBtn.setOnClickListener(new ShowMoreBtnClickListener());

        printReportLayout=rootView.findViewById(R.id.last_Report_Fragment_Print_Report_Layout);
        printReportLayout.setOnClickListener(new PrintReportLayoutClickListener());

        sendReportLayout=rootView.findViewById(R.id.last_Report_Fragment_Send_Report_Layout);
        sendReportLayout.setOnClickListener(new SendReportLayoutClickListener());
        return rootView;
    }

    public List<LastReportDataMode> initListData(boolean isShowAll){

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr;
        if(isShowAll){
            sqlStr = "select * from tb_history_data_content where sample_name=(select sample_name from tb_history_data order by test_datetime desc LIMIT 1) order by element_concentration desc";
        }else{
            sqlStr = "select * from tb_history_data_content where sample_name=(select sample_name from tb_history_data order by test_datetime desc LIMIT 1) order by element_concentration desc LIMIT 4";
        }
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        DecimalFormat decimalFormat=new DecimalFormat(SharedPreferenceUtil.getString(DecimalPointFragment.DECIMAL_POINT_KEY,"0.00"));
        while(cursor.moveToNext()){
            System.out.println("结论->"+cursor.getString(cursor.getColumnIndex("element_range")));
            LastReportDataMode reportItem=new LastReportDataMode(cursor.getString(cursor.getColumnIndex("element_name")),
                    decimalFormat.format(cursor.getFloat(cursor.getColumnIndex("element_concentration"))),
                    cursor.getString(cursor.getColumnIndex("element_range")),
                    decimalFormat.format(cursor.getFloat(cursor.getColumnIndex("element_average"))));
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(topNavigationSampleName.getWindowToken(), 0);

                SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
                SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

                String checkNameSqlStr="select sample_name from tb_history_data where sample_name='"+topNavigationSampleName.getText().toString()+"'";
                Cursor cursor = sqLiteDatabase.rawQuery(checkNameSqlStr, null);

                if(cursor.moveToNext()){
                    Toast.makeText(getContext(),R.string.home_last_report_sameple_rename_faild_tip,Toast.LENGTH_LONG).show();
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
            reportShowAsList=(++reportShowAsList)%3;

            if(0==reportShowAsList){
                reportMcaLineView.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.VISIBLE);
                lastReportDataArray.clear();
                initListData(isShowAll);
                lastReportDataAdapter.notifyDataSetChanged();

                reportShowType.setImageResource(R.drawable.report_show_type_list_icon);
                reportDataRV.setAdapter(lastReportDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
                reportDataRV.setLayoutManager(contentRvLayoutManager);
                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=0;
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.VISIBLE);
                operationOneLabel.setVisibility(View.VISIBLE);
                if(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%").equals("mg/cm")){
                    operationOneLabelSmall.setVisibility(View.VISIBLE);
                }else{
                    operationOneLabelSmall.setVisibility(View.GONE);
                }
                operationTwoLabel.setVisibility(View.VISIBLE);
                reportChangeColumnBtn.setVisibility(View.VISIBLE);
                showMoreBtn.setVisibility(View.VISIBLE);
            }else if(1==reportShowAsList){
                reportMcaLineView.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.VISIBLE);
                lastReportDataArray.clear();
                initListData(true);
                lastReportDataGridAdapter.notifyDataSetChanged();

                reportShowType.setImageResource(R.drawable.report_show_type_grid_icon);
                reportDataRV.setAdapter(lastReportDataGridAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
                reportDataRV.setLayoutManager(contentRvLayoutManager);

                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=(int)dp2px(15);
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.GONE);
                operationOneLabel.setVisibility(View.GONE);
                operationOneLabelSmall.setVisibility(View.GONE);
                operationTwoLabel.setVisibility(View.GONE);
                reportChangeColumnBtn.setVisibility(View.GONE);
                showMoreBtn.setVisibility(View.GONE);
            }else if(2==reportShowAsList){
                reportShowType.setImageResource(R.drawable.report_show_type_line_icon);
                elementNameLabel.setVisibility(View.GONE);
                operationOneLabel.setVisibility(View.GONE);
                operationOneLabelSmall.setVisibility(View.GONE);
                operationTwoLabel.setVisibility(View.GONE);
                reportChangeColumnBtn.setVisibility(View.GONE);
                reportDataRV.setVisibility(View.GONE);
                reportMcaLineView.setVisibility(View.VISIBLE);
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
                operationOneLabelSmall.setVisibility(View.GONE);
                operationTwoLabel.setText(getResources().getString(R.string.home_last_report_column_mean_value));
            }else{
                reportChangeColumnBtn.setImageResource(R.drawable.vector_drawable_triangle_right);
                operationOneLabel.setText(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%"));
                if(SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT,"%").equals("mg/cm")){
                    operationOneLabelSmall.setVisibility(View.VISIBLE);
                }else{
                    operationOneLabelSmall.setVisibility(View.GONE);
                }
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
            initListData(isShowAll);
            lastReportDataAdapter.notifyDataSetChanged();
        }
    }

    class PrintReportLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(!LinkFragment.isConnected && LinkFragment.mPrinter == null) {
                Toast.makeText(getContext(),"打印机未连接，请先连接打印机",Toast.LENGTH_LONG).show();
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
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
            intent.putExtra(Intent.EXTRA_TEXT, "元素\t\t%\t\t范围\nCr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~19\n" +
                    "Cr\t\t16.95\t\t18~12\n");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "分享"));
        }
    }

}

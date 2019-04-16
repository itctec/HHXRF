package itc.ink.hhxrf.home_fragment.last_report;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class LastReportFragment extends Fragment {
    private ImageView reportShowType;
    private ImageView reportChangeColumnBtn;
    private TextView elementNameLabel;
    private TextView operationOneLabel;
    private TextView operationTwoLabel;
    private RecyclerView reportDataRV;
    private boolean reportShowAsList=true;
    private LastReportDataListAdapter lastReportDataAdapter;
    private LastReportDataGridAdapter lastReportDataGridAdapter;
    private List<LastReportDataMode> lastReportDataArray;
    public static boolean showForthColumn=false;

    private ConstraintLayout sendReportLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastReportDataArray=initListData();

        lastReportDataAdapter=new LastReportDataListAdapter(getContext(),lastReportDataArray);
        lastReportDataGridAdapter=new LastReportDataGridAdapter(getContext(),lastReportDataArray);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_last_report, container, false);

        reportShowType=rootView.findViewById(R.id.last_Report_Fragment_Report_Show_Type);
        reportShowType.setOnClickListener(new ReportShowTypeClickListener());

        reportChangeColumnBtn=rootView.findViewById(R.id.last_Report_Fragment_Report_Change_Column_Btn);
        reportChangeColumnBtn.setOnClickListener(new ReportChangeColumnRightBtnClickListener());

        elementNameLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Name);
        operationOneLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Operation_One_Label);
        operationTwoLabel=rootView.findViewById(R.id.last_Report_Fragment_Element_Operation_Two_Label);

        reportDataRV=rootView.findViewById(R.id.last_Report_Fragment_Report_Data_RV);
        reportDataRV.setAdapter(lastReportDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
        reportDataRV.setLayoutManager(contentRvLayoutManager);

        sendReportLayout=rootView.findViewById(R.id.last_Report_Fragment_Send_Report_Layout);
        sendReportLayout.setOnClickListener(new SendReportLayoutClickListener());
        return rootView;
    }

    public List<LastReportDataMode> initListData(){
        List<LastReportDataMode> lastReportDataArray=new ArrayList<>();
        for(int i=0;i<10;i++){
            LastReportDataMode reportItem=new LastReportDataMode("AC"+i,"62","18~19","62");
            lastReportDataArray.add(reportItem);
        }

        return lastReportDataArray;
    }

    class ReportShowTypeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            reportShowAsList=!reportShowAsList;

            if(reportShowAsList){
                reportShowType.setImageResource(R.drawable.report_show_type_grid_icon);
                reportDataRV.setAdapter(lastReportDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
                reportDataRV.setLayoutManager(contentRvLayoutManager);
                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=0;
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.VISIBLE);
                operationOneLabel.setVisibility(View.VISIBLE);
                operationTwoLabel.setVisibility(View.VISIBLE);
                reportChangeColumnBtn.setVisibility(View.VISIBLE);
            }else{
                reportShowType.setImageResource(R.drawable.report_show_type_list_icon);
                reportDataRV.setAdapter(lastReportDataGridAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
                reportDataRV.setLayoutManager(contentRvLayoutManager);

                ConstraintLayout.LayoutParams rvLayoutParam=(ConstraintLayout.LayoutParams)reportDataRV.getLayoutParams();
                rvLayoutParam.rightMargin=(int)dp2px(15);
                reportDataRV.setLayoutParams(rvLayoutParam);

                elementNameLabel.setVisibility(View.GONE);
                operationOneLabel.setVisibility(View.GONE);
                operationTwoLabel.setVisibility(View.GONE);
                reportChangeColumnBtn.setVisibility(View.GONE);
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

}

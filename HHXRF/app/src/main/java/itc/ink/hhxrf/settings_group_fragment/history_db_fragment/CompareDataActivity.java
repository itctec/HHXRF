package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class CompareDataActivity extends BaseActivity {
    private EditText searchBox;
    private Button compareDataBtn;
    private RecyclerView sampleHistoryRecyclerView;
    private HistoryDbDataAdapter sampleHistoryDataAdapter;
    private List<HistoryDBDataMode> historyItemArray=new ArrayList<HistoryDBDataMode>();

    public static boolean isMultiChoiceState=false;
    public static int choiceCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_compare_data);

        searchBox=findViewById(R.id.data_Compare_SearchBox);
        searchBox.setOnEditorActionListener(new SearchBoxEditorActionListener());

        compareDataBtn=findViewById(R.id.data_Compare_Btn);
        compareDataBtn.setOnClickListener(new CompareDataBtnClickListener());

        initHistoryData();
        sampleHistoryDataAdapter=new HistoryDbDataAdapter(this, historyItemArray,new ItemChoiceCallBack());
        sampleHistoryRecyclerView=findViewById(R.id.data_Compare_History_RV);
        sampleHistoryRecyclerView.setAdapter(sampleHistoryDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sampleHistoryRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMultiChoiceState=true;
        choiceCount=0;
    }

    public List<HistoryDBDataMode> initHistoryData() {
        historyItemArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(CompareDataActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "";
        if(searchBox.getText().toString().trim().isEmpty()){
            sqlStr = "select * from tb_history_data order by test_datetime desc";
        }else{
            String sampleName[] = searchBox.getText().toString().split(",");
            if(sampleName.length==2){
                sqlStr="select * from tb_history_data where sample_name like '%"+sampleName[0]+"%' or sample_name like '%"+sampleName[1]+"%' order by test_datetime desc";
            }else{
                sqlStr="select * from tb_history_data where sample_name like '%"+searchBox.getText().toString().trim()+"%' order by test_datetime desc";
            }
        }
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            HistoryDBDataMode historyShowDataItem=new HistoryDBDataMode(cursor.getString(0),cursor.getString(1),cursor.getString(2),false);
            historyItemArray.add(historyShowDataItem);
        }

        return historyItemArray;
    }


    public void onBackBtnClick(View view){
        finish();
    }


    class SearchBoxEditorActionListener implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                searchBox.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

                initHistoryData();
                sampleHistoryDataAdapter.notifyDataSetChanged();

                choiceCount=0;
            }
            return false;
        }
    }


    class CompareDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(CompareDataActivity.this,CompareResultActivity.class);
            ArrayList<String> itemCompareArray=new ArrayList<>();

            int itemCount=historyItemArray.size();
            for (int i=itemCount-1;i>=0;i--){
                if (historyItemArray.get(i).isEditSelected){
                    itemCompareArray.add(historyItemArray.get(i).getSample_name());
                }
            }
            intent.putExtra("SAMPLE_ONE_NAME",itemCompareArray.get(0));
            intent.putExtra("SAMPLE_TWO_NAME",itemCompareArray.get(1));
            startActivity(intent);
            finish();
        }
    }

    class ItemChoiceCallBack implements HistoryDbDataAdapter.ItemChoiceCallBack{
        @Override
        public void onItemChoiceEqualThanTwo() {
            compareDataBtn.setEnabled(true);
            compareDataBtn.setAlpha(1);
        }

        @Override
        public void onItemChoiceLessThanTwo() {
            compareDataBtn.setEnabled(false);
            compareDataBtn.setAlpha(0.8f);
        }
    }
}

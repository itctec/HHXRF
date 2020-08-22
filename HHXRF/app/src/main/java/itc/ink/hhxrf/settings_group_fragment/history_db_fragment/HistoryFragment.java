package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class HistoryFragment extends Fragment{
    private RecyclerView historyRecyclerView;
    private HistoryDbDataAdapter historyDataAdapter;
    private List<HistoryDBDataMode> mHistoryListData;

    private TextView editBtn;
    public static boolean isEditState=false;
    private ImageView historyDelBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryListData=initHistoryData();
        historyDataAdapter=new HistoryDbDataAdapter(getContext(), mHistoryListData,null);
    }

    @Override
    public void onResume() {
        super.onResume();
        CompareDataActivity.isMultiChoiceState=false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_db, container, false);
        isEditState=false;
        editBtn=rootView.findViewById(R.id.history_Fragment_Edit_Btn);
        editBtn.setOnClickListener(new EditBtnClickListener());
        historyDelBtn=rootView.findViewById(R.id.history_Fragment_Item_Del_Btn);
        historyDelBtn.setOnClickListener(new ElementDelBtnClickListener());

        Button compareDataBtn=rootView.findViewById(R.id.history_Fragment_Compare_Data_Btn);
        compareDataBtn.setOnClickListener(new CompareDataBtnClickListener());

        historyRecyclerView=rootView.findViewById(R.id.history_Fragment_Sample_RV);
        historyRecyclerView.setAdapter(historyDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        historyRecyclerView.setLayoutManager(contentRvLayoutManager);

        return rootView;
    }

    public List<HistoryDBDataMode> initHistoryData() {
        List<HistoryDBDataMode> historyItemArray=new ArrayList<>();

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_history_data order by test_datetime desc";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            HistoryDBDataMode historyShowDataItem=new HistoryDBDataMode(cursor.getString(cursor.getColumnIndex("sample_name")),
                    cursor.getString(cursor.getColumnIndex("test_datetime")),
                    cursor.getString(cursor.getColumnIndex("test_way")),
                    false);
            historyItemArray.add(historyShowDataItem);
        }

        return historyItemArray;
    }


    class EditBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            isEditState=!isEditState;
            if(isEditState){
                editBtn.setText(getString(R.string.history_fragment_cancel_edit_btn_text));
                historyDelBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setText(getString(R.string.history_fragment_edit_btn_text));
                historyDelBtn.setVisibility(View.GONE);
                for(int i=0;i<mHistoryListData.size();i++){
                    mHistoryListData.get(i).isEditSelected=false;
                }
            }
            historyDataAdapter.notifyDataSetChanged();
        }
    }

    class CompareDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(getContext(),CompareDataActivity.class);
            startActivity(intent);
        }
    }

    class ElementDelBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int itemCount=mHistoryListData.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mHistoryListData.get(i).isEditSelected){
                    deleteElementFromDB(mHistoryListData.get(i).getSample_name());
                    mHistoryListData.remove(i);
                }
            }
            historyDataAdapter.notifyDataSetChanged();
        }
    }

    public void deleteElementFromDB(String sampleName){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

        String historyInsertSqlStr = "delete from tb_history_data where sample_name='"+sampleName+"'";
        sqLiteDatabase.execSQL(historyInsertSqlStr);
    }
}


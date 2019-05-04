package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.calibration.TypeCalibrationAddSpTwo;
import itc.ink.hhxrf.settings_group_fragment.calibration.TypeCalibrationElementDataMode;
import itc.ink.hhxrf.utils.MarkSimpleItemTouchCallback;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class MarkActivity extends Activity implements OnStartDragListener{
    private TextView activityTitle;
    private ImageView menuBtn;
    private TextView cancelEditBtn;
    private ConstraintLayout menuLayout;
    private TextView importDataBtn;
    private TextView editDataBtn;
    private RecyclerView markLibRV;
    private MarkDataAdapter mMarkDataAdapter;
    private List<MarkDataMode> mMarkDataArray=new ArrayList<>();
    private ItemTouchHelper mHelper;

    public static boolean isEditState=false;
    private ImageView itemDelBtn;
    private long markDBID=0;
    private String markDBName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Intent intent=getIntent();
        markDBID=intent.getLongExtra("MARK_DB_ID",0);
        markDBName=intent.getStringExtra("MARK_DB_NAME");

        setContentView(R.layout.activity_mark);

        activityTitle=findViewById(R.id.mark_Top_Navigation_Activity_Title);
        activityTitle.setText(markDBName);

        initMarkData(mMarkDataArray);
        mMarkDataAdapter=new MarkDataAdapter(this,mMarkDataArray,this,new AddItemCallBack());

        isEditState=false;

        menuBtn=findViewById(R.id.mark_Activity_Menu_Btn);
        menuBtn.setOnClickListener(new MenuBtnClickListener());
        cancelEditBtn=findViewById(R.id.mark_Cancel_Edit_Btn);
        cancelEditBtn.setOnClickListener(new CancelEditBtnClickListener());
        menuLayout=findViewById(R.id.mark_Activity_Menu_Layout);
        importDataBtn=findViewById(R.id.mark_Activity_Import_Data_Menu_Btn);
        importDataBtn.setOnClickListener(new ImportDataBtnClickListener());
        editDataBtn=findViewById(R.id.mark_Activity_Edit_Data_Menu_Btn);
        editDataBtn.setOnClickListener(new EditDataBtnClickListener());

        markLibRV=findViewById(R.id.mark_Item_RV);
        markLibRV.setAdapter(mMarkDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        markLibRV.setLayoutManager(contentRvLayoutManager);
        mHelper = new ItemTouchHelper(new MarkSimpleItemTouchCallback(this,mMarkDataAdapter, mMarkDataArray));
        mHelper.attachToRecyclerView(markLibRV);

        itemDelBtn=findViewById(R.id.mark_Item_Del_Btn);
        itemDelBtn.setOnClickListener(new ItemDelBtnClickListener());
    }

    public void onBackBtnClick(View view){
        finish();
    }


    public void initMarkData(List<MarkDataMode> mMarkDBDataArray) {
        mMarkDBDataArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_mark where mark_db_id="+markDBID;
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            MarkDataMode markDBDataItem=new MarkDataMode(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),false);
            mMarkDBDataArray.add(markDBDataItem);
        }

        Collections.sort(mMarkDBDataArray, new Comparator<MarkDataMode>() {

            @Override
            public int compare(MarkDataMode o1, MarkDataMode o2) {
                if(o1.getMark_rank_num()>o2.getMark_rank_num()){
                    return 1;
                }else if(o1.getMark_rank_num()==o2.getMark_rank_num()){
                    return 0;
                }else{
                    return -1;
                }
            }

        });

        MarkDataMode item_Add_Btn=new MarkDataMode(-1,"",getString(R.string.mark_activity_add_new_mark),"",-1,false);
        mMarkDBDataArray.add(item_Add_Btn);
    }

    class AddItemCallBack implements MarkDataAdapter.AddItemCallBack{
        @Override
        public void addItem() {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            String sqlStr = "select max(mark_rank_num) from tb_mark where mark_db_id="+markDBID;
            Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
            int rankNum=0;
            if (cursor.moveToNext()){
                rankNum=cursor.getInt(0);
            }
            rankNum++;
            ContentValues markValues = new ContentValues();
            markValues.put("mark_id", System.currentTimeMillis());
            markValues.put("mark_name", "?");
            markValues.put("mark_num", "unset");
            markValues.put("mark_db_id", markDBID);
            markValues.put("mark_rank_num", rankNum);
            sqLiteDatabase.insert("tb_mark","",markValues);

            initMarkData(mMarkDataArray);
            mMarkDataAdapter.notifyDataSetChanged();
        }
    }

    class MenuBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (menuLayout.getVisibility()){
                case View.VISIBLE:
                    menuLayout.setVisibility(View.GONE);
                    break;
                case View.GONE:
                    menuLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    class CancelEditBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            for(MarkDataMode markItem:mMarkDataArray){
                String checkOlderNameStr="select mark_num from tb_mark where mark_id="+markItem.getMark_id();
                Cursor nameCursor=sqLiteDatabase.rawQuery(checkOlderNameStr,null);
                if(nameCursor.moveToNext()){
                    if(!markItem.getMark_num().equals(nameCursor.getString(0))){
                        String updateStr="update tb_mark set mark_num='"+markItem.getMark_num()+"' where mark_id="+markItem.getMark_id();
                        sqLiteDatabase.execSQL(updateStr);
                    }
                }
            }

            itemDelBtn.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            menuBtn.setVisibility(View.VISIBLE);
            isEditState=false;
            for (int i = 0; i < mMarkDataArray.size(); i++) {
                mMarkDataArray.get(i).setEdit_selected(false);
            }
            initMarkData(mMarkDataArray);
            mMarkDataAdapter.notifyDataSetChanged();
        }
    }

    class ImportDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            menuLayout.setVisibility(View.GONE);
        }
    }

    class EditDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            menuLayout.setVisibility(View.GONE);
            menuBtn.setVisibility(View.GONE);
            cancelEditBtn.setVisibility(View.VISIBLE);
            itemDelBtn.setVisibility(View.VISIBLE);
            isEditState=true;
            for (int i = 0; i < mMarkDataArray.size(); i++) {
                mMarkDataArray.get(i).setEdit_selected(false);
            }
            mMarkDataAdapter.notifyDataSetChanged();
        }
    }


    class ItemDelBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int itemCount=mMarkDataArray.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mMarkDataArray.get(i).isEdit_selected()){
                    deleteTypeFromDB(mMarkDataArray.get(i).getMark_id());
                }
            }
            initMarkData(mMarkDataArray);
            mMarkDataAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTypeFromDB(long markID){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

        String markDeleteSqlStr = "delete from tb_mark where mark_id="+markID;
        sqLiteDatabase.execSQL(markDeleteSqlStr);
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder holder) {
        mHelper.startDrag(holder);
    }

}

interface OnStartDragListener{
    void startDrag(RecyclerView.ViewHolder holder);
}

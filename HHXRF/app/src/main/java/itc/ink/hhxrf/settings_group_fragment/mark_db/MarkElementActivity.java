package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.CommonDialog;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class MarkElementActivity extends Activity{
    private TextView activityTitle;
    private ImageView menuBtn;
    private TextView cancelEditBtn;
    private ConstraintLayout menuLayout;
    private TextView importDataBtn;
    private TextView editDataBtn;
    private RecyclerView markElementRV;
    private MarkElementDataAdapter mMarkElementDataAdapter;
    private List<MarkElementDataMode> mMarkElementDataArray=new ArrayList<>();

    public static boolean isEditState=false;
    private ImageView itemDelBtn;
    private long markID=0;
    private String markNum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Intent intent=getIntent();
        markID=intent.getLongExtra("MARK_ID",0);
        markNum=intent.getStringExtra("MARK_NUM");

        setContentView(R.layout.activity_mark_element);

        activityTitle=findViewById(R.id.mark_Element_Top_Navigation_Activity_Title);
        activityTitle.setText(markNum);

        initMarkData(mMarkElementDataArray);
        mMarkElementDataAdapter=new MarkElementDataAdapter(this,mMarkElementDataArray,new AddItemCallBack());

        isEditState=false;

        menuBtn=findViewById(R.id.mark_Element_Activity_Menu_Btn);
        menuBtn.setOnClickListener(new MenuBtnClickListener());
        cancelEditBtn=findViewById(R.id.mark_Element_Cancel_Edit_Btn);
        cancelEditBtn.setOnClickListener(new CancelEditBtnClickListener());
        menuLayout=findViewById(R.id.mark_Element_Activity_Menu_Layout);
        importDataBtn=findViewById(R.id.mark_Element_Activity_Import_Data_Menu_Btn);
        importDataBtn.setOnClickListener(new ImportDataBtnClickListener());
        editDataBtn=findViewById(R.id.mark_Element_Activity_Edit_Data_Menu_Btn);
        editDataBtn.setOnClickListener(new EditDataBtnClickListener());

        markElementRV=findViewById(R.id.mark_Element_Item_RV);
        markElementRV.setAdapter(mMarkElementDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        markElementRV.setLayoutManager(contentRvLayoutManager);

        itemDelBtn=findViewById(R.id.mark_Element_Item_Del_Btn);
        itemDelBtn.setOnClickListener(new ItemDelBtnClickListener());
    }

    public void onBackBtnClick(View view){
        finish();
    }


    public void initMarkData(List<MarkElementDataMode> mMarkElementDataArray) {
        mMarkElementDataArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkElementActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_mark_element where mark_id="+markID;
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            MarkElementDataMode markElementDataItem=new MarkElementDataMode(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getLong(4),false);
            mMarkElementDataArray.add(markElementDataItem);
        }

        MarkElementDataMode item_Add_Btn=new MarkElementDataMode(-1,"",getString(R.string.mark_element_activity_title),"",-1,false);
        mMarkElementDataArray.add(item_Add_Btn);
    }

    class AddItemCallBack implements MarkElementDataAdapter.AddItemCallBack{
        @Override
        public void addItem() {

            new CommonDialog(MarkElementActivity.this, getString(R.string.mark_element_activity_dialog_hint), new CommonDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, String contentValue, boolean confirm) {
                    if (confirm) {
                        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkElementActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
                        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
                        String sqlStr = "select element_id from tb_element_lib_info where element_name='"+contentValue+"'";
                        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
                        int elementID=0;
                        if (cursor.moveToNext()){
                            elementID=cursor.getInt(0);
                        }
                        if(elementID!=0){
                            ContentValues markElementValues = new ContentValues();
                            markElementValues.put("element_id", elementID);
                            markElementValues.put("element_name", contentValue);
                            markElementValues.put("element_min_value", "0");
                            markElementValues.put("element_max_value", "100");
                            markElementValues.put("mark_id", markID);
                            sqLiteDatabase.insert("tb_mark_element","",markElementValues);

                            initMarkData(mMarkElementDataArray);
                            mMarkElementDataAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MarkElementActivity.this,"元素表中没有此元素，请重新输入！",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        dialog.dismiss();
                    }

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    finish();
                }
            }).setTitle(getString(R.string.dialog_title_tip_text))
                    .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                    .setPositiveButton(getString(R.string.dialog_positive_save_btn_text))
                    .show();
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
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkElementActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            for(MarkElementDataMode markItem:mMarkElementDataArray){
                String checkOlderValueStr="select element_min_value element_max_value from tb_mark_element where mark_id="+markItem.getMark_id()+" and element_id="+markItem.getElement_id();
                System.out.println("查询语句->"+checkOlderValueStr);
                Cursor olderValueCursor=sqLiteDatabase.rawQuery(checkOlderValueStr,null);
                if(olderValueCursor.moveToNext()){
                    if(!markItem.getElement_min_value().equals(olderValueCursor.getString(0))||!markItem.getElement_max_value().equals(olderValueCursor.getString(1))){
                        String updateStr="update tb_mark_element set element_min_value='"+markItem.getElement_min_value()+"' , element_max_value='"+markItem.getElement_max_value()+"' where mark_id="+markItem.getMark_id()+" and element_id="+markItem.getElement_id();
                        sqLiteDatabase.execSQL(updateStr);
                    }
                }
            }

            itemDelBtn.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            menuBtn.setVisibility(View.VISIBLE);
            isEditState=false;
            for (int i = 0; i < mMarkElementDataArray.size(); i++) {
                mMarkElementDataArray.get(i).setEdit_selected(false);
            }
            initMarkData(mMarkElementDataArray);
            mMarkElementDataAdapter.notifyDataSetChanged();
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
            for (int i = 0; i < mMarkElementDataArray.size(); i++) {
                mMarkElementDataArray.get(i).setEdit_selected(false);
            }
            mMarkElementDataAdapter.notifyDataSetChanged();
        }
    }


    class ItemDelBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int itemCount=mMarkElementDataArray.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mMarkElementDataArray.get(i).isEdit_selected()){
                    deleteTypeFromDB(mMarkElementDataArray.get(i).getMark_id(),mMarkElementDataArray.get(i).getElement_id());
                }
            }
            initMarkData(mMarkElementDataArray);
            mMarkElementDataAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTypeFromDB(long markID,int elementID){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(MarkElementActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

        String markElementDeleteSqlStr = "delete from tb_mark_element where mark_id="+markID+" and element_id="+elementID;
        sqLiteDatabase.execSQL(markElementDeleteSqlStr);
    }


}

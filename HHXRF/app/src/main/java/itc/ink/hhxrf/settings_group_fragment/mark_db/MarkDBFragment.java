package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.calibration.TypeCalibrationAddSpOne;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MarkDBFragment extends Fragment {
    private ImageView menuBtn;
    private TextView cancelEditBtn;
    private ConstraintLayout menuLayout;
    private TextView importDataBtn;
    private TextView editDataBtn;
    private RecyclerView markLibRV;
    private MarkDBDataAdapter mMarkDBDataAdapter;
    private List<MarkDBDataMode> mMarkDBDataArray=new ArrayList<>();

    public static boolean isEditState=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMarkDBData(mMarkDBDataArray);
        mMarkDBDataAdapter=new MarkDBDataAdapter(getContext(),mMarkDBDataArray,new AddItemCallBack());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mark_db, container, false);
        isEditState=false;

        menuBtn=rootView.findViewById(R.id.mark_db_Fragment_Menu_Btn);
        menuBtn.setOnClickListener(new MenuBtnClickListener());
        cancelEditBtn=rootView.findViewById(R.id.mark_db_Fragment_Cancel_Edit_Btn);
        cancelEditBtn.setOnClickListener(new CancelEditBtnClickListener());
        menuLayout=rootView.findViewById(R.id.mark_db_Fragment_Menu_Layout);
        importDataBtn=rootView.findViewById(R.id.mark_db_Fragment_Import_Data_Menu_Btn);
        importDataBtn.setOnClickListener(new ImportDataBtnClickListener());
        editDataBtn=rootView.findViewById(R.id.mark_db_Fragment_Edit_Data_Menu_Btn);
        editDataBtn.setOnClickListener(new EditDataBtnClickListener());

        markLibRV=rootView.findViewById(R.id.mark_db_Fragment_Mark_Lib_RV);
        markLibRV.setAdapter(mMarkDBDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
        markLibRV.setLayoutManager(contentRvLayoutManager);

        return rootView;
    }

    public void initMarkDBData(List<MarkDBDataMode> mMarkDBDataArray) {
        mMarkDBDataArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_mark_db";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            MarkDBDataMode markDBDataItem=new MarkDBDataMode(cursor.getLong(0),cursor.getString(1),false);
            mMarkDBDataArray.add(markDBDataItem);
        }

        MarkDBDataMode item_Add_Btn=new MarkDBDataMode(-1,getString(R.string.mark_db_fragment_add_db),false);
        mMarkDBDataArray.add(item_Add_Btn);
    }

    class AddItemCallBack implements MarkDBDataAdapter.AddItemCallBack{
        @Override
        public void addItem() {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            ContentValues markDbValues = new ContentValues();
            markDbValues.put("mark_db_id",System.currentTimeMillis());
            markDbValues.put("mark_db_name", "库001");
            sqLiteDatabase.insert("tb_mark_db","",markDbValues);

            mMarkDBDataArray.clear();
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();
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

            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            /*for(MarkDBDataMode markDBItem:mMarkDBDataArray){
                if(!markDBItem.getMark_lib_name().equals("库001")){
                    String updateStr="update tb_mark_db set mark_db_name='"+markDBItem.getMark_lib_name()+"' where mark_db_name=库001";
                    sqLiteDatabase.execSQL(updateStr);
                }
            }*/

            for(MarkDBDataMode markDBItem:mMarkDBDataArray){
                String checkOlderNameStr="select mark_db_name from tb_mark_db where mark_db_id="+markDBItem.getMark_lib_id();
                Cursor nameCursor=sqLiteDatabase.rawQuery(checkOlderNameStr,null);
                if(nameCursor.moveToNext()){
                    if(!markDBItem.getMark_lib_name().equals(nameCursor.getString(0))){
                        String updateStr="update tb_mark_db set mark_db_name='"+markDBItem.getMark_lib_name()+"' where mark_db_id="+markDBItem.getMark_lib_id();
                        sqLiteDatabase.execSQL(updateStr);
                    }
                }
            }

            view.setVisibility(View.GONE);
            menuBtn.setVisibility(View.VISIBLE);
            isEditState=false;
            for (int i = 0; i < mMarkDBDataArray.size(); i++) {
                mMarkDBDataArray.get(i).setEdit_selected(false);
            }
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();

            menuBtn.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);

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
            isEditState=true;
            for (int i = 0; i < mMarkDBDataArray.size(); i++) {
                mMarkDBDataArray.get(i).setEdit_selected(false);
            }
            mMarkDBDataAdapter.notifyDataSetChanged();
        }
    }




}

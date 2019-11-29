package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class TypeCalibrationActivity extends Activity {
    public static final int NEW_TYPE_CALIBRATION_REQUEST_CODE=0x01;
    public static final String CA_KEY="CA_KEY";
    public static final String CA_NONE_VALUE="CA_NONE";
    private RecyclerView typeCalibrationShowRecyclerView;
    private TypeCalibrationDataAdapter typeCalibrationDataAdapter;
    private List<TypeCalibrationDataMode> mTypeCalibrationListData=new ArrayList<>();

    private TextView editBtn;
    public static boolean isEditState=false;
    private ImageView typeCalibrationDelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_type_calibration);
        typeCalibrationShowRecyclerView=findViewById(R.id.type_Calibration_Item_RV);
        initTypeCalibrationData(mTypeCalibrationListData);
        typeCalibrationDataAdapter=new TypeCalibrationDataAdapter(TypeCalibrationActivity.this, mTypeCalibrationListData,new AddItemCallBack(),typeCalibrationShowRecyclerView);

        isEditState=false;
        editBtn=findViewById(R.id.type_Calibration_Edit_Btn);
        editBtn.setOnClickListener(new EditBtnClickListener());
        typeCalibrationDelBtn=findViewById(R.id.type_Calibration_Item_Del_Btn);
        typeCalibrationDelBtn.setOnClickListener(new ItemDelBtnClickListener());
        typeCalibrationShowRecyclerView.setAdapter(typeCalibrationDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(TypeCalibrationActivity.this, LinearLayoutManager.VERTICAL, false);
        typeCalibrationShowRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTypeCalibrationListData.clear();
        initTypeCalibrationData(mTypeCalibrationListData);
        typeCalibrationDataAdapter.notifyDataSetChanged();
    }

    public void onBackBtnClick(View view){
        finish();
    }

    public void initTypeCalibrationData(List<TypeCalibrationDataMode> mTypeCalibrationListData) {

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(TypeCalibrationActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_type_calibration";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            TypeCalibrationDataMode typeCalibrationDataItem=new TypeCalibrationDataMode(cursor.getString(0),Boolean.parseBoolean(cursor.getString(1)),false,false);
            if (SharedPreferenceUtil.getString(CA_KEY,CA_NONE_VALUE).equals(typeCalibrationDataItem.getCalibration_type_name())){
                typeCalibrationDataItem.setCA_Enable(true);
            }else{
                typeCalibrationDataItem.setCA_Enable(false);
            }
            mTypeCalibrationListData.add(typeCalibrationDataItem);
        }

        TypeCalibrationDataMode item_Add_Btn=new TypeCalibrationDataMode(getString(R.string.calibration_fragment_add_new_type),false,false,false);
        mTypeCalibrationListData.add(item_Add_Btn);
    }

    class EditBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            isEditState=!isEditState;
            if(isEditState){
                editBtn.setText(getString(R.string.cancel_edit_btn_text));
                typeCalibrationDelBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setText(getString(R.string.edit_btn_text));
                typeCalibrationDelBtn.setVisibility(View.GONE);
                for(int i=0;i<mTypeCalibrationListData.size();i++){
                    mTypeCalibrationListData.get(i).setEditSelected(false);
                }
            }
            typeCalibrationDataAdapter.notifyDataSetChanged();
        }
    }

    class AddItemCallBack implements TypeCalibrationDataAdapter.AddItemCallBack{
        @Override
        public void addItem() {
            Intent intent=new Intent();
            intent.setClass(TypeCalibrationActivity.this, TypeCalibrationAddSpOne.class);
            startActivityForResult(intent,NEW_TYPE_CALIBRATION_REQUEST_CODE);
        }
    }

    class ItemDelBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int itemCount=mTypeCalibrationListData.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mTypeCalibrationListData.get(i).isEditSelected()){
                    deleteTypeFromDB(mTypeCalibrationListData.get(i).getCalibration_type_name());
                    mTypeCalibrationListData.remove(i);
                }
            }
            typeCalibrationDataAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTypeFromDB(String typeName){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(TypeCalibrationActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

        String typeCalibrationContentDeleteSqlStr = "delete from tb_type_calibration_content where type_name='"+typeName+"'";
        sqLiteDatabase.execSQL(typeCalibrationContentDeleteSqlStr);

        String typeCalibrationDeleteSqlStr = "delete from tb_type_calibration where type_name='"+typeName+"'";
        sqLiteDatabase.execSQL(typeCalibrationDeleteSqlStr);
    }
}

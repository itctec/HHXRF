package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.unit.UnitFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class TypeCalibrationAddSpOne extends BaseActivity {
    private EditText nameEdit;
    private Button nextStepBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_type_calibration_add_sp_one);

        nameEdit=findViewById(R.id.type_Calibration_Add_Sp_One_Name_Edit);
        nextStepBtn=findViewById(R.id.type_Calibration_Add_Sp_One_Next_Step_Btn);
        nextStepBtn.setOnClickListener(new NextStepBtnClickListener());

    }

    class NextStepBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(hasSameItem(nameEdit.getText().toString().trim())){
                Toast.makeText(TypeCalibrationAddSpOne.this,R.string.calibration_fragment_add_item_same_tip,Toast.LENGTH_SHORT).show();
            }else if(nameEdit.getText().toString().isEmpty()){
                Toast.makeText(TypeCalibrationAddSpOne.this,R.string.calibration_fragment_add_item_empty_tip,Toast.LENGTH_SHORT).show();
            }else{
                insertDataToDB(nameEdit.getText().toString().trim());
                Intent intent=new Intent();
                intent.putExtra("TYPE_NAME",nameEdit.getText().toString().trim());
                intent.setClass(TypeCalibrationAddSpOne.this,TypeCalibrationAddSpTwo.class);
                startActivity(intent);
                finish();
            }
        }

        private boolean hasSameItem(String typeName){
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(TypeCalibrationAddSpOne.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            String sqlStr = "select * from tb_type_calibration where type_name=?";
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{typeName});
            if(cursor.moveToNext()){
                return true;
            }else {
                return false;
            }
        }

        private void insertDataToDB(String typeName){
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(TypeCalibrationAddSpOne.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            ContentValues typeValues = new ContentValues();
            typeValues.put("type_name", typeName);
            typeValues.put("enable_state", "false");
            sqLiteDatabase.insert("tb_type_calibration","",typeValues);

            String sqlStr = "select * from tb_element_lib_info";
            Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
            String unitStr=SharedPreferenceUtil.getString(UnitFragment.KEY_UNIT);
            if(unitStr==null||unitStr.isEmpty()){
                unitStr="%";
            }

            while(cursor.moveToNext()){
                ContentValues contentValues = new ContentValues();
                contentValues.put("element_id", cursor.getString(0));
                contentValues.put("element_name", cursor.getString(1));
                contentValues.put("value_multiplication", "1");
                contentValues.put("value_plus", "0");
                contentValues.put("value_unit", unitStr);
                contentValues.put("type_name", typeName);
                sqLiteDatabase.insert("tb_type_calibration_content","",contentValues);
            }
        }
    }

    public void onBackBtnClick(View view){
        finish();
    }
}

package itc.ink.hhxrf.settings_group_fragment.calibration;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class TypeCalibrationAddSpTwo extends Activity {
    private Switch showChangedItemSwitch;
    private RecyclerView elementRecyclerView;
    private TypeCalibrationElementDataAdapter typeCalibrationElementDataAdapter;
    private List<TypeCalibrationElementDataMode> mElementListData=new ArrayList<>();

    private boolean isShowChangedItem=false;
    private String TypeName="";

    private Button sureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Intent intent=getIntent();
        TypeName=intent.getStringExtra("TYPE_NAME");
        mElementListData=initCompareResultData(TypeName,mElementListData);

        setContentView(R.layout.activity_type_calibration_add_sp_two);

        showChangedItemSwitch=findViewById(R.id.type_Calibration_Add_Sp_Two_Top_Navigation_Show_Changed_Item_Switch);
        showChangedItemSwitch.setOnCheckedChangeListener(new ShowChangedItemSwitchCheckedChangeListener());

        typeCalibrationElementDataAdapter=new TypeCalibrationElementDataAdapter(this, mElementListData);
        elementRecyclerView=findViewById(R.id.type_Calibration_Add_Sp_Two_Element_RV);
        elementRecyclerView.setAdapter(typeCalibrationElementDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        elementRecyclerView.setLayoutManager(contentRvLayoutManager);

        sureBtn=findViewById(R.id.type_Calibration_Add_Sp_Two_Sure_Btn);
        sureBtn.setOnClickListener(new SureBtnClickListener());
    }

    public List<TypeCalibrationElementDataMode> initCompareResultData(String typeName,List<TypeCalibrationElementDataMode> typeCalibrationElementItemArray) {
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        String elementSqlStr;
        Cursor elementListCursor;
        if(isShowChangedItem){
            elementSqlStr = "select * from tb_type_calibration_content where type_name=? and (value_multiplication!='1' or value_plus!='0') ";
            elementListCursor = sqLiteDatabase.rawQuery(elementSqlStr, new String[]{typeName});
        }else{
            elementSqlStr = "select * from tb_type_calibration_content where type_name=?";
            elementListCursor = sqLiteDatabase.rawQuery(elementSqlStr, new String[]{typeName});
        }

        while(elementListCursor.moveToNext()){
            TypeCalibrationElementDataMode typeCalibrationElementDataItem=new TypeCalibrationElementDataMode(elementListCursor.getInt(0),elementListCursor.getString(1),elementListCursor.getString(2),elementListCursor.getString(3),elementListCursor.getString(4));
            typeCalibrationElementItemArray.add(typeCalibrationElementDataItem);
        }
        return typeCalibrationElementItemArray;
    }

    public void onBackBtnClick(View view){
        finish();
    }

    class ShowChangedItemSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            isShowChangedItem=b;
            mElementListData.clear();
            typeCalibrationElementDataAdapter.notifyDataSetChanged();
            mElementListData=initCompareResultData(TypeName,mElementListData);
            typeCalibrationElementDataAdapter.notifyDataSetChanged();
        }
    }

    class SureBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(TypeCalibrationAddSpTwo.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

            for(TypeCalibrationElementDataMode elementItem:mElementListData){
                if(!elementItem.getValue_multiplication().equals("1")||!elementItem.getValue_plus().equals("0")){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("value_multiplication", elementItem.getValue_multiplication());
                    contentValues.put("value_plus", elementItem.getValue_plus());
                    sqLiteDatabase.update("tb_type_calibration_content",contentValues,"type_name=? and element_id=?",new String[]{TypeName,elementItem.getElement_id()+""});
                }
            }

            finish();
        }
    }
}

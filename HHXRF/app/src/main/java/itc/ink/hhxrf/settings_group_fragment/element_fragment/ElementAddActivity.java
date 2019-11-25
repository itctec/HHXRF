package itc.ink.hhxrf.settings_group_fragment.element_fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class ElementAddActivity extends BaseActivity {
    private ImageView backBtn;
    private EditText searchBox;
    private TextView multiChoiceBtn;
    private ImageView addBtn;
    private RecyclerView elementLibRecyclerView;
    private ElementLibDataAdapter elementLibDataAdapter;
    private List<ElementLibDataMode> elementLibItemArray=new ArrayList<ElementLibDataMode>();

    public static boolean isMultiChoiceState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        isMultiChoiceState=false;

        setContentView(R.layout.activity_element_add);

        backBtn=findViewById(R.id.element_Add_Top_Navigation_Back_Btn);
        searchBox=findViewById(R.id.element_Add_SearchBox);
        searchBox.addTextChangedListener(new SearchBoxTextWatcher());
        multiChoiceBtn=findViewById(R.id.element_Add_Top_Navigation_Multi_Sel_Btn);
        multiChoiceBtn.setOnClickListener(new MultiChoiceBtnClickListener());
        addBtn=findViewById(R.id.element_Fragment_Element_Lib_Add_Btn);
        addBtn.setOnClickListener(new AddBtnClickListener());

        initElementLibData("");
        ItemClickCallBack itemClickCallBack=new ItemClickCallBack();
        elementLibDataAdapter=new ElementLibDataAdapter(this, elementLibItemArray,new ItemClickCallBack());
        elementLibRecyclerView=findViewById(R.id.element_Fragment_Element_Lib_RV);
        elementLibRecyclerView.setAdapter(elementLibDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        elementLibRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    public List<ElementLibDataMode> initElementLibData(String elementSearchStrr) {
        elementLibItemArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_element_lib_info where element_name like '"+elementSearchStrr+"%' or element_ordinal like '"+elementSearchStrr+"%'";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            ElementLibDataMode elementLibDataItem=new ElementLibDataMode(cursor.getInt(0),cursor.getString(1),cursor.getString(2),false);
            elementLibItemArray.add(elementLibDataItem);
        }
        return elementLibItemArray;
    }

    class SearchBoxTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            System.out.println(editable.toString());
            initElementLibData(editable.toString());
            elementLibDataAdapter.notifyDataSetChanged();

        }
    }

    class ItemClickCallBack implements ElementLibDataAdapter.ItemClickCallBack{
        @Override
        public void onItemClick(int position) {
            Intent intent=new Intent();
            ArrayList<Integer> itemAddArray=new ArrayList<>();
            itemAddArray.add(elementLibItemArray.get(position).getElement_id());
            intent.putIntegerArrayListExtra("ITEM_ADD_ARRAY",itemAddArray);
            setResult(1,intent);
            finish();
        }
    }

    public void onBackBtnClick(View view){
        Intent intent=new Intent();
        ArrayList<Integer> itemAddArray=new ArrayList<>();
        intent.putIntegerArrayListExtra("ITEM_ADD_ARRAY",itemAddArray);
        setResult(1,intent);
        finish();
    }

    class MultiChoiceBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            isMultiChoiceState=!isMultiChoiceState;
            if(isMultiChoiceState){
                multiChoiceBtn.setText(getString(R.string.element_add_activity_cancel_multi_choice_btn_text));
                backBtn.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);
            }else{
                multiChoiceBtn.setText(getString(R.string.element_add_activity_multi_choice_btn_text));
                backBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.GONE);

                for(int i=0;i<elementLibItemArray.size();i++){
                    elementLibItemArray.get(i).isEditSelected=false;
                }
            }
            elementLibDataAdapter.notifyDataSetChanged();
        }
    }

    class AddBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            ArrayList<Integer> itemAddArray=new ArrayList<>();

            int itemCount=elementLibItemArray.size();
            for (int i=itemCount-1;i>=0;i--){
                if (elementLibItemArray.get(i).isEditSelected){
                    itemAddArray.add(elementLibItemArray.get(i).getElement_id());
                }
            }
            intent.putIntegerArrayListExtra("ITEM_ADD_ARRAY",itemAddArray);
            setResult(1,intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        ArrayList<Integer> itemAddArray=new ArrayList<>();
        intent.putIntegerArrayListExtra("ITEM_ADD_ARRAY",itemAddArray);
        setResult(1,intent);
        finish();
    }
}

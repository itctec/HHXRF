package itc.ink.hhxrf.settings_group_fragment.element_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.ElementShowSimpleItemTouchCallback;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.FragmentIndicatorSimpleItemTouchCallback;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class ElementFragment extends Fragment implements OnStartDragListener{
    public static final int GET_ELEMENT_ADDED_REQUEST_CODE=0x01;
    private RecyclerView elementShowRecyclerView;
    private ElementShowDataAdapter elementShowDataAdapter;
    private List<ElementShowDataMode> mElementShowListData;
    private ItemTouchHelper mHelper;

    private TextView editBtn;
    public static boolean isEditState=false;
    public static List<ElementShowDataMode> mElementWillBeDelete;
    private ImageView elementDelBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mElementShowListData=initElementShowData();
        elementShowDataAdapter=new ElementShowDataAdapter(getContext(), mElementShowListData,this,new AddItemCallBack());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_element, container, false);
        isEditState=false;
        editBtn=rootView.findViewById(R.id.element_Fragment_Edit_Btn);
        editBtn.setOnClickListener(new EditBtnClickListener());
        elementDelBtn=rootView.findViewById(R.id.element_Fragment_Element_Show_Del_Btn);
        elementDelBtn.setOnClickListener(new ElementDelBtnClickListener());
        elementShowRecyclerView=rootView.findViewById(R.id.element_Fragment_Element_Show_RV);
        elementShowRecyclerView.setAdapter(elementShowDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        elementShowRecyclerView.setLayoutManager(contentRvLayoutManager);
        mHelper = new ItemTouchHelper(new ElementShowSimpleItemTouchCallback(getContext(),elementShowDataAdapter, mElementShowListData));
        mHelper.attachToRecyclerView(elementShowRecyclerView);

        return rootView;
    }

    public int checkItemRank(int item_id){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select element_rank_num from tb_element_show_rank_info where element_id=?";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{item_id+""});
        cursor.moveToNext();
        return Integer.parseInt(cursor.getString(0));
    }

    public List<ElementShowDataMode> initElementShowData() {
        List<ElementShowDataMode> elementShowItemArray=new ArrayList<>();

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_element_show_rank_info";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            ElementShowDataMode elementShowDataItem=new ElementShowDataMode(cursor.getInt(0),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)));
            elementShowItemArray.add(elementShowDataItem);
        }

        Collections.sort(elementShowItemArray, new Comparator<ElementShowDataMode>() {

            @Override
            public int compare(ElementShowDataMode o1, ElementShowDataMode o2) {
                if(o1.getElement_rank_num()>o2.getElement_rank_num()){
                    return 1;
                }else if(o1.getElement_rank_num()==o2.getElement_rank_num()){
                    return 0;
                }else{
                    return -1;
                }
            }

        });

        ElementShowDataMode ele_Add_Btn=new ElementShowDataMode(-1,"","添加元素",-1);
        elementShowItemArray.add(ele_Add_Btn);

        return elementShowItemArray;
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder holder) {
        mHelper.startDrag(holder);
    }

    class EditBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            isEditState=!isEditState;
            if(isEditState){
                editBtn.setText(getString(R.string.element_fragment_cancel_edit_btn_text));
                elementDelBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setText(getString(R.string.element_fragment_edit_btn_text));
                elementDelBtn.setVisibility(View.GONE);
                for(int i=0;i<mElementShowListData.size();i++){
                    mElementShowListData.get(i).isEditSelected=false;
                }
            }
            elementShowDataAdapter.notifyDataSetChanged();
        }
    }

    class AddItemCallBack implements ElementShowDataAdapter.AddItemCallBack{
        @Override
        public void addItem() {
            Intent intent=new Intent();
            intent.setClass(getContext(),ElementAddActivity.class);
            startActivityForResult(intent,GET_ELEMENT_ADDED_REQUEST_CODE);
        }
    }

    class ElementDelBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int itemCount=mElementShowListData.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mElementShowListData.get(i).isEditSelected){
                    deleteElementFromDB(mElementShowListData.get(i).getElement_id());
                    mElementShowListData.remove(i);
                }
            }
            elementShowDataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GET_ELEMENT_ADDED_REQUEST_CODE){
            List<Integer> itemIDArray=data.getIntegerArrayListExtra("ITEM_ADD_ARRAY");
            for (int id:itemIDArray){
                if(!checkElementIsAdded(id)){
                    ElementShowDataMode elementItem=createElementShowByID(id);
                    if(elementItem!=null){
                        mElementShowListData.add(mElementShowListData.size()-1,elementItem);
                        insertElementToDB(elementItem);
                    }
                }
            }
            elementShowDataAdapter.notifyDataSetChanged();
        }
    }

    public boolean checkElementIsAdded(int id){
        for (ElementShowDataMode elementShowItem:mElementShowListData){
            if(elementShowItem.getElement_id()==id){
                return true;
            }
        }
        return false;
    }

    public ElementShowDataMode createElementShowByID(int id){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_element_lib_info where element_id=?";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{id+""});
        if(cursor.moveToNext()){
            ElementShowDataMode  elementItem=new ElementShowDataMode(id,cursor.getString(1),cursor.getString(2),mElementShowListData.size()+1);
            return elementItem;
        }else {
            return null;
        }
    }

    public void insertElementToDB(ElementShowDataMode elementItem){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

        String elementInsertSqlStr = "insert into tb_element_show_rank_info(element_id,element_name,element_ordinal,element_rank_num) values ('"+elementItem.getElement_id()+"','"+elementItem.getElement_name()+"','"+elementItem.getElement_ordinal()+"','"+elementItem.getElement_rank_num()+"')";
        sqLiteDatabase.execSQL(elementInsertSqlStr);
    }

    public void deleteElementFromDB(int id){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

        String elementInsertSqlStr = "delete from tb_element_show_rank_info where element_id='"+id+"'";
        sqLiteDatabase.execSQL(elementInsertSqlStr);
    }
}

interface OnStartDragListener{
    void startDrag(RecyclerView.ViewHolder holder);
}

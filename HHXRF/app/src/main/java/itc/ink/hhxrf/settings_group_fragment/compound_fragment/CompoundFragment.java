package itc.ink.hhxrf.settings_group_fragment.compound_fragment;

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

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementLibDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementLibDataMode;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class CompoundFragment extends Fragment {
    private RecyclerView compoundLibRecyclerView;
    private CompoundLibDataAdapter compoundLibDataAdapter;
    private List<CompoundLibDataMode> mCompoundLibListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompoundLibListData=initElementLibData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compound, container, false);

        compoundLibRecyclerView=rootView.findViewById(R.id.compound_Fragment_Compound_Lib_RV);
        compoundLibDataAdapter=new CompoundLibDataAdapter(getContext(), mCompoundLibListData,new ItemClickCallBack(),compoundLibRecyclerView);
        compoundLibRecyclerView.setAdapter(compoundLibDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        compoundLibRecyclerView.setLayoutManager(contentRvLayoutManager);

        return rootView;
    }

    public List<CompoundLibDataMode> initElementLibData() {
        List<CompoundLibDataMode> compoundLibItemArray=new ArrayList<>();

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_compound_lib_info";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            CompoundLibDataMode compoundLibDataItem=new CompoundLibDataMode(cursor.getInt(0),cursor.getString(1),cursor.getString(2),Boolean.parseBoolean(cursor.getString(3)));
            compoundLibItemArray.add(compoundLibDataItem);
        }
        return compoundLibItemArray;
    }

    class ItemClickCallBack implements CompoundLibDataAdapter.ItemClickCallBack{
        @Override
        public void onItemClick(int position) {
        }
    }


}

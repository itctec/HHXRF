package itc.ink.hhxrf.settings_group_fragment.mark_db_fragment;

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
    private List<MarkDBDataMode> mMarkDBDataArray = new ArrayList<>();
    private ConstraintLayout floatMenuOuterLayout;
    private ConstraintLayout floatMenuInnerLayout;
    private TextView menuItemCopyBtn;
    private TextView menuItemDelBtn;
    private TextView menuItemTransportBtn;
    private MarkDBDataMode currentLongClickMarkDBDataItem;

    public static boolean isEditState = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMarkDBData(mMarkDBDataArray);
        mMarkDBDataAdapter = new MarkDBDataAdapter(getContext(), mMarkDBDataArray, new ItemCallBack());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mark_db, container, false);
        isEditState = false;

        menuBtn = rootView.findViewById(R.id.mark_db_Fragment_Menu_Btn);
        menuBtn.setOnClickListener(new MenuBtnClickListener());
        cancelEditBtn = rootView.findViewById(R.id.mark_db_Fragment_Cancel_Edit_Btn);
        cancelEditBtn.setOnClickListener(new CancelEditBtnClickListener());
        menuLayout = rootView.findViewById(R.id.mark_db_Fragment_Menu_Layout);
        importDataBtn = rootView.findViewById(R.id.mark_db_Fragment_Import_Data_Menu_Btn);
        importDataBtn.setOnClickListener(new ImportDataBtnClickListener());
        editDataBtn = rootView.findViewById(R.id.mark_db_Fragment_Edit_Data_Menu_Btn);
        editDataBtn.setOnClickListener(new EditDataBtnClickListener());

        markLibRV = rootView.findViewById(R.id.mark_db_Fragment_Mark_Lib_RV);
        markLibRV.setAdapter(mMarkDBDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
        markLibRV.setLayoutManager(contentRvLayoutManager);

        floatMenuOuterLayout = rootView.findViewById(R.id.mark_db_Fragment_Float_Menu_Outer_Layout);
        floatMenuOuterLayout.setOnClickListener(new FloatMenuOuterLayoutClickListener());
        floatMenuInnerLayout = rootView.findViewById(R.id.mark_db_Fragment_Float_Menu_Inner_Layout);
        menuItemCopyBtn = rootView.findViewById(R.id.mark_db_Fragment_Float_Menu_Item_Copy);
        menuItemCopyBtn.setOnClickListener(new MenuItemCopyBtnClickListener());
        menuItemDelBtn = rootView.findViewById(R.id.mark_db_Fragment_Float_Menu_Item_Del);
        menuItemDelBtn.setOnClickListener(new MenuItemDelBtnClickListener());
        menuItemTransportBtn = rootView.findViewById(R.id.mark_db_Fragment_Float_Menu_Item_Transport);
        menuItemTransportBtn.setOnClickListener(new MenuItemTransportBtnClickListener());

        return rootView;
    }

    public void initMarkDBData(List<MarkDBDataMode> mMarkDBDataArray) {
        mMarkDBDataArray.clear();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_mark_db";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while (cursor.moveToNext()) {
            MarkDBDataMode markDBDataItem = new MarkDBDataMode(cursor.getLong(0), cursor.getString(1), false);
            mMarkDBDataArray.add(markDBDataItem);
        }

        MarkDBDataMode item_Add_Btn = new MarkDBDataMode(-1, getString(R.string.mark_db_fragment_add_db), false);
        mMarkDBDataArray.add(item_Add_Btn);
    }

    class ItemCallBack implements MarkDBDataAdapter.ItemCallBack {
        @Override
        public void addItem() {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            ContentValues markDbValues = new ContentValues();
            markDbValues.put("mark_db_id", System.currentTimeMillis());
            markDbValues.put("mark_db_name", "库001");
            sqLiteDatabase.insert("tb_mark_db", "", markDbValues);

            mMarkDBDataArray.clear();
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemLongClick(int x, int y, MarkDBDataMode markDBDataItem) {
            currentLongClickMarkDBDataItem = markDBDataItem;
            floatMenuOuterLayout.setVisibility(View.VISIBLE);
            if (x < 420) {
                floatMenuInnerLayout.setTranslationX(120);
            } else if (x < 780) {
                floatMenuInnerLayout.setTranslationX(480);
            } else {
                floatMenuInnerLayout.setTranslationX(660);
            }

            if (y < 1115) {
                floatMenuInnerLayout.setTranslationY(180);
            } else if (y < 1443) {
                floatMenuInnerLayout.setTranslationY(508);
            } else {
                floatMenuInnerLayout.setTranslationY(540);
            }

        }
    }

    class MenuBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (menuLayout.getVisibility()) {
                case View.VISIBLE:
                    menuLayout.setVisibility(View.GONE);
                    break;
                case View.GONE:
                    menuLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    class CancelEditBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

            for (MarkDBDataMode markDBItem : mMarkDBDataArray) {
                String checkOlderNameStr = "select mark_db_name from tb_mark_db where mark_db_id=" + markDBItem.getMark_lib_id();
                Cursor nameCursor = sqLiteDatabase.rawQuery(checkOlderNameStr, null);
                if (nameCursor.moveToNext()) {
                    if (!markDBItem.getMark_lib_name().equals(nameCursor.getString(0))) {
                        String updateStr = "update tb_mark_db set mark_db_name='" + markDBItem.getMark_lib_name() + "' where mark_db_id=" + markDBItem.getMark_lib_id();
                        sqLiteDatabase.execSQL(updateStr);
                    }
                }
            }

            view.setVisibility(View.GONE);
            menuBtn.setVisibility(View.VISIBLE);
            isEditState = false;
            for (int i = 0; i < mMarkDBDataArray.size(); i++) {
                mMarkDBDataArray.get(i).setEdit_selected(false);
            }
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();

            menuBtn.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);

        }
    }

    class ImportDataBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            menuLayout.setVisibility(View.GONE);
        }
    }

    class EditDataBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            menuLayout.setVisibility(View.GONE);
            menuBtn.setVisibility(View.GONE);
            cancelEditBtn.setVisibility(View.VISIBLE);
            isEditState = true;
            for (int i = 0; i < mMarkDBDataArray.size(); i++) {
                mMarkDBDataArray.get(i).setEdit_selected(false);
            }
            mMarkDBDataAdapter.notifyDataSetChanged();
        }
    }

    class FloatMenuOuterLayoutClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            floatMenuOuterLayout.setVisibility(View.GONE);
        }
    }

    class MenuItemCopyBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            floatMenuOuterLayout.setVisibility(View.GONE);

            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            ContentValues markDbValues = new ContentValues();
            markDbValues.put("mark_db_id", System.currentTimeMillis());
            markDbValues.put("mark_db_name", currentLongClickMarkDBDataItem.getMark_lib_name() + "_copy");
            sqLiteDatabase.insert("tb_mark_db", "", markDbValues);

            String markCheckStr = "select * from tb_mark where mark_db_id=" + currentLongClickMarkDBDataItem.getMark_lib_id();
            Cursor markCursor = sqLiteDatabase.rawQuery(markCheckStr, null);
            while (markCursor.moveToNext()) {
                ContentValues markValues = new ContentValues();
                markValues.put("mark_id", System.currentTimeMillis());
                markValues.put("mark_name", markCursor.getString(markCursor.getColumnIndex("mark_name")));
                markValues.put("mark_num", markCursor.getString(markCursor.getColumnIndex("mark_num")));
                markValues.put("mark_db_id", Long.parseLong(markDbValues.getAsString("mark_db_id")));
                markValues.put("mark_rank_num", markCursor.getString(markCursor.getColumnIndex("mark_rank_num")));
                sqLiteDatabase.insert("tb_mark", "", markValues);

                String markElementCheckStr = "select * from tb_mark_element where mark_id=" + markCursor.getLong(markCursor.getColumnIndex("mark_id"));
                Cursor markElementCursor = sqLiteDatabase.rawQuery(markElementCheckStr, null);
                while (markElementCursor.moveToNext()) {
                    ContentValues markElementValues = new ContentValues();
                    markElementValues.put("element_id", markElementCursor.getString(0));
                    markElementValues.put("element_name", markElementCursor.getString(1));
                    markElementValues.put("element_min_value", markElementCursor.getString(2));
                    markElementValues.put("element_max_value", markElementCursor.getString(3));
                    markElementValues.put("element_tol_value", markElementCursor.getString(4));
                    markElementValues.put("mark_id",  Long.parseLong(markValues.getAsString("mark_id")));
                    sqLiteDatabase.insert("tb_mark_element", "", markElementValues);
                }
            }

            mMarkDBDataArray.clear();
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();
        }
    }

    class MenuItemDelBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            floatMenuOuterLayout.setVisibility(View.GONE);

            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();

            String markDBDeleteSqlStr = "delete from tb_mark_db where mark_db_id="+currentLongClickMarkDBDataItem.getMark_lib_id();
            sqLiteDatabase.execSQL(markDBDeleteSqlStr);

            mMarkDBDataArray.clear();
            initMarkDBData(mMarkDBDataArray);
            mMarkDBDataAdapter.notifyDataSetChanged();
        }
    }

    class MenuItemTransportBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

}

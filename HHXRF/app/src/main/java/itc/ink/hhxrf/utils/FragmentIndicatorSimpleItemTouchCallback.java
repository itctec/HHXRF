package itc.ink.hhxrf.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.settings_group_fragment.adapter.SettingsGroupIndicatorDataAdapter;

/**
 * Created by xiazdong on 16/10/6.
 */
public class FragmentIndicatorSimpleItemTouchCallback extends ItemTouchHelper.Callback {

    private SettingsGroupIndicatorDataAdapter mSettingsGroupDataAdapter;
    private List<LeftDrawerSubDataMode> mData;
    private Context mContext;
    public FragmentIndicatorSimpleItemTouchCallback(Context mContext, SettingsGroupIndicatorDataAdapter adapter, List<LeftDrawerSubDataMode> data){
        this.mContext=mContext;
        mSettingsGroupDataAdapter = adapter;
        mData = data;
    }
    /**
     * 设置支持的拖拽、滑动的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag,swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        Collections.swap(mData, from, to);

        mSettingsGroupDataAdapter.notifyItemMoved(from, to);

        storeRankData();
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        mData.remove(pos);
        mSettingsGroupDataAdapter.notifyItemRemoved(pos);
    }


    /**
     * 状态改变时回调
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        Log.i("Callback", actionState + "");
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            SettingsGroupIndicatorDataAdapter.VH holder = (SettingsGroupIndicatorDataAdapter.VH)viewHolder;
            holder.itemView.setScaleX(1.1f);
            holder.itemView.setScaleY(1.1f);
        }
    }

    /**
     * 拖拽或滑动完成之后调用，用来清除一些状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        SettingsGroupIndicatorDataAdapter.VH holder = (SettingsGroupIndicatorDataAdapter.VH)viewHolder;
        holder.itemView.setScaleX(1.0f);
        holder.itemView.setScaleY(1.0f);
    }

    public void storeRankData(){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(mContext, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

        for (int i=0;i<mData.size();i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("rank_num", (i+1)+"");
            sqLiteDatabase.update("tb_fragment_rank_info", contentValues, "item_id=?", new String[]{mData.get(i).getItem_id()+""});
        }
    }
}

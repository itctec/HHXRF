package itc.ink.hhxrf.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementShowDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementShowDataMode;
import itc.ink.hhxrf.settings_group_fragment.mark_db.MarkDataAdapter;
import itc.ink.hhxrf.settings_group_fragment.mark_db.MarkDataMode;

/**
 * Created by xiazdong on 16/10/6.
 */
public class MarkSimpleItemTouchCallback extends ItemTouchHelper.Callback {

    private MarkDataAdapter mMarkDataAdapter;
    private List<MarkDataMode> mData;
    private Context mContext;
    public MarkSimpleItemTouchCallback(Context mContext, MarkDataAdapter adapter, List<MarkDataMode> data){
        this.mContext=mContext;
        mMarkDataAdapter = adapter;
        mData = data;
    }
    /**
     * 设置支持的拖拽、滑动的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.ACTION_STATE_IDLE;
        return makeMovementFlags(dragFlag,swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        if(to<mData.size()-1){
            Collections.swap(mData, from, to);

            mMarkDataAdapter.notifyItemMoved(from, to);

            storeRankData();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        mData.remove(pos);
        mMarkDataAdapter.notifyItemRemoved(pos);
    }


    /**
     * 状态改变时回调
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        Log.i("Callback", actionState + "");
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            MarkDataAdapter.VH holder = (MarkDataAdapter.VH)viewHolder;
            holder.itemView.setBackgroundColor(0xffff0000);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 拖拽或滑动完成之后调用，用来清除一些状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        MarkDataAdapter.VH holder = (MarkDataAdapter.VH)viewHolder;
        holder.itemView.setBackgroundColor(0xffffffff);
    }

    public void storeRankData(){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(mContext, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

        for (int i=0;i<mData.size();i++){
            String updateStr="update tb_mark set mark_rank_num='"+(i+1)+"' where mark_id="+mData.get(i).getMark_id();
            sqLiteDatabase.execSQL(updateStr);
        }
    }
}

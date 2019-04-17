package itc.ink.hhxrf.settings_group_fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.utils.OnStartDragListener;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SettingsGroupIndicatorDataAdapter extends RecyclerView.Adapter<SettingsGroupIndicatorDataAdapter.VH>{
    private final static String LOG_TAG = "SettingsGroupIndicatorDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<LeftDrawerSubDataMode> mData;
    private ItemClickCallback mItemClickCallback;
    private SubItemClickListener subItemClickListener=new SubItemClickListener();
    private int preItemPosition=-1;

    public SettingsGroupIndicatorDataAdapter(Context mContext, List<LeftDrawerSubDataMode> mData, ItemClickCallback mItemClickCallback) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemClickCallback=mItemClickCallback;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_settings_indicator_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        LeftDrawerSubDataMode drawerSubDataItem=mData.get(position);
        Tag tag=new Tag();
        tag.position=position;
        tag.title=drawerSubDataItem.getSub_title();
        tag.icon_resource_sel_id=drawerSubDataItem.getIcon_resource_sel_id();
        tag.item_id=drawerSubDataItem.getItem_id();

        holder.settingsIndicatorSubItemLayout.setTag(tag);
        holder.settingsIndicatorSubItemLayout.setOnClickListener(subItemClickListener);
        holder.settingsIndicatorSubItemLayout.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        holder.settingsIndicatorSubItemIcon.setImageResource(drawerSubDataItem.getIcon_resource_unsel_id());
        holder.settingsIndicatorSubItemTitle.setText(drawerSubDataItem.getSub_title());
        holder.settingsIndicatorSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.result_text_dark,null));

        if(position==0&&preItemPosition==-1){
            holder.settingsIndicatorSubItemLayout.setBackgroundResource(R.drawable.round_rectangle_bg_dark);
            holder.settingsIndicatorSubItemIcon.setImageResource(drawerSubDataItem.getIcon_resource_sel_id());
            holder.settingsIndicatorSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.result_text_light,null));
            preItemPosition=0;
        }

        if(MainActivity.commandFromLeftDrawer&&checkItemRank(MainActivity.currentSubFragmentID)-1==position&&preItemPosition==-1){
            holder.settingsIndicatorSubItemLayout.setBackgroundResource(R.drawable.round_rectangle_bg_dark);
            holder.settingsIndicatorSubItemIcon.setImageResource(drawerSubDataItem.getIcon_resource_sel_id());
            holder.settingsIndicatorSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.result_text_light,null));
            preItemPosition=position;
        }
    }

    public int checkItemRank(int item_id){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select rank_num from tb_fragment_rank_info where item_id=?";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{item_id+""});
        cursor.moveToNext();
        return Integer.parseInt(cursor.getString(0));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        public ConstraintLayout settingsIndicatorSubItemLayout;
        public ImageView settingsIndicatorSubItemIcon;
        public TextView settingsIndicatorSubItemTitle;

        public VH(View view) {
            super(view);
            settingsIndicatorSubItemLayout = view.findViewById(R.id.settings_Indicator_Item_Layout);
            settingsIndicatorSubItemIcon = view.findViewById(R.id.settings_Indicator_Item_Icon);
            settingsIndicatorSubItemTitle = view.findViewById(R.id.settings_Indicator_Item_Title);
        }
    }

    class SubItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Tag tag=(Tag)view.getTag();
            if(preItemPosition!=tag.position){
                notifyItemChanged(preItemPosition);
                preItemPosition=tag.position;
                ConstraintLayout settingsIndicatorSubItemLayout=(ConstraintLayout)view;
                settingsIndicatorSubItemLayout.setBackgroundResource(R.drawable.round_rectangle_bg_dark);
                ImageView settingsIndicatorSubItemIcon=(ImageView)settingsIndicatorSubItemLayout.getViewById(R.id.settings_Indicator_Item_Icon);
                settingsIndicatorSubItemIcon.setImageResource(tag.icon_resource_sel_id);
                TextView settingsIndicatorSubItemTitle=(TextView)settingsIndicatorSubItemLayout.getViewById(R.id.settings_Indicator_Item_Title);
                settingsIndicatorSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.result_text_light,null));

                mItemClickCallback.onItemClick(tag.item_id);
            }
        }
    }

    public interface ItemClickCallback{
        void onItemClick(int fragmentID);
    }

    class Tag{
        public int position=0;
        public int item_id=0;
        public String title="";
        public int icon_resource_sel_id=-1;
    }
}

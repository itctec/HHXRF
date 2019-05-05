package itc.ink.hhxrf.left_drawer.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class LeftDrawerSubDataAdapter extends RecyclerView.Adapter<LeftDrawerSubDataAdapter.VH> {
    private final static String LOG_TAG ="LeftDrawerSubDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<LeftDrawerSubDataMode> mData;
    private ItemClickCallback mItemClickCallback;
    private DrawerSubItemClickListener drawerSubItemClickListener=new DrawerSubItemClickListener();

    public LeftDrawerSubDataAdapter(Context mContext, List<LeftDrawerSubDataMode> mData,ItemClickCallback mItemClickCallback) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_item_sub, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        LeftDrawerSubDataMode drawerSubDataItem=mData.get(position);
        Tag tag=new Tag();
        tag.item_id=drawerSubDataItem.getItem_id();

        holder.drawerSubItemLayout.setTag(tag);
        holder.drawerSubItemLayout.setOnClickListener(drawerSubItemClickListener);
        holder.drawerSubItemIcon.setImageResource(drawerSubDataItem.getIcon_resource_unsel_id());
        holder.drawerSubItemTitle.setText(drawerSubDataItem.getSub_title());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ConstraintLayout drawerSubItemLayout;
        private ImageView drawerSubItemIcon;
        private TextView drawerSubItemTitle;


        public VH(View view) {
            super(view);
            drawerSubItemLayout = view.findViewById(R.id.left_Drawer_Sub_Item_Layout);
            drawerSubItemIcon = view.findViewById(R.id.left_Drawer_Sub_Item_Icon);
            drawerSubItemTitle = view.findViewById(R.id.left_Drawer_Sub_Item_Title);
        }
    }

    class DrawerSubItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Tag tag=(Tag)view.getTag();
            mItemClickCallback.onItemClick(tag.item_id);
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

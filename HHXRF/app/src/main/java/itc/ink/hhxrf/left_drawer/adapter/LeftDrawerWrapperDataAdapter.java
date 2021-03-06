package itc.ink.hhxrf.left_drawer.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerWrapperDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class LeftDrawerWrapperDataAdapter extends RecyclerView.Adapter<LeftDrawerWrapperDataAdapter.VH> {
    private final static String LOG_TAG = "LeftDrawerWrapperDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<LeftDrawerWrapperDataMode> mData;
    private ItemClickCallback mItemClickCallback;


    public LeftDrawerWrapperDataAdapter(Context mContext, List<LeftDrawerWrapperDataMode> mData,ItemClickCallback mItemClickCallback) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemClickCallback=mItemClickCallback;
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.BANNER.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_item_banner, parent, false);
            return new LeftDrawerWrapperDataAdapter.VH(view, ITEM_TYPE.BANNER) {
            };
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_item_group, parent, false);
            return new VH(view,ITEM_TYPE.GRID);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (position == 0) {;
        }else{
            LeftDrawerWrapperDataMode drawerWrapperDataItem = mData.get(position-1);

            holder.drawerGroupTitleText.setText(drawerWrapperDataItem.getMain_title());

            ItemClickCallbackIMP itemClickCallbackIMP=new ItemClickCallbackIMP();
            LeftDrawerSubDataAdapter drawerSubDataAdapter = new LeftDrawerSubDataAdapter(getContext(), drawerWrapperDataItem.getSub_item_data_array(),itemClickCallbackIMP);
            holder.drawerGroupRecyclerView.setAdapter(drawerSubDataAdapter);
            RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
            holder.drawerGroupRecyclerView.setLayoutManager(contentRvLayoutManager);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.BANNER.ordinal();
        } else {
            return ITEM_TYPE.GRID.ordinal();
        }
    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView drawerCompanyName;

        private TextView drawerGroupTitleText;
        private RecyclerView drawerGroupRecyclerView;

        public VH(View view) {
            super(view);
        }

        public VH(View view, ITEM_TYPE item_type) {
            this(view);
            if (item_type == ITEM_TYPE.BANNER) {
                drawerCompanyName = view.findViewById(R.id.left_Drawer_Company_Name_Label);
            } else if (item_type == ITEM_TYPE.GRID) {
                drawerGroupTitleText = view.findViewById(R.id.left_Drawer_Group_Title_Text);
                drawerGroupRecyclerView = view.findViewById(R.id.left_Drawer_Group_RecyclerView);
            }
        }
    }

    class ItemClickCallbackIMP implements LeftDrawerSubDataAdapter.ItemClickCallback{
        @Override
        public void onItemClick(int fragmentID) {
            mItemClickCallback.onItemClick(fragmentID);
        }
    }

    public interface ItemClickCallback{
        void onItemClick(int fragmentID);
    }

    private enum ITEM_TYPE {
        BANNER,
        GRID
    }

}

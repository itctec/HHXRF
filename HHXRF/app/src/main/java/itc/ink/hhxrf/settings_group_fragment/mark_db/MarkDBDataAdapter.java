package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.home_fragment.last_report.LastReportDataMode;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementFragment;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MarkDBDataAdapter extends RecyclerView.Adapter<MarkDBDataAdapter.VH> {
    private final static String LOG_TAG = "MarkDBDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<MarkDBDataMode> mData;
    private AddItemCallBack mAddItemCallBack;

    public MarkDBDataAdapter(Context mContext, List<MarkDBDataMode> mData,AddItemCallBack mAddItemCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mAddItemCallBack=mAddItemCallBack;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mark_db_grid_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        MarkDBDataMode markDBDataItem=mData.get(position);

        holder.markDBIcon.setImageResource(R.drawable.mark_db_icon_normal);
        holder.markDBNameLabel.setText(markDBDataItem.getMark_lib_name());

        holder.itemView.setOnClickListener(null);
        holder.itemView.setVisibility(View.VISIBLE);

        if(MarkDBFragment.isEditState){
            if(markDBDataItem.isEdit_selected()){
                holder.markDBIcon.setImageResource(R.drawable.mark_db_edit_selected);
            }else{
                holder.markDBIcon.setImageResource(R.drawable.mark_db_edit);
            }
            holder.markDBNameLabel.setEnabled(true);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemEditClickListener());
            if(position==mData.size()-1){
                holder.itemView.setVisibility(View.GONE);
            }
        }else{
            holder.markDBNameLabel.setEnabled(false);
            if(position==mData.size()-1){
                holder.markDBIcon.setImageResource(R.drawable.mark_db_icon_add);
                holder.itemView.setOnClickListener(new AddItemClickListener());
            }else {
                holder.markDBIcon.setImageResource(R.drawable.mark_db_icon_normal);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ItemEditClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            mData.get(position).setEdit_selected(!mData.get(position).isEdit_selected());
            ConstraintLayout itemView=(ConstraintLayout)view;
            ImageView markDBIcon=itemView.findViewById(R.id.element_Show_Item_Select_Icon);
            if(mData.get(position).isEdit_selected()){
                markDBIcon.setImageResource(R.drawable.mark_db_edit_selected);
            }else{
                markDBIcon.setImageResource(R.drawable.mark_db_edit);
            }
        }
    }

    class AddItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mAddItemCallBack.addItem();
        }
    }


    public interface AddItemCallBack{
        public void addItem();
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ImageView markDBIcon;
        public TextView markDBNameLabel;

        public VH(View view) {
            super(view);
            markDBIcon = view.findViewById(R.id.mark_DB_Icon);
            markDBNameLabel = view.findViewById(R.id.mark_DB_Name_Edit_Label);
        }
    }



}

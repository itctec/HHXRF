package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ItemCallBack mItemCallBack;

    public MarkDBDataAdapter(Context mContext, List<MarkDBDataMode> mData,ItemCallBack mItemCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemCallBack=mItemCallBack;
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
        if (holder.markDBNameLabel.getTag() != null && holder.markDBNameLabel.getTag() instanceof TextWatcher) {
            holder.markDBNameLabel.removeTextChangedListener((TextWatcher) holder.markDBNameLabel.getTag());
        }
        holder.markDBNameLabel.setText(mData.get(position).getMark_lib_name());
        TextWatcher multiplicationEditWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mData.get(position).setMark_lib_name(s.toString());
            }
        };
        holder.markDBNameLabel.addTextChangedListener(multiplicationEditWatcher);
        holder.markDBNameLabel.setTag(multiplicationEditWatcher);

        holder.itemView.setOnClickListener(null);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.markDBIcon.setOnLongClickListener(null);

        if(MarkDBFragment.isEditState){
            if(markDBDataItem.isEdit_selected()){
                holder.markDBIcon.setImageResource(R.drawable.mark_db_edit_selected);
            }else{
                holder.markDBIcon.setImageResource(R.drawable.mark_db_edit);
            }
            holder.markDBNameLabel.setEnabled(true);
            holder.markDBIcon.setTag(position);
            holder.markDBIcon.setOnClickListener(new ItemEditClickListener());
            if(position==mData.size()-1){
                holder.itemView.setVisibility(View.GONE);
            }
        }else{
            holder.markDBNameLabel.setEnabled(false);
            if(position==mData.size()-1){
                holder.markDBIcon.setImageResource(R.drawable.mark_db_icon_add);
                holder.markDBIcon.setOnClickListener(new AddItemClickListener());
            }else {
                holder.markDBIcon.setImageResource(R.drawable.mark_db_icon_normal);
                holder.markDBIcon.setOnClickListener(new ItemClickListener(markDBDataItem.getMark_lib_id(),markDBDataItem.getMark_lib_name()));
                holder.markDBIcon.setOnLongClickListener(new ItemLongClickListener(markDBDataItem));
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
            ImageView markDBIcon=(ImageView)view;
            if(mData.get(position).isEdit_selected()){
                markDBIcon.setImageResource(R.drawable.mark_db_edit_selected);
            }else{
                markDBIcon.setImageResource(R.drawable.mark_db_edit);
            }
        }
    }

    class ItemClickListener implements View.OnClickListener{
        private long markDBID=0;
        private String markDBName="";

        public ItemClickListener(long markDBID, String markDBName) {
            this.markDBID = markDBID;
            this.markDBName = markDBName;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(getContext(),MarkActivity.class);
            intent.putExtra("MARK_DB_ID",markDBID);
            intent.putExtra("MARK_DB_NAME",markDBName);
            getContext().startActivity(intent);
        }
    }

    class ItemLongClickListener implements View.OnLongClickListener{
        private MarkDBDataMode markDBDataItem;

        public ItemLongClickListener(MarkDBDataMode markDBDataItem) {
            this.markDBDataItem = markDBDataItem;
        }

        @Override
        public boolean onLongClick(View view) {
            int[] location=new int[2];
            view.getLocationInWindow(location);
            mItemCallBack.onItemLongClick(location[0],location[1],markDBDataItem);
            return true;
        }
    }

    class AddItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mItemCallBack.addItem();
        }
    }


    public interface ItemCallBack{
        void addItem();
        void onItemLongClick(int x,int y,MarkDBDataMode markDBDataItem);
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ImageView markDBIcon;
        public EditText markDBNameLabel;

        public VH(View view) {
            super(view);
            markDBIcon = view.findViewById(R.id.mark_DB_Icon);
            markDBNameLabel = view.findViewById(R.id.mark_DB_Name_Edit_Label);
        }
    }



}

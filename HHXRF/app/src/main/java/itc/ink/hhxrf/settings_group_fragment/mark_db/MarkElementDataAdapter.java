package itc.ink.hhxrf.settings_group_fragment.mark_db;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MarkElementDataAdapter extends RecyclerView.Adapter<MarkElementDataAdapter.VH>{
    private final static String LOG_TAG = "MarkElementDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<MarkElementDataMode> mData;
    private AddItemCallBack mAddItemCallBack;

    public MarkElementDataAdapter(Context mContext, List<MarkElementDataMode> mData, AddItemCallBack mAddItemCallBack) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mark_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        MarkElementDataMode markElementDataItem=mData.get(position);
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.markItemNameIcon.setText(markElementDataItem.getElement_name());
        holder.markItemNameIcon.setBackgroundResource(R.drawable.round_line_bg_gray);
        if (holder.markNum.getTag() != null && holder.markNum.getTag() instanceof TextWatcher) {
            holder.markNum.removeTextChangedListener((TextWatcher) holder.markNum.getTag());
        }
        holder.markNum.setText(mData.get(position).getElement_min_value());
        TextWatcher multiplicationEditWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mData.get(position).setElement_min_value(s.toString());
            }
        };
        holder.markNum.addTextChangedListener(multiplicationEditWatcher);
        holder.markNum.setTag(multiplicationEditWatcher);

        holder.itemView.setOnClickListener(null);
        holder.itemView.setVisibility(View.VISIBLE);

        if(MarkElementActivity.isEditState){
            if(markElementDataItem.isEdit_selected()){
                holder.itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                holder.markSelBtn.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.markSelBtn.setImageResource(R.drawable.check_box_unsel_icon);
            }
            holder.markNum.setEnabled(true);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemEditClickListener());
            holder.markSelBtn.setVisibility(View.VISIBLE);
            holder.markDragBtn.setVisibility(View.GONE);
            if(position==mData.size()-1){
                holder.itemView.setVisibility(View.GONE);
            }
        }else{
            holder.markSelBtn.setVisibility(View.GONE);
            holder.markNum.setEnabled(false);
            if(position==mData.size()-1){
                holder.markDragBtn.setVisibility(View.GONE);
                holder.markItemNameIcon.setBackgroundResource(R.drawable.element_add_btn_icon);
                holder.itemView.setOnClickListener(new AddItemClickListener());
            }else {
                holder.itemView.setOnClickListener(new ItemClickListener());
                holder.markDragBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface AddItemCallBack{
        public void addItem();
    }

    class ItemEditClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            mData.get(position).setEdit_selected(!mData.get(position).isEdit_selected());
            ConstraintLayout itemView=(ConstraintLayout)view;
            ImageView itemSelIcon=itemView.findViewById(R.id.mark_Item_Select_Icon);
            if(mData.get(position).isEdit_selected()){
                itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                itemSelIcon.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                itemView.setBackgroundColor(Color.WHITE);
                itemSelIcon.setImageResource(R.drawable.check_box_unsel_icon);
            }
        }
    }

    class ItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }

    class AddItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mAddItemCallBack.addItem();
        }
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ImageView markSelBtn;
        public TextView markItemNameIcon;
        public EditText markNum;
        public ImageView markDragBtn;

        public VH(View view) {
            super(view);
            markSelBtn=view.findViewById(R.id.mark_Item_Select_Icon);
            markItemNameIcon = view.findViewById(R.id.mark_Item_Name_Icon);
            markNum = view.findViewById(R.id.mark_Item_Num);
            markDragBtn = view.findViewById(R.id.mark_Item_Drag_Icon);
        }
    }

}

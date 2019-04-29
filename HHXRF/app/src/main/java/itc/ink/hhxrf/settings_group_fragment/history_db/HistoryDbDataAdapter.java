package itc.ink.hhxrf.settings_group_fragment.history_db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.home_fragment.last_report.ReportActivity;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementFragment;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementShowDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class HistoryDbDataAdapter extends RecyclerView.Adapter<HistoryDbDataAdapter.VH>{
    private final static String LOG_TAG = "HistoryDbDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<HistoryDBDataMode> mData;
    private ItemChoiceCallBack mItemChoiceCallBack;

    public HistoryDbDataAdapter(Context mContext, List<HistoryDBDataMode> mData,ItemChoiceCallBack mItemChoiceCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemChoiceCallBack =mItemChoiceCallBack;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_db_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        HistoryDBDataMode historyDataItem=mData.get(position);
        if(position%2==0){
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark,null));
        }else{
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light,null));
        }
        holder.historyItemIcon.setText(historyDataItem.getSample_name());
        holder.historyOrdinal.setText(historyDataItem.getTest_datetime());
        holder.historyWay.setText(historyDataItem.getTest_way());
        holder.itemView.setOnClickListener(null);
        holder.itemView.setVisibility(View.VISIBLE);

        if(HistoryFragment.isEditState||CompareDataActivity.isMultiChoiceState){
            if(historyDataItem.isEditSelected){
                holder.itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                holder.historySelBtn.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                if(position%2==0){
                    holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark,null));
                }else{
                    holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light,null));
                }
                holder.historySelBtn.setImageResource(R.drawable.check_box_unsel_icon);
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemEditClickListener());
            holder.historySelBtn.setVisibility(View.VISIBLE);
        }else{
            holder.historySelBtn.setVisibility(View.GONE);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemClickListener());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            Intent intent=new Intent();
            intent.setClass(getContext(), ReportActivity.class);
            getContext().startActivity(intent);
        }
    }

    public interface ItemChoiceCallBack{
        void onItemChoiceEqualThanTwo();
        void onItemChoiceLessThanTwo();
    }


    class ItemEditClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            if(CompareDataActivity.isMultiChoiceState){
                if(CompareDataActivity.choiceCount<2||(CompareDataActivity.choiceCount>1&&mData.get(position).isEditSelected)){
                    mData.get(position).isEditSelected=!mData.get(position).isEditSelected;
                    ConstraintLayout itemView=(ConstraintLayout)view;
                    ImageView itemSelIcon=itemView.findViewById(R.id.history_Item_Select_Icon);
                    if(mData.get(position).isEditSelected){
                        CompareDataActivity.choiceCount++;
                        itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                        itemSelIcon.setImageResource(R.drawable.check_box_sel_icon);
                    }else{
                        CompareDataActivity.choiceCount--;
                        if(position%2==0){
                            itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark,null));
                        }else{
                            itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light,null));
                        }
                        itemSelIcon.setImageResource(R.drawable.check_box_unsel_icon);
                    }
                    if(mItemChoiceCallBack!=null){
                        if(CompareDataActivity.choiceCount<2){
                            mItemChoiceCallBack.onItemChoiceLessThanTwo();
                        }else if(CompareDataActivity.choiceCount==2){
                            mItemChoiceCallBack.onItemChoiceEqualThanTwo();
                        }
                    }
                }
            }else{
                mData.get(position).isEditSelected=!mData.get(position).isEditSelected;
                ConstraintLayout itemView=(ConstraintLayout)view;
                ImageView itemSelIcon=itemView.findViewById(R.id.history_Item_Select_Icon);
                if(mData.get(position).isEditSelected){
                    itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                    itemSelIcon.setImageResource(R.drawable.check_box_sel_icon);
                }else{
                    if(position%2==0){
                        itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark,null));
                    }else{
                        itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light,null));
                    }
                    itemSelIcon.setImageResource(R.drawable.check_box_unsel_icon);
                }
            }

        }
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ImageView historySelBtn;
        public TextView historyItemIcon;
        public TextView historyOrdinal;
        public TextView historyWay;

        public VH(View view) {
            super(view);
            historySelBtn=view.findViewById(R.id.history_Item_Select_Icon);
            historyItemIcon = view.findViewById(R.id.history_Item_Sample_Name);
            historyOrdinal = view.findViewById(R.id.history_Item_Datetime_Label);
            historyWay = view.findViewById(R.id.history_Item_Test_Way_Label);
        }
    }

}
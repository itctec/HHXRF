package itc.ink.hhxrf.settings_group_fragment.element_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ElementLibDataAdapter extends RecyclerView.Adapter<ElementLibDataAdapter.VH>{
    private final static String LOG_TAG = "ElementLibDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<ElementLibDataMode> mData;
    private ItemClickCallBack mItemClickCallBack;

    public ElementLibDataAdapter(Context mContext, List<ElementLibDataMode> mData,ItemClickCallBack mItemClickCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemClickCallBack=mItemClickCallBack;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_element_add_lib_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        ElementLibDataMode elementLibDataItem=mData.get(position);
        holder.elementLibItemIcon.setText(elementLibDataItem.getElement_name());

        if(elementLibDataItem.getElement_id()==-1){
            holder.sortLabelLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.sortLabel.setText(elementLibDataItem.getElement_name());
            holder.itemView.setOnClickListener(null);
        }else{
            holder.sortLabelLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.elementOrdinal.setText(elementLibDataItem.getElement_ordinal());
            if(ElementAddActivity.isMultiChoiceState){
                if(elementLibDataItem.isEditSelected){
                    holder.itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                    holder.elementSelBtn.setImageResource(R.drawable.check_box_sel_icon);
                }else{
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.elementSelBtn.setImageResource(R.drawable.check_box_unsel_icon);
                }
                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(new ItemEditClickListener());
                holder.elementSelBtn.setVisibility(View.VISIBLE);
            }else{
                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(new ItemClickListener());
                holder.elementSelBtn.setVisibility(View.GONE);
            }
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
            mItemClickCallBack.onItemClick(position);
        }
    }

    class ItemEditClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            mData.get(position).isEditSelected=!mData.get(position).isEditSelected;
            ConstraintLayout itemView=(ConstraintLayout)view;
            ImageView itemSelIcon=itemView.findViewById(R.id.element_Lib_Item_Select_Icon);
            if(mData.get(position).isEditSelected){
                itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                itemSelIcon.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                itemView.setBackgroundColor(Color.WHITE);
                itemSelIcon.setImageResource(R.drawable.check_box_unsel_icon);
            }
        }
    }

    public interface ItemClickCallBack{
        public void onItemClick(int position);
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ConstraintLayout sortLabelLayout;
        public TextView sortLabel;
        public ConstraintLayout contentLayout;
        public ImageView elementSelBtn;
        public TextView elementLibItemIcon;
        public TextView elementOrdinal;

        public VH(View view) {
            super(view);
            sortLabelLayout=view.findViewById(R.id.element_Lib_Element_Sort_Label_Layout);
            sortLabel=view.findViewById(R.id.element_Lib_Element_Sort_Label);
            contentLayout=view.findViewById(R.id.element_Lib_Element_Content_Layout);
            elementSelBtn=view.findViewById(R.id.element_Lib_Item_Select_Icon);
            elementLibItemIcon = view.findViewById(R.id.element_Lib_Item_Name_Icon);
            elementOrdinal = view.findViewById(R.id.element_Lib_Item_Ordinal);
        }
    }

}

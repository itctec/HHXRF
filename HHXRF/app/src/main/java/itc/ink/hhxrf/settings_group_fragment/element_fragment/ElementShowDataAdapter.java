package itc.ink.hhxrf.settings_group_fragment.element_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.left_drawer.mode.LeftDrawerSubDataMode;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ElementShowDataAdapter extends RecyclerView.Adapter<ElementShowDataAdapter.VH>{
    private final static String LOG_TAG = "ElementShowDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<ElementShowDataMode> mData;
    private OnStartDragListener mDragListener;
    private AddItemCallBack mAddItemCallBack;

    public ElementShowDataAdapter(Context mContext, List<ElementShowDataMode> mData,OnStartDragListener mDragListener,AddItemCallBack mAddItemCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mDragListener=mDragListener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_element_show_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        ElementShowDataMode elementShowDataItem=mData.get(position);
        elementShowDataItem.isEditSelected=false;
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.elementShowItemIcon.setText(elementShowDataItem.getElement_name());
        holder.elementOrdinal.setText(elementShowDataItem.getElement_ordinal());
        holder.itemView.setOnClickListener(null);

        if(ElementFragment.isEditState){
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemEditClickListener());
            holder.elementSelBtn.setImageResource(R.drawable.check_box_unsel_icon);
            holder.elementSelBtn.setVisibility(View.VISIBLE);
            holder.elementShowRankBtn.setVisibility(View.GONE);
            if(position==mData.size()-1){
                holder.itemView.setVisibility(View.GONE);
            }
        }else{
            holder.elementSelBtn.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.VISIBLE);
            if(position==mData.size()-1){
                holder.elementShowRankBtn.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new AddItemClickListener());
            }else {
                holder.elementShowRankBtn.setVisibility(View.VISIBLE);
                holder.elementShowRankBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            mDragListener.startDrag(holder);
                        }
                        return false;
                    }
                });
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
            mData.get(position).isEditSelected=!mData.get(position).isEditSelected;
            ConstraintLayout itemView=(ConstraintLayout)view;
            ImageView itemSelIcon=itemView.findViewById(R.id.element_Show_Item_Select_Icon);
            if(mData.get(position).isEditSelected){
                itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                itemSelIcon.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                itemView.setBackgroundColor(Color.WHITE);
                itemSelIcon.setImageResource(R.drawable.check_box_unsel_icon);
            }
        }
    }

    class AddItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mAddItemCallBack.addItem();
        }
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ImageView elementSelBtn;
        public TextView elementShowItemIcon;
        public TextView elementOrdinal;
        public ImageView elementShowRankBtn;

        public VH(View view) {
            super(view);
            elementSelBtn=view.findViewById(R.id.element_Show_Item_Select_Icon);
            elementShowItemIcon = view.findViewById(R.id.element_Show_Item_Name_Icon);
            elementOrdinal = view.findViewById(R.id.element_Show_Item_Ordinal);
            elementShowRankBtn = view.findViewById(R.id.element_Show_Item_Icon);
        }
    }

}

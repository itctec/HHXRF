package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class TypeCalibrationDataAdapter extends RecyclerView.Adapter<TypeCalibrationDataAdapter.VH>{
    private final static String LOG_TAG = "TypeCalibrationDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<TypeCalibrationDataMode> mData;
    private AddItemCallBack mAddItemCallBack;
    private RecyclerView mRv;

    public TypeCalibrationDataAdapter(Context mContext, List<TypeCalibrationDataMode> mData,AddItemCallBack mAddItemCallBack,RecyclerView mRV) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mAddItemCallBack=mAddItemCallBack;
        this.mRv=mRV;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_type_calibration_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        TypeCalibrationDataMode typeDataItem=mData.get(position);
        holder.itemView.setBackgroundColor(Color.WHITE);

        holder.typeName.setText(typeDataItem.getCalibration_type_name());
        holder.itemView.setOnClickListener(null);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.typeItemIcon.setVisibility(View.GONE);

        if(TypeCalibrationActivity.isEditState){
            if(typeDataItem.isEditSelected()){
                holder.itemView.setBackgroundColor(getContext().getColor(R.color.element_show_item_sel_bg));
                holder.typeSelBtn.setImageResource(R.drawable.check_box_sel_icon);
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.typeSelBtn.setImageResource(R.drawable.check_box_unsel_icon);
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemChoiceClickListener());
            holder.typeSelBtn.setVisibility(View.VISIBLE);
            holder.typeItemSwitchBtn.setVisibility(View.GONE);
            if(position==mData.size()-1){
                holder.itemView.setVisibility(View.GONE);
            }
        }else{
            holder.typeSelBtn.setVisibility(View.GONE);
            if(position==mData.size()-1){
                holder.typeItemIcon.setVisibility(View.VISIBLE);
                holder.typeItemSwitchBtn.setVisibility(View.GONE);
                holder.typeItemIcon.setBackgroundResource(R.drawable.element_add_btn_icon);
                holder.itemView.setOnClickListener(new AddItemClickListener());
            }else {
                holder.itemView.setTag(typeDataItem.getCalibration_type_name());
                holder.itemView.setOnClickListener(new ItemEditClickListener());
                holder.typeItemIcon.setVisibility(View.GONE);
                holder.typeItemSwitchBtn.setVisibility(View.VISIBLE);
                holder.typeItemSwitchBtn.setOnCheckedChangeListener(new ItemCheckListener(typeDataItem.getCalibration_type_name(),typeDataItem));
                if(typeDataItem.isCA_Enable()){
                    holder.typeItemSwitchBtn.setChecked(true);
                }else{
                    holder.typeItemSwitchBtn.setChecked(false);
                }
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
            String typeName=(String) view.getTag();
            Intent intent=new Intent();
            intent.setClass(getContext(),TypeCalibrationAddSpTwo.class);
            intent.putExtra("TYPE_NAME",typeName);
            getContext().startActivity(intent);
        }
    }

    class ItemCheckListener implements CompoundButton.OnCheckedChangeListener{
        private String cAName;
        private TypeCalibrationDataMode typeDataItem;
        public ItemCheckListener(String cAName,TypeCalibrationDataMode typeDataItem) {
            this.cAName=cAName;
            this.typeDataItem=typeDataItem;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                if(!mRv.isComputingLayout()){
                    for(int i=0;i<mData.size();i++){
                        if (mData.get(i).isCA_Enable()){
                            mData.get(i).setCA_Enable(false);
                            notifyItemChanged(i);
                        }
                    }
                }
                SharedPreferenceUtil.putString(TypeCalibrationActivity.CA_KEY,cAName);
                typeDataItem.setCA_Enable(true);
            }else{
                SharedPreferenceUtil.putString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE);
            }
        }
    }

    class ItemChoiceClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position=(int)view.getTag();
            mData.get(position).setEditSelected(!mData.get(position).isEditSelected());
            ConstraintLayout itemView=(ConstraintLayout)view;
            ImageView itemSelIcon=itemView.findViewById(R.id.type_Calibration_Item_Select_Icon);
            if(mData.get(position).isEditSelected()){
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
        public ImageView typeSelBtn;
        public TextView typeItemIcon;
        public TextView typeName;
        public Switch typeItemSwitchBtn;

        public VH(View view) {
            super(view);
            typeSelBtn=view.findViewById(R.id.type_Calibration_Item_Select_Icon);
            typeItemIcon = view.findViewById(R.id.type_Calibration_Item_Icon);
            typeName = view.findViewById(R.id.type_Calibration_Item_Name);
            typeItemSwitchBtn = view.findViewById(R.id.type_Calibration_Item_Switch);
        }
    }

}

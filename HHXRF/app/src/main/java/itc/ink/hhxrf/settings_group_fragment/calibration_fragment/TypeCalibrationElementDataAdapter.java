package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class TypeCalibrationElementDataAdapter extends RecyclerView.Adapter<TypeCalibrationElementDataAdapter.VH>{
    private final static String LOG_TAG = "TypeCalibrationElementDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<TypeCalibrationElementDataMode> mData;

    public TypeCalibrationElementDataAdapter(Context mContext, List<TypeCalibrationElementDataMode> mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_type_calibration_element_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        TypeCalibrationElementDataMode elementDataItem=mData.get(position);
        holder.elementName.setText(elementDataItem.getElement_name());

        if(elementDataItem.getElement_id()==-1){
            holder.sortLabelLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.sortLabel.setText(elementDataItem.getElement_name());
            holder.itemView.setOnClickListener(null);
        }else{
            holder.sortLabelLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.WHITE);

            if (holder.multiplicationValue.getTag() != null && holder.multiplicationValue.getTag() instanceof TextWatcher) {
                holder.multiplicationValue.removeTextChangedListener((TextWatcher) holder.multiplicationValue.getTag());
            }
            holder.multiplicationValue.setText(mData.get(position).getValue_multiplication());
            TextWatcher multiplicationEditWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    mData.get(position).setValue_multiplication(s.toString());
                }
            };
            holder.multiplicationValue.addTextChangedListener(multiplicationEditWatcher);
            holder.multiplicationValue.setTag(multiplicationEditWatcher);

            if (holder.plusValue.getTag() != null && holder.plusValue.getTag() instanceof TextWatcher) {
                holder.plusValue.removeTextChangedListener((TextWatcher) holder.plusValue.getTag());
            }
            holder.plusValue.setText(mData.get(position).getValue_plus());
            TextWatcher plusEditWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    mData.get(position).setValue_plus(s.toString());
                }
            };
            holder.plusValue.addTextChangedListener(plusEditWatcher);
            holder.plusValue.setTag(plusEditWatcher);

            holder.unitValue.setText(elementDataItem.getValue_unit());
        }

        if(position==mData.size()-1){
            holder.itemView.setPadding(holder.itemView.getPaddingLeft(),holder.itemView.getPaddingTop(),holder.itemView.getPaddingRight(),holder.itemView.getPaddingBottom()+205);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        public ConstraintLayout sortLabelLayout;
        public TextView sortLabel;
        public ConstraintLayout contentLayout;
        public TextView elementName;
        public EditText multiplicationValue;
        public EditText plusValue;
        public TextView unitValue;

        public VH(View view) {
            super(view);
            sortLabelLayout=view.findViewById(R.id.type_Calibration_Element_Sort_Label_Layout);
            sortLabel=view.findViewById(R.id.type_Calibration_Element_Sort_Label);
            contentLayout=view.findViewById(R.id.type_Calibration_Element_Content_Layout);
            elementName = view.findViewById(R.id.type_Calibration_Add_Sp_Two_Element_Value);
            multiplicationValue = view.findViewById(R.id.type_Calibration_Add_Sp_Two_Multiplication_Value);
            plusValue = view.findViewById(R.id.type_Calibration_Add_Sp_Two_Plus_Value);
            unitValue = view.findViewById(R.id.type_Calibration_Add_Sp_Two_Unit_Value);
        }
    }

}

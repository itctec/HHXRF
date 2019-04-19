package itc.ink.hhxrf.settings_group_fragment.compound_fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementAddActivity;
import itc.ink.hhxrf.settings_group_fragment.element_fragment.ElementLibDataMode;
import itc.ink.hhxrf.utils.SQLiteDBHelper;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class CompoundLibDataAdapter extends RecyclerView.Adapter<CompoundLibDataAdapter.VH> {
    private final static String LOG_TAG = "ElementLibDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<CompoundLibDataMode> mData;
    private ItemClickCallBack mItemClickCallBack;

    public CompoundLibDataAdapter(Context mContext, List<CompoundLibDataMode> mData, ItemClickCallBack mItemClickCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mItemClickCallBack = mItemClickCallBack;
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_compound_lib_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        CompoundLibDataMode compoundLibDataItem = mData.get(position);
        holder.compoundLibItemIcon.setText(compoundLibDataItem.getCompound_element());

        if (compoundLibDataItem.getCompound_id() == -1) {
            holder.sortLabelLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.sortLabel.setText(compoundLibDataItem.getCompound_element());
            holder.itemView.setOnClickListener(null);
        } else {
            holder.sortLabelLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.compoundName.setText(compoundLibDataItem.getCompound_name());
            if (compoundLibDataItem.isVisibility()) {
                holder.compoundItemSwitch.setChecked(true);
            } else {
                holder.compoundItemSwitch.setChecked(false);
            }
            holder.compoundItemSwitch.setTag(compoundLibDataItem);
            holder.compoundItemSwitch.setOnCheckedChangeListener(new CompoundItemSwitchChangeListener());
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new ItemClickListener());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            mItemClickCallBack.onItemClick(position);
        }
    }

    class CompoundItemSwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();

            CompoundLibDataMode compoundLibDataItem = (CompoundLibDataMode) compoundButton.getTag();
            ContentValues contentValues = new ContentValues();
            contentValues.put("show_state", b + "");
            sqLiteDatabase.update("tb_compound_lib_info", contentValues, "compound_id=?", new String[]{compoundLibDataItem.getCompound_id() + ""});

        }
    }

    public interface ItemClickCallBack {
        public void onItemClick(int position);
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ConstraintLayout sortLabelLayout;
        public TextView sortLabel;
        public ConstraintLayout contentLayout;
        public TextView compoundLibItemIcon;
        public TextView compoundName;
        public Switch compoundItemSwitch;

        public VH(View view) {
            super(view);
            sortLabelLayout = view.findViewById(R.id.compound_Lib_Sort_Label_Layout);
            sortLabel = view.findViewById(R.id.compound_Lib_Sort_Label);
            contentLayout = view.findViewById(R.id.compound_Lib_Content_Layout);
            compoundLibItemIcon = view.findViewById(R.id.compound_Lib_Item_Element_Icon);
            compoundName = view.findViewById(R.id.compound_Lib_Item_Name);
            compoundItemSwitch = view.findViewById(R.id.compound_Lib_Item_Switch);
        }
    }

}

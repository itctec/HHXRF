package itc.ink.hhxrf.settings_group_fragment.history_db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class CompareResultDataAdapter extends RecyclerView.Adapter<CompareResultDataAdapter.VH> {
    private final static String LOG_TAG = "HistoryDbDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<CompareResultDataMode> mData;

    public CompareResultDataAdapter(Context mContext, List<CompareResultDataMode> mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_compare_data_result_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        CompareResultDataMode compareResultDataItem = mData.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark, null));
        } else {
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light, null));
        }
        holder.resultElementName.setText(compareResultDataItem.getElement_name());
        holder.resultSampleOneElementContent.setText(compareResultDataItem.getSample_one_element_content());
        holder.resultSampleTwoElementContent.setText(compareResultDataItem.getSample_two_element_content());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark, null));
        } else {
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light, null));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView resultElementName;
        public TextView resultSampleOneElementContent;
        public TextView resultSampleTwoElementContent;

        public VH(View view) {
            super(view);
            resultElementName = view.findViewById(R.id.compare_Result_Item_Element_Name);
            resultSampleOneElementContent = view.findViewById(R.id.compare_Result_Item_Sample_One_Element_Content);
            resultSampleTwoElementContent = view.findViewById(R.id.compare_Result_Item_Sample_Two_Element_Content);
        }
    }

}

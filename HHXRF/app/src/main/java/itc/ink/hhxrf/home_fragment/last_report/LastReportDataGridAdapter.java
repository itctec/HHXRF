package itc.ink.hhxrf.home_fragment.last_report;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class LastReportDataGridAdapter extends RecyclerView.Adapter<LastReportDataGridAdapter.VH> {
    private final static String LOG_TAG = "LastReportDataListAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<LastReportDataMode> mData;

    public LastReportDataGridAdapter(Context mContext, List<LastReportDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_report_grid_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        LastReportDataMode lastReportDataItem=mData.get(position);

        holder.elementName.setText(lastReportDataItem.getElement_name());
        holder.percentLabel.setText(lastReportDataItem.getElement_percent()+"%");

        if (position>=mData.size()-1){
            holder.lastItemDivider.setVisibility(View.VISIBLE);
        }else{
            holder.lastItemDivider.setVisibility(View.GONE);
        }

    }

    private float dp2px(int dp){
        return Resources.getSystem().getDisplayMetrics().density *dp;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private TextView elementName;
        private TextView percentLabel;
        private View lastItemDivider;

        public VH(View view) {
            super(view);
            elementName = view.findViewById(R.id.last_Report_Grid_Item_Element_Name);
            percentLabel = view.findViewById(R.id.last_Report_Grid_Item_Percent_Label);
            lastItemDivider=view.findViewById(R.id.last_Report_Grid_Last_Item_Divider);
        }
    }



}

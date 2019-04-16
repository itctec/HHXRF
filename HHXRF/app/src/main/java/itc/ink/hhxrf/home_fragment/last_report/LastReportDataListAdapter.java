package itc.ink.hhxrf.home_fragment.last_report;

import android.content.Context;
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

public class LastReportDataListAdapter extends RecyclerView.Adapter<LastReportDataListAdapter.VH> {
    private final static String LOG_TAG = "LastReportDataListAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<LastReportDataMode> mData;

    public LastReportDataListAdapter(Context mContext, List<LastReportDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_report_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        LastReportDataMode lastReportDataItem=mData.get(position);

        if(position%2==0){
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_dark,null));
        }else{
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.home_last_report_item_light,null));
        }

        holder.elementName.setText(lastReportDataItem.getElement_name());

        if(LastReportFragment.showForthColumn){
            holder.operationOneLabel.setText(lastReportDataItem.getElement_range());
            holder.operationTwoLabel.setText(lastReportDataItem.getElement_mean_value());
        }else{
            holder.operationOneLabel.setText(lastReportDataItem.getElement_percent());
            holder.operationTwoLabel.setText(lastReportDataItem.getElement_range());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private TextView elementName;
        private TextView operationOneLabel;
        private TextView operationTwoLabel;

        public VH(View view) {
            super(view);
            elementName = view.findViewById(R.id.last_Report_Item_Element_Name);
            operationOneLabel = view.findViewById(R.id.last_Report_Item_Operation_One_Label);
            operationTwoLabel = view.findViewById(R.id.last_Report_Item_Operation_Two_Label);
        }
    }



}

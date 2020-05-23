package itc.ink.hhxrf.settings_group_fragment.link_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import itc.ink.hhxrf.home_fragment.last_report.ReportActivity;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.CompareDataActivity;
import itc.ink.hhxrf.settings_group_fragment.history_db_fragment.HistoryFragment;
import itc.ink.hhxrf.settings_group_fragment.link_fragment.printer.IPrinterOpertion;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class LinkBluetoothDataAdapter extends RecyclerView.Adapter<LinkBluetoothDataAdapter.VH>{
    private final static String LOG_TAG = "HistoryDbDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    public List<LinkBluetoothDataMode> mData;
    private IPrinterOpertion myOpertion;

    public LinkBluetoothDataAdapter(Context mContext, List<LinkBluetoothDataMode> mData, IPrinterOpertion myOpertion) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.myOpertion=myOpertion;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_link_device_item, parent, false);
        return new VH(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        LinkBluetoothDataMode deviceDataItem=mData.get(position);
        holder.deviceName.setText(deviceDataItem.getDevice_name());
        holder.deviceAddress.setText(deviceDataItem.getDevice_address());
        holder.itemView.setOnClickListener(new ItemClickListener(deviceDataItem.getDevice_name()+deviceDataItem.getDevice_address()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ItemClickListener implements View.OnClickListener{
        private String info="";
        public ItemClickListener(String info) {
            this.info=info;
        }

        @Override
        public void onClick(View view) {
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.length() - 17);
            System.out.println("name:" + name);
            final Intent data = new Intent();
            data.putExtra(LinkFragment.EXTRA_DEVICE_ADDRESS, address);
            data.putExtra(LinkFragment.EXTRA_DEVICE_NAME, name);
            new Thread(new Runnable() {
                public void run() {
                    myOpertion.open(data);
                }
            }).start();
        }
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;

        public VH(View view) {
            super(view);
            deviceName = view.findViewById(R.id.link_Fragment_Item_Bt_Name);
            deviceAddress = view.findViewById(R.id.link_Fragment_Item_Bt_Address);
        }
    }

}

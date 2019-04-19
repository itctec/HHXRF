package itc.ink.hhxrf.settings_group_fragment.unit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class UnitFragment extends Fragment {
    private ImageView percentItemBg;
    private TextView percentTitle;
    private TextView percentSelLabel;
    private ImageView ppmItemBg;
    private TextView ppmTitle;
    private TextView ppmSelLabel;
    private ImageView mgcmItemBg;
    private TextView mgcmTitle;
    private TextView mgcmSelLabel;
    private ImageView umItemBg;
    private TextView umTitle;
    private TextView umSelLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unit, container, false);
        percentItemBg=rootView.findViewById(R.id.unit_Fragment_Percent_Item_Btn_Back);
        percentItemBg.setTag("percent");
        percentItemBg.setOnClickListener(new ItemBgClickListener());
        percentTitle=rootView.findViewById(R.id.unit_Fragment_Percent_Btn_Title);
        percentSelLabel=rootView.findViewById(R.id.unit_Fragment_Percent_Select_Label);
        ppmItemBg=rootView.findViewById(R.id.unit_Fragment_PPM_Item_Btn_Back);
        ppmItemBg.setTag("ppm");
        ppmItemBg.setOnClickListener(new ItemBgClickListener());
        ppmTitle=rootView.findViewById(R.id.unit_Fragment_PPM_Btn_Title);
        ppmSelLabel=rootView.findViewById(R.id.unit_Fragment_PPM_Select_Label);
        mgcmItemBg=rootView.findViewById(R.id.unit_Fragment_MGCM_Item_Btn_Back);
        mgcmItemBg.setTag("mgcm");
        mgcmItemBg.setOnClickListener(new ItemBgClickListener());
        mgcmTitle=rootView.findViewById(R.id.unit_Fragment_MGCM_Btn_Title);
        mgcmSelLabel=rootView.findViewById(R.id.unit_Fragment_MGCM_Select_Label);
        umItemBg=rootView.findViewById(R.id.unit_Fragment_UM_Item_Btn_Back);
        umItemBg.setTag("um");
        umItemBg.setOnClickListener(new ItemBgClickListener());
        umTitle=rootView.findViewById(R.id.unit_Fragment_UM_Btn_Title);
        umSelLabel=rootView.findViewById(R.id.unit_Fragment_UM_Select_Label);
        return rootView;
    }

    class ItemBgClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String tagStr=(String)view.getTag();
            percentItemBg.setBackground(null);
            percentTitle.setTextColor(getResources().getColor(R.color.unit_text_light,null));
            percentSelLabel.setVisibility(View.GONE);
            ppmItemBg.setBackground(null);
            ppmTitle.setTextColor(getResources().getColor(R.color.unit_text_light,null));
            ppmSelLabel.setVisibility(View.GONE);
            mgcmItemBg.setBackground(null);
            mgcmTitle.setTextColor(getResources().getColor(R.color.unit_text_light,null));
            mgcmSelLabel.setVisibility(View.GONE);
            umItemBg.setBackground(null);
            umTitle.setTextColor(getResources().getColor(R.color.unit_text_light,null));
            umSelLabel.setVisibility(View.GONE);

            view.setBackgroundResource(R.drawable.unit_sel_icon);
            switch (tagStr){
                case "percent":
                    percentTitle.setTextColor(getResources().getColor(R.color.unit_text_gray,null));
                    percentSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "ppm":
                    ppmTitle.setTextColor(getResources().getColor(R.color.unit_text_gray,null));
                    ppmSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "mgcm":
                    mgcmTitle.setTextColor(getResources().getColor(R.color.unit_text_gray,null));
                    mgcmSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "um":
                    umTitle.setTextColor(getResources().getColor(R.color.unit_text_gray,null));
                    umSelLabel.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


}

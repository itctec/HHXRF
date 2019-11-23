package itc.ink.hhxrf.settings_group_fragment.decimal_point_fragment;

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

public class DecimalPointFragment extends Fragment {
    private ImageView zeroItemBg;
    private TextView zeroTitle;
    private TextView zeroSelLabel;
    private ImageView oneItemBg;
    private TextView oneTitle;
    private TextView oneSelLabel;
    private ImageView twoItemBg;
    private TextView twoTitle;
    private TextView twoSelLabel;
    private ImageView threeItemBg;
    private TextView threeTitle;
    private TextView threeSelLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_decimal_point, container, false);
        zeroItemBg=rootView.findViewById(R.id.decimal_point_Fragment_Zero_Item_Btn_Back);
        zeroItemBg.setTag("0");
        zeroItemBg.setOnClickListener(new ItemBgClickListener());
        zeroTitle=rootView.findViewById(R.id.decimal_point_Fragment_Zero_Btn_Title);
        zeroSelLabel=rootView.findViewById(R.id.decimal_point_Fragment_Zero_Select_Label);
        oneItemBg=rootView.findViewById(R.id.decimal_point_Fragment_One_Item_Btn_Back);
        oneItemBg.setTag("0.0");
        oneItemBg.setOnClickListener(new ItemBgClickListener());
        oneTitle=rootView.findViewById(R.id.decimal_point_Fragment_One_Btn_Title);
        oneSelLabel=rootView.findViewById(R.id.decimal_point_Fragment_One_Select_Label);
        twoItemBg=rootView.findViewById(R.id.decimal_point_Fragment_Two_Item_Btn_Back);
        twoItemBg.setTag("0.00");
        twoItemBg.setOnClickListener(new ItemBgClickListener());
        twoTitle=rootView.findViewById(R.id.decimal_point_Fragment_Two_Btn_Title);
        twoSelLabel=rootView.findViewById(R.id.decimal_point_Fragment_Two_Select_Label);
        threeItemBg=rootView.findViewById(R.id.decimal_point_Fragment_Three_Item_Btn_Back);
        threeItemBg.setTag("0.000");
        threeItemBg.setOnClickListener(new ItemBgClickListener());
        threeTitle=rootView.findViewById(R.id.decimal_point_Fragment_Three_Btn_Title);
        threeSelLabel=rootView.findViewById(R.id.decimal_point_Fragment_Three_Select_Label);
        return rootView;
    }

    class ItemBgClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String tagStr=(String)view.getTag();
            zeroItemBg.setBackground(null);
            zeroTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_light,null));
            zeroSelLabel.setVisibility(View.GONE);
            oneItemBg.setBackground(null);
            oneTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_light,null));
            oneSelLabel.setVisibility(View.GONE);
            twoItemBg.setBackground(null);
            twoTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_light,null));
            twoSelLabel.setVisibility(View.GONE);
            threeItemBg.setBackground(null);
            threeTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_light,null));
            threeSelLabel.setVisibility(View.GONE);

            view.setBackgroundResource(R.drawable.unit_sel_icon);
            switch (tagStr){
                case "0":
                    zeroTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_gray,null));
                    zeroSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "0.0":
                    oneTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_gray,null));
                    oneSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "0.00":
                    twoTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_gray,null));
                    twoSelLabel.setVisibility(View.VISIBLE);
                    break;
                case "0.000":
                    threeTitle.setTextColor(getResources().getColor(R.color.decimal_point_text_gray,null));
                    threeSelLabel.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


}

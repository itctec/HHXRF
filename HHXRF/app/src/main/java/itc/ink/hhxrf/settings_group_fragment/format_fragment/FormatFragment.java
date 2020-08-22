package itc.ink.hhxrf.settings_group_fragment.format_fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.home_fragment.OnTestingActivity;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class FormatFragment extends Fragment {
    private ConstraintLayout formatOneLayoutBtn;
    private TextView formatOneLayoutBtnTitle;
    private TextView formatOneLayoutBtnSelTip;
    private ConstraintLayout formatTwoLayoutBtn;
    private TextView formatTwoLayoutBtnTitle;
    private TextView formatTwoLayoutBtnSelTip;
    private ConstraintLayout formatThreeLayoutBtn;
    private TextView formatThreeLayoutBtnTitle;
    private TextView formatThreeLayoutBtnSelTip;
    private NumberPicker formatThreeNumPicker;

    public static final String FORMAT_KEY="format";
    public static final String FORMAT_VALUE_HANLINAG="FORMAT_HANLIANG";
    public static final String FORMAT_VALUE_HANLIANG_PAIHAO="FORMAT_HANLIANG_PAIHAO";
    public static final String FORMAT_VALUE_AVE_PAIHAO="FORMAT_AVE_PAIHAO";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_format, container, false);

        formatOneLayoutBtn=rootView.findViewById(R.id.format_Fragment_Format_One_Layout_Btn);
        formatOneLayoutBtn.setOnClickListener(new FormatOneLayoutBtnClickListener());
        formatOneLayoutBtnTitle=rootView.findViewById(R.id.format_Fragment_Format_One_Title);
        formatOneLayoutBtnSelTip=rootView.findViewById(R.id.format_Fragment_Format_One_Selected_Tip);

        formatTwoLayoutBtn=rootView.findViewById(R.id.format_Fragment_Format_Two_Layout_Btn);
        formatTwoLayoutBtn.setOnClickListener(new FormatTwoLayoutBtnClickListener());
        formatTwoLayoutBtnTitle=rootView.findViewById(R.id.format_Fragment_Format_Two_Title);
        formatTwoLayoutBtnSelTip=rootView.findViewById(R.id.format_Fragment_Format_Two_Selected_Tip);

        formatThreeLayoutBtn=rootView.findViewById(R.id.format_Fragment_Format_Three_Layout_Btn);
        formatThreeLayoutBtn.setOnClickListener(new FormatThreeLayoutBtnClickListener());
        formatThreeLayoutBtnTitle=rootView.findViewById(R.id.format_Fragment_Format_Three_Title);
        formatThreeLayoutBtnSelTip=rootView.findViewById(R.id.format_Fragment_Format_Three_Selected_Tip);
        formatThreeNumPicker=rootView.findViewById(R.id.format_Fragment_Format_Three_Number_Picker);
        formatThreeNumPicker.setOnValueChangedListener(new FormatThreeNumPickerValueChangeListener());
        formatThreeNumPicker.setMinValue(0);
        formatThreeNumPicker.setMaxValue(99);
        formatThreeNumPicker.setValue(0);
        setNumberPickerDividerColor(formatThreeNumPicker, Color.TRANSPARENT);

        if(SharedPreferenceUtil.getString(FORMAT_KEY,FORMAT_VALUE_HANLINAG).equals(FORMAT_VALUE_HANLINAG)){
            selectFormat(1);
        }else if(SharedPreferenceUtil.getString(FORMAT_KEY,FORMAT_VALUE_HANLINAG).equals(FORMAT_VALUE_HANLIANG_PAIHAO)){
            selectFormat(2);
        }else if(SharedPreferenceUtil.getString(FORMAT_KEY,FORMAT_VALUE_HANLINAG).equals(FORMAT_VALUE_AVE_PAIHAO)){
            selectFormat(3);
        }

        formatThreeNumPicker.setValue(SharedPreferenceUtil.getInt(FORMAT_VALUE_AVE_PAIHAO,0));
        
        return rootView;
    }

    private void selectFormat(int formatNum){
        formatOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        formatOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        formatOneLayoutBtnSelTip.setVisibility(View.GONE);

        formatTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        formatTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        formatTwoLayoutBtnSelTip.setVisibility(View.GONE);

        formatThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
        formatThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
        formatThreeLayoutBtnSelTip.setVisibility(View.GONE);
        switch (formatNum){
            case 1:
                formatOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                formatOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                formatOneLayoutBtnSelTip.setVisibility(View.VISIBLE);
                SharedPreferenceUtil.putString(FORMAT_KEY,FORMAT_VALUE_HANLINAG);
                OnTestingActivity.currentTestNum=-1;
                break;
            case 2:
                formatTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                formatTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                formatTwoLayoutBtnSelTip.setVisibility(View.VISIBLE);
                SharedPreferenceUtil.putString(FORMAT_KEY,FORMAT_VALUE_HANLIANG_PAIHAO);
                OnTestingActivity.currentTestNum=-1;
                break;
            case 3:
                formatThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
                formatThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
                formatThreeLayoutBtnSelTip.setVisibility(View.VISIBLE);
                SharedPreferenceUtil.putString(FORMAT_KEY,FORMAT_VALUE_AVE_PAIHAO);
                OnTestingActivity.currentTestNum=0;
                break;
        }
    }

    public void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field SelectionDividerField : pickerFields) {
            if (SelectionDividerField.getName().equals("mSelectionDivider")) {
                SelectionDividerField.setAccessible(true);
                try {
                    SelectionDividerField.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    class FormatOneLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectFormat(1);
        }
    }

    class FormatTwoLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectFormat(2);
        }
    }

    class FormatThreeLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectFormat(3);
        }
    }

    class FormatThreeNumPickerValueChangeListener implements NumberPicker.OnValueChangeListener{
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            SharedPreferenceUtil.putInt(FORMAT_VALUE_AVE_PAIHAO,i1);
        }
    }


}

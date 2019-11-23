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

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class FormatFragment extends Fragment {
    private ConstraintLayout formatOneLayoutBtn;
    private TextView formatOneLayoutBtnTitle;
    private TextView formatOneLayoutBtnSelTip;
    private NumberPicker formatOneNumPicker;
    private ConstraintLayout formatTwoLayoutBtn;
    private TextView formatTwoLayoutBtnTitle;
    private TextView formatTwoLayoutBtnSelTip;
    private ConstraintLayout formatThreeLayoutBtn;
    private TextView formatThreeLayoutBtnTitle;
    private TextView formatThreeLayoutBtnSelTip;

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
        formatOneNumPicker=rootView.findViewById(R.id.format_Fragment_Format_One_Number_Picker);
        formatOneNumPicker.setOnValueChangedListener(new FormatOneNumPickerValueChangeListener());
        formatOneNumPicker.setMinValue(0);
        formatOneNumPicker.setMaxValue(99);
        formatOneNumPicker.setValue(0);
        setNumberPickerDividerColor(formatOneNumPicker, Color.TRANSPARENT);

        formatTwoLayoutBtn=rootView.findViewById(R.id.format_Fragment_Format_Two_Layout_Btn);
        formatTwoLayoutBtn.setOnClickListener(new FormatTwoLayoutBtnClickListener());
        formatTwoLayoutBtnTitle=rootView.findViewById(R.id.format_Fragment_Format_Two_Title);
        formatTwoLayoutBtnSelTip=rootView.findViewById(R.id.format_Fragment_Format_Two_Selected_Tip);

        formatThreeLayoutBtn=rootView.findViewById(R.id.format_Fragment_Format_Three_Layout_Btn);
        formatThreeLayoutBtn.setOnClickListener(new FormatThreeLayoutBtnClickListener());
        formatThreeLayoutBtnTitle=rootView.findViewById(R.id.format_Fragment_Format_Three_Title);
        formatThreeLayoutBtnSelTip=rootView.findViewById(R.id.format_Fragment_Format_Three_Selected_Tip);
        
        return rootView;
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
            formatOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            formatOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            formatOneLayoutBtnSelTip.setVisibility(View.VISIBLE);

            formatTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatTwoLayoutBtnSelTip.setVisibility(View.GONE);

            formatThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatThreeLayoutBtnSelTip.setVisibility(View.GONE);
        }
    }

    class FormatTwoLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            formatOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatOneLayoutBtnSelTip.setVisibility(View.GONE);

            formatTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            formatTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            formatTwoLayoutBtnSelTip.setVisibility(View.VISIBLE);

            formatThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatThreeLayoutBtnSelTip.setVisibility(View.GONE);
        }
    }

    class FormatThreeLayoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            formatOneLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatOneLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatOneLayoutBtnSelTip.setVisibility(View.GONE);

            formatTwoLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_light);
            formatTwoLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_gray,null));
            formatTwoLayoutBtnSelTip.setVisibility(View.GONE);

            formatThreeLayoutBtn.setBackgroundResource(R.drawable.round_rectangle_bg_orange);
            formatThreeLayoutBtnTitle.setTextColor(getResources().getColor(R.color.edit_report_text_white,null));
            formatThreeLayoutBtnSelTip.setVisibility(View.VISIBLE);
        }
    }

    class FormatOneNumPickerValueChangeListener implements NumberPicker.OnValueChangeListener{
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            System.out.println("值->"+i1);
        }
    }


}

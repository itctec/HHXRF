package itc.ink.hhxrf.settings_group_fragment.language_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class LanguageFragment extends Fragment {
    public static final String LANGUAGE_KEY = "LANGUAGE";
    public static final int LANGUAGE_VALUE_CHINESE = 0;
    public static final int LANGUAGE_VALUE_ENGLISH = 1;

    private ImageView languageItemBack;
    private TextView languageChineseBtnTitle;
    private TextView languageChineseBtnSelectedTip;
    private TextView languageEnglishBtnTitle;
    private TextView languageEnglishBtnSelectedTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_language, container, false);

        ConstraintLayout languageSwitchBtn = rootView.findViewById(R.id.language_Fragment_Item_Switch_Layout_Btn);
        languageSwitchBtn.setOnClickListener(new TestWaySwitchBtnClickListener());

        languageItemBack = rootView.findViewById(R.id.language_Fragment_Item_Btn_Back);
        languageChineseBtnTitle = rootView.findViewById(R.id.language_Fragment_Chinese_Btn_Title);
        languageChineseBtnSelectedTip = rootView.findViewById(R.id.language_Fragment_Chinese_Btn_Selected_Tip);
        languageEnglishBtnTitle = rootView.findViewById(R.id.language_Fragment_English_Btn_Title);
        languageEnglishBtnSelectedTip = rootView.findViewById(R.id.language_Fragment_English_Btn_Selected_Tip);


        if (SharedPreferenceUtil.getInt(LANGUAGE_KEY) == LANGUAGE_VALUE_CHINESE) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) languageItemBack.getLayoutParams();
            lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp.rightToRight = ConstraintLayout.LayoutParams.UNSET;
            languageItemBack.setLayoutParams(lp);
            languageChineseBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
            languageChineseBtnSelectedTip.setVisibility(View.VISIBLE);
            languageEnglishBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
            languageEnglishBtnSelectedTip.setVisibility(View.GONE);
        } else {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) languageItemBack.getLayoutParams();
            lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            lp.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
            languageItemBack.setLayoutParams(lp);
            languageChineseBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
            languageChineseBtnSelectedTip.setVisibility(View.GONE);
            languageEnglishBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
            languageEnglishBtnSelectedTip.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    class TestWaySwitchBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (SharedPreferenceUtil.getInt(LANGUAGE_KEY) == LANGUAGE_VALUE_CHINESE) {
                SharedPreferenceUtil.putInt(LANGUAGE_KEY, LANGUAGE_VALUE_ENGLISH);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) languageItemBack.getLayoutParams();
                lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                languageItemBack.setLayoutParams(lp);
                languageChineseBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                languageChineseBtnSelectedTip.setVisibility(View.GONE);
                languageEnglishBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                languageEnglishBtnSelectedTip.setVisibility(View.VISIBLE);
            } else {
                SharedPreferenceUtil.putInt(LANGUAGE_KEY, LANGUAGE_VALUE_CHINESE);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) languageItemBack.getLayoutParams();
                lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                languageItemBack.setLayoutParams(lp);
                languageChineseBtnTitle.setTextColor(getResources().getColor(R.color.result_text_black, null));
                languageChineseBtnSelectedTip.setVisibility(View.VISIBLE);
                languageEnglishBtnTitle.setTextColor(getResources().getColor(R.color.result_text_white, null));
                languageEnglishBtnSelectedTip.setVisibility(View.GONE);
            }

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(intent);
        }
    }

}

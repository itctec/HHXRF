package itc.ink.hhxrf.settings_group_fragment.test_time_fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.pull_time_fragment.PullTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class TestTimeFragment extends Fragment {
    public static final String TEST_TIME_KEY="test_time";
    private TextView tipText;
    private EditText timeEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_time, container, false);

        tipText=rootView.findViewById(R.id.test_Time_Fragment_Label_Tip);
        timeEdit=rootView.findViewById(R.id.test_Time_Fragment_Time_Edit);
        timeEdit.setText(""+SharedPreferenceUtil.getInt(TEST_TIME_KEY,15));
        timeEdit.setOnEditorActionListener(new SearchBarEditActionListener());
        timeEdit.addTextChangedListener(new MyTextWatcher());

        return rootView;
    }

    class SearchBarEditActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                int inputTime=1;
                try {
                    inputTime=Integer.parseInt(timeEdit.getText().toString());
                }catch (NumberFormatException e){
                    tipText.setText(R.string.test_time_fragment_label_empty_tip);
                }

                if (inputTime < 1||inputTime>1000) {
                    tipText.setText(R.string.test_time_fragment_label_tip_error);
                }else{
                    SharedPreferenceUtil.putInt(TEST_TIME_KEY,inputTime);
                    Intent intent = new Intent();
                    intent.putExtra("TrigMode",SharedPreferenceUtil.getString(PullTimeFragment.PULL_TIME_KEY,PullTimeFragment.PULL_TIME_VALUE_SHORT));//短按Short   长按Long
                    intent.putExtra("period",SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15));
                    intent.putExtra("MaterialType",SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY,TestWayFragment.TEST_WAY_VALUE_METAL));//金属  Mental 土壤Soil
                    intent.setAction("xray.information");
                    intent.setComponent(new ComponentName("com.example.androidjnitest","com.example.androidjnitest.BroadcastReceiver1"));
                    getContext().sendBroadcast(intent);
                }

            }
            return false;
        }
    }

    class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            tipText.setText(R.string.test_time_fragment_label_tip);
        }
    }


}

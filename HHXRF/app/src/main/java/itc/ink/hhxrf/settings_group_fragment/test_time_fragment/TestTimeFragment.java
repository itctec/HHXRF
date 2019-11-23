package itc.ink.hhxrf.settings_group_fragment.test_time_fragment;

import android.app.Fragment;
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

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class TestTimeFragment extends Fragment {
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
        timeEdit.setOnEditorActionListener(new SearchBarEditActionListener());
        timeEdit.addTextChangedListener(new MyTextWatcher());

        return rootView;
    }

    class SearchBarEditActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                int inputTime=Integer.parseInt(timeEdit.getText().toString());
                if (inputTime < 1||inputTime>1000) {
                    tipText.setText(R.string.test_time_fragment_label_tip_error);
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
            if (timeEdit.getText().toString().trim().isEmpty()){
                tipText.setText(R.string.test_time_fragment_label_tip);
            }
        }
    }


}

package itc.ink.hhxrf.settings_group_fragment.safe_fragment;

import android.app.Fragment;
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
import android.widget.Toast;

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.launching.LoginActivity;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class SafeFragment extends Fragment {
    public static final String PASSWORD_USER_KEY="PASSWORD_USER";
    private TextView passwordInputTip;
    private EditText passwordEditOne;
    private EditText passwordEditTwo;
    private EditText passwordEditThree;
    private EditText passwordEditFour;
    private String passwordStr="";
    private String passwordCatchStr="";
    private int passwordResetStep=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_safe, container, false);

        passwordInputTip=rootView.findViewById(R.id.safe_Fragment_Title_Sub_Tip);

        passwordEditOne=rootView.findViewById(R.id.safe_Fragment_Password_Input_Edit_One);
        passwordEditOne.addTextChangedListener(new PasswordEditOneTextWatcher());
        passwordEditOne.requestFocus();
        passwordEditTwo=rootView.findViewById(R.id.safe_Fragment_Password_Input_Edit_Two);
        passwordEditTwo.addTextChangedListener(new PasswordEditTwoTextWatcher());
        passwordEditThree=rootView.findViewById(R.id.safe_Fragment_Password_Input_Edit_Three);
        passwordEditThree.addTextChangedListener(new PasswordEditThreeTextWatcher());
        passwordEditFour=rootView.findViewById(R.id.safe_Fragment_Password_Input_Edit_Four);
        passwordEditFour.setOnEditorActionListener(new PasswordEditFourEditorActionListener());

        return rootView;
    }

    class PasswordTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    class PasswordEditOneTextWatcher extends PasswordTextWatcher{
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditTwo.requestFocus();
        }
    }
    class PasswordEditTwoTextWatcher extends PasswordTextWatcher{
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditThree.requestFocus();
        }
    }
    class PasswordEditThreeTextWatcher extends PasswordTextWatcher{
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditFour.requestFocus();
        }
    }

    class PasswordEditFourEditorActionListener implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if(i == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                String passwordStr1=passwordEditOne.getText().toString();
                String passwordStr2=passwordEditTwo.getText().toString();
                String passwordStr3=passwordEditThree.getText().toString();
                String passwordStr4=passwordEditFour.getText().toString();

                if(!passwordStr1.isEmpty()&&!passwordStr2.isEmpty()&&!passwordStr3.isEmpty()&&!passwordStr4.isEmpty()){
                    passwordStr=passwordStr1+passwordStr2+passwordStr3+passwordStr4;

                    if(passwordResetStep==1){
                        if(passwordStr.equals(SharedPreferenceUtil.getString(PASSWORD_USER_KEY,"0000"))||passwordStr.equals("1236")){
                            passwordInputTip.setText(R.string.safe_fragment_input_new_password_tip);
                            passwordResetStep=2;
                        }else{
                            Toast.makeText(getContext(),R.string.safe_fragment_input_current_password_error_tip,Toast.LENGTH_SHORT).show();
                            passwordInputTip.setText(R.string.safe_fragment_input_current_password_tip);
                            passwordResetStep=1;
                        }
                    }else if(passwordResetStep==2){
                        passwordInputTip.setText(R.string.safe_fragment_input_new_password_repeat_tip);
                        passwordCatchStr=passwordStr;
                        passwordResetStep=3;
                    }else if(passwordResetStep==3){
                        if(passwordCatchStr.equals(passwordStr)){
                            SharedPreferenceUtil.putString(PASSWORD_USER_KEY,passwordStr);
                            passwordInputTip.setText(R.string.safe_fragment_input_current_password_tip);
                            passwordResetStep=1;
                            Toast.makeText(getContext(),R.string.safe_fragment_input_new_password_success_tip,Toast.LENGTH_SHORT).show();
                        }else{
                            passwordInputTip.setText(R.string.safe_fragment_input_new_password_tip);
                            passwordResetStep=2;
                            Toast.makeText(getContext(),R.string.safe_fragment_input_new_password_repeat_error_tip,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                passwordEditOne.setText("");
                passwordEditTwo.setText("");
                passwordEditThree.setText("");
                passwordEditFour.setText("");
                passwordEditOne.requestFocus();
            }
            return false;
        }
    }

}

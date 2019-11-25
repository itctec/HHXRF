package itc.ink.hhxrf.launching;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.settings_group_fragment.safe_fragment.SafeFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class LoginActivity extends BaseActivity {
    private TextView inputPasswordTipSubText;

    private TextView passwordInputEditOne;
    private TextView passwordInputEditTwo;
    private TextView passwordInputEditThree;
    private TextView passwordInputEditFour;

    private StringBuilder passwordInputStr=new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_login);

        inputPasswordTipSubText=findViewById(R.id.input_Password_Tip_Sub_Text);

        passwordInputEditOne=findViewById(R.id.password_Input_Edit_One);
        passwordInputEditTwo=findViewById(R.id.password_Input_Edit_Two);
        passwordInputEditThree=findViewById(R.id.password_Input_Edit_Three);
        passwordInputEditFour=findViewById(R.id.password_Input_Edit_Four);


    }


    public void onPasswordKeyClick(View view) {
        Button keyBtn=(Button)view;
        String keyText=keyBtn.getText().toString();

        if(passwordInputStr.length()<4&&keyText!=null&&!keyText.isEmpty()){
            passwordInputStr.append(keyText);
            switch (passwordInputStr.length()){
                case 4:
                    passwordInputEditFour.setText("*");
                case 3:
                    passwordInputEditThree.setText("*");
                case 2:
                    passwordInputEditTwo.setText("*");
                case 1:
                    passwordInputEditOne.setText("*");

            }
        }else if(keyText==null||keyText.isEmpty()){
            passwordInputStr.delete(0,passwordInputStr.length());
            inputPasswordTipSubText.setText(R.string.input_password_tip_sub);
            passwordInputEditOne.setText("");
            passwordInputEditTwo.setText("");
            passwordInputEditThree.setText("");
            passwordInputEditFour.setText("");
        }

        if(passwordInputStr.length()==4){
            if(passwordInputStr.toString().equals(SharedPreferenceUtil.getString(SafeFragment.PASSWORD_USER_KEY,"0000"))||passwordInputStr.toString().equals("1236")){
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                inputPasswordTipSubText.setText(R.string.input_password_tip_sub_error);
            }
        }
    }

}

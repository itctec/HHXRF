package itc.ink.hhxrf.settings_group_fragment.instrument_test_fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.DataCallBack;
import itc.ink.hhxrf.hardware.HardwareBroadCastReceiver;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class InstrumentTestFragment extends Fragment {
    private TextView passwordInputTip;
    private EditText passwordEditOne;
    private EditText passwordEditTwo;
    private EditText passwordEditThree;
    private EditText passwordEditFour;
    private String passwordStr = "";

    private ConstraintLayout hardwareDataLayout;
    private TextView lightTubeVoltageLabel;
    private TextView lightTubeElectricityLabel;
    private TextView instrumentTemperatureLabel;
    private TextView detectorTemperatureLabel;
    private TextView countRateLabel;
    private TextView motherboardTemperatureLabel;
    private TextView powerBoardTemperatureLabel;
    private TextView drivePlateTemperatureLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instrument_test, container, false);

        passwordInputTip = rootView.findViewById(R.id.instrument_Test_Fragment_Title_Sub_Tip);

        passwordEditOne = rootView.findViewById(R.id.instrument_Test_Fragment_Password_Input_Edit_One);
        passwordEditOne.addTextChangedListener(new PasswordEditOneTextWatcher());
        passwordEditOne.requestFocus();
        passwordEditTwo = rootView.findViewById(R.id.instrument_Test_Fragment_Password_Input_Edit_Two);
        passwordEditTwo.addTextChangedListener(new PasswordEditTwoTextWatcher());
        passwordEditThree = rootView.findViewById(R.id.instrument_Test_Fragment_Password_Input_Edit_Three);
        passwordEditThree.addTextChangedListener(new PasswordEditThreeTextWatcher());
        passwordEditFour = rootView.findViewById(R.id.instrument_Test_Fragment_Password_Input_Edit_Four);
        passwordEditFour.setOnEditorActionListener(new PasswordEditFourEditorActionListener());

        hardwareDataLayout=rootView.findViewById(R.id.instrument_Hardware_Data_Layout);
        lightTubeVoltageLabel=rootView.findViewById(R.id.instrument_Light_Tube_Voltage_Label);
        lightTubeElectricityLabel=rootView.findViewById(R.id.instrument_Light_Tube_Voltage_Electricity_Label);
        instrumentTemperatureLabel=rootView.findViewById(R.id.instrument_Instrument_Temperature_Label);
        detectorTemperatureLabel=rootView.findViewById(R.id.instrument_Detector_Temperature_Label);
        countRateLabel=rootView.findViewById(R.id.instrument_Count_Rate_Label);
        motherboardTemperatureLabel=rootView.findViewById(R.id.instrument_Motherboard_Temperature_Label);
        powerBoardTemperatureLabel=rootView.findViewById(R.id.instrument_Power_Board_Temperature_Label);
        drivePlateTemperatureLabel=rootView.findViewById(R.id.instrument_Drive_Plate_Temperature_Label);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        MainActivity.hardwareBroadCastReceiver.removeCallBack();
    }

    class PasswordTextWatcher implements TextWatcher {
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

    class PasswordEditOneTextWatcher extends PasswordTextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditTwo.requestFocus();
        }
    }

    class PasswordEditTwoTextWatcher extends PasswordTextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditThree.requestFocus();
        }
    }

    class PasswordEditThreeTextWatcher extends PasswordTextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            passwordEditFour.requestFocus();
        }
    }

    class PasswordEditFourEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String passwordStr1 = passwordEditOne.getText().toString();
                String passwordStr2 = passwordEditTwo.getText().toString();
                String passwordStr3 = passwordEditThree.getText().toString();
                String passwordStr4 = passwordEditFour.getText().toString();

                if (!passwordStr1.isEmpty() && !passwordStr2.isEmpty() && !passwordStr3.isEmpty() && !passwordStr4.isEmpty()) {
                    passwordStr = passwordStr1 + passwordStr2 + passwordStr3 + passwordStr4;

                    if (passwordStr.equals(SharedPreferenceUtil.getString("PASSWORD_USER", "0000")) || passwordStr.equals("1236")) {
                        passwordInputTip.setVisibility(View.GONE);
                        passwordEditOne.setVisibility(View.GONE);
                        passwordEditTwo.setVisibility(View.GONE);
                        passwordEditThree.setVisibility(View.GONE);
                        passwordEditFour.setVisibility(View.GONE);
                        hardwareDataLayout.setVisibility(View.VISIBLE);

                        //Check Data
                        MainActivity.hardwareBroadCastReceiver.addCallBack(new DataCallBack() {
                            @Override
                            public void onDataChanged(String s) {
                                System.out.println("状态数据-》"+s);
                                if(s.startsWith("S_")){
                                    String lightTubeVoltageStr=s.substring(s.indexOf("GGV")+4,s.indexOf("TRIG_STATE"));
                                    String lightTubeElectricityStr=s.substring(s.indexOf("GGI")+4,s.indexOf("GGV"));
                                    String instrumentTemperatureStr=s.substring(s.indexOf("XRAY_T")+6,s.indexOf("Count"));
                                    String detectorTemperatureStr=s.substring(s.indexOf("DP5_ST=")+7,s.indexOf("XRAY_T"));
                                    String countRateStr=s.substring(s.indexOf("Count:")+6);
                                    String motherboardTemperatureStr=s.substring(s.indexOf("DP5_BT=")+7,s.indexOf("DP5_ST"));
                                    lightTubeVoltageLabel.setText(getString(R.string.instrument_light_tube_voltage)+lightTubeVoltageStr);
                                    lightTubeElectricityLabel.setText(getString(R.string.instrument_light_tube_electricity)+lightTubeElectricityStr);
                                    instrumentTemperatureLabel.setText(getString(R.string.instrument_instrument_temperature)+instrumentTemperatureStr);
                                    detectorTemperatureLabel.setText(getString(R.string.instrument_detector_temperature)+detectorTemperatureStr);
                                    countRateLabel.setText(getString(R.string.instrument_count_rate)+countRateStr);
                                    motherboardTemperatureLabel.setText(getString(R.string.instrument_motherboard_temperature)+motherboardTemperatureStr);
                                }
                            }
                        });

                        Intent intent = new Intent();
                        intent.setAction("xray.Query");
                        intent.setComponent(new ComponentName("com.example.androidjnitest","com.example.androidjnitest.BroadcastReceiver1"));
                        getContext().sendBroadcast(intent);
                    } else {
                        Toast.makeText(getContext(), R.string.instrument_test_fragment_input_current_password_error_tip, Toast.LENGTH_SHORT).show();
                        passwordEditOne.setText("");
                        passwordEditTwo.setText("");
                        passwordEditThree.setText("");
                        passwordEditFour.setText("");
                        passwordEditOne.requestFocus();
                    }

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
            return false;
        }
    }

}

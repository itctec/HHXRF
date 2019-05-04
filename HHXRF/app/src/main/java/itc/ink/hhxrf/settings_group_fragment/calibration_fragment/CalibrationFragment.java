package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class CalibrationFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calibration, container, false);

        Button powerCalibrationBtn=rootView.findViewById(R.id.calibration_Fragment_Power_Calibration_Btn);
        powerCalibrationBtn.setOnClickListener(new PowerCalibrationBtnClickListener());

        Button typeCalibrationBtn=rootView.findViewById(R.id.calibration_Fragment_Type_Calibration_Btn);
        typeCalibrationBtn.setOnClickListener(new TypeCalibrationBtnClickListener());

        return rootView;
    }

    class PowerCalibrationBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            //intent.setClass(getContext(),);
            startActivity(intent);
        }
    }

    class TypeCalibrationBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(getContext(),TypeCalibrationActivity.class);
            startActivity(intent);
        }
    }

}

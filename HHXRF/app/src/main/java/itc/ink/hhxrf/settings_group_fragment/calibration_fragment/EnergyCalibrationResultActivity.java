package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class EnergyCalibrationResultActivity extends Activity {
    private TextView resultText;

    public static boolean isEditState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_energy_calibration_result);

        resultText=findViewById(R.id.energy_Calibration_Result_Text);
        resultText.setText(readSummaryDataFromCsv());

    }

    private String readSummaryDataFromCsv() {
        String resultStr="";
        try {

            File csv = new File("data/XRS/Data_S/EnergyCalibResults.csv");
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine();
            String line = "";

            resultStr+="Version\t\tGain\t\tOffset\t\teVCh\t\tFWHM"+"\r\n";
            while ((line = br.readLine()) != null) {
                System.out.println("Data Line-->" + line);
                resultStr+=line.replace(",","\t\t")+"\r\n";

            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            resultStr=e.toString();
        }

        return resultStr;
    }

    public void onBackBtnClick(View view){
        finish();
    }

}

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        try {
            copyFileUsingFileStreams(new File("data/XRS/Data_S/EnergyCalibResults.csv"),
                    new File("data/XRS/Data_T/EnergyCalibResults.csv"));
        }catch (IOException e){
            e.printStackTrace();
        }

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

    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public void onBackBtnClick(View view){
        finish();
    }

}

package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

public class TypeCalibrationDataMode {
    private String calibration_type_name;
    private boolean calibration_type_enable_state=false;
    private boolean isEditSelected=false;
    private boolean isCA_Enable=false;

    public TypeCalibrationDataMode() {
    }

    public TypeCalibrationDataMode(String calibration_type_name, boolean calibration_type_enable_state, boolean isEditSelected, boolean isCA_Enable) {
        this.calibration_type_name = calibration_type_name;
        this.calibration_type_enable_state = calibration_type_enable_state;
        this.isEditSelected = isEditSelected;
        this.isCA_Enable = isCA_Enable;
    }

    public String getCalibration_type_name() {
        return calibration_type_name;
    }

    public void setCalibration_type_name(String calibration_type_name) {
        this.calibration_type_name = calibration_type_name;
    }

    public boolean isCalibration_type_enable_state() {
        return calibration_type_enable_state;
    }

    public void setCalibration_type_enable_state(boolean calibration_type_enable_state) {
        this.calibration_type_enable_state = calibration_type_enable_state;
    }

    public boolean isEditSelected() {
        return isEditSelected;
    }

    public void setEditSelected(boolean editSelected) {
        isEditSelected = editSelected;
    }

    public boolean isCA_Enable() {
        return isCA_Enable;
    }

    public void setCA_Enable(boolean CA_Enable) {
        isCA_Enable = CA_Enable;
    }
}

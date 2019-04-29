package itc.ink.hhxrf.settings_group_fragment.calibration;

public class TypeCalibrationDataMode {
    private String calibration_type_name;
    private boolean calibration_type_enable_state=false;
    private boolean isEditSelected=false;

    public TypeCalibrationDataMode() {
    }

    public TypeCalibrationDataMode(String calibration_type_name, boolean calibration_type_enable_state, boolean isEditSelected) {
        this.calibration_type_name = calibration_type_name;
        this.calibration_type_enable_state = calibration_type_enable_state;
        this.isEditSelected = isEditSelected;
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
}

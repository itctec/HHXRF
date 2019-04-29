package itc.ink.hhxrf.settings_group_fragment.calibration;

public class TypeCalibrationElementDataMode {
    private int element_id;
    private String element_name;
    private String value_multiplication;
    private String value_plus;
    private String value_unit;

    public TypeCalibrationElementDataMode() {
    }

    public TypeCalibrationElementDataMode(int element_id, String element_name, String value_multiplication, String value_plus, String value_unit) {
        this.element_id = element_id;
        this.element_name = element_name;
        this.value_multiplication = value_multiplication;
        this.value_plus = value_plus;
        this.value_unit = value_unit;
    }

    public int getElement_id() {
        return element_id;
    }

    public void setElement_id(int element_id) {
        this.element_id = element_id;
    }

    public String getElement_name() {
        return element_name;
    }

    public void setElement_name(String element_name) {
        this.element_name = element_name;
    }

    public String getValue_multiplication() {
        return value_multiplication;
    }

    public void setValue_multiplication(String value_multiplication) {
        this.value_multiplication = value_multiplication;
    }

    public String getValue_plus() {
        return value_plus;
    }

    public void setValue_plus(String value_plus) {
        this.value_plus = value_plus;
    }

    public String getValue_unit() {
        return value_unit;
    }

    public void setValue_unit(String value_unit) {
        this.value_unit = value_unit;
    }
}

package itc.ink.hhxrf.home_fragment.last_report;

public class LastReportDataMode {
    private String element_name="";
    private String element_percent="";
    private String element_range="";
    private String element_mean_value="";

    public LastReportDataMode() {
    }

    public LastReportDataMode(String element_name, String element_percent, String element_range, String element_mean_value) {
        this.element_name = element_name;
        this.element_percent = element_percent;
        this.element_range = element_range;
        this.element_mean_value = element_mean_value;
    }

    public String getElement_name() {
        return element_name;
    }

    public void setElement_name(String element_name) {
        this.element_name = element_name;
    }

    public String getElement_percent() {
        return element_percent;
    }

    public void setElement_percent(String element_percent) {
        this.element_percent = element_percent;
    }

    public String getElement_range() {
        return element_range;
    }

    public void setElement_range(String element_range) {
        this.element_range = element_range;
    }

    public String getElement_mean_value() {
        return element_mean_value;
    }

    public void setElement_mean_value(String element_mean_value) {
        this.element_mean_value = element_mean_value;
    }
}

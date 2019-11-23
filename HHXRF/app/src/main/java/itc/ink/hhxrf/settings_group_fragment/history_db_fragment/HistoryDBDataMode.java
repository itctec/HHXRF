package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

public class HistoryDBDataMode {
    private String sample_name;
    private String test_datetime;
    private String test_way;
    public boolean isEditSelected=false;

    public HistoryDBDataMode() {
    }

    public HistoryDBDataMode(String sample_name, String test_datetime, String test_way, boolean isEditSelected) {
        this.sample_name = sample_name;
        this.test_datetime = test_datetime;
        this.test_way = test_way;
        this.isEditSelected = isEditSelected;
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    public String getTest_datetime() {
        return test_datetime;
    }

    public void setTest_datetime(String test_datetime) {
        this.test_datetime = test_datetime;
    }

    public String getTest_way() {
        return test_way;
    }

    public void setTest_way(String test_way) {
        this.test_way = test_way;
    }

    public boolean isEditSelected() {
        return isEditSelected;
    }

    public void setEditSelected(boolean editSelected) {
        isEditSelected = editSelected;
    }
}

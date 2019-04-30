package itc.ink.hhxrf.settings_group_fragment.mark_db;

public class MarkElementDataMode {
    private int element_id;
    private String element_name;
    private String element_min_value;
    private String element_max_value;
    private long mark_id;
    private boolean edit_selected=false;

    public MarkElementDataMode() {
    }

    public MarkElementDataMode(int element_id, String element_name, String element_min_value, String element_max_value, long mark_id, boolean edit_selected) {
        this.element_id = element_id;
        this.element_name = element_name;
        this.element_min_value = element_min_value;
        this.element_max_value = element_max_value;
        this.mark_id = mark_id;
        this.edit_selected = edit_selected;
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

    public String getElement_min_value() {
        return element_min_value;
    }

    public void setElement_min_value(String element_min_value) {
        this.element_min_value = element_min_value;
    }

    public String getElement_max_value() {
        return element_max_value;
    }

    public void setElement_max_value(String element_max_value) {
        this.element_max_value = element_max_value;
    }

    public long getMark_id() {
        return mark_id;
    }

    public void setMark_id(long mark_id) {
        this.mark_id = mark_id;
    }

    public boolean isEdit_selected() {
        return edit_selected;
    }

    public void setEdit_selected(boolean edit_selected) {
        this.edit_selected = edit_selected;
    }
}

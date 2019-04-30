package itc.ink.hhxrf.settings_group_fragment.mark_db;

public class MarkDBDataMode {
    private String mark_lib_name;
    private boolean edit_selected=false;

    public MarkDBDataMode() {
    }

    public MarkDBDataMode(String mark_lib_name, boolean edit_selected) {
        this.mark_lib_name = mark_lib_name;
        this.edit_selected = edit_selected;
    }

    public String getMark_lib_name() {
        return mark_lib_name;
    }

    public void setMark_lib_name(String mark_lib_name) {
        this.mark_lib_name = mark_lib_name;
    }

    public boolean isEdit_selected() {
        return edit_selected;
    }

    public void setEdit_selected(boolean edit_selected) {
        this.edit_selected = edit_selected;
    }
}

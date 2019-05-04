package itc.ink.hhxrf.settings_group_fragment.mark_db_fragment;

public class MarkDBDataMode {
    private long mark_lib_id;
    private String mark_lib_name;
    private boolean edit_selected=false;

    public MarkDBDataMode() {
    }

    public MarkDBDataMode(long mark_lib_id, String mark_lib_name, boolean edit_selected) {
        this.mark_lib_id = mark_lib_id;
        this.mark_lib_name = mark_lib_name;
        this.edit_selected = edit_selected;
    }

    public long getMark_lib_id() {
        return mark_lib_id;
    }

    public void setMark_lib_id(long mark_lib_id) {
        this.mark_lib_id = mark_lib_id;
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

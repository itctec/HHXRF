package itc.ink.hhxrf.settings_group_fragment.mark_db_fragment;

public class MarkDataMode {
    private long mark_id;
    private String mark_name;
    private String mark_num;
    private String mark_lib_id;
    private int mark_rank_num;
    private boolean edit_selected=false;

    public MarkDataMode() {
    }

    public MarkDataMode(long mark_id, String mark_name, String mark_num, String mark_lib_id, int mark_rank_num, boolean edit_selected) {
        this.mark_id = mark_id;
        this.mark_name = mark_name;
        this.mark_num = mark_num;
        this.mark_lib_id = mark_lib_id;
        this.mark_rank_num = mark_rank_num;
        this.edit_selected = edit_selected;
    }

    public long getMark_id() {
        return mark_id;
    }

    public void setMark_id(long mark_id) {
        this.mark_id = mark_id;
    }

    public String getMark_name() {
        return mark_name;
    }

    public void setMark_name(String mark_name) {
        this.mark_name = mark_name;
    }

    public String getMark_num() {
        return mark_num;
    }

    public void setMark_num(String mark_num) {
        this.mark_num = mark_num;
    }

    public String getMark_lib_id() {
        return mark_lib_id;
    }

    public void setMark_lib_id(String mark_lib_id) {
        this.mark_lib_id = mark_lib_id;
    }

    public int getMark_rank_num() {
        return mark_rank_num;
    }

    public void setMark_rank_num(int mark_rank_num) {
        this.mark_rank_num = mark_rank_num;
    }

    public boolean isEdit_selected() {
        return edit_selected;
    }

    public void setEdit_selected(boolean edit_selected) {
        this.edit_selected = edit_selected;
    }
}

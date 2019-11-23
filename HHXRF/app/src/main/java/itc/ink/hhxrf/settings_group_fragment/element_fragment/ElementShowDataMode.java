package itc.ink.hhxrf.settings_group_fragment.element_fragment;

public class ElementShowDataMode {
    private int element_id;
    private String element_name;
    private String element_ordinal;
    private int element_rank_num;
    public boolean isEditSelected=false;

    public ElementShowDataMode() {
    }

    public ElementShowDataMode(int element_id, String element_name, String element_ordinal, int element_rank_num) {
        this.element_id = element_id;
        this.element_name = element_name;
        this.element_ordinal = element_ordinal;
        this.element_rank_num = element_rank_num;
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

    public String getElement_ordinal() {
        return element_ordinal;
    }

    public void setElement_mark(String element_ordinal) {
        this.element_ordinal = element_ordinal;
    }

    public int getElement_rank_num() {
        return element_rank_num;
    }

    public void setElement_rank_num(int element_rank_num) {
        this.element_rank_num = element_rank_num;
    }
}

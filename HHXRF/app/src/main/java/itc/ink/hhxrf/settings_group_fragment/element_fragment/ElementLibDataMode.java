package itc.ink.hhxrf.settings_group_fragment.element_fragment;

public class ElementLibDataMode {
    private int element_id;
    private String element_name;
    private String element_ordinal;
    private boolean isTagItem=false;
    public boolean isEditSelected=false;

    public ElementLibDataMode() {
    }

    public ElementLibDataMode(int element_id, String element_name, String element_ordinal, boolean isTagItem) {
        this.element_id = element_id;
        this.element_name = element_name;
        this.element_ordinal = element_ordinal;
        this.isTagItem = isTagItem;
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

    public void setElement_ordinal(String element_ordinal) {
        this.element_ordinal = element_ordinal;
    }

    public boolean isTagItem() {
        return isTagItem;
    }

    public void setTagItem(boolean tagItem) {
        isTagItem = tagItem;
    }
}

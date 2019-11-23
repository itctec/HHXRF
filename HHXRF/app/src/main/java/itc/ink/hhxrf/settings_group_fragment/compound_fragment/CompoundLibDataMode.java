package itc.ink.hhxrf.settings_group_fragment.compound_fragment;

public class CompoundLibDataMode {
    private int compound_id;
    private String compound_element;
    private String compound_name;
    private boolean isVisibility=false;

    public CompoundLibDataMode() {
    }

    public CompoundLibDataMode(int compound_id, String compound_element, String compound_name, boolean isVisibility) {
        this.compound_id = compound_id;
        this.compound_element = compound_element;
        this.compound_name = compound_name;
        this.isVisibility = isVisibility;
    }

    public int getCompound_id() {
        return compound_id;
    }

    public void setCompound_id(int compound_id) {
        this.compound_id = compound_id;
    }

    public String getCompound_element() {
        return compound_element;
    }

    public void setCompound_element(String compound_element) {
        this.compound_element = compound_element;
    }

    public String getCompound_name() {
        return compound_name;
    }

    public void setCompound_name(String compound_name) {
        this.compound_name = compound_name;
    }

    public boolean isVisibility() {
        return isVisibility;
    }

    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }
}

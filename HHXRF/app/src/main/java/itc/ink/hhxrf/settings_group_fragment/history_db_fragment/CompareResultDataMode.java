package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

public class CompareResultDataMode {
    private String element_name;
    private String sample_one_element_content;
    private String sample_two_element_content;

    public CompareResultDataMode() {
    }

    public CompareResultDataMode(String element_name, String sample_one_element_content, String sample_two_element_content) {
        this.element_name = element_name;
        this.sample_one_element_content = sample_one_element_content;
        this.sample_two_element_content = sample_two_element_content;
    }

    public String getElement_name() {
        return element_name;
    }

    public void setElement_name(String element_name) {
        this.element_name = element_name;
    }

    public String getSample_one_element_content() {
        return sample_one_element_content;
    }

    public void setSample_one_element_content(String sample_one_element_content) {
        this.sample_one_element_content = sample_one_element_content;
    }

    public String getSample_two_element_content() {
        return sample_two_element_content;
    }

    public void setSample_two_element_content(String sample_two_element_content) {
        this.sample_two_element_content = sample_two_element_content;
    }
}

package itc.ink.hhxrf.left_drawer.mode;

import java.util.ArrayList;
import java.util.List;

public class LeftDrawerWrapperDataMode {
    private String main_title="Title";
    private List<LeftDrawerSubDataMode> sub_item_data_array=new ArrayList<>();

    public LeftDrawerWrapperDataMode() {
    }

    public LeftDrawerWrapperDataMode(String main_title, List<LeftDrawerSubDataMode> sub_item_data_array) {
        this.main_title = main_title;
        this.sub_item_data_array = sub_item_data_array;
    }

    @Override
    public String toString() {
        return "LeftDrawerWrapperDataMode{" +
                "main_title='" + main_title + '\'' +
                ", sub_item_data_array=" + sub_item_data_array +
                '}';
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public List<LeftDrawerSubDataMode> getSub_item_data_array() {
        return sub_item_data_array;
    }

    public void setSub_item_data_array(List<LeftDrawerSubDataMode> sub_item_data_array) {
        this.sub_item_data_array = sub_item_data_array;
    }
}

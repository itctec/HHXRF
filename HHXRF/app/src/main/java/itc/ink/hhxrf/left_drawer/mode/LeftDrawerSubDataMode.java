package itc.ink.hhxrf.left_drawer.mode;

public class LeftDrawerSubDataMode {
    private int item_id;
    private String sub_title;
    private int icon_resource_sel_id;
    private int icon_resource_unsel_id;
    private int rank_num;

    public LeftDrawerSubDataMode() {
    }

    public LeftDrawerSubDataMode(int item_id, String sub_title, int icon_resource_sel_id, int icon_resource_unsel_id, int rank_num) {
        this.item_id = item_id;
        this.sub_title = sub_title;
        this.icon_resource_sel_id = icon_resource_sel_id;
        this.icon_resource_unsel_id = icon_resource_unsel_id;
        this.rank_num = rank_num;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public int getIcon_resource_sel_id() {
        return icon_resource_sel_id;
    }

    public void setIcon_resource_sel_id(int icon_resource_sel_id) {
        this.icon_resource_sel_id = icon_resource_sel_id;
    }

    public int getIcon_resource_unsel_id() {
        return icon_resource_unsel_id;
    }

    public void setIcon_resource_unsel_id(int icon_resource_unsel_id) {
        this.icon_resource_unsel_id = icon_resource_unsel_id;
    }

    public int getRank_num() {
        return rank_num;
    }

    public void setRank_num(int rank_num) {
        this.rank_num = rank_num;
    }
}

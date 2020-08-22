package itc.ink.hhxrf.home_fragment;

import java.util.ArrayList;
import java.util.List;

public class ElementMarkMapMode {
    private String markID="";
    private String markName="";
    public List<ElementMarkMapElementMode> elementList=new ArrayList<ElementMarkMapElementMode>();
    public int suitCount=0;

    public String getMarkID() {
        return markID;
    }

    public void setMarkID(String markID) {
        this.markID = markID;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

}

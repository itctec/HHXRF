package itc.ink.hhxrf.home_fragment;

import android.support.annotation.Nullable;

public class ElementMarkMapElementMode {
    private String element_id="";
    private String element_name="";
    private String element_min_value="";
    private String element_max_value="";
    private String element_tol_value;

    public ElementMarkMapElementMode(String element_id, String element_name, String element_min_value, String element_max_value, String element_tol_value) {
        this.element_id = element_id;
        this.element_name = element_name;
        this.element_min_value = element_min_value;
        this.element_max_value = element_max_value;
        this.element_tol_value = element_tol_value;
    }

    public String getElement_id() {
        return element_id;
    }

    public void setElement_id(String element_id) {
        this.element_id = element_id;
    }

    public String getElement_name() {
        return element_name;
    }

    public void setElement_name(String element_name) {
        this.element_name = element_name;
    }

    public String getElement_min_value() {
        return element_min_value;
    }

    public void setElement_min_value(String element_min_value) {
        this.element_min_value = element_min_value;
    }

    public String getElement_max_value() {
        return element_max_value;
    }

    public void setElement_max_value(String element_max_value) {
        this.element_max_value = element_max_value;
    }

    public String getElement_tol_value() {
        return element_tol_value;
    }

    public void setElement_tol_value(String element_tol_value) {
        this.element_tol_value = element_tol_value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        System.out.println("匹配测试-》");
        if(this==obj){
            System.out.println("匹配测试-》"+true);
            return true;
        }
        if(obj!=null && obj.getClass()==String.class){
            String elementName=(String)obj;
            if(this.getElement_name().equals(elementName)){
                System.out.println("匹配测试-》"+true);
                return true;
            }
        }
        System.out.println("匹配测试-》"+false);
        return false;
    }


}

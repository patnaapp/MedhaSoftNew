package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class CATEGORY implements KvmSerializable {
    public static Class<CATEGORY> USER_CLASS = CATEGORY.class;

    private String CategoryID = "";
    private String CategoryValue = "";
    private String CategoryValueHn = "";

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryValue() {
        return CategoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        CategoryValue = categoryValue;
    }

    public String getCategoryValueHn() {
        return CategoryValueHn;
    }

    public void setCategoryValueHn(String categoryValueHn) {
        CategoryValueHn = categoryValueHn;
    }
}

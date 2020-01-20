package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class BENSECTION implements KvmSerializable {
    public static Class<BENSECTION> USER_CLASS = BENSECTION.class;

    private String SectionID = "";
    private String SectionValue = "";
    private String SectionValueHn = "";

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

    public String getSectionID() {
        return SectionID;
    }

    public void setSectionID(String sectionID) {
        SectionID = sectionID;
    }

    public String getSectionValue() {
        return SectionValue;
    }

    public void setSectionValue(String sectionValue) {
        SectionValue = sectionValue;
    }

    public String getSectionValueHn() {
        return SectionValueHn;
    }

    public void setSectionValueHn(String sectionValueHn) {
        SectionValueHn = sectionValueHn;
    }
}

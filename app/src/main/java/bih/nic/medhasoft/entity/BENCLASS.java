package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class BENCLASS implements KvmSerializable {
    public static Class<BENCLASS> USER_CLASS = BENCLASS.class;

    private String ClassID = "";
    private String ClassValue = "";
    private String ClassValueHn = "";

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

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getClassValue() {
        return ClassValue;
    }

    public void setClassValue(String classValue) {
        ClassValue = classValue;
    }

    public String getClassValueHn() {
        return ClassValueHn;
    }

    public void setClassValueHn(String classValueHn) {
        ClassValueHn = classValueHn;
    }
}

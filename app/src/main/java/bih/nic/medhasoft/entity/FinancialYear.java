package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class FinancialYear implements KvmSerializable {
    public static Class<FinancialYear> USER_CLASS = FinancialYear.class;

    private String FyearID = "";
    private String FyearValue = "";
    private String FyearValueHn = "";

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

    public String getFyearID() {
        return FyearID;
    }

    public void setFyearID(String fyearID) {
        FyearID = fyearID;
    }

    public String getFyearValue() {
        return FyearValue;
    }

    public void setFyearValue(String fyearValue) {
        FyearValue = fyearValue;
    }

    public String getFyearValueHn() {
        return FyearValueHn;
    }

    public void setFyearValueHn(String fyearValueHn) {
        FyearValueHn = fyearValueHn;
    }
}

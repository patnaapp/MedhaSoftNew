package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class ACHOLDERTYPE implements KvmSerializable {
    public static Class<ACHOLDERTYPE> USER_CLASS = ACHOLDERTYPE.class;

    private String ACHolderID = "";
    private String ACHolderValue = "";
    private String ACHolderValueHn = "";

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

    public String getACHolderID() {
        return ACHolderID;
    }

    public void setACHolderID(String ACHolderID) {
        this.ACHolderID = ACHolderID;
    }

    public String getACHolderValue() {
        return ACHolderValue;
    }

    public void setACHolderValue(String ACHolderValue) {
        this.ACHolderValue = ACHolderValue;
    }

    public String getACHolderValueHn() {
        return ACHolderValueHn;
    }

    public void setACHolderValueHn(String ACHolderValueHn) {
        this.ACHolderValueHn = ACHolderValueHn;
    }
}

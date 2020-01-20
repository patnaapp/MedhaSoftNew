package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class BENSTATUS implements KvmSerializable {
    public static Class<BENSTATUS> USER_CLASS = BENSTATUS.class;

    private String BenStatusID = "";
    private String BenStatusValue = "";
    private String BenStatusValueHn = "";

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

    public String getBenStatusID() {
        return BenStatusID;
    }

    public void setBenStatusID(String benStatusID) {
        BenStatusID = benStatusID;
    }

    public String getBenStatusValue() {
        return BenStatusValue;
    }

    public void setBenStatusValue(String benStatusValue) {
        BenStatusValue = benStatusValue;
    }

    public String getBenStatusValueHn() {
        return BenStatusValueHn;
    }

    public void setBenStatusValueHn(String benStatusValueHn) {
        BenStatusValueHn = benStatusValueHn;
    }
}

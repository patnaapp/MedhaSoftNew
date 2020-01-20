package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class CheckUnCheck implements KvmSerializable {
    public static Class<CheckUnCheck> USER_CLASS = CheckUnCheck.class;

    private String BenID = "";
    private String IsChecked = "";
    public CheckUnCheck() {

    }
    public CheckUnCheck(SoapObject obj) {

    }
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

    public String getBenID() {
        return BenID;
    }

    public void setBenID(String benID) {
        BenID = benID;
    }

    public String getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(String isChecked) {
        IsChecked = isChecked;
    }
}

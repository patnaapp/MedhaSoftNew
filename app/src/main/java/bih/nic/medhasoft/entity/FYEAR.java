package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class FYEAR implements KvmSerializable {
    public static Class<FYEAR> USER_CLASS = FYEAR.class;

    private String FYearID = "";
    private String FYearValue = "";
    private String Status = "";
    private String FDate = "";
    public FYEAR() {

    }
    public FYEAR(SoapObject obj) {

        this.FYearID = obj.getProperty("FYearID").toString();
        this.FYearValue = obj.getProperty("FinYear").toString();
        this.Status = obj.getProperty("Status").toString();
       // this.FDate = obj.getProperty("FDate").toString();
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

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getFYearID() {
        return FYearID;
    }

    public void setFYearID(String FYearID) {
        this.FYearID = FYearID;
    }

    public String getFYearValue() {
        return FYearValue;
    }

    public void setFYearValue(String FYearValue) {
        this.FYearValue = FYearValue;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
}

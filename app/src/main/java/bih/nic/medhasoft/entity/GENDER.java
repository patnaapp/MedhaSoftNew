package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class GENDER implements KvmSerializable {
    public static Class<GENDER> Remarks_CLASS = GENDER.class;

    String GenderID="";
    String GenderName="";
    String GenderNameHn="";


    // Empty constructor
    public GENDER() {
    }

    // constructor
//    public GENDER(SoapObject obj) {
//
//        this.GenderID = obj.getProperty("Class_Id").toString();
//        this.GenderName = obj.getProperty("Class_Name").toString();
//    }
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

    public String getGenderID() {
        return GenderID;
    }

    public void setGenderID(String genderID) {
        GenderID = genderID;
    }

    public String getGenderName() {
        return GenderName;
    }

    public void setGenderName(String genderName) {
        GenderName = genderName;
    }

    public String getGenderNameHn() {
        return GenderNameHn;
    }

    public void setGenderNameHn(String genderNameHn) {
        GenderNameHn = genderNameHn;
    }
}

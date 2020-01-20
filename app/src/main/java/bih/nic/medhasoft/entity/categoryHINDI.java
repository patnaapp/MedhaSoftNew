package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class categoryHINDI implements KvmSerializable {
    public static Class<categoryHINDI> Remarks_CLASS = categoryHINDI.class;

    String CategoryID="";
    String CategoryName="";
    String CategoryNameHn="";


    // Empty constructor
    public categoryHINDI() {
    }

    // constructor
    public categoryHINDI(SoapObject obj) {


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

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryNameHn() {
        return CategoryNameHn;
    }

    public void setCategoryNameHn(String categoryNameHn) {
        CategoryNameHn = categoryNameHn;
    }
}

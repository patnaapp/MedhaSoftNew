package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class CLASSLIST implements KvmSerializable {
    public static Class<CLASSLIST> Remarks_CLASS = CLASSLIST.class;

    String ClassID="";
    String ClassName="";
    String ClassNamehn="";


    // Empty constructor
    public CLASSLIST() {
    }

    // constructor
    public CLASSLIST(SoapObject obj) {

        this.ClassID = obj.getProperty("_ClassType_Id").toString();
        this.ClassName = obj.getProperty("_ClassType_Name").toString();
        this.ClassNamehn = obj.getProperty("_ClassType_NameHn").toString();
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

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassNamehn() {
        return ClassNamehn;
    }

    public void setClassNamehn(String classNamehn) {
        ClassNamehn = classNamehn;
    }
}

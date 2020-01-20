package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class MARKATTPERRESULT implements KvmSerializable {
    public static Class<MARKATTPERRESULT> Remarks_CLASS = MARKATTPERRESULT.class;

    //Status=1; RowsEffected=1; Message=Ok;
    String Status="";
    String RowsEffected="";
    String Message="";


    // Empty constructor
    public MARKATTPERRESULT() {
    }

    // constructor
    public MARKATTPERRESULT(SoapObject obj) {

        this.Status = obj.getProperty("Status").toString();
        this.RowsEffected = obj.getProperty("RowsEffected").toString();
        this.Message = obj.getProperty("Message").toString();
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRowsEffected() {
        return RowsEffected;
    }

    public void setRowsEffected(String rowsEffected) {
        RowsEffected = rowsEffected;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}

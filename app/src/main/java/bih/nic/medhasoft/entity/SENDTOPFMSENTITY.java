package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class SENDTOPFMSENTITY implements KvmSerializable {
    public static Class<SENDTOPFMSENTITY> Pfms_CLASS = SENDTOPFMSENTITY.class;

    private String SendToPfms_ID = "";
    private String SendToPfms_Status = "";


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

    public String getSendToPfms_ID() {
        return SendToPfms_ID;
    }

    public void setSendToPfms_ID(String sendToPfms_ID) {
        SendToPfms_ID = sendToPfms_ID;
    }

    public String getSendToPfms_Status() {
        return SendToPfms_Status;
    }

    public void setSendToPfms_Status(String sendToPfms_Status) {
        SendToPfms_Status = sendToPfms_Status;
    }
}

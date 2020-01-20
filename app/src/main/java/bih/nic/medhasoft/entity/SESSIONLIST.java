package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class SESSIONLIST implements KvmSerializable {
    public static Class<SESSIONLIST> Remarks_CLASS = SESSIONLIST.class;
   
    String SessionID="";
    String SessionName="";
    String SessionNamehn="";


    // Empty constructor
    public SESSIONLIST() {
    }

    // constructor
    public SESSIONLIST(SoapObject obj) {

        this.SessionID = obj.getProperty("_Section_id").toString();
        this.SessionName = obj.getProperty("_Section_Name").toString();
        this.SessionNamehn = obj.getProperty("_Section_NameHn").toString();
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

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getSessionName() {
        return SessionName;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public String getSessionNamehn() {
        return SessionNamehn;
    }

    public void setSessionNamehn(String sessionNamehn) {
        SessionNamehn = sessionNamehn;
    }
}

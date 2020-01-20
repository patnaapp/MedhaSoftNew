package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Findpin implements KvmSerializable {
    public static Class<Findpin> USER_CLASS = Findpin.class;

    private String et_mobile = "";
    private String pinotp = "";

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

    public String getEt_mobile() {
        return et_mobile;
    }

    public void setEt_mobile(String et_mobile) {
        this.et_mobile = et_mobile;
    }

    public String getPinotp() {
        return pinotp;
    }

    public void setPinotp(String pinotp) {
        this.pinotp = pinotp;
    }
}

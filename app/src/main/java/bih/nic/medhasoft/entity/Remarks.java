package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Remarks implements KvmSerializable {
    public static Class<Remarks> Remarks_CLASS = Remarks.class;
    String RemarksCode,RemarkName;
    String pin="";
    String mobile="";
    String messageid="";
    String title="";
    String acno="";
    String pcno="";
    String pdfurl="";


    // Empty constructor
    public Remarks() {
    }

    // constructor
    public Remarks(SoapObject obj) {

        this.pin = obj.getProperty("pin").toString();
        this.pin = obj.getProperty("mobile").toString();
        this.mobile = obj.getProperty("mobile").toString();
        this.messageid = obj.getProperty("mobile").toString();
        this.title = obj.getProperty("mobile").toString();
        this.acno = obj.getProperty("mobile").toString();
        this.pcno = obj.getProperty("mobile").toString();
        this.pdfurl = obj.getProperty("mobile").toString();

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

    public String getRemarksCode() {
        return RemarksCode;
    }

    public void setRemarksCode(String remarksCode) {
        RemarksCode = remarksCode;
    }

    public String getRemarkName() {
        return RemarkName;
    }

    public void setRemarkName(String remarkName) {
        RemarkName = remarkName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcno() {
        return acno;
    }

    public void setAcno(String acno) {
        this.acno = acno;
    }

    public String getPcno() {
        return pcno;
    }

    public void setPcno(String pcno) {
        this.pcno = pcno;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}

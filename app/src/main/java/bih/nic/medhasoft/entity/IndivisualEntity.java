package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class IndivisualEntity implements Serializable {
    public static Class<IndivisualEntity> SyncExstngSpCpData_CLASS = IndivisualEntity.class;




    //
    private String description="";
    private String pin="";
    private String messageid="";
    private String pdfurl="";
    private String title="";
    private String acno="";
    private String pcno="";
    private String id="";
    private String mobile="";




    public IndivisualEntity() {
    }

    public IndivisualEntity(SoapObject obj) {
        this.description = obj.getProperty("State_Code").toString();


    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

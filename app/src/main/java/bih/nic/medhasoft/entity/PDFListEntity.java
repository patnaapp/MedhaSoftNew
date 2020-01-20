package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class PDFListEntity implements Serializable {
    public static Class<PDFListEntity> SyncExstngSpCpData_CLASS = PDFListEntity.class;


    //
    private String mobile="";
    private String ID="";
    private String pin="";
    private String messageid="";
    private String pdfurl="";
    private String title="";
    private String acno="";
    private String pcno="";

    private String filetype="";
    private String pcname="";
    private String acname="";
    private String msgdate="";



    public PDFListEntity() {
    }

    public PDFListEntity(SoapObject obj) {


    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getPcname() {
        return pcname;
    }

    public void setPcname(String pcname) {
        this.pcname = pcname;
    }

    public String getAcname() {
        return acname;
    }

    public void setAcname(String acname) {
        this.acname = acname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(String msgdate) {
        this.msgdate = msgdate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}

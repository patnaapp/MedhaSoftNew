package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class UserDetails1 implements KvmSerializable {
    public static Class<UserDetails1> USER_CLASS = UserDetails1.class;

    private String AC_NO = "";
    private String AC_NAME = "";
    private String MesgId = "";
    private String MesgDate = "";
    private String MesgDesc = "";
    private String Type = "";
    private String PIN_NO= "";
    private String PDF_LINK = "";
    private String UploadBy = "";
    private String DistCode = "";
    private String DistName = "";
    private String PC_NO = "";
    private String PC_NAME_EN = "";




    public UserDetails1() {
    }

    // constructor
    public UserDetails1(SoapObject obj) {
        /*
         <PIN_NO>string</PIN_NO>
        <OFF_CODE>string</OFF_CODE>
        <NODALOFFICERNAME>string</NODALOFFICERNAME>
        <NODALOFFICERDESIGNATION>string</NODALOFFICERDESIGNATION>
        <NODALOFFICERMOBILENO>string</NODALOFFICERMOBILENO>
        <Type>string</Type>
        <Registered>string</Registered>
        <isAuthenticated>boolean</isAuthenticated>
         */

        this.AC_NO = obj.getProperty("AC_NO").toString();
        this.AC_NAME = obj.getProperty("AC_NAME").toString();
        this.MesgId = obj.getProperty("MesgId").toString();
        this.MesgDate = obj.getProperty("MesgDate").toString();
        this.MesgDesc = obj.getProperty("MesgDesc").toString();
        this.Type = obj.getProperty("Type").toString();
        this.PIN_NO = obj.getProperty("PIN_NO").toString();
        this.PDF_LINK = obj.getProperty("PDF_LINK").toString();
        this.PC_NO = obj.getProperty("PC_NO").toString();

        this.DistCode = obj.getProperty("DistCode").toString();

        this.DistName = obj.getProperty("DistName").toString();

        this.PC_NAME_EN = obj.getProperty("PC_NAME_EN").toString();


    }

    public String getAC_NO() {
        return AC_NO;
    }

    public void setAC_NO(String AC_NO) {
        this.AC_NO = AC_NO;
    }

    public String getAC_NAME() {
        return AC_NAME;
    }

    public void setAC_NAME(String AC_NAME) {
        this.AC_NAME = AC_NAME;
    }

    public String getMesgId() {
        return MesgId;
    }

    public void setMesgId(String mesgId) {
        MesgId = mesgId;
    }

    public String getMesgDate() {
        return MesgDate;
    }

    public void setMesgDate(String mesgDate) {
        MesgDate = mesgDate;
    }

    public String getMesgDesc() {
        return MesgDesc;
    }

    public void setMesgDesc(String mesgDesc) {
        MesgDesc = mesgDesc;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPIN_NO() {
        return PIN_NO;
    }

    public void setPIN_NO(String PIN_NO) {
        this.PIN_NO = PIN_NO;
    }

    public String getPDF_LINK() {
        return PDF_LINK;
    }

    public void setPDF_LINK(String PDF_LINK) {
        this.PDF_LINK = PDF_LINK;
    }

    public String getUploadBy() {
        return UploadBy;
    }

    public void setUploadBy(String uploadBy) {
        UploadBy = uploadBy;
    }

    public String getDistCode() {
        return DistCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
    }

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getPC_NO() {
        return PC_NO;
    }

    public void setPC_NO(String PC_NO) {
        this.PC_NO = PC_NO;
    }

    public String getPC_NAME_EN() {
        return PC_NAME_EN;
    }

    public void setPC_NAME_EN(String PC_NAME_EN) {
        this.PC_NAME_EN = PC_NAME_EN;
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
}

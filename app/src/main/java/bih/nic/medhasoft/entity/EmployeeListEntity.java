package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class EmployeeListEntity implements Serializable {
    public static Class<EmployeeListEntity> SyncExstngSpCpData_CLASS = EmployeeListEntity.class;


/*
<OFF_CODE>100001</OFF_CODE>
<NAME>SADHNA KUMARI</NAME>
<DESIG>CLERK</DESIG>
<DATE_RETIRE>10/31/2048 12:00:00 AM</DATE_RETIRE>
<BLOODGROUP>B+</BLOODGROUP>
<CONTACTNO>9631664068</CONTACTNO>
<PIN_NO>1000001</PIN_NO>
 */

    //
    private String name="";
    private String designa="";
    private String bloodgroup="";
    private String retdate="";
    private String pinnum="";
    private String contnum="";
    private String officode="";



    public EmployeeListEntity() {
    }

    public EmployeeListEntity(SoapObject final_object) {
        this.officode=final_object.getProperty("OFF_CODE").toString();
        this.name=final_object.getProperty("NAME").toString();
        this.designa=final_object.getProperty("DESIG").toString();
        this.retdate=final_object.getProperty("DATE_RETIRE").toString();
        this.bloodgroup=final_object.getProperty("BLOODGROUP").toString();
        this.contnum=final_object.getProperty("CONTACTNO").toString();
        this.pinnum=final_object.getProperty("PIN_NO").toString();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesigna() {
        return designa;
    }

    public void setDesigna(String designa) {
        this.designa = designa;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getRetdate() {
        return retdate;
    }

    public void setRetdate(String retdate) {
        this.retdate = retdate;
    }

    public String getPinnum() {
        return pinnum;
    }

    public void setPinnum(String pinnum) {
        this.pinnum = pinnum;
    }

    public String getContnum() {
        return contnum;
    }

    public void setContnum(String contnum) {
        this.contnum = contnum;
    }

    public String getOfficode() {
        return officode;
    }

    public void setOfficode(String officode) {
        this.officode = officode;
    }
}

package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;


public class Get_usersalldetails implements KvmSerializable {
    public static Class<Get_usersalldetails> USER_CLASS = Get_usersalldetails.class;
    private boolean isAuthenticated = false;

    private String _District_Code = "";
    private String _District_Name = "";
    private String _Block_Code = "";
    private String _Block_Name = "";
    private String _DISE_CODE= "";
    private String _School_Name = "";
    private String _School_NameHn = "";
    private String _otpentereddate = "";
    private String _otpentered = "";
    private String _otpgenerateddate = "";
    private String _otpgenerated = "";
    private String _otp = "";
    private String _mobileno = "";
    private String _RoleDesc = "";
    private String _UserRole = "";
    private String _IsActive = "";
    private String _UserID = "";
    private String _UserName = "";
    private String _UserPass = "";



/*
<_DISE_CODE>string</_DISE_CODE>
        <_School_Name>string</_School_Name>
        <_OTPEntered>string</_OTPEntered>
        <_otpentereddate>string</_otpentereddate>
        <_otpgenerated>string</_otpgenerated>
        <_otpgenerateddate>string</_otpgenerateddate>
        <_DistrictName>string</_DistrictName>
        <_BlockName>string</_BlockName>
        <_RoleDesc>string</_RoleDesc>
        <_District_Code>string</_District_Code>
        <_Block_Code>string</_Block_Code>
        <_UserRole>string</_UserRole>
        <_Mobile_No>string</_Mobile_No>
        <_OTP>string</_OTP>
        <_IsActive>string</_IsActive>
        <_IsAuthenticated>boolean</_IsAuthenticated>

 <_eupi_CpsmsId>string</_eupi_CpsmsId>
          <_eupi_BenNameasPerBank>string</_eupi_BenNameasPerBank>
          <_eupi_IFSC>string</_eupi_IFSC>
          <_eupi_AccountNo>string</_eupi_AccountNo>
          <_eupi_BankName>string</_eupi_BankName>
          <_eupi_Address1>string</_eupi_Address1>
          <_eupi_Address2>string</_eupi_Address2>
          <_eupi_Address3>string</_eupi_Address3>
          <_eupi_Mobile>string</_eupi_Mobile>
          <_eupi_ResponseFileName>string</_eupi_ResponseFileName>
          <_eupi_ResponseReadDate>string</_eupi_ResponseReadDate>
          <_maxscore>string</_maxscore>
          <_Isattendance75>string</_Isattendance75>
          <_markedDate>string</_markedDate>
 */

    public Get_usersalldetails() {
    }

    @SuppressWarnings("deprecation")
    public Get_usersalldetails(SoapObject obj) {

        this.setAuthenticated(Boolean.parseBoolean(obj.getProperty("_IsAuthenticated").toString()));

        this.set_District_Code(obj.getProperty("_District_Code").toString());
        this.set_District_Name(obj.getProperty("_DistrictName").toString());

        this.set_Block_Code(obj.getProperty("_Block_Code").toString());
        this.set_Block_Name(obj.getProperty("_BlockName").toString());

        this.set_DISE_CODE(obj.getProperty("_DISE_CODE").toString());

        this.set_School_NameHn(obj.getProperty("_School_Name").toString());

        this.set_School_Name(obj.getProperty("_School_Name").toString());
        // this.set_School_NameHn(obj.getProperty("School_NameHn").toString());

        this.set_otpentereddate(obj.getProperty("_otpentereddate").toString());
        this.set_otpentered(obj.getProperty("_OTPEntered").toString());

        this.set_otpgenerateddate(obj.getProperty("_otpgenerateddate").toString());
        this.set_otpgenerated(obj.getProperty("_otpgenerated").toString());

        this.set_otp(obj.getProperty("_OTP").toString());
        this.set_mobileno(obj.getProperty("_Mobile_No").toString());
        this.set_RoleDesc(obj.getProperty("_RoleDesc").toString());
        this.set_UserRole(obj.getProperty("_UserRole").toString());
        this.set_IsActive(obj.getProperty("_IsActive").toString());




    }


    public static Class<Get_usersalldetails> getUserClass() {
        return USER_CLASS;
    }

    public static void setUserClass(Class<Get_usersalldetails> userClass) {
        USER_CLASS = userClass;
    }


    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        // TODO Auto-generated method stub
        return 13;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }

    public String get_IsActive() {
        return _IsActive;
    }

    public void set_IsActive(String _IsActive) {
        this._IsActive = _IsActive;
    }

    public String get_UserRole() {
        return _UserRole;
    }

    public void set_UserRole(String _UserRole) {
        this._UserRole = _UserRole;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String get_RoleDesc() {
        return _RoleDesc;
    }

    public void set_RoleDesc(String _RoleDesc) {
        this._RoleDesc = _RoleDesc;
    }

    public String get_District_Code() {
        return _District_Code;
    }

    public void set_District_Code(String _District_Code) {
        this._District_Code = _District_Code;
    }

    public String get_District_Name() {
        return _District_Name;
    }

    public void set_District_Name(String _District_Name) {
        this._District_Name = _District_Name;
    }



    public String get_Block_Code() {
        return _Block_Code;
    }

    public void set_Block_Code(String _Block_Code) {
        this._Block_Code = _Block_Code;
    }

    public String get_Block_Name() {
        return _Block_Name;
    }

    public void set_Block_Name(String _Block_Name) {
        this._Block_Name = _Block_Name;
    }


    public String get_DISE_CODE() {
        return _DISE_CODE;
    }

    public void set_DISE_CODE(String _DISE_CODE) {
        this._DISE_CODE = _DISE_CODE;
    }





    public String get_School_Name() {
        return _School_Name;
    }

    public void set_School_Name(String _School_Name) {
        this._School_Name = _School_Name;
    }

    public String get_School_NameHn() {
        return _School_NameHn;
    }

    public void set_School_NameHn(String _School_NameHn) {
        this._School_NameHn = _School_NameHn;
    }

    public String get_otpentereddate() {
        return _otpentereddate;
    }

    public void set_otpentereddate(String _otpentereddate) {
        this._otpentereddate = _otpentereddate;
    }

    public String get_otpentered() {
        return _otpentered;
    }

    public void set_otpentered(String _otpentered) {
        this._otpentered = _otpentered;
    }

    public String get_otpgenerateddate() {
        return _otpgenerateddate;
    }

    public void set_otpgenerateddate(String _otpgenerateddate) {
        this._otpgenerateddate = _otpgenerateddate;
    }

    public String get_otpgenerated() {
        return _otpgenerated;
    }

    public void set_otpgenerated(String _otpgenerated) {
        this._otpgenerated = _otpgenerated;
    }

    public String get_otp() {
        return _otp;
    }

    public void set_otp(String _otp) {
        this._otp = _otp;
    }

    public String get_mobileno() {
        return _mobileno;
    }

    public void set_mobileno(String _mobileno) {
        this._mobileno = _mobileno;
    }


    public String get_UserID() {
        return _UserID;
    }

    public void set_UserID(String _UserID) {
        this._UserID = _UserID;
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_UserPass() {
        return _UserPass;
    }

    public void set_UserPass(String _UserPass) {
        this._UserPass = _UserPass;
    }



}

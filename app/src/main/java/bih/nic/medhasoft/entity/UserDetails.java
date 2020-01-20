package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;


public class UserDetails implements KvmSerializable {
    public static Class<UserDetails> USER_CLASS = UserDetails.class;
    private boolean isAuthenticated = false;
    private String Password = "";
    private String DistrictCode = "";
    private String DistrictName = "";
    private String BlockCode = "";
    private String BlockName = "";
    private String FatherName = "";
    private String DOB = "";
    private String UserID = "";
    private String Role = "";
    private String UserName = "";
    private String Qualification = "";

    private String Address = "";
    private String Email = "";
    private String LastVisitedOn = "";
    private String register_otp = "";
    private String entry_date = "";
    private String pin = "";
    private String mobile = "";
    private String Status="";
    private String UserType="";
    private String DiseCode="";



    public UserDetails() {
    }

    @SuppressWarnings("deprecation")
    public UserDetails(SoapObject obj) {
        this.setAuthenticated(Boolean.parseBoolean(obj.getProperty("isAuthenticated").toString()));
        this.setPin(obj.getProperty("_PIN_NO").toString());
        this.setMobile(obj.getProperty("_MobileNo").toString());
        this.setPassword(obj.getProperty("Password").toString());
        this.setAddress(obj.getProperty("Address").toString());
        this.setEmail(obj.getProperty("Email").toString());
        this.setLastVisitedOn(obj.getProperty("LastVisitedOn").toString());

    }


    public static Class<UserDetails> getUserClass() {
        return USER_CLASS;
    }

    public static void setUserClass(Class<UserDetails> userClass) {
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

    public String getDiseCode() {
        return DiseCode;
    }

    public void setDiseCode(String diseCode) {
        DiseCode = diseCode;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }



    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLastVisitedOn() {
        return LastVisitedOn;
    }

    public void setLastVisitedOn(String lastVisitedOn) {
        LastVisitedOn = lastVisitedOn;
    }

    public String getRegister_otp() {
        return register_otp;
    }

    public void setRegister_otp(String register_otp) {
        this.register_otp = register_otp;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}

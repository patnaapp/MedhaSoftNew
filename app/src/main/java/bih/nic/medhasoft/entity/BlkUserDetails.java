package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class BlkUserDetails implements KvmSerializable {
    public static Class<BlkUserDetails> BlkUSER_CLASS = BlkUserDetails.class;

    private boolean isAuthenticated = false;
    private String _BlockCode = "";
    private String _Password = "";
    private String _UserID = "";
    private String _UserName = "";
    private String _DistrictCode = "";
    private String _Block_Code = "";
    private String _UserRole = "";
    private String _DistrictName = "";
    private String _BlockName = "";





    public BlkUserDetails() {
    }

    @SuppressWarnings("deprecation")
    public BlkUserDetails(SoapObject obj) {
        this.setAuthenticated(Boolean.parseBoolean(obj.getProperty("_IsAuthenticated").toString()));
        this.set_BlockCode(obj.getProperty("_Block_Code").toString());
        this.set_BlockName(obj.getProperty("_BlockName").toString());
        this.set_Password(obj.getProperty("_Password").toString());
        this.set_UserID(obj.getProperty("_UserID").toString());
        this.set_UserName(obj.getProperty("_UserName").toString());
        this.set_DistrictCode(obj.getProperty("_DistrictCode").toString());
        this.set_DistrictName(obj.getProperty("_DistrictName").toString());
        this.set_UserRole(obj.getProperty("_UserRole").toString());


    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String get_BlockCode() {
        return _BlockCode;
    }

    public void set_BlockCode(String _BlockCode) {
        this._BlockCode = _BlockCode;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String _Password) {
        this._Password = _Password;
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

    public String get_DistrictCode() {
        return _DistrictCode;
    }

    public void set_DistrictCode(String _DistrictCode) {
        this._DistrictCode = _DistrictCode;
    }

    public String get_Block_Code() {
        return _Block_Code;
    }

    public void set_Block_Code(String _Block_Code) {
        this._Block_Code = _Block_Code;
    }

    public String get_UserRole() {
        return _UserRole;
    }

    public void set_UserRole(String _UserRole) {
        this._UserRole = _UserRole;
    }

    public String get_DistrictName() {
        return _DistrictName;
    }

    public void set_DistrictName(String _DistrictName) {
        this._DistrictName = _DistrictName;
    }

    public String get_BlockName() {
        return _BlockName;
    }

    public void set_BlockName(String _BlockName) {
        this._BlockName = _BlockName;
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

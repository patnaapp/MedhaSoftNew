package bih.nic.medhasoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class AccountHolderType implements KvmSerializable {
    public static Class<AccountHolderType> USER_CLASS = AccountHolderType.class;

    private String AccountHolderTypeID = "";
    private String AccountHolderTypeName = "";
    private String AccountHolderTypeNameHn = "";

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

    public String getAccountHolderTypeID() {
        return AccountHolderTypeID;
    }

    public void setAccountHolderTypeID(String accountHolderTypeID) {
        AccountHolderTypeID = accountHolderTypeID;
    }

    public String getAccountHolderTypeName() {
        return AccountHolderTypeName;
    }

    public void setAccountHolderTypeName(String accountHolderTypeName) {
        AccountHolderTypeName = accountHolderTypeName;
    }

    public String getAccountHolderTypeNameHn() {
        return AccountHolderTypeNameHn;
    }

    public void setAccountHolderTypeNameHn(String accountHolderTypeNameHn) {
        AccountHolderTypeNameHn = accountHolderTypeNameHn;
    }
}

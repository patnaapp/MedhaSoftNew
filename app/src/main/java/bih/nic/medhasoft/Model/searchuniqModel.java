package bih.nic.medhasoft.Model;

import org.ksoap2.serialization.SoapObject;

public class searchuniqModel {

    private String _uniqNo;

    public searchuniqModel(SoapObject res1) {
    }

    public String get_uniqNo() {
        return _uniqNo;
    }

    public void set_uniqNo(String _uniqNo) {
        this._uniqNo = _uniqNo;
    }
}

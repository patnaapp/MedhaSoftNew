package bih.nic.medhasoft.entity;

import android.util.Log;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class BarcodeEntity implements KvmSerializable {
    public static Class<BarcodeEntity> Barcode_CLASS = BarcodeEntity.class;

    private String BlockCode = "";
    private String ORcode = "";
    private String Status = "";
    private String Message = "";
    private String UniqueNo = "";
    private String serialStatus="";
    private String serialmessage="";
    private String serialNo="";
    private String BenName="";
    private String AccountNo="";
    private String IFSC="";
    private String totalData="";
    private String totalDataReprt="";
    private String TotalDataEarlier="";
    private String TotalDataRemaning="";

    static ArrayList<String> newstring = new ArrayList<>();

    private barlist[] lecture;


    private String _Qr_Response_status = "";
    private String _DistrictCode = "";


    public BarcodeEntity() {
    }

    public void setStudentLecture(barlist[] lecture) {
        this.lecture = lecture;
    }

    public barlist[] getStudentLecture() {
        return lecture;
    }

    @SuppressWarnings("deprecation")
    public BarcodeEntity(SoapObject obj) {

        this.setBlockCode(obj.getProperty("BlockCode").toString());
        this.setStatus(obj.getProperty("Status").toString());
        this.setMessage(obj.getProperty("Message").toString());
        this.setUniqueNo(obj.getProperty("UniqueNo").toString());
        this.setTotalData(obj.getProperty("TotalData").toString());
        this.setTotalDataReprt(obj.getProperty("TotalDataReport").toString());
        this.setTotalDataEarlier(obj.getProperty("TotalDataEarlier").toString());
        this.setTotalDataRemaning(obj.getProperty("TotalDataRemaning").toString());
        if ((obj.getProperty("Status").toString().equalsIgnoreCase("y"))) {
            SoapObject obj1 = (SoapObject) obj.getProperty(8);
            BarcodeEntity barcodeEntity=new BarcodeEntity();
            barlist[] lectures = new barlist[3];
            for (int i = 0; i < obj1.getPropertyCount(); i++)
            {
                SoapObject obj3 = (SoapObject) obj1.getProperty(i);

                lectures[i]=new barlist(obj3.getProperty(0).toString(),obj3.getProperty(1).toString(),obj3.getProperty(2).toString(),obj3.getProperty(3).toString());

            }
            barcodeEntity.setStudentLecture(lectures);
            this.setStudentLecture(lectures);


            barlist[] lectures1 = barcodeEntity.getStudentLecture();
            for (int z = 0; z <lectures1.length; ++z) {
                System.out.println(lectures1[z]);
                Log.d("ggsvxhhhhhhhj","tttttttt"+lectures1[z]);
            }
        }


    }
    public BarcodeEntity(SoapObject obj,String str) {

        this.setSerialStatus(obj.getProperty("Status").toString());
        this.setSerialmessage(obj.getProperty("Message").toString());



    }

    public String get_Qr_Response_status() {
        return _Qr_Response_status;
    }

    public void set_Qr_Response_status(String _Qr_Response_status) {
        this._Qr_Response_status = _Qr_Response_status;
    }

    public String get_DistrictCode() {
        return _DistrictCode;
    }

    public void set_DistrictCode(String _DistrictCode) {
        this._DistrictCode = _DistrictCode;
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

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getORcode() {
        return ORcode;
    }

    public void setORcode(String ORcode) {
        this.ORcode = ORcode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUniqueNo() {
        return UniqueNo;
    }

    public void setUniqueNo(String uniqueNo) {
        UniqueNo = uniqueNo;
    }

    public String getSerialStatus() {
        return serialStatus;
    }

    public void setSerialStatus(String serialStatus) {
        this.serialStatus = serialStatus;
    }

    public String getSerialmessage() {
        return serialmessage;
    }

    public void setSerialmessage(String serialmessage) {
        this.serialmessage = serialmessage;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }


    public class barlist {

        private String name;
        private String status;
        private String BenId;
        private String AcNo;
        private String IFSC;

//        public barlist(String name) {
//            this.name = name;
//        }
        public barlist(String name,String BenID,String ac,String IFSC) {
            this.name = name;
            this.BenId=BenID;
            this.AcNo=ac;
            this.IFSC=IFSC;
        }


        public String getName(){
            return name;
        }

        public String getBenId() {
            return BenId;
        }

        public void setBenId(String benId) {
            BenId = benId;
        }

        public String getAcNo() {
            return AcNo;
        }

        public void setAcNo(String acNo) {
            AcNo = acNo;
        }

        public String getIFSC() {
            return IFSC;
        }

        public void setIFSC(String IFSC) {
            this.IFSC = IFSC;
        }
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }

    public String getTotalDataReprt() {
        return totalDataReprt;
    }

    public void setTotalDataReprt(String totalDataReprt) {
        this.totalDataReprt = totalDataReprt;
    }

    public String getTotalDataEarlier() {
        return TotalDataEarlier;
    }

    public void setTotalDataEarlier(String totalDataEarlier) {
        TotalDataEarlier = totalDataEarlier;
    }

    public String getTotalDataRemaning() {
        return TotalDataRemaning;
    }

    public void setTotalDataRemaning(String totalDataRemaning) {
        TotalDataRemaning = totalDataRemaning;
    }

    public String getBenName() {
        return BenName;
    }

    public void setBenName(String benName) {
        BenName = benName;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }
}


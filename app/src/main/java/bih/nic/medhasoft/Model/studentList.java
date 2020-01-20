package bih.nic.medhasoft.Model;

import android.util.Log;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Hashtable;

import bih.nic.medhasoft.entity.AccountHolderType;
import bih.nic.medhasoft.entity.BENCLASS;
import bih.nic.medhasoft.entity.BENSECTION;
import bih.nic.medhasoft.entity.CATEGORY;
import bih.nic.medhasoft.entity.FinancialYear;
import bih.nic.medhasoft.entity.GENDER;

public class studentList extends ArrayList<studentList> implements KvmSerializable {


    private String id;
    private String officeCode;


    public String stdname;
    public String stdnamehn;
    public String stdclass;
    public String stdclasshn;
    public String stdclassid;
    public String stdsession;
    public String stdsessionhn;
    public String stdsessionid;
    public String stdfname;
    public String stdfnamehn;
    public String stdmname;
    public String stdmnamehn;
    //public String stdgender;
    public String stdateendanceless;
    public String stdbenid;
    public String stdfyearid;
    public String stdfyearval;
    public String stdfyearvalhn;
    public String aID;
    public String stdadmnum;
    public String stdatndless;
    //--------------------------
    public String stdadmdate;
    public String stdgenid;
    public String stdgenname;
    public String stdgennamehn;

    public String stdcatid;
    public String stdcatname;
    public String stdcatnamehn;

    public String stdisminority;
    public String stdishandicaped;
    public String stdaadharcardno;
    public String stdaadharcardname;
    public String stdemailid;
    public String stdmobile;
    public String stdisbpl;
    public String stdifsc;
    public String stdacnum;
    public String stdacholdername;

    public String stdacholdertypeid;
    public String stdacholdertypename;
    public String stdacholdertypenamehn;

    public String stdisaadhaarverified;
    public String stdisforwarded;
    public String stdisverified;

    public String stdstatus;
    public String stdattseventyfiveper;
    public String stdisreverified;
    public String stdpfmsbennameasbank;
    public String stdpfmsstatus;
    public String stdisapprovedandlocked;
    public String stdDOB;
    public String WardVillage;

    //---------------------

    private boolean isSelected;
    private FinancialYear financial_year;
    private BENCLASS ben_class;
    private BENSECTION ben_section;
    private GENDER ben_gender;
    private CATEGORY ben_category;
    private AccountHolderType ben_accholdertype;

    private String Dise_Code;
    //private String IsAadhaarVerified;
    private String AttendancePer;
    private String AttendancePerBy;
    private String AttendancePerDate;
    private String CUBy;
    private String CUDate;
    private String CUIP;
    private String UpdateBy;
    private String UpdateIP;
    private String IsOfflineEntry;
    private String APIKey;
    private String IsDeleted;
    private String DeleteDate;
    private String DeletedBy;
    private String DeletedReason;
    private String DeletedRemark;
    private String IsActive;
    private String ActiveDate;
    private String MAppVersion;
    private String StopBeneficiary;
    private String OptVerify;
    private String StopBenDate;
    private String StopBenBy;
    private String StopBenReason;
    private String UpdateDate;
    private String BankCode;
    private String AccMap;
    //private String BeneficieryStatus;
    private String tempFlag;
    private String BenificiaryStatus1;
    private String AccVerXmlFileName;
    private String eupiStatus;
    private String eupiReasonCode;

    private String DistrictCode;
    private String BlockCode;
    private String AreaType;
    private String WardCode;
    private String Age;


    private String _eupi_CpsmsId = "";
    private String _eupi_BenNameasPerBank = "";
    private String _eupi_IFSC = "";
    private String _eupi_AccountNo = "";
    private String _eupi_BankName = "";
    private String _eupi_Address1 = "";
    private String _eupi_Address2 = "";
    private String _eupi_Address3 = "";

    private String _eupi_Mobile = "";
    private String _eupi_ResponseFileName = "";
    private String _eupi_ResponseReadDate = "";
    private String _maxscore = "";

    private String _markedDate = "";
    private String _ManualMatch = "";
    private String _sendPfms_status = "";
    private String _sendPfms_id = "";
    private String _eupi_Reason = "";

    public static Class<studentList> getdata = studentList.class;
/*
<_FYearId>1920</_FYearId>
<_a_Id>5985193</_a_Id>
<_BeneficieryId>10059851933</_BeneficieryId>
<_DiseCode>10280105518</_DiseCode>

//------------
<_DistrictCode>230</_DistrictCode>
<_BlockCode>1400</_BlockCode>
<_AreaType/>
<_WardCode>URBAN</_WardCode>
//-------------

<_ClassId>10</_ClassId>
<_SectionId>2</_SectionId>
<_AdmNo>16</_AdmNo>
<_AdmDate>20180425</_AdmDate>
<_BeneficieryName>ABHISHEK KUMAR</_BeneficieryName>
<_BeneficieryNameHn>अभिषेक कुमार</_BeneficieryNameHn>
<_FHName>RATAN SAW</_FHName>
<_FHNameHn>रतन साव</_FHNameHn>
<_MName>CHANDRAWATI DEVI</_MName>
<_MNameHn>चंद्रावती देवी</_MNameHn>
<_Gender>1</_Gender>
<_DOB>20040808</_DOB>
<_Age/> //---------------
<_CategaryId>3</_CategaryId>---------------------
<_IsMinority>N</_IsMinority>
<_IsDisabled>N</_IsDisabled>
<_IsIncome>Y</_IsIncome>
<_MobileNo>9386142415</_MobileNo>
<_AadhaarNo>234533043765</_AadhaarNo>
<_AadharName>ABHISHEK KUMAR</_AadharName>
<_IsAadhaarVerified/>
<_Email/>
<_BenAccountNo>35945579129</_BenAccountNo>
<_IFSCCode>SBIN0016613</_IFSCCode>
<_AccountHolderType>1</_AccountHolderType>
<_AccountHolderName/>
<_AttendancePer/>
<_AttendancePerBy/>
<_AttendancePerDate/>
<_CUBy>10280105518</_CUBy>
<_CUDate>20191019</_CUDate>
<_CUIP>10.133.24.7</_CUIP>
<_UpdateBy/>
<_UpdateIP/>
<_IsOfflineEntry/>
<_APIKey/>
<_IsDeleted/>
<_DeleteDate/>
<_DeletedBy/>
<_DeletedReason/>
<_DeletedRemark/>
<_IsActive/>
<_ActiveDate/>
<_M_AppVersion/>
<_StopBeneficiary/>
<_OptVerify>N</_OptVerify>
<_StopBenDate/>
<_StopBenBy/>
<_StopBenReason/>
<_UpdateDate/>
<_BankCode>29</_BankCode>
<_AccMap/>
<_BeneficieryStatus>3</_BeneficieryStatus>
<_tempFlag/>
<_BenificiaryStatus1/>
<_AccVerXmlFileName/>
<_eupi_Status>ACCP</_eupi_Status>
<_eupi_ReasonCode>CPW0004</_eupi_ReasonCode>
 */

    public studentList() {
    }

    public studentList(SoapObject final_object) {
        this.stdfyearid=final_object.getProperty("_FYearId").toString();
        this.aID=final_object.getProperty("_a_Id").toString();

        this.stdbenid=final_object.getProperty("_BeneficieryId").toString();
        this.Dise_Code=final_object.getProperty("_DiseCode").toString();

        this.DistrictCode=final_object.getProperty("_DistrictCode").toString();
        this.BlockCode=final_object.getProperty("_BlockCode").toString();
        this.AreaType=final_object.getProperty("_AreaType").toString();
        //this.WardCode=final_object.getProperty("_WardCode").toString();
        this.WardVillage=final_object.getProperty("_WardCode").toString();


        this.stdclassid=final_object.getProperty("_ClassId").toString();
        this.stdsessionid=final_object.getProperty("_SectionId").toString();
        this.stdadmnum=final_object.getProperty("_AdmNo").toString();
        this._eupi_Reason=final_object.getProperty("_eupi_Reason").toString();



        String admd=final_object.getProperty("_AdmDate")!=null ? final_object.getProperty("_AdmDate").toString() : "";
        int y=0,m=0,d=0;
        if(admd.trim().length()==8)
        {
            y = Integer.parseInt(admd.substring(0, 4));
            m = Integer.parseInt(admd.substring(4, 6));
            d = Integer.parseInt(admd.substring(6, 8));
            Log.e("DATE DoB:",""+y+"-"+m+"-"+d);
            Log.e("DATE DoB:",""+admd);
            String mm="00";
            if(m<10)
            {
                mm="0"+m;
            }
            else
            {
                mm=""+m;
            }
            String dd="00";
            if(d<10)
            {
                dd="0"+d;
            }
            else
            {
                dd=""+d;
            }
            admd=dd+"-"+mm+"-"+y;
        }
        this.stdadmdate=admd;


        this.stdname=final_object.getProperty("_BeneficieryName").toString();
        this.stdnamehn=final_object.getProperty("_BeneficieryNameHn").toString();
        this.stdfname=final_object.getProperty("_FHName").toString();
        this.stdfnamehn=final_object.getProperty("_FHNameHn").toString();
        this.stdmname=final_object.getProperty("_MName").toString();
        this.stdmnamehn=final_object.getProperty("_MNameHn").toString();
        this.stdgenid=final_object.getProperty("_Gender").toString();

        String dobd=final_object.getProperty("_DOB")!=null ? final_object.getProperty("_DOB").toString() : "";

        if(dobd.trim().length()==8)
        {
            y = Integer.parseInt(dobd.substring(0, 4));
            m = Integer.parseInt(dobd.substring(4, 6));
            d = Integer.parseInt(dobd.substring(6, 8));
            Log.e("DATE DoB:",""+y+"-"+m+"-"+d);
            Log.e("DATE DoB:",""+dobd);
            String mm="00";
            if(m<10)
            {
                mm="0"+m;
            }
            else
            {
                mm=""+m;
            }
            String dd="00";
            if(d<10)
            {
                dd="0"+d;
            }
            else
            {
                dd=""+d;
            }
            dobd=dd+"-"+mm+"-"+y;
        }
        this.stdDOB=dobd;

       // this.stdDOB=final_object.getProperty("_DOB").toString();


        this.Age=final_object.getProperty("_Age").toString();

        this.stdcatid=final_object.getProperty("_CategaryId").toString(); //num
        this.stdisminority=final_object.getProperty("_IsMinority").toString(); //char
        this.stdishandicaped=final_object.getProperty("_IsDisabled").toString(); //char
        this.stdisbpl=final_object.getProperty("_IsIncome").toString();  //income </> then 1.5
        this.stdmobile=final_object.getProperty("_MobileNo").toString();
        this.stdaadharcardno=final_object.getProperty("_AadhaarNo").toString();
        this.stdaadharcardname=final_object.getProperty("_AadharName").toString();
        //this.IsAadhaarVerified=final_object.getProperty("_IsAadhaarVerified").toString();
        this.stdemailid=final_object.getProperty("_Email").toString();
        this.stdacnum=final_object.getProperty("_BenAccountNo").toString();
        this.stdifsc=final_object.getProperty("_IFSCCode").toString();
        this.stdacholdertypeid=final_object.getProperty("_AccountHolderType").toString(); // id-num
        this.stdacholdertypename = final_object.getProperty("_AccountHolderName").toString();

        this.AttendancePer = final_object.getProperty("_AttendancePer").toString();
        this.AttendancePerBy = final_object.getProperty("_AttendancePerBy").toString();
        this.AttendancePerDate = final_object.getProperty("_AttendancePerDate").toString();
        this.CUBy = final_object.getProperty("_CUBy").toString();
        this.CUDate = final_object.getProperty("_CUDate").toString();
        this.CUIP = final_object.getProperty("_CUIP").toString();
        this.UpdateBy = final_object.getProperty("_UpdateBy").toString();
        this.UpdateIP = final_object.getProperty("_UpdateIP").toString();
        this.IsOfflineEntry = final_object.getProperty("_IsOfflineEntry").toString();
        this.APIKey = final_object.getProperty("_APIKey").toString();
        this.IsDeleted = final_object.getProperty("_IsDeleted").toString();
        this.DeleteDate = final_object.getProperty("_DeleteDate").toString();
        this.DeletedBy = final_object.getProperty("_DeletedBy").toString();
        this.DeletedReason = final_object.getProperty("_DeletedReason").toString();
        this.DeletedRemark = final_object.getProperty("_DeletedRemark").toString();
        this.IsActive = final_object.getProperty("_IsActive").toString();
        this.ActiveDate = final_object.getProperty("_ActiveDate").toString();
        this.StopBeneficiary = final_object.getProperty("_StopBeneficiary").toString();
        this.OptVerify = final_object.getProperty("_OptVerify").toString();
        this.StopBenDate = final_object.getProperty("_StopBenDate").toString();
        this.StopBenBy = final_object.getProperty("_StopBenBy").toString();
        this.StopBenReason = final_object.getProperty("_StopBenReason").toString();
        this.UpdateDate = final_object.getProperty("_UpdateDate").toString();
        this.BankCode = final_object.getProperty("_BankCode").toString();
        this.AccMap = final_object.getProperty("_AccMap").toString();
        // this.BeneficieryStatus = final_object.getProperty("_BeneficieryStatus").toString();
        this.tempFlag = final_object.getProperty("_tempFlag").toString();
        //this.BenificiaryStatus1 = final_object.getProperty("_BenificiaryStatus1").toString();
        this.AccVerXmlFileName = final_object.getProperty("_AccVerXmlFileName").toString();
        this.eupiStatus = final_object.getProperty("_eupi_Status").toString(); //ACCP-accept
         //ACCP-accept
        //RJCT
        this.eupiReasonCode = final_object.getProperty("_eupi_ReasonCode").toString();

        this.stdisaadhaarverified=final_object.getProperty("_IsAadhaarVerified").toString();
        this.stdstatus=final_object.getProperty("_BeneficieryStatus").toString();


        this._eupi_CpsmsId=final_object.getProperty("_eupi_CpsmsId").toString();
        this._eupi_BenNameasPerBank=final_object.getProperty("_eupi_BenNameasPerBank").toString();
        this._eupi_IFSC=final_object.getProperty("_eupi_IFSC").toString();
        this._eupi_AccountNo=final_object.getProperty("_eupi_AccountNo").toString();
        this._eupi_BankName=final_object.getProperty("_eupi_BankName").toString();

        this._eupi_Address1=final_object.getProperty("_eupi_Address1").toString();
        this._eupi_Address2=final_object.getProperty("_eupi_Address2").toString();
        this._eupi_Address3=final_object.getProperty("_eupi_Address3").toString();
        this._eupi_Mobile=final_object.getProperty("_eupi_Mobile").toString();
        this._eupi_ResponseFileName=final_object.getProperty("_eupi_ResponseFileName").toString();

        this._eupi_ResponseReadDate=final_object.getProperty("_eupi_ResponseReadDate").toString();
        this._maxscore=final_object.getProperty("_maxscore").toString();
       // this.stdateendanceless=final_object.getProperty("_Isattendance75").toString();
//        if (final_object.getProperty("_AttendancePer").toString().equalsIgnoreCase("anyType{}")){
//            this.stdateendanceless="";
//        }
//        else {
            this.stdateendanceless=final_object.getProperty("_AttendancePer").toString();
      //  }




       // this.stdattseventyfiveper=final_object.getProperty("_Isattendance75").toString();
//        if (final_object.getProperty("_AttendancePer").toString().equalsIgnoreCase("anyType{}")){
//            this.stdattseventyfiveper="";
//        }else {

        this.stdattseventyfiveper=final_object.getProperty("_AttendancePer").toString();
//            if(this.stdattseventyfiveper.equalsIgnoreCase("anyType{}")){
//                this.stdattseventyfiveper="";
//            }
//            else {
//                this.stdattseventyfiveper=final_object.getProperty("_AttendancePer").toString();
//            }
       // }

       // this.stdattseventyfiveper=this.stdattseventyfiveper.equalsIgnoreCase("anyType{}")?"0":final_object.getProperty("_Isattendance75").toString();
       // this.stdattseventyfiveper=this.stdattseventyfiveper.equalsIgnoreCase("anyType{}")?"0":final_object.getProperty("_AttendancePer").toString();
       // this.stdattseventyfiveper=this.stdattseventyfiveper.equalsIgnoreCase("anyType{}")?"N":final_object.getProperty("_AttendancePer").toString();

       // this._markedDate=final_object.getProperty("_markedDate").toString();
//        if (final_object.getProperty("_AttendancePerDate").toString().equalsIgnoreCase("anyType{}")){
//            this._markedDate="";
//        }else {
            this._markedDate=final_object.getProperty("_AttendancePerDate").toString();
            this._ManualMatch=final_object.getProperty("_ManualMatch").toString();
      //  }


    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

    }

    public String getStdadmdate() {
        return stdadmdate;
    }

    public void setStdadmdate(String stdadmdate) {
        this.stdadmdate = stdadmdate;
    }

    public String getStdgenid() {
        return stdgenid;
    }

    public void setStdgenid(String stdgenid) {
        this.stdgenid = stdgenid;
    }

    public String getStdgenname() {
        return stdgenname;
    }

    public void setStdgenname(String stdgenname) {
        this.stdgenname = stdgenname;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getStdname() {
        return stdname;
    }

    public void setStdname(String stdname) {
        this.stdname = stdname;
    }

    public String getStdclass() {
        return stdclass;
    }

    public void setStdclass(String stdclass) {
        this.stdclass = stdclass;
    }

    public String getStdsession() {
        return stdsession;
    }

    public void setStdsession(String stdsession) {
        this.stdsession = stdsession;
    }

    public String getStdfname() {
        return stdfname;
    }

    public void setStdfname(String stdfname) {
        this.stdfname = stdfname;
    }

    public String getStdmname() {
        return stdmname;
    }

    public void setStdmname(String stdmname) {
        this.stdmname = stdmname;
    }

    public String getStdcatid() {
        return stdcatid;
    }

    public void setStdcatid(String stdcatid) {
        this.stdcatid = stdcatid;
    }

    public String getStdcatname() {
        return stdcatname;
    }

    public void setStdcatname(String stdcatname) {
        this.stdcatname = stdcatname;
    }

    public String getStdcatnamehn() {
        return stdcatnamehn;
    }

    public void setStdcatnamehn(String stdcatnamehn) {
        this.stdcatnamehn = stdcatnamehn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStdateendanceless() {
        return stdateendanceless;
    }

    public void setStdateendanceless(String stdateendanceless) {
        this.stdateendanceless = stdateendanceless;
    }

    public String getStdfnamehn() {
        return stdfnamehn;
    }

    public void setStdfnamehn(String stdfnamehn) {
        this.stdfnamehn = stdfnamehn;
    }

    public String getStdbenid() {
        return stdbenid;
    }

    public void setStdbenid(String stdbenid) {
        this.stdbenid = stdbenid;
    }

    public String getStdclassid() {
        return stdclassid;
    }

    public void setStdclassid(String stdclassid) {
        this.stdclassid = stdclassid;
    }

    public String getStdsessionid() {
        return stdsessionid;
    }

    public void setStdsessionid(String stdsession_id) {
        this.stdsessionid = stdsession_id;
    }

    public String getStdfyearid() {
        return stdfyearid;
    }

    public void setStdfyearid(String stdfyearid) {
        this.stdfyearid = stdfyearid;
    }

    public String getStdfyearval() {
        return stdfyearval;
    }

    public void setStdfyearval(String stdfyearval) {
        this.stdfyearval = stdfyearval;
    }

    public String getaID() {
        return aID;
    }

    public void setaID(String aID) {
        this.aID = aID;
    }

    public String getStdadmnum() {
        return stdadmnum;
    }

    public void setStdadmnum(String std_admnum) {
        this.stdadmnum = std_admnum;
    }

    public String getStdatndless() {
        return stdatndless;
    }

    public void setStdatndless(String stdatndless) {
        this.stdatndless = stdatndless;
    }

    public String getStdnamehn() {
        return stdnamehn;
    }

    public void setStdnamehn(String stdnamehn) {
        this.stdnamehn = stdnamehn;
    }

    public String getStdfyearvalhn() {
        return stdfyearvalhn;
    }

    public void setStdfyearvalhn(String stdfyearvalhn) {
        this.stdfyearvalhn = stdfyearvalhn;
    }

    public String getStdclasshn() {
        return stdclasshn;
    }

    public void setStdclasshn(String stdclasshn) {
        this.stdclasshn = stdclasshn;
    }

    public String getStdsessionhn() {
        return stdsessionhn;
    }

    public void setStdsessionhn(String stdsessionhn) {
        this.stdsessionhn = stdsessionhn;
    }

    public String getStdgennamehn() {
        return stdgennamehn;
    }

    public void setStdgennamehn(String stdgennamehn) {
        this.stdgennamehn = stdgennamehn;
    }

    public String getStdisminority() {
        return stdisminority;
    }

    public void setStdisminority(String stdisminority) {
        this.stdisminority = stdisminority;
    }

    public String getStdishandicaped() {
        return stdishandicaped;
    }

    public void setStdishandicaped(String stdishandicaped) {
        this.stdishandicaped = stdishandicaped;
    }

    public String getStdaadharcardno() {
        return stdaadharcardno;
    }

    public void setStdaadharcardno(String stdaadharcardno) {
        this.stdaadharcardno = stdaadharcardno;
    }

    public String getStdaadharcardname() {
        return stdaadharcardname;
    }

    public void setStdaadharcardname(String stdaadharcardname) {
        this.stdaadharcardname = stdaadharcardname;
    }

    public String getStdemailid() {
        return stdemailid;
    }

    public void setStdemailid(String stdemailid) {
        this.stdemailid = stdemailid;
    }

    public String getStdmobile() {
        return stdmobile;
    }

    public void setStdmobile(String stdmobile) {
        this.stdmobile = stdmobile;
    }

    public String getStdisbpl() {
        return stdisbpl;
    }

    public void setStdisbpl(String stdisbpl) {
        this.stdisbpl = stdisbpl;
    }

    public String getStdifsc() {
        return stdifsc;
    }

    public void setStdifsc(String stdifsc) {
        this.stdifsc = stdifsc;
    }

    public String getStdacnum() {
        return stdacnum;
    }

    public void setStdacnum(String stdacnum) {
        this.stdacnum = stdacnum;
    }

    public String getStdacholdername() {
        return stdacholdername;
    }

    public void setStdacholdername(String stdacholdername) {
        this.stdacholdername = stdacholdername;
    }

    public String getStdacholdertypeid() {
        return stdacholdertypeid;
    }

    public void setStdacholdertypeid(String stdacholdertypeid) {
        this.stdacholdertypeid = stdacholdertypeid;
    }

    public String getStdacholdertypename() {
        return stdacholdertypename;
    }

    public void setStdacholdertypename(String stdacholdertypename) {
        this.stdacholdertypename = stdacholdertypename;
    }

    public String getStdacholdertypenamehn() {
        return stdacholdertypenamehn;
    }

    public void setStdacholdertypenamehn(String stdacholdertypenamehn) {
        this.stdacholdertypenamehn = stdacholdertypenamehn;
    }

    public String getStdisaadhaarverified() {
        return stdisaadhaarverified;
    }

    public void setStdisaadhaarverified(String stdisaadhaarverified) {
        this.stdisaadhaarverified = stdisaadhaarverified;
    }

    public String getStdisforwarded() {
        return stdisforwarded;
    }

    public void setStdisforwarded(String stdisforwarded) {
        this.stdisforwarded = stdisforwarded;
    }

    public String getStdisverified() {
        return stdisverified;
    }

    public void setStdisverified(String stdisverified) {
        this.stdisverified = stdisverified;
    }

    public String getStdstatus() {
        return stdstatus;
    }

    public void setStdstatus(String stdstatus) {
        this.stdstatus = stdstatus;
    }

    public String getStdattseventyfiveper() {
        return stdattseventyfiveper;
    }

    public void setStdattseventyfiveper(String stdattseventyfiveper) {
        this.stdattseventyfiveper = stdattseventyfiveper;
    }

    public String getStdisreverified() {
        return stdisreverified;
    }

    public void setStdisreverified(String stdisreverified) {
        this.stdisreverified = stdisreverified;
    }

    public String getStdpfmsbennameasbank() {
        return stdpfmsbennameasbank;
    }

    public void setStdpfmsbennameasbank(String stdpfmsbennameasbank) {
        this.stdpfmsbennameasbank = stdpfmsbennameasbank;
    }

    public String getStdmnamehn() {
        return stdmnamehn;
    }

    public void setStdmnamehn(String stdmnamehn) {
        this.stdmnamehn = stdmnamehn;
    }

    public String getStdpfmsstatus() {
        return stdpfmsstatus;
    }

    public void setStdpfmsstatus(String stdpfmsstatus) {
        this.stdpfmsstatus = stdpfmsstatus;
    }

    public String getStdisapprovedandlocked() {
        return stdisapprovedandlocked;
    }

    public void setStdisapprovedandlocked(String stdisapprovedandlocked) {
        this.stdisapprovedandlocked = stdisapprovedandlocked;
    }

    public String getStdDOB() {
        return stdDOB;
    }

    public void setStdDOB(String stdDOB) {
        this.stdDOB = stdDOB;
    }

    public String getWardVillage() {
        return WardVillage;
    }

    public void setWardVillage(String wardVillage) {
        WardVillage = wardVillage;
    }

    public String getDise_Code() {
        return Dise_Code;
    }

    public void setDise_Code(String dise_Code) {
        Dise_Code = dise_Code;
    }



    public String getAttendancePer() {
        return AttendancePer;
    }

    public void setAttendancePer(String attendancePer) {
        AttendancePer = attendancePer;
    }

    public String getAttendancePerBy() {
        return AttendancePerBy;
    }

    public void setAttendancePerBy(String attendancePerBy) {
        AttendancePerBy = attendancePerBy;
    }

    public String getAttendancePerDate() {
        return AttendancePerDate;
    }

    public void setAttendancePerDate(String attendancePerDate) {
        AttendancePerDate = attendancePerDate;
    }

    public String getCUBy() {
        return CUBy;
    }

    public void setCUBy(String CUBy) {
        this.CUBy = CUBy;
    }

    public String getCUDate() {
        return CUDate;
    }

    public void setCUDate(String CUDate) {
        this.CUDate = CUDate;
    }

    public String getCUIP() {
        return CUIP;
    }

    public void setCUIP(String CUIP) {
        this.CUIP = CUIP;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

    public String getUpdateIP() {
        return UpdateIP;
    }

    public void setUpdateIP(String updateIP) {
        UpdateIP = updateIP;
    }

    public String getIsOfflineEntry() {
        return IsOfflineEntry;
    }

    public void setIsOfflineEntry(String isOfflineEntry) {
        IsOfflineEntry = isOfflineEntry;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getDeleteDate() {
        return DeleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        DeleteDate = deleteDate;
    }

    public String getDeletedBy() {
        return DeletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        DeletedBy = deletedBy;
    }

    public String getDeletedReason() {
        return DeletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        DeletedReason = deletedReason;
    }

    public String getDeletedRemark() {
        return DeletedRemark;
    }

    public void setDeletedRemark(String deletedRemark) {
        DeletedRemark = deletedRemark;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getActiveDate() {
        return ActiveDate;
    }

    public void setActiveDate(String activeDate) {
        ActiveDate = activeDate;
    }

    public String getMAppVersion() {
        return MAppVersion;
    }

    public void setMAppVersion(String MAppVersion) {
        this.MAppVersion = MAppVersion;
    }

    public String getStopBeneficiary() {
        return StopBeneficiary;
    }

    public void setStopBeneficiary(String stopBeneficiary) {
        StopBeneficiary = stopBeneficiary;
    }

    public String getOptVerify() {
        return OptVerify;
    }

    public void setOptVerify(String optVerify) {
        OptVerify = optVerify;
    }

    public String getStopBenDate() {
        return StopBenDate;
    }

    public void setStopBenDate(String stopBenDate) {
        StopBenDate = stopBenDate;
    }

    public String getStopBenBy() {
        return StopBenBy;
    }

    public void setStopBenBy(String stopBenBy) {
        StopBenBy = stopBenBy;
    }

    public String getStopBenReason() {
        return StopBenReason;
    }

    public void setStopBenReason(String stopBenReason) {
        StopBenReason = stopBenReason;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getAccMap() {
        return AccMap;
    }

    public void setAccMap(String accMap) {
        AccMap = accMap;
    }



    public String getTempFlag() {
        return tempFlag;
    }

    public void setTempFlag(String tempFlag) {
        this.tempFlag = tempFlag;
    }

    public String getBenificiaryStatus1() {
        return BenificiaryStatus1;
    }

    public void setBenificiaryStatus1(String benificiaryStatus1) {
        BenificiaryStatus1 = benificiaryStatus1;
    }

    public String getAccVerXmlFileName() {
        return AccVerXmlFileName;
    }

    public void setAccVerXmlFileName(String accVerXmlFileName) {
        AccVerXmlFileName = accVerXmlFileName;
    }

    public String getEupiStatus() {
        return eupiStatus;
    }

    public void setEupiStatus(String eupiStatus) {
        this.eupiStatus = eupiStatus;
    }

    public String getEupiReasonCode() {
        return eupiReasonCode;
    }

    public void setEupiReasonCode(String eupiReasonCode) {
        this.eupiReasonCode = eupiReasonCode;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getAreaType() {
        return AreaType;
    }

    public void setAreaType(String areaType) {
        AreaType = areaType;
    }

    public String getWardCode() {
        return WardCode;
    }

    public void setWardCode(String wardCode) {
        WardCode = wardCode;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }


    public String get_eupi_CpsmsId() {
        return _eupi_CpsmsId;
    }

    public void set_eupi_CpsmsId(String _eupi_CpsmsId) {
        this._eupi_CpsmsId = _eupi_CpsmsId;
    }

    public String get_eupi_BenNameasPerBank() {
        return _eupi_BenNameasPerBank;
    }

    public void set_eupi_BenNameasPerBank(String _eupi_BenNameasPerBank) {
        this._eupi_BenNameasPerBank = _eupi_BenNameasPerBank;
    }

    public String get_eupi_IFSC() {
        return _eupi_IFSC;
    }

    public void set_eupi_IFSC(String _eupi_IFSC) {
        this._eupi_IFSC = _eupi_IFSC;
    }

    public String get_eupi_AccountNo() {
        return _eupi_AccountNo;
    }

    public void set_eupi_AccountNo(String _eupi_AccountNo) {
        this._eupi_AccountNo = _eupi_AccountNo;
    }

    public String get_eupi_BankName() {
        return _eupi_BankName;
    }

    public void set_eupi_BankName(String _eupi_BankName) {
        this._eupi_BankName = _eupi_BankName;
    }

    public String get_eupi_Address1() {
        return _eupi_Address1;
    }

    public void set_eupi_Address1(String _eupi_Address1) {
        this._eupi_Address1 = _eupi_Address1;
    }

    public String get_eupi_Address2() {
        return _eupi_Address2;
    }

    public void set_eupi_Address2(String _eupi_Address2) {
        this._eupi_Address2 = _eupi_Address2;
    }

    public String get_eupi_Address3() {
        return _eupi_Address3;
    }

    public void set_eupi_Address3(String _eupi_Address3) {
        this._eupi_Address3 = _eupi_Address3;
    }

    public String get_eupi_Mobile() {
        return _eupi_Mobile;
    }

    public void set_eupi_Mobile(String _eupi_Mobile) {
        this._eupi_Mobile = _eupi_Mobile;
    }

    public String get_eupi_ResponseFileName() {
        return _eupi_ResponseFileName;
    }

    public void set_eupi_ResponseFileName(String _eupi_ResponseFileName) {
        this._eupi_ResponseFileName = _eupi_ResponseFileName;
    }

    public String get_eupi_ResponseReadDate() {
        return _eupi_ResponseReadDate;
    }

    public void set_eupi_ResponseReadDate(String _eupi_ResponseReadDate) {
        this._eupi_ResponseReadDate = _eupi_ResponseReadDate;
    }

    public String get_maxscore() {
        return _maxscore;
    }

    public void set_maxscore(String _maxscore) {
        this._maxscore = _maxscore;
    }



    public String get_markedDate() {
        return _markedDate;
    }

    public void set_markedDate(String _markedDate) {
        this._markedDate = _markedDate;
    }

    public String get_ManualMatch() {
        return _ManualMatch;
    }

    public void set_ManualMatch(String _ManualMatch) {
        this._ManualMatch = _ManualMatch;
    }

    public String get_sendPfms_status() {
        return _sendPfms_status;
    }

    public void set_sendPfms_status(String _sendPfms_status) {
        this._sendPfms_status = _sendPfms_status;
    }

    public String get_sendPfms_id() {
        return _sendPfms_id;
    }

    public void set_sendPfms_id(String _sendPfms_id) {
        this._sendPfms_id = _sendPfms_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_eupi_Reason() {
        return _eupi_Reason;
    }

    public void set_eupi_Reason(String _eupi_Reason) {
        this._eupi_Reason = _eupi_Reason;
    }
}

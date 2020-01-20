package bih.nic.medhasoft.database;

import android.content.Context;
import android.util.Log;

import bih.nic.medhasoft.EditBendDetailsActivity;
import bih.nic.medhasoft.Model.searchuniqModel;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.SearchUniqueNoManual;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.EmployeeListEntity;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Findpin;
import bih.nic.medhasoft.entity.Get_usersalldetails;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.UserDetails;
import bih.nic.medhasoft.entity.UserDetails1;
import bih.nic.medhasoft.entity.Versioninfo;
import bih.nic.medhasoft.utility.ShorCutICON;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.util.Log;


public class WebServiceHelper  {


    // public static final String SERVICENAMESPACE = "http://10.133.20.159/";
    public static final String SERVICENAMESPACE = "http://164.100.37.26/";
    // public static final String SERVICEURL = "http://10.133.20.159/TestService/EducationAPI.asmx";
    public static final String SERVICEURL = "http://164.100.37.26/medhasoft/EducationAPI.asmx";

    public static final String APPVERSION_METHOD = "getAppLatest";

    private static final String Authenticate_School_User = "Authenticate";

    private static final String GETClassListDetails="ClassList";
    private static final String GETSessionListDetails="SectionList";
    private static final String GetFYearList = "FYearList";

    private static final String GETStudentAttendanceDetails="StudentDetailsAll";


    private static final String UPLOAD_UPDATED_STD_DETAILS="UpdateStudent";

    public static final String REGISTRATIONOTP = "ValidateOTP";
    public static final String FORGETPIN = "GenerateMobOTP";
    public static final String FORGETOTPPIN = "ValidateMobOTP";
    public static final String UPLOAD_Match_Status = "UpdateMismatch";
    public static final String AUTHENTICATE_METHOD = "BlockAuthenticate";
    public static final String UPLOAD_Barcode_Status = "ValidateDiseBillPdf";
    public static final String ValidateDiseBillPdfSerial = "ValidateDiseBillPdfSerial";
    public static final String INSERT_Final_details = "ValidateDiseBillPdfMark";

    //private static final String GETStudentAttendanceDetails="getdata";

    private static final String GETEmployeeListDetails="getOfficer";
    // private static final String UPLOAD_ATT_OF_PER_LESS_THEN_75Percent="MarkAttPer";
    private static final String UPLOAD_ATT_OF_PER_LESS_THEN_75Percent="Attendance";  // for entry
    // private static final String UPLOAD_ATT_OF_PER_LESS_THEN_75Percent="AttendanceUpdate";

    ///--fmwU5VppkTdUrro59LIeDlsjsgUuR1ol

    private static final String API_Key="fmwU5VppkTdUrro59LIeDlsjsgUuR1ol";

    static String rest;
    static ShorCutICON ico;

    private static String parseSOAPResponse(SoapObject response) {
        String res = null;
        SoapObject result = (SoapObject) response.getProperty("InsertJFMWorkResult");
        res = result.toString();
        // ...
        // more code in here to populate the CityForecastBO object from the
        // response object (illustrated in the steps below)

        return res;
    }
    public static Versioninfo CheckVersion(String imei, String version) {
        Versioninfo versioninfo;
        SoapObject res1;
        try {
            //res1=getServerData(APPVERSION_METHOD,Versioninfo.Versioninfo_CLASS,"IMEI","Ver",imei,version);
            SoapObject request = new SoapObject(SERVICENAMESPACE, APPVERSION_METHOD);
            request.addProperty("IMEI", imei);
            request.addProperty("Ver", version);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, Versioninfo.Versioninfo_CLASS.getSimpleName(), Versioninfo.Versioninfo_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + APPVERSION_METHOD, envelope);
            res1 = (SoapObject) envelope.getResponse();
            SoapObject final_object = (SoapObject) res1.getProperty(0);

            versioninfo = new Versioninfo(final_object);

        } catch (Exception e) {

            return null;
        }
        return versioninfo;

    }

    public static ArrayList<FYEAR> GetFYearList() {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GetFYearList);
        //request.addProperty("APIKey", API_Key);
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.addMapping(SERVICENAMESPACE, FYEAR.USER_CLASS.getSimpleName(), FYEAR.USER_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + GetFYearList,
                    envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();

        ArrayList<FYEAR> pvmArrayList = new ArrayList<>();

        for (int ii = 0; ii < TotalProperty; ii++) {
            if (res1.getProperty(ii) != null) {
                Object property = res1.getProperty(ii);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    FYEAR district = new FYEAR(final_object);
                    pvmArrayList.add(district);
                }
            } else
                return pvmArrayList;
        }


        return pvmArrayList;
    }



    public static String forget_PIN(Findpin userid) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE,FORGETPIN);
            request.addProperty("_MobileNo",userid.getEt_mobile());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    UserDetails.USER_CLASS.getSimpleName(),
                    UserDetails.USER_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + FORGETPIN,
                    envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String forget_Password_otp(Findpin data) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE,FORGETOTPPIN);
            request.addProperty("_MobileNo",data.getEt_mobile());
            request.addProperty("_OTP",data.getPinotp());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    UserDetails.USER_CLASS.getSimpleName(),
                    UserDetails.USER_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + FORGETOTPPIN,
                    envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // public static ArrayList<studentList> GetStudentAttendanceList(String disecode,String classid) {
    public static ArrayList<studentList> GetStudentAttendanceList(String disecode) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GETStudentAttendanceDetails);

        request.addProperty("DiseCode", disecode);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, studentList.getdata.getSimpleName(), studentList.getdata);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + GETStudentAttendanceDetails, envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();
        ArrayList<studentList> pvmArrayList = new ArrayList<studentList>();
        if(TotalProperty>0) {


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        studentList state = new studentList(final_object);
                        pvmArrayList.add(state);
                    }
                } else
                    return pvmArrayList;
            }
        }

        return pvmArrayList;
    }

    public static ArrayList<CLASSLIST> GetClassList() {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GETClassListDetails);

        // request.addProperty("APIKey", API_Key);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    studentList.getdata.getSimpleName(), studentList.getdata);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + GETClassListDetails, envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();
        ArrayList<CLASSLIST> pvmArrayList = new ArrayList<>();
        if(TotalProperty>0) {


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        CLASSLIST state = new CLASSLIST(final_object);
                        pvmArrayList.add(state);
                    }
                } else
                    return pvmArrayList;
            }
        }


        return pvmArrayList;
    }

    public static ArrayList<SESSIONLIST> GetSessionList() {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GETSessionListDetails);

        //request.addProperty("APIKey", API_Key);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    studentList.getdata.getSimpleName(), studentList.getdata);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + GETSessionListDetails, envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();
        ArrayList<SESSIONLIST> pvmArrayList = new ArrayList<>();
        if(TotalProperty>0) {


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        SESSIONLIST state = new SESSIONLIST(final_object);
                        pvmArrayList.add(state);
                    }
                } else
                    return pvmArrayList;
            }
        }


        return pvmArrayList;
    }
    public static ArrayList<EmployeeListEntity> GetEmployeeListDetails(String officer_code) {



        SoapObject request = new SoapObject(SERVICENAMESPACE, GETEmployeeListDetails);

        request.addProperty("_Officer_Code", officer_code);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    EmployeeListEntity.SyncExstngSpCpData_CLASS.getSimpleName(), EmployeeListEntity.SyncExstngSpCpData_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + GETEmployeeListDetails, envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();
        ArrayList<EmployeeListEntity> pvmArrayList = new ArrayList<>();
        if(TotalProperty>0) {


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        EmployeeListEntity state = new EmployeeListEntity(final_object);
                        pvmArrayList.add(state);
                    }
                } else
                    return pvmArrayList;
            }
        }


        return pvmArrayList;
    }
    public static Get_usersalldetails GetUserallDetails(String dcode,String mnum, String uotp) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, Authenticate_School_User);

        request.addProperty("DiseCode", dcode);
        request.addProperty("MobileNumber", mnum);
        request.addProperty("otp", uotp);
        Get_usersalldetails userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,Get_usersalldetails.USER_CLASS.getSimpleName(), Get_usersalldetails.USER_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + Authenticate_School_User, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new Get_usersalldetails(res1);

        } catch (Exception e) {

            e.printStackTrace();
            if(e.getLocalizedMessage()!=null) {
                Log.e("EXC", e.getLocalizedMessage());
            }
            return null;
        }
        return userDetails;

    }
    public static String UploadAttendanceData(Context context,String updateBy,String updtedDate,String diceCode,String a_ID,String status)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, UPLOAD_ATT_OF_PER_LESS_THEN_75Percent);

        // public string Attendance(string Disecode, string BeneficeryID,
        // string IsAttendanceLess75, string MarkedDate, string UpdateDate)

        request.addProperty("Disecode", diceCode);
        // request.addProperty("BeneficeryID", benID);
        request.addProperty("a_Id", a_ID);
        // request.addProperty("IsAttendanceLess75", status);
        request.addProperty("AttendancePer", status);
        // request.addProperty("MarkedDate", updtedDate);        //----for entry
        request.addProperty("AttendancePerBy", diceCode);        //----for entry
        // request.addProperty("UpdateDate", updtedDate);
        request.addProperty("AttendancePerDate", updtedDate);

//        request.addProperty("APIKey", API_Key);
//        request.addProperty("UpdatedBy", updateBy); //mobile number





        Log.e("a_ID",a_ID + " Status" + status);
        Log.e("---------","-------");

        MARKATTPERRESULT pvmArrayList;
        // SoapObject res1;
        Object res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    MARKATTPERRESULT.Remarks_CLASS.getSimpleName(),  MARKATTPERRESULT.Remarks_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + UPLOAD_ATT_OF_PER_LESS_THEN_75Percent, envelope);

//            res1 = (SoapObject) envelope.getResponse();
//            int TotalProperty = res1.getPropertyCount();
//            pvmArrayList = new MARKATTPERRESULT(res1);

            res1 =  envelope.getResponse();


        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return res1.toString();
    }

    public static String UploadStdUpdatedDetails(Context context,String device,String appver,String... param) {

        String response = "";
        SoapObject request = new SoapObject(SERVICENAMESPACE, UPLOAD_UPDATED_STD_DETAILS);

        Log.e("---------","-------");

//        request.addProperty("APIKey", API_Key);
//        Log.e("APIKey",API_Key);
//        request.addProperty("FinYear_Id", param[24]); //mobile number
//        Log.e("FinYear_Id", param[24]);

        request.addProperty("ClassId", param[26]);
        Log.e("ClassId",param[26]);
        request.addProperty("SectionId", param[27]);
        Log.e("SectionId",param[27]);
        request.addProperty("a_BenId",  param[31]);  //a_ID
        Log.e("a_BenId",param[31]);
        request.addProperty("WardCode", param[32]);
        Log.e("WardCode",param[32]);
        request.addProperty("DistrictCode", param[39]);
        Log.e("DistrictCode",param[39]);
        request.addProperty("BlockCode", param[34]);
        Log.e("BlockCode",param[34]);

        request.addProperty("AdmNo",  param[0]);
        Log.e("AdmNo",param[0]);
        request.addProperty("AdmDate",  param[1]);
        Log.e("AdmDate",param[1]);

        request.addProperty("BeneficieryName",  param[2]);
        Log.e("BeneficieryName",param[2]);
        request.addProperty("BeneficieryNameHN",  param[3]);
        Log.e("BeneficieryNameHN",param[3]);
        request.addProperty("FHName",  param[4]);
        Log.e("FHName",param[4]);
        request.addProperty("FHNameHN",  param[5]);
        Log.e("FHNameHN",param[5]);
        request.addProperty("MName", param[6]);
        Log.e("MName",param[6]);
        request.addProperty("MNameHn", param[7]);
        Log.e("MNameHn",param[7]);
        request.addProperty("Gender",  param[8]);
        Log.e("Gender",param[8]);
        request.addProperty("DOB",  param[28]);
        Log.e("DOB",param[28]);

        request.addProperty("CategaryId",  param[11]);
        Log.e("CategaryId",param[11]);
        request.addProperty("MobileNo",  param[19]);
        Log.e("MobileNo",param[19]);
        request.addProperty("IsMinority",  param[14]);
        Log.e("IsMinority",param[14]);

        request.addProperty("IsDisabled",  param[15]);
        Log.e("IsDisabled",param[15]);
        request.addProperty("IsIncome",  param[16]);
        Log.e("IsIncome",param[16]);

        request.addProperty("AadhaarNo",  param[17]);
        Log.e("AadhaarNo",param[17]);
        request.addProperty("AadhaarName",  param[18]);
        Log.e("AadhaarName",param[18]);
        request.addProperty("AccountHolderType",  param[23]);
        Log.e("AccountHolderType",param[23]);
        request.addProperty("AccountHolderName",  param[40]);
        Log.e("AccountHolderName",""+param[40]);
        request.addProperty("IFSCcode", param[21]);
        Log.e("IFSCcode",param[21]);
        request.addProperty("BenAccountNo",  param[22]);
        Log.e("BenAccountNo",param[22]);

        request.addProperty("CUBy",  param[35]);
        Log.e("CUBy",param[35]);
        request.addProperty("OptVerify",  param[37]);
        Log.e("OptVerify",param[37]);
        request.addProperty("CUDate",  param[36]);
        Log.e("CUDate",param[36]);
        request.addProperty("IP",  param[38]);
       // Log.e("IP",param[38]);
        request.addProperty("RePFMS",  param[41]);
        request.addProperty("DeviceId",  device);
        request.addProperty("App_version",appver);
//        request.addProperty("EmailId",  param[20]);
//        Log.e("EmailId",param[20]);
//        request.addProperty("EntryBy",  param[29]);
//        Log.e("EntryBy",param[29]);
//        request.addProperty("EntryDate",  param[30]);
//        Log.e("EntryDate",param[30]);
//        request.addProperty("IsAadhaarVerified",  "");
//        Log.e("IsAadhaarVerified","");
//
//        request.addProperty("WardVillage",  param[32]);


        MARKATTPERRESULT pvmArrayList;
        Object res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,
                    MARKATTPERRESULT.Remarks_CLASS.getSimpleName(),  MARKATTPERRESULT.Remarks_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + UPLOAD_UPDATED_STD_DETAILS, envelope);

            res1 =  envelope.getResponse();
            // int TotalProperty = res1.getPropertyCount();
            // pvmArrayList = new MARKATTPERRESULT(res1);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return res1.toString();
    }


    //public static MARKATTPERRESULT UploadStdUpdatedDetails(Context context,String... param) {
    public static String UploadUploadAttendanceDataArray(Context context,String... param)
    {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,UPLOAD_UPDATED_STD_DETAILS, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element stdBasicElement = doc.getDocumentElement();

        Element PoleType = doc.createElement("APIKey");
        PoleType.appendChild(doc.createTextNode(API_Key));
        stdBasicElement.appendChild(PoleType);

        Element pdElement = doc.createElement("_BenDetails");
        Element isa = doc.createElement("IsAuthenticated");
        isa.appendChild(doc.createTextNode("true"));
        pdElement.appendChild(isa);

        Element aid = doc.createElement("a_Id");
        aid.appendChild(doc.createTextNode("1"));
        pdElement.appendChild(aid);

        Element bid = doc.createElement("Ben_Id");
        bid.appendChild(doc.createTextNode(param[31]));
        pdElement.appendChild(bid);
        Log.e("Ben_Id",param[31]);

        Element dcode = doc.createElement("DiseCode");
        dcode.appendChild(doc.createTextNode(param[25]));
        pdElement.appendChild(dcode);
        stdBasicElement.appendChild(pdElement);

        Element fyElement = doc.createElement("Financialyear");
        Element fid = doc.createElement("FinYear_Id");
        fid.appendChild(doc.createTextNode(param[24]));
        fyElement.appendChild(fid);
        Element fyr = doc.createElement("FinYear_Name");
        fyr.appendChild(doc.createTextNode(param[24]));
        fyElement.appendChild(fyr);
        Element fyrhn = doc.createElement("FinYear_NameHn");
        fyrhn.appendChild(doc.createTextNode(param[24]));
        fyElement.appendChild(fyrhn);
        stdBasicElement.appendChild(fyElement);

        Element bcElement = doc.createElement("Ben_Class");
        Element msg = doc.createElement("Message");
        msg.appendChild(doc.createTextNode("BC"));
        bcElement.appendChild(msg);
        Element cid = doc.createElement("Class_Id");
        cid.appendChild(doc.createTextNode(param[26]));
        bcElement.appendChild(cid);
        Element cn = doc.createElement("Class_Name");
        cn.appendChild(doc.createTextNode(param[26]));
        bcElement.appendChild(cn);
        Element cnhn = doc.createElement("Class_NameHn");
        cnhn.appendChild(doc.createTextNode(param[26]));
        bcElement.appendChild(cnhn);
        stdBasicElement.appendChild(bcElement);

        Element bsElement = doc.createElement("Ben_Section");
        Element bsid = doc.createElement("Section_Id");
        bsid.appendChild(doc.createTextNode(param[27]));
        bsElement.appendChild(bsid);
        Element bsn = doc.createElement("Section_Name");
        bsn.appendChild(doc.createTextNode(param[27]));
        bsElement.appendChild(bsn);
        Element bsnhn = doc.createElement("Section_NameHn");
        bsnhn.appendChild(doc.createTextNode(param[27]));
        bsElement.appendChild(bsnhn);
        Element bsmsg = doc.createElement("Message");
        bsmsg.appendChild(doc.createTextNode("BS"));
        bsElement.appendChild(bsmsg);
        stdBasicElement.appendChild(bsElement);

        Element ano = doc.createElement("AddmissionNo");
        ano.appendChild(doc.createTextNode(param[0]));
        stdBasicElement.appendChild(ano);

        Element andt = doc.createElement("Addmissiondate");
        andt.appendChild(doc.createTextNode(param[1]));
        stdBasicElement.appendChild(andt);

        Element bname = doc.createElement("Ben_Name");
        bname.appendChild(doc.createTextNode(param[2]));
        stdBasicElement.appendChild(bname);

        Element bnamehn = doc.createElement("Ben_NameHN");
        bnamehn.appendChild(doc.createTextNode(param[3]));
        stdBasicElement.appendChild(bnamehn);

        Element benfname = doc.createElement("BenFH_Name");
        benfname.appendChild(doc.createTextNode(param[4]));
        stdBasicElement.appendChild(benfname);

        Element benfnamehn = doc.createElement("BenFH_NameHN");
        benfnamehn.appendChild(doc.createTextNode(param[5]));
        stdBasicElement.appendChild(benfnamehn);

        Element benmnam = doc.createElement("BenM_Name");
        benmnam.appendChild(doc.createTextNode(param[6]));
        stdBasicElement.appendChild(benmnam);

        Element benmnamhn = doc.createElement("BenM_NameHN");
        benmnamhn.appendChild(doc.createTextNode(param[7]));
        stdBasicElement.appendChild(benmnamhn);

        Element bendob = doc.createElement("Ben_DOB");
        bendob.appendChild(doc.createTextNode(param[28]));
        stdBasicElement.appendChild(bendob);


        Element bgElement = doc.createElement("Ben_Gender");
        Element bgmsg= doc.createElement("Message");
        bgmsg.appendChild(doc.createTextNode("M3"));
        bgElement.appendChild(bgmsg);
        Element bgid = doc.createElement("Gender_Id");
        bgid.appendChild(doc.createTextNode(param[8]));
        bgElement.appendChild(bgid);
        Element bgn = doc.createElement("Gender_Name");
        bgn.appendChild(doc.createTextNode(param[8]));
        bgElement.appendChild(bgn);
        Element bgnhn = doc.createElement("Gender_NameHn");
        bgnhn.appendChild(doc.createTextNode(param[8]));
        bgElement.appendChild(bgnhn);
        stdBasicElement.appendChild(bgElement);

        Element bctElement = doc.createElement("Ben_Category");
        Element bcmsg= doc.createElement("Message");
        bcmsg.appendChild(doc.createTextNode("1"));
        bctElement.appendChild(bcmsg);
        Element ctid = doc.createElement("Category_Id");
        ctid.appendChild(doc.createTextNode(param[11]));
        bctElement.appendChild(ctid);
        Element cnam = doc.createElement("Category_Name");
        cnam.appendChild(doc.createTextNode(param[11]));
        bctElement.appendChild(cnam);
        Element cnamhn = doc.createElement("Category_NameHn");
        cnamhn.appendChild(doc.createTextNode(param[11]));
        bctElement.appendChild(cnamhn);
        stdBasicElement.appendChild(bctElement);


        Element aadr = doc.createElement("AadharCardNo");
        aadr.appendChild(doc.createTextNode(param[17]));
        stdBasicElement.appendChild(aadr);

        Element aadrnam = doc.createElement("AadharCardName");
        aadrnam.appendChild(doc.createTextNode(param[18]));
        stdBasicElement.appendChild(aadrnam);

        Element email = doc.createElement("EmailId");
        email.appendChild(doc.createTextNode( param[20]));
        stdBasicElement.appendChild(email);

        Element mnum = doc.createElement("MobileNo");
        mnum.appendChild(doc.createTextNode( param[19]));
        stdBasicElement.appendChild(mnum);

        Element ifsc = doc.createElement("IFSC");
        ifsc.appendChild(doc.createTextNode(param[16]));
        stdBasicElement.appendChild(ifsc);

        Element acn = doc.createElement("AccountNo");
        acn.appendChild(doc.createTextNode(param[22]));
        stdBasicElement.appendChild(acn);

        Element achn = doc.createElement("AccountHolderName");
        achn.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(achn);

        Element bnachElement = doc.createElement("Ben_AccountHolderType");
        Element bnachmsg= doc.createElement("Message");
        bnachmsg.appendChild(doc.createTextNode("M4"));
        bnachElement.appendChild(bnachmsg);
        Element bnachid = doc.createElement("AccountHolderType_Id");
        bnachid.appendChild(doc.createTextNode( param[23]));
        bnachElement.appendChild(bnachid);
        Element bnachnam = doc.createElement("AccountHolderType_Name");
        bnachnam.appendChild(doc.createTextNode( param[23]));
        bnachElement.appendChild(bnachnam);
        Element bnachnamhn = doc.createElement("AccountHolderType_NameHn");
        bnachnamhn.appendChild(doc.createTextNode( param[23]));
        bnachElement.appendChild(bnachnamhn);
        stdBasicElement.appendChild(bnachElement);

        Element enby = doc.createElement("EntryBy");
        enby.appendChild(doc.createTextNode(param[29]));
        stdBasicElement.appendChild(enby);

        Element endt = doc.createElement("EntryDate");
        endt.appendChild(doc.createTextNode(param[30]));
        stdBasicElement.appendChild(endt);

        Element endtf = doc.createElement("EntryDate_From");
        endtf.appendChild(doc.createTextNode(param[30]));
        stdBasicElement.appendChild(endtf);

        Element endtt = doc.createElement("EntryDate_To");
        endtt.appendChild(doc.createTextNode(param[30]));
        stdBasicElement.appendChild(endtt);

        Element endto = doc.createElement("OfflineEntryDate");
        endto.appendChild(doc.createTextNode(param[30]));
        stdBasicElement.appendChild(endto);

        Element qrElement = doc.createElement("queryresult");
        Element qrs= doc.createElement("Status");
        qrs.appendChild(doc.createTextNode("ok"));
        qrElement.appendChild(qrs);
        Element qref = doc.createElement("RowsEffected");
        qref.appendChild(doc.createTextNode("1"));
        qrElement.appendChild(qref);
        Element qrem = doc.createElement("Message");
        qrem.appendChild(doc.createTextNode("good job"));
        qrElement.appendChild(qrem);
        stdBasicElement.appendChild(qrElement);

        Element bmsg = doc.createElement("Message");
        bmsg.appendChild(doc.createTextNode("M5"));
        stdBasicElement.appendChild(bmsg);

        Element benid1 = doc.createElement("Ben_Id1");
        benid1.appendChild(doc.createTextNode(param[31]));
        stdBasicElement.appendChild(benid1);

        Element bnst = doc.createElement("BenificiaryStatus");
        bnst.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(bnst);

        Element benst = doc.createElement("BenStatus");
        benst.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(benst);

        Element pfmst = doc.createElement("PFMS_Status");
        pfmst.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(pfmst);

        Element pfmsbnbnk = doc.createElement("pfms_BenNameAsPerBank");
        pfmsbnbnk.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(pfmsbnbnk);

        Element pfmsrsdt = doc.createElement("pfms_ResponseDate");
        pfmsrsdt.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(pfmsrsdt);

        Element sxsts = doc.createElement("SoundXStatus");
        sxsts.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(sxsts);

        Element pfmrejd = doc.createElement("pfms_RejectionDetail");
        pfmrejd.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(pfmrejd);

        Element ismin = doc.createElement("IsMinority");
        ismin.appendChild(doc.createTextNode(param[14]));
        stdBasicElement.appendChild(ismin);

        Element ishnd = doc.createElement("IsHandicapped");
        ishnd.appendChild(doc.createTextNode(param[15]));
        stdBasicElement.appendChild(ishnd);

        Element isbpl = doc.createElement("IsBPL");
        isbpl.appendChild(doc.createTextNode(param[16]));
        stdBasicElement.appendChild(isbpl);

        Element isoffline = doc.createElement("IsOfflineEntry");
        isoffline.appendChild(doc.createTextNode("N"));
        stdBasicElement.appendChild(isoffline);

        Element isaadver = doc.createElement("IsAadhaarVerified");
        isaadver.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(isaadver);

        Element att75 = doc.createElement("AttSeventyFivePercent");
        att75.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(att75);

        Element isdel = doc.createElement("IsDelete");
        isdel.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(isdel);

        Element isstp = doc.createElement("IsStop");
        isstp.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(isstp);

        Element isrs = doc.createElement("IsResume");
        isrs.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(isrs);

        Element delrem = doc.createElement("deleteremarks");
        delrem.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(delrem);

        Element stprm = doc.createElement("stopremarks");
        stprm.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(stprm);

        Element delby = doc.createElement("deletedby");
        delby.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(delby);

        Element stpby = doc.createElement("stopby");
        stpby.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(stpby);

        Element rsmby = doc.createElement("resumeby");
        rsmby.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(rsmby);

        Element entip = doc.createElement("EntryIP");
        entip.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(entip);

        Element wrdv = doc.createElement("WardVillage");
        wrdv.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(wrdv);

        Element encmob = doc.createElement("encryptedMobile");
        encmob.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(encmob);

        Element encac = doc.createElement("encryptedAccount");
        encac.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(encac);

        Element encaad = doc.createElement("encryptedAadhaar");
        encaad.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(encaad);

        Element encaadh = doc.createElement("encryptedAadhaar");
        encaadh.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(encaadh);

        Element schid = doc.createElement("SchemeID");
        schid.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(schid);

        Element schnam = doc.createElement("SchemeName");
        schnam.appendChild(doc.createTextNode(""));
        stdBasicElement.appendChild(schnam);

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try {

            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "0";
            }

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            BasicHttpResponse httpResponse = null;

            try {
                trans.transform(source, result);
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "0";
            }

            String SOAPRequestXML = sw.toString();

            String startTag = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"   >  "
                    + "<soap:Body > ";
            String endTag = "</soap:Body > " + "</soap:Envelope>";

            HttpPost httppost = new HttpPost(SERVICEURL);

            // Log.i("Request: ", "XML Request= " + startTag + SOAPRequestXML
            // + endTag);

            StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML
                    + endTag, HTTP.UTF_8);

            sEntity.setContentType("text/xml");
            // httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setEntity(sEntity);

            HttpClient httpclient = new DefaultHttpClient();

            httpResponse = (BasicHttpResponse) httpclient.execute(httppost);

            Log.i("Response: ", httpResponse.getStatusLine().toString());

            if (httpResponse.getStatusLine().getStatusCode() == 200
                    || httpResponse.getStatusLine().getReasonPhrase()
                    .toString().equals("OK")) {
                res = "1";
            } else {
                res = "0";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }

        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());

        return res;
    }



    public static String UploadMatchStatusa(Context context,String diceCode,String a_ID,String status,String deviceid,String appver)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, UPLOAD_Match_Status);

        request.addProperty("Disecode", diceCode);
        request.addProperty("a_id", a_ID);
        request.addProperty("ManualMatch", status);
        if (status.equals("Y")){
            request.addProperty("maxscore", "50");
        }
        else if(status.equals("N")){
            request.addProperty("maxscore", "0");
        }

        request.addProperty("DeviceID", deviceid);
        request.addProperty("M_AppVersion", appver);


        Log.e("a_ID",a_ID + " Status" + status);
        Log.e("---------","-------");

        MARKATTPERRESULT pvmArrayList;
        // SoapObject res1;
        Object res1;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + UPLOAD_Match_Status,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();



        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return rest.toString();
    }




    public static BlkUserDetails AuthenticateBlock(String UserName, String Password) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, AUTHENTICATE_METHOD);

        request.addProperty("BlockCode", UserName);
        request.addProperty("Password", Password);
        BlkUserDetails userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BlkUserDetails.BlkUSER_CLASS.getSimpleName(), BlkUserDetails.BlkUSER_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + AUTHENTICATE_METHOD, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BlkUserDetails(res1);

        }
        catch (Exception e) {
        e.printStackTrace();
            return null;
        }
        return userDetails;

    }




    public static BarcodeEntity UploadBarcodeResponse(String blkcode, String qrcodr)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, UPLOAD_Barcode_Status);

        request.addProperty("BlockCode", blkcode);
        request.addProperty("QRCode", qrcodr);
        BarcodeEntity userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BarcodeEntity.Barcode_CLASS.getSimpleName(), BarcodeEntity.Barcode_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + UPLOAD_Barcode_Status, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BarcodeEntity(res1);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userDetails;

    }



 public static BarcodeEntity UploadSerialBarcodeResponse(String uniqNo, String serialNo, String qrCode)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, ValidateDiseBillPdfSerial);

        request.addProperty("UniqueNo", uniqNo);
        request.addProperty("Serial", serialNo);
        request.addProperty("QRCode", qrCode);
        BarcodeEntity userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BarcodeEntity.Barcode_CLASS.getSimpleName(), BarcodeEntity.Barcode_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + ValidateDiseBillPdfSerial, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BarcodeEntity(res1,"1");

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userDetails;

    }
 public static searchuniqModel DataAsPerUniqNo(String uniqNo)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, ValidateDiseBillPdfSerial);

        request.addProperty("UniqueNo", uniqNo);

        searchuniqModel searchUniqueNoManual;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BarcodeEntity.Barcode_CLASS.getSimpleName(), BarcodeEntity.Barcode_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + ValidateDiseBillPdfSerial, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            searchUniqueNoManual = new searchuniqModel(res1);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return searchUniqueNoManual;

    }





    public static BarcodeEntity UploadFinalData(BarcodeEntity data,String uid,String uniqno)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_Final_details);

        request.addProperty("UserId",uid);
       // request.addProperty("UniqueNo", data.getUniqueNo());
        request.addProperty("UniqueNo", uniqno);
        request.addProperty("Status","Y");
        request.addProperty("Remarks","");
        BarcodeEntity userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BarcodeEntity.Barcode_CLASS.getSimpleName(), BarcodeEntity.Barcode_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + INSERT_Final_details, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BarcodeEntity(res1,"2");

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userDetails;

    }


    public static BarcodeEntity RejectFinalData(String uniqno,String uid,String reason)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_Final_details);

        request.addProperty("UserId",uid);
        request.addProperty("UniqueNo", uniqno);
        request.addProperty("Status","N");
        request.addProperty("Remarks",reason);
        BarcodeEntity userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, BarcodeEntity.Barcode_CLASS.getSimpleName(), BarcodeEntity.Barcode_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL);
            androidHttpTransport.call(SERVICENAMESPACE + INSERT_Final_details, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BarcodeEntity(res1,"2");

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userDetails;

    }

}


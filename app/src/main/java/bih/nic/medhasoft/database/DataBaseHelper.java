package bih.nic.medhasoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.entity.ACHOLDERTYPE;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.Get_usersalldetails;
import bih.nic.medhasoft.entity.SENDTOPFMSENTITY;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.UserDetails;
import bih.nic.medhasoft.entity.categoryHINDI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    // The Android's default system path of your application database.
    private static String DB_PATH = "";// "/data/data/com.bih.nic.app.biharmunicipalcorporation/databases/";

    //private static String DB_NAME = "LOCDB_org";

    private static String DB_NAME = "LOCDB";


    private SQLiteDatabase myDataBase;

    private final Context myContext;

    String TAG="DataBaseHelper";
    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 3);
        Log.e("DataBaseHelper", "1");
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {

            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            //CreateNewTables(db);

        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();


            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            //this.getReadableDatabase();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS
                            | SQLiteDatabase.OPEN_READWRITE);


        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;

    }

    public boolean databaseExist() {


        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        this.getReadableDatabase().close();

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public void openDataBase() throws SQLException {

        // Open the database
        this.getReadableDatabase();
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }



    public long deleteBenIDForAttendanceSubmittions(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        long c = db.delete("StudentListForAttendance", "id = " + id, null);
        if (c > 0) return c;
        else return 0;
    }

    public long UpdateForAttendanceSubmittions(String id) {

        // IsAttendanceUpdated
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long c=0;
        try {

            values.put("IsAttendanceUpdated", "N");//admission num
            String[] param = {id};
            c = db.update("StudentListForAttendance", values, "BenID = ?", param);

            this.getWritableDatabase().close();
            db.close();
            if (c > 0) return c;
            else return 0;
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();

            if(db!=null)
            {
                db.close();
            }
            Log.e("log_tag", "Error UpdateForAttendanceSubmittions() ");
        }
        finally {
            if (db != null) {
                safeCloseDB(db);
            }
        }
        return c;
    }
    public long setAttendenceLocal(ArrayList<studentList> result) {
        long c = -1;
        ArrayList<studentList> info = result;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("StudentListForAttendance", null, null);
        ContentValues values = new ContentValues();

        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("a_ID", info.get(i).getaID());
                    values.put("StdName", info.get(i).getStdname());
                    values.put("StdNameHn", info.get(i).getStdnamehn());
                    values.put("StdClass", info.get(i).getStdclass());
                    values.put("StdClassHn", info.get(i).getStdclasshn());
                    values.put("StdClassID", info.get(i).getStdclassid());
                    values.put("StdMobNum", info.get(i).getStdmobile());
                    values.put("StdDoB", info.get(i).getStdDOB());

                    values.put("StdSession", info.get(i).getStdsession());
                    values.put("StdSessionHn", info.get(i).getStdsessionhn());
                    values.put("StdSectionID", info.get(i).getStdsessionid());

                    values.put("BenID", info.get(i).getStdbenid());
                    values.put("StdRegNum", info.get(i).getStdadmnum());//admission num
                    values.put("Addmissiondate", info.get(i).getStdadmdate());

                    values.put("StdFName", info.get(i).getStdfname());
                    values.put("StdFNameHn", info.get(i).getStdfnamehn());
                    values.put("StdMName", info.get(i).getStdmname());
                    values.put("StdMNameHn", info.get(i).getStdmnamehn());

                    values.put("StdCategoryID", info.get(i).getStdcatid());
                    values.put("StdCategoryName", info.get(i).getStdcatname());
                    values.put("StdCategoryNameHn", info.get(i).getStdcatnamehn());


                    values.put("FYearID", info.get(i).getStdfyearid());
                    values.put("FYearName", info.get(i).getStdfyearval());
                    values.put("FYearNameHn", info.get(i).getStdfyearvalhn());

                    values.put("StdGenderID", info.get(i).getStdgenid());
                    values.put("StdGender", info.get(i).getStdgenname());
                    values.put("StdGenderHn", info.get(i).getStdgennamehn());

                    values.put("IsMinority", info.get(i).getStdisminority());
                    values.put("IsHandicapped", info.get(i).getStdishandicaped());
                    values.put("AadharCardNo", info.get(i).getStdaadharcardno());
                    values.put("AadharCardName", info.get(i).getStdaadharcardname());

                    values.put("EmailId", info.get(i).getStdemailid());
                    values.put("IsBPL", info.get(i).getStdisbpl());
                    values.put("IFSC", info.get(i).getStdifsc());
                    values.put("AccountNo", info.get(i).getStdacnum());

                    values.put("AccountHolderName", info.get(i).getStdacholdername());
                    values.put("AccountHolderType_Id", info.get(i).getStdacholdertypeid());
                    values.put("AccountHolderType_Name", info.get(i).getStdacholdertypename());
                    values.put("AccountHolderType_NameHn", info.get(i).getStdacholdertypenamehn());

                    values.put("IsAadhaarVerified", info.get(i).getStdisaadhaarverified());
                    values.put("IsForwarded", info.get(i).getStdisforwarded());
                    values.put("IsVerified", info.get(i).getStdisverified());
                    values.put("BenificiaryStatus", info.get(i).getStdstatus());
                    //  values.put("BenificiaryStatus", "0");
                    values.put("isreverified", info.get(i).getStdisreverified());
                    values.put("pfms_BenNameAsPerBank", info.get(i).getStdpfmsbennameasbank());
                    //values.put("pfms_BenNameAsPerBank", info.get(i).get_eupi_BenNameasPerBank());
                    values.put("PFMS_Status", info.get(i).getStdpfmsstatus());
                    values.put("AttSeventyFivePercent", info.get(i).getStdattseventyfiveper());
                    values.put("IsApprovedAndLocked", info.get(i).getStdisapprovedandlocked());

                    values.put("WardVillage", info.get(i).getWardVillage());

                    values.put("AttendancePer", info.get(i).getAttendancePer());
                    values.put("AttendancePerBy", info.get(i).getAttendancePerBy());
                    values.put("AttendancePerDate", info.get(i).getAttendancePerDate());
                    values.put("CUBy", info.get(i).getCUBy());
                    values.put("CUDate", info.get(i).getCUDate());
                    values.put("CUIP", info.get(i).getCUIP());
                    values.put("UpdateBy", info.get(i).getUpdateBy());
                    values.put("UpdateIP", info.get(i).getUpdateIP());
                    values.put("IsOfflineEntry", info.get(i).getIsOfflineEntry());
                    values.put("IsDeleted", info.get(i).getIsDeleted());
                    values.put("DeleteDate", info.get(i).getDeleteDate());
                    values.put("DeletedBy", info.get(i).getDeletedBy());


                    values.put("DeletedReason", info.get(i).getDeletedReason());
                    values.put("DeletedRemark", info.get(i).getDeletedRemark());
                    values.put("IsActive", info.get(i).getIsActive());
                    values.put("ActiveDate", info.get(i).getActiveDate());
                    values.put("MAppVersion", info.get(i).getMAppVersion());
                    values.put("StopBeneficiary", info.get(i).getStopBeneficiary());
                    values.put("OptVerify", info.get(i).getOptVerify());
                    values.put("StopBenDate", info.get(i).getStopBenDate());
                    values.put("StopBenBy", info.get(i).getStopBenBy());
                    values.put("StopBenReason", info.get(i).getStopBenReason());
                    values.put("UpdateDate", info.get(i).getUpdateDate());
                    values.put("BankCode", info.get(i).getBankCode());

                    values.put("AccMap", info.get(i).getAccMap());
                    values.put("tempFlag", info.get(i).getTempFlag());
                    values.put("BenificiaryStatus1", info.get(i).getBenificiaryStatus1());
                    values.put("AccVerXmlFileName", info.get(i).getAccVerXmlFileName());
                    values.put("eupiStatus", info.get(i).getEupiStatus());
                    //  values.put("eupiStatus", "RJCT");
                    values.put("eupiReasonCode", info.get(i).getEupiReasonCode());

                    values.put("DistrictCode", info.get(i).getDistrictCode());
                    values.put("BlockCode", info.get(i).getBlockCode());
                    values.put("AreaType", info.get(i).getAreaType());
                    values.put("WardCode", info.get(i).getWardCode());
                    values.put("Age", info.get(i).getAge());

                    values.put("IsRecordUpdated", "N");
                    values.put("IsAttendanceUpdated", "N");

//                    values.put("eupi_CpsmsId", info.get(i).get_eupi_CpsmsId());
//                    values.put("eupi_BenNameasPerBank", info.get(i).get_eupi_BenNameasPerBank());
//                    values.put("eupi_IFSC", info.get(i).get_eupi_IFSC());
//                    values.put("eupi_AccountNo", info.get(i).get_eupi_AccountNo());
//                    values.put("eupi_BankName", info.get(i).get_eupi_BankName());
//
//                    values.put("eupi_Address1", info.get(i).get_eupi_Address1());
//                    values.put("eupi_Address2", info.get(i).get_eupi_Address2());
//                    values.put("eupi_Address3", info.get(i).get_eupi_Address3());
//                    values.put("eupi_Mobile", info.get(i).get_eupi_Mobile());
//                    values.put("eupi_ResponseFileName", info.get(i).get_eupi_ResponseFileName());
//
//                    values.put("eupi_ResponseReadDate", info.get(i).get_eupi_ResponseReadDate());
                    values.put("maxscore", info.get(i).get_maxscore());
                    // values.put("maxscore", "48");
                    //values.put("Isattendance75", info.get(i).getStdateendanceless());
                    values.put("markedDate", info.get(i).getWardCode());
                    values.put("eupi_BenNameasPerBank", info.get(i).get_eupi_BenNameasPerBank());
                    values.put("eupi_reject_reason", info.get(i).get_eupi_Reason());

//                    if(info.get(i).get_ManualMatch().equalsIgnoreCase("anyType{}")){
//                        values.put("ManualMatch",null);
//                    }

                    values.put("ManualMatch", info.get(i).get_ManualMatch());

                    c = db.insert("StudentListForAttendance", null, values);

                    if (c > 0) {
                        Log.e("Data", "Inserted");
                    } else {
                        Log.e("Data", "Not Inserted");
                    }
                }
                this.getWritableDatabase().close();
                db.close();


            }
            catch (Exception e) {
                e.printStackTrace();

                if(db!=null)
                {
                    db.close();
                }
                return c;
            }
            finally {
                if(db!=null)
                {
                    safeCloseDB(db);
                }
            }
        }
        return c;


    }
    public long setClassList(ArrayList<CLASSLIST> result) {
        long c = -1;
        ArrayList<CLASSLIST> info = result;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ClassList", null, null);
        ContentValues values = new ContentValues();

        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("ClassID", info.get(i).getClassID());
                    values.put("ClassName", info.get(i).getClassName());
                    values.put("ClassNameHn", info.get(i).getClassNamehn());

                    String[] param = {info.get(i).getClassID()};
                    long update = db.update("ClassList", values, "ClassID = ?", param);

                    if (!(update > 0))
                        c = db.insert("ClassList", null, values);

                    if (c > 0) {
                        Log.e("Data", "Inserted");
                    } else {
                        Log.e("Data", "Not Inserted");
                    }
                }
                this.getWritableDatabase().close();
                db.close();
            }
            catch (Exception e) {

                if(db!=null)
                {
                    db.close();
                }
                return c;
            }
            finally {
                if(db!=null)
                {
                    safeCloseDB(db);
                }
            }
        }
        return c;


    }
    public long setSessionList(ArrayList<SESSIONLIST> result) {
        long c = -1;
        ArrayList<SESSIONLIST> info = result;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("SessionList", null, null);
        ContentValues values = new ContentValues();

        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("SessionID", info.get(i).getSessionID());
                    values.put("SessionName", info.get(i).getSessionName());

                    String[] param = {info.get(i).getSessionID()};
                    long update = db.update("SessionList", values, "SessionID = ?", param);

                    if (!(update > 0))
                        c = db.insert("SessionList", null, values);

                    if (c > 0) {
                        Log.e("Data", "Inserted");
                    } else {
                        Log.e("Data", "Not Inserted");
                    }
                }
                this.getWritableDatabase().close();
                db.close();
            }
            catch (Exception e) {

                if(db!=null)
                {
                    db.close();
                }
                return c;
            }
            finally {
                if(db!=null)
                {
                    safeCloseDB(db);
                }
            }
        }
        return c;
    }
    public String getValueOfID(String sqlQuery) {
        long c = -1;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);

            int x = cursor.getCount();

            Log.e("Count", "" + x);
            Log.e("GETVALUE-Q", "" + sqlQuery);
            while (cursor.moveToNext()) {
                return cursor.getString(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public long setFinancialYearList(ArrayList<FYEAR> result) {
        long c = -1;
        ArrayList<FYEAR> info = result;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("FYear", null, null);
        ContentValues values = new ContentValues();

        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("FYearID", info.get(i).getFYearID());
                    values.put("FYearValue", info.get(i).getFYearValue());
                    values.put("Status", info.get(i).getStatus());

                    String[] param = {info.get(i).getFYearID()};
                    long update = db.update("FYear", values, "FYearID = ?", param);

                    if (!(update > 0))
                        c = db.insert("FYear", null, values);

                    if (c > 0) {
                        Log.e("Data", "FYearInserted");
                    } else {
                        Log.e("Data", "FYearNot Inserted");
                    }
                }
                this.getWritableDatabase().close();
                db.close();
            }
            catch (Exception e) {

                if(db!=null)
                {
                    db.close();
                }
                return c;
            }
            finally {
                if(db!=null)
                {
                    safeCloseDB(db);
                }
            }
        }
        return c;
    }
    public ArrayList<studentList> getStudentListForAttendance(String dc,String cid,String ses,String ismarked,String fyearid) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;

        String whereCondition=getPostWhereConditionForStudentListForAttendance( dc, cid, ses, ismarked, fyearid);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) >=50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP' ORDER BY StdRegNum ASC";


            SQLQ=SQLQ.replace("WHERE   AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            int x = cursor.getCount();

            Log.e("Count",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));
                stdData.set_eupi_BenNameasPerBank((cursor.getString(cursor.getColumnIndex("eupi_BenNameasPerBank"))));
                stdData.set_eupi_AccountNo((cursor.getString(cursor.getColumnIndex("AccountNo"))));
                stdData.setaID((cursor.getString(cursor.getColumnIndex("a_ID"))));
                stdData.setCUBy((cursor.getString(cursor.getColumnIndex("CUBy"))));
                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);
                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));
                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }


                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);
            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }
    public String getPostWhereConditionForStudentListForAttendance(String dc,String cid,String ses,String ismarked,String fyearid) {

        String subWhere=" ";

        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere += "StdClassID='"+cid+"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere += " AND StdSectionID='"+ ses +"'";
        }
        if(!ismarked.equalsIgnoreCase(""))
        {
            subWhere += " AND AttSeventyFivePercent='"+ ismarked  +"'";
        }
        if(!fyearid.equalsIgnoreCase("0"))
        {
            subWhere += " AND FYearID='"+ fyearid  +"'";
        }

        Log.e("SUBQUERY",subWhere);
        return subWhere;
    }
    public ArrayList<studentList> getStudentListForUpdatedAttendance(String dc,String cid,String ses,String ismarked) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase("0"))
            {
                // cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' ORDER BY StdRegNum ASC", null);
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }


            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' AND AttSeventyFivePercent='"+ismarked+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND AttSeventyFivePercent='"+ismarked+"'  ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' AND AttSeventyFivePercent='"+ismarked+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }
            int x = cursor.getCount();



            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));

                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }

    public ArrayList<studentList> getStudentAttendanceRecordForUploading(String dc,String cid,String ses,String ismarked) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }


            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsAttendanceUpdated='Y' AND AttSeventyFivePercent='"+ismarked+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND AttSeventyFivePercent='"+ismarked+"'  ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !ismarked.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsAttendanceUpdated='Y' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' AND AttSeventyFivePercent='"+ismarked+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }
            int x = cursor.getCount();



            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }

        return stdDataEntities;

    }

    public ArrayList<studentList> getUpdatedStudentList(String dc,String cid,String ses) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance  ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where  StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }
            int x = cursor.getCount();



            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));

                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }

    public String getStudentCheckedAttendance(String dc,String cid,String ses,String isAtt75Per) {

        //CLASS ID MUST BE SELECTED
        Cursor cursor=null;
        int x=0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where AttSeventyFivePercent='"+isAtt75Per+"' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && !isAtt75Per.equalsIgnoreCase(""))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where AttSeventyFivePercent='"+isAtt75Per+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            x = cursor.getCount();

            cursor.close();
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return String.valueOf(x);

    }
    public String getStudentCheckedAttendance(String dc,String cid,String ses) {

        //CLASS ID MUST BE SELECTED
        Cursor cursor=null;
        int x=0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") )
            {
                // cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance ORDER BY StdRegNum ASC", null);
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where AttSeventyFivePercent='N'  ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") )
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdClassID='"+cid+"' AND AttSeventyFivePercent='N' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") )
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' AND AttSeventyFivePercent='N' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") )
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where AttSeventyFivePercent='N' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }


            x = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return String.valueOf(x);

    }
    public ArrayList<studentList> getStudentDetaislForClassSectionFYear(String dc,String cid,String ses,String fyear) {

        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        int x=0;
        String whereCondition=getPostWhereConditionForStudentDetaislForClassSectionFYear( dc, cid, ses, fyear);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {

            String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            if(whereCondition.toString().trim().length()<=0) {
                SQLQ = SQLQ.replace("WHERE", " ");
            }

            SQLQ=SQLQ.replace("WHERE  AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            x = cursor.getCount();

            Log.e("Count",""+x);

            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));

                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);

                String catid;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    catid="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    catid="";
                }
                else
                {
                    catid=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatid(catid);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }
    public String getPostWhereConditionForStudentDetaislForClassSectionFYear(String dc,String cid,String ses,String fyear) {

        String subWhere="";

        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere += "StdClassID='"+cid+"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere += " AND StdSectionID='"+ ses +"'";
        }

        if(!fyear.equalsIgnoreCase("0"))
        {
            subWhere += " AND FYearID='"+ fyear  +"'";
        }

        Log.e("SUBQUERY",subWhere);
        return subWhere;
    }

    public ArrayList<studentList> getStudentDetaislAsPerAdditionFilter(String dc,String cid,String ses,String ismarked,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue) {

        //StdCategoryID, IsMinority, StdGenderID, IsBPL, IsHandicapped
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        String preWhereCondition;

        if(ses.equalsIgnoreCase("0"))
        {
            if(ismarked.equalsIgnoreCase("")) {
                preWhereCondition = "StdClassID='" + cid +  "' AND FYearID='" + fyear + "'";

            }
            else {
                preWhereCondition = "StdClassID='" + cid + "' AND FYearID='" + fyear + "' AND AttSeventyFivePercent='" + ismarked + "'";
            }
        }
        else
        {
            if(ismarked.equalsIgnoreCase("")) {
                preWhereCondition = "StdClassID='" + cid + "' AND StdSectionID='" + ses + "' AND FYearID='" + fyear + "'";

            }
            else {
                preWhereCondition = "StdClassID='" + cid + "' AND StdSectionID='" + ses + "' AND FYearID='" + fyear + "' AND AttSeventyFivePercent='" + ismarked + "'";
            }
        }


        int x=0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE "+ preWhereCondition  + getPostWhereCondition(cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue)+" ORDER BY StdRegNum ASC", null);

            Log.e("QUERY:","SELECT * FROM StudentListForAttendance WHERE "+ preWhereCondition  + getPostWhereCondition(cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue)+" ORDER BY StdRegNum ASC");

            x = cursor.getCount();
            Log.e("COUNT:",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }
    public Cursor getCustomSearchResult(String sqlquery,String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String attend) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        int x=0;
        try {

            cursor = sqLiteDatabase.rawQuery(sqlquery+" "+ getAddAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus,attend)+" ORDER BY StdRegNum ASC", null);

            Log.e("QUERY-DBHelper:",sqlquery +" "+ getAddAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus,attend)+" ORDER BY StdRegNum ASC");

            x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {

            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getAdvanceCustomSearchResult() ");
        }
        finally {
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }

    public String getPostWhereCondition(String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue)
    {
        String subWhere="";
        if(!cate.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdCategoryID='"+ cate +"'";
        }
        if(!min.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsMinority='"+ min +"'";
        }
        if(!gen.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdGenderID='"+ gen +"'";
        }
        if(!isbpl.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsBPL='"+ isbpl +"'";
        }
        if(!ishnd.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsHandicapped='"+ ishnd +"'";
        }

        if(lang.equalsIgnoreCase("en")) {
            if (std_name.trim().length()>0) {
                subWhere += " AND StdName LIKE '%" + std_name + "%'";
            }
        }
        if(lang.equalsIgnoreCase("hn")) {
            if (std_name.trim().length()>0) {
                subWhere += " AND StdNameHn LIKE '%" + std_name + "%'";
            }
        }
        if(!nonvalue.equalsIgnoreCase("0"))
        {
            subWhere+=" AND ( "+nonvalue + " IS NULL OR " + nonvalue+" LIKE 'anyType{}' OR " + nonvalue+" LIKE '' )";
        }
        return subWhere;
    }
    public String getAdvancePostWhereCondition(String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus) {
        String subWhere = "";
        //if(benstatus.equalsIgnoreCase("0"))
        //{
        //SELECT * FROM StudentListForAttendance WHERE

        // subWhere+=" 1=1 AND BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50  AND maxscore NOT LIKE 'anyType{}'";
        subWhere+=" 1=1 AND BenificiaryStatus='3' ";
        // subWhere+=" 1=1 AND BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) >=50 AND eupiStatus='ACCP'";

        if(!benstatus.equalsIgnoreCase("0"))
        {
            subWhere+=" AND eupiStatus='"+benstatus+"'";
        }


        // }
        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdClassID='"+ cid +"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdSectionID='"+ ses +"'";
        }

        if(!fyear.equalsIgnoreCase("0"))
        {
            subWhere+=" AND FYearID='"+ fyear +"'";
        }

        //-----------------------

        if(!cate.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdCategoryID='"+ cate +"'";
        }
        if(!min.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsMinority='"+ min +"'";
        }
        if(!gen.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdGenderID='"+ gen +"'";
        }
        if(!isbpl.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsBPL='"+ isbpl +"'";
        }
        if(!ishnd.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsHandicapped='"+ ishnd +"'";
        }
//        if(!atten.equalsIgnoreCase(""))
//        {
//            subWhere+=" AND AttSeventyFivePercent='"+ atten +"'";
//        }
        if(std_name!=null) {
            if (lang.equalsIgnoreCase("en")) {
                if (std_name.trim().length() > 0) {
                    subWhere += " AND StdName LIKE '%" + std_name + "%'";
                }
            }
            if (lang.equalsIgnoreCase("hn")) {
                if (std_name.trim().length() > 0) {
                    subWhere += " AND StdNameHn LIKE '%" + std_name + "%'";
                }
            }
        }
        if(!nonvalue.equalsIgnoreCase("0"))
        {
            subWhere+=" AND ( "+nonvalue + " IS NULL OR " + nonvalue+" LIKE 'anyType{}' OR " + nonvalue+" LIKE '' )";
        }
        return subWhere;
    }



    public String getAddAdvancePostWhereCondition(String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String attn)
    {
        String subWhere=" 1+1 ";
        if(!benstatus.equalsIgnoreCase("0")) {
            //SELECT * FROM StudentListForAttendance WHERE

            subWhere += " AND eupiStatus='"+ benstatus+"'";
        }
        // }
        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdClassID='"+ cid +"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdSectionID='"+ ses +"'";
        }

        if(!fyear.equalsIgnoreCase("0"))
        {
            subWhere+=" AND FYearID='"+ fyear +"'";
        }

        //-----------------------

        if(!cate.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdCategoryID='"+ cate +"'";
        }
        if(!min.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsMinority='"+ min +"'";
        }
        if(!gen.equalsIgnoreCase("0"))
        {
            subWhere+=" AND StdGenderID='"+ gen +"'";
        }
        if(!isbpl.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsBPL='"+ isbpl +"'";
        }
        if(!ishnd.equalsIgnoreCase("0"))
        {
            subWhere+=" AND IsHandicapped='"+ ishnd +"'";
        }

        if(!attn.equalsIgnoreCase(""))
        {
            subWhere+=" AND AttSeventyFivePercent='"+ attn +"'";
        }
//        if(!atten.equalsIgnoreCase(""))
//        {
//            subWhere+=" AND AttSeventyFivePercent='"+ atten +"'";
//        }
        if(lang.equalsIgnoreCase("en")) {
            if (std_name.trim().length()>0) {
                subWhere += " AND StdName LIKE '%" + std_name + "%'";
            }
        }
        if(lang.equalsIgnoreCase("hn")) {
            if (std_name.trim().length()>0) {
                subWhere += " AND StdNameHn LIKE '%" + std_name + "%'";
            }
        }
        if(!nonvalue.equalsIgnoreCase("0"))
        {
            subWhere+=" AND ( "+nonvalue + " IS NULL OR " + nonvalue+" LIKE 'anyType{}' OR " + nonvalue+" LIKE '' )";
        }
        return subWhere;
    }
    public ArrayList<studentList> getUpdatedStudentDetaislForClassSectionFYear(String dc,String cid,String ses,String fyear) {

        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int x=0;

        try {

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsRecordUpdated='Y' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where WHERE IsRecordUpdated='Y' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsRecordUpdated='Y' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsRecordUpdated='Y' AND FYearID='"+fyear+"' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
            }
            if(!ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance WHERE IsRecordUpdated='Y' AND StdSectionID='"+ ses +"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }

            if(ses.equalsIgnoreCase("0") && cid.equalsIgnoreCase("0") && !fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsRecordUpdated='Y' AND FYearID='"+fyear+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            if(ses.equalsIgnoreCase("0") && !cid.equalsIgnoreCase("0") && !fyear.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where IsRecordUpdated='Y' AND StdClassID='"+cid+"' AND FYearID='"+fyear+"' ORDER BY StdRegNum ASC", null);
                //cursor = sqLiteDatabase.rawQuery("select * From StudentListForAttendance Where StdAttendanceLess='n' AND StdClassID='"+cid+"' ORDER BY StdRegNum ASC", null);
            }
            x = cursor.getCount();

            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);
                String catid;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryID"))==null)
                {
                    catid="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryID")).equalsIgnoreCase("anyType{}"))
                {
                    catid="";
                }
                else
                {
                    catid=cursor.getString(cursor.getColumnIndex("StdCategoryID"));
                }
                stdData.setStdcatid(catid);

                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                String genid;
                if(cursor.getString(cursor.getColumnIndex("StdGenderID"))==null)
                {
                    genid="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdGenderID")).equalsIgnoreCase("anyType{}"))
                {
                    genid="";
                }
                else
                {
                    genid=cursor.getString(cursor.getColumnIndex("StdGenderID"));
                }
                stdData.setStdgenid(genid);
                String wvill;
                if(cursor.getString(cursor.getColumnIndex("WardVillage"))==null)
                {
                    wvill="";
                }
                else if(cursor.getString(cursor.getColumnIndex("WardVillage")).equalsIgnoreCase("anyType{}"))
                {
                    wvill="";
                }
                else
                {
                    wvill=cursor.getString(cursor.getColumnIndex("WardVillage"));
                }
                stdData.setWardVillage(wvill);

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getUpdatedStudentDetaislForClassSectionFYear ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }

    public Cursor getStudentIDForAttendanceUploading(String classid) {

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {
            //cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE StdAttendanceLess='y'", null);
            if(classid.equalsIgnoreCase("0"))
            {
                cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'", null);
            }
            else
            {
                cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y' AND StdClassID='"+classid+"'", null);
            }

            int x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
        }
        finally {

            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }

    public Cursor getStudentAttendanceForBenID(String benid) {

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {
            //cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE StdAttendanceLess='y'", null);
            cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y' AND BenID='"+benid+"'", null);

            int x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
        }
        finally {
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }
    public Cursor getUpdatedStudentAttendance() {

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {
            //cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE StdAttendanceLess='y'", null);
            cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'", null);

            int x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
        }
        finally {
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }

    public ArrayList<CLASSLIST> getClassList() {
        ArrayList<CLASSLIST> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM ClassList", null);
        try {
// CREATE TABLE `NONFUNCTIONINGREASIONtbl` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `NONFUNCTIONINGREASION_ID` TEXT, `NONFUNCTIONINGREASION_Name` TEXT )

            int x = cur.getCount();
            while (cur.moveToNext()) {
                CLASSLIST vl = new CLASSLIST();
                vl.setClassID(cur.getString(cur.getColumnIndex("ClassID")));
                vl.setClassName(cur.getString(cur.getColumnIndex("ClassName")));
                vl.setClassNamehn(cur.getString(cur.getColumnIndex("ClassNameHn")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getClassList() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }

    public ArrayList<SESSIONLIST> getSessionList() {
        ArrayList<SESSIONLIST> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM SessionList", null);

        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                SESSIONLIST vl = new SESSIONLIST();
                vl.setSessionID(cur.getString(cur.getColumnIndex("SessionID")));
                vl.setSessionName(cur.getString(cur.getColumnIndex("SessionName")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }

    public ArrayList<FYEAR> getFYearList() {
        ArrayList<FYEAR> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM FYear", null);
        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                FYEAR vl = new FYEAR();
                vl.setFYearID(cur.getString(cur.getColumnIndex("FYearID")));
                vl.setFYearValue(cur.getString(cur.getColumnIndex("FYearValue")));
                vl.setStatus(cur.getString(cur.getColumnIndex("Status")));
                pdetail.add(vl);
            }

            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getFYearList() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }

    public ArrayList<Genderlist> getGenderList() {
        ArrayList<Genderlist> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM GenderHN", null);
        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                Genderlist vl = new Genderlist();
                vl.setGenderID(cur.getString(cur.getColumnIndex("GenderId")));
                vl.setGenderName(cur.getString(cur.getColumnIndex("GenderName")));
                vl.setGenderNameHn(cur.getString(cur.getColumnIndex("GenderNameHn")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getGenderList() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }
    public ArrayList<ACHOLDERTYPE> getACHolderType() {
        ArrayList<ACHOLDERTYPE> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM AccountHolderType", null);
        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                ACHOLDERTYPE vl = new ACHOLDERTYPE();
                vl.setACHolderID(cur.getString(cur.getColumnIndex("id")));
                vl.setACHolderValue(cur.getString(cur.getColumnIndex("HolderName")));
                vl.setACHolderValueHn(cur.getString(cur.getColumnIndex("HolderNameHn")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getACHolderType() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }
    public ArrayList<categoryHINDI> getCategoryHNList() {
        ArrayList<categoryHINDI> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM CategoryHN", null);
        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                categoryHINDI vl = new categoryHINDI();
                vl.setCategoryID(cur.getString(cur.getColumnIndex("CategoryId")));
                vl.setCategoryName(cur.getString(cur.getColumnIndex("CategoryName")));
                vl.setCategoryNameHn(cur.getString(cur.getColumnIndex("CategoryNameHn")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getCategoryHNList() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }
    public long insertAllUserDetails(Get_usersalldetails result) {

        long c = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //  db.delete("UserDetailsNew", null, null);

            ContentValues values = new ContentValues();
            values.put("District_Code", result.get_District_Code());
            values.put("District_Name", result.get_District_Name());

            values.put("Block_Code", result.get_Block_Code());
            values.put("Block_Name", result.get_Block_Name());

            values.put("DISE_CODE", result.get_DISE_CODE());

            values.put("School_Name", result.get_School_Name());
            values.put("School_NameHn", result.get_School_NameHn());

            values.put("otpentereddate", result.get_otpentereddate());
            values.put("otpentered", result.get_otpentered());
            values.put("otpgenerateddate", result.get_otpgenerateddate());
            values.put("otpgenerated", result.get_otpgenerated());
            values.put("otp", result.get_otp());
            values.put("mobileno", result.get_mobileno());

            values.put("IsActive", result.get_IsActive());
            values.put("UserRole", result.get_UserRole());
            values.put("RoleDesc", result.get_RoleDesc());


            //String[] whereArgs = new String[]{CommonPref.getUserDetails(New_Entry.this).getUserID()};
            String[] whereArgs1 = new String[]{result.get_mobileno(),result.get_DISE_CODE()};

            c = db.update("UserDetailsNew", values, "mobileno=? AND DISE_CODE =?", whereArgs1);

            if (!(c > 0)) {

                c = db.insert("UserDetailsNew", null, values);
            }
            this.getWritableDatabase().close();
            db.close();
        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error insertAllUserDetails() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return c;

    }



    public boolean isUserRegistered() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=null;
        try {

            res = db.rawQuery(" SELECT * FROM UserDetailsNew", null);
            //res = db.rawQuery(" SELECT * FROM UserDetailsNew WHERE otpentered='Y'", null);

            int counts = res.getCount();
            res.close();
            db.close();
            this.getReadableDatabase().close();
            if (counts > 0) {
                return true;
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(res!=null)
            {
                res.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error isUserRegistered() ");
        }
        finally {
            if(res!=null)
            {
                safeCloseCursor(res);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }

        return false;
    }
    public Cursor getUserRegisteredDetails() {
        Cursor res=null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            // Cursor res = db.rawQuery(" SELECT * FROM UserDetailsNew", null);
            res = db.rawQuery(" SELECT * FROM UserDetailsNew", null);

            int counts = res.getCount();

            db.close();
            this.getReadableDatabase().close();
            if (counts > 0) {
                return res;
            }
        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getUserRegisteredDetails() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return res;
    }


    public long UpdateStatusofUser(String userid,String Registred) {

        long c = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            ContentValues values = new ContentValues();
            // values.put("mobileno", userid);
            values.put("otpentered", Registred);
            //String[] whereArgs = new String[]{CommonPref.getUserDetails(New_Entry.this).getUserID()};
            String[] whereArgs1 = new String[]{userid};
            c = db.update("UserDetailsNew", values, "mobileno=? ", whereArgs1);

            this.getReadableDatabase().close();
            db.close();

        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error UpdateStatusofUser() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return c;

    }

    public ArrayList<studentList> getAllEntryAtudentList(String dc,String cid,String ses,String fyear) {
        ArrayList<studentList> stdDataEntities=new ArrayList<studentList>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        try {

            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance",null);

            int x = cursor.getCount();

            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }

                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);

            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
//            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities ;
    }
    public ArrayList<studentList> getDataForStudent(String benid){
        ArrayList<studentList> stdDataEntities=new ArrayList<studentList>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        try {

            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE BenID='"+ benid +"'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            int x = cursor.getCount();

            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmnamehn((cursor.getString(cursor.getColumnIndex("StdMNameHn"))));

                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);
                stdData.setWardVillage((cursor.getString(cursor.getColumnIndex("WardVillage"))));
                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                stdData.setStdgenid(cursor.getString(cursor.getColumnIndex("StdGenderID")));
                stdData.setStdcatid(cursor.getString(cursor.getColumnIndex("StdCategoryID")));
                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdData.setStdacholdertypeid(cursor.getString(cursor.getColumnIndex("AccountHolderType_Id")));//AccountHolderType_Id
                stdData.setStdacholdertypename(cursor.getString(cursor.getColumnIndex("AccountHolderType_Name")));//AccountHolderType_Id
                stdData.set_sendPfms_id(cursor.getString(cursor.getColumnIndex("sendPfms_id")));
                stdData.set_sendPfms_status(cursor.getString(cursor.getColumnIndex("sendpfms_status")));
                stdData.set_eupi_Reason(cursor.getString(cursor.getColumnIndex("eupi_reject_reason")));

                stdDataEntities.add(stdData);

            }

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getDataForStudent() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities ;
    }
    public long getStudentCount(){

        long countStd=0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        try {


            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCount() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }
    public long getStudentCountForUploading(){

        long countStd=0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsRecordUpdated='Y'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }
    public long getStudentCountForAttendanceUploading(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            // cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='0'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }



    public long getStudentCountNew(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='0'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }


    public long getTOTALStudentCount(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getTOTALStudentCount() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }
    public long getTOTALRejectdStudentCount(String fy){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            //cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE eupiStatus='RJCT'",null);
            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='RJCT' AND FYearID='"+fy+"'",null);
            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE BenificiaryStatus='3'  AND eupiStatus='RJCT' AND FYearID='"+fy+"'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getTOTALStudentCount() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }

    //public ArrayList<studentList> getAdvanceSearchResult(String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String atten) {
    public ArrayList<studentList> getAdvanceSearchResult(String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus) {

        //StdCategoryID, IsMinority, StdGenderID, IsBPL, IsHandicapped
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;
        String preWhereCondition;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int x=0;
        try {

            //String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus)

            cursor = sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE "+ getAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus)+" ORDER BY StdRegNum ASC", null);

            Log.e("QUERY:","SELECT * FROM StudentListForAttendance WHERE "+ getAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus)+" ORDER BY StdRegNum ASC");

            x = cursor.getCount();
            Log.e("COUNT:",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);

                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;
                //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
                //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }
                stdData.setStdfyearval(fnyear);

                stdDataEntities.add(stdData);

            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getAdvanceSearchResult() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;
    }

    public Cursor getAdvanceCustomSearchResult(String sqlquery,String dc,String cid,String ses,String fyear,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String attend) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor=null;
        int x=0;
        try {

            cursor = sqLiteDatabase.rawQuery(sqlquery+" "+ getAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus)+" ORDER BY StdRegNum ASC", null);

            Log.e("QUERY-DBHelper:",sqlquery +" "+ getAdvancePostWhereCondition(dc,cid,ses,fyear,cate,min,gen,isbpl,ishnd,std_name,lang,nonvalue,benstatus)+" ORDER BY StdRegNum ASC");

            x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {

            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getAdvanceCustomSearchResult() ");
        }
        finally {
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }

    public static void safeCloseIS(InputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                Log.e("log_tag", "Error safeCloseIS()getAdvanceCustomSearchResult" );
            }
        }
    }
    public static void safeCloseOS(OutputStream fos) {
        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("log_tag", "Error safeCloseOS()" );
            }
        }
    }
    public static void safeCloseDB(SQLiteDatabase db) {
        if (db != null) {
            try {
                db.close();
            } catch (SQLiteException e) {
                Log.e("log_tag", "Error safeCloseDB" );
            }
        }
    }
    public static void safeCloseCursor(Cursor cur) {
        if (cur != null) {
            try {
                cur.close();
            } catch (SQLiteException e) {
                Log.e("log_tag", "Error safeCloseCursor" );
            }
        }
    }

    //new entry

    public ArrayList<studentList> getNewEnteredStudent(String cid,String ses) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;

        String whereCondition=getPostWhereConditionForStudentListForMismatch(cid,ses);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //    String SQLQ="SELECT * FROM StudentListForAttendance  ORDER BY StdRegNum ASC";
            //  String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='0' AND CAST(maxscore AS INTEGER) >=50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP' ORDER BY StdRegNum ASC";
            String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='0' ORDER BY StdRegNum ASC";


            SQLQ=SQLQ.replace("WHERE   AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            int x = cursor.getCount();

            Log.e("Count",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));
                stdData.set_eupi_BenNameasPerBank((cursor.getString(cursor.getColumnIndex("eupi_BenNameasPerBank"))));
                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);
                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));
                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }


                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);
            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }

    public ArrayList<studentList> PendingPFmsStudentList(String cid,String ses) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;

        String whereCondition=getPostWhereConditionForStudentListForMismatch(cid,ses);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //    String SQLQ="SELECT * FROM StudentListForAttendance  ORDER BY StdRegNum ASC";
            //  String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='0' AND CAST(maxscore AS INTEGER) >=50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP' ORDER BY StdRegNum ASC";
            String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='1' ORDER BY StdRegNum ASC";


            SQLQ=SQLQ.replace("WHERE   AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            int x = cursor.getCount();

            Log.e("Count",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));
                stdData.set_eupi_BenNameasPerBank((cursor.getString(cursor.getColumnIndex("eupi_BenNameasPerBank"))));
                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);
                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));
                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }


                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);
            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }


    public ArrayList<studentList> MatchedWithBankStudentList(String cid,String ses) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;



        String whereCondition1=getPostWhereConditionForStudentListForMismatch(cid, ses);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //    String SQLQ="SELECT * FROM StudentListForAttendance  ORDER BY StdRegNum ASC";
            String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition1 +" AND BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) >=50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP'  ORDER BY StdRegNum ASC  ";
            //  String SQLQ="SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='1' ORDER BY StdRegNum ASC";


            SQLQ=SQLQ.replace("WHERE   AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            int x = cursor.getCount();

            Log.e("Count",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));
                stdData.set_eupi_BenNameasPerBank((cursor.getString(cursor.getColumnIndex("eupi_BenNameasPerBank"))));
                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);
                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));
                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }


                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);
            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }


    public long updateMismatchStatus(String benid,String aid,String dise,String status) {

        long c = 0;
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("Delete from UserDetail");

            ContentValues values = new ContentValues();

            values.put("a_Id", aid);
            values.put("matchstatus", status);
            values.put("disecode",dise);
            values.put("BenId",benid);
            String[] whereArgs = new String[] {benid};
            c = db.update("MismatchBenStatus", values, "BenId=?", whereArgs);
            if (!(c > 0)) {

                c = db.insert("MismatchBenStatus", null, values);
            }

            // c = db.insert("UserDetail", null, values);

            db.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return c;

    }

    public long getStudentCountForNewEntry(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }



    public long getStudentCountForMatchedByBank(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) >=50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }


    public long getStudentCountPFMSPending(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='1'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }




    public long getStudentCountForNotMatchedByBank(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50 AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP'  AND ManualMatch LIKE 'anyType{}'",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }



    public long getStudentCountMismatch(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM MismatchBenStatus ",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }



    public long getRejectedStudentCount(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long countStd=0;
        Cursor cursor=null;
        try {

            // cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance WHERE IsAttendanceUpdated='Y'",null);
            // cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='RJCT' ",null);
            cursor=sqLiteDatabase.rawQuery("SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3'  AND eupiStatus='RJCT' ",null);
            // Cursor cursor=sqLiteDatabase.rawQuery("select * from StudentListForAttendance where UserId=?"+" AND "+" id=? "+" ORDER BY " + "id " +"ASC",new String[]{userid,keyId});
            countStd = cursor.getCount();

            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e(TAG, "Error getStudentCountForAttendanceUploading() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return countStd ;
    }



    public ArrayList<studentList> getStudentListForMismatchedBen(String dc,String cid,String ses,String ismarked,String fyearid) {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
        Cursor cursor=null;

        String whereCondition=getPostWhereConditionForStudentListForAttendance( dc, cid, ses, ismarked, fyearid);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" ORDER BY StdRegNum ASC";
            //  String SQLQ="SELECT * FROM StudentListForAttendance WHERE "+ whereCondition +" AND  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50  AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP' AND ManualMatch is  null AND ManualMatch LIKE 'anyType{}' ORDER BY StdRegNum ASC";
            // String SQLQ="SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50  AND eupiStatus='ACCP'  AND ManualMatch LIKE '%anyType{}%'";
            String SQLQ="SELECT * FROM StudentListForAttendance WHERE  BenificiaryStatus='3' AND CAST(maxscore AS INTEGER) <50 AND maxscore NOT LIKE 'anyType{}' AND eupiStatus='ACCP'  AND ManualMatch LIKE 'anyType{}'";


            SQLQ=SQLQ.replace("WHERE   AND","WHERE");
            Log.e("Q",SQLQ);

            cursor = sqLiteDatabase.rawQuery(SQLQ, null);

            int x = cursor.getCount();

            Log.e("Count",""+x);
            while (cursor.moveToNext()) {
                studentList stdData = new studentList();
                stdData.setId((cursor.getString(cursor.getColumnIndex("id"))));
                stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
                stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
                stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
                stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
                stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
                stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));
                stdData.set_eupi_BenNameasPerBank((cursor.getString(cursor.getColumnIndex("eupi_BenNameasPerBank"))));
                stdData.set_eupi_AccountNo((cursor.getString(cursor.getColumnIndex("AccountNo"))));
                stdData.setaID((cursor.getString(cursor.getColumnIndex("a_ID"))));
                stdData.setCUBy((cursor.getString(cursor.getColumnIndex("CUBy"))));
                String sec;
                if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
                {
                    sec="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
                {
                    sec="";
                }
                else
                {
                    sec=cursor.getString(cursor.getColumnIndex("StdSession"));
                }
                stdData.setStdsession(sec);
                stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
                /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));
                stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
                //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
                stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
                stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

                String dob;


                if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
                {
                    dob="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
                {
                    dob="";
                }
                else
                {
                    dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
                }

                stdData.setStdDOB(dob);

                String mobnum;
                //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
                if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
                {
                    mobnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
                {
                    mobnum="";
                }
                else
                {
                    mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
                }
                stdData.setStdmobile(mobnum);


                String admdate;
                if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
                {
                    admdate="";
                }
                else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
                {
                    admdate="";
                }
                else
                {
                    admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                }
                stdData.setStdadmdate(admdate);

                stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
                stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

                String aadharno;
                //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
                {
                    aadharno="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
                {
                    aadharno="";
                }
                else
                {
                    aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                }

                stdData.setStdaadharcardno(aadharno);

                stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

                String email;
                //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
                if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
                {
                    email="";
                }
                else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
                {
                    email="";
                }
                else
                {
                    email=cursor.getString(cursor.getColumnIndex("EmailId"));
                }
                stdData.setStdemailid(email);

                stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

                String ifsc;
                //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
                if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
                {
                    ifsc="";
                }
                else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
                {
                    ifsc="";
                }
                else
                {
                    ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
                }
                stdData.setStdifsc(ifsc);

                String acnum;
                //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
                if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
                {
                    acnum="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
                {
                    acnum="";
                }
                else
                {
                    acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
                }
                stdData.setStdacnum(acnum);

                String acname;
                //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
                {
                    acname="";
                }
                else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
                {
                    acname="";
                }
                else
                {
                    acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                }
                stdData.setStdacholdername(acname);


                String cat;
                if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
                {
                    cat="";
                }
                else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
                {
                    cat="";
                }
                else
                {
                    cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                }
                stdData.setStdcatname(cat);


                String fnyear;
                if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
                {
                    fnyear="";
                }
                else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
                {
                    fnyear="";
                }
                else
                {
                    fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
                }


                stdData.setStdfyearval(fnyear);
                stdDataEntities.add(stdData);
            }
            this.getReadableDatabase().close();
            cursor.close();
            sqLiteDatabase.close();

        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cursor!=null)
            {
                cursor.close();
            }
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
            Log.e("log_tag", "Error getUserType() ");
        }
        finally {
            if(cursor!=null)
            {
                safeCloseCursor(cursor);
            }
            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return stdDataEntities;

    }

    public Cursor getMatchStatusOfStudent() {

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try {
            //cursor = sqLiteDatabase.rawQuery("Select * from StudentListForAttendance WHERE StdAttendanceLess='y'", null);

            cursor = sqLiteDatabase.rawQuery("Select * from MismatchBenStatus ", null);


            int x = cursor.getCount();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            if(sqLiteDatabase!=null)
            {
                sqLiteDatabase.close();
            }
        }
        finally {

            if(sqLiteDatabase!=null)
            {
                safeCloseDB(sqLiteDatabase);
            }
        }
        return cursor;
    }
    public boolean deleterow(int aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MismatchBenStatus", "a_Id" + "=" + aid, null) > 0;
    }
    public boolean deleterow1(int aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("StudentListForAttendance", "a_ID" + "=" + aid, null) > 0;
    }



    public ArrayList<SENDTOPFMSENTITY> getPfmsStatus() {
        ArrayList<SENDTOPFMSENTITY> pdetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM SendToPfms", null);
        try {

            int x = cur.getCount();
            while (cur.moveToNext()) {
                SENDTOPFMSENTITY vl = new SENDTOPFMSENTITY();
                vl.setSendToPfms_ID(cur.getString(cur.getColumnIndex("Pfms_id")));
                vl.setSendToPfms_Status(cur.getString(cur.getColumnIndex("Pfms_Status")));
                //   vl.setACHolderValueHn(cur.getString(cur.getColumnIndex("HolderNameHn")));
                pdetail.add(vl);
            }
            this.getReadableDatabase().close();
            cur.close();
            db.close();
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            if(cur!=null)
            {
                cur.close();
            }
            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error getACHolderType() ");
        }
        finally {
            if(cur!=null)
            {
                safeCloseCursor(cur);
            }
            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return pdetail;
    }



    public String getPostWhereConditionForStudentListForMismatch(String cid,String ses) {

        String subWhere=" ";

        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere += "StdClassID='"+cid+"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere += " AND StdSectionID='"+ ses +"'";
        }
//        if(!ismarked.equalsIgnoreCase(""))
//        {
//            subWhere += " AND AttSeventyFivePercent='"+ ismarked  +"'";
//        }
//        if(!fyearid.equalsIgnoreCase("0"))
//        {
//            subWhere += " AND FYearID='"+ fyearid  +"'";
//        }

        Log.e("SUBQUERY",subWhere);
        return subWhere;
    }


    public long insertBlockUserDetails(BlkUserDetails result) {

        long c = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //  db.delete("UserDetailsNew", null, null);

            ContentValues values = new ContentValues();
            values.put("Blk_UserId", result.get_UserID());
            values.put("Blk_UserName", result.get_UserName());

            values.put("DistCode", result.get_DistrictCode());
            values.put("DistName", result.get_DistrictName());

            values.put("BlkCode", result.get_BlockCode());

            values.put("BlkName", result.get_BlockName());
            values.put("UserRole", result.get_UserRole());
            values.put("Password", result.get_Password());




            //String[] whereArgs = new String[]{CommonPref.getUserDetails(New_Entry.this).getUserID()};
            String[] whereArgs1 = new String[]{result.get_UserID()};

            c = db.update("BlockUserDetails", values, "Blk_UserId=?", whereArgs1);

            if (!(c > 0)) {

                c = db.insert("BlockUserDetails", null, values);
            }
            this.getWritableDatabase().close();
            db.close();
        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error insertAllUserDetails() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return c;

    }



    public long insertQR(BarcodeEntity result) {

        long c = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("QRCodeTAble",null,null);

        try {
            //  db.delete("UserDetailsNew", null, null);

            ContentValues values = new ContentValues();
            values.put("blockCode", result.getBlockCode());
            values.put("uniqueCode", result.getUniqueNo());
            values.put("status", result.getStatus());
            values.put("message", result.getMessage());
            values.put("benname", result.getBenName());
            values.put("accno", result.getAccountNo());
            values.put("ifsc", result.getIFSC());
            values.put("totaldata", result.getTotalData());
            values.put("totaldatareprt", result.getTotalDataReprt());
            values.put("totalrem", result.getTotalDataRemaning());
            values.put("totalearlier", result.getTotalDataEarlier());


            String[] whereArgs1 = new String[]{result.getUniqueNo()};

          /*  c = db.update("QRCodeTAble", values, "uniqueCode=?", whereArgs1);

            if (!(c > 0)) {*/

            c = db.insert("QRCodeTAble", null, values);
            //  }
            this.getWritableDatabase().close();
            db.close();
        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error insertAllUserDetails() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return c;

    }

    public long insertSerialQR(String blckcode,String unqNo,String serialNo,String benname,String acc,String ifsc,String status) {

        long c = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete("QRCodeTAble",null,null);

        try {
            //  db.delete("UserDetailsNew", null, null);

            ContentValues values = new ContentValues();
            values.put("blockcode", blckcode);
            values.put("serialNo", serialNo);
            values.put("uniqNo", unqNo);
            values.put("status", status);
            values.put("benname", benname);
            values.put("accno", acc);
            values.put("ifsc", ifsc);



            String[] whereArgs1 = new String[]{serialNo};

            c = db.update("QRcodeSerialNo", values, "serialNo=?", whereArgs1);

            if (!(c > 0)) {

                c = db.insert("QRcodeSerialNo", null, values);
            }
            this.getWritableDatabase().close();
            db.close();
        }
        catch (Exception e) {

            if(db!=null)
            {
                db.close();
            }
            Log.e(TAG, "Error insertAllUserDetails() ");
        }
        finally {

            if(db!=null)
            {
                safeCloseDB(db);
            }
        }
        return c;

    }

    public ArrayList<BarcodeEntity> getQRCODE() {


        ArrayList<BarcodeEntity> typeList = new ArrayList<BarcodeEntity>();

        try {
//CREATE TABLE `Gender` ( `GenderID` TEXT, `GenderName` TEXT )
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cur = db
                    .rawQuery(
                            "SELECT * from QRCodeTAble",
                            null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                BarcodeEntity gen = new BarcodeEntity();
                gen.setBlockCode(cur.getString(cur.getColumnIndex("blockCode")));
                gen.setUniqueNo(cur.getString(cur.getColumnIndex("uniqueCode")));
                gen.setTotalData(cur.getString(cur.getColumnIndex("totaldata")));
                gen.setTotalDataReprt(cur.getString(cur.getColumnIndex("totaldatareprt")));
                gen.setTotalDataRemaning(cur.getString(cur.getColumnIndex("totalrem")));
                gen.setTotalDataEarlier(cur.getString(cur.getColumnIndex("totalearlier")));
                gen.setIFSC(cur.getString(cur.getColumnIndex("ifsc")));
                gen.setBenName(cur.getString(cur.getColumnIndex("accno")));
                gen.setAccountNo(cur.getString(cur.getColumnIndex("uniqueCode")));

                typeList.add(gen);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return typeList;
    }

    public ArrayList<BarcodeEntity> getQRCODESerial() {


        ArrayList<BarcodeEntity> typeList = new ArrayList<BarcodeEntity>();

        try {
//CREATE TABLE `Gender` ( `GenderID` TEXT, `GenderName` TEXT )
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cur = db
                    .rawQuery(
                            "SELECT * from QRcodeSerialNo",
                            null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                BarcodeEntity gen = new BarcodeEntity();
                gen.setUniqueNo(cur.getString(cur.getColumnIndex("uniqNo")));
                gen.setBlockCode(cur.getString(cur
                        .getColumnIndex("blockcode")));
                gen.setSerialNo(cur.getString(cur
                        .getColumnIndex("serialNo")));
                gen.setStatus(cur.getString(cur
                        .getColumnIndex("status")));

                gen.setBenName(cur.getString(cur.getColumnIndex("benname")));
                gen.setAccountNo(cur.getString(cur.getColumnIndex("accno")));
                gen.setIFSC(cur.getString(cur.getColumnIndex("ifsc")));

                typeList.add(gen);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return typeList;
    }

    public long deleteQRCODESERIAL(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        long c = db.delete("QRcodeSerialNo", "uniqNo = " + id, null);
        if (c > 0) return c;
        else return 0;
    }

    public long UpdateQRCODESERIAL(String id) {

        // IsAttendanceUpdated
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long c=0;
        try {

            values.put("status", "Y");//admission num
            String[] param = {id};
            c = db.update("QRcodeSerialNo", values, "serialNo = ?", param);

            this.getWritableDatabase().close();
            db.close();
            if (c > 0) return c;
            else return 0;
        }
        catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();

            if(db!=null)
            {
                db.close();
            }
            Log.e("log_tag", "Error UpdateForAttendanceSubmittions() ");
        }
        finally {
            if (db != null) {
                safeCloseDB(db);
            }
        }
        return c;
    }

    public long deleteQRCODESERIALTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        long c = db.delete("QRCodeTAble",null,null);
        c = db.delete("QRcodeSerialNo",null,null);
        if (c > 0) return c;
        else return 0;
    }


    public int gettotalCount(String uniq) {

        String[] user_id = {uniq};
        String countQuery = "SELECT  * FROM QRcodeSerialNo where uniqNo=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, user_id);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

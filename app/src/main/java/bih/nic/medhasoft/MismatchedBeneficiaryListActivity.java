package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bih.nic.medhasoft.Adapter.MismatchedListAdaptor;
import bih.nic.medhasoft.Adapter.StudentListAdaptor;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

public class MismatchedBeneficiaryListActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog1;
    public static ProgressDialog pd2;
    ShorCutICON ico;
    MismatchedListAdaptor studentListAdaptor;

    ListView list_mismatched_student;
    Button btnSave,btnUpload;
    //ImageView btnSave,btnUpload;
    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();
    ArrayList<bih.nic.medhasoft.Model.studentList> studentArrayList=new ArrayList<>();

    Spinner spClass,spSession,spn_marked;

    public static String TotnnoClasses="";

    DataBaseHelper localDB ;
    SQLiteDatabase db;
    String version="";
    ArrayList<CLASSLIST> ClassList = new ArrayList<>();
    ArrayAdapter<String> ClassListadapter;
    String _varClass_Name="All",_varClass_Id="0";
    String _varClass_NameHn="सभी";

    ArrayList<SESSIONLIST> SessionList = new ArrayList<>();
    ArrayAdapter<String> SessionListadapter;
    String _varSession_Name="None",_varSession_Id="0";
    String _varSession_NameHn="कोई नहीं";
    // String officer_code;
    String _diseCode="0";
    String _mobilenum="0";
    LinearLayout lnBtns;

    String[] MarkedUnmarked = new String[]{
            "-All-",
            "Above 75 % attendance",
            "Below 75 % attendance",
            "Unmarked"
    };
    final List<String> MarkedList = new ArrayList<>(Arrays.asList(MarkedUnmarked));

    String[] MarkedUnmarkedHn = new String[]{
            "-सभी-",
            "75% से अधिक उपस्थिति",
            "75% से नीचे उपस्थिति",
            "चिह्नित नहीं"
    };
    final List<String> MarkedListHn = new ArrayList<>(Arrays.asList(MarkedUnmarkedHn));

    ArrayAdapter<String> spinnerMarkedAdapter;
    String _isMarked="";

    //----------------

    String timeStamp;
    TextView showpdfdata;
    TextView txtPDFHeader;
    WebView showpdfdataWV;
    String _schoolName="",_distBlock="";
    String _filePrefixName="medhasoft";

    TextView txtHeaderName;
    TextView txtSection;
    //TextView txtFyear;
    TextView txtClass,txtMarked,txt_StdClass;
    TextView tvattendence;
    String _lang="en";
    TextView txt_FName;
    TextView txt_MName;
    TextView txt_DoB;
    TextView txt_Name;
    TextView txt_ANum;
    public static final String HINDI_FONT = "FreeSans.ttf";

    String diseCode;
    String _varFYear_Id="0";
    //--------------------------------
    ArrayList<Genderlist> genderlists = new ArrayList<>();
    String _varCategory_Name="all",_varCategory_NameHn="all",_varCategory_Id="0";

    ArrayList<categoryHINDI> catlists = new ArrayList<>();
    ArrayAdapter<String> catListadapter;
    ArrayList<String> minoritytlist;
    ArrayList<String> divyanglist;
    ArrayList<String> havingnonevaluelist;
    ArrayList<String> bpllist;
    String str_minority="0",str_divyang="0",str_bpl="0", str_none_value="0";

    ArrayAdapter<String> GenderListadapter;
    String _varGender_Name="All",_varGender_Id="0";
    String _varGender_NameHn="सभी";
    TextView txtStdCount;

    //-----FILTER------------------
    Spinner spnCategory;
    Spinner spnIsMinority;
    Spinner spnGender;
    Spinner spnIsBPL;
    Spinner spnIsHandicaped;
    Spinner spnHavingNonValue;
    ImageView imgFilter;
    EditText txtStdNameForSearch;
    String std_name;
    TextView txtAdnStdCount;
    boolean flag_loading=false;
    TextView tvmsg;
    TextView txtInfo75More;
    public static ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismatched_beneficiary_list);

        ico=new ShorCutICON(MismatchedBeneficiaryListActivity.this);
        localDB = new DataBaseHelper(this);

        db = localDB.getWritableDatabase();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        TextView textView = new TextView(this);
        textView.setText("Update");
        textView.setTextSize(100);
        tvattendence =(TextView)findViewById(R.id.tvattendence);
        txtStdCount =(TextView)findViewById(R.id.txtStdCount);



        txtStdCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MismatchedBeneficiaryListActivity.this, "Total Student: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();


            }
        });
        txtInfo75More =(TextView)findViewById(R.id.txtInfo75More);
        // tvmsg =(TextView)findViewById(R.id.tvmsg);
        // tvmsg.setVisibility(View.GONE);
        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");


        list_mismatched_student=(ListView)findViewById(R.id.list_mismatchedstudent);
        FloatingActionButton btnupdatestatus=(FloatingActionButton)findViewById(R.id.btnupdatestatus);
//        btnupdatestatus.setCustomSize(400);
//        btnupdatestatus.ad
        BlinkButtonView(btnupdatestatus);
        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");
        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
        if(_varFYear_Id!=null)
        {
            if(_varFYear_Id.equalsIgnoreCase(""))
            {
                _varFYear_Id="0";
            }
        }
        else {
            _varFYear_Id="0";
        }

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }


        _diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        _mobilenum= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");

        searchStdList();

        btnupdatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
                long isfound = helper.getStudentCountMismatch();
                if(isfound<=0)
                {

                    noRecordFoundToUploadItOnServer();

                }
                else {
                    UploadAttendanceRecord();
                }



            }
        });

    }

    public void searchStdList()
    {
        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
        studentList = helper.getStudentListForMismatchedBen(_diseCode,_varClass_Id,_varSession_Id,_isMarked,_varFYear_Id);
        studentArrayList=studentList;
        Parcelable state = list_mismatched_student.onSaveInstanceState();
        if(studentList.size()>0)
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new MismatchedListAdaptor(MismatchedBeneficiaryListActivity.this, studentList,TotnnoClasses);
            list_mismatched_student.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            list_mismatched_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            if(!_varClass_Name.equalsIgnoreCase("")) {
                //  Toast.makeText(this, studentList.size() + " records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new MismatchedListAdaptor(MismatchedBeneficiaryListActivity.this, studentList,TotnnoClasses);
            list_mismatched_student.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            list_mismatched_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            if(!_varClass_Name.equalsIgnoreCase("")) {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(this, "Sorry! no records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "माफ़ कीजिये! कक्षा " + _varClass_NameHn +", सेक्शन " + _varSession_NameHn + " के लिए कोई रिकॉर्ड नहीं मिला " , Toast.LENGTH_SHORT).show();
                }
            }
        }

        list_mismatched_student.onRestoreInstanceState(state);
    }


    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void UploadAttendanceRecord() {

        if (Utiilties.isOnline(MismatchedBeneficiaryListActivity.this)) {

            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            long isfound = helper.getStudentCountForNotMatchedByBank();
            if(isfound<=0)
            {

                noRecordFoundToUploadItOnServer();

            }
            else
            {
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        MismatchedBeneficiaryListActivity.this);


                ab.setTitle("Upload Records");
                ab.setMessage("Are you sure ? Want to upload records to the server ?");
                ab.setNegativeButton("[ NO ]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                ab.setPositiveButton(
                        "[ YES ]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String newFormat = "yyyy-MM-dd";  ////"dd-MM-yyyy";

                                String vardatetime = Utiilties.getDateString(newFormat);
                                Log.e("vardatetime", vardatetime);
                                vardatetime = vardatetime.replace("-", "");
                                Log.e("vardatetimeN", vardatetime);
                                Cursor cursor = localDB.getMatchStatusOfStudent();
                                Toast.makeText(MismatchedBeneficiaryListActivity.this, cursor.getCount() + " records uploading...", Toast.LENGTH_SHORT).show();
                                while (cursor.moveToNext()) {
                                    String benid = cursor.getString(cursor.getColumnIndex("BenId"));
                                    String uby = diseCode;
                                    // String uby = cursor.getString(cursor.getColumnIndex("id"));
                                    //String udate = cursor.getString(cursor.getColumnIndex("CreatedDate"));
                                    String dcode = diseCode;
                                    // String bid = cursor.getString(cursor.getColumnIndex("BenID"));
                                    String aid = cursor.getString(cursor.getColumnIndex("a_Id"));
                                    String status = cursor.getString(cursor.getColumnIndex("matchstatus"));
                                    //profressBar1.setVisibility(View.VISIBLE);
                                    //  new UploadAttendance(id, uby, udate, dcode, bid, marked).execute();
                                    new UploadMatchStatus(benid,dcode,aid,status).execute();
                                    // new UploadAttendanceOLD(id,uby,udate,dcode,bid).execute();
                                }
                            }
                        });

                ab.show();


            }
        }
        else {

            final AlertDialog alertDialog = new AlertDialog.Builder(MismatchedBeneficiaryListActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.no_internet_title));
            alertDialog.setMessage(getResources().getString(R.string.no_internet_msg));
            alertDialog.setButton(getResources().getString(R.string.turnon_now), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    alertDialog.cancel();
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });

            alertDialog.show();
            //Toast.makeText(AttendanceListActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void noRecordFoundToUploadItOnServer()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(MismatchedBeneficiaryListActivity.this);
        ab.setIcon(R.drawable.infosml);

        ab.setTitle("NO RECORDS");
        ab.setMessage("Sorry! no records found for uploading to server.");
        ab.setPositiveButton("[ OK ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                });

        ab.show();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {

            return model.toUpperCase();
        } else {

            return manufacturer.toUpperCase() + " " + model;
        }
    }
    public String getAppVersion(){
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    private class UploadMatchStatus extends AsyncTask<String, Void, String> {

        String _benid,_dcode,_aid,_status;

        private final ProgressDialog progressDialog = new ProgressDialog(MismatchedBeneficiaryListActivity.this);


        UploadMatchStatus(String benid,String dcode,String aid,String status)
        {
            this._benid=benid;
            this._dcode=dcode;
            this._aid=aid;
            this._status=status;
        }
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.show();

            progressDialog.setMessage("Uploading attendance...");

        }

        @Override
        protected String doInBackground(String... param) {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            boolean isTablet=isTablet(MismatchedBeneficiaryListActivity.this);
            if(isTablet) {
                devicename="Tablet::"+devicename;
                Log.e("DEVICE_TYPE", "Tablet");
            } else {
                devicename="Mobile::"+devicename;
                Log.e("DEVICE_TYPE", "Mobile");
            }
            String res= WebServiceHelper.UploadMatchStatusa(MismatchedBeneficiaryListActivity.this,_dcode,_aid,_status,devicename,app_version);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //profressBar1.setVisibility(View.GONE);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
            }
            catch(Exception e)
            {

            }

            try
            {
                if(result!=null)
                {


                    if(result.equalsIgnoreCase("Updated"))
                    {

                        int PID = Integer.parseInt(_aid);
                        boolean isDel = localDB.deleterow(PID);
                        if (isDel=true){
                            boolean isDel1 = localDB.deleterow1(PID);
                            if (isDel1=true){
                                //searchStdList();

                                SQLiteDatabase db = localDB.getWritableDatabase();
                                String count = "SELECT count(*) FROM MismatchBenStatus";
                                Cursor mcursor = db.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);
                                if(icount>0){

                                }
                                else {
                                    new LoadAttendance1(diseCode).execute();
                                }

                            }
                        }

                        Toast.makeText(getApplicationContext(), " Status updated for a_ID " + _aid+" successfully", Toast.LENGTH_SHORT).show();

                        // removeShortcut();
                        // ContentValues values = new ContentValues();
                        //values.put("IsAttendanceUpdated","N");
                        //  String[] whereArgsss = new String[]{_bid};
//                        String[] whereArgsss = new String[]{_aid};
//                        SQLiteDatabase db = localDB.getWritableDatabase();
//                        long c = db.update("StudentListForAttendance", values, "a_ID=?", whereArgsss);

                        // resetValueS();
                        // createShortCut();



                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry! failed to upload Attendance for " + _aid+" \nResponse " , Toast.LENGTH_SHORT).show();


                    }
                }
                else
                {

                    Toast.makeText(getApplicationContext(), "Response:NULL, Sorry! failed to upload Attendance for " + _aid, Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception e)
            {

            }
        }
    }


    private class LoadAttendance1 extends AsyncTask<String, Void, ArrayList<studentList>> {

        String _dcode;

        LoadAttendance1(String dcode)
        {
            this._dcode=dcode;
        }
        @Override
        protected void onPreExecute() {
            try {


                progressDialog.setMessage("Please wait...\nDownloading Student's Details");

                progressDialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected ArrayList<studentList> doInBackground(String... param) {

            return WebServiceHelper.GetStudentAttendanceList(_dcode);
        }

        @Override
        protected void onPostExecute(ArrayList<studentList> result) {


            if (result != null) {
                if (result.size() > 0) {

                    Log.d("result.size",""+result.size());
                    final String totalres=""+result.size();
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setAttendenceLocal(result);

                    if(i>0){
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        searchStdList();
                        AlertDialog.Builder ab = new AlertDialog.Builder(MismatchedBeneficiaryListActivity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                        ab.setTitle("Match Status updated");
                       // ab.setMessage(result.size() +" Student Details Downloaded Successfully.");
                        ab.setMessage("Match Status Of Beneficiary Updated Successfully");
                        Dialog dialog = new Dialog(MismatchedBeneficiaryListActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                    txtTStd.setText(totalres);
//                                    txtTotalStudents.setText("Total Student : " + totalres);
                                // resetValueS();


                                dialog.dismiss();

                            }
                        });

                        ab.show();


                    }
                    else{
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        AlertDialog.Builder ab = new AlertDialog.Builder(MismatchedBeneficiaryListActivity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                        ab.setTitle("FAILed");
                       // ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                        ab.setMessage("Match Status Not Updated Successfully");
                        Dialog dialog = new Dialog(MismatchedBeneficiaryListActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                            }
                        });

                        ab.show();


                    }

                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder ab = new AlertDialog.Builder(MismatchedBeneficiaryListActivity.this);
                    ab.setCancelable(false);
                    ab.setIcon(R.drawable.download);

                    ab.setTitle("Result Null");
                   // ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                    Dialog dialog = new Dialog(MismatchedBeneficiaryListActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();

                        }
                    });

                    ab.show();


                }
            }
            else
            {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Toast.makeText(getApplicationContext(), "Response NULL.", Toast.LENGTH_SHORT).show();


            }
            // btnSync.clearAnimation();
            //profressBar1.setVisibility(View.GONE);
        }
    }



    public  void BlinkButtonView(FloatingActionButton btn)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(800); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btn.startAnimation(anim);
    }
}

package bih.nic.medhasoft;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;

public class Main2Activity extends AppCompatActivity {

    Context context;
    public static SharedPreferences prefs;
    @SuppressLint("NewApi")
    ActionBar actionBar;
    @SuppressLint("NewApi")
    DataBaseHelper databaseHelper;
    String diseCode,uotp,version;
    DataBaseHelper localDB ;
    SQLiteDatabase db;
    LinearLayout lin_chossecenter,lin_staff,lin_student,lin_schoolDet,lin_waterfacility,lin_otherDetail,lin_Res_arrangment,lin_logout;
    TextView user_name,user_role,sch_name;
    TextView tv_newCount,tv_Count2,tv_Count3,tv_Count4,tv_Count5,user_dise,sch_instructions;
    DataBaseHelper dataBaseHelper;
    String USERID="",SchId="";
    // UserDetail userDetail,getUserDetail;
    TextView res_status,schdet_status,waterfac_status,otherdet_status,total_stud;
    Cursor curShoolDetails;
    long countstd=0;
    long countattstd=0;
    long countnew=0;
    long countMatched=0;
    long countNotMatchedBank=0;
    long countNotMatchedBank1=0;
    long countRejected=0;
    long countPFMS=0;
    public static ProgressDialog progressDialog;
    ImageView sync,inf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        localDB = new DataBaseHelper(this);
        databaseHelper=new DataBaseHelper(getApplicationContext());
        db = localDB.getWritableDatabase();
        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        uotp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        context=this;


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        FloatingActionButton btnscrolldown=findViewById(R.id.btnscrolldown);
        final ScrollView scroll=findViewById(R.id.scroll);

        Initialization();
        showinstructions();
        CREATETABLEIFNOTEXIST1();
        CREATETABLEIFNOTEXIST2();
        ModifyTable();
        deleteMismatchData();

        db = localDB.getWritableDatabase();
        String count = "SELECT count(*) FROM SendToPfms";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            Log.e("Table SendToPfms", "Row already exists");
        } else {
            INSERTVALUESIFTABLEEXIST();
            INSERTVALUESIFTABLEEXIST1();

        }

        btnscrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
        curShoolDetails=databaseHelper.getUserRegisteredDetails();
        if(curShoolDetails.getCount()>0)
        {
            while(curShoolDetails.moveToNext())
            {

                user_name.setText(curShoolDetails.getString(curShoolDetails.getColumnIndex("School_Name"))+" SCHOOL");
                String ditsblok=curShoolDetails.getString(curShoolDetails.getColumnIndex("District_Name"))+  ", "+curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_Name"));
                user_role.setText(ditsblok);
                user_dise.setText("Dise Code-"+diseCode);


            }
        }
        curShoolDetails.close();




        lin_chossecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Main2Activity.this,NewEntryStudentList.class);
                startActivity(i);
            }
        });

        lin_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Activity.this,MachtedWithBankStudentList.class);
                startActivity(i);
            }
        });


        lin_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Main2Activity.this,PendingPfmsStudentList.class);
                startActivity(i);
                // finish();
            }
        });



        lin_schoolDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 countNotMatchedBank1=localDB.getStudentCountForNotMatchedByBank();
                if (Utiilties.isOnline(Main2Activity.this)) {
                    if (countNotMatchedBank1 == 0) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.infosml);

                        ab.setTitle("NO RECORDS");
                        ab.setMessage("Sorry! no mismatch records found.");
                        // ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                        Dialog dialog = new Dialog(Main2Activity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                            }
                        });

                        ab.show();
                    } else {
                        Intent i = new Intent(Main2Activity.this, MismatchedBeneficiaryListActivity.class);
                        startActivity(i);
                    }

                }
                else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                    ab.setIcon(R.drawable.wifi);

                    ab.setTitle(R.string.no_internet_title);
                    ab.setMessage(R.string.no_internet_msg);
                    ab.setPositiveButton(R.string.turnon_now,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    GlobalVariables.isOffline = false;
                                    Intent I = new Intent(
                                            android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(I);
                                }
                            });
                    ab.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    GlobalVariables.isOffline = true;
                                    dialog.dismiss();

                                }
                            });
                    ab.show();
                }
                // finish();
            }

        });

        lin_waterfacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Main2Activity.this,BenDetailsActivity.class);
                startActivity(i);
                // finish();
            }
        });

        lin_otherDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Main2Activity.this,AttendanceActivity.class);
                startActivity(i);
                // finish();
            }
        });
        lin_Res_arrangment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Main2Activity.this,SearchCustomActivity.class);
                startActivity(i);
                // finish();
            }
        });


        sch_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showinstructions();
            }
        });


        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                ab.setIcon(R.drawable.logout1);
                ab.setTitle("LOGOUT");
                ab.setMessage("Are You Sure You Want To Logout?");
                ab.setPositiveButton("[ YES ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {


                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("DISECODE", "").commit();
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("OTP", "").commit();
                                deleteUserdetails();
                                deleteStudentdetails();
                                Intent intent = new Intent(Main2Activity.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                                dialog.dismiss();
                            }
                        });
                ab.setNegativeButton("[ NO ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();


                            }
                        });
                ab.show();

            }
        });

    }



    private void Initialization(){
        lin_chossecenter=(LinearLayout)findViewById(R.id.lin_chossecenter);
        lin_Res_arrangment=(LinearLayout)findViewById(R.id.lin_Res_arrangment);
        lin_staff=(LinearLayout)findViewById(R.id.lin_staff);
        lin_student=(LinearLayout)findViewById(R.id.lin_student);
        lin_schoolDet=(LinearLayout)findViewById(R.id.lin_schoolDet);
        lin_waterfacility=(LinearLayout)findViewById(R.id.lin_waterfacility);
        lin_otherDetail=(LinearLayout)findViewById(R.id.lin_otherDetail);
        lin_logout=(LinearLayout)findViewById(R.id.lin_logout);
        //lin_Res_arrangment=(LinearLayout)findViewById(R.id.lin_Res_arrangment);
        // lin_Upload=(LinearLayout)findViewById(R.id.lin_Upload);
        user_name=(TextView)findViewById(R.id.user_name);
        user_role=(TextView)findViewById(R.id.user_role);
        sch_name=(TextView)findViewById(R.id.sch_name);
        // res_status=(TextView)findViewById(R.id.res_status);
//        schdet_status=(TextView)findViewById(R.id.schdet_status);
//        waterfac_status=(TextView)findViewById(R.id.waterfac_status);
//        otherdet_status=(TextView)findViewById(R.id.otherdet_status);
        total_stud=(TextView)findViewById(R.id.total_stud);
        tv_newCount=(TextView)findViewById(R.id.tv_newCount);
        tv_Count2=(TextView)findViewById(R.id.tv_Count2);
        tv_Count3=(TextView)findViewById(R.id.tv_Count3);
        tv_Count4=(TextView)findViewById(R.id.tv_Count4);
        tv_Count5=(TextView)findViewById(R.id.tv_Count5);
        user_dise=(TextView)findViewById(R.id.user_dise);
        sch_instructions=(TextView)findViewById(R.id.sch_instructions);
        sync=(ImageView) findViewById(R.id.syn);
        inf=(ImageView) findViewById(R.id.inf);

        BlinkImageView(sync);
        BlinkImageView(inf);

    }


    public void onClick_SyncAllStudentDetails(View v)
    {
        resetValueS();
        if (Utiilties.isOnline(Main2Activity.this)) {

            if(countstd==0 && countattstd==0)
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                ab.setIcon(R.mipmap.ic_launcher);

                ab.setTitle("WANT TO SYCN");
                ab.setMessage("Are you sure, want to Synchronize data from server ?\nClick on 'STUDENT LIST' to Synchronize student basic details.\nClick on 'CSF' to Synchronize Class, Section and Academic year.");
                ab.setNegativeButton("[ STUDENT LIST ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                //profressBar1.setVisibility(View.VISIBLE);
                                progressDialog.show();
                                new LoadAttendance(diseCode).execute();

                            }
                        });
                //setPositiveButton
                ab.setNeutralButton("[ CSF ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                progressDialog.show();
                                new LoadClassList().execute();

                            }
                        });
                //setNegativeButton
                ab.setPositiveButton("[ CANCEL ]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ab.show();



            }
            else if(countstd!=0 || countattstd!=0)
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                ab.setIcon(R.drawable.infosml);

                ab.setTitle("WARNING");
                ab.setMessage("You have updated data in your device.\nIf you sync student list it will remove old student data and store new data from server.\nClick on:-\n'STUDENT LIST' to Synchronize student list.\n'CSF' to Synchronize class, section and academic year.");
                ab.setNegativeButton("[ STUDENT LIST ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                progressDialog.show();
                                new LoadAttendance(diseCode).execute();

                            }
                        });
                ab.setNeutralButton("[ CSF ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();
                                if(progressDialog.isShowing())
                                {
                                    progressDialog.dismiss();
                                }
                                progressDialog.show();
                                new LoadClassList().execute();

                            }
                        });
                ab.setPositiveButton("[ CANCEL ]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ab.show();


            }

        }
        else {

            AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
            ab.setIcon(R.drawable.wifi);

            ab.setTitle(R.string.no_internet_title);
            ab.setMessage(R.string.no_internet_msg);
            ab.setPositiveButton(R.string.turnon_now,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            GlobalVariables.isOffline = false;
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            GlobalVariables.isOffline = true;
                            dialog.dismiss();

                        }
                    });
            ab.show();

        }
    }



    private class LoadAttendance extends AsyncTask<String, Void, ArrayList<studentList>> {

        String _dcode;

        LoadAttendance(String dcode)
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
                        AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                        ab.setTitle("DOWNLOAD SUCCESS");
                        ab.setMessage(result.size() +" Student Details Downloaded Successfully.");
                        Dialog dialog = new Dialog(Main2Activity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                    txtTStd.setText(totalres);
//                                    txtTotalStudents.setText("Total Student : " + totalres);
                                resetValueS();
                                dialog.dismiss();

                            }
                        });

                        ab.show();


                    }
                    else{
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                        ab.setTitle("DOWNLOAD FAIL");
                        ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                        Dialog dialog = new Dialog(Main2Activity.this);
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
                    AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
                    ab.setCancelable(false);
                    ab.setIcon(R.drawable.download);

                    ab.setTitle("DOWNLOAD FAIL");
                    ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                    Dialog dialog = new Dialog(Main2Activity.this);
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

    private class LoadClassList extends AsyncTask<String, Void, ArrayList<CLASSLIST>> {

        @Override
        protected void onPreExecute() {
            try {

                progressDialog.setMessage("Please wait...\nDownloading Class List");


            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected ArrayList<CLASSLIST> doInBackground(String... param) {

            return WebServiceHelper.GetClassList();
        }

        @Override
        protected void onPostExecute(ArrayList<CLASSLIST> result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt",""+result);
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setClassList(result);

                    if(i>0){
                        Toast.makeText(Main2Activity.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(Main2Activity.this, "Class List not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    Toast.makeText(getApplicationContext(), "No class list Found", Toast.LENGTH_LONG).show();


                }
            }
            progressDialog.show();
            new Main2Activity.LoadSessionList().execute();
        }
    }

    private class LoadSessionList extends AsyncTask<String, Void, ArrayList<SESSIONLIST>> {

        @Override
        protected void onPreExecute() {
            try {

                progressDialog.setMessage("Please wait...\nDownloading Section List");



            } catch (Exception e) {

            }
        }

        @Override
        protected ArrayList<SESSIONLIST> doInBackground(String... param) {

            return WebServiceHelper.GetSessionList();
        }

        @Override
        protected void onPostExecute(ArrayList<SESSIONLIST> result) {
            if (progressDialog.isShowing()) {

                progressDialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt", "" + result);
                    DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                    long i = helper.setSessionList(result);

                    if (i > 0) {
                        Toast.makeText(Main2Activity.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Main2Activity.this, "not inserted Section data", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    Toast.makeText(getApplicationContext(), "No record Found for Section data", Toast.LENGTH_LONG).show();


                }
            }

            progressDialog.show();
            new Main2Activity.LoadFYearList().execute();

        }
    }

    private class LoadFYearList extends AsyncTask<String, Void, ArrayList<FYEAR>> {

        @Override
        protected void onPreExecute() {
            try {

                progressDialog.setMessage("Please wait...\nDownloading academic Year");


            } catch (Exception e) {

            }
        }

        @Override
        protected ArrayList<FYEAR> doInBackground(String... param) {

            return WebServiceHelper.GetFYearList();
        }

        @Override
        protected void onPostExecute(ArrayList<FYEAR> result) {
            if (progressDialog.isShowing()) {

                progressDialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt", "" + result);
                    DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                    long i = helper.setFinancialYearList(result);

                    if (i > 0) {
                        Toast.makeText(Main2Activity.this, "academic Year Data downloaded...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Main2Activity.this, "not inserted academic year data", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record found for academic year", Toast.LENGTH_LONG).show();



                }
            }

        }
    }


    public void resetValueS()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        countstd=helper.getStudentCountForUploading();


        countattstd=helper.getStudentCountForAttendanceUploading();
        countnew=helper.getStudentCountNew();
        countMatched=helper.getStudentCountForMatchedByBank();
        countNotMatchedBank=helper.getStudentCountForNotMatchedByBank();
        countRejected=helper.getRejectedStudentCount();
        countPFMS=helper.getStudentCountPFMSPending();
//        txtCount3.setText(String.valueOf(countattstd));
//        txtCount4.setText(String.valueOf(countattstd));
        tv_newCount.setText(String.valueOf(countnew));
        tv_Count2.setText(String.valueOf(countMatched));
        tv_Count3.setText(String.valueOf(countPFMS));
        tv_Count4.setText(String.valueOf(countNotMatchedBank));
        tv_Count5.setText(String.valueOf(countRejected));

        long totalstd=helper.getTOTALStudentCount();

        total_stud.setText("Total Student : " + totalstd);

        // txtTStd.setText(""+totalstd);
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetValueS();

        CREATETABLEIFNOTEXIST1();
        deleteMismatchData();


    }


    public  void deleteUserdetails()
    {

//        DataBaseHelper placeData = new DataBaseHelper(mContext);
//        SQLiteDatabase db = placeData.getWritableDatabase();

//        SQLiteHelper placeData = new SQLiteHelper(this);
        SQLiteDatabase db = localDB.getWritableDatabase();

        long c = db.delete("UserDetailsNew", null, null);
        if(c>0)
        {
            Log.e("UserDetailsNew","success");
        }
        else
        {
            Log.e("UserDetailsNew","failed");
        }

    }

    public  void deleteMismatchData()
    {

//        DataBaseHelper placeData = new DataBaseHelper(mContext);
//        SQLiteDatabase db = placeData.getWritableDatabase();

//        SQLiteHelper placeData = new SQLiteHelper(this);
        SQLiteDatabase db = localDB.getWritableDatabase();

        long c = db.delete("MismatchBenStatus", null, null);
        if(c>0)
        {
            Log.e("MismatchBenStatus","success");
        }
        else
        {
            Log.e("MismatchBenStatus","failed");
        }

    }

    public  void deleteStudentdetails()
    {

//        DataBaseHelper placeData = new DataBaseHelper(mContext);
//        SQLiteDatabase db = placeData.getWritableDatabase();

//        SQLiteHelper placeData = new SQLiteHelper(this);
        SQLiteDatabase db = localDB.getWritableDatabase();

        long c = db.delete("StudentListForAttendance", null, null);
        if(c>0)
        {
            Log.e("StudentListAttendance","success");
        }
        else
        {
            Log.e("StudentListAttendance","failed");
        }

    }



    public  void showinstructions()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(Main2Activity.this);
        ab.setIcon(R.drawable.usermanual);

//        String green="If the checkbox is checked and beneficiary name is written in&nbsp" + "<font color='#006400'><b>GREEN</b></font>" + "&nbsp colour , it means attendance is above 75%."+"<Br><Br>";
//
//        String red="If the checkbox is unchecked and beneficiary name is written in&nbsp" + "<font color='#8B0000'><b>RED</b></font>" + "&nbsp colour , it means attendance is below 75%."+"<Br><Br>";
//
//        String black=" If the checkbox is unchecked and beneficiary name is written in&nbsp " + "<font color='#000000'><b>BLACK</b></font>" + "&nbsp colour , it means attendance is unmarked till now.";
//



            ab.setTitle(Html.fromHtml("<font color='#990000'>स्टूडेंट चेक लिस्ट आवश्यक सूचना:-</font>"));
            // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
            ab.setMessage(Html.fromHtml("<font color='#0000FF'>1).पहली बार लॉग इन करने पे स्टूडेंट सूचि Fetch Current Record  को क्लिक कर के sync करे |<BR><BR>2).Matched - विद्यार्थी के द्वारा विधालय में दिए गए बैंक विवरण तथा बैंक (PFMS) से प्राप्त विवरण को मिलान कर लिया गया है एवं दिये गये खाते मे पैसे भेजा जा सकता है |<BR><BR>3). Not Matched - विद्यार्थी के द्वारा विधालय में दिए गए बैंक विवरण तथा बैंक (PFMS) से प्राप्त विवरण में भिन्नता है, अतः इसे विधालय स्तर से सत्यापन जरुरी है। सत्यापन के पहचात विद्यार्थी के विवरण को Update करना अपेक्षित है |<BR><BR>4).Rejected - विद्यार्थी के द्वारा विधालय में दिए गए बैंक विवरण को बैंक (PFMS) के माध्यम से अस्वीकृत कर दिया है। अतः विद्यार्थी का बैंक विवरण पुनः अपेक्षित है।<BR><BR>5).PFMS Pending Response - बैंक (PFMS) को विधार्थी का विवरण सत्यापन के लिए भेजा गया है तथा अभी जानकारी नहीं आयी है। जानकारी आने पर उपलब्ध होगा।<BR><BR>6).New Entry - विद्यार्थी का विवरण अभी बैंक (PFMS) को भेजा नहीं गया है। जल्दी इसे सत्यापन के लिए भेजा जाएगा।<BR><BR>7).All Record Of School - विद्यार्थियों की उपरोक्त सभी प्रकार के रिकॉर्ड या अधायंतन जानकारी की वर्त्तमान स्थिति |</font>"));
            ab.setNegativeButton("[ OK ]",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog,int whichButton)
                {
                    dialog.dismiss();
                }
            });
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);


            ab.show();

    }



    public  void BlinkTextView(TextView txt)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(anim);
    }


    public  void BlinkImageView(ImageView img)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        img.startAnimation(anim);
    }




    public void CREATETABLEIFNOTEXIST1() {

        db = localDB.getWritableDatabase();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS MismatchBenStatus (a_Id TEXT, matchstatus TEXT, disecode TEXT, BenId TEXT)");
            localDB.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "MismatchBenStatus");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CREATE Failed ", "MismatchBenStatus");
        }
    }


    public void CREATETABLEIFNOTEXIST2() {

        db = localDB.getWritableDatabase();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS SendToPfms (Pfms_id TEXT, Pfms_Status TEXT)");
            localDB.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "SendToPfms");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CREATE Failed ", "SendToPfms");
        }
    }


    public void INSERTVALUESIFTABLEEXIST() {

        db = localDB.getWritableDatabase();
        try {
            //  db.execSQL("INSERT or replace INTO CheckList (Chk_Id,Chk_Name) VALUES('1','Yes')");
            db.execSQL("INSERT or REPLACE INTO SendToPfms (Pfms_id,Pfms_Status) VALUES('Y','YES')");

            localDB.getWritableDatabase().close();
            Log.e("INSERT SUCCESS ", "SendToPfms");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("INSERT Failed ", "SendToPfms");
        }
    }

    public void INSERTVALUESIFTABLEEXIST1() {

        db = localDB.getWritableDatabase();
        try {
            //db.execSQL("INSERT or replace INTO CheckList (Chk_Id,Chk_Name) VALUES('2','No')");
            db.execSQL("insert or replace INTO SendToPfms (Pfms_id,Pfms_Status) VALUES('N','NO')");
            localDB.getWritableDatabase().close();
            Log.e("INSERT SUCCESS ", "SendToPfms");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("INSERT Failed ", "SendToPfms");
        }
    }



    public void ModifyTable() {


        if (isColumnExists("StudentListForAttendance", "ManualMatch") == false) {
            AlterTable("StudentListForAttendance", "ManualMatch");
        }
        if (isColumnExists("StudentListForAttendance", "sendPfms_id") == false) {
            AlterTable("StudentListForAttendance", "sendPfms_id");
        }
        if (isColumnExists("StudentListForAttendance", "sendpfms_status") == false) {
            AlterTable("StudentListForAttendance", "sendpfms_status");
        }
        if (isColumnExists("StudentListForAttendance", "eupi_reject_reason") == false) {
            AlterTable("StudentListForAttendance", "eupi_reject_reason");
        }


    }

    public boolean isColumnExists(String table, String column) {
        db = localDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (column.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        localDB.getReadableDatabase().close();
        return false;
    }

    public void AlterTable(String tableName, String columnName) {
        try {
            db = localDB.getWritableDatabase();
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT");
            Log.e("ALTER Done", tableName + "-" + columnName);
            localDB.getWritableDatabase().close();
        } catch (Exception e) {
            Log.e("ALTER Failed", tableName + "-" + columnName);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something on back.
            // Display alert message when back button has been pressed
            backButtonHandler();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void backButtonHandler() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(Main2Activity.this);
        // Setting Dialog Title
        alertDialog.setTitle(R.string.app_name);
        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure Want To EXIT this Application?");
        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.bulb_1);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("[ NO ]", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("[ YES ]", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


}

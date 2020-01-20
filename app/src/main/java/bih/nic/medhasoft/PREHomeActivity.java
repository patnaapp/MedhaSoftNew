package bih.nic.medhasoft;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.StudentListAdaptor;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

public class PREHomeActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    public static SharedPreferences prefs;
    @SuppressLint("NewApi")
    ActionBar actionBar;
    @SuppressLint("NewApi")
    DataBaseHelper databaseHelper;
    String diseCode,uotp,version;
    Button btnAttendance,btnSync,btnSkip,btnExit;
    Button btnHelpNext,btnBenDetails,btnDownload,btnSearch;
    //,btnUpload,btnViewUpdatedAttendance,btnUploadAttendance;
    TextView app_version,txtSchoolName,txtDistBlock;
    public static ProgressDialog progressDialog;
   // ProgressBar profressBar1;
    // Button btnPDFReader;
    DataBaseHelper localDB ;
    SQLiteDatabase db;
    ArrayList<CLASSLIST> ClassList = new ArrayList<>();

    ArrayList<SESSIONLIST> SessionList = new ArrayList<>();

    //    TextView txtCount;
//    TextView txtCount2;
    long countstd=0;
    long countattstd=0;

//    TextView txtCount3;
//    TextView txtCount4;
    TextView txtTotalStudents;
    TextView txtTStd,txthelpinfo;
    TextView txtHeaderName;
    boolean isUp;
    LinearLayout lnmenus;
    LinearLayout lnHelp;
    CheckBox repeatChkBx;
    int nexthelp=0;
    ImageView imgMenus,imgInfoClose;
    boolean _btnShowNextHelpclicked=false;
    Cursor curShoolDetails;
    ShorCutICON ico;
    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    String _varFyear_Name="All",_varFYear_Id="0";
    String _varFyear_NameHn="सभी";
    String want_to_see_again;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    String _lang="en";
    ToggleButton toggle,toggle2;
    RelativeLayout rlMain;

    //https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prehome);
        //logout=(ImageView) findViewById(R.id.img_logout);
        rlMain =  findViewById(R.id.rlMain);

        ico=new ShorCutICON(PREHomeActivity.this);
        btnAttendance = (Button) findViewById(R.id.btnAttendance);
        btnSync = (Button) findViewById(R.id.btnSync);
        app_version = (TextView) findViewById(R.id.app_version);
        txtSchoolName = (TextView) findViewById(R.id.txtSchoolName);
        txtDistBlock = (TextView) findViewById(R.id.txtDistBlock);
        txthelpinfo = (TextView) findViewById(R.id.txtHelpInfo);
        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        localDB = new DataBaseHelper(this);

//        profressBar1=findViewById(R.id.profressBar1);
//        profressBar1.setVisibility(View.GONE);

        imgMenus=findViewById(R.id.imgMenus);
        imgInfoClose=findViewById(R.id.imgInfoClose);
        imgInfoClose.setOnClickListener(this);
        imgInfoClose.bringToFront();


        btnBenDetails=findViewById(R.id.btnBenDetails);
        btnDownload=findViewById(R.id.btnDownload);

        btnSkip=findViewById(R.id.btnSkip);
        btnHelpNext=findViewById(R.id.btnHelpNext);
        btnSearch=findViewById(R.id.btnSearch);


        btnExit=findViewById(R.id.btnExit);


        txtTotalStudents=findViewById(R.id.txtTotalStudents);
        txtTStd=findViewById(R.id.txtTStd);
        lnmenus=findViewById(R.id.lnMenus);
        lnHelp=findViewById(R.id.lnHelp);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        repeatChkBx = findViewById( R.id.chkDontShowAgain );
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("WANTTOSEEAGAIN", "No").commit();
                }
                else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("WANTTOSEEAGAIN", "Yes").commit();
                }

            }
        });

        toggle = (ToggleButton) findViewById(R.id.toggle1);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    _lang="en";
                    setTextMessageOfID(String.valueOf(nexthelp),_lang);
                    setButtonsAndOtherLabels(_lang);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", _lang).commit();
                } else {
                    // The toggle is disabled
                    _lang="hn";
                    setTextMessageOfID(String.valueOf(nexthelp),_lang);
                    setButtonsAndOtherLabels(_lang);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", _lang).commit();
                }
            }
        });



        toggle2 = (ToggleButton) findViewById(R.id.toggle2);


        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    _lang="en";
                    setTextMessageOfID(String.valueOf(nexthelp),_lang);
                    setButtonsAndOtherLabels(_lang);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", _lang).commit();
                } else {
                    // The toggle is disabled
                    _lang="hn";
                    setTextMessageOfID(String.valueOf(nexthelp),_lang);
                    setButtonsAndOtherLabels(_lang);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", _lang).commit();
                }
            }
        });

        db = localDB.getWritableDatabase();
        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        uotp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");

        want_to_see_again= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("WANTTOSEEAGAIN", "");

        want_to_see_again= "No";  // stop showing this box

        if(want_to_see_again!=null)
        {
            if(want_to_see_again.equalsIgnoreCase("No"))
            {
                SkipAndContinue();
            }
            else  if(want_to_see_again.equalsIgnoreCase(""))
            {
                setEnableDisable(false); ///set default value turn on
            }
        }
        else {
            setEnableDisable(false);
        }
        //setEnableDisable(false); ///set default value turn on

        Utiilties.setStatusBarColor(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        context=this;
        databaseHelper=new DataBaseHelper(getApplicationContext());

        try {

            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if(_lang.equalsIgnoreCase("en"))
            {
                app_version.setText("App Version : "  +  version);
            }
            else
            {
                app_version.setText("ऐप भर्सन : "  +  version);
            }

        } catch (PackageManager.NameNotFoundException e) {

        }


        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }
        else
        {
            GlobalVariables.LANG=_lang;
        }




        curShoolDetails=databaseHelper.getUserRegisteredDetails();
        if(curShoolDetails.getCount()>0)
        {
            while(curShoolDetails.moveToNext())
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    txtSchoolName.setText(curShoolDetails.getString(curShoolDetails.getColumnIndex("School_Name"))+" SCHOOL");
                    String ditsblok=curShoolDetails.getString(curShoolDetails.getColumnIndex("District_Name"))+  ", "+curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_Name"));
                    txtDistBlock.setText(ditsblok);
                }
                else
                {
                    txtSchoolName.setText(curShoolDetails.getString(curShoolDetails.getColumnIndex("School_NameHn"))+" SCHOOL");
                 //   String ditsblok=curShoolDetails.getString(curShoolDetails.getColumnIndex("District_NameHn"))+  ", "+curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_NameHn"));
                    String ditsblok=curShoolDetails.getString(curShoolDetails.getColumnIndex("District_Name"))+  ", "+curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_Name"));
                    txtDistBlock.setText(ditsblok);
                }

            }
        }
        curShoolDetails.close();

        if (shouldAskPermissions()) {
            askPermissions();
        }

        loadClassList();

        setTextMessageOfID("0",_lang);

        setButtonsAndOtherLabels(_lang);

        CheckWhetherTheAppIsInstalledOrNot();
    }
    public void loadFYearList(Spinner spfyear)
    {
        localDB = new DataBaseHelper(PREHomeActivity.this);
        FYearList=localDB.getFYearList();
        if(FYearList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }

            FYearListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spfyear.setAdapter(FYearListadapter);

            int setID=0;
            for(int i=0;i<FYearList.size();i++)
            {
                StringList.add(FYearList.get(i).getFYearValue());
                if(_varFYear_Id.equalsIgnoreCase(FYearList.get(i).getFYearID()))
                {
                    setID=(i+1);
                }
                spfyear.setSelection(setID);
            }
        }
    }
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.imgMenus)
        {
            if (isUp) {
                slideDown(lnmenus);

            } else {
                lnmenus.setVisibility(View.VISIBLE);
                slideUp(lnmenus);

            }
            isUp = !isUp;
        }
        if(v.getId()==R.id.imgInfoClose)
        {
            SkipAndContinue();
        }
    }

    public void CheckWhetherTheAppIsInstalledOrNot()
    {
        //https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi
        boolean isAppInstalled = appInstalledOrNot("com.google.android.apps.inputmethod.hindi");
        // boolean isAppInstalled = appInstalledOrNot("com.jetstartgames.chess");

        if(isAppInstalled) {

            Log.i("Installed.","OK");
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
            ab.setIcon(R.drawable.keyboard);
            if(_lang.equalsIgnoreCase("en"))
            {
                ab.setTitle("GOOGLE INDIC KEYBOARD");
                ab.setMessage("This app is not available in your device. If you have other Hindi Typing Keyboard then click on 'CANCEL' or If you want to install it then click on 'INSTALL'?");
                ab.setPositiveButton("[ INSTALL ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                try {
                                    Intent  intent = new Intent(android.content.Intent.ACTION_VIEW);
                                    //Copy App URL from Google Play Store.
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi"));
                                    // intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.jetstartgames.chess"));

                                    startActivity(intent);
                                } catch (android.content.ActivityNotFoundException anfe) {

                                    finish();
                                }

                                dialog.dismiss();
                            }
                        });
                ab.setNegativeButton("[ CANCEL ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();


                            }
                        });
                ab.show();
            }
            else
            {
                ab.setTitle("गूगल इंडिक कीबोर्ड");
                ab.setMessage("यह ऐप आपके डिवाइस में उपलब्ध नहीं है। यदि आपके पास अन्य हिंदी टाइपिंग कीबोर्ड है तो 'रद्द करें' पर क्लिक करें या यदि आप इसे इंस्टॉल करना चाहते हैं तो 'इंस्टॉल' पर क्लिक करें ?");
                ab.setPositiveButton("[ इंस्टॉल ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                try {
                                    Intent  intent = new Intent(android.content.Intent.ACTION_VIEW);
                                    //Copy App URL from Google Play Store.
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi"));
                                    // intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.jetstartgames.chess"));

                                    startActivity(intent);
                                } catch (android.content.ActivityNotFoundException anfe) {

                                    finish();
                                }

                                dialog.dismiss();
                            }
                        });
                ab.setNegativeButton("[ रद्द करें ]",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                            }
                        });
                ab.show();
            }

            Log.i("Installed.","NOT");
        }
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PKG","EXCEPTION");
            e.printStackTrace();
        }

        return false;
    }
    public  void BlinkButtonView(Button btn)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btn.startAnimation(anim);
    }
    public  void BlinkImageView(ImageView img)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        img.startAnimation(anim);
    }
    @Override
    protected void onResume() {
        super.onResume();

        resetValueS();

    }

    public void resetValueS()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        countstd=helper.getStudentCountForUploading();


        countattstd=helper.getStudentCountForAttendanceUploading();
//        txtCount3.setText(String.valueOf(countattstd));
//        txtCount4.setText(String.valueOf(countattstd));

        long totalstd=helper.getTOTALStudentCount();
        if(_lang.equalsIgnoreCase("en"))
        {
            txtTotalStudents.setText("Total Student : " + totalstd);
        }
        else
        {
            txtTotalStudents.setText("कुल छात्र : " + totalstd);
        }
        txtTStd.setText(""+totalstd);
    }
    public void loadClassList()
    {
        localDB = new DataBaseHelper(PREHomeActivity.this);
        ClassList=localDB.getClassList();
        if(ClassList.size()>0 ) {

        }
        else {

            if (Utiilties.isOnline(PREHomeActivity.this)) {
                Toast.makeText(this, "Please wait downloading class", Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                progressDialog.show();
                new LoadClassList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                ab.setMessage("Please Turn on Internet connection.");
                ab.setPositiveButton("Turn On",
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
                ab.setNegativeButton("Cancel",
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
    }
    public void loadSessionList()
    {
        localDB = new DataBaseHelper(PREHomeActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {

        }

        else {

            if (Utiilties.isOnline(PREHomeActivity.this)) {
                Toast.makeText(this, "Please wait downloading sections", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                new LoadSessionList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                ab.setMessage("Please Turn on Internet connection.");
                ab.setPositiveButton("Turn On",
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
                ab.setNegativeButton("Cancel",
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
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onBackPressed(){

        if(lnHelp.getVisibility()==View.VISIBLE)
        {
            lnHelp.setVisibility(View.INVISIBLE);
        }

        android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(PREHomeActivity.this);
        ab.setIcon(R.mipmap.ic_launcher);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle(R.string.app_name);
            ab.setMessage("Are You Sure Want To Exit Application?");
            ab.setNegativeButton("[ NO ]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    if(want_to_see_again!=null)
                    {
                        if(want_to_see_again.equalsIgnoreCase("No"))
                        {
                            lnHelp.setVisibility(View.INVISIBLE);
                        }
                        else  if(want_to_see_again.equalsIgnoreCase(""))
                        {
                            lnHelp.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        lnHelp.setVisibility(View.VISIBLE);
                    }


                    // lnHelp.setVisibility(View.VISIBLE);
                }
            });

            ab.setPositiveButton(
                    "[ YES ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                            finish();
                        }
                    });

            ab.show();
        }
        else
        {
            ab.setTitle(R.string.app_namehn);
            ab.setMessage("क्या आप सुनिश्चित हैं, इस एप्लिकेशन को बंद करना चाहते हैं?");
            ab.setNegativeButton("[ नही ]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    if(want_to_see_again!=null)
                    {
                        if(want_to_see_again.equalsIgnoreCase("No"))
                        {
                            lnHelp.setVisibility(View.INVISIBLE);
                        }
                        else  if(want_to_see_again.equalsIgnoreCase(""))
                        {
                            lnHelp.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        lnHelp.setVisibility(View.VISIBLE);
                    }
                    // lnHelp.setVisibility(View.VISIBLE);
                }
            });

            ab.setPositiveButton(
                    "[ हाॅं ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                            finish();
                        }
                    });

            ab.show();
        }


    }




    public void GoToEditStdBasicDetails()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCount();
        if(countstd>0)
        {
            Intent i = new Intent(PREHomeActivity.this, ViewEditListActivity.class);
            i.putExtra("DISECODE", diseCode);
            i.putExtra("MOBILENUM", uotp);
            i.putExtra("RECORDTYPE", "ALL");
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("RECORDTYPE", "ALL").commit();
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
            ab.setIcon(R.drawable.infosml);
            if(_lang.equalsIgnoreCase("en")) {
                ab.setTitle("NO RECORDS");
                ab.setMessage("Sorry! no records found. Please syncronize data first to download student details from server.");
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
            else
            {
                ab.setTitle("रिकॉर्ड नहीं");
                ab.setMessage("माफ़ कीजिये! कोई रिकॉर्ड नहीं मिला। कृपया सर्वर से छात्र का विवरण डाउनलोड करने के लिए पहले डाटा सिंक्रोनाइज़ करें |");
                ab.setPositiveButton(getResources().getString(R.string.okhn),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        });

                ab.show();
            }
        }
    }


    public void onClick_SyncStudentDetails(View v)
    {
        resetValueS();
        if (Utiilties.isOnline(PREHomeActivity.this)) {

            if(countstd==0 && countattstd==0)
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                ab.setIcon(R.mipmap.ic_launcher);
                if(_lang.equalsIgnoreCase("en")) {
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
                else
                {
                    ab.setTitle("सिंक करना चाहते हैं");
                    ab.setMessage("क्या आप वाकई सर्वर से डेटा को सिंक्रनाइज़ करना चाहते हैं?\n" +
                            "छात्र सूची को सिंक्रनाइज़ करने के लिए 'छात्र सूची' पर क्लिक करें।\n" +
                            "वर्ग, अनुभाग और शैक्षणिक वर्ष को सिंक्रनाइज़ करने के लिए 'सीएसफ' पर क्लिक करें।");
                    ab.setNegativeButton("[ छात्र सूची ]",
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
                    ab.setNeutralButton("[ सीएसफ ]",
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
                    ab.setPositiveButton("[ कैंसिल ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ab.show();
                }

            }
            else if(countstd!=0 || countattstd!=0)
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                ab.setIcon(R.drawable.infosml);
                if(_lang.equalsIgnoreCase("en"))
                {
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
                else
                {
                    ab.setTitle("चेतावनी");
                    ab.setMessage("आपने अपने डिवाइस में डेटा अपडेट किया है।\n" +
                            "यदि आप छात्र सूची को सिंक करते हैं तो यह पुराने छात्र डेटा को हटा देगा और सर्वर से नए डेटा को संग्रहीत करेगा।\n" +
                            "पर क्लिक करें:-\n" +
                            "छात्र सूची को सिंक्रनाइज़ करने के लिए 'छात्र सूची'।\n" +
                            "वर्ग, अनुभाग और शैक्षणिक वर्ष को सिंक्रनाइज़ करने के लिए 'सीएसएफ'।");
                    ab.setPositiveButton("[ छात्र सूची ]",
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
                    ab.setNegativeButton("[ सीएसएफ ]",
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
                    ab.setNeutralButton("[ कैंसिल ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ab.show();
                }
            }

        }
        else {

            AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
            ab.setIcon(R.drawable.wifi);
            if(_lang.equalsIgnoreCase("en")) {
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
            else
            {
                ab.setTitle(R.string.no_internet_titlehn);
                ab.setMessage(R.string.no_internet_msghn);
                ab.setPositiveButton(R.string.turnon_nowhn,
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
                ab.setNegativeButton(R.string.cancehn,
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

                if(_lang.equalsIgnoreCase("en")) {
                    progressDialog.setMessage("Please wait...\nDownloading Student's Details");
                }
                else
                {
                    progressDialog.setMessage("कृपया प्रतीक्षा करें...\n" +
                            "छात्रों का विवरण डाउनलोड हो रहा है");
                }
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
                        AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                            ab.setTitle("DOWNLOAD SUCCESS");
                            ab.setMessage(result.size() +" Student Details Downloaded Successfully.");
                            Dialog dialog = new Dialog(PREHomeActivity.this);
                            dialog.setCanceledOnTouchOutside(false);
                            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    txtTStd.setText(totalres);
                                    txtTotalStudents.setText("Total Student : " + totalres);
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
                        AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.download);

                            ab.setTitle("DOWNLOAD FAIL");
                            ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                            Dialog dialog = new Dialog(PREHomeActivity.this);
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
                    AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
                    ab.setCancelable(false);
                    ab.setIcon(R.drawable.download);
                    if(_lang.equalsIgnoreCase("en")) {
                        ab.setTitle("DOWNLOAD FAIL");
                        ab.setMessage(" No Student Details Downloaded for DISE CODE: " + diseCode);
                        Dialog dialog = new Dialog(PREHomeActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                            }
                        });

                        ab.show();
                    }
                    else
                    {
                        ab.setTitle("डाउनलोड विफल");
                        ab.setMessage( diseCode + " डाइस कोड के लिए कोई छात्र विवरण डाउनलोड नहीं किया गया है: ");
                        Dialog dialog = new Dialog(PREHomeActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        ab.setPositiveButton(R.string.okhn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                            }
                        });

                        ab.show();
                    }
                }
            }
            else
            {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(getApplicationContext(), "Response NULL.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "रिस्पॉन्स नल", Toast.LENGTH_SHORT).show();
                }

            }
            btnSync.clearAnimation();
            //profressBar1.setVisibility(View.GONE);
        }
    }

    private class LoadClassList extends AsyncTask<String, Void, ArrayList<CLASSLIST>> {

        @Override
        protected void onPreExecute() {
            try {
                if(_lang.equalsIgnoreCase("en")) {
                    progressDialog.setMessage("Please wait...\nDownloading Class List");
                }
                else
                {
                    progressDialog.setMessage("कृपया प्रतीक्षा करें...\nवर्ग सूची डाउनलोड हो रहा है");
                }

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
                        Toast.makeText(PREHomeActivity.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(PREHomeActivity.this, "Class List not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(_lang.equalsIgnoreCase("en"))
                    {
                        Toast.makeText(getApplicationContext(), "No class list Found", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "कोई वर्ग सूची नहीं मिला", Toast.LENGTH_LONG).show();
                    }

                }
            }
            progressDialog.show();
            new LoadSessionList().execute();
        }
    }

    private class LoadSessionList extends AsyncTask<String, Void, ArrayList<SESSIONLIST>> {

        @Override
        protected void onPreExecute() {
            try {
                if(_lang.equalsIgnoreCase("en")) {
                    progressDialog.setMessage("Please wait...\nDownloading Section List");
                }
                else
                {

                    progressDialog.setMessage("कृपया प्रतीक्षा करें...\nसेक्शन सूची डाउनलोड हो रहा है");
                }


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
                        Toast.makeText(PREHomeActivity.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(PREHomeActivity.this, "not inserted Section data", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(_lang.equalsIgnoreCase("en"))
                    {
                        Toast.makeText(getApplicationContext(), "No record Found for Section data", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "कोई सेक्शन सूची नहीं मिला", Toast.LENGTH_LONG).show();
                    }

                }
            }

            progressDialog.show();
            new LoadFYearList().execute();

        }
    }

    private class LoadFYearList extends AsyncTask<String, Void, ArrayList<FYEAR>> {

        @Override
        protected void onPreExecute() {
            try {

                if(_lang.equalsIgnoreCase("en")) {
                    progressDialog.setMessage("Please wait...\nDownloading academic Year");
                }
                else
                {
                    progressDialog.setMessage("कृपया प्रतीक्षा करें...\nशैक्षणिक वर्ष सूची डाउनलोड हो रहा है");
                }

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
                        Toast.makeText(PREHomeActivity.this, "academic Year Data downloaded...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(PREHomeActivity.this, "not inserted academic year data", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(_lang.equalsIgnoreCase("en"))
                    {
                        Toast.makeText(getApplicationContext(), "No record found for academic year", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "शैक्षणिक वर्ष सूची नहीं मिला", Toast.LENGTH_LONG).show();
                    }


                }
            }

        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public void onClick_ShowHideMenu(View v)
    {

        if (isUp) {
            slideDown(lnmenus);

        } else {
            slideUp(lnmenus);

        }
        isUp = !isUp;

    }

    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onClick_ShowNextHelp(View v)
    {
        _btnShowNextHelpclicked=true;
        if(btnHelpNext.getText().toString().equalsIgnoreCase("Next") || btnHelpNext.getText().toString().equalsIgnoreCase("आगे")) {
            nexthelp = nexthelp + 1;

            setEnableDisable(false);
            if (nexthelp == 0) {
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();

                btnExit.clearAnimation();
            }
            if (nexthelp == 1) {
                BlinkImageView(imgMenus);
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();
            }
            else if (nexthelp == 2) {
                BlinkButtonView(btnSync);
                imgMenus.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();
            }
            else if (nexthelp == 3) {
                BlinkButtonView(btnBenDetails);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();
            }
            else if (nexthelp == 4) {
                BlinkButtonView(btnAttendance);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();
            }
            else if (nexthelp == 5) {
                BlinkButtonView(btnSearch);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnExit.clearAnimation();
                btnDownload.clearAnimation();
            }
            else if (nexthelp == 6) {
                BlinkButtonView(btnDownload);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();
            }
            else if (nexthelp == 7) {
                BlinkButtonView(btnExit);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnSearch.clearAnimation();
                btnDownload.clearAnimation();
            }
            setTextMessageOfID(String.valueOf(nexthelp),_lang);
            setButtonsAndOtherLabels(_lang);
        }
        else
        {
            SkipAndContinue();
        }

    }

    public void setEnableDisable(boolean truefasle)
    {
        imgMenus.setEnabled(truefasle);
        btnSync.setEnabled(truefasle);
        btnBenDetails.setEnabled(truefasle);
        btnAttendance.setEnabled(truefasle);
        btnDownload.setEnabled(truefasle);
        btnSearch.setEnabled(truefasle);

        btnExit.setEnabled(truefasle);

        imgMenus.setClickable(truefasle);
        imgMenus.setOnClickListener(this);
        btnSync.setClickable(truefasle);
        btnBenDetails.setClickable(truefasle);
        btnAttendance.setClickable(truefasle);
        btnDownload.setClickable(truefasle);

        btnExit.setClickable(truefasle);

    }

    public void setTextMessageOfID(String msgid,String inLang)
    {
        String msg="";
        if(inLang.equalsIgnoreCase("hn"))
        {
            if(msgid.equalsIgnoreCase("0"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("स्किप");
                msg="<h5>मेधा सॉफ्ट मोबाइल ऐप में आपका स्वागत है। <br> विभिन्न <i> मेन्यू/बटन्स  के उपयोग पर एक संक्षिप्त विवरण</i><h5>";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("1"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="आप ऊपरी दाएं कोने पर ड्रॉवर की एक छोटी छवि देख सकते हैं। इस पर क्लिक करने पर मेन्यू स्क्रॉल अप या स्क्रॉल डाउन होगा (मेन्यू एक लंबवत व्यवस्थित बटन जिसे नीचे देख सकते हैं)";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            if(msgid.equalsIgnoreCase("2"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="बटन 'डेटा सिंक्रनाइज़ करें':- यह सर्वर से छात्रों, कक्षा, अनुभाग और शैक्षणिक वर्ष के डेटा को सिंक्रनाइज़ करेगा। </font> <br> <br> <font color = '#FF0000' align = 'center'> [ इंटरनेट कनेक्शन आवश्यक ]";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("3"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="बटन 'लाभार्थी का मूल विवरण':-लाभार्थी के मूल विवरण को देखने, संपादित करने और अपलोड करने के लिए यहां क्लिक करें |";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            if(msgid.equalsIgnoreCase("4"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="बटन 'उपस्थिति दर्ज करें': - यह एक स्क्रीन खोलेगा जहाँ से आप छात्रों की 75% उपस्थिति को चिन्हित कर सकते हैं।";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("5"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="बटन 'रिपोर्ट':- अनुकूलित रिपोर्ट जनरेट करने के लिए 'रिपोर्ट' पर क्लिक करें।";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("6"))
            {
                btnHelpNext.setText("आगे");
                btnSkip.setText("पीछे");
                msg="बटन 'डाउनलोड और सेटअप':- हिंदी टाइपिंग के की टूल्स और पीडीएफ रीडर डाउनलोड करने के लिए, यहां क्लिक करें। ।";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }

            if(msgid.equalsIgnoreCase("7"))
            {
                if(_btnShowNextHelpclicked)
                {
                    btnHelpNext.setText("जारी रखें");
                    btnSkip.setText("पीछे");
                }
                else
                {
                    btnHelpNext.setText("आगे");
                    btnSkip.setText("पीछे");
                }
                msg="बटन 'बाहर जाएं':-  इस ऐप को बंद करने के लिए यहां क्लिक करें ।";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }


        }
        else  if(inLang.equalsIgnoreCase("en"))
        {
            if(msgid.equalsIgnoreCase("0"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Skip");

                msg="<h5>Welcome to Medha Soft Mobile App.<br>A brief description on use of different <i>MENU / BUTTONS</i><h5>";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5><h5>"+msg+"</h5></h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("1"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="You can see a small image of drawer at top right corner. When you tap on it will scroll-up or scroll down the menu(menu is a vertically arranged buttons which is show below)";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            if(msgid.equalsIgnoreCase("2"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="Button 'SYNCRONIZE DATA':- It will syncronize students, class,section and academic year data from server.</font><br><br><font color='#FF0000' align='centre'>[ Internet Connection Required ]";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("3"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="Button 'BENEFICIARY BASIC DETAILS':- To view, edit and upload beneficieary's basic details click here.";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            if(msgid.equalsIgnoreCase("4"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="Button 'MARK ATTENDANCE':- It will open a screen from where you can mark/un-mark attendance of students.";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("5"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="Button 'REPORTS':- Click on 'REPORTS' to generate customize reports.";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }
            else if(msgid.equalsIgnoreCase("6"))
            {
                btnHelpNext.setText("Next");
                btnSkip.setText("Back");
                msg="Button 'DOWNLOAD TOOLS':- To download Hindi Typing key tools and PDF reader, click here.";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }

            if(msgid.equalsIgnoreCase("7"))
            {
                if(_btnShowNextHelpclicked)
                {
                    btnHelpNext.setText("Continue");
                    btnSkip.setText("Back");
                }
                else
                {
                    btnHelpNext.setText("Next");
                    btnSkip.setText("Back");
                }

                msg="Button 'EXIT':- Click here to close this app.";
                msg="<p style=\"text-align:justify\"><font color='#00499B' align='centre'><h5>"+msg+"</h5></font></p>";
            }

        }


        txthelpinfo.setBackgroundColor(Color.TRANSPARENT);
        txthelpinfo.setMovementMethod(LinkMovementMethod.getInstance());
        txthelpinfo.setText(Html.fromHtml(msg),TextView.BufferType.SPANNABLE);
    }
    public void onClick_Skip(View v)
    {
        _btnShowNextHelpclicked=false;
        if(btnSkip.getText().toString().equalsIgnoreCase("Back") || btnSkip.getText().toString().equalsIgnoreCase("पीछे")) {
            nexthelp = nexthelp - 1;

            setEnableDisable(false);
            if (nexthelp == 0) {
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();


                btnExit.clearAnimation();
            }
            if (nexthelp == 1) {
                BlinkImageView(imgMenus);
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();

                btnExit.clearAnimation();

            }
            else if (nexthelp == 2) {
                BlinkButtonView(btnSync);
                imgMenus.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();

                btnExit.clearAnimation();
            }
            else if (nexthelp == 3) {
                BlinkButtonView(btnBenDetails);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnAttendance.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();

                btnExit.clearAnimation();
            }
            else if (nexthelp == 4) {
                BlinkButtonView(btnAttendance);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnDownload.clearAnimation();
                btnSearch.clearAnimation();

                btnExit.clearAnimation();
            }
            else if (nexthelp == 5) {
                BlinkButtonView(btnSearch);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnExit.clearAnimation();
                btnDownload.clearAnimation();

            }
            else if (nexthelp == 6) {
                BlinkButtonView(btnDownload);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnSearch.clearAnimation();
                btnExit.clearAnimation();

            }
            else if (nexthelp == 7) {
                BlinkButtonView(btnExit);
                imgMenus.clearAnimation();
                btnSync.clearAnimation();
                btnBenDetails.clearAnimation();
                btnAttendance.clearAnimation();
                btnSearch.clearAnimation();
                btnDownload.clearAnimation();

            }
            setTextMessageOfID(String.valueOf(nexthelp),_lang);
            setButtonsAndOtherLabels(_lang);
        }
        else
        {
            SkipAndContinue();
        }
    }


    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            btnSync.setText("SYNCRONIZE DATA");
            btnBenDetails.setText("BENEFICIARY BASIC DETAILS");
            btnAttendance.setText("MARK ATTENDANCE");
            btnDownload.setText("DOWNLOAD & SETUP");

            txtHeaderName.setText(R.string.app_name);
            repeatChkBx.setText("Don't show again");

            btnExit.setText("EXIT");
            btnSearch.setText("REPORTS");
            toggle.setChecked(true);
            toggle2.setChecked(true);

            rlMain.setBackground(getResources().getDrawable(R.drawable.medhasoftsplace));
        }
        else
        {
            btnSync.setText("डेटा सिंक्रनाइज़ करें");
            btnBenDetails.setText("लाभार्थी का मूल विवरण");
            btnAttendance.setText("उपस्थिति दर्ज करें");
            btnDownload.setText("डाउनलोड और सेटअप");
            btnSearch.setText("रिपोर्ट");
            txtHeaderName.setText(R.string.app_namehn);
            repeatChkBx.setText("फिर मत दिखाना");

            btnExit.setText("बाहर जाएं");

            toggle.setChecked(false);
            toggle2.setChecked(false);

            rlMain.setBackground(getResources().getDrawable(R.drawable.medhasoftsplacehn));

        }
        try
        {
            long totalstd=databaseHelper.getTOTALStudentCount();
            if(_lang.equalsIgnoreCase("en"))
            {
                txtTotalStudents.setText("Total Student : " + totalstd);
            }
            else
            {
                txtTotalStudents.setText("कुल छात्र : " + totalstd);
            }
        }
        catch (Exception ex)
        {

        }
        try {
            curShoolDetails = databaseHelper.getUserRegisteredDetails();
            if (curShoolDetails.getCount() > 0) {
                while (curShoolDetails.moveToNext()) {
                    if (_lang.equalsIgnoreCase("en")) {
                        txtSchoolName.setText(curShoolDetails.getString(curShoolDetails.getColumnIndex("School_Name")) + " SCHOOL");
                        String ditsblok = curShoolDetails.getString(curShoolDetails.getColumnIndex("District_Name")) + ", " + curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_Name"));
                        txtDistBlock.setText(ditsblok);
                    } else {
                        txtSchoolName.setText(curShoolDetails.getString(curShoolDetails.getColumnIndex("School_NameHn")) + " SCHOOL");
                      //  String ditsblok = curShoolDetails.getString(curShoolDetails.getColumnIndex("District_NameHn")) + ", " + curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_NameHn"));
                        String ditsblok = curShoolDetails.getString(curShoolDetails.getColumnIndex("District_Name")) + ", " + curShoolDetails.getString(curShoolDetails.getColumnIndex("Block_Name"));
                        txtDistBlock.setText(ditsblok);
                    }
                }
            }
            curShoolDetails.close();
        }
        catch (Exception e)
        {
            Toast.makeText(context, "EXCEPTION--curShoolDetails", Toast.LENGTH_SHORT).show();
        }
    }
    public void SkipAndContinue()
    {
        lnHelp.setVisibility(View.GONE);

        imgMenus.clearAnimation();
        btnSync.clearAnimation();
        btnBenDetails.clearAnimation();
        btnAttendance.clearAnimation();
        btnDownload.clearAnimation();
        btnSearch.clearAnimation();

        btnExit.clearAnimation();

        isUp = false;
        lnmenus.setVisibility(View.INVISIBLE);

        setEnableDisable(true);
    }

    public void noRecordFoundToUploadItOnServer()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
        ab.setIcon(R.drawable.infosml);
        if(_lang.equalsIgnoreCase("en")) {
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
        else
        {
            ab.setTitle("रिकॉर्ड नहीं");
            ab.setMessage("माफ़ कीजिये! सर्वर पर अपलोड करने के लिए कोई रिकॉर्ड नहीं मिला।");
            ab.setPositiveButton("[ ओके ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });

            ab.show();
        }
    }


    public void onCLick_ViewBenRecords(View v) {

        // showSelectFYearDialog("EDITBASICDETAILS");
        startActivity(new Intent(PREHomeActivity.this,BenDetailsActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
    public void onCLick_MaintainAttendance(View v) {

        // showSelectFYearDialog("EDITBASICDETAILS");
        startActivity(new Intent(PREHomeActivity.this,AttendanceActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
    public void onCLick_Download(View v) {

        // showSelectFYearDialog("EDITBASICDETAILS");
        startActivity(new Intent(PREHomeActivity.this,DownloadToolsActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

//    public void onCLick_ShowSetUP(View v)
//    {
//        startActivity(new Intent(PREHomeActivity.this,SetUpActivity.class));
//        overridePendingTransition(R.anim.enter, R.anim.exit);
//    }

    public void onCLick_Exit(View v)
    {
        android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(PREHomeActivity.this);
        ab.setIcon(R.mipmap.ic_launcher);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle(R.string.app_name);
            ab.setMessage("Are You Sure Want To EXIT this Application?");
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

                            dialog.dismiss();
                            finish();
                        }
                    });

            ab.show();
        }
        else
        {
            ab.setTitle(R.string.app_namehn);
            ab.setMessage("क्या आप सुनिश्चित हैं, इस एप्लिकेशन को बंद करना चाहते हैं?");
            ab.setNegativeButton("[ नही ]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();

                }
            });

            ab.setPositiveButton(
                    "[ हाॅं ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                            finish();
                        }
                    });

            ab.show();
        }
    }
    public void onCLick_SearchBenRecords(View v)
    {
        // startActivity(new Intent(BenDetailsActivity.this,SearchActivity.class));
        startActivity(new Intent(PREHomeActivity.this,SearchCustomActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    public void onCLick_Logout(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(PREHomeActivity.this);
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
                        Intent intent = new Intent(PREHomeActivity.this, Login.class);
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



}

package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.webviewtopdf.PdfView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bih.nic.medhasoft.Adapter.StudentListAdaptor;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

//======================


public class AttendanceUpdatedListActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog1;
    public static ProgressDialog pd2;

    StudentListAdaptor studentListAdaptor;

    ListView list_student;
    Button btnSave,btnUpload;
    //ImageView btnSave,btnUpload;
    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();

    Spinner spClass,spSession,spn_marked;

    public static String TotnnoClasses="";

    DataBaseHelper localDB ;
    SQLiteDatabase db;

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
    LinearLayout lnfilter;
//    String[] MarkedUnmarked = new String[]{
//            "-All-",
//            "Marked",
//            "Unmarked"
//    };

    String[] MarkedUnmarked = new String[]{
            "-All-",
            "Above 75 % attendance",
            "Below 75 % attendance",
            "Unmarked"
    };
    final List<String> MarkedList = new ArrayList<>(Arrays.asList(MarkedUnmarked));
//    String[] MarkedUnmarkedHn = new String[]{
//            "-सभी-",
//            "चिह्नित",
//            "चिह्नित नहीं"
//    };

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

    String _lang="en";

    TextView txtHeaderName;
    TextView txtSection;
    //TextView txtFyear;
    TextView txtClass,txtMarked;
    TextView tvattendence;
    TextView txt_FName;
    TextView txt_Name;
    TextView txt_ANum;
    public static final String HINDI_FONT = "FreeSans.ttf";
    LinearLayout lnHeading;
    String diseCode;
    ShorCutICON ico;
    ImageView imgFilter;
    TextView txt_MName;
    TextView txt_DoB;
    TextView txtStdCount;
    TextView tvmsg;
    TextView txtInfo75More;
    //----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendancelist);

        ico=new ShorCutICON(AttendanceUpdatedListActivity.this);
        localDB = new DataBaseHelper(this);

        db = localDB.getWritableDatabase();
        tvattendence =(TextView)findViewById(R.id.tvattendence);
        imgFilter =  findViewById(R.id.imgFilter);




        txtStdCount =(TextView)findViewById(R.id.txtStdCount);
        txtInfo75More =(TextView)findViewById(R.id.txtInfo75More);
        tvmsg =(TextView)findViewById(R.id.tvmsg);
        tvmsg.setVisibility(View.VISIBLE);

        txtStdCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(AttendanceUpdatedListActivity.this, "Total Student: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, "कुल छात्र: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });






//        tvattendence.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), EditBendDetailsActivity.class);
//                startActivity(i);
//
//            }
//        });

        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");

        list_student=(ListView)findViewById(R.id.list_student);
        spClass=(Spinner)findViewById(R.id.spn_class);
        spSession=(Spinner)findViewById(R.id.spn_session);
        spn_marked=(Spinner)findViewById(R.id.spn_marked);


        lnHeading= findViewById(R.id.lnHeading);


        btnSave= findViewById(R.id.btnsave);
        lnBtns= findViewById(R.id.lnBtns);
        lnfilter= findViewById(R.id.lnfilter);

        txt_FName = (TextView) findViewById(R.id.txt_FName);
        txt_Name = (TextView) findViewById(R.id.txt_Name);
        txt_ANum = (TextView) findViewById(R.id.txt_ANum);
        txt_MName = (TextView) findViewById(R.id.txt_MName);
        txt_DoB = (TextView) findViewById(R.id.txt_Dob);
        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        txtSection = (TextView) findViewById(R.id.txtSection);
        txtMarked = (TextView) findViewById(R.id.txtMarked);
        txtClass = (TextView) findViewById(R.id.txtClass);


        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }
        btnUpload=findViewById(R.id.btnupload);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);
        _diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        _mobilenum= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");

        showpdfdata=(TextView)findViewById(R.id.showpdfdata); //-------------------------
        txtPDFHeader=(TextView)findViewById(R.id.txtPDFHeader); //-------------------------
        showpdfdataWV=findViewById(R.id.showpdfdataWV); //-------------------------
        // lnBtns.setVisibility(View.GONE);


        list_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view.findViewById(R.id.txtBenID);

                Intent iListPendingUpload = new Intent(AttendanceUpdatedListActivity.this, EditBendDetailsActivity.class);
                iListPendingUpload.putExtra("EDIT", "VIEW");
                iListPendingUpload.putExtra("BENID", tv.getText().toString());
                startActivity(iListPendingUpload);
            }
        });


        list_student.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                      //  Toast.makeText(AttendanceUpdatedListActivity.this, "OnTouch DOWN", Toast.LENGTH_SHORT).show();
                        // lnBtns.setVisibility(View.GONE);

                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                      //  Toast.makeText(AttendanceUpdatedListActivity.this, "OnTouch UP", Toast.LENGTH_SHORT).show();
                        // lnBtns.setVisibility(View.VISIBLE);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

        Cursor cur=helper.getUserRegisteredDetails();
        if(cur.getCount()>0)
        {
            while(cur.moveToNext())
            {
                _schoolName = cur.getString(cur.getColumnIndex("School_Name"))+" SCHOOL";
                String ditsblok=cur.getString(cur.getColumnIndex("District_Name"))+  ", "+cur.getString(cur.getColumnIndex("Block_Name"));
               _distBlock = ditsblok;
            }
        }

        searchStdList();

        spinnerMarkedAdapter=new ArrayAdapter(this,R.layout.dropdownlist,MarkedList);
        spn_marked.setAdapter(spinnerMarkedAdapter);
        spn_marked.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    if(_lang.equalsIgnoreCase("en"))
                    {
                        _isMarked = MarkedList.get(arg2);
//                        if(_isMarked.equalsIgnoreCase("Marked"))
//                        {
//                            _isMarked="1";
//                            searchStdList();
//                        }
//                        else if(_isMarked.equalsIgnoreCase("Unmarked"))
//                        {
//                            _isMarked="0";
//                            searchStdList();
//                        }

                        if(_isMarked.equalsIgnoreCase("Above 75 % attendance"))
                        {
                           // _isMarked="0";
                            _isMarked="Y";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("Below 75 % attendance"))
                        {
                            //_isMarked="1";
                            _isMarked="N";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("Unmarked"))
                        {
                            //_isMarked="1";
                            _isMarked="anyType{}";
                            searchStdList();
                        }
                    }
                    else
                    {
                        _isMarked = MarkedListHn.get(arg2);
//                        if(_isMarked.equalsIgnoreCase("चिह्नित"))
//                        {
//                            _isMarked="1";
//                            searchStdList();
//                        }
//                        else if(_isMarked.equalsIgnoreCase("चिह्नित नहीं"))
//                        {
//                            _isMarked="0";
//                            searchStdList();
//                        }

                        if(_isMarked.equalsIgnoreCase("75% से अधिक उपस्थिति"))
                        {
                            //_isMarked="0";
                            _isMarked="Y";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("75% से नीचे उपस्थिति"))
                        {
                           // _isMarked="1";
                            _isMarked="N";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("चिह्नित नहीं"))
                        {
                            // _isMarked="1";
                            _isMarked="anyType{}";
                            searchStdList();
                        }
                    }

                } else {
                    _isMarked = "";
                    searchStdList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        btnSave.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                boolean val=false;
//
//                try {
//                    ArrayList<CheckUnCheck> benids = GlobalVariables.benIDArrayList;
//                    long c=0;
//                    if (benids != null) {
//                        if (benids.size() > 0) {
//                            for (int x = 0; x < benids.size(); x++) {
//                                if(!benids.get(x).getBenID().equalsIgnoreCase("")) {
//                                    Log.e("BEN ID ", benids.get(x).getBenID() + "\n Status " + benids.get(x).getIsChecked());
//
//                                    ContentValues values = new ContentValues();
//                                    values.put("CreatedBy", diseCode);
//                                    String cdate=Utiilties.getDateString("yyyy-MM-dd");
//                                    cdate=cdate.replace("-","");
//                                    values.put("CreatedDate", cdate);
//                                            values.put("StdAttendanceLess", benids.get(x).getIsChecked());
//                                            values.put("AttSeventyFivePercent",benids.get(x).getIsChecked());
//                                            values.put("IsAttendanceUpdated","Y");
//
//                                        String[] whereArgsss = new String[]{benids.get(x).getBenID()};
//                                        SQLiteDatabase db = localDB.getWritableDatabase();
//                                        c = db.update("StudentListForAttendance", values, "BenID=?", whereArgsss);
//
//                                    if(c>0)
//                                    {
//                                        removeShortcut();
//                                        Toast.makeText(AttendanceUpdatedListActivity.this, "Saved Locally", Toast.LENGTH_SHORT).show();
//                                        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);
//                                        if(_lang.equalsIgnoreCase("en")) {
//                                            ab.setIcon(R.drawable.saveimgns);
//                                            ab.setTitle("UPDATED");
//                                            ab.setMessage("Attendance updated for BEN ID: "+ benids.get(x).getBenID() );
//                                            ab.setPositiveButton(R.string.ok,
//                                                    new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog,
//                                                                            int whichButton) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    });
//
//                                            ab.show();
//                                        }
//                                        else
//                                        {
//                                            ab.setIcon(R.drawable.saveimgns);
//                                            ab.setTitle("अपडेटेड");
//                                            ab.setMessage(" बेनआईडी "+ benids.get(x).getBenID() +" के लिए उपस्थिति सुरक्षित की गई|");
//                                            ab.setPositiveButton(R.string.okhn,
//                                                    new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog,
//                                                                            int whichButton) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    });
//
//                                            ab.show();
//                                        }
//
//                                        createShortCut();
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                }
//                catch(Exception e)
//                {
//                    Toast.makeText(AttendanceUpdatedListActivity.this, "EXCEPTION getting data from GlobalVariables.benIDArrayList", Toast.LENGTH_SHORT).show();
//                    Log.e("EXCEPTION","EXCEPTION getting data from GlobalVariables.benIDArrayList");
//                }
//
//
//
//            }
//        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);

                ArrayList<CheckUnCheck> benids = GlobalVariables.benIDArrayList;
                long c=0;
                if (benids != null) {
                    if (benids.size() > 0) {
                        if(_lang.equalsIgnoreCase("en")) {
                            ab.setIcon(R.drawable.saveimgns);
                            ab.setTitle("UPDATED");
                            ab.setMessage("Attendance updated");
                            ab.setPositiveButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
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
                            ab.setIcon(R.drawable.saveimgns);
                            ab.setTitle("अपडेटेड");
                            ab.setMessage("उपस्थिति सुरक्षित की गई|");
                            ab.setPositiveButton(R.string.okhn,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,  int whichButton) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });

                            ab.show();
                        }
                    }
                    else
                    {
                        Toast.makeText(AttendanceUpdatedListActivity.this, "No attendance updated.", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, "No attendance updated.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    CLASSLIST currentPhysical = ClassList.get(arg2 - 1);
                    _varClass_Id = currentPhysical.getClassID();
                    _varClass_Name = currentPhysical.getClassName();
                    _varClass_NameHn = currentPhysical.getClassNamehn();
                    searchStdList();
//                    new LoadAttendance(_diseCode,_varClass_Id).execute();
                } else {
                    _varClass_Id = "0";
                    _varClass_Name = "All";
                    _varClass_NameHn = "सभी";
                    searchStdList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    SESSIONLIST currentPhysical = SessionList.get(arg2 - 1);
                    _varSession_Id = currentPhysical.getSessionID();
                    _varSession_Name = currentPhysical.getSessionName();
                    _varSession_NameHn = currentPhysical.getSessionNamehn();
                    searchStdList();

                } else {
                    _varSession_Id = "0";
                    _varSession_Name = "None";
                    _varSession_NameHn = "कोई नहीं";
                    searchStdList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        setButtonsAndOtherLabels(_lang);
        loadClassList();
        loadSessionList();

        //HIDE FILTER SCREEN
       // lnHeading.setVisibility(View.GONE);
        txtSection.setVisibility(View.GONE);
        spSession.setVisibility(View.GONE);
        imgFilter.setVisibility(View.GONE);
        txtClass.setVisibility(View.GONE);
        spClass.setVisibility(View.GONE);
        txtMarked.setVisibility(View.VISIBLE);
        spn_marked.setVisibility(View.VISIBLE);
        //lnfilter.setVisibility(View.GONE);
    }
    
    public void createShortCut()
    {
        ico.CreateShortCut("","U");
    }

    private void removeShortcut() {
        ico.removeShortcut(getResources().getString(R.string.app_name));
    }
    public void setButtonsAndOtherLabels(String lang)
    {
//        if(lang.equalsIgnoreCase("en"))
//        {

            txtSection.setText("SECTION");
            txtMarked.setText("MARKED ?");
            txtClass.setText("CLASS");
            txt_FName.setText("FATHER NAME");
            txt_Name.setText("NAME");
            txt_ANum.setText("ADM.NO");
            txtHeaderName.setText(R.string.app_name);
            tvattendence.setText("ATTENDANCE LESS THEN 75%");
            btnSave.setText(R.string.submit);
            btnUpload.setText(R.string.upload);
            txtInfo75More.setText("छात्र (जिनकी उपस्थिति 75% से कम है उन्हें N चिह्नित करें/जिनकी उपस्थिति 75% से ऊपर है उन्हें Y चिह्नित करें)");
            tvmsg.setText("In below list only updated record(s) will be shown.");

     //   }
//        else
//        {
//            txtSection.setText("सेक्शन");
//            txtMarked.setText("चिह्नित ?");
//            txtClass.setText("कक्षा");
//            txt_FName.setText("पिता का नाम");
//            txt_Name.setText("नाम");
//            txt_ANum.setText("प्र. संख्या");
//            txtHeaderName.setText(R.string.app_namehn);
//            tvattendence.setText("75% से कम उपस्थिति");
//
//            btnSave.setText(R.string.submithn);
//            btnUpload.setText(R.string.uploadhn);
//            txtInfo75More.setText("कृपया बॉक्स को चेक करें यदि उपस्थिति 75% से अधिक है");
//            tvmsg.setText("नीचे दी गई सूची में केवल अपडेट किया गया रिकॉर्ड दिखाया जाएगा।");
//
//        }

    }
    public void loadClassList()
    {
        localDB = new DataBaseHelper(AttendanceUpdatedListActivity.this);
        ClassList=localDB.getClassList();
        if(ClassList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<ClassList.size();i++)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(ClassList.get(i).getClassName());
                }
                else
                {
                    StringList.add(ClassList.get(i).getClassNamehn());
                }
                if(_varClass_Id.equalsIgnoreCase(ClassList.get(i).getClassID()))
                {
                    setID=(i+1);
                }
            }
            ClassListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spClass.setAdapter(ClassListadapter);
        }
        else {

            if (Utiilties.isOnline(AttendanceUpdatedListActivity.this)) {
                Toast.makeText(this, "Please wait downloading class", Toast.LENGTH_SHORT).show();
                new LoadClassList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);
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
        localDB = new DataBaseHelper(AttendanceUpdatedListActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("None");
            }
            else
            {
                StringList.add("कोई नहीं");
            }
            int setID=0;
            for(int i=0;i<SessionList.size();i++)
            {
                StringList.add(SessionList.get(i).getSessionName());
                if(_varSession_Id.equalsIgnoreCase(SessionList.get(i).getSessionID()))
                {
                    setID=(i+1);
                }
            }
            SessionListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spSession.setAdapter(SessionListadapter);
        }

        else {


            if (Utiilties.isOnline(AttendanceUpdatedListActivity.this)) {
                Toast.makeText(this, "Please wait downloading sections", Toast.LENGTH_SHORT).show();
                new LoadSessionList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);
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

    public void loadGenderList()
    {
        localDB = new DataBaseHelper(AttendanceUpdatedListActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            StringList.add("-सभी-");
            int setID=0;
            for(int i=0;i<SessionList.size();i++)
            {
                StringList.add(SessionList.get(i).getSessionName());
                if(_varSession_Id.equalsIgnoreCase(SessionList.get(i).getSessionID()))
                {
                    setID=(i+1);
                }
            }
            SessionListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spSession.setAdapter(SessionListadapter);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();

    }


    private class LoadClassList extends AsyncTask<String, Void, ArrayList<CLASSLIST>> {

        @Override
        protected void onPreExecute() {
            pd2 = new ProgressDialog(AttendanceUpdatedListActivity.this);
            pd2.setTitle("Downloading Class...");
            pd2.setCancelable(false);
            pd2.show();
        }

        @Override
        protected ArrayList<CLASSLIST> doInBackground(String... param) {

            return WebServiceHelper.GetClassList();
        }

        @Override
        protected void onPostExecute(ArrayList<CLASSLIST> result) {
            if (pd2.isShowing()) {

                pd2.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt",""+result);
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setClassList(result);

                    if(i>0){
                        Toast.makeText(AttendanceUpdatedListActivity.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadClassList();
                        }
                    }
                    else{
                        Toast.makeText(AttendanceUpdatedListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class LoadSessionList extends AsyncTask<String, Void, ArrayList<SESSIONLIST>> {

        @Override
        protected void onPreExecute() {
            pd2 = new ProgressDialog(AttendanceUpdatedListActivity.this);
            pd2.setTitle("Downloading Section...");
            pd2.setCancelable(false);
            pd2.show();
        }

        @Override
        protected ArrayList<SESSIONLIST> doInBackground(String... param) {

            return WebServiceHelper.GetSessionList();
        }

        @Override
        protected void onPostExecute(ArrayList<SESSIONLIST> result) {
            if (pd2.isShowing()) {

                pd2.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt",""+result);
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setSessionList(result);

                    if(i>0){
                        Toast.makeText(AttendanceUpdatedListActivity.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadSessionList();
                        }
                    }
                    else{
                        Toast.makeText(AttendanceUpdatedListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void chk_tamilasucess(String msg) {
        // final String wantToUpdate;
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);
        ab.setCancelable(false);
        // ab.setIcon(R.drawable.bedicon);
        ab.setTitle("Upload Success");
        ab.setMessage(msg);
        Dialog dialog = new Dialog(AttendanceUpdatedListActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                dialog.dismiss();

            }
        });

        //  ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
        ab.show();
    }

    public void onCLick_UploadAttendance(View v)
    {

        if (Utiilties.isOnline(AttendanceUpdatedListActivity.this)) {

            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
           // String isfound = helper.getStudentCheckedAttendance(_diseCode,_varClass_Id,_varSession_Id,_isMarked);
            //final Cursor cursor = localDB.getStudentIDForAttendanceUploading(_varClass_Id);
            final Cursor cursor = localDB.getUpdatedStudentAttendance();
            String isfound=String.valueOf(cursor.getCount());
            if(isfound.equalsIgnoreCase("0"))
            {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(AttendanceUpdatedListActivity.this, "No records found to upload it on server.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, "सर्वर पर अपलोड करने के लिए कोई रिकॉर्ड नहीं मिला।", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        AttendanceUpdatedListActivity.this);
                if(_lang.equalsIgnoreCase("en")) {
                    ab.setTitle(R.string.upload);
                    ab.setMessage("Are you sure ? Want to upload records to the server ?\nIt will upload records of selected class or all class if not selected.");
                    ab.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

                    ab.setPositiveButton(
                            R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String newFormat = "yyyy-MM-dd";  ////"dd-MM-yyyy";

                                    String vardatetime = Utiilties.getDateString(newFormat);
                                    Log.e("vardatetime", vardatetime);
                                    vardatetime = vardatetime.replace("-", "");
                                    Log.e("vardatetimeN", vardatetime);

                                    Toast.makeText(AttendanceUpdatedListActivity.this, cursor.getCount() + " records uploading...", Toast.LENGTH_SHORT).show();
                                    while (cursor.moveToNext()) {
                                        String id = cursor.getString(cursor.getColumnIndex("id"));
                                        String uby = _diseCode;
                                        String udate = cursor.getString(cursor.getColumnIndex("CreatedDate"));;;
                                        String dcode = _diseCode;
                                        //String bid = cursor.getString(cursor.getColumnIndex("BenID"));
                                        String aid = cursor.getString(cursor.getColumnIndex("a_ID"));
                                        String marked = cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"));
                                        new UploadAttendance(id, uby, udate, dcode, aid, marked).execute();
                                    }
                                    if(!cursor.isClosed())
                                    {
                                        cursor.close();
                                    }
                                }
                            });

                    ab.show();
                }
                else
                {
                    ab.setTitle(R.string.uploadhn);
                    ab.setMessage("क्या आपको यकीन है ? सर्वर पर रिकॉर्ड अपलोड करना चाहते हैं?\n" +
                            "यह चयनित कक्षा या सभी कक्षा के रिकॉर्ड अपलोड करेगा यदि नहीं चुना गया है।");
                    ab.setNegativeButton(R.string.nohn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

                    ab.setPositiveButton(
                            R.string.yeshn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String newFormat= "yyyy-MM-dd";  ////"dd-MM-yyyy";

                                    String vardatetime = Utiilties.getDateString(newFormat);
                                    Log.e("vardatetime",vardatetime);
                                    vardatetime=vardatetime.replace("-","");
                                    Log.e("vardatetimeN",vardatetime);
                                    Cursor cursor=localDB.getStudentIDForAttendanceUploading(_varClass_Id);
                                    Toast.makeText(AttendanceUpdatedListActivity.this, cursor.getCount()+ " records uploading...", Toast.LENGTH_SHORT).show();
                                    while (cursor.moveToNext())
                                    {
                                        String id=cursor.getString(cursor.getColumnIndex("id"));
                                        String uby=_diseCode;
                                        String udate=vardatetime;
                                        String dcode=_diseCode;
                                        String aid = cursor.getString(cursor.getColumnIndex("a_ID"));
                                      //  String bid=cursor.getString(cursor.getColumnIndex("BenID"));
                                        String marked=cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"));
                                        new UploadAttendance(id,uby,udate,dcode,aid,marked).execute();
                                    }
                                }
                            });

                    ab.show();
                }
            }
        }
        else {

            final AlertDialog alertDialog = new AlertDialog.Builder(AttendanceUpdatedListActivity.this).create();
            if(_lang.equalsIgnoreCase("en")) {
                alertDialog.setTitle(R.string.no_internet_title);
                alertDialog.setMessage(getResources().getString(R.string.no_internet_msg));
                alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();
                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                    }
                });

                alertDialog.show();
            }
            else
            {
                alertDialog.setTitle(R.string.no_internet_titlehn);
                alertDialog.setMessage(getResources().getString(R.string.no_internet_msghn));
                alertDialog.setButton(getResources().getString(R.string.okhn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();
                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                    }
                });

                alertDialog.show();
            }
        }
    }
    private class UploadAttendance extends AsyncTask<String, Void, String> {

        String _id,_uby,_udate,_dcode,_aid,_status;

        private final ProgressDialog progressDialog = new ProgressDialog(AttendanceUpdatedListActivity.this);


        UploadAttendance(String id,String uby,String udate,String dcode,String aid,String status)
        {
            this._id=id;
            this._uby=uby;
            this._udate=udate;
            this._dcode=dcode;
            this._aid=aid;
            this._status=status;
        }
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.show();
            if(_lang.equalsIgnoreCase("en")) {
                progressDialog.setMessage("Uploading attendance...");
            }
            else
            {
                progressDialog.setMessage("उपस्थिति अपलोड हो रहा  है...");
            }
        }

        @Override
        protected String doInBackground(String... param) {

            String res= WebServiceHelper.UploadAttendanceData(AttendanceUpdatedListActivity.this,_uby,_udate,_dcode,_aid,_status);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
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
                    ///==Status=1; RowsEffected=1; Message=Ok;

                    if(result.equalsIgnoreCase("Updated")|| result.equalsIgnoreCase("Inserted"))
                    {
                        Log.e("Updated For a_ID ",_aid);
                        removeShortcut();
                        long c=  localDB.UpdateForAttendanceSubmittions(_aid);
                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "% Attendance updated on server" , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "सर्वर पर % उपस्थिति अपडेट हो गया", Toast.LENGTH_SHORT).show();
                        }
                        createShortCut();
                    }
                    else {
                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "Sorry! failed to upload Attendance for " + _aid + " \nResponse " , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "माफ़ कीजिये! " + _aid + " के लिए उपस्थिति अपलोड करने में विफल रहा  \nरिस्पोंस " , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    if(_lang.equalsIgnoreCase("en")) {
                        Toast.makeText(getApplicationContext(), "Response:NULL, Sorry! failed to upload Attendance for " + _aid, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "रिस्पोंस: NULL, क्षमा करें! " + _aid + " के लिए उपस्थिति अपलोड करने में विफल रहा \n", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public void searchStdList()
    {
        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
        studentList = helper.getStudentListForUpdatedAttendance(_diseCode,_varClass_Id,_varSession_Id,_isMarked);


        Parcelable state = list_student.onSaveInstanceState();
        if(studentList.size()>0)
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new StudentListAdaptor(AttendanceUpdatedListActivity.this, studentList,TotnnoClasses);
            list_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            if(!_varClass_Name.equalsIgnoreCase("")) {
                //Toast.makeText(this, studentList.size() + " records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
            }
            txtStdCount.setText(String.valueOf(studentList.size()));
        }
        else
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new StudentListAdaptor(AttendanceUpdatedListActivity.this, studentList,TotnnoClasses);
            list_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            if(!_varClass_Name.equalsIgnoreCase("")) {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(this, "Sorry! no records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "माफ़ कीजिये! कक्षा " + _varClass_Name +", सेक्शन " + _varSession_Name + " के लिए कोई रिकॉर्ड नहीं मिला " , Toast.LENGTH_SHORT).show();
                }
            }
            txtStdCount.setText(String.valueOf(studentList.size()));
        }

        list_student.onRestoreInstanceState(state);
    }

    public void onClick_GeneratePDF(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceUpdatedListActivity.this);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle(R.string.report_title);
            ab.setMessage(R.string.report_msg);
        }
        else
        {
            ab.setTitle(R.string.report_titlehn);
            ab.setMessage(R.string.report_msghn);
        }
        ab.setPositiveButton("[ English ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        try {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            progressDialog1.show();
                            dialog.dismiss();
                            createPdfviewlocal("en");

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        ab.setNegativeButton("[ हिंदी ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        try {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            progressDialog1.show();
                            dialog.dismiss();
                            createWebViewContaintsForPDF();
                        } catch (Exception e) {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            e.printStackTrace();
                        }

                    }
                });
        ab.show();
    }

    public void createWebViewContaintsForPDF()
    {

        // style='background-color:#0000CC; height:4px;
        String htmlTableH="<table width='100%'>";
        htmlTableH+="<tr>";
        htmlTableH+="<th><img alt='Medha Soft' src='file:///android_asset/title.png' height='50' width='100%'/></th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr>";
        htmlTableH+="<th>" + _schoolName  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr>";
        htmlTableH+="<th>" + _distBlock  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr><td><hr style='background-color: #0000FF; height:3'><br><br></td></tr>";

        String cDate=Utiilties.getDateString("dd-MMM-yyyy");

        if(_varClass_Name.contains("All"))
        {      }
        else
        {
            _varClass_Name=_varClass_Id;
        }
        String info="CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate;
        htmlTableH+="<tr>";
        htmlTableH+="<th style='color: #0000FF;'>" + info  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="</table><br><br>";

        String htmlTable="<table width='100%' style='border: thin solid #000000;'><tr style='color: #FFFFFF; background-color: #666666'>";
        htmlTable+="<th style='border: thin solid #000000; width:10%'>[ ]</th>";


        htmlTable+="<th style='border: thin solid #000000; width:20%'>प्रवेश संख्या</th>";
        htmlTable+="<th style='border: thin solid #000000; width:35%'>लाभार्थी</th>";
        htmlTable+="<th style='border: thin solid #000000; width:35%'>पिता का नाम</th>";

        htmlTable+="</tr>";


        int countLess75Per=0;
        if(studentList!=null)
        {
            if(studentList.size()>0)
            {

                for(int x=0;x<studentList.size();x++) {
                    htmlTable+="<tr>";
                    if (studentList.get(x).getStdattseventyfiveper().equalsIgnoreCase("1")) {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>[x]</th>";
                        countLess75Per = countLess75Per + 1;
                    } else {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>[ ]</th>";
                    }



                    htmlTable += "<th style='border: thin solid #000000; width:35%'>"+Utiilties.getValue(studentList.get(x).getStdadmnum())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:35%'>"+Utiilties.getValue(studentList.get(x).getStdnamehn())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:35%'>"+Utiilties.getValue(studentList.get(x).getStdfnamehn())+"</th>";

                    htmlTable+="</tr>";
                }
            }
        }
        htmlTable+="</table><br><br>";

        String htmlTable2="<table width='100%' style='border: thin solid #000000;'><tr style='color: #FFFFFF; background-color: #666666'>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>विवरण</th>";
        htmlTable2+="<th style='border: thin solid #000000; width:10%'>गिनती</th>";
        htmlTable2+="</tr>";

        htmlTable+="<tr>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>छात्रों की कुल संख्या</th>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>"+ String.valueOf(studentList.size()) +"</th>";
        htmlTable2+="</tr>";

        htmlTable+="<tr>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>छात्रों की संख्या उपस्थिति 75% से कम है</th>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>"+ String.valueOf(countLess75Per) +"</th>";
        htmlTable2+="</tr>";

        htmlTable+="<tr>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>छात्रों की संख्या उपस्थिति 75% से अधिक है</th>";
        htmlTable2+="<th style='border: thin solid #000000; width:80%'>"+String.valueOf(studentList.size()- countLess75Per)  +"</th>";
        htmlTable2+="</tr>";
        htmlTable2+="</table>";

        WebSettings webSettings = showpdfdataWV.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");

        showpdfdataWV.setWebViewClient(new myWebClient());


        String myHtmlString=htmlTableH+htmlTable+htmlTable2;

        Log.e("HTML",myHtmlString);

        //showpdfdataWV.loadData(myHtmlString, "text/html; charset=UTF-8", null);
        showpdfdataWV.loadDataWithBaseURL(null,myHtmlString, "text/html; charset=UTF-8", null,null);
    }
    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            Log.i("OnPageLoadFinished", url);

            if(progressDialog1.isShowing())
            {
                progressDialog1.dismiss();
            }
            //printOrCreatePdfFromWebview(view);
            generatePDFForHindiFont(view);
        }
    }


    public  void generatePDFForHindiFont(WebView wv)
    {
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        final String fileName=_filePrefixName+timeStamp+".pdf";

        final ProgressDialog progressDialog=new ProgressDialog(AttendanceUpdatedListActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(AttendanceUpdatedListActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(AttendanceUpdatedListActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    public void writeFont()
    {
        try {
            File file = new File(getFilesDir(), HINDI_FONT);
            if (file.length() == 0) {
                InputStream fs = getAssets().open(HINDI_FONT);
                FileOutputStream os = new FileOutputStream(file);
                int i;
                while ((i = fs.read()) != -1) {
                    os.write(i);
                }
                os.flush();
                os.close();
                fs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void createPdfviewlocal(String lang) throws IOException, DocumentException {
        if(progressDialog1.isShowing())
        {
            progressDialog1.dismiss();
        }
        if(lang.equalsIgnoreCase("en")) {
            progressDialog1.setMessage("Please wait. Genreating PDF file");
        }
        else
        {
            progressDialog1.setMessage("कृपया प्रतीक्षा करें। पीडीएफ फाइल जेनेरेट हो रहा है");
        }
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

//        Font f;
//        writeFont();

        String targetPdf = "/sdcard/"+_filePrefixName+timeStamp+".pdf";
        File filePath;
        filePath = new File(targetPdf);

        // myFile= new File(pdfFolder+ ".pdf");

        OutputStream output = new FileOutputStream(filePath);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        // PdfContentByte canvas = writer.getDirectContent();
        Drawable d = getResources().getDrawable(R.drawable.title);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        //image.setAbsolutePosition(50f,750f);
        image.setAlignment(Element.ALIGN_CENTER);
        image.scaleToFit(550,50);
        document.add(image);
        //Step 4 Add content
        document.setPageSize(PageSize.LETTER);
        document.setMargins(0, 0, 75, 180);
        document.setMarginMirroring(true);

        document.add(new Chunk("\n"));

        Font f1= new Font (Font.FontFamily.UNDEFINED, 18, Font.BOLD, BaseColor.BLUE);
        Paragraph info= new Paragraph(_schoolName,f1);
        // info.setAlignment(Element.ALIGN_MIDDLE);


        Paragraph addr= new Paragraph("",f1);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        table.spacingAfter();
        PdfPCell cell = new PdfPCell(info);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        cell = new PdfPCell(addr);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        document.add(table);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.UNDERLINE, BaseColor.BLUE);
        Paragraph title1= new Paragraph(_distBlock,font1);
        title1.setAlignment(Element.ALIGN_CENTER);
        document.add(title1);
        document.add(new Chunk("\n"));


        document.add(new LineSeparator(2f,100,BaseColor.GRAY,Element.ALIGN_CENTER,-1f));

        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLUE);
        String cDate=Utiilties.getDateString("dd-MMM-yyyy");

        if(_varClass_Name.contains("All"))
        {

        }
        else
        {
            _varClass_Name=_varClass_Id;
        }

        Paragraph createdDate= new Paragraph("CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate,font2 );
        //Paragraph createdDate= new Paragraph("Created On: "+ cDate,font2 );
        createdDate.setAlignment(Element.ALIGN_CENTER);
        document.add(createdDate);

        Font fonthdr = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE);

        document.add(new Chunk("\n"));
        PdfPTable tableC = new PdfPTable(new float[] { 1,5, 5, 5 });
        tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        if(lang.equalsIgnoreCase("en")) {
            tableC.addCell(new Phrase("[ ]", fonthdr));

            tableC.addCell(new Phrase("ADMISSION NO.", fonthdr));
            tableC.addCell(new Phrase("BENEFICIARY", fonthdr));
            tableC.addCell(new Phrase("FATHER NAME", fonthdr));;

        }
//

        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);

        }

        int countLess75Per=0;
        if(studentList!=null)
        {
            if(studentList.size()>0)
            {
                if(lang.equalsIgnoreCase("en"))
                {
                    for(int x=0;x<studentList.size();x++)
                    {
                        if(studentList.get(x).getStdattseventyfiveper().equalsIgnoreCase("1"))
                        {
                            tableC.addCell("[x]");
                            countLess75Per=countLess75Per+1;
                        }
                        else
                        {
                            tableC.addCell("[ ]");
                        }


                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdadmnum()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdname()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdfname()));

                    }
                }
                else
                {
                    for(int x=0;x<studentList.size();x++)
                    {
                        if(studentList.get(x).getStdattseventyfiveper().equalsIgnoreCase("1"))
                        {
                            tableC.addCell("[x]");
                            countLess75Per=countLess75Per+1;
                        }
                        else
                        {
                            tableC.addCell("[ ]");
                        }



                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdadmnum()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdnamehn()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdfnamehn()));

                    }
                }

            }
        }
        document.add(tableC);

        document.add(new Chunk("\n"));
        //Another table
        PdfPTable tableD = new PdfPTable(new float[] { 2,1 });
        tableD.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell(new Phrase("DESCRIPTION", fonthdr));
            tableD.addCell(new Phrase("COUNT", fonthdr));
        }


        tableD.setHeaderRows(1);
        PdfPCell[] cellsd = tableD.getRow(0).getCells();
        for (int j=0;j<cellsd.length;j++){
            cellsd[j].setBackgroundColor(BaseColor.GRAY);

        }

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("TOTAL NUMBER OF STUDENTS");
        }

        tableD.addCell(String.valueOf(studentList.size()));

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("STUDENTS ATTENDANCE LESS THEN 75%");
        }

        tableD.addCell(String.valueOf(countLess75Per));

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("STUDENTS ATTENDANCE MORE THEN 75%");
        }
        tableD.addCell(String.valueOf(studentList.size()-countLess75Per));

        document.add(tableD);


        //Step 5: Close the document
        document.close();

        if(Build.VERSION.SDK_INT>=24) {
            openGeneratedPDF2();
        }
        else
        {
            openGeneratedPDF();
        }
    }
    private void openGeneratedPDF(){
        //File file = new File("/sdcard/bedpost"+timeStamp+".pdf");
        File file = new File("/sdcard/"+_filePrefixName+timeStamp+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void openGeneratedPDF2(){
        //File file = new File("/sdcard/bedpost"+timeStamp+".pdf");
        File file = new File("/sdcard/"+_filePrefixName+timeStamp+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            //Uri uri = Uri.fromFile(file);
            Uri uri = FileProvider.getUriForFile(AttendanceUpdatedListActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AttendanceUpdatedListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(AttendanceUpdatedListActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}

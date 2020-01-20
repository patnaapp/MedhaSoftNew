package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
//import android.support.v4.os.BuildCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import bih.nic.medhasoft.Adapter.StudentListAdaptor;
import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.Model.studentList;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//======================
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;






public class AttendanceListActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog1;
    public static ProgressDialog pd2;
    ShorCutICON ico;
    StudentListAdaptor studentListAdaptor;

    ListView list_student;
    Button btnSave,btnUpload;
    //ImageView btnSave,btnUpload;
    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();
    ArrayList<bih.nic.medhasoft.Model.studentList> studentArrayList=new ArrayList<>();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendancelist);
        ico=new ShorCutICON(AttendanceListActivity.this);
        localDB = new DataBaseHelper(this);

        db = localDB.getWritableDatabase();
        tvattendence =(TextView)findViewById(R.id.tvattendence);
        txtStdCount =(TextView)findViewById(R.id.txtStdCount);

        txtStdCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(AttendanceListActivity.this, "Total Student: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AttendanceListActivity.this, "कुल छात्र: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        txtInfo75More =(TextView)findViewById(R.id.txtInfo75More);
        tvmsg =(TextView)findViewById(R.id.tvmsg);
        tvmsg.setVisibility(View.GONE);
        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");

        list_student=(ListView)findViewById(R.id.list_student);
        spClass=(Spinner)findViewById(R.id.spn_class);
        spSession=(Spinner)findViewById(R.id.spn_session);
        spn_marked=(Spinner)findViewById(R.id.spn_marked);
        // btnSave=(Button) findViewById(R.id.btnsave);
        btnSave= findViewById(R.id.btnsave);
        lnBtns= findViewById(R.id.lnBtns);
        imgFilter =  findViewById(R.id.imgFilter);
        txt_FName = (TextView) findViewById(R.id.txt_FName);
        txt_MName = (TextView) findViewById(R.id.txt_MName);
        txt_DoB = (TextView) findViewById(R.id.txt_Dob);
        txt_Name = (TextView) findViewById(R.id.txt_Name);
        txt_ANum = (TextView) findViewById(R.id.txt_ANum);

        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        txtSection = (TextView) findViewById(R.id.txtSection);
        txtMarked = (TextView) findViewById(R.id.txtMarked);
        txtClass = (TextView) findViewById(R.id.txtClass);
        txt_StdClass = (TextView) findViewById(R.id.txt_StdClass);

        btnUpload=findViewById(R.id.btnupload);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);




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

        showpdfdata=(TextView)findViewById(R.id.showpdfdata); //-------------------------
        txtPDFHeader=(TextView)findViewById(R.id.txtPDFHeader); //------------
        showpdfdataWV=findViewById(R.id.showpdfdataWV); //-------------------------
        showinstructions();

        list_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view.findViewById(R.id.txtBenID);
                Intent iListPendingUpload = new Intent(AttendanceListActivity.this, EditBendDetailsActivity.class);
                iListPendingUpload.putExtra("EDIT", "VIEW");
                iListPendingUpload.putExtra("BENID", tv.getText().toString());
                startActivity(iListPendingUpload);
                //   Toast.makeText(AttendanceListActivity.this, "OK" + tv.getText(), Toast.LENGTH_SHORT).show();
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

                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);

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

        if(_lang.equalsIgnoreCase("en"))
        {
            spinnerMarkedAdapter=new ArrayAdapter(this,R.layout.dropdownlist,MarkedList);
        }
        else
        {
            spinnerMarkedAdapter=new ArrayAdapter(this,R.layout.dropdownlist,MarkedListHn);
        }

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

                        //if(_isMarked.equalsIgnoreCase("Marked"))
                        if(_isMarked.equalsIgnoreCase("Above 75 % attendance"))
                        {
                            _isMarked="Y";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("Below 75 % attendance"))
                        {
                            _isMarked="N";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("Unmarked"))
                        {
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
                            _isMarked="Y";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("75% से नीचे उपस्थिति"))
                        {
                            _isMarked="N";
                            searchStdList();
                        }
                        else if(_isMarked.equalsIgnoreCase("चिह्नित नहीं"))
                        {
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
//
//                                    values.put("CreatedBy", diseCode);
//                                    String cdate=Utiilties.getDateString("yyyy-MM-dd");
//                                    cdate=cdate.replace("-","");
//                                    values.put("CreatedDate", cdate);
//                                    values.put("StdAttendanceLess", benids.get(x).getIsChecked());
//                                    values.put("AttSeventyFivePercent",benids.get(x).getIsChecked());
//                                    values.put("IsAttendanceUpdated","Y");
//
//                                    String[] whereArgsss = new String[]{benids.get(x).getBenID()};
//                                    SQLiteDatabase db = localDB.getWritableDatabase();
//                                    c = db.update("StudentListForAttendance", values, "BenID=?", whereArgsss);
//
//                                    if(c>0)
//                                    {
//                                        removeShortcut();
//                                        Toast.makeText(AttendanceListActivity.this, "Saved Locally", Toast.LENGTH_SHORT).show();
//                                        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
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
//
//                                }
//                            }
//                        }
//                    }
//                }
//                catch(Exception e)
//                {
//                    Toast.makeText(AttendanceListActivity.this, "EXCEPTION getting data from GlobalVariables.benIDArrayList", Toast.LENGTH_SHORT).show();
//                    Log.e("EXCEPTION","EXCEPTION getting data from GlobalVariables.benIDArrayList");
//                }
//
//                //============
//
//            }
//        });




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);

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
                        Toast.makeText(AttendanceListActivity.this, "No attendance updated.", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(AttendanceListActivity.this, "No attendance updated.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {
                if (arg2 > 0)
                {

                    CLASSLIST currentPhysical = ClassList.get(arg2 - 1);
                    _varClass_Id = currentPhysical.getClassID();
                    _varClass_Name = currentPhysical.getClassName();
                    _varClass_NameHn = currentPhysical.getClassNamehn();

                    searchStdList();
//                    new LoadAttendance(_diseCode,_varClass_Id).execute();
                }
                else
                {
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
    }

    public void additems()
    {
        flag_loading=false;
        searchStdList();

    }
    public void setButtonsAndOtherLabels(String lang)
    {
//        if(lang.equalsIgnoreCase("en"))
//        {

            txtSection.setText("SECTION");
            txtMarked.setText("MARKED ?");
            txtClass.setText("CLASS");
            txt_StdClass.setText("CLASS");
            txt_FName.setText("FATHER NAME");
            txt_MName.setText("MOTHER NAME");
            txt_DoB.setText("DoB");
            txt_Name.setText("NAME");
            txt_ANum.setText("ADM.NO");

            txtHeaderName.setText(R.string.app_name);

            tvattendence.setText("ATTENDANCE LESS THEN 75%");
            btnSave.setText(R.string.submit);
            btnUpload.setText(R.string.upload);
            //   txtInfo75More.setText("Please uncheck box if attendance in more then 75%");
            //txtInfo75More.setText("Please check the checkbox if attendance is more than 75%");
            txtInfo75More.setText("छात्र (जिनकी उपस्थिति 75% से कम है उन्हें N चिह्नित करें/जिनकी उपस्थिति 75% से ऊपर है उन्हें Y चिह्नित करें)");
            tvmsg.setText("In below list only updated record(s) will be shown.");
       // }
//        else
//        {
//            txtSection.setText("सेक्शन");
//            txtMarked.setText("चिह्नित ?");
//            txtClass.setText("कक्षा");
//            txt_StdClass.setText("कक्षा");
//            txt_FName.setText("पिता का नाम");
//            txt_MName.setText("माता का नाम");
//            txt_DoB.setText("जन्म तिथि");
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
        localDB = new DataBaseHelper(AttendanceListActivity.this);
        ClassList=localDB.getClassList();
        if(ClassList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();

                StringList.add("-All-");

            int setID=0;
            for(int i=0;i<ClassList.size();i++)
            {

                    StringList.add(ClassList.get(i).getClassName());

                if(_varClass_Id.equalsIgnoreCase(ClassList.get(i).getClassID()))
                {
                    setID=(i+1);
                }
            }
            ClassListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spClass.setAdapter(ClassListadapter);
        }
        else {

            if (Utiilties.isOnline(AttendanceListActivity.this)) {
                Toast.makeText(this, "Please wait downloading class", Toast.LENGTH_SHORT).show();
                new LoadClassList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
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
        localDB = new DataBaseHelper(AttendanceListActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();

                StringList.add("None");

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


            if (Utiilties.isOnline(AttendanceListActivity.this)) {
                Toast.makeText(this, "Please wait downloading sections", Toast.LENGTH_SHORT).show();
                new LoadSessionList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
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
        localDB = new DataBaseHelper(AttendanceListActivity.this);
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
            pd2 = new ProgressDialog(AttendanceListActivity.this);
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
                        Toast.makeText(AttendanceListActivity.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadClassList();
                        }
                    }
                    else{
                        Toast.makeText(AttendanceListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
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
            pd2 = new ProgressDialog(AttendanceListActivity.this);
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
                        Toast.makeText(AttendanceListActivity.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadSessionList();
                        }
                    }
                    else{
                        Toast.makeText(AttendanceListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void chk_tamilasucess(String msg) {
        // final String wantToUpdate;
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
        ab.setCancelable(false);
        // ab.setIcon(R.drawable.bedicon);
        ab.setTitle("Upload Success");
        ab.setMessage(msg);
        Dialog dialog = new Dialog(AttendanceListActivity.this);
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

        if (Utiilties.isOnline(AttendanceListActivity.this)) {

            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            String isfound = helper.getStudentCheckedAttendance(_diseCode,_varClass_Id,_varSession_Id,_isMarked);
            if(isfound.equalsIgnoreCase("0"))
            {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(AttendanceListActivity.this, "No records found to upload it on server.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AttendanceListActivity.this, "सर्वर पर अपलोड करने के लिए कोई रिकॉर्ड नहीं मिला।", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        AttendanceListActivity.this);
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
                                    dialog.dismiss();

                                    getAttendanceAndUploadIT();
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

                                    dialog.dismiss();
                                    getAttendanceAndUploadIT();
                                }
                            });

                    ab.show();
                }
            }
        }
        else {

            final AlertDialog alertDialog = new AlertDialog.Builder(AttendanceListActivity.this).create();
            //alertDialog.setTitle(getResources().getString(R.string.no_internet_title));
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

    public void getAttendanceAndUploadIT()
    {
        try {
            ArrayList<CheckUnCheck> benids = GlobalVariables.benIDArrayList;
            long c = 0;
            if (benids != null) {
                if (benids.size() > 0) {
                    for (int x = 0; x < benids.size(); x++) {
                        if(!benids.get(x).getBenID().equalsIgnoreCase("")) {
                            Cursor cursor = localDB.getStudentAttendanceForBenID(benids.get(x).getBenID().toString().trim());
                            Toast.makeText(AttendanceListActivity.this, cursor.getCount() + " records uploading...", Toast.LENGTH_SHORT).show();
                            while (cursor.moveToNext()) {
                                String id = cursor.getString(cursor.getColumnIndex("id"));
                                String uby = _diseCode;
                                String udate = cursor.getString(cursor.getColumnIndex("CreatedDate"));;
                                String dcode = _diseCode;
                                String bid = cursor.getString(cursor.getColumnIndex("BenID"));
                                String marked = cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"));
                                new UploadAttendance(id, uby, udate, dcode, bid, marked).execute();
                            }
                            if(!cursor.isClosed())
                            {
                                cursor.close();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    private class UploadAttendance extends AsyncTask<String, Void, String> {

        String _id,_uby,_udate,_dcode,_bid,_status;

        private final ProgressDialog progressDialog = new ProgressDialog(AttendanceListActivity.this);


        UploadAttendance(String id,String uby,String udate,String dcode,String bid,String status)
        {
            this._id=id;
            this._uby=uby;
            this._udate=udate;
            this._dcode=dcode;
            this._bid=bid;
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

            String res= WebServiceHelper.UploadAttendanceData(AttendanceListActivity.this,_uby,_udate,_dcode,_bid,_status);

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
                        Log.e("Updated For Ben ID ",_bid);

                        ContentValues values = new ContentValues();
                        values.put("IsAttendanceUpdated","N");
                        String[] whereArgsss = new String[]{_bid};
                        SQLiteDatabase db = localDB.getWritableDatabase();
                        long c = db.update("StudentListForAttendance", values, "BenID=?", whereArgsss);
                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "% Attendance updated on server" , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "सर्वर पर % उपस्थिति अपडेट हो गया", Toast.LENGTH_SHORT).show();
                        }
                        searchStdList();
                    }
                    else {
                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "Sorry! failed to upload Attendance for " + _bid + " \nResponse " , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "माफ़ कीजिये! " + _bid + " के लिए उपस्थिति अपलोड करने में विफल रहा  \nरिस्पोंस ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    if(_lang.equalsIgnoreCase("en")) {
                        Toast.makeText(getApplicationContext(), "Response:NULL, Sorry! failed to upload Attendance for " + _bid, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "रिस्पोंस: NULL, क्षमा करें! " + _bid + " के लिए उपस्थिति अपलोड करने में विफल रहा \n" , Toast.LENGTH_SHORT).show();
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
        studentList = helper.getStudentListForAttendance(_diseCode,_varClass_Id,_varSession_Id,_isMarked,_varFYear_Id);
        studentArrayList=studentList;
        Parcelable state = list_student.onSaveInstanceState();
        if(studentList.size()>0)
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new StudentListAdaptor(AttendanceListActivity.this, studentList,TotnnoClasses);
            list_student.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            list_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            if(!_varClass_Name.equalsIgnoreCase("")) {
                //  Toast.makeText(this, studentList.size() + " records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            txtStdCount.setText(String.valueOf(studentList.size()));
            studentListAdaptor = new StudentListAdaptor(AttendanceListActivity.this, studentList,TotnnoClasses);
            list_student.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            list_student.setAdapter(studentListAdaptor);
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

        list_student.onRestoreInstanceState(state);
    }

    public void onClick_GeneratePDF(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
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
        //Font f;
        //writeFont();

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
        // end

        //document.add(new Paragraph(showpdfdata.getText().toString()));
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
//        else
//        {
//            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            tableD.addCell(new Phrase("विवरण", f));
//            tableD.addCell(new Phrase("गिनती", f));
//        }

        tableD.setHeaderRows(1);
        PdfPCell[] cellsd = tableD.getRow(0).getCells();
        for (int j=0;j<cellsd.length;j++){
            cellsd[j].setBackgroundColor(BaseColor.GRAY);

        }

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("TOTAL NUMBER OF STUDENTS");
        }
//        else
//        {
//            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            tableD.addCell(new Phrase("छात्रों की कुल संख्या",f));
//        }
        tableD.addCell(String.valueOf(studentList.size()));

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("STUDENTS ATTENDANCE LESS THEN 75%");
        }
//        else
//        {
//            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            tableD.addCell(new Phrase("छात्रों की संख्या उपस्थिति 75% से कम है",f));
//        }
        tableD.addCell(String.valueOf(countLess75Per));

        if(lang.equalsIgnoreCase("en")) {
            tableD.addCell("STUDENTS ATTENDANCE MORE THEN 75%");
        }
//        else
//        {
//            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            tableD.addCell(new Phrase("छात्रों की संख्या उपस्थिति 75% से अधिक है",f));
//        }
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
                    Toast.makeText(AttendanceListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AttendanceListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
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
            Uri uri = FileProvider.getUriForFile(AttendanceListActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
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
                    Toast.makeText(AttendanceListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AttendanceListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }
            }
        }
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

        final ProgressDialog progressDialog=new ProgressDialog(AttendanceListActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(AttendanceListActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(AttendanceListActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    public PrintJob printOrCreatePdfFromWebview(WebView webview) {
        String jobName = getString(R.string.app_name) + " Document";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Context c = webview.getContext();
            PrintDocumentAdapter printAdapter;
            PrintManager printManager = (PrintManager) c.getSystemService(Context.PRINT_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = webview.createPrintDocumentAdapter(jobName);
            } else {
                printAdapter = webview.createPrintDocumentAdapter();
            }
            if (printManager != null) {
                return printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
            }
        } else {
            Log.e(getClass().getName(), "ERROR: Method called on too low Android API version");
        }
        return null;
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
    public void createShortCut()
    {
        ico.CreateShortCut("","U");
    }

    private void removeShortcut() {
        ico.removeShortcut(getResources().getString(R.string.app_name));
    }

    public void onClick_MoreFilter(View v)
    {
        String msg="";

        if(_varClass_Id.equalsIgnoreCase(""))
        {
            if(_lang.equalsIgnoreCase("en"))
            {
                msg="Please select Class";
            }
            else
            {
                msg="कृपया कक्षा का चयन करें";
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        }
        else if(_varClass_Id.equalsIgnoreCase("0"))
        {
            if(_lang.equalsIgnoreCase("en"))
            {
                msg="Please select Class";
            }
            else
            {
                msg="कृपया कक्षा का चयन करें";
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        }
//        else if(_varSession_Id.equalsIgnoreCase(""))
//        {
//            if(_lang.equalsIgnoreCase("en"))
//            {
//                msg="Please select Section";
//            }
//            else
//            {
//                msg="कृपया सेक्शन का चयन करें";
//            }
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//        }
//        else if(_varSession_Id.equalsIgnoreCase("0"))
//        {
//            if(_lang.equalsIgnoreCase("en"))
//            {
//                msg="Please select Section";
//            }
//            else
//            {
//                msg="कृपया सेक्शन का चयन करें";
//            }
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//        }
        else  if(_isMarked.equalsIgnoreCase(""))
        {

            if(_lang.equalsIgnoreCase("en"))
            {
                msg="Please select Marked / Un-Marked Year";
            }
            else
            {
                msg="कृपया चिह्नित / अचिह्नित का चयन करें";
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        }

        else
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.additionalfilter, null);
            dialogBuilder.setView(dialogView);

            spnCategory = (Spinner) dialogView.findViewById(R.id.spnCategory);
            spnIsMinority = (Spinner) dialogView.findViewById(R.id.spnIsMinority);
            spnGender = (Spinner) dialogView.findViewById(R.id.spnGender);
            spnIsBPL = (Spinner) dialogView.findViewById(R.id.spnIsBPL);
            spnIsHandicaped = (Spinner) dialogView.findViewById(R.id.spnIsHandicaped);
            spnHavingNonValue = (Spinner) dialogView.findViewById(R.id.spnHavingNonValue);
            txtStdNameForSearch =  dialogView.findViewById(R.id.txtStdNameForSearch);
            minoritytlist=new ArrayList<String>();
            divyanglist=new ArrayList<String>();
            havingnonevaluelist=new ArrayList<String>();
            bpllist=new ArrayList<String>();
            txtAdnStdCount =  dialogView.findViewById(R.id.txtAdnStdCount);

            loadGenderList(spnGender);
            loadCategoryHNList(spnCategory);
            addminority(spnIsMinority);
            adddivyang(spnIsHandicaped);
            addNoneValueField(spnHavingNonValue);
            adddbpl(spnIsBPL);

            if(std_name!=null)
            {
                if(std_name.length()>0)
                {
                    txtStdNameForSearch.setText(std_name);
                }
            }
            else
            {
                std_name="";
            }

            spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {
                        categoryHINDI currentPhysical = catlists.get(arg2 - 1);
                        _varCategory_Id = currentPhysical.getCategoryID();
                        _varCategory_Name = currentPhysical.getCategoryName();
                        _varCategory_NameHn = currentPhysical.getCategoryNameHn();

                    } else {
                        _varCategory_Id = "0";
                        _varCategory_Name = "";
                        _varCategory_NameHn="";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spnIsMinority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {

                        str_minority = minoritytlist.get(arg2 );

                        if(str_minority.equalsIgnoreCase("हाँ") || str_minority.equalsIgnoreCase("Yes"))
                        {
                            str_minority="Y";
                        }
                        else
                        {
                            str_minority="N";
                        }

                    } else {

                        str_minority="0";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {

                        Genderlist currentPhysical = genderlists.get(arg2 - 1);
                        _varGender_Id = currentPhysical.getGenderID();
                        _varGender_Name = currentPhysical.getGenderName();
                        _varGender_NameHn = currentPhysical.getGenderNameHn();

                    } else {
                        _varGender_Id = "0";
                        _varGender_Name = "";
                        _varGender_NameHn="";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spnIsBPL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {

                        str_bpl = bpllist.get(arg2 );

                        if(str_bpl.equalsIgnoreCase("हाँ") || str_bpl.equalsIgnoreCase("Yes"))
                        {
                            str_bpl="Y";
                        }
                        else
                        {
                            str_bpl="N";
                        }

                    } else {
                        str_bpl="0";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spnIsHandicaped.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {

                        str_divyang = divyanglist.get(arg2 );

                        if(str_divyang.equalsIgnoreCase("हाँ") || str_divyang.equalsIgnoreCase("Yes"))
                        {
                            str_divyang="Y";
                        }
                        else
                        {
                            str_divyang="N";
                        }

                    } else {
                        str_divyang="0";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spnHavingNonValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (arg2 > 0) {

                        str_none_value = havingnonevaluelist.get(arg2 );

                        if(str_none_value.equalsIgnoreCase("आधार संख्या") || str_none_value.equalsIgnoreCase("Aadhar Number"))
                        {
                            str_none_value="AadharCardNo";
                        }
                        else if(str_none_value.equalsIgnoreCase("खाता संख्या") || str_none_value.equalsIgnoreCase("Account Number"))
                        {
                            str_none_value="AccountNo";
                        }
                        else if(str_none_value.equalsIgnoreCase("प्रवेश संख्या") || str_none_value.equalsIgnoreCase("Admission Number"))
                        {
                            str_none_value="StdRegNum";
                        }
                        else if(str_none_value.equalsIgnoreCase("IFSC कोड") || str_none_value.equalsIgnoreCase("IFSC Code"))
                        {
                            str_none_value="IFSC";
                        }
                    } else {
                        str_none_value="0";
                    }
                    getAdditionalSearchResult();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


            txtStdNameForSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.length()>=1)
                    {
                        Log.e("onTextChanged",s.toString());
                        std_name=s.toString();
                        getAdditionalSearchResult();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            dialogBuilder.setIcon(R.drawable.filters);
            if(_lang.equalsIgnoreCase("en")) {
                txtStdNameForSearch.setHint("Enter Student Name in English");
                dialogBuilder.setTitle("Additional Filter");
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        std_name = txtStdNameForSearch.getText().toString();
                        dialog.dismiss();
                        getAdditionalSearchResult();

                    }
                });

                dialogBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        resetAdditionSearchCriteria();
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        //pass
                    }
                });
            }
            else
            {
                txtStdNameForSearch.setHint("हिंदी में छात्र का नाम दर्ज करें");
                dialogBuilder.setTitle("अतिरिक्त फ़िल्टर");
                dialogBuilder.setPositiveButton(R.string.okhn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // if(isSelectedAnyValue( _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang)) {
                        std_name = txtStdNameForSearch.getText().toString();
                        dialog.dismiss();
                        getAdditionalSearchResult();
                    }
                });

                dialogBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        resetAdditionSearchCriteria();
                    }
                });

                dialogBuilder.setNegativeButton(R.string.cancehn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        //pass
                    }
                });
            }
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

    }
    public void resetAdditionSearchCriteria()
    {
        spnCategory.setSelection(0);
        _varCategory_Id="0";

        spnIsMinority.setSelection(0);
        str_minority="0";

        spnGender.setSelection(0);
        _varGender_Id="0";

        spnIsBPL.setSelection(0);
        str_bpl="0";

        spnIsHandicaped.setSelection(0);
        str_divyang="0";

        txtStdNameForSearch.setSelection(0);
        txtAdnStdCount.setText("");
        std_name="";
        getAdditionalSearchResult();
    }
    public void getAdditionalSearchResult()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        studentList = helper.getStudentDetaislAsPerAdditionFilter(_diseCode, _varClass_Id, _varSession_Id,_isMarked, _varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value);

        studentListAdaptor = new StudentListAdaptor(AttendanceListActivity.this,studentList,"");
        list_student.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();
        txtStdCount.setText(String.valueOf(studentList.size()));
        txtAdnStdCount.setText(String.valueOf(studentList.size()));
    }
    public void loadGenderList(Spinner spn_gender)
    {
        localDB = new DataBaseHelper(AttendanceListActivity.this);
        genderlists=localDB.getGenderList();
        if(genderlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            // StringList.add("-चुने-");
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<genderlists.size();i++)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    StringList.add(genderlists.get(i).getGenderName());
                }
                else
                {
                    StringList.add(genderlists.get(i).getGenderNameHn());
                }
                if(_varGender_Id.equalsIgnoreCase(genderlists.get(i).getGenderID()))
                {
                    setID=(i+1);
                }

            }

            GenderListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_gender.setAdapter(GenderListadapter);
            spn_gender.setSelection(setID);
        }
    }
    public void loadCategoryHNList(Spinner spn_category)
    {
        localDB = new DataBaseHelper(AttendanceListActivity.this);
        catlists=localDB.getCategoryHNList();
        if(catlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            //StringList.add("-चुने-");
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<catlists.size();i++)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(catlists.get(i).getCategoryName());
                }
                else
                {
                    StringList.add(catlists.get(i).getCategoryNameHn());
                }
                if(_varCategory_Id.equalsIgnoreCase(catlists.get(i).getCategoryID()))
                {
                    setID=(i+1);
                }
            }

            catListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_category.setAdapter(catListadapter);
            catListadapter.notifyDataSetChanged();
            spn_category.setSelection(setID);
        }

    }

    public void addminority(Spinner spn_minority) {
        if(_lang.equalsIgnoreCase("en")) {
            minoritytlist.add("-All-");
            minoritytlist.add("Yes");
            minoritytlist.add("No");
        }
        else
        {
            minoritytlist.add("-सभी-");
            minoritytlist.add("हाँ");
            minoritytlist.add("नहीं");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, minoritytlist);
        spn_minority.setAdapter(dataAdapter);

        if(str_minority.equalsIgnoreCase("Y"))
        {
            spn_minority.setSelection(1);
        }
        else  if(str_minority.equalsIgnoreCase("N"))
        {
            spn_minority.setSelection(2);
        }

    }

    public void adddivyang(Spinner spn_divyang ) {
        if(_lang.equalsIgnoreCase("en")) {
            divyanglist.add("-All-");
            divyanglist.add("Yes");
            divyanglist.add("No");
        }
        else
        {
            divyanglist.add("-सभी-");
            divyanglist.add("हाँ");
            divyanglist.add("नहीं");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, divyanglist);
        spn_divyang.setAdapter(dataAdapter);
        if(str_divyang.equalsIgnoreCase("Y"))
        {
            spn_divyang.setSelection(1);
        }
        else  if(str_divyang.equalsIgnoreCase("N"))
        {
            spn_divyang.setSelection(2);
        }

    }
    public void addNoneValueField(Spinner spn_HavingNonValue ) {
        /*
        AadharCardNo
AccountNo
StdRegNum
         */
        if(_lang.equalsIgnoreCase("en")) {
            havingnonevaluelist.add("-None-");
            havingnonevaluelist.add("Aadhar Number");
            havingnonevaluelist.add("Account Number");
            havingnonevaluelist.add("Admission Number");
            havingnonevaluelist.add("IFSC Code");
        }
        else
        {
            havingnonevaluelist.add("-कोई नहीं-");
            havingnonevaluelist.add("आधार संख्या");
            havingnonevaluelist.add("खाता संख्या");
            havingnonevaluelist.add("प्रवेश संख्या");
            havingnonevaluelist.add("IFSC कोड");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, havingnonevaluelist);
        spn_HavingNonValue.setAdapter(dataAdapter);
        if(str_none_value.equalsIgnoreCase("AadharCardNo"))
        {
            spn_HavingNonValue.setSelection(1);
        }
        else  if(str_none_value.equalsIgnoreCase("AccountNo"))
        {
            spn_HavingNonValue.setSelection(2);
        }
        else  if(str_none_value.equalsIgnoreCase("StdRegNum"))
        {
            spn_HavingNonValue.setSelection(3);
        }
        else  if(str_none_value.equalsIgnoreCase("IFSC"))
        {
            spn_HavingNonValue.setSelection(4);
        }
    }
    public void adddbpl(Spinner spn_bpl) {
        if(_lang.equalsIgnoreCase("en")) {
            bpllist.add("-All-");
            bpllist.add("Yes");
            bpllist.add("No");
        }
        else {
            bpllist.add("-सभी-");
            bpllist.add("हाँ");
            bpllist.add("नहीं");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, bpllist);
        spn_bpl.setAdapter(dataAdapter);
        if(str_bpl.equalsIgnoreCase("Y"))
        {
            spn_bpl.setSelection(1);
        }
        else  if(str_bpl.equalsIgnoreCase("N"))
        {
            spn_bpl.setSelection(2);
        }
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(AttendanceListActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig)
//    {
//        Log.d("tag", "config changed");
//        super.onConfigurationChanged(newConfig);
//
//        int orientation = newConfig.orientation;
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.d("tag", "Portrait");
//            setContentView(R.layout.activity_attendancelist);
//            reload();
//        }
//        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.d("tag", "Landscape");
//            setContentView(R.layout.activity_attendancelistland);
//            reload();
//        }
//        else
//            Log.w("tag", "other: " + orientation);
//
//    }
//
//    public void reload()
//    {
//
//        if(_lang==null)
//        {
//            _lang= "en";
//        }
//        else if(_lang=="")
//        {
//            _lang= "en";
//        }
//
//        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
//
//        Cursor cur=helper.getUserRegisteredDetails();
//        if(cur.getCount()>0)
//        {
//            while(cur.moveToNext())
//            {
//                _schoolName = cur.getString(cur.getColumnIndex("School_Name"))+" SCHOOL";
//                String ditsblok=cur.getString(cur.getColumnIndex("District_Name"))+  ", "+cur.getString(cur.getColumnIndex("Block_Name"));
//                _distBlock = ditsblok;
//            }
//        }
//
//        searchStdList();
//
//        if(_lang.equalsIgnoreCase("en"))
//        {
//            spinnerMarkedAdapter=new ArrayAdapter(this,R.layout.dropdownlist,MarkedList);
//        }
//        else
//        {
//            spinnerMarkedAdapter=new ArrayAdapter(this,R.layout.dropdownlist,MarkedListHn);
//        }
//        spn_marked.setAdapter(spinnerMarkedAdapter);
//
//        setButtonsAndOtherLabels(_lang);
//
//        loadClassList();
//        loadSessionList();

    //    }
    public  void showinstructions()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceListActivity.this);
        ab.setIcon(R.drawable.usermanual);

        //String green="If the checkbox is checked and beneficiary name is written in&nbsp" + "<font color='#006400'><b>GREEN</b></font>" + "&nbsp colour , it means attendance is above 75%."+"<Br><Br>";
        String green="If the  beneficiary name is written in&nbsp" + "<font color='#006400'><b>GREEN</b></font>" + "&nbsp colour , it means attendance is above 75%."+"<Br><Br>";
        String greenhindi="अगर चेकबॉक्स चेक्ड है और लाभार्थी का नाम "+"<font color='#006400'><b>हरे</b></font>"+" रंग में लिखा है तो उपस्थिति 75% से ऊपर है "+"<Br><Br>";
      //  String red="If the checkbox is unchecked and beneficiary name is written in&nbsp" + "<font color='#8B0000'><b>RED</b></font>" + "&nbsp colour , it means attendance is below 75%."+"<Br><Br>";
        String red="If  beneficiary name is written in&nbsp" + "<font color='#8B0000'><b>RED</b></font>" + "&nbsp colour , it means attendance is below 75%."+"<Br><Br>";
        String redhindi="अगर चेकबॉक्स चेक्ड  नहीं है और लाभार्थी का नाम " + "<font color='#8B0000'><b>लाल</b></font>" + "  रंग में लिखा है तो उपस्थिति 75% से नीचे है "+"<Br><Br>";
       // String black=" If the checkbox is unchecked and beneficiary name is written in&nbsp " + "<font color='#000000'><b>BLACK</b></font>" + "&nbsp colour , it means attendance is unmarked till now.";
        String black=" If the  beneficiary name is written in&nbsp " + "<font color='#000000'><b>BLACK</b></font>" + "&nbsp colour , it means attendance is unmarked till now.";
        String blackhindi=" अगर चेकबॉक्स चेक्ड  नहीं है और लाभार्थी का नाम " + "<font color='#000000'><b>काले</b></font>" + " रंग में लिखा है तो उपस्थिति मार्क नही की गयी है";

        if(_lang.equalsIgnoreCase("en"))
        {

            ab.setTitle("Please Note:-");
            // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
            ab.setMessage(Html.fromHtml("1)."+green+" 2)."+red+" 3)."+black+" "));
            ab.setNegativeButton("[ OK ]",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog,int whichButton)
                {
                    dialog.dismiss();
                }
            });
            ab.show();
        }
        else
        {
            ab.setTitle("कृपया ध्यान दें");
            ab.setMessage(Html.fromHtml("1)."+greenhindi+" 2)."+redhindi+" 3)."+blackhindi+""));
            ab.setPositiveButton("[ ओके ]",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog,int whichButton)
                {
                    dialog.dismiss();
                }
            });

            ab.show();
        }
    }


}

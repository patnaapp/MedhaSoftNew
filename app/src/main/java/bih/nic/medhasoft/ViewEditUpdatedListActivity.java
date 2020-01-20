package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import java.util.Date;

import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.Model.checkboxitem;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;

public class ViewEditUpdatedListActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog1;
    public static ProgressDialog pd2;

    String COLLRegNo="";
    VIewEditListAdapter studentListAdaptor;
    private final static int CAMERA_PIC = 99;
    ListView list_student;
    //Button btnSave,btnUpload;
    //ImageView btnSave,btnUpload;
    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();
    ArrayList<checkboxitem>checkbox=new ArrayList<>();
    String USERID="";
    ArrayList<String> spinnerDataList1;
    private static String version = "0";
    //Spinner spClass,spSession,spn_fyear;
    public static String spn1value="";
    public static String TotnnoClasses="";
    EditText edt_totclassees;
    TextView txtheader;
    String COLLNAME="";

    Intent imageData1;
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
    String _outp="0";

    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    String _varFyear_Name="All",_varFyear_NameHn="सभी",_varFYear_Id="0";


    //------------------
    TextView txtHeaderName;
//    TextView txtSection;
//    TextView txtFyear;
//    TextView txtClass;
    TextView tvattendence;
    TextView txt_FName;
    TextView txt_Name;
    TextView txt_ANum;

    String timeStamp;
    TextView showpdfdata;
    TextView txtPDFHeader;
    WebView showpdfdataWV;
    String _schoolName="",_distBlock="";
    String _filePrefixName="medhasoft";
    ImageView imgFilter,imgPDF;
    String _lang="en";
    LinearLayout lnFilterPDF;

    TextView txtStdCountEdit;

    public static final String HINDI_FONT = "FreeSans.ttf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieweditlist);


        //lnHeading= findViewById(R.id.lnHeading);
        imgFilter =  findViewById(R.id.imgFilter);
        imgPDF =  findViewById(R.id.imgPDF);
        txtStdCountEdit =findViewById(R.id.txtStdCountVE);
        lnFilterPDF =findViewById(R.id.lnFilterPDF);

        localDB = new DataBaseHelper(this);
        db = localDB.getWritableDatabase();


        tvattendence =(TextView)findViewById(R.id.tvattendence);

        //        tvattendence.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), EditBendDetailsActivity.class);
//                startActivity(i);
//
//            }
//        });

        txtStdCountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(ViewEditUpdatedListActivity.this, "Total Student: " + txtStdCountEdit.getText(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ViewEditUpdatedListActivity.this, "कुल छात्र: " + txtStdCountEdit.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        list_student=(ListView)findViewById(R.id.list_student);
//        spClass=(Spinner)findViewById(R.id.spn_class);
//        spSession=(Spinner)findViewById(R.id.spn_session);
//        spn_fyear=(Spinner)findViewById(R.id.spn_fyear);

        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);

//        txtSection = (TextView) findViewById(R.id.txtSection);
//        txtFyear = (TextView) findViewById(R.id.txtFyear);
//        txtClass = (TextView) findViewById(R.id.txtClass);

        txt_FName = (TextView) findViewById(R.id.txt_FName);
        txt_Name = (TextView) findViewById(R.id.txt_Name);
        txt_ANum = (TextView) findViewById(R.id.txt_ANum);


        showpdfdata=(TextView)findViewById(R.id.showpdfdata); //-------------------------
        txtPDFHeader=(TextView)findViewById(R.id.txtPDFHeader); //-------------------------
        showpdfdataWV=findViewById(R.id.showpdfdataWV);

        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);
        _diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        _outp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("OTP", "");




        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }
        list_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentList country = (studentList) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Clicked : " + country.getStdname() + " Ben ID " + country.getStdbenid(), Toast.LENGTH_LONG).show();
            }
        });



//        spn_fyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                if (arg2 > 0) {
//
//                    FYEAR fyear = FYearList.get(arg2 - 1);
//                    _varFYear_Id = fyear.getFYearID();
//                    _varFyear_Name = fyear.getFYearValue();
//                    _varFyear_NameHn = fyear.getFYearValue();
//                    searchStdList();
//
//                } else {
//                    _varFYear_Id = "0";
//                    _varFyear_Name = "All";
//                    _varFyear_NameHn = "All";
//                    searchStdList();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });
//
//        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                if (arg2 > 0) {
//
//                    CLASSLIST currentPhysical = ClassList.get(arg2 - 1);
//                    _varClass_Id = currentPhysical.getClassID();
//                    _varClass_Name = currentPhysical.getClassName();
//
//                    searchStdList();
////                    new LoadAttendance(_diseCode,_varClass_Id).execute();
//                } else {
//                    _varClass_Id = "0";
//                    _varClass_Name = "All";
//                    searchStdList();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });
//
//        spSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                if (arg2 > 0) {
//
//                    SESSIONLIST currentPhysical = SessionList.get(arg2 - 1);
//                    _varSession_Id = currentPhysical.getSessionID();
//                    _varSession_Name = currentPhysical.getSessionName();
//                    searchStdList();
//
//                } else {
//                    _varSession_Id = "0";
//                    _varSession_Name = "None";
//                    searchStdList();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });

        setButtonsAndOtherLabels(_lang);
//        loadClassList();
//        loadSessionList();
//        loadFYearList();

        searchStdList();

        //     //HIDE FILTER SCREEN
       //lnHeading.setVisibility(View.GONE);
       // txtSection.setVisibility(View.GONE);
       //  spSession.setVisibility(View.GONE);
       // imgFilter.setVisibility(View.GONE);

        lnFilterPDF.setVisibility(View.GONE);
        imgPDF =  findViewById(R.id.imgPDF);
        imgPDF.setVisibility(View.GONE);
    }

    public void loadClassList()
    {
        localDB = new DataBaseHelper(ViewEditUpdatedListActivity.this);
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
            //spClass.setAdapter(ClassListadapter);
        }
        else {

            if (Utiilties.isOnline(ViewEditUpdatedListActivity.this)) {
                Toast.makeText(this, "Please wait downloading class", Toast.LENGTH_SHORT).show();
                new LoadClassList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
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
        localDB = new DataBaseHelper(ViewEditUpdatedListActivity.this);
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
            //spSession.setAdapter(SessionListadapter);
        }

        else {


            if (Utiilties.isOnline(ViewEditUpdatedListActivity.this)) {
                Toast.makeText(this, "Please wait downloading sections", Toast.LENGTH_SHORT).show();
                new LoadSessionList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
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
    public void loadFYearList()
    {
        localDB = new DataBaseHelper(ViewEditUpdatedListActivity.this);
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
            int setID=0;
            for(int i=0;i<FYearList.size();i++)
            {
                StringList.add(FYearList.get(i).getFYearValue());
                if(_varSession_Id.equalsIgnoreCase(FYearList.get(i).getFYearID()))
                {
                    setID=(i+1);
                }
            }
            FYearListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
           // spn_fyear.setAdapter(FYearListadapter);
        }

        else {


            if (Utiilties.isOnline(ViewEditUpdatedListActivity.this)) {
                Toast.makeText(this, "Please wait downloading Financial Year List", Toast.LENGTH_SHORT).show();
                new LoadFYearList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
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
    private class LoadFYearList extends AsyncTask<String, Void, ArrayList<FYEAR>> {

        @Override
        protected void onPreExecute() {
            try {

                pd2 = new ProgressDialog(ViewEditUpdatedListActivity.this);
                pd2.setTitle("Downloading FYear...");
                pd2.setCancelable(false);
                pd2.show();

            } catch (Exception e) {

            }
        }

        @Override
        protected ArrayList<FYEAR> doInBackground(String... param) {

            return WebServiceHelper.GetFYearList();
        }

        @Override
        protected void onPostExecute(ArrayList<FYEAR> result) {
            if (pd2.isShowing()) {

                pd2.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("FYear", "" + result);
                    DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                    long i = helper.setFinancialYearList(result);

                    if (i > 0) {
                        Toast.makeText(ViewEditUpdatedListActivity.this, "FYear Data downloaded...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ViewEditUpdatedListActivity.this, "not inserted FYear data", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found for FYear data", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    public void loadGenderList()
    {
        localDB = new DataBaseHelper(ViewEditUpdatedListActivity.this);
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
           // spSession.setAdapter(SessionListadapter);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        //searchStdList();
    }


    private class LoadClassList extends AsyncTask<String, Void, ArrayList<CLASSLIST>> {

        @Override
        protected void onPreExecute() {
            pd2 = new ProgressDialog(ViewEditUpdatedListActivity.this);
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
                        Toast.makeText(ViewEditUpdatedListActivity.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadClassList();
                        }
                    }
                    else{
                        Toast.makeText(ViewEditUpdatedListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
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
            pd2 = new ProgressDialog(ViewEditUpdatedListActivity.this);
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
                        Toast.makeText(ViewEditUpdatedListActivity.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadSessionList();
                        }
                    }
                    else{
                        Toast.makeText(ViewEditUpdatedListActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void chk_tamilasucess(String msg) {
        // final String wantToUpdate;
        AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
        ab.setCancelable(false);
        // ab.setIcon(R.drawable.bedicon);
        ab.setTitle("Upload Success");
        ab.setMessage(msg);
        Dialog dialog = new Dialog(ViewEditUpdatedListActivity.this);
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



    public void onClick_SyncClass(View v)
    {
        if (Utiilties.isOnline(ViewEditUpdatedListActivity.this)) {
            Toast.makeText(this, "Please wait downloading...", Toast.LENGTH_SHORT).show();
            // new LoadClassList().execute();
            if(_varClass_Id.equalsIgnoreCase("0"))
            {
                Toast.makeText(this, "Please select Class", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // new LoadAttendance(_diseCode,_varClass_Id).execute();
            }

        }
        else {

            AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
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
    public void onClick_SyncSession(View v)
    {
        if (Utiilties.isOnline(ViewEditUpdatedListActivity.this)) {
            Toast.makeText(this, "Please wait downloading...", Toast.LENGTH_SHORT).show();
            new LoadSessionList().execute();
        }
        else {

            AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
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

    public  void onClick_LoadStudentList(View v)
    {
        searchStdList();
    }


    public void searchStdList()
    {
        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
        //studentList = helper.getStudentListForAttendance(_diseCode,_varClass_Id,_varSession_Id);
        studentList = helper.getUpdatedStudentDetaislForClassSectionFYear(_diseCode,_varClass_Id,_varSession_Id,_varFYear_Id);
        if(studentList.size()>0)
        {
            studentListAdaptor = new VIewEditListAdapter(ViewEditUpdatedListActivity.this,"EU");
            studentListAdaptor.upDateEntries(studentList);
            list_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            txtStdCountEdit.setText(String.valueOf(studentList.size()));
            if(!_varClass_Name.equalsIgnoreCase("")) {
                //Toast.makeText(this, studentList.size() + " records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            studentListAdaptor = new VIewEditListAdapter(ViewEditUpdatedListActivity.this,"EU");
            list_student.setAdapter(studentListAdaptor);
            studentListAdaptor.notifyDataSetChanged();
            txtStdCountEdit.setText(String.valueOf(studentList.size()));
            if(!_varClass_Name.equalsIgnoreCase("")) {
                if(_lang.equalsIgnoreCase("en")) {
                    Toast.makeText(this, "Sorry! no records found for class " + _varClass_Name + " section " + _varSession_Name, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "माफ़ कीजिये! कोई रिकॉर्ड नहीं मिला कक्षा " + _varClass_Name + " सेक्शन " + _varSession_Name, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onClick_GeneratePDF(View v)
    {
        if(txtStdCountEdit.getText().toString().equalsIgnoreCase("0"))
        {
            if(_lang.equalsIgnoreCase("en")) {
                Toast.makeText(this, "Sorry! no records for Report", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "माफ़ कीजिये! कोई रिकॉर्ड नहीं ", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditUpdatedListActivity.this);
            if (_lang.equalsIgnoreCase("en")) {
                ab.setTitle(R.string.report_title);
                ab.setMessage(R.string.report_msg);
            } else {
                ab.setTitle(R.string.report_titlehn);
                ab.setMessage(R.string.report_msghn);
            }
            ab.setPositiveButton("[ English ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            try {
                                if (progressDialog1.isShowing()) {
                                    progressDialog1.dismiss();
                                }
                                progressDialog1.show();
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
                                if (progressDialog1.isShowing()) {
                                    progressDialog1.dismiss();
                                }
                                progressDialog1.show();
                                createWebViewContaintsForPDF();
                            } catch (Exception e) {
                                if (progressDialog1.isShowing()) {
                                    progressDialog1.dismiss();
                                }
                                e.printStackTrace();
                            }

                        }
                    });
            ab.show();
        }

    }

    private void createPdfviewlocal(String lang) throws IOException, DocumentException {

        progressDialog1.setMessage("Please wait. Genreating PDF file");
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        Font f;
        writeFont();
        //Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        //BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        //String targetPdf = "/sdcard/medhasoft"+timeStamp+".pdf";
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
        document.setPageSize(PageSize.A4);
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
        PdfPTable tableC = new PdfPTable(new float[] { 5,5, 3, 3,3,3,4 });
        tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        if(lang.equalsIgnoreCase("en")) {

            tableC.addCell(new Phrase("ADM.NO.", fonthdr));
            tableC.addCell(new Phrase("BENEFICIARY", fonthdr));
            tableC.addCell(new Phrase("FATHER NAME", fonthdr));
            tableC.addCell(new Phrase("A.DATE", fonthdr));
            tableC.addCell(new Phrase("CLASS", fonthdr));
            tableC.addCell(new Phrase("DoB", fonthdr));
            tableC.addCell(new Phrase("MOBILE", fonthdr));
        }
        else if(lang.equalsIgnoreCase("hn")) {

            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


            tableC.addCell(new Phrase("प्रवेश संख्या", f));
            tableC.addCell(new Phrase("लाभार्थी", f));
            tableC.addCell(new Phrase("पिता का नाम", f));
            tableC.addCell(new Phrase("प्रवेश तिथि", f));
            tableC.addCell(new Phrase("कक्षा", f));
            tableC.addCell(new Phrase("जन्म तिथि", f));
            tableC.addCell(new Phrase("मोबाइल", f));
        }
        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);

        }
        if(lang.equalsIgnoreCase("en")) {
            if (studentList != null) {
                if (studentList.size() > 0) {
                    for (int x = 0; x < studentList.size(); x++) {


                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdadmnum()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdname()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdfname()));

                        String y, m, da, addate;
                        String admdate = studentList.get(x).getStdadmdate() != "anyType{}" ? studentList.get(x).getStdadmdate() : "";
                        addate = admdate;
                        try {
                            if (admdate != null) {
                                if (!admdate.equalsIgnoreCase("")) {

                                    y = admdate.substring(0, 4);
                                    m = admdate.substring(4, 6);
                                    da = admdate.substring(6, 8);
                                    addate = da + "-" + m + "-" + y;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }
                        tableC.addCell(addate);

                        if (studentList.get(x).getStdsession().equalsIgnoreCase("")) {
                            tableC.addCell(studentList.get(x).getStdclassid());
                        } else {
                            tableC.addCell(studentList.get(x).getStdclassid() + " (" + studentList.get(x).getStdsession() + ")");
                        }

                        String y1, m1, d1, tdate;
                        String dobBen = studentList.get(x).getStdDOB() != "anyType{}" ? studentList.get(x).getStdDOB() : "";
                        tdate = dobBen;
                        try {
                            if (dobBen != null) {
                                if (!dobBen.equalsIgnoreCase("")) {

                                    y1 = dobBen.substring(0, 4);
                                    m1 = dobBen.substring(4, 6);
                                    d1 = dobBen.substring(6, 8);
                                    tdate = d1 + "-" + m1 + "-" + y1;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }

                        tableC.addCell(tdate);
                        tableC.addCell(studentList.get(x).getStdmobile());
                    }
                }
            }
        }

        document.add(tableC);

        document.add(new Chunk("\n"));


        //Step 5: Close the document
        document.close();

        if(progressDialog1.isShowing())
        {
            progressDialog1.dismiss();
        }
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
                Toast.makeText(ViewEditUpdatedListActivity.this, "Please Download PDF Viewer", Toast.LENGTH_LONG).show();
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
            Uri uri = FileProvider.getUriForFile(ViewEditUpdatedListActivity.this,
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
                Toast.makeText(ViewEditUpdatedListActivity.this, "Please Download PDF  Viewer", Toast.LENGTH_LONG).show();
            }
        }
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
    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

//            txtSection.setText("SECTION");
//            txtFyear.setText("F.YEAR");
//            txtClass.setText("CLASS");

            txt_FName.setText("FATHER NAME");
            txt_Name.setText("NAME");
            txt_ANum.setText("ADM.NO");

            txtHeaderName.setText(R.string.app_name);

            tvattendence.setText("View-Edit Student Details");

        }
        else
        {
//            txtSection.setText("सेक्शन");
//            txtFyear.setText("वित्तीय वर्ष");
//            txtClass.setText("कक्षा");

            txt_FName.setText("पिता का नाम");
            txt_Name.setText("नाम");
            txt_ANum.setText("प्र. संख्या");

            txtHeaderName.setText(R.string.app_namehn);
            tvattendence.setText("छात्र विवरण देखें-संपादित करें");

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


        htmlTable+="<th style='border: thin solid #000000; width:11%'>प्रवेश संख्या</th>";
        htmlTable+="<th style='border: thin solid #000000; width:22%'>लाभार्थी</th>";
        htmlTable+="<th style='border: thin solid #000000; width:22%'>पिता का नाम</th>";
        htmlTable+="<th style='border: thin solid #000000; width:12%'>प्रवेश तिथि</th>";
        htmlTable+="<th style='border: thin solid #000000; width:10%'>कक्षा</th>";
        htmlTable+="<th style='border: thin solid #000000; width:11%'>जन्म तिथि</th>";
        htmlTable+="<th style='border: thin solid #000000; width:12%'>मोबाइल</th>";
        htmlTable+="</tr>";


        int countLess75Per=0;
        if(studentList!=null)
        {
            if(studentList.size()>0)
            {

                for(int x=0;x<studentList.size();x++) {
                    htmlTable+="<tr>";


                    htmlTable += "<th style='border: thin solid #000000; width:11%'>"+studentList.get(x).getStdadmnum()+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+studentList.get(x).getStdnamehn()+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+studentList.get(x).getStdfnamehn()+"</th>";
                    String y, m, da, addate;
                    String admdate = studentList.get(x).getStdadmdate() != "anyType{}" ? studentList.get(x).getStdadmdate() : "";
                    addate = admdate;
                    try {
                        if (admdate != null) {
                            if (!admdate.equalsIgnoreCase("")) {

                                y = admdate.substring(0, 4);
                                m = admdate.substring(4, 6);
                                da = admdate.substring(6, 8);
                                addate = da + "-" + m + "-" + y;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EXCEPTION", "date2 exception");
                    }
                    htmlTable += "<th style='border: thin solid #000000; width:12%'>"+addate+"</th>";

                    if (studentList.get(x).getStdsession().equalsIgnoreCase("")) {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+studentList.get(x).getStdclassid()+"</th>";
                    } else {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+studentList.get(x).getStdclassid() + " (" + studentList.get(x).getStdsession() + ")"+"</th>";
                    }

                    String y1, m1, d1, tdate;
                    String dobBen = studentList.get(x).getStdDOB() != "anyType{}" ? studentList.get(x).getStdDOB() : "";
                    tdate = dobBen;
                    try {
                        if (dobBen != null) {
                            if (!dobBen.equalsIgnoreCase("")) {
                                y1 = dobBen.substring(0, 4);
                                m1 = dobBen.substring(4, 6);
                                d1 = dobBen.substring(6, 8);
                                tdate = d1 + "-" + m1 + "-" + y1;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EXCEPTION", "date2 exception");
                    }

                    htmlTable += "<th style='border: thin solid #000000; width:11%'>"+tdate+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:12%'>"+studentList.get(x).getStdmobile()+"</th>";

                    htmlTable+="</tr>";
                }
            }
        }
        htmlTable+="</table><br><br>";

        WebSettings webSettings = showpdfdataWV.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");

        showpdfdataWV.setWebViewClient(new myWebClient());


        String myHtmlString=htmlTableH+htmlTable;//+htmlTable2;

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

        final ProgressDialog progressDialog=new ProgressDialog(ViewEditUpdatedListActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ViewEditUpdatedListActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(ViewEditUpdatedListActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void setCount(int cnt)
    {
        txtStdCountEdit.setText(String.valueOf(cnt));

//        if(cnt==0)
//        {
//            imgPDF.setVisibility(View.GONE);
//        }
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(ViewEditUpdatedListActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public  void onClick_MoreFilter(View v)
    {

    }
}

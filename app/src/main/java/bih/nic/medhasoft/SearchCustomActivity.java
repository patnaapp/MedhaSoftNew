package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
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

import bih.nic.medhasoft.Adapter.SearchAdapter;
import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.Utiilties;

public class SearchCustomActivity extends AppCompatActivity {

    //VIewEditListAdapter studentListAdaptor;
    SearchAdapter studentListAdaptor;
    ListView list_student;
    Cursor list_student_cursor;

    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();

    public static ProgressDialog pd2;
    public static ProgressDialog progressDialog1;

    Spinner spClass,spSession,spn_fyear;
    DataBaseHelper localDB ;
    SQLiteDatabase db;

    ArrayList<CLASSLIST> ClassList = new ArrayList<>();
    ArrayAdapter<String> ClassListadapter;
    String _varClass_Name="All",_varClass_Id="0";
    String _varClass_NameHn="सभी";

    ArrayList<SESSIONLIST> SessionList = new ArrayList<>();
    ArrayAdapter<String> SessionListadapter;
    String _varSession_Name="All",_varSession_Id="0";
    String _varSession_NameHn="सभी";


    ArrayAdapter<String> GenderListadapter;
    String _varGender_Name="All",_varGender_Id="0";
    String _varGender_NameHn="सभी";

    String _diseCode="0";
    String _outp="0";

    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    String _varFyear_Name="All",_varFYear_Id="0";
    String _varFyear_NameHn="सभी";
    String _lang="en";
    ArrayList<Genderlist> genderlists = new ArrayList<>();
    String _varCategory_Name="all",_varCategory_NameHn="all",_varCategory_Id="0";

    ArrayList<categoryHINDI> catlists = new ArrayList<>();
    ArrayAdapter<String> catListadapter;
    ArrayList<String> minoritytlist;
    ArrayList<String> benstatuslist;
    ArrayList<String> divyanglist;
    ArrayList<String> havingnonevaluelist;
    ArrayList<String> bpllist;
    ArrayList<String> atnlist;
    String str_minority="0",str_divyang="0",str_bpl="0",str_none_value="0";
    String str_benstatus="0",str_atn="";
    TextView txtStdCountVE,txtSelectFiled;

    //-----FILTER------------------
    Spinner spn_benstatus;
    Spinner spnCategory;
    Spinner spnIsMinority;
    Spinner spnGender;
    Spinner spnIsBPL;
    Spinner spnIsHandicaped;
    Spinner spnHavingNonValue;
    Spinner spnAttendance;
    EditText txtStdNameForSearch;
    TextView txtAdnStdCount,txtTotalStdLbl;
    TextView txtHeaderName,tvSUbHeader;
    String std_name;


    String timeStamp;
    String _filePrefixName="medhasoft";

    TextView showpdfdata;
    TextView txtPDFHeader;
    WebView showpdfdataWV;
    String _schoolName="",_distBlock="";

    ImageView imgFilter;
    public static final String HINDI_FONT = "FreeSans.ttf";
    String SQLSelectQuery="";
    String ColumnList="";

    CheckBox StdRegNum,StdName,StdClass,StdSession,StdDoB,StdMobNum;
    CheckBox BenID,StdCategoryName,EmailId,StdGender,Addmissiondate,AadharCardNo;
    CheckBox IsAadhaarVerified,IsMinority,IsHandicapped,AccountNo,AccountHolderName;
    CheckBox AccountHolderType_Name,AttSeventyFivePercent,BenificiaryStatus;
    CheckBox StdFName,StdMName;
    TextView txt_FName;
    TextView txt_Name;
    TextView txt_ANum;
    String reportLable="";

    long num_of_field_selected=3;  // by default:
    Button btnReadText;
    private static final int RC_OCR_CAPTURE = 9003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        localDB = new DataBaseHelper(this);
        db = localDB.getWritableDatabase();
        list_student=(ListView)findViewById(R.id.list_student);

        txtStdCountVE=findViewById(R.id.txtStdCountVE);
        txtHeaderName=findViewById(R.id.txtHeaderName);
        tvSUbHeader=findViewById(R.id.tvSUbHeader);

        txt_FName = (TextView) findViewById(R.id.txt_FName);
        txt_Name = (TextView) findViewById(R.id.txt_Name);
        txt_ANum = (TextView) findViewById(R.id.txt_ANum);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);

        showpdfdata=(TextView)findViewById(R.id.showpdfdata); //-------------------------
        txtPDFHeader=(TextView)findViewById(R.id.txtPDFHeader); //-------------------------
        showpdfdataWV=findViewById(R.id.showpdfdataWV);


        _diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        _outp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("OTP", "");

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


        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }

        MoreFilter();
        // getAdditionalCustomSearchResult(SQLSelectQuery);
        setLabelsTextAsPerLanguage();
    }
    public void onClick_AdvSearch(View v)
    {
        ColumnList=null;
        MoreFilter();
    }

    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(SearchCustomActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void loadClassList(Spinner spCls)
    {
        localDB = new DataBaseHelper(SearchCustomActivity.this);
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
                Log.e("_varClass_Id",_varClass_Id);
                Log.e("setID",""+setID);
            }

            ClassListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spCls.setAdapter(ClassListadapter);
            ClassListadapter.notifyDataSetChanged();
            spCls.setSelection(setID);
        }
    }

    public void loadSessionList(Spinner spSess)
    {
        localDB = new DataBaseHelper(SearchCustomActivity.this);
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
            spSess.setAdapter(SessionListadapter);
            SessionListadapter.notifyDataSetChanged();
            spSess.setSelection(setID);
        }
    }
    public void loadFYearList(Spinner spfyr)
    {
        localDB = new DataBaseHelper(SearchCustomActivity.this);
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
            spfyr.setAdapter(FYearListadapter);

            int setID=0;
            for(int i=0;i<FYearList.size();i++)
            {
                StringList.add(FYearList.get(i).getFYearValue());
                if(_varFYear_Id.equalsIgnoreCase(FYearList.get(i).getFYearID()))
                {
                    setID=(i+1);
                }
                spfyr.setSelection(setID);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAdditionalCustomSearchResult(SQLSelectQuery);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    // statusMessage.setText(R.string.ocr_success);
                    txtStdNameForSearch.setText(text);
                    Log.d("XX", "Text read: " + text);
                } else {
                    // statusMessage.setText(R.string.ocr_failure);
                    Log.d("XX", "No Text captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void MoreFilter()
    {

        //*StdRegNum,StdName,StdSession,StdClass,

        if(ColumnList!=null)
        {
            if(ColumnList.length()>1)
            {
                ColumnList=ColumnList.substring(0,ColumnList.length()-1);
            }
        }


        SQLSelectQuery="SELECT "+ ColumnList +" FROM StudentListForAttendance WHERE ";
        String msg="";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.additionalfiltermore, null);

        dialogBuilder.setView(dialogView);

        spn_benstatus=(Spinner) dialogView.findViewById(R.id.spn_benstatus);
        spClass=(Spinner) dialogView.findViewById(R.id.spn_class);
        spSession=(Spinner) dialogView.findViewById(R.id.spn_session);
        spn_fyear=(Spinner) dialogView.findViewById(R.id.spn_fyear);
        spnCategory = (Spinner) dialogView.findViewById(R.id.spnCategory);
        spnIsMinority = (Spinner) dialogView.findViewById(R.id.spnIsMinority);
        spnGender = (Spinner) dialogView.findViewById(R.id.spnGender);
        spnIsBPL = (Spinner) dialogView.findViewById(R.id.spnIsBPL);
        spnIsHandicaped = (Spinner) dialogView.findViewById(R.id.spnIsHandicaped);
        spnHavingNonValue = (Spinner) dialogView.findViewById(R.id.spnHavingNonValue);
        spnAttendance = (Spinner) dialogView.findViewById(R.id.spnAttendance);
        txtStdNameForSearch =  dialogView.findViewById(R.id.txtStdNameForSearch);
        txtAdnStdCount =  dialogView.findViewById(R.id.txtAdnStdCount);
        txtTotalStdLbl =  dialogView.findViewById(R.id.txtTotalStdLbl);
        txtSelectFiled=dialogView.findViewById(R.id.txtSelectFiled);
        benstatuslist=new ArrayList<String>();
        minoritytlist=new ArrayList<String>();
        divyanglist=new ArrayList<String>();
        havingnonevaluelist=new ArrayList<String>();
        bpllist=new ArrayList<String>();
        atnlist=new ArrayList<String>();

        StdRegNum=dialogView.findViewById(R.id.StdRegNum);
        StdName=dialogView.findViewById(R.id.StdName);
        StdFName=dialogView.findViewById(R.id.StdFName);
        StdMName=dialogView.findViewById(R.id.StdMName);

        StdClass=dialogView.findViewById(R.id.StdClass);
        StdSession=dialogView.findViewById(R.id.StdSession);
        StdDoB=dialogView.findViewById(R.id.StdDoB);
        StdMobNum=dialogView.findViewById(R.id.StdMobNum);

        BenID=dialogView.findViewById(R.id.BenID);
        StdCategoryName=dialogView.findViewById(R.id.StdCategoryName);
        EmailId=dialogView.findViewById(R.id.EmailId);
        StdGender=dialogView.findViewById(R.id.StdGender);
        Addmissiondate=dialogView.findViewById(R.id.Addmissiondate);
        AadharCardNo=dialogView.findViewById(R.id.AadharCardNo);

        IsAadhaarVerified=dialogView.findViewById(R.id.IsAadhaarVerified);
        IsMinority=dialogView.findViewById(R.id.IsMinority);
        IsHandicapped=dialogView.findViewById(R.id.IsHandicapped);
        AccountNo=dialogView.findViewById(R.id.AccountNo);
        AccountHolderName=dialogView.findViewById(R.id.AccountHolderName);

        AccountHolderType_Name=dialogView.findViewById(R.id.AccountHolderType_Name);
        AttSeventyFivePercent=dialogView.findViewById(R.id.AttSeventyFivePercent);
        BenificiaryStatus=dialogView.findViewById(R.id.BenificiaryStatus);
        btnReadText=dialogView.findViewById(R.id.btnReadText);


        addBenStatus(spn_benstatus);
        loadClassList(spClass);
        loadSessionList(spSession);
        loadFYearList(spn_fyear);
        loadGenderList(spnGender);
        loadCategoryHNList(spnCategory);
        addminority(spnIsMinority);
        adddivyang(spnIsHandicaped);
        adddbpl(spnIsBPL);
        addNoneValueField(spnHavingNonValue);
        addAttendance(spnAttendance);


        btnReadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                //intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

//        //DEFAULT FIELD
//        if(_lang.equalsIgnoreCase("en")) {
//            ColumnList = "StdRegNum AS [REG-NUM],StdName AS NAME,StdFName AS [F-NAME],StdRegNum AS [प्रवेश संख्या],StdNameHn AS लाभार्थी,StdFNameHn AS [पिता का नाम], ";
//        }
//        else
//        {
//            ColumnList = "StdRegNum AS [REG-NUM],StdName AS NAME,StdFName AS [F-NAME],StdRegNum AS [प्रवेश संख्या],StdNameHn AS लाभार्थी,StdFNameHn AS [पिता का नाम],";
//        }

        if(_lang.equalsIgnoreCase("en")) {
            ColumnList = "StdRegNum AS [REG-NUM],StdName AS NAME,StdFName AS [F-NAME],";
            txtSelectFiled.setText("Select Field (NOT MORE THEN 10) To Show On Report");
        }
        else
        {
            ColumnList = "StdRegNum AS [प्रवेश संख्या],StdNameHn AS लाभार्थी,StdFNameHn AS [पिता का नाम],";
            txtSelectFiled.setText("रिपोर्ट दिखाने के लिए फ़ील्ड (10 से अधिक नहीं) का चयन करें");
        }

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


        spn_benstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_benstatus =benstatuslist.get(arg2 );

                    // if(str_benstatus.equalsIgnoreCase("New Entry/Edited") || str_benstatus.equalsIgnoreCase("नई प्रविष्टि / संपादित"))
                    if(str_benstatus.equalsIgnoreCase("All") || str_benstatus.equalsIgnoreCase("सभी"))
                    {
                        str_benstatus="0";
                    }
//                    else if(str_benstatus.equalsIgnoreCase("Sent To PFMS For Verification") || str_benstatus.equalsIgnoreCase("सत्यापन के लिए पीएफएमएस को भेजा गया"))
//                    {
//                        str_benstatus="1";
//                    }
                    else if(str_benstatus.equalsIgnoreCase("Accepted") || str_benstatus.equalsIgnoreCase("स्वीकृत"))
                    {
                        str_benstatus="ACCP";
                    }
                    else if(str_benstatus.equalsIgnoreCase("Rejected") || str_benstatus.equalsIgnoreCase("अस्वीकृत"))
                    {
                        str_benstatus="RJCT";
                    }
                } else {
                    str_benstatus="0";
                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spn_fyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    FYEAR fyear = FYearList.get(arg2 - 1);
                    _varFYear_Id = fyear.getFYearID();
                    _varFyear_Name = fyear.getFYearValue();
                    _varFyear_NameHn = fyear.getFYearValue();

                } else {
                    _varFYear_Id = "0";
                    _varFyear_Name = "ALl";
                    _varFyear_NameHn = "All";
                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
                } else {
                    _varClass_Id = "0";
                    _varClass_Name = "All";
                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
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


                } else {
                    _varSession_Id = "0";
                    _varSession_Name = "All";

                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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
                getAdditionalCustomSearchResult(SQLSelectQuery);
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
                getAdditionalCustomSearchResult(SQLSelectQuery);
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

                    Genderlist currentPhysical = genderlists.get(arg2-1);
                    _varGender_Id = currentPhysical.getGenderID();
                    _varGender_Name = currentPhysical.getGenderName();
                    _varGender_NameHn = currentPhysical.getGenderNameHn();

                } else {
                    _varGender_Id = "0";
                    _varGender_Name = "";
                    _varGender_NameHn="";
                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
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
                getAdditionalCustomSearchResult(SQLSelectQuery);
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
                getAdditionalCustomSearchResult(SQLSelectQuery);
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
                getAdditionalCustomSearchResult(SQLSelectQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spnAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_atn = atnlist.get(arg2 );

                    if(str_atn.equalsIgnoreCase("< 75%"))
                    {
                        //str_atn="1";
                        str_atn="N";
                    }
                    else
                    {
                       // str_atn="0";
                        str_atn="Y";
                    }

                } else {
                    str_atn="";
                }
                getAdditionalCustomSearchResult(SQLSelectQuery);
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
                    getAdditionalCustomSearchResult(SQLSelectQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        //---CHECK BOX
        StdMName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    if (StdMName.isChecked()) {
                        ColumnList += "StdMName AS MOTHER,";
                        num_of_field_selected++;
                    } else {
                        ColumnList = ColumnList.replace("StdMName AS MOTHER,", "");
                        num_of_field_selected--;
                    }

                }
                else
                {
                    if (StdMName.isChecked()) {
                        ColumnList += "StdMNameHn AS माता,";
                        num_of_field_selected++;
                    } else {
                        ColumnList = ColumnList.replace("StdMNameHn AS माता,", "");
                        num_of_field_selected--;
                    }
                }
                alertMesage(StdMName);
            }
        });
        StdClass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (StdClass.isChecked())
                    {
                        ColumnList+="StdClassID AS CLASS,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdClassID AS CLASS,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (StdClass.isChecked())
                    {
                        ColumnList+="StdClassID AS कक्षा,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdClassID AS कक्षा,","");
                        num_of_field_selected--;
                    }

                }
                alertMesage(StdClass);

            }
        });
        StdSession.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if(_lang.equalsIgnoreCase("en"))
//                {
//                    if (StdSession.isChecked())
//                    {
//                        ColumnList+="StdSectionID AS SECTION,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdSectionID AS SECTION,","");
//                        num_of_field_selected--;
//                    }
//                }
//                else
//                {
//                    if (StdSession.isChecked())
//                    {
//                        ColumnList+="StdSectionID AS सेक्शन,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdSectionID AS सेक्शन,","");
//                        num_of_field_selected--;
//                    }
//                }

                if (StdSession.isChecked())
                {
                    ColumnList+="StdSectionID AS SECTION,";
                    num_of_field_selected++;
                }
                else
                {
                    ColumnList=ColumnList.replace("StdSectionID AS SECTION,","");
                    num_of_field_selected--;
                }
                alertMesage(StdSession);
            }
        });
        StdDoB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (StdDoB.isChecked())
                    {
                        ColumnList+="StdDoB AS DoB,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdDoB AS DoB,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (StdDoB.isChecked())
                    {
                        ColumnList+="StdDoB AS [जन्म तिथि],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdDoB AS [जन्म तिथि],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(StdDoB);
            }
        });
        StdMobNum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (StdMobNum.isChecked())
                    {
                        ColumnList+="StdMobNum AS MOBILE,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdMobNum AS MOBILE,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (StdMobNum.isChecked())
                    {
                        ColumnList+="StdMobNum AS मोबाइल,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("StdMobNum AS मोबाइल,","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(StdMobNum);
            }
        });

        BenID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (BenID.isChecked())
                    {
                        ColumnList+="BenID AS [BEN-ID],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("BenID AS [BEN-ID],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (BenID.isChecked())
                    {
                        ColumnList+="BenID AS [बेन आइड],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("BenID AS [बेन आइड],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(BenID);
            }
        });
        StdCategoryName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if(_lang.equalsIgnoreCase("en"))
//                {
//                    if (StdCategoryName.isChecked())
//                    {
//                        ColumnList+="StdCategoryID AS CATEGORY,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdCategoryID AS CATEGORY,","");
//                        num_of_field_selected--;
//                    }
//                }
//                else
//                {
//                    if (StdCategoryName.isChecked())
//                    {
//                        ColumnList+="StdCategoryID AS वर्ग,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdCategoryID AS वर्ग,","");
//                        num_of_field_selected--;
//                    }
//                }

                if (StdCategoryName.isChecked())
                {
                    ColumnList+="StdCategoryID AS CATEGORY,";
                    num_of_field_selected++;
                }
                else
                {
                    ColumnList=ColumnList.replace("StdCategoryID AS CATEGORY,","");
                    num_of_field_selected--;
                }
                alertMesage(StdCategoryName);
            }
        });
        EmailId.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (EmailId.isChecked())
                    {
                        ColumnList+="EmailId AS [EMAIL-ID],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("EmailId AS [EMAIL-ID],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (EmailId.isChecked())
                    {
                        ColumnList+="EmailId AS [ईमेल आईडी],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("EmailId AS [ईमेल आईडी],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(EmailId);
            }
        });
        StdGender.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if(_lang.equalsIgnoreCase("en"))
//                {
//                    if (StdGender.isChecked())
//                    {
//                        ColumnList+="StdGenderID AS GENDER,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdGenderID AS GENDER,","");
//                        num_of_field_selected--;
//                    }
//                }
//                else
//                {
//                    if (StdGender.isChecked())
//                    {
//                        ColumnList+="StdGenderID AS लिंग,";
//                        num_of_field_selected++;
//                    }
//                    else
//                    {
//                        ColumnList=ColumnList.replace("StdGenderID AS लिंग,","");
//                        num_of_field_selected--;
//                    }
//                }

                if (StdGender.isChecked())
                {
                    ColumnList+="StdGenderID AS GENDER,";
                    num_of_field_selected++;
                }
                else
                {
                    ColumnList=ColumnList.replace("StdGenderID AS GENDER,","");
                    num_of_field_selected--;
                }
                alertMesage(StdGender);
            }
        });
        Addmissiondate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (Addmissiondate.isChecked())
                    {
                        ColumnList+="Addmissiondate AS [ADM-DATE],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("Addmissiondate AS [ADM-DATE],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (Addmissiondate.isChecked())
                    {
                        ColumnList+="Addmissiondate AS [प्रवेश तिथि],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("Addmissiondate AS [प्रवेश तिथि],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(Addmissiondate);
            }
        });
        AadharCardNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (AadharCardNo.isChecked())
                    {
                        ColumnList+="AadharCardNo AS AADHAAR,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AadharCardNo AS AADHAAR,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (AadharCardNo.isChecked())
                    {
                        ColumnList+="AadharCardNo AS [आधार-संख्या],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AadharCardNo AS [आधार-संख्या],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(AadharCardNo);
            }
        });

        IsAadhaarVerified.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (IsAadhaarVerified.isChecked())
                    {
                        ColumnList+="IsAadhaarVerified AS [AAD-VER?],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsAadhaarVerified AS [AAD-VER?],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (IsAadhaarVerified.isChecked())
                    {
                        ColumnList+="IsAadhaarVerified AS [आधार सत्यापित?],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsAadhaarVerified AS [आधार सत्यापित?],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(IsAadhaarVerified);
            }
        });

        IsMinority.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (IsMinority.isChecked())
                    {
                        ColumnList+="IsMinority AS ISMIN,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsMinority AS ISMIN,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (IsMinority.isChecked())
                    {
                        ColumnList+="IsMinority AS अल्पसंख्यक,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsMinority AS अल्पसंख्यक,","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(IsMinority);
            }
        });

        IsHandicapped.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (IsHandicapped.isChecked())
                    {
                        ColumnList+="IsHandicapped AS ISHAND,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsHandicapped AS ISHAND,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (IsHandicapped.isChecked())
                    {
                        ColumnList+="IsHandicapped AS विकलांग,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("IsHandicapped AS विकलांग,","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(IsHandicapped);
            }
        });

        AccountNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (AccountNo.isChecked())
                    {
                        ColumnList+="AccountNo AS ACNUM,";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountNo AS ACNUM,","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (AccountNo.isChecked())
                    {
                        ColumnList+="AccountNo AS [खाता-संख्या],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountNo AS [खाता-संख्या],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(AccountNo);
            }
        });
        AccountHolderName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (AccountHolderName.isChecked())
                    {
                        ColumnList+="AccountHolderName AS [AC-HOLDER],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountHolderName AS [AC-HOLDER],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (AccountHolderName.isChecked())
                    {
                        ColumnList+="AccountHolderName AS [खाता-धारक],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountHolderName AS [खाता-धारक],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(AccountHolderName);
            }
        });
        AccountHolderType_Name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (AccountHolderType_Name.isChecked())
                    {
                        ColumnList+="AccountHolderType_Name AS [AC-HO-TYPE],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountHolderType_Name AS [AC-HO-TYPE],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (AccountHolderType_Name.isChecked())
                    {
                        ColumnList+="AccountHolderType_NameHn AS [खाता धारक प्रकार],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AccountHolderType_NameHn AS [खाता धारक प्रकार],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(AccountHolderType_Name);
            }
        });
        AttSeventyFivePercent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (AttSeventyFivePercent.isChecked())
                    {
                        ColumnList+="AttSeventyFivePercent AS [>75%],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AttSeventyFivePercent AS [>75%],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (AttSeventyFivePercent.isChecked())
                    {
                        ColumnList+="AttSeventyFivePercent AS [>75%],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("AttSeventyFivePercent AS [>75%],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(AttSeventyFivePercent);
            }
        });
        BenificiaryStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    if (BenificiaryStatus.isChecked())
                    {
                        ColumnList+="BenificiaryStatus AS [BEN-STATUS],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("BenificiaryStatus AS [BEN-STATUS],","");
                        num_of_field_selected--;
                    }
                }
                else
                {
                    if (BenificiaryStatus.isChecked())
                    {
                        ColumnList+="BenificiaryStatus AS [लाभार्थी-स्थिति],";
                        num_of_field_selected++;
                    }
                    else
                    {
                        ColumnList=ColumnList.replace("BenificiaryStatus AS [लाभार्थी-स्थिति],","");
                        num_of_field_selected--;
                    }
                }
                alertMesage(BenificiaryStatus);
            }
        });
        //------------END CHECK BOX
        // dialogBuilder.setIcon(R.drawable.filters);
        if(_lang.equalsIgnoreCase("en")) {
            txtStdNameForSearch.setHint("Enter Student Name in English");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    std_name = txtStdNameForSearch.getText().toString();
                    if(num_of_field_selected<=10)
                    {
                        dialog.dismiss();
                        getAdditionalCustomSearchResult(SQLSelectQuery);
                    }
                    else
                    {
                        Toast.makeText(SearchCustomActivity.this, "Please select field not more than 12.", Toast.LENGTH_SHORT).show();
                    }

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
            //dialogBuilder.setTitle("अतिरिक्त फ़िल्टर");
            dialogBuilder.setPositiveButton(R.string.okhn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // if(isSelectedAnyValue( _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang)) {
                    std_name = txtStdNameForSearch.getText().toString();
                    if(num_of_field_selected<=10)
                    {
                        dialog.dismiss();
                        getAdditionalCustomSearchResult(SQLSelectQuery);
                    }
                    else
                    {
                        Toast.makeText(SearchCustomActivity.this, "कृपया 12 से अधिक फ़ील्ड का चयन नहीं करें |", Toast.LENGTH_SHORT).show();
                    }


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

    public void getAdditionalCustomSearchResult(String SQLSelectQuery)
    {
        String sql_query=getSQLSelectQuery();
        Log.e("sql_query",sql_query);
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        list_student_cursor = helper.getCustomSearchResult(sql_query,_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus,str_atn);
        studentList=getCustomRecords(list_student_cursor);
        studentListAdaptor = new SearchAdapter(SearchCustomActivity.this,"E");
        studentListAdaptor.upDateEntries(studentList);
        list_student.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();
        txtStdCountVE.setText(String.valueOf(studentList.size()));
        txtAdnStdCount.setText(String.valueOf(studentList.size()));
        int setID=0;
        String setid="0";
        for(int i=0;i<SessionList.size();i++)
        {
            if(_varSession_Id.equalsIgnoreCase(SessionList.get(i).getSessionID()))
            {
                setID=(i+1);
                setid=""+setID;
            }
        }
        if(_lang.equalsIgnoreCase("en"))
        {
            txtTotalStdLbl.setText("TOTAL RECORD FOUND: ");
            reportLable=getReportSubLabelEN(_diseCode, _varClass_Id, setid,_varFYear_Id,_varFyear_Name, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus,str_atn);
        }
        else
        {
            txtTotalStdLbl.setText("कुल रिकॉर्ड मिला: ");
            reportLable=getReportSubLabelHN(_diseCode, _varClass_Id, setid,_varFYear_Id,_varFyear_Name, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus,str_atn);
        }
    }
    public String getReportSubLabelEN(String dc,String cid,String ses,String fyear,String fyearNm,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String atten)
    {
        String subWhere="";

        if(!benstatus.equalsIgnoreCase("0"))
        {
            //benstatus=benstatuslist.get(Integer.parseInt(benstatus));
            benstatus=benstatus;
            subWhere+=" eupiStatus='"+ benstatus +"'";
        }
       else {
            subWhere += " eupiStatus='All(Accepted/Rejected)'";
        }

        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Class='"+ cid +"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Section='"+  SessionList.get(Integer.parseInt(ses)-1).getSessionName() +"'";
        }
        else
        {
            subWhere+=" AND Section='None'";
        }

        if(!fyear.equalsIgnoreCase("0"))
        {
//            String fyear1="";
//            if(fyear.equals("1920")){
//                 fyear1="2019-2020";
//            }
//            else if (fyear.equals("2021")){
//                fyear1="2020-2021";
//            }
            subWhere+=" AND Academic Year='"+ fyearNm +"'";
        }

        //-----------------------

        if(!cate.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Category='"+ catlists.get(Integer.parseInt(cate)-1).getCategoryName() +"'";
        }
        if(!min.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Minority='"+ min +"'";
        }
        if(!gen.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Gender='"+ genderlists.get(Integer.parseInt(gen)-1).getGenderName() +"'";
        }
        if(!isbpl.equalsIgnoreCase("0"))
        {
            subWhere+=" AND BPL='"+ isbpl +"'";
        }
        if(!ishnd.equalsIgnoreCase("0"))
        {
            subWhere+=" AND Handicapped='"+ ishnd +"'";
        }
        if(!atten.equalsIgnoreCase(""))
        {
            if(atten.equalsIgnoreCase("N"))
            {
                atten="< 75%";
            }
            else
            {
                atten="> 75%";
            }
            subWhere+=" AND Attendance='"+ atten +"'";
        }
        if(lang.equalsIgnoreCase("en")) {
            if (std_name.trim().length()>0) {
                subWhere += " AND Beneficiary Name '" + std_name + "'";
            }
        }

        return subWhere;
    }
    public String getReportSubLabelHN(String dc,String cid,String ses,String fyear,String fyearNm,String cate,String min,String gen,String isbpl,String ishnd,String std_name,String lang,String nonvalue,String benstatus,String atten)
    {
        String subWhere="";

        if(!benstatus.equalsIgnoreCase(""))
        {
            benstatus=benstatuslist.get(Integer.parseInt(benstatus));
        }
        subWhere+=" लाभार्थी की स्थिति ='"+ benstatus +"'";

        if(!cid.equalsIgnoreCase("0"))
        {
            subWhere+=" और कक्षा='"+ cid +"'";
        }
        if(!ses.equalsIgnoreCase("0"))
        {
            subWhere+=" और सेक्शन='"+ SessionList.get(Integer.parseInt(ses)-1).getSessionName() +"'";
        }

        if(!fyear.equalsIgnoreCase("0"))
        {
            subWhere+=" और वित्तीय वर्ष='"+ fyearNm +"'";
        }

        //-----------------------

        if(!cate.equalsIgnoreCase("0"))
        {
            subWhere+=" और वर्ग='"+ catlists.get(Integer.parseInt(cate)-1).getCategoryNameHn() +"'";
        }
        if(!min.equalsIgnoreCase("0"))
        {
            subWhere+=" और अल्पसंख्यक='"+min +"'";
        }
        if(!gen.equalsIgnoreCase("0"))
        {
            subWhere+=" और लिंग='"+ genderlists.get(Integer.parseInt(gen)-1).getGenderNameHn() +"'";
        }
        if(!isbpl.equalsIgnoreCase("0"))
        {
            subWhere+=" और गरीबी रेखा से नीचे='"+ isbpl +"'";
        }
        if(!ishnd.equalsIgnoreCase("0"))
        {
            subWhere+=" और विकलांग='"+ ishnd +"'";
        }
        if(!atten.equalsIgnoreCase(""))
        {
            if(atten.equalsIgnoreCase("N"))
            {
                atten="< 75%";
            }
            else
            {
                atten="> 75%";
            }
            subWhere+=" और उपस्थिति='"+ atten +"'";
        }

        if(lang.equalsIgnoreCase("hn")) {
            if (std_name.trim().length()>0) {
                subWhere += " और लाभार्थी का नाम  '" + std_name + "'";
            }
        }

        return subWhere;
    }
    public void loadGenderList(Spinner spn_gender)
    {
        localDB = new DataBaseHelper(SearchCustomActivity.this);
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
                if(_lang.equalsIgnoreCase("en")) {
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
        localDB = new DataBaseHelper(SearchCustomActivity.this);
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

    public void addAttendance(Spinner spn_atten) {
        if(_lang.equalsIgnoreCase("en")) {
            atnlist.add("-All-");  //-सभी-
            atnlist.add("Less tha 75%"); //1
            atnlist.add("Above 75%"); //0
        }
        else
        {
            atnlist.add("-सभी-");  //-सभी-
            atnlist.add("75% से नीचे"); //1
            atnlist.add("75% से ऊपर"); //0
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, atnlist);
        spn_atten.setAdapter(dataAdapter);
    //    if(str_atn.equalsIgnoreCase("1"))
        if(str_atn.equalsIgnoreCase("N"))
        {
            spn_atten.setSelection(1);
        }
     //   else  if(str_atn.equalsIgnoreCase("0"))
        else  if(str_atn.equalsIgnoreCase("Y"))
        {
            spn_atten.setSelection(2);
        }
    }
    public void addBenStatus(Spinner spn_benS) {

        if(_lang.equalsIgnoreCase("en")) {
            benstatuslist.add("All");
            //  benstatuslist.add("Sent To PFMS For Verification");
            benstatuslist.add("Accepted");
            benstatuslist.add("Rejected");
        }
        else
        {
            benstatuslist.add("सभी");
            //benstatuslist.add("सत्यापन के लिए पीएफएमएस को भेजा गया");
            benstatuslist.add("स्वीकृत");
            benstatuslist.add("अस्वीकृत");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, benstatuslist);
        spn_benS.setAdapter(dataAdapter);


        // if(str_benstatus.equalsIgnoreCase("0"))
        if(str_benstatus.equalsIgnoreCase("All") || str_benstatus.equalsIgnoreCase("सभी"))
        {
            spn_benS.setSelection(0);
        }
        // else  if(str_benstatus.equalsIgnoreCase("ACCP"))
        else if(str_benstatus.equalsIgnoreCase("Accepted") || str_benstatus.equalsIgnoreCase("स्वीकृत"))
        {
            spn_benS.setSelection(1);
        }
        else if(str_benstatus.equalsIgnoreCase("Rejected") || str_benstatus.equalsIgnoreCase("अस्वीकृत"))
        // else  if(str_benstatus.equalsIgnoreCase("RJCT"))
        {
            spn_benS.setSelection(2);
        }

//        if(str_benstatus.equalsIgnoreCase("0"))
//        {
//            spn_benS.setSelection(0);
//        }
//        else  if(str_benstatus.equalsIgnoreCase("1"))
//        {
//            spn_benS.setSelection(1);
//        }
//        else  if(str_benstatus.equalsIgnoreCase("2"))
//        {
//            spn_benS.setSelection(2);
//        }
//        else  if(str_benstatus.equalsIgnoreCase("3"))
//        {
//            spn_benS.setSelection(3);
//        }

    }
    public void onClick_GeneratePDF(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(SearchCustomActivity.this);
        if(_lang.equalsIgnoreCase("en"))
        {

            ab.setTitle(R.string.report_title);
            ab.setMessage("Click on OK to continue");
        }
        else
        {
            ab.setTitle(R.string.report_titlehn);
            ab.setMessage("जारी रखने के लिए OK पर क्लिक करें");
        }
        ab.setPositiveButton("[ OK ]",
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
                            // createCustomPdfviewlocal("en");
                            createCustomWebViewContaintsForPDF();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
        ab.show();

    }

    public void alertMesage(CheckBox chk)
    {

        if(num_of_field_selected>10)
        {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(SearchCustomActivity.this, "Please select field not more than 10." + num_of_field_selected, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(SearchCustomActivity.this, "कृपया 10 से अधिक फ़ील्ड का चयन नहीं करें |" + num_of_field_selected, Toast.LENGTH_SHORT).show();
            }
            num_of_field_selected--;
            chk.setChecked(false);
        }

    }
    private void createPdfviewlocal(String lang) throws IOException, DocumentException {

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


        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++)
        {
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
    private void createCustomPdfviewlocal(String lang) throws IOException, DocumentException {

        if(lang.equalsIgnoreCase("en")) {
            progressDialog1.setMessage("Please wait. Genreating PDF file");
        }
        else
        {
            progressDialog1.setMessage("कृपया प्रतीक्षा करें। पीडीएफ फाइल जेनेरेट हो रहा है");
        }
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        String targetPdf = "/sdcard/"+_filePrefixName+timeStamp+".pdf";
        File filePath;
        filePath = new File(targetPdf);

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
        // PdfPTable tableC = new PdfPTable(new float[] { 5,5, 3, 3,3,3,4 });

        int ColCount = list_student_cursor.getColumnCount();

        PdfPTable tableC = new PdfPTable(ColCount);
        tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        if(lang.equalsIgnoreCase("en")) {

            for(int i=0;i<ColCount;i++) {
                tableC.addCell(new Phrase(list_student_cursor.getColumnName(i), fonthdr));
                Log.e("HEADER",list_student_cursor.getColumnName(i));
            }
        }


        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);

        }
        if(lang.equalsIgnoreCase("en")) {
            DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

            String sqlQuery=getSQLSelectQuery();
            Cursor  list_student_cursor = helper.getAdvanceCustomSearchResult(sqlQuery,_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus,str_atn);
            if (list_student_cursor != null) {

                Log.e("sqlQuery",sqlQuery);
                if (list_student_cursor.getCount() > 0) {
//                    list_student_cursor.moveToFirst();
                    while(list_student_cursor.moveToNext())
                    {
                        for (int i = 0; i < ColCount; i++) {
                            String cls = "";
                            String sess = "";

                            if (list_student_cursor.getColumnName(i).equalsIgnoreCase("Addmissiondate")) {
                                String y, m, da, addate;
                                String admdate = list_student_cursor.getString(i) != "anyType{}" ? list_student_cursor.getString(i) : "";
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
                            } else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("StdClassID")) {
                                cls = Utiilties.getValue(list_student_cursor.getString(i));
                                tableC.addCell(cls);
                            } else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("StdSectionID")) {
                                sess = Utiilties.getValue(list_student_cursor.getString(i));
                                tableC.addCell(sess);
                            } else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("StdDoB")) {

                                String y1, m1, d1, tdate;
                                // String admdate = list_student_cursor.getString(i) != "anyType{}" ? list_student_cursor.getString(i) : "";
                                String dobBen = list_student_cursor.getColumnName(i) != "anyType{}" ? list_student_cursor.getColumnName(i) : "";
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
                            }
                            else {
                                tableC.addCell(new Phrase(Utiilties.getValue(list_student_cursor.getString(i))));
                            }
                        }
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


    public String getValue(String forID, String tblName)
    {
        String val="";
        DataBaseHelper helper=new DataBaseHelper(SearchCustomActivity.this);
        try {
            if (tblName.equalsIgnoreCase("CategoryHN")) {
                val = helper.getValueOfID("SELECT * FROM  " + tblName + " WHERE CategoryId=" + forID);
            } else if (tblName.equalsIgnoreCase("GenderHN")) {
                val = helper.getValueOfID("SELECT * FROM  " + tblName + " WHERE GenderId=" + forID);
            } else if (tblName.equalsIgnoreCase("SessionList")) {
                val = helper.getValueOfID("SELECT * FROM  " + tblName + " WHERE SessionID=" + forID);
            } else if (tblName.equalsIgnoreCase("AccountHolderType")) {
                val = helper.getValueOfID("SELECT * FROM  " + tblName + " WHERE id=" + forID);
            }
            return val;
        }
        catch (Exception e)
        {
            val="";
        }
        return val;
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
                    Toast.makeText(SearchCustomActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SearchCustomActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
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
            Uri uri = FileProvider.getUriForFile(SearchCustomActivity.this,
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
                    Toast.makeText(SearchCustomActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SearchCustomActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }

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

                    htmlTable += "<th style='border: thin solid #000000; width:11%'>"+Utiilties.getValue(studentList.get(x).getStdadmnum())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+Utiilties.getValue(studentList.get(x).getStdnamehn())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+Utiilties.getValue(studentList.get(x).getStdfnamehn())+"</th>";

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
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+Utiilties.getValue(studentList.get(x).getStdclassid())+"</th>";
                    } else {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+Utiilties.getValue(studentList.get(x).getStdclassid()) + " (" + Utiilties.getValue(studentList.get(x).getStdsession()) + ")"+"</th>";
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
                    htmlTable += "<th style='border: thin solid #000000; width:12%'>"+Utiilties.getValue(studentList.get(x).getStdmobile())+"</th>";

                    htmlTable+="</tr>";
                }
            }
        }
        htmlTable+="</table><br><br>";



        WebSettings webSettings = showpdfdataWV.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");

        showpdfdataWV.setWebViewClient(new SearchCustomActivity.myWebClient());


        String myHtmlString=htmlTableH+htmlTable;

        Log.e("HTML",myHtmlString);

        //showpdfdataWV.loadData(myHtmlString, "text/html; charset=UTF-8", null);
        showpdfdataWV.loadDataWithBaseURL(null,myHtmlString, "text/html; charset=UTF-8", null,null);
    }

    public void createCustomWebViewContaintsForPDF()
    {
        // style='background-color:#0000CC; height:4px;
        try
        {
            String info="";
            String htmlTableH="<table width='100%'>";
            htmlTableH+="<tr>";
            if(_lang.equalsIgnoreCase("en")) {
                htmlTableH += "<th><img alt='Medha Soft' src='file:///android_asset/title.png' height='50' width='100%'/></th>";
            }
            else
            {
                htmlTableH += "<th><img alt='Medha Soft' src='file:///android_asset/titlehn.png' height='50' width='100%'/></th>";
            }
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

            if(_lang.equalsIgnoreCase("en"))
            {
                // info="REPORT BASED ON CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate;
                info="REPORT BASED ON : " +reportLable;
            }
            else
            {
                // info="रिपोर्ट आधारित कक्षा: " +_varClass_Name+", सेक्शन: " + _varSession_Name+"  दिनांक: "+ cDate;
                info="REPORT BASED ON : " +reportLable;
            }

            htmlTableH+="<tr>";
            htmlTableH+="<th style='color: #0000FF;'>" + info  + "</th>";
            htmlTableH+="</tr>";
            htmlTableH+="</table><br><br>";

            String htmlTable="<table width='100%' style='border: thin solid #000000;'>";

            int ColCount = list_student_cursor.getColumnCount();
            htmlTable+="<tr height ='35dp' style='color: #FFFFFF; background-color: #666666'>";
            for(int i=0;i<ColCount;i++) {
                // tableC.addCell(new Phrase(list_student_cursor.getColumnName(i), fonthdr));
                htmlTable+="<th style='border: thin solid #000000; width:11%'>"+list_student_cursor.getColumnName(i)+"</th>";
                Log.e("Header;;",list_student_cursor.getColumnName(i));
            }

            htmlTable+="</tr>";


            int countLess75Per=0;

            DataBaseHelper helper = new DataBaseHelper(SearchCustomActivity.this);

            String sqlQuery=getSQLSelectQuery();
            Cursor  list_student_cursor = helper.getCustomSearchResult(sqlQuery,_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id,_varCategory_Id,str_minority,_varGender_Id, str_bpl,str_divyang,std_name,_lang,str_none_value,str_benstatus,str_atn);
            if (list_student_cursor != null) {

                Log.e("sqlQuery",sqlQuery);
                if (list_student_cursor.getCount() > 0) {
//                    list_student_cursor.moveToFirst();
                    while(list_student_cursor.moveToNext())
                    {
                        htmlTable+="<tr>";
                        for (int i = 0; i < ColCount; i++) {
                            String cls = "";
                            String sess = "";

                            if (list_student_cursor.getColumnName(i).equalsIgnoreCase("ADM-DATE") || list_student_cursor.getColumnName(i).equalsIgnoreCase("प्रवेश तिथि")) {
                                String y, m, da, addate;
                                String admdate = list_student_cursor.getString(i) != "anyType{}" ? list_student_cursor.getString(i) : "";
                                addate = admdate;
                                try {
                                    if (admdate != null) {
                                        if (!admdate.equalsIgnoreCase("")) {

                                            y = admdate.substring(0, 4);
                                            m = admdate.substring(4, 6);
                                            da = admdate.substring(6, 8);
                                            addate = admdate;//da + "-" + m + "-" + y;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("EXCEPTION", "date2 exception");
                                }
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+addate+"</th>";
                                //  tableC.addCell(addate);
                            } else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("CLASS")) {
                                cls = Utiilties.getValue(list_student_cursor.getString(i));
                                //tableC.addCell(cls);
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+cls+"</th>";
                            }
                            else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("SECTION")) {
                                sess = getValue(Utiilties.getValue(list_student_cursor.getString(i)),"SessionList");

                                if(sess!=null)
                                {
                                    if(sess.equalsIgnoreCase(""))
                                    {
                                        sess="None";
                                    }
                                }
                                else
                                {
                                    sess="None";
                                }

                                //tableC.addCell(sess);
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+sess+"</th>";
                            }
                            else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("CATEGORY")) {
                                sess = getValue(Utiilties.getValue(list_student_cursor.getString(i)),"CategoryHN");
                                if(sess.equalsIgnoreCase(""))
                                {
                                    sess="None";
                                }
                                else if(sess==null)
                                {
                                    sess="None";
                                }

                                //tableC.addCell(sess);
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+sess+"</th>";
                            }
                            else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("GENDER")) {
                                sess = getValue(Utiilties.getValue(list_student_cursor.getString(i)),"GenderHN");
                                if(sess.equalsIgnoreCase(""))
                                {
                                    sess="None";
                                }
                                else if(sess==null)
                                {
                                    sess="None";
                                }

                                //tableC.addCell(sess);
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+sess+"</th>";
                            }

                            else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("StdGenderID")) {
                                sess = getValue(Utiilties.getValue(list_student_cursor.getString(i)),"GenderHN");
                                if(sess.equalsIgnoreCase(""))
                                {
                                    sess="None";
                                }
                                else if(sess==null)
                                {
                                    sess="None";
                                }

                                //tableC.addCell(sess);
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+sess+"</th>";
                            }


                            else if (list_student_cursor.getColumnName(i).equalsIgnoreCase("DoB") || list_student_cursor.getColumnName(i).equalsIgnoreCase("जन्म तिथि")) {

                                String y1, m1, d1, tdate;
                                // String admdate = list_student_cursor.getString(i) != "anyType{}" ? list_student_cursor.getString(i) : "";
                                String dobBen = list_student_cursor.getString(i) != "anyType{}" ? list_student_cursor.getString(i) : "";
                                tdate = dobBen;
                                try {
                                    if (dobBen != null) {
                                        if (!dobBen.equalsIgnoreCase("")) {
                                            y1 = dobBen.substring(0, 4);
                                            m1 = dobBen.substring(4, 6);
                                            d1 = dobBen.substring(6, 8);
                                            tdate =dobBen;// d1 + "-" + m1 + "-" + y1;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("EXCEPTION", "date2 exception");
                                }
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+tdate+"</th>";
                                // tableC.addCell(tdate);
                            }
                            else {
                                //tableC.addCell(new Phrase(Utiilties.getValue(list_student_cursor.getString(i))));
                                htmlTable += "<th style='border: thin solid #000000; width:11%'>"+Utiilties.getValue(list_student_cursor.getString(i))+"</th>";
                            }
                        }
                        htmlTable+="</tr>";
                    }

                }
            }

            htmlTable+="</table><br><br>";



            WebSettings webSettings = showpdfdataWV.getSettings();
            webSettings.setDefaultTextEncodingName("utf-8");

            showpdfdataWV.setWebViewClient(new SearchCustomActivity.myWebClient());


            String myHtmlString=htmlTableH+htmlTable;

            Log.e("HTML",myHtmlString);

            //showpdfdataWV.loadData(myHtmlString, "text/html; charset=UTF-8", null);
            showpdfdataWV.loadDataWithBaseURL(null,myHtmlString, "text/html; charset=UTF-8", null,null);
        }
        catch (Exception e)
        {
            if(progressDialog1.isShowing())
            {
                progressDialog1.dismiss();
            }
            Toast.makeText(this, "Some error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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
        //NOTE: make changes for file provider in [com.webviewtopdf.PdfView.java-in function fileChooser(Activity activity, String path) {
        //Replace package name by app pakage name

        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        final String fileName=_filePrefixName+timeStamp+".pdf";

        final ProgressDialog progressDialog=new ProgressDialog(SearchCustomActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(SearchCustomActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(SearchCustomActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }

    public void setLabelsTextAsPerLanguage()
    {
        if(_lang.equalsIgnoreCase("en")) {
            // txtTotalStdLbl.setText("TOTAL RECORD FOUND: ");
            tvSUbHeader.setText("SEARCH FOR BENEFICIARY");
            txtHeaderName.setText(R.string.app_name);
            txt_FName.setText("FATHER NAME");
            txt_Name.setText("NAME");
            txt_ANum.setText("ADM.NO");

        }
        else
        {
            //txtTotalStdLbl.setText("कुल रिकॉर्ड मिला: ");
            tvSUbHeader.setText("लाभार्थी को खोजें");
            txtHeaderName.setText(R.string.app_namehn);
            txt_FName.setText("पिता का नाम");
            txt_Name.setText("नाम");
            txt_ANum.setText("प्र. संख्या");
        }
    }

    public ArrayList<studentList> getCustomRecords(Cursor cursor)
    {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();

        while (cursor.moveToNext()) {
            studentList stdData = new studentList();
            try {
                if(_lang.equalsIgnoreCase("en")) {
                    stdData.setStdname((cursor.getString(cursor.getColumnIndex("NAME"))));
                    stdData.setStdfname((cursor.getString(cursor.getColumnIndex("F-NAME"))));
                    stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("REG-NUM"))));

                }
                else {
                    stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("लाभार्थी"))));
                    stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("पिता का नाम"))));
                    stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("प्रवेश संख्या"))));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("लाभार्थी"))));
                stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("पिता का नाम"))));
                stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("प्रवेश संख्या"))));
            }

            stdDataEntities.add(stdData);
        }

        cursor.close();
        return stdDataEntities;
    }


    public String getSQLSelectQuery()
    {
        String sqlqery="";
        if(ColumnList.trim().length()<=0)
        {
            ColumnList=" * ";
        }
        if(ColumnList.trim().length()>1)
        {
            ColumnList=ColumnList.replace("*","");
        }

        Log.e("ColumnList",ColumnList);
        sqlqery="SELECT "+ ColumnList +" FROM StudentListForAttendance WHERE";
        if(sqlqery.contains(", FROM"))
        {
            sqlqery=sqlqery.replace(", FROM"," FROM");
        }

        return sqlqery;
    }
}

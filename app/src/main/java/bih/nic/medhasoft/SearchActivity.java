package bih.nic.medhasoft;

import android.app.AlertDialog;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import bih.nic.medhasoft.Adapter.SearchAdapter;
import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;

public class SearchActivity extends AppCompatActivity {

    SearchAdapter studentListAdaptor;
    ListView list_student;

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
    String str_minority="0",str_divyang="0",str_bpl="0",str_none_value="0",str_benstatus="0";
    TextView txtStdCountVE;

    //-----FILTER------------------
    Spinner spn_benstatus;
    Spinner spnCategory;
    Spinner spnIsMinority;
    Spinner spnGender;
    Spinner spnIsBPL;
    Spinner spnIsHandicaped;
    Spinner spnHavingNonValue;
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
       // getAdditionalSearchResult(SQLSelectQuery);
        setLabelsTextAsPerLanguage();
    }
    public void onClick_AdvSearch(View v)
    {
        MoreFilter();
    }

    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(SearchActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void loadClassList(Spinner spCls)
    {
        localDB = new DataBaseHelper(SearchActivity.this);
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
            spCls.setAdapter(ClassListadapter);
            ClassListadapter.notifyDataSetChanged();
            spCls.setSelection(setID);
        }
    }

    public void loadSessionList(Spinner spSess)
    {
        localDB = new DataBaseHelper(SearchActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("No");
            }
            else
            {
                StringList.add("नहीं");
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
        localDB = new DataBaseHelper(SearchActivity.this);
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

    public void MoreFilter()
    {
        ColumnList="*";
        SQLSelectQuery="SELECT "+ ColumnList +" FROM StudentListForAttendance WHERE";
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
        txtStdNameForSearch =  dialogView.findViewById(R.id.txtStdNameForSearch);
        txtAdnStdCount =  dialogView.findViewById(R.id.txtAdnStdCount);
        txtTotalStdLbl =  dialogView.findViewById(R.id.txtTotalStdLbl);

        benstatuslist=new ArrayList<String>();
        minoritytlist=new ArrayList<String>();
        divyanglist=new ArrayList<String>();
        havingnonevaluelist=new ArrayList<String>();
        bpllist=new ArrayList<String>();

        StdRegNum=dialogView.findViewById(R.id.StdRegNum);
        StdName=dialogView.findViewById(R.id.StdName);
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

        getCheckBoxData();

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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                    _varSession_Name = "No";
                    _varSession_NameHn = "नहीं";

                }
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                getAdditionalSearchResult(SQLSelectQuery);
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
                    getAdditionalSearchResult(SQLSelectQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        // dialogBuilder.setIcon(R.drawable.filters);
        if(_lang.equalsIgnoreCase("en")) {
            txtStdNameForSearch.setHint("Enter Student Name in English");
            // dialogBuilder.setTitle("Additional Filter");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    std_name = txtStdNameForSearch.getText().toString();
                    dialog.dismiss();
                    getAdditionalSearchResult(SQLSelectQuery);

                }
            });
//            dialogBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    //resetAdditionSearchCriteria();
//                }
//            });
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
                    dialog.dismiss();
                    getAdditionalSearchResult(SQLSelectQuery);

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
        // }

    }

    public void getAdditionalSearchResult(String SQLSelectQuery)
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

        //----studentList = helper.getAdvanceSearchResult(_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus,"0");
        studentList = helper.getAdvanceSearchResult(_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,str_benstatus);

        studentListAdaptor = new SearchAdapter(SearchActivity.this,"E");
        studentListAdaptor.upDateEntries(studentList);
        list_student.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();
        txtStdCountVE.setText(String.valueOf(studentList.size()));
        txtAdnStdCount.setText(String.valueOf(studentList.size()));
        if(_lang.equalsIgnoreCase("en")) {
            txtTotalStdLbl.setText("TOTAL RECORD FOUND: ");
        }
        else
        {
            txtTotalStdLbl.setText("कुल रिकॉर्ड मिला: ");
        }
    }
    public void loadGenderList(Spinner spn_gender)
    {
        localDB = new DataBaseHelper(SearchActivity.this);
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
        localDB = new DataBaseHelper(SearchActivity.this);
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

    public void addBenStatus(Spinner spn_benS) {
        /*
        0-new enery/edited
1-pfms sent to for veryfication
2-accpted at pfms
3- rejected
         */
        if(_lang.equalsIgnoreCase("en")) {
            benstatuslist.add("All");
            //benstatuslist.add("Sent To PFMS For Verification");
            benstatuslist.add("Accepted");
            benstatuslist.add("Rejected");
        }
        else
        {
            //benstatuslist.add("नई प्रविष्टि / संपादित");
            benstatuslist.add("सभी");
            //benstatuslist.add("सत्यापन के लिए पीएफएमएस को भेजा गया");
           // benstatuslist.add("पीएफएमएस में स्वीकृत");
            benstatuslist.add("स्वीकृत");
            benstatuslist.add("अस्वीकृत");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, benstatuslist);
        spn_benS.setAdapter(dataAdapter);

        if(str_benstatus.equalsIgnoreCase("0"))
        {
            spn_benS.setSelection(0);
        }
//        else  if(str_benstatus.equalsIgnoreCase("1"))
//        {
//            spn_benS.setSelection(1);
//        }
        else  if(str_benstatus.equalsIgnoreCase("ACCP"))
        {
            spn_benS.setSelection(1);
        }
        else  if(str_benstatus.equalsIgnoreCase("RJCT"))
        {
            spn_benS.setSelection(2);
        }

    }
    public void onClick_GeneratePDF(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(SearchActivity.this);
        if(_lang.equalsIgnoreCase("en"))
        {

            ab.setTitle(R.string.report_title);
            ab.setMessage( R.string.report_msg);
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
                            // printRegistrationForm("en");
                            createPdfviewlocal("en");


                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e)
                        {
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
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(SearchActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SearchActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
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
            Uri uri = FileProvider.getUriForFile(SearchActivity.this,
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
                    Toast.makeText(SearchActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SearchActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
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

        showpdfdataWV.setWebViewClient(new SearchActivity.myWebClient());


        String myHtmlString=htmlTableH+htmlTable;

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
        //NOTE: make changes for file provider in [com.webviewtopdf.PdfView.java-in function fileChooser(Activity activity, String path) {
        //Replace package name by app pakage name

        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        final String fileName=_filePrefixName+timeStamp+".pdf";

        final ProgressDialog progressDialog=new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(SearchActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(SearchActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
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
        }
        else
        {
            //txtTotalStdLbl.setText("कुल रिकॉर्ड मिला: ");
            tvSUbHeader.setText("लाभार्थी को खोजें");
            txtHeaderName.setText(R.string.app_namehn);
        }
    }

    public void getCheckBoxData()
    {
        StdRegNum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdRegNum.isChecked())
                {
                    ColumnList+="StdRegNum,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdRegNum,","");
                }
            }
        });
        StdName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdName.isChecked())
                {
                    ColumnList+="StdName,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdName,","");
                }
            }
        });
        StdClass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdClass.isChecked())
                {
                    ColumnList+="StdClass,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdClass,","");
                }
            }
        });
        StdSession.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdSession.isChecked())
                {
                    ColumnList+="StdSession,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdSession,","");
                }
            }
        });
        StdDoB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdDoB.isChecked())
                {
                    ColumnList+="StdDoB,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdDoB,","");
                }
            }
        });
        StdMobNum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdMobNum.isChecked())
                {
                    ColumnList+="StdMobNum,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdMobNum,","");
                }
            }
        });

        BenID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (BenID.isChecked())
                {
                    ColumnList+="BenID,";
                }
                else
                {
                    ColumnList=ColumnList.replace("BenID,","");
                }
            }
        });
        StdCategoryName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdCategoryName.isChecked())
                {
                    ColumnList+="StdCategoryName,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdCategoryName,","");
                }
            }
        });
        EmailId.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (EmailId.isChecked())
                {
                    ColumnList+="EmailId,";
                }
                else
                {
                    ColumnList=ColumnList.replace("EmailId,","");
                }
            }
        });
        StdGender.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StdGender.isChecked())
                {
                    ColumnList+="StdGender,";
                }
                else
                {
                    ColumnList=ColumnList.replace("StdGender,","");
                }
            }
        });
        Addmissiondate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Addmissiondate.isChecked())
                {
                    ColumnList+="Addmissiondate,";
                }
                else
                {
                    ColumnList=ColumnList.replace("Addmissiondate,","");
                }
            }
        });
        AadharCardNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AadharCardNo.isChecked())
                {
                    ColumnList+="AadharCardNo,";
                }
                else
                {
                    ColumnList=ColumnList.replace("AadharCardNo,","");
                }
            }
        });

        IsAadhaarVerified.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsAadhaarVerified.isChecked())
                {
                    ColumnList+="IsAadhaarVerified,";
                }
                else
                {
                    ColumnList=ColumnList.replace("IsAadhaarVerified,","");
                }
            }
        });

        IsMinority.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsMinority.isChecked())
                {
                    ColumnList+="IsMinority,";
                }
                else
                {
                    ColumnList=ColumnList.replace("IsMinority,","");
                }
            }
        });

        IsHandicapped.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsHandicapped.isChecked())
                {
                    ColumnList+="IsHandicapped,";
                }
                else
                {
                    ColumnList=ColumnList.replace("IsHandicapped,","");
                }
            }
        });

        AccountNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AccountNo.isChecked())
                {
                    ColumnList+="AccountNo,";
                }
                else
                {
                    ColumnList=ColumnList.replace("AccountNo,","");
                }
            }
        });
        AccountHolderName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AccountHolderName.isChecked())
                {
                    ColumnList+="AccountHolderName,";
                }
                else
                {
                    ColumnList=ColumnList.replace("AccountHolderName,","");
                }
            }
        });
        AccountHolderType_Name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AccountHolderType_Name.isChecked())
                {
                    ColumnList+="AccountHolderType_Name,";
                }
                else
                {
                    ColumnList=ColumnList.replace("AccountHolderType_Name,","");
                }
            }
        });
        AttSeventyFivePercent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AttSeventyFivePercent.isChecked())
                {
                    ColumnList+="AttSeventyFivePercent,";
                }
                else
                {
                    ColumnList=ColumnList.replace("AttSeventyFivePercent,","");
                }
            }
        });
        BenificiaryStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (BenificiaryStatus.isChecked())
                {
                    ColumnList+="BenificiaryStatus,";
                }
                else
                {
                    ColumnList=ColumnList.replace("BenificiaryStatus,","");
                }
            }
        });
    }
}

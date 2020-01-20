package bih.nic.medhasoft;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import java.text.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.ACHOLDERTYPE;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.entity.SENDTOPFMSENTITY;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditBendDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_dob, et_dakhilaNumber,et_dakhilaDAte,et_studentNameEnglish,et_studentnameHindi,et_fatherNameEnglish,et_fathernameHindi,et_mothersNameEnglish,et_mothersnameHindi,et_AadharNameEnglish,
            et_AadharNumber,et_emailId,et_mobileNumber,et_ifsc,et_bankacnum;
    String version="";
    String checklist;
    ImageView imgsave,imgupload;
    EditText et_wardvill,et_actypeholder_other;
    Spinner spn_gender,spn_category,spn_minority,spn_divyang,spn_bpl,spn_actypeholder,spn_send_pfms;
    DataBaseHelper localDB;
    ArrayList<Genderlist> genderlists = new ArrayList<>();
    ArrayAdapter<String> GenderListadapter;
    String _varGender_Name="all",_varGender_NameHn="all",_varGender_Id="0";
    String _varCategory_Name="all",_varCategory_NameHn="all",_varCategory_Id="0";

    String _varACHolder_Name="all",_varACHolder_Id="0";
    String _varSendPfms_Status="",_varSendPfms_Id="";


    ArrayList<categoryHINDI> catlists = new ArrayList<>();
    ArrayAdapter<String> catListadapter;
    ArrayList<String> minoritytlist=new ArrayList<String>();
    ArrayList<String> divyanglist=new ArrayList<String>();
    ArrayList<String> bpllist=new ArrayList<String>();
    String str_minority,str_divyang,str_bpl;
    ArrayList<studentList> EntryList=new ArrayList<>();

    ArrayList<ACHOLDERTYPE> acHolderLists = new ArrayList<>();
    ArrayList<SENDTOPFMSENTITY> asendPfmsLists = new ArrayList<>();
    ArrayAdapter<String> AcHolderAdapter;
    ArrayAdapter<String> SendPfmsAdapter;
    String diseCode;
    String benid;

    ImageView cal1,cal2;

    private Calendar cal;
    private int d;
    private int m;
    private int y;
    boolean date1=false;
    boolean date2=false;
    String adm_dateSave="";
    String dob_dateSave="";
    ConversionTable conversionTable;
    String _lang="en";

    TextView txtHeaderName,tv_dakhilaNumber,tv_dakhilaDAte,tv_studentnameHindi,tv_studentNameEnglish;
    TextView tv_fathernameHindi,tv_fatherNameEnglish,tv_mothersnameHindi,tv_mothersNameEnglish,tv_dob;
    TextView tv_gender,tv_category,tv_minority,tv_divyangstu,tv_bpl,tv_AadharNumber,tv_AadharNameEnglish;
    TextView tv_mobileNumber,tv_emailId,tv_ifsc,tv_bankacnum,tv_acholdername,txtheaderBD;
    TextView tv_bankdetails;
    TextView tv_wardvill,lbl_benid,tv_benid;
    LinearLayout lin_sendPfms;
    ShorCutICON ico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbendetails);

        ico=new ShorCutICON(EditBendDetailsActivity.this);

        et_dakhilaNumber=(EditText) findViewById(R.id.et_dakhilaNumber);
        et_dakhilaDAte=(EditText) findViewById(R.id.et_dakhilaDAte);
        et_studentNameEnglish=(EditText) findViewById(R.id.et_studentNameEnglish);
        et_studentnameHindi=(EditText) findViewById(R.id.et_studentnameHindi);
        et_fatherNameEnglish=(EditText) findViewById(R.id.et_fatherNameEnglish);
        et_fathernameHindi=(EditText) findViewById(R.id.et_fathernameHindi);
        et_mothersNameEnglish=(EditText) findViewById(R.id.et_mothersNameEnglish);
        et_mothersnameHindi=(EditText) findViewById(R.id.et_mothersnameHindi);
        et_AadharNameEnglish=(EditText) findViewById(R.id.et_AadharNameEnglish);
        et_AadharNumber=(EditText) findViewById(R.id.et_AadharNumber);
        et_dob=(EditText) findViewById(R.id.et_dob);
        et_emailId=(EditText) findViewById(R.id.et_emailId);
        et_mobileNumber=(EditText) findViewById(R.id.et_mobileNumber);

        et_bankacnum=(EditText) findViewById(R.id.et_bankacnum);
        et_ifsc=(EditText) findViewById(R.id.et_ifsc);
        et_actypeholder_other=(EditText) findViewById(R.id.et_actypeholder_other);
        et_actypeholder_other.setVisibility(View.GONE);

        tv_wardvill= findViewById(R.id.tv_wardvill);
        et_wardvill= findViewById(R.id.et_wardvill);


        spn_gender=(Spinner) findViewById(R.id.spn_gender);
        spn_category=(Spinner) findViewById(R.id.spn_category);
        spn_minority=(Spinner) findViewById(R.id.spn_minority);
        spn_divyang=(Spinner) findViewById(R.id.spn_divyang);
        spn_bpl=(Spinner) findViewById(R.id.spn_bpl);
        spn_actypeholder=(Spinner) findViewById(R.id.spn_actypeholder);
        spn_send_pfms=(Spinner) findViewById(R.id.spn_send_pfms);

        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");

        cal1=(ImageView) findViewById(R.id.imageView_Dakhila);
        cal2=(ImageView) findViewById(R.id.image_dob);

        cal1.setOnClickListener(this);
        cal2.setOnClickListener(this);


        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        tv_dakhilaNumber = (TextView) findViewById(R.id.tv_dakhilaNumber);
        tv_dakhilaDAte = (TextView) findViewById(R.id.tv_dakhilaDAte);
        tv_studentnameHindi = (TextView) findViewById(R.id.tv_studentnameHindi);
        tv_studentNameEnglish = (TextView) findViewById(R.id.tv_studentNameEnglish);

        tv_fathernameHindi = (TextView) findViewById(R.id.tv_fathernameHindi);
        tv_fatherNameEnglish = (TextView) findViewById(R.id.tv_fatherNameEnglish);
        tv_mothersnameHindi = (TextView) findViewById(R.id.tv_mothersnameHindi);
        tv_mothersNameEnglish = (TextView) findViewById(R.id.tv_mothersNameEnglish);
        tv_dob = (TextView) findViewById(R.id.tv_dob);

        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_minority = (TextView) findViewById(R.id.tv_minority);
        tv_divyangstu = (TextView) findViewById(R.id.tv_divyangstu);
        tv_bpl = (TextView) findViewById(R.id.tv_bpl);
        tv_AadharNumber = (TextView) findViewById(R.id.tv_AadharNumber);
        tv_AadharNameEnglish = (TextView) findViewById(R.id.tv_AadharNameEnglish);

        tv_mobileNumber = (TextView) findViewById(R.id.tv_mobileNumber);
        tv_emailId = (TextView) findViewById(R.id.tv_emailId);
        tv_bankdetails = (TextView) findViewById(R.id.tv_bankdetails);

        tv_ifsc = (TextView) findViewById(R.id.tv_ifsc);
        tv_bankacnum = (TextView) findViewById(R.id.tv_bankacnum);
        tv_acholdername = (TextView) findViewById(R.id.tv_acholdername);
        txtheaderBD = (TextView) findViewById(R.id.txtheaderBD);

        lbl_benid = (TextView) findViewById(R.id.lbl_benid);
        tv_benid = (TextView) findViewById(R.id.tv_benid);
        lin_sendPfms = (LinearLayout) findViewById(R.id.lin_sendPfms);

        imgsave=findViewById(R.id.imgsave);
        imgupload=findViewById(R.id.imgupload);


        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }

        conversionTable = new ConversionTable();


//        et_studentnameHindi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (!hasFocus) {
//                    String s=et_studentnameHindi.getText().toString().trim();
//                    if(s.length()>1)
//                    {
//                        String Transformed_String = conversionTable.transform(s.toString());
//
//                        if(!isInputInEnglish(Transformed_String)) {
//                            et_studentNameEnglish.setText(Transformed_String);
//                        }
//                        else
//                        {
//                            if(_lang.equalsIgnoreCase("en")) {
//                                Toast.makeText(EditBendDetailsActivity.this, "Please enter student name in Hindi.", Toast.LENGTH_LONG).show();
//                                showAlert("STUDENT NAME","Please enter student name in Hindi.","OK");
//                            }
//                            else
//                            {
//                                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में छात्र का नाम दर्ज करें", Toast.LENGTH_LONG).show();
//                                showAlert("छात्र का नाम","कृपया हिंदी में छात्र का नाम दर्ज करें |","ओके");
//                            }
//                            //et_studentnameHindi.requestFocus();
//                            et_studentnameHindi.setText("");
//
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(et_studentnameHindi, InputMethodManager.SHOW_IMPLICIT);
//                            imm.hideSoftInputFromWindow(et_studentNameEnglish.getWindowToken(), 0);
//
//                        }
//                    }
//                }
//            }
//        });

//        et_fathernameHindi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (!hasFocus) {
//                    String s=et_fathernameHindi.getText().toString().trim();
//                    if(s.length()>1)
//                    {
//                        String Transformed_String = conversionTable.transform(s.toString());
//                        // et_fatherNameEnglish.setText(Transformed_String);
//                        if(!isInputInEnglish(Transformed_String)) {
//                            et_fatherNameEnglish.setText(Transformed_String);
//                        }
//                        else
//                        {
//                            if(_lang.equalsIgnoreCase("en")) {
//                                Toast.makeText(EditBendDetailsActivity.this, "Please enter father name in Hindi.", Toast.LENGTH_LONG).show();
//                                showAlert("FATHER NAME","Please enter father name in Hindi.","ओके");
//                            }
//                            else
//                            {
//                                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में पिता का नाम दर्ज करें", Toast.LENGTH_LONG).show();
//                                showAlert("पिता का नाम","कृपया हिंदी में पिता का नाम दर्ज करें|","ओके");
//                            }
//                            et_fathernameHindi.setText("");
//                           // et_fathernameHindi.requestFocus();
//                        }
//
//                    }
//                }
//            }
//        });


        et_mothersnameHindi.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    String s=et_mothersnameHindi.getText().toString().trim();
                    if(s.length()>1)
                    {
                        String Transformed_String = conversionTable.transform(s.toString());
                       // et_mothersNameEnglish.setText(Transformed_String);

                        if(!isInputInEnglish(Transformed_String)) {
                            et_mothersNameEnglish.setText(Transformed_String);
                        }
                        else
                        {
                            if(_lang.equalsIgnoreCase("en")) {
                                Toast.makeText(EditBendDetailsActivity.this, "Please enter mother name in Hindi.", Toast.LENGTH_LONG).show();
                                showAlert("MOTHER NAME","Please enter mother name in Hindi.","ओके");
                            }
                            else
                            {
                                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में माँ का नाम दर्ज करें", Toast.LENGTH_LONG).show();
                                showAlert("माँ का नाम","कृपया हिंदी में माँ का नाम दर्ज करें|","ओके");
                            }
                            et_mothersnameHindi.setText("");
                            //et_mothersnameHindi.requestFocus();
                        }
                    }
                }
            }
        });


        spn_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spn_minority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spn_divyang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spn_bpl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spn_actypeholder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 >= 0) {

                    ACHOLDERTYPE currentPhysical = acHolderLists.get(arg2 );
                    _varACHolder_Id = currentPhysical.getACHolderID();
                    _varACHolder_Name = currentPhysical.getACHolderValue();
                    if(_varACHolder_Id.equalsIgnoreCase("0")){
                        et_actypeholder_other.setVisibility(View.VISIBLE);
                    }else{
                        et_actypeholder_other.setVisibility(View.GONE);
                        _varACHolder_Name = "";
                        et_actypeholder_other.setText("");
                    }


                } else {
                    _varACHolder_Id = "0";
                    _varACHolder_Name = "";
                    et_actypeholder_other.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spn_send_pfms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    SENDTOPFMSENTITY currentPhysical = asendPfmsLists.get(arg2 - 1);
                    _varSendPfms_Id = currentPhysical.getSendToPfms_ID();
                    _varSendPfms_Status = currentPhysical.getSendToPfms_Status();
                   // _varGender_NameHn = currentPhysical.getGenderNameHn();

                } else {
                    _varSendPfms_Id = "";
                    _varSendPfms_Status = "";
                   // _varGender_NameHn="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        loadGenderList();
        loadCategoryHNList();
        addminority();
        adddivyang();
        adddbpl();
        loadACHolderType();
        loadSendToPfms();
        setButtonsAndOtherLabels(_lang);

        if(getIntent().hasExtra("BENID"))
        {
            benid=getIntent().getStringExtra("BENID");
            ShowStdDetails(benid);
        }
        if(getIntent().hasExtra("EDIT"))
        {
            if(getIntent().getStringExtra("EDIT").equalsIgnoreCase("VIEW"))
            {
                HideButtonAndSetDisableEdidView();
            }
        }

    }
    public void HideButtonAndSetDisableEdidView()
    {
        imgsave.setVisibility(View.GONE);
        imgupload.setVisibility(View.GONE);

        et_dakhilaNumber.setEnabled(false);
        et_dakhilaDAte.setEnabled(false);
        et_studentNameEnglish.setEnabled(false);
        et_studentnameHindi.setEnabled(false);
        et_fatherNameEnglish.setEnabled(false);
        et_fathernameHindi.setEnabled(false);
        et_mothersNameEnglish.setEnabled(false);
        et_mothersnameHindi.setEnabled(false);
        et_AadharNameEnglish.setEnabled(false);
        et_AadharNumber.setEnabled(false);
        et_dob.setEnabled(false);
        et_emailId.setEnabled(false);
        et_mobileNumber.setEnabled(false);

        et_bankacnum.setEnabled(false);
        et_ifsc.setEnabled(false);

        tv_wardvill.setEnabled(false);
        et_wardvill.setEnabled(false);


        spn_gender.setEnabled(false);
        spn_category.setEnabled(false);
        spn_minority.setEnabled(false);
        spn_divyang.setEnabled(false);
        spn_bpl.setEnabled(false);
        spn_actypeholder.setEnabled(false);

        cal1.setVisibility(View.GONE);
        cal2.setVisibility(View.GONE);
        lin_sendPfms.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        int thisID=0;
        if (view.equals(cal1)) {
            date1=true;
            date2=false;
            thisID=1;
        }
        if (view.equals(cal2)) {
            date2=true;
            date1=false;
            thisID=2;
        }
        // showDialog(0);
        showDialog(thisID);
    }


    protected Dialog onCreateDialog(int id) {
        // return new DatePickerDialog(this, datePickerListener, year, month, day);

//        DatePickerDialog dialog=null;
//        DatePickerDialog dialog2=null;
        y=Calendar.YEAR;m=Calendar.MONTH;d=Calendar.DAY_OF_MONTH;

        //10-02-2019
        //20191002
        if(id==1) {
            DatePickerDialog dialog=null;
            if(adm_dateSave.length()>=8)
            {
                Log.e("DATE ADMIS:",""+adm_dateSave);

                d = Integer.parseInt(adm_dateSave.substring(0, 2));
                m = Integer.parseInt(adm_dateSave.substring(3, 5));
                y = Integer.parseInt(adm_dateSave.substring(6, 10));
                 Log.e("DATE ADMIS:",""+adm_dateSave);
//                adm_dateSave=d+"/"+m+"/"+y;
                adm_dateSave=adm_dateSave;
            }
            dialog = new DatePickerDialog(this, datePickerListener, y, m-1, d);
            dialog.updateDate(y,m-1,d);
            return  dialog;
        }

        else if(id==2)
        {
            DatePickerDialog dialog2=null;
            if(dob_dateSave.length()>=8)
            {
                Log.e("DATE DoB:",""+dob_dateSave);
                d = Integer.parseInt(dob_dateSave.substring(0, 2));
                m = Integer.parseInt(dob_dateSave.substring(3, 5));
                y = Integer.parseInt(dob_dateSave.substring(6, 10));
                Log.e("DATE DoB:",""+dob_dateSave);
//               dob_dateSave=d+"/"+m+"/"+y;
                dob_dateSave=dob_dateSave;
            }
            dialog2 = new DatePickerDialog(this, datePickerListener1, y, m-1, d);
            dialog2.updateDate(y,m-1,d);
            return  dialog2;
        }
//        dialog = new DatePickerDialog(this, datePickerListener, y, m-1, d);
//        dialog.updateDate(y,m-1,d);

        return  null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String ds,strselectedDay="0",strselectedMonth="0";
            if(String.valueOf(selectedDay).trim().length()==1)
            {
                strselectedDay="0"+selectedDay;
            }
            else
            {
                strselectedDay=String.valueOf(selectedDay).trim();
            }
            if(String.valueOf(selectedMonth + 1).trim().length()==1)
            {
                strselectedMonth="0"+(selectedMonth + 1);
            }
            else
            {
                strselectedMonth=String.valueOf(selectedMonth + 1).trim();
            }
            ds=  strselectedDay+ "/"+ strselectedMonth  +  "/"+ selectedYear;
            ds=ds.replace("/","-");

            if(date1)
            {
                et_dakhilaDAte.setText(ds);
                //adm_dateSave=selectedYear+""+strselectedMonth+""+strselectedDay;
                adm_dateSave=ds;
                Log.e("et_dakhilaDAte", ds);
            }
            if(date2)
            {
                et_dob.setText(ds);
                //dob_dateSave=selectedYear+""+strselectedMonth+""+strselectedDay;
                dob_dateSave=ds;
                Log.e("et_dob", ds);
            }
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String ds,strselectedDay="0",strselectedMonth="0";
            if(String.valueOf(selectedDay).trim().length()==1)
            {
                strselectedDay="0"+selectedDay;
            }
            else
            {
                strselectedDay=String.valueOf(selectedDay).trim();
            }
            if(String.valueOf(selectedMonth + 1).trim().length()==1)
            {
                strselectedMonth="0"+(selectedMonth + 1);
            }
            else
            {
                strselectedMonth=String.valueOf(selectedMonth + 1).trim();
            }
            ds=  strselectedDay+ "/"+ strselectedMonth  +  "/"+ selectedYear;
            ds=ds.replace("/","-");

            if(date1)
            {
                et_dakhilaDAte.setText(ds);
                //adm_dateSave=selectedYear+""+strselectedMonth+""+strselectedDay;
                adm_dateSave=ds;
                Log.e("et_dakhilaDAte", ds);
            }
            if(date2)
            {
                et_dob.setText(ds);
                //dob_dateSave=selectedYear+""+strselectedMonth+""+strselectedDay;
                dob_dateSave=ds;
                Log.e("et_dob", ds);
            }
        }
    };
    public void loadGenderList()
    {
        localDB = new DataBaseHelper(EditBendDetailsActivity.this);
        genderlists=localDB.getGenderList();
        if(genderlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-Select-");
            }
            else  if(_lang.equalsIgnoreCase("hn")) {
                StringList.add("-चुनें-");
            }
            int setID=0;
            for(int i=0;i<genderlists.size();i++)
            {

                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(genderlists.get(i).getGenderName());
                }
                else  if(_lang.equalsIgnoreCase("hn")) {
                    StringList.add(genderlists.get(i).getGenderNameHn());
                }
                if(_varGender_Id.equalsIgnoreCase(genderlists.get(i).getGenderID()))
                {
                    setID=(i+1);
                }
            }
            GenderListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_gender.setAdapter(GenderListadapter);
        }
    }
    public void loadCategoryHNList()
    {
        localDB = new DataBaseHelper(EditBendDetailsActivity.this);
        catlists=localDB.getCategoryHNList();
        if(catlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-Select-");
            }
            else  if(_lang.equalsIgnoreCase("hn")) {
                StringList.add("-चुनें-");
            }
            int setID=0;
            for(int i=0;i<catlists.size();i++)
            {

                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(catlists.get(i).getCategoryName());
                }
                else  if(_lang.equalsIgnoreCase("hn")) {
                    StringList.add(catlists.get(i).getCategoryNameHn());
                }
                if(_varGender_Id.equalsIgnoreCase(catlists.get(i).getCategoryID()))
                {
                    setID=(i+1);
                }
            }
            catListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_category.setAdapter(catListadapter);
        }


    }
    public void loadACHolderType()
    {
        localDB = new DataBaseHelper(EditBendDetailsActivity.this);
        acHolderLists=localDB.getACHolderType();
        if(genderlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
//            if(_lang.equalsIgnoreCase("en")) {
//                StringList.add("-Select-");
//            }
//            else  if(_lang.equalsIgnoreCase("hn")) {
//                StringList.add("-चुनें-");
//            }

            int setID=0;
            for(int i=0;i<acHolderLists.size();i++)
            {

                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(acHolderLists.get(i).getACHolderValue());
                }
                else  if(_lang.equalsIgnoreCase("hn")) {
                    StringList.add(acHolderLists.get(i).getACHolderValueHn());
                }
                if(_varACHolder_Id.equalsIgnoreCase(acHolderLists.get(i).getACHolderID()))
                {
                    setID=(i+1);
                }
            }
            AcHolderAdapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_actypeholder.setAdapter(AcHolderAdapter);
        }


    }



    public void loadSendToPfms()
    {

        localDB = new DataBaseHelper(EditBendDetailsActivity.this);
        asendPfmsLists = localDB.getPfmsStatus();
        String[] divNameArray = new String[asendPfmsLists.size() + 1];
        divNameArray[0] = "-चुनें-";
        int i = 1;
        int setID=0;
        for (SENDTOPFMSENTITY gen : asendPfmsLists) {
            divNameArray[i] = gen.getSendToPfms_Status();
            if (checklist == gen.getSendToPfms_ID()){
                setID = i;
            }
            i++;

        }
        SendPfmsAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, divNameArray);
        SendPfmsAdapter.setDropDownViewResource(R.layout.dropdownlist);
        spn_send_pfms.setAdapter(SendPfmsAdapter);

    }
    public void addminority() {
//        minoritytlist.add("--चुने--");
//        minoritytlist.add("हाँ");
//        minoritytlist.add("नहीं");

        if(_lang.equalsIgnoreCase("en")) {
            minoritytlist.add("-Select-");
            minoritytlist.add("Yes");
            minoritytlist.add("No");
        }
        else
        {
            minoritytlist.add("-चुनें-");
            minoritytlist.add("हाँ");
            minoritytlist.add("नहीं");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, minoritytlist);
        spn_minority.setAdapter(dataAdapter);


    }

    public void adddivyang() {
//        divyanglist.add("--चुने--");
//        divyanglist.add("हाँ");
//        divyanglist.add("नहीं");


        if(_lang.equalsIgnoreCase("en")) {
            divyanglist.add("-Select-");
            divyanglist.add("Yes");
            divyanglist.add("No");
        }
        else
        {
            divyanglist.add("-चुनें-");
            divyanglist.add("हाँ");
            divyanglist.add("नहीं");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, divyanglist);
        spn_divyang.setAdapter(dataAdapter);


    }

    public void adddbpl() {
//        bpllist.add("--चुने--");
//        bpllist.add("हाँ");
//        bpllist.add("नहीं");

        if(_lang.equalsIgnoreCase("en")) {
            bpllist.add("-Select-");
            bpllist.add("Yes");
            bpllist.add("No");
        }
        else {
            bpllist.add("-चुनें-");
            bpllist.add("हाँ");
            bpllist.add("नहीं");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, bpllist);
        spn_bpl.setAdapter(dataAdapter);


    }

    public void ShowStdDetails(String benid) {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

        tv_benid.setText(benid);
        EntryList=helper.getDataForStudent(benid);
        for (studentList Entity : EntryList)
        {

            et_dakhilaNumber.setText(Utiilties.getValue(Entity.getStdadmnum()));
            et_dakhilaNumber.setEnabled(false);

            //String admBen=Entity.getStdadmdate()!="anyType{}"?Entity.getStdadmdate():"";
            String admBen=Utiilties.getValue(Entity.getStdadmdate());
            String y,m,d,admdate;
            admdate=admBen;
            try {
                if(admBen!=null) {
                    if (!admBen.equalsIgnoreCase("")) {

//                        y = admBen.substring(0, 4);
//                        m = admBen.substring(4, 6);
//                        d = admBen.substring(6, 8);
//                        admdate = d + "-" + m + "-" + y;
                        admdate = admBen;
                    }
                    et_dakhilaDAte.setText(admdate);
                    adm_dateSave = admBen;
                    et_dakhilaDAte.setEnabled(false);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("EXCEPTION",e.getLocalizedMessage());
            }

            et_studentNameEnglish.setText(Utiilties.getValue(Entity.getStdname()));
            et_studentnameHindi.setText(Utiilties.getValue(Entity.getStdnamehn()));

            et_fatherNameEnglish.setText(Utiilties.getValue(Entity.getStdfname()));
            et_fathernameHindi.setText(Utiilties.getValue(Entity.getStdfnamehn()));

            et_mothersNameEnglish.setText(Utiilties.getValue(Entity.getStdmname()));
            et_mothersnameHindi.setText(Utiilties.getValue(Entity.getStdmnamehn()));


            String y1,m1,d1,tdate;
            String dobBen=Utiilties.getValue(Entity.getStdDOB());
            tdate=dobBen;
            try {
                if(dobBen!=null)
                {
                    if (!dobBen.equalsIgnoreCase("")) {

//                        y1 = dobBen.substring(0, 4);
//                        m1 = dobBen.substring(4, 6);
//                        d1 = dobBen.substring(6, 8);
//                        tdate = d1 + "-" + m1 + "-" + y1;
                        tdate = dobBen;
                    }
                    et_dob.setText(tdate);
                    dob_dateSave = dobBen;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("EXCEPTION","date2 exception");
            }
            if(Entity.getStdgenid()!=null) {
                String indxG = !Entity.getStdgenid().contains("anyType{}") ? Entity.getStdgenid() : "0";
                spn_gender.setSelection(Integer.parseInt(indxG));
                _varGender_Id = indxG;
            }

            if(Entity.getStdcatid()!=null) {
                String indxC = !Entity.getStdcatid().contains("anyType{}") ? Entity.getStdcatid() : "0";
                spn_category.setSelection(Integer.parseInt(indxC));
                _varCategory_Id = indxC;
            }
            if(Entity.get_sendPfms_status()!=null) {
                String indxpf = !Entity.get_sendPfms_status().contains("anyType{}") ? Entity.get_sendPfms_status() : "";
               // spn_send_pfms.setSelection(indxpf);
                _varSendPfms_Id = Entity.get_sendPfms_status();
                _varSendPfms_Status = indxpf;
                spn_send_pfms.setSelection(((ArrayAdapter<String>) spn_send_pfms.getAdapter()).getPosition(Entity.get_sendPfms_status()));
            }

            if(Entity.getStdisminority()!=null) {
                String indxM = !Entity.getStdisminority().contains("anyType{}") ? Entity.getStdisminority() : "0";
                if(indxM.equalsIgnoreCase("Y"))
                {
                    spn_minority.setSelection(1);
                    str_minority = "Y";
                }
                else if(indxM.equalsIgnoreCase("N"))
                {
                    spn_minority.setSelection(2);
                    str_minority = "N";
                }
                else
                {
                    spn_minority.setSelection(0);
                    str_minority = "0";
                }
            }

            if(Entity.getStdishandicaped()!=null) {
                String indxD = !Entity.getStdishandicaped().contains("anyType{}") ? Entity.getStdishandicaped() : "0";

                if (indxD.equalsIgnoreCase("Y")) {
                    spn_divyang.setSelection(1);
                    str_divyang = "Y";
                } else if (indxD.equalsIgnoreCase("N")) {
                    spn_divyang.setSelection(2);
                    str_divyang = "N";
                } else {
                    spn_divyang.setSelection(0);
                    str_divyang = "0";
                }
            }

            if(Entity.getStdisbpl()!=null) {
                String indxBPL = !Entity.getStdisbpl().contains("anyType{}") ? Entity.getStdisbpl() : "0";

                if(indxBPL.equalsIgnoreCase("Y")) {
                    spn_bpl.setSelection(1);
                    str_bpl = "Y";
                }
                else if(indxBPL.equalsIgnoreCase("N")) {
                    spn_bpl.setSelection(2);
                    str_bpl = "N";
                }
                else
                {
                    spn_bpl.setSelection(0);
                    str_bpl = "0";
                }
            }


            et_AadharNumber.setText(Utiilties.getValue(Entity.getStdaadharcardno()));
            et_AadharNameEnglish.setText(Utiilties.getValue(Entity.getStdaadharcardname()));

            et_mobileNumber.setText(Utiilties.getValue(Entity.getStdmobile()));
            et_emailId.setText(Utiilties.getValue(Entity.getStdemailid()));

            et_ifsc.setText(Utiilties.getValue(Entity.getStdifsc()));
            et_bankacnum.setText(Utiilties.getValue(Entity.getStdacnum()));

            if(Entity.getStdacholdertypeid()!=null) {
                String indxAC = !Entity.getStdacholdertypeid().contains("anyType{}") ? Entity.getStdacholdertypeid() : "0";
                if(indxAC.equalsIgnoreCase("0"))
                {
                    indxAC="3";
                }
                else
                {
                    int in=Integer.parseInt(indxAC);
                    in=in-1;
                    indxAC=""+in;
                }
                spn_actypeholder.setSelection(Integer.parseInt(indxAC));
                if(Entity.getStdacholdertypeid().equalsIgnoreCase("0"))
                {
                    et_actypeholder_other.setVisibility(View.VISIBLE);
                    et_actypeholder_other.setText(Entity.getStdacholdertypename());
                }
            }

            et_wardvill.setText(Utiilties.getValue(Entity.getWardVillage()));

        }
    }


    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void onClick_Home(View v)
    {
        finish();
    }
    public void onClick_Save(View v)
    {

        String benid="0";

        if(isValidData().equalsIgnoreCase("yes"))
        {
            if(getIntent().hasExtra("BENID"))
            {
                benid=getIntent().getStringExtra("BENID");
            }
            SQLiteDatabase db = localDB.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("StdRegNum", et_dakhilaNumber.getText().toString());
            values.put("Addmissiondate",adm_dateSave);
            ////values.put("Addmissiondate", et_dakhilaDAte.getText().toString());
            values.put("StdName", et_studentNameEnglish.getText().toString());
            values.put("StdNameHn",  et_studentnameHindi.getText().toString());

            values.put("StdFName",  et_fatherNameEnglish.getText().toString());
            values.put("StdFNameHn", et_fathernameHindi.getText().toString());

            values.put("StdMName",  et_mothersNameEnglish.getText().toString());
            values.put("StdMNameHn",  et_mothersnameHindi.getText().toString());

            values.put("StdGenderID", _varGender_Id);
            values.put("StdGender", _varGender_Name);
            values.put("StdGenderHn", _varGender_NameHn);

            values.put("StdDoB",dob_dateSave);  //et_dob.getText().toString());
            values.put("StdCategoryID", _varCategory_Id);
            values.put("StdCategoryName", _varCategory_Name);
            values.put("StdCategoryNameHn", _varCategory_NameHn);

            values.put("IsMinority", str_minority);
            values.put("IsHandicapped", str_divyang);
            values.put("IsBPL", str_bpl);

            values.put("AadharCardNo", et_AadharNumber.getText().toString());
            values.put("AadharCardName", et_AadharNameEnglish.getText().toString());

            values.put("StdMobNum", et_mobileNumber.getText().toString());
            values.put("EmailId",  et_emailId.getText().toString());
            values.put("WardVillage",  et_wardvill.getText().toString());

            values.put("IFSC",  et_ifsc.getText().toString());
            values.put("AccountNo", et_bankacnum.getText().toString());
            values.put("AccountHolderType_Id", _varACHolder_Id);
            if(_varACHolder_Id.equalsIgnoreCase("0")){
                values.put("AccountHolderType_Name", et_actypeholder_other.getText().toString());
            }else{
                values.put("AccountHolderType_Name", _varACHolder_Name);
            }

            values.put("CreatedBy", diseCode);
            values.put("sendPfms_id", _varSendPfms_Id);
            values.put("sendpfms_status", _varSendPfms_Status);
            String cdate=Utiilties.getDateString("yyyy-MM-dd");
            cdate=cdate.replace("-","");
            values.put("CreatedDate", cdate);
            values.put("IsRecordUpdated", "Y");


            Log.e("CREATED DATE",cdate);


            String[] whereArgs = new String[]{String.valueOf(benid)};
            long c = db.update("StudentListForAttendance", values, "BenID=?", whereArgs);

            if (c > 0) {
                removeShortcut();

                    //Toast.makeText(this, "Record of Beneficiary " + et_studentNameEnglish.getText()+ " is Updated.", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder ab = new AlertDialog.Builder(EditBendDetailsActivity.this);
                    ab.setIcon(R.drawable.saveimgns);
                    ab.setTitle("UPDATED");
                    ab.setMessage("Record of Beneficiary " + et_studentNameEnglish.getText().toString().toUpperCase()+ " is Updated.");
                    ab.setPositiveButton("[ OK ]",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                    ab.show();


                createShortCut();
            }
        }
        else
        {

        }

    }


    public void createShortCut()
    {
        ico.CreateShortCut("","U");
    }

    private void removeShortcut() {
        ico.removeShortcut(getResources().getString(R.string.app_name));
    }
    public void onClick_Upload(View v)
    {
        if (Utiilties.isOnline(EditBendDetailsActivity.this)) {


                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        EditBendDetailsActivity.this);
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
                                UploadRecord(benid);
                            }
                        });

                ab.show();


        }
        else
        {
            if(_lang.equalsIgnoreCase("hn"))
            {
                final AlertDialog alertDialog = new AlertDialog.Builder(EditBendDetailsActivity.this).create();
                alertDialog.setTitle(getResources().getString(R.string.no_internet_titlehn));
                alertDialog.setMessage(getResources().getString(R.string.no_internet_msghn));
                alertDialog.setButton(getResources().getString(R.string.turnon_nowhn), new DialogInterface.OnClickListener() {
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
                final AlertDialog alertDialog = new AlertDialog.Builder(EditBendDetailsActivity.this).create();
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
            }

        }
    }
    public void UploadRecord(String benid) {

        DataBaseHelper placeData = new DataBaseHelper(this);
        SQLiteDatabase db = placeData.getReadableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT * FROM StudentListForAttendance WHERE BenID='"+benid+"'",
                        null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String[] param = new String[42];

                param[0] = cursor.getString(cursor.getColumnIndex("StdRegNum"));
                param[1] = cursor.getString(cursor.getColumnIndex("Addmissiondate"));
                param[2] = cursor.getString(cursor.getColumnIndex("StdName"));
                param[3] = cursor.getString(cursor.getColumnIndex("StdNameHn"));
                param[4] = cursor.getString(cursor.getColumnIndex("StdFName"));
                param[5] = cursor.getString(cursor.getColumnIndex("StdFNameHn"));

                param[6] = cursor.getString(cursor.getColumnIndex("StdMName"));
                param[7] = cursor.getString(cursor.getColumnIndex("StdMNameHn"));

                param[8] = cursor.getString(cursor.getColumnIndex("StdGenderID"));
                param[9] = cursor.getString(cursor.getColumnIndex("StdGender"));
                param[10] = cursor.getString(cursor.getColumnIndex("StdGenderHn"));

                param[11] = cursor.getString(cursor.getColumnIndex("StdCategoryID"));
                param[12] = cursor.getString(cursor.getColumnIndex("StdCategoryName"));
                param[13] = cursor.getString(cursor.getColumnIndex("StdCategoryNameHn"));

                param[14] = cursor.getString(cursor.getColumnIndex("IsMinority"));
                param[15] = cursor.getString(cursor.getColumnIndex("IsHandicapped"));
                param[16] = cursor.getString(cursor.getColumnIndex("IsBPL"));

                param[17] = cursor.getString(cursor.getColumnIndex("AadharCardNo"));
                param[18] = cursor.getString(cursor.getColumnIndex("AadharCardName"));
                param[19] = cursor.getString(cursor.getColumnIndex("StdMobNum"));

                param[20] = cursor.getString(cursor.getColumnIndex("EmailId"));
                param[21] = cursor.getString(cursor.getColumnIndex("IFSC"));
                param[22] = cursor.getString(cursor.getColumnIndex("AccountNo"));
                param[23] = cursor.getString(cursor.getColumnIndex("AccountHolderType_Id"));
                param[24] = cursor.getString(cursor.getColumnIndex("FYearID"));
                param[25] =diseCode;
                param[26] =cursor.getString(cursor.getColumnIndex("StdClassID"));
                param[27] =cursor.getString(cursor.getColumnIndex("StdSectionID"));

                param[28] =cursor.getString(cursor.getColumnIndex("StdDoB"));
                param[29] =cursor.getString(cursor.getColumnIndex("CreatedBy"));
                param[30] =cursor.getString(cursor.getColumnIndex("CreatedDate"));
               // param[31] =cursor.getString(cursor.getColumnIndex("BenID"));
                param[31] =cursor.getString(cursor.getColumnIndex("a_ID"));
                param[32] =cursor.getString(cursor.getColumnIndex("WardVillage"));

                param[33] =cursor.getString(cursor.getColumnIndex("WardCode"));
                param[34] =cursor.getString(cursor.getColumnIndex("BlockCode"));

                param[35] =cursor.getString(cursor.getColumnIndex("CUBy"));
                param[36] =cursor.getString(cursor.getColumnIndex("CUDate"));
                param[37] =cursor.getString(cursor.getColumnIndex("OptVerify"));

                param[38] =getIMEI();
                param[39] =cursor.getString(cursor.getColumnIndex("DistrictCode"));
                //param[40] =cursor.getString(cursor.getColumnIndex("AccountHolderName"));
                param[40] =cursor.getString(cursor.getColumnIndex("AccountHolderType_Name"));
                param[41] =cursor.getString(cursor.getColumnIndex("sendPfms_id"));
                new UploadEditedDetailsOfStudent().execute(param);

            }
        } else {
            Toast.makeText(EditBendDetailsActivity.this, "You have no upload pending !", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        placeData.getReadableDatabase().close();
    }
    private String getIMEI(){
        TelephonyManager tm = null;
        String imei = null;
        MarshmallowPermission permission;
        permission=new MarshmallowPermission(EditBendDetailsActivity.this, Manifest.permission.READ_PHONE_STATE);
        if(permission.result==-1 || permission.result==0)
        {
            try
            {
                tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if(tm!=null) imei = tm.getDeviceId();
            }catch(Exception e){
                imei="000000000000";


                imei = Settings.Secure.getString(EditBendDetailsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return imei;
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



    private class UploadEditedDetailsOfStudent extends AsyncTask<String, Void, String> {

        String _bid;

        private final ProgressDialog progressDialog = new ProgressDialog(EditBendDetailsActivity.this);


        UploadEditedDetailsOfStudent()
        {
//             this._bid=param[31];
        }
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("Uploading ...");
        }

        @Override
        protected String doInBackground(String... param) {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            boolean isTablet=isTablet(EditBendDetailsActivity.this);
            if(isTablet) {
                devicename="Tablet::"+devicename;
                Log.e("DEVICE_TYPE", "Tablet");
            } else {
                devicename="Mobile::"+devicename;
                Log.e("DEVICE_TYPE", "Mobile");
            }


            this._bid=param[31];
            String res= WebServiceHelper.UploadStdUpdatedDetails(EditBendDetailsActivity.this,devicename,app_version,param);

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
                    if(result.contains("रिकॉर्ड सफलतापूर्वक अपडेट हो  गया है.."))
                    {
                        removeShortcut();
                        ContentValues values = new ContentValues();
                        values.put("IsRecordUpdated","N");
                        String[] whereArgsss = new String[]{_bid};
                        SQLiteDatabase db = localDB.getWritableDatabase();
                        long c = db.update("StudentListForAttendance", values, "a_ID=?", whereArgsss);

                        if(_lang.equalsIgnoreCase("en"))
                        {
                            Toast.makeText(getApplicationContext(), "Uploaded to server successfully for ben id "+_bid, Toast.LENGTH_SHORT).show();
                            Log.e("Updated For Ben ID ","");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "बेन आईडी "+_bid + " के लिए सफलतापूर्वक सर्वर पर अपलोड किया गया ", Toast.LENGTH_SHORT).show();
                            Log.e("Updated For Ben ID ","");
                        }
                        createShortCut();
                    }
                    else {
                        if(_lang.equalsIgnoreCase("en"))
                        {
                            Toast.makeText(getApplicationContext(), "Sorry! failed to upload for ben id "+_bid + " \nERROR: "+result.toString() , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "माफ़ कीजिये! बेन आईडी "+_bid + " के लिए अपलोड विफल | \nERROR: "+result, Toast.LENGTH_SHORT).show();
                        }

                        if(_lang.equalsIgnoreCase("hn"))
                        {
                            final AlertDialog alertDialog = new AlertDialog.Builder(EditBendDetailsActivity.this).create();
                            alertDialog.setTitle("FAILED TO UPLOAD");
                            alertDialog.setMessage(result);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    alertDialog.cancel();

                                }
                            });

                            alertDialog.show();
                        }
                        else
                        {
                            final AlertDialog alertDialog = new AlertDialog.Builder(EditBendDetailsActivity.this).create();
                            alertDialog.setTitle("FAILED TO UPLOAD");
                            alertDialog.setMessage(result);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    alertDialog.cancel();
                                }
                            });

                            alertDialog.show();
                        }

                    }
                }
                else
                {
                    if(_lang.equalsIgnoreCase("en")) {
                        Toast.makeText(getApplicationContext(), "Response:NULL, Sorry! failed to upload", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "रेस्पोंस: NULL, क्षमा करें! अपलोड करने में विफल", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            tv_dakhilaNumber.setText("Admission Number:");
            tv_dakhilaDAte.setText("Admission Date:");
            tv_studentnameHindi.setText("Name of Student (in Hindi):");
            tv_studentNameEnglish.setText("Student's name (in English):");

            tv_fathernameHindi.setText("Father's name [without Mr.] (in Hindi):");
            tv_fatherNameEnglish.setText("Father's name [without Mr.] (in English):");
            tv_mothersnameHindi.setText("Mother's name [without Mrs.] (in Hindi):");
            tv_mothersNameEnglish.setText("Mother's name [without Mrs.] (in English):");
            tv_dob.setText("Date of Birth:");

            tv_gender.setText("Gender:");
            tv_category.setText("Category:");
            tv_minority.setText("Minority:");
            tv_divyangstu.setText("Handicapped:");
            tv_bpl.setText("Annual income below 1.5 lakhs:");
            tv_AadharNumber.setText("Student's Aadhar number:");
            tv_AadharNameEnglish.setText("Student's Name in Aadhar Card (in English):");

            tv_mobileNumber.setText("Contact Mobile Number:");
            tv_emailId.setText("Contact email ID:");
            tv_bankdetails.setText("Bank account details");

            tv_ifsc.setText("IFSC:");
            tv_bankacnum.setText("Bank account number:");
            tv_acholdername.setText("The name of the bank account holder:");

            txtheaderBD.setText("Personal Details");
            tv_wardvill.setText("Ward(Urban)-Village(Gramin Chetra):");

            txtHeaderName.setText(R.string.app_name);

            lbl_benid.setText("BENEFICIARY ID: ");
        }
        else
        {
            tv_dakhilaNumber.setText("दाखिला संख्या:");
            tv_dakhilaDAte.setText("दाखिला तिथि:");
            tv_studentnameHindi.setText("विद्यार्थी का नाम (हिंदी मे):");
            tv_studentNameEnglish.setText("विद्यार्थी का नाम (अंग्रेजी में):");

            tv_fathernameHindi.setText("पिता का नाम [श्री के बिना](हिंदी में):");
            tv_fatherNameEnglish.setText("पिता का नाम [श्री के बिना](अंग्रेजी में):");
            tv_mothersnameHindi.setText("माता का नाम [श्रीमती के बिना](हिंदी में):");
            tv_mothersNameEnglish.setText("माता का नाम [श्रीमती के बिना](अंग्रेजी में):");
            tv_dob.setText("जन्म तिथि:");

            tv_gender.setText("लिंग:");
            tv_category.setText("श्रेणी:");
            tv_minority.setText("अल्पसंख्यक:");
            tv_divyangstu.setText("दिव्यांग:");
            tv_bpl.setText("वार्षिक आय 1.5 लाख से कम:");
            tv_AadharNumber.setText("विद्यार्थी का आधार नंबर:");
            tv_AadharNameEnglish.setText("विद्यार्थी का आधार कार्ड में नाम (अंग्रेजी में):");

            tv_mobileNumber.setText("सम्पर्क मोबाइल नंबर:");
            tv_emailId.setText("सम्पर्क ईमेल आईडी:");
            tv_bankdetails.setText("बैंक खाते का विवरण");


            tv_ifsc.setText("आई०एफ०एस०सी० [IFSC]:");
            tv_bankacnum.setText("बैंक खाता संख्या:");
            tv_acholdername.setText("बैंक खाता किसके नाम का है:");
            txtheaderBD.setText("व्यक्तिगत विवरण");
//वार्ड (राउरबन) -विलेज (ग्रामीण क्षेत्र)
            tv_wardvill.setText("वार्ड(शहरी क्षेत्र)/ गांव (ग्रामीण क्षेत्र) : [*]:");

            txtHeaderName.setText(R.string.app_namehn);

            lbl_benid.setText("लाभार्थी आई.डी.: ");
        }

    }
    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public String isValidData()
    {
        String isvalid = "yes";


        if (et_dakhilaNumber.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter admission number.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया प्रवेश संख्या दर्ज करें |", Toast.LENGTH_LONG).show();
            }
            et_dakhilaNumber.requestFocus();
            return "no";
        }
        if (et_dakhilaDAte.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter admission date.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया प्रवेश तिथि दर्ज करें |", Toast.LENGTH_LONG).show();
            }
            et_dakhilaDAte.requestFocus();
            return "no";
        }
        if (et_studentnameHindi.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter student name in Hindi.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में छात्र का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_studentnameHindi.requestFocus();
            return "no";
        }
        if (et_studentNameEnglish.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter student name in English.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया अंग्रेजी में छात्र का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_studentNameEnglish.requestFocus();
            return "no";
        }
        if (et_fathernameHindi.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter father name in Hindi.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में पिता का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_fathernameHindi.requestFocus();
            return "no";
        }
        if (et_fatherNameEnglish.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter father name in English.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया अंग्रेजी में पिता का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_fatherNameEnglish.requestFocus();
            return "no";
        }
        if (et_mothersnameHindi.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter mother name in Hindi.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया हिंदी में माँ का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_mothersnameHindi.requestFocus();
            return "no";
        }
        if (et_mothersNameEnglish.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter mother name in English.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया अंग्रेजी में माँ का नाम दर्ज करें।|", Toast.LENGTH_LONG).show();
            }
            et_mothersNameEnglish.requestFocus();
            return "no";
        }
        //==============

        if((spn_gender != null && spn_gender.getSelectedItem() !=null ))
        {
            if(_lang.equalsIgnoreCase("hn")) {
                if ((String) spn_gender.getSelectedItem() != "-चुनें-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "कृपया लिंग का चयन करें", Toast.LENGTH_LONG).show();
                    spn_gender.requestFocus();
                    return "no";
                }
            }
            else {
                if ((String) spn_gender.getSelectedItem() != "-Select-") {
                    isvalid = "yes";
                } else {

                    Toast.makeText(EditBendDetailsActivity.this, "Please select gender", Toast.LENGTH_LONG).show();
                    spn_gender.requestFocus();
                    return "no";
                }
            }
        }
        //YOB.value < 1918
        if (et_dob.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please select date of birth.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया जन्म तिथि चुनें|", Toast.LENGTH_LONG).show();
            }
            et_dob.requestFocus();
            return "no";
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date dt = sdf.parse(et_dob.getText().toString());
               // Date dt=new Date(et_dob.getText().toString());
                String ddt=et_dob.getText().toString();
                ///=2-2005
                ddt=ddt.substring(6,ddt.length());
                int yr=Integer.parseInt(ddt);
                if(yr<=1918)
                {
                    if(_lang.equalsIgnoreCase("en"))
                    {
                        Toast.makeText(EditBendDetailsActivity.this, "Please select valid date of birth.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(EditBendDetailsActivity.this, "कृपया मान्य जन्म तिथि चुनें|", Toast.LENGTH_LONG).show();
                    }
                    et_dob.requestFocus();
                    return "no";
                }
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }


        }

        if((spn_category != null && spn_category.getSelectedItem() !=null ))
        {
            if(_lang.equalsIgnoreCase("hn")) {
                if ((String) spn_category.getSelectedItem() != "-चुनें-") {
                    isvalid = "yes";
                } else {

                    Toast.makeText(EditBendDetailsActivity.this, "कृपया वर्ग का चयन करें", Toast.LENGTH_LONG).show();

                    spn_category.requestFocus();
                    return "no";
                }
            }
            else {
                if ((String) spn_category.getSelectedItem() != "-Select-") {
                    isvalid = "yes";
                } else {

                    Toast.makeText(EditBendDetailsActivity.this, "Please select category", Toast.LENGTH_LONG).show();

                    spn_category.requestFocus();
                    return "no";
                }
            }
        }

        if((spn_minority != null && spn_minority.getSelectedItem() !=null ))
        {
            if(_lang.equalsIgnoreCase("hn")) {
                if ((String) spn_minority.getSelectedItem() != "-चुनें-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "कृपया अल्पसंख्यक का चयन करें", Toast.LENGTH_LONG).show();
                    spn_minority.requestFocus();
                    return "no";
                }
            }
            else
            {
                if ((String) spn_minority.getSelectedItem() != "-Select-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "Please select minority", Toast.LENGTH_LONG).show();
                    spn_minority.requestFocus();
                    return "no";
                }
            }
        }


        if((spn_send_pfms != null && spn_send_pfms.getSelectedItem() !=null ))
        {

                if ((String) spn_send_pfms.getSelectedItem() != "-चुनें-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "कृपया सेंड pfms का चयन करें", Toast.LENGTH_LONG).show();

                    spn_send_pfms.requestFocus();
                    return "no";
                }

        }

        if((spn_divyang != null && spn_divyang.getSelectedItem() !=null ))
        {
            if(_lang.equalsIgnoreCase("hn")) {
                if ((String) spn_divyang.getSelectedItem() != "-चुनें-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "कृपया दिव्यांग है या नहीं का चयन करें", Toast.LENGTH_LONG).show();

                    spn_divyang.requestFocus();
                    return "no";
                }
            }
            else
            {
                if ((String) spn_divyang.getSelectedItem() != "-Select-") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(EditBendDetailsActivity.this, "Please select is handicapped", Toast.LENGTH_LONG).show();
                    spn_divyang.requestFocus();
                    return "no";
                }
            }
        }

        if (et_AadharNumber.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter aadhaar number", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया आधार संख्या दर्ज करें|", Toast.LENGTH_LONG).show();
            }
            et_AadharNumber.requestFocus();
            return "no";
        }
        else
        {
            if(!validateAadharNumber(et_AadharNumber.getText().toString().trim()))
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(EditBendDetailsActivity.this, "Please enter valid Aadhaar Number", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(EditBendDetailsActivity.this, "कृपया मान्य आधार संख्या दर्ज करें|", Toast.LENGTH_LONG).show();
                }
                et_AadharNumber.requestFocus();
                return "no";
            }
        }

        if (et_AadharNameEnglish.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter name in English as per your aadhaar card", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया अपने आधार कार्ड के अनुसार अंग्रेजी में नाम दर्ज करें|", Toast.LENGTH_LONG).show();
            }
            et_AadharNameEnglish.requestFocus();
            return "no";
        }

        if (et_mobileNumber.getText().toString().trim().length() < 10) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter 10 digit mobile number", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया 10 अंकों का मोबाइल नंबर दर्ज करें|", Toast.LENGTH_LONG).show();
            }
            et_mobileNumber.requestFocus();
            return "no";
        }

//        if (et_emailId.getText().toString().trim().length() <= 0) {
//            if(_lang.equalsIgnoreCase("en"))
//            {
//                Toast.makeText(EditBendDetailsActivity.this, "Please enter email id", Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                Toast.makeText(EditBendDetailsActivity.this, "कृपया ईमेल दर्ज करें|", Toast.LENGTH_LONG).show();
//            }
//            et_emailId.requestFocus();
//            return "no";
//        }
//        else
//        {
//            if(!isValidEmail(et_emailId.getText().toString().trim()))
//            {
//                if(_lang.equalsIgnoreCase("en"))
//                {
//                    Toast.makeText(EditBendDetailsActivity.this, "Please enter valid email id", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(EditBendDetailsActivity.this, "कृपया मान्य ईमेल दर्ज करें|", Toast.LENGTH_LONG).show();
//                }
//                et_emailId.requestFocus();
//                return "no";
//            }
//        }

        if (et_wardvill.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter ward-village.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया वार्ड-गांव दर्ज करें |", Toast.LENGTH_LONG).show();
            }
            et_wardvill.requestFocus();
            return "no";
        }

        if (et_ifsc.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter IFSC code.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया आईएफसी कोड दर्ज करें।", Toast.LENGTH_LONG).show();
            }
            et_ifsc.requestFocus();
            return "no";
        }

        if (et_bankacnum.getText().toString().trim().length() <= 0) {
            if(_lang.equalsIgnoreCase("en"))
            {
                Toast.makeText(EditBendDetailsActivity.this, "Please enter bank account number.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(EditBendDetailsActivity.this, "कृपया बैंक खाता नंबर दर्ज करें |", Toast.LENGTH_LONG).show();
            }
            et_bankacnum.requestFocus();
            return "no";
        }

        if((spn_actypeholder != null && spn_actypeholder.getSelectedItem() !=null ))
        {
//            if((String)spn_actypeholder.getSelectedItem()!="-चुनें-")
//            {


                if(((String) spn_actypeholder.getSelectedItem()).equalsIgnoreCase("Other"))
                {
                    if (et_actypeholder_other.getText().toString().trim().length() <= 0) {
                        if(_lang.equalsIgnoreCase("en"))
                        {
                            Toast.makeText(EditBendDetailsActivity.this, "Please enter other account holder type name.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(EditBendDetailsActivity.this, "कृपया अन्य खाता धारक प्रकार नाम दर्ज करें|", Toast.LENGTH_LONG).show();
                        }
                        et_actypeholder_other.requestFocus();
                        return "no";
                    }
                    else
                    {
                        isvalid="yes";
                    }


                }

//            }
//            else {
//                if(_lang.equalsIgnoreCase("en")) {
//                    Toast.makeText(EditBendDetailsActivity.this, "Please select account holder type", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(EditBendDetailsActivity.this, "कृपया खाता धारक प्रकार का चयन करें", Toast.LENGTH_LONG).show();
//                }
//
//                spn_actypeholder.requestFocus();
//                return "no";
//            }
        }


        return isvalid;
    }

    public void showAlert(String title,String msg,String btnlbl)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(EditBendDetailsActivity.this);
        ab.setIcon(R.drawable.infosml);
        ab.setTitle(title);
        ab.setMessage(msg);
        ab.setPositiveButton("[ "+btnlbl+" ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                });

        ab.show();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean validateAadharNumber(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public static boolean isInputInEnglish(String txtVal)
    {
//        CharSequence inputStr = txtVal;
//        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]*$"));
//        Matcher matcher = pattern.matcher(inputStr);
        if(txtVal.contains("nullnull") || txtVal.contains("null0x0951") || txtVal.contains("0x0951"))
        {
           return true;
        }
        else
        {
            //if pattern does not matches
            return false;
        }
       
    }
    public static boolean isInputInEnglishOLD(String txtVal)
    {

        String regx = "^[a-zA-Z ]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txtVal);
        return matcher.find();
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(EditBendDetailsActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}
class VerhoeffAlgorithm{
    static int[][] d  = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                    {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                    {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                    {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                    {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                    {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                    {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                    {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                    {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
            };
    static int[][] p = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                    {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                    {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                    {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                    {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                    {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                    {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
            };
    static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    public static boolean validateVerhoeff(String num){
        int c = 0;
        int[] myArray = StringToReversedIntArray(num);
        for (int i = 0; i < myArray.length; i++){
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
    }
    private static int[] StringToReversedIntArray(String num){
        int[] myArray = new int[num.length()];
        for(int i = 0; i < num.length(); i++){
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        myArray = Reverse(myArray);
        return myArray;
    }
    private static int[] Reverse(int[] myArray){
        int[] reversed = new int[myArray.length];
        for(int i = 0; i < myArray.length ; i++){
            reversed[i] = myArray[myArray.length - (i + 1)];
        }
        return reversed;
    }






}

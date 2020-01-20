package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

public class AttendanceActivity extends AppCompatActivity {
    DataBaseHelper databaseHelper;
    String diseCode,uotp,version;

    Button btnAttendance,btnViewUpdatedAttendance,btnUploadAttendance;

    TextView txtHeaderName,txtSubHeader,txtStdCount,txtCount3;

    TextView txtCount4;
    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    String _varFyear_Name="All",_varFYear_Id="0";
    String _varFyear_NameHn="सभी";
    ShorCutICON ico;

    DataBaseHelper localDB;
    String _lang="en";
    SQLiteDatabase db;
    long countstd=0;
    long countattstd=0;
    ProgressBar profressBar1;
    Spinner spfyear ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        ico=new ShorCutICON(AttendanceActivity.this);
        btnAttendance = (Button) findViewById(R.id.btnAttendance);
        btnViewUpdatedAttendance = (Button) findViewById(R.id.btnViewUpdatedAttendance);
        btnUploadAttendance = (Button) findViewById(R.id.btnUploadAttendance);

        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        txtSubHeader = (TextView) findViewById(R.id.txtSubHeader);

        txtStdCount = (TextView) findViewById(R.id.txtStdCount);
        txtCount3 = (TextView) findViewById(R.id.txtCount3);
        txtCount4 = (TextView) findViewById(R.id.txtCount4);

        profressBar1=findViewById(R.id.profressBar1);
        profressBar1.setVisibility(View.GONE);

        localDB = new DataBaseHelper(this);
        db = localDB.getWritableDatabase();

        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        uotp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");
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

        spfyear = (Spinner) findViewById(R.id.spFYear);

        spfyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    FYEAR fyear = FYearList.get(arg2 - 1);
                    _varFYear_Id = fyear.getFYearID();
                    _varFyear_Name = fyear.getFYearValue();
                    _varFyear_NameHn = fyear.getFYearValue();
                    // GoToEditStdBasicDetails();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                } else {
                    _varFYear_Id = "0";
                    _varFyear_Name = "Select Academic Year";
                    _varFyear_NameHn = "शैक्षणिक वर्ष का चयन करें";
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
        loadFYearList(spfyear);
        setButtonsAndOtherLabels(_lang);
        resetValueS();
    }

    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetValueS();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            btnAttendance.setText("MARK ATTENDANCE");
            btnViewUpdatedAttendance.setText("VIEW UPDATED ATTENDANCE");
            btnUploadAttendance.setText("UPLOAD UPDATED ATTENDANCE");

            txtHeaderName.setText(R.string.app_name);
            txtSubHeader.setText("BENEFICIARY ATTENDANCE");

        }
        else
        {
            btnAttendance.setText("उपस्थिति मार्क करें");
            btnViewUpdatedAttendance.setText("अपडेटेड उपस्थिति को देखें");
            btnUploadAttendance.setText("अपडेटेड उपस्थिति को अपलोड करें");

            txtHeaderName.setText(R.string.app_namehn);
            txtSubHeader.setText("लाभार्थी की उपस्थिति");
        }

    }
    public void onCLick_ViewAttendance(View v) {
        //showSelectFYearDialog("MARKATTENDANCE");


        if(_varFYear_Id.equalsIgnoreCase("0") || _varFYear_Id==null) {
            showSelectFYearDialog("EDITBASICDETAILS");
        }
        else
        {
            GoToMarkAttendance();
        }
    }

    public void showSelectFYearDialog(final String forWhat) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.row_spinner, null);
        dialogBuilder.setView(dialogView);
        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
//        final Spinner spfyear = (Spinner) dialogView.findViewById(R.id.spFYear);
//
//        loadFYearList(spfyear);
//        spfyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                if (arg2 > 0) {
//
//                    FYEAR fyear = FYearList.get(arg2 - 1);
//                    _varFYear_Id = fyear.getFYearID();
//                    _varFyear_Name = fyear.getFYearValue();
//                    _varFyear_NameHn = fyear.getFYearValue();
//
//                } else {
//                    _varFYear_Id = "0";
//                    _varFyear_Name = "ALl";
//                    _varFyear_NameHn = "All";
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });


        if(_lang.equalsIgnoreCase("en")) {
            dialogBuilder.setTitle("Academic Year");
            dialogBuilder.setMessage("Please Select Academic Year.");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                    dialog.dismiss();
                    //GoToMarkAttendance();
                }
            });
//            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    dialog.dismiss();
//                    //pass
//                }
//            });
        }
        else
        {
            dialogBuilder.setTitle("शैक्षणिक वर्ष");
            dialogBuilder.setMessage("कृपया शैक्षणिक वर्ष का चयन करें ।");

            dialogBuilder.setPositiveButton(R.string.okhn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                    dialog.dismiss();

                    //GoToMarkAttendance();
                }
            });
//            dialogBuilder.setNegativeButton(R.string.cancehn, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    dialog.dismiss();
//                    //pass
//                }
//            });
        }
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void createShortCut()
    {
        ico.CreateShortCut("","U");
    }

    private void removeShortcut() {
        ico.removeShortcut(getResources().getString(R.string.app_name));
    }
    public void loadFYearList(Spinner spfyear)
    {
        localDB = new DataBaseHelper(AttendanceActivity.this);
        FYearList=localDB.getFYearList();
        if(FYearList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("Select Academic Year");
            }
            else
            {
                StringList.add("शैक्षणिक वर्ष का चयन करें");
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
    public void GoToMarkAttendance()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCount();
        if(countstd>0)
        {
            Intent i = new Intent(AttendanceActivity.this, AttendanceListActivity.class);
            i.putExtra("DISECODE", diseCode);
            i.putExtra("MOBILENUM", uotp);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceActivity.this);
            ab.setIcon(R.drawable.infosml);
            if(_lang.equalsIgnoreCase("en"))
            {
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
                ab.setMessage("माफ़ कीजिये! कोई रिकॉर्ड नहीं मिला। कृपया सर्वर से छात्र का विवरण डाउनलोड करने के लिए पहले डाटा सिंक्रोनाइज़ करें ।");
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
    }


    public void onCLick_ViewUpdatedAttendance(View v) {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCountForAttendanceUploading();
        if(countstd>0)
        {
            Intent i = new Intent(AttendanceActivity.this, AttendanceUpdatedListActivity.class);
            i.putExtra("DISECODE", diseCode);
            i.putExtra("MOBILENUM", uotp);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceActivity.this);
            ab.setIcon(R.drawable.infosml);
            if(_lang.equalsIgnoreCase("en")) {
                ab.setTitle("NO RECORDS");
                ab.setMessage("Sorry! no records found.");
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
                ab.setMessage("माफ़ कीजिये! कोई रिकॉर्ड नहीं मिला।");
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
    }
    //onCLick_UploadUpdatedAttendance

    public void onCLick_UploadUpdatedAttendance(View v)
    {
        UploadAttendanceRecord();
    }





    public void noRecordFoundToUploadItOnServer()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceActivity.this);
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

    public void UploadAttendanceRecord() {

        if (Utiilties.isOnline(AttendanceActivity.this)) {

            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            long isfound = helper.getStudentCountForAttendanceUploading();
            if(isfound<=0)
            {

                noRecordFoundToUploadItOnServer();

            }
            else
            {
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        AttendanceActivity.this);

                if(_lang.equalsIgnoreCase("en")) {
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
                                    Cursor cursor = localDB.getStudentIDForAttendanceUploading("0");
                                    Toast.makeText(AttendanceActivity.this, cursor.getCount() + " records uploading...", Toast.LENGTH_SHORT).show();
                                    while (cursor.moveToNext()) {
                                        String id = cursor.getString(cursor.getColumnIndex("id"));
                                        String uby = diseCode;
                                       // String uby = cursor.getString(cursor.getColumnIndex("id"));
                                        String udate = cursor.getString(cursor.getColumnIndex("CreatedDate"));
                                        String dcode = diseCode;
                                       // String bid = cursor.getString(cursor.getColumnIndex("BenID"));
                                        String aid = cursor.getString(cursor.getColumnIndex("a_ID"));
                                        String marked = cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"));
                                        profressBar1.setVisibility(View.VISIBLE);
                                      //  new UploadAttendance(id, uby, udate, dcode, bid, marked).execute();
                                        new UploadAttendance(id, uby, udate, dcode, aid, marked).execute();
                                        // new UploadAttendanceOLD(id,uby,udate,dcode,bid).execute();
                                    }
                                }
                            });

                    ab.show();
                }
                else
                {
                    ab.setTitle("रिकॉर्ड अपलोड करें");
                    ab.setMessage("क्या आपको यकीन है ? सर्वर पर रिकॉर्ड अपलोड करना चाहते हैं?");
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
                                    String newFormat = "yyyy-MM-dd";  ////"dd-MM-yyyy";

                                    String vardatetime = Utiilties.getDateString(newFormat);
                                    Log.e("vardatetime", vardatetime);
                                    vardatetime = vardatetime.replace("-", "");
                                    Log.e("vardatetimeN", vardatetime);
                                    Cursor cursor = localDB.getStudentIDForAttendanceUploading("0");
                                    Toast.makeText(AttendanceActivity.this, cursor.getCount() + " records uploading...", Toast.LENGTH_SHORT).show();
                                    while (cursor.moveToNext()) {
                                        String id = cursor.getString(cursor.getColumnIndex("id"));
                                        String uby = diseCode;
                                        String udate = vardatetime;
                                        String dcode = diseCode;
                                        String aid = cursor.getString(cursor.getColumnIndex("a_ID"));
                                        String marked = cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"));
                                        profressBar1.setVisibility(View.VISIBLE);
                                       // new UploadAttendance(id, uby, udate, dcode, bid, marked).execute();
                                        new UploadAttendance(id, uby, udate, dcode, aid, marked).execute();
                                        // new UploadAttendanceOLD(id,uby,udate,dcode,bid).execute();
                                    }
                                }
                            });

                    ab.show();
                }
            }
        }
        else {

            final AlertDialog alertDialog = new AlertDialog.Builder(AttendanceActivity.this).create();
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


    private class UploadAttendance extends AsyncTask<String, Void, String> {

        String _id,_uby,_udate,_dcode,_aid,_status;

        private final ProgressDialog progressDialog = new ProgressDialog(AttendanceActivity.this);


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
                progressDialog.setMessage("उपस्थिति को अपलोड कर रहा है ...");
            }
        }

        @Override
        protected String doInBackground(String... param) {

            String res= WebServiceHelper.UploadAttendanceData(AttendanceActivity.this,_uby,_udate,_dcode,_aid,_status);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                profressBar1.setVisibility(View.GONE);
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
                        Log.e("Updated For Ben ID ",_aid);
                        removeShortcut();
                        ContentValues values = new ContentValues();
                        values.put("IsAttendanceUpdated","N");
                      //  String[] whereArgsss = new String[]{_bid};
                        String[] whereArgsss = new String[]{_aid};
                        SQLiteDatabase db = localDB.getWritableDatabase();
                        long c = db.update("StudentListForAttendance", values, "a_ID=?", whereArgsss);

                        resetValueS();
                        createShortCut();
                        if(_lang.equalsIgnoreCase("en"))
                        {
                            Toast.makeText(getApplicationContext(), " Attendance uploaded for a_ID " + _aid+" successfully", Toast.LENGTH_SHORT).show();
//                            AlertDialog.Builder ab = new AlertDialog.Builder(AttendanceActivity.this);
//                            ab.setIcon(R.drawable.infosml);
//                            ab.setTitle("NO RECORDS");
//                            ab.setMessage("Sorry! no records found. Please syncronize data first to download student details from server.");
//                            ab.setPositiveButton("[ OK ]",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog,
//                                                            int whichButton) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                            ab.show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "" + _aid+" के लिए उपस्थिति अपलोड किया गया " , Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(_lang.equalsIgnoreCase("en"))
                        {
                            Toast.makeText(getApplicationContext(), "Sorry! failed to upload Attendance for " + _aid+" \nResponse " , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "माफ़ कीजिये! " + _aid+" के लिए उपस्थिति अपलोड करने में विफल रहा  \nResponse " , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "प्रतिक्रिया: NULL, क्षमा करें ! " + _aid + " के लिए उपस्थिति अपलोड करने में विफल रहा" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
    }
    public void resetValueS()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        countstd=helper.getStudentCountForUploading();


       countattstd=helper.getStudentCountForAttendanceUploading();
       txtCount3.setText(String.valueOf(countattstd));
       txtCount4.setText(String.valueOf(countattstd));

       long totalstd=helper.getTOTALStudentCount();
    
       txtStdCount.setText(""+totalstd);
    }
}

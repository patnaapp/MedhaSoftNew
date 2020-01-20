package bih.nic.medhasoft;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import bih.nic.medhasoft.entity.CATEGORY;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;

public class BenDetailsActivity extends AppCompatActivity {

    Button btnViewEdit,btnViewUpdatedRec,btnUpload,btnSearch;
    TextView txtSubHeader,txtHeaderName;
    String diseCode,uotp,version,_lang;
    ShorCutICON ico;
    DataBaseHelper localDB ;
    SQLiteDatabase db;
    TextView txtCount;
    TextView txtCount2;
    TextView txtTStd;
    TextView txtStdCount;
    String _varFyear_Name="All",_varFYear_Id="0";
    String _varFyear_NameHn="सभी";
    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    Spinner spfyear ;
    //onCLick_ViewUpdatedRecords
    //onCLick_ViewEditRecords
    //onCLick_UploadRecords
    ProgressDialog pd1;
    long totalstd=0;
    long totalRstd=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ben_details);

        btnViewEdit=findViewById(R.id.btnViewEdit);
        btnViewUpdatedRec=findViewById(R.id.btnViewUpdatedRec);
        btnUpload=findViewById(R.id.btnUpload);
        btnSearch=findViewById(R.id.btnSearch);
        txtCount=findViewById(R.id.txtCount);
        txtCount2=findViewById(R.id.txtCount2);
        txtTStd=findViewById(R.id.txtTStd);
        txtStdCount=findViewById(R.id.txtStdCount);
        txtSubHeader=findViewById(R.id.txtSubHeader);
        txtHeaderName=findViewById(R.id.txtHeaderName);

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
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                    resetValueS();

                } else {
                    _varFYear_Id = "0";
                    _varFyear_Name = "Select Academic Year";
                    _varFyear_NameHn = "शैक्षणिक वर्ष का चयन करें";
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FYEARID", _varFYear_Id).commit();
                    resetValueS();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        pd1= new ProgressDialog(BenDetailsActivity.this);
        pd1 = new ProgressDialog(BenDetailsActivity.this);
        pd1.setTitle("Data is Uploading Wait");
        pd1.setCancelable(false);

        ico=new ShorCutICON(BenDetailsActivity.this);
        diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        uotp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MOBILENUM", "");
        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
        localDB = new DataBaseHelper(this);
        db = localDB.getWritableDatabase();
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
        loadFYearList(spfyear);
        setButtonsAndOtherLabels(_lang);





    }
//    public void onCLick_SearchBenRecords(View v)
//    {
//       // startActivity(new Intent(BenDetailsActivity.this,SearchActivity.class));
//        startActivity(new Intent(BenDetailsActivity.this,SearchCustomActivity.class));
//        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//    }
    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onCLick_ViewEditRecords(View v) {
      //  _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
        if(_varFYear_Id.equalsIgnoreCase("0") || _varFYear_Id==null) {
            showSelectFYearDialog("EDITBASICDETAILS");
        }
        else
        {
            if(totalRstd==0)
            {
                AlertDialog.Builder ab = new AlertDialog.Builder(BenDetailsActivity.this);
                ab.setIcon(R.drawable.infosml);

                    ab.setTitle("REJECTED RECORDS");
                    ab.setMessage("Sorry! no rejected records found.");
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
                GoToEditStdBasicDetails();
            }

        }
        //startActivity(new Intent(BenDetailsActivity.this,BenDetailsActivity.class));
    }
    public void showSelectFYearDialog(final String forWhat) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.row_spinner, null);
        dialogBuilder.setView(dialogView);
        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");
//        final Spinner spfyear = (Spinner) dialogView.findViewById(R.id.spFYear);

        //loadFYearList(spfyear);
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

                  //  GoToEditStdBasicDetails();
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

                   // GoToEditStdBasicDetails();
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
    public void GoToEditStdBasicDetails()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCount();
        if(countstd>0)
        {
            Intent i = new Intent(BenDetailsActivity.this, ViewEditListActivity.class);
            i.putExtra("DISECODE", diseCode);
            i.putExtra("MOBILENUM", uotp);
            i.putExtra("RECORDTYPE", "ALL");
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("RECORDTYPE", "ALL").commit();
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(BenDetailsActivity.this);
            ab.setIcon(R.drawable.infosml);

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
    }
    public void onCLick_ViewUpdatedRecords(View v)
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCountForUploading();
        if(countstd>0)
        {
            Intent i = new Intent(BenDetailsActivity.this, ViewEditUpdatedListActivity.class);
            i.putExtra("DISECODE", diseCode);
            i.putExtra("MOBILENUM", uotp);
            i.putExtra("RECORDTYPE", "UPDATED");
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("RECORDTYPE", "UPDATED").commit();
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(BenDetailsActivity.this);
            ab.setIcon(R.drawable.infosml);
            if(_lang.equalsIgnoreCase("hn")) {
                ab.setTitle("रिकॉर्ड नहीं");
                ab.setMessage("माफ़ कीजिये! कोई अपडेटेड रिकॉर्ड नहीं मिला।");
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
            else
            {
                ab.setTitle("NO RECORDS");
                ab.setMessage("Sorry! no updated records found.");
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
        }
    }
    
    public void onCLick_UploadRecords(View v)
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCountForUploading();
        if(countstd>0)
        {
            if (Utiilties.isOnline(BenDetailsActivity.this)) {
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
                        BenDetailsActivity.this);
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
                                    //dialog.dismiss();
                                   // dialog.cancel();
                                    UploadRecord();
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
                                    //dialog.dismiss();
                                   // dialog.cancel();
                                    UploadRecord();
                                }
                            });

                    ab.show();
                }
            }
            else
            {
                final AlertDialog alertDialog = new AlertDialog.Builder(BenDetailsActivity.this).create();
                if(_lang.equalsIgnoreCase("en")) {
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
                else
                {
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
            }
            ////txtCount.setText(String.valueOf(countstd));
        }
        else
        {
            noRecordFoundToUploadItOnServer();
        }
    }
    public void UploadRecord() {

        DataBaseHelper placeData = new DataBaseHelper(this);
        SQLiteDatabase db = placeData.getReadableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "SELECT * FROM StudentListForAttendance WHERE IsRecordUpdated='Y'",
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
            if(_lang.equalsIgnoreCase("en")) {
                Toast.makeText(BenDetailsActivity.this, "You have no upload pending!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(BenDetailsActivity.this, "आपके पास कोई अपलोड लंबित नहीं है!", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        placeData.getReadableDatabase().close();
    }

    private String getIMEI(){
        TelephonyManager tm = null;
        String imei = null;
        MarshmallowPermission permission;
        permission=new MarshmallowPermission(BenDetailsActivity.this, Manifest.permission.READ_PHONE_STATE);
        if(permission.result==-1 || permission.result==0)
        {
            try
            {
                tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if(tm!=null) imei = tm.getDeviceId();
            }catch(Exception e){
                imei="000000000000";


                imei = Settings.Secure.getString(BenDetailsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
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

        UploadEditedDetailsOfStudent()
        {
//             this._bid=param[31];
        }
        protected void onPreExecute() {
            super.onPreExecute();

            pd1.show();
//            try {
//                pd.setCancelable(false);
//                if(pd!=null)
//                {
//                    if(pd.isShowing())
//                    {
//                        pd.cancel();
//                    }
//                    else
//                    {
//                        pd.show();
//                        if (_lang.equalsIgnoreCase("en")) {
//                            pd.setMessage("Uploading ...");
//                        } else {
//                            pd.setMessage("अपलोडिंग ...");
//                        }
//                    }
//                }
//
//            }
//            catch (Exception e)
//            {
//
//            }
        }

        @Override
        protected String doInBackground(String... param) {

            String devicename=getDeviceName();
            String app_version=getAppVersion();
            boolean isTablet=isTablet(BenDetailsActivity.this);
            if(isTablet) {
                devicename="Tablet::"+devicename;
                Log.e("DEVICE_TYPE", "Tablet");
            } else {
                devicename="Mobile::"+devicename;
                Log.e("DEVICE_TYPE", "Mobile");
            }

            this._bid=param[31];
            String res= WebServiceHelper.UploadStdUpdatedDetails(BenDetailsActivity.this,devicename,app_version,param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                if (pd1.isShowing()) {
                    pd1.dismiss();
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

                        resetValueS();

                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "Uploaded to server successfully for ben id " + _bid, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), _bid +" बेन आईडी के लिए सफलतापूर्वक सर्वर पर अपलोड किया गया " , Toast.LENGTH_SHORT).show();
                        }
                        Log.e("Updated For Ben ID ","");
                        createShortCut();
                    }
                    else {
                        if(_lang.equalsIgnoreCase("en")) {
                            Toast.makeText(getApplicationContext(), "Sorry! failed to upload for ben id " + _bid + "\nResponse " + result, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "माफ़ कीजिये! बेन आईडी " + _bid + " के लिए अपलोड विफल \nरेस्पोंस " + result, Toast.LENGTH_SHORT).show();
                        }

                        if(_lang.equalsIgnoreCase("hn"))
                        {
                            final AlertDialog alertDialog = new AlertDialog.Builder(BenDetailsActivity.this).create();
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
                            final AlertDialog alertDialog = new AlertDialog.Builder(BenDetailsActivity.this).create();
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
    public void loadFYearList(Spinner spfyear)
    {
        localDB = new DataBaseHelper(BenDetailsActivity.this);
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
    public void noRecordFoundToUploadItOnServer()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(BenDetailsActivity.this);
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
    public void createShortCut()
    {
        ico.CreateShortCut("","U");
    }

    private void removeShortcut() {
        ico.removeShortcut(getResources().getString(R.string.app_name));
    }
    public void resetValueS()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        long countstd=helper.getStudentCountForUploading();
        txtCount.setText(String.valueOf(countstd));
        txtCount2.setText(String.valueOf(countstd));
        totalstd=helper.getTOTALStudentCount();
        totalRstd=helper.getTOTALRejectdStudentCount(_varFYear_Id);
        txtTStd.setText(""+totalRstd); //rejected std
        txtStdCount.setText(""+totalstd);

    }
    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            btnViewEdit.setText("UPDATE REJECTED RECORDS");
//            btnSearch.setText("VIEW REPORT");
            btnViewUpdatedRec.setText("VIEW UPDATED RECORDS");
            btnUpload.setText("UPLOAD UPDATED RECORDS");

            txtHeaderName.setText(R.string.app_name);
            txtSubHeader.setText("BENEFICIARY RECORDS");


        }
        else
        {
            btnViewEdit.setText("अस्वीकृत रिकार्ड्स को अपडेट करें");
//            btnSearch.setText("रिपोर्ट देखें");
            btnViewUpdatedRec.setText("अपडेट रिकॉर्ड को देखें");
            btnUpload.setText("अपडेट रिकॉर्ड को अपलोड करें");

            txtHeaderName.setText(R.string.app_namehn);
            txtSubHeader.setText("लाभार्थी का रिकॉर्ड");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        resetValueS();
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(BenDetailsActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}

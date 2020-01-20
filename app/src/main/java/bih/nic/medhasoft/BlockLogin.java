package bih.nic.medhasoft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;
import java.util.ArrayList;

import bih.nic.medhasoft.R;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.utility.CommonPref;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.Utiilties;


public class BlockLogin extends AppCompatActivity {
    ConnectivityManager cm;
    public static String UserPhoto;
    String version;
    TelephonyManager tm;
    private static String imei;
    DataBaseHelper localDBHelper;
    Context context;
    EditText userName, userPass;
    boolean doubleBackToExitPressedOnce = false;
    TextView txtVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_login);

        localDBHelper = new DataBaseHelper(BlockLogin.this);

        userName=(EditText)findViewById(R.id.edt_userid);
        userPass=(EditText)findViewById(R.id.edt_pass);

        try {
            localDBHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {

            localDBHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
    }

    @SuppressLint("MissingPermission")
    private void getIMEI() {
        context = this;
        //Database Opening
        localDBHelper = new DataBaseHelper(BlockLogin.this);
        localDBHelper = new DataBaseHelper(this);

        MarshmallowPermission permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permission.result == -1 || permission.result == 0) {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (tm != null) imei = tm.getDeviceId();
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) imei = tm.getDeviceId();
                    /* Intent i=new Intent(this,BlockLogin.class);
                     startActivity(i);
	            	 finish();*/
        }
        // AutoCompleteTextView userName = (AutoCompleteTextView) findViewById(R.id.et_User_Id);
        //userName.setText(CommonPref.getUserDetails(getApplicationContext()).get_UserName());
        try {

            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            txtVersion = (TextView) findViewById(R.id.txtVersion);
            txtVersion.setText("app version-" + version +"");

        } catch (PackageManager.NameNotFoundException e) {

        }

    }
    protected void onResume() {
        super.onResume();
        //getIMEI();

    }

    public void Login(View view) {
        initLogin();
    }

    private void initLogin() {
        String[] param = new String[2];
        param[0] = userName.getText().toString();
        param[1] = userPass.getText().toString();

        if (param[0].trim().length() == 0) {
            userName.setError("यह फ़ील्ड आवश्यक है");
        } else if (param[1].trim().length() == 0) {
            userPass.setError("यह फ़ील्ड आवश्यक है");

            //&& param[0].length() > 0 ){

        } else {
            new LoginTask().execute(param);


        }
    }

    private class LoginTask extends AsyncTask<String, Void, BlkUserDetails> {


        private final ProgressDialog dialog = new ProgressDialog(BlockLogin.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(BlockLogin.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("प्रमाणित कर रहा है...");
            this.dialog.show();
        }

        @Override
        protected BlkUserDetails doInBackground(String... param) {


            if (!Utiilties.isOnline(BlockLogin.this))
                return OfflineLogin(param[0],param[1]);
            else
                return WebServiceHelper.AuthenticateBlock(param[0], param[1]);

            //return WebServiceHelper.Authenticate( param[0], param[1]);

        }


        @Override
        protected void onPostExecute(final BlkUserDetails result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

           /* final EditText userPass = (EditText) findViewById(R.id.input_Password);
            final AutoCompleteTextView userName = (AutoCompleteTextView) findViewById(R.id.email);*/

            if (!(result != null) || result.isAuthenticated() == false) {

                alertDialog.setTitle("विफल ");
                alertDialog.setMessage("यूज़र आईडी और पासवर्ड गलत है");
                alertDialog.show();

            } /*else if(!(result.get_IMEI().trim().equalsIgnoreCase(imei)))
            {

                Toast.makeText(getApplicationContext(),
                        "Your Device is not Registered", Toast.LENGTH_SHORT).show();

            }*/ else {
                DataBaseHelper placeData = new DataBaseHelper(BlockLogin.this);

                try {
                    //UsercodeToken = result.getGeneratedUserId() + "@" + result.getToken();
                    GlobalVariables.LoggedUser = result;
                    GlobalVariables.UserId = result.get_UserID();
                    CommonPref.setUserDetails(getApplicationContext(), GlobalVariables.LoggedUser);
                    SQLiteDatabase db = localDBHelper.getReadableDatabase();
                    long c = localDBHelper.insertBlockUserDetails(result);
                    if (c > 0) {
                        insertinShared(result);
                        Intent iUserHome = new Intent(getApplicationContext(), BlockHomeActivity.class);
                        iUserHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iUserHome);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(BlockLogin.this, "लोकल डेटाबेस में इन्सर्ट नहीं हुआ  ", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception ex) {
                    Toast.makeText(BlockLogin.this, "लॉगिन विफल", Toast.LENGTH_SHORT).show();
                }



            }

        }
    }

//        public class GetFyear extends AsyncTask<Void, Void, ArrayList<Fyear>>{
//            public GetFyear() {
//
//            }
//            private final ProgressDialog dialog = new ProgressDialog(BlockLogin.this);
//
//            private final AlertDialog alertDialog = new AlertDialog.Builder(BlockLogin.this).create();
//
//            @Override
//            protected void onPreExecute() {
//                this.dialog.setCanceledOnTouchOutside(false);
//                this.dialog.setMessage("Loading Financial Year.\nPlease wait...");
//                this.dialog.show();
//            }
//
//            @Override
//            protected ArrayList<Fyear> doInBackground(Void... params) {
//                ArrayList<Fyear> res1 = WebServiceHelper.loadFYearList();
//
//                return res1;
//            }
//        }




    protected BlkUserDetails OfflineLogin(String User_ID, String Pwd){
        DataBaseHelper placeData = new DataBaseHelper(BlockLogin.this);
        SQLiteDatabase db = placeData.getReadableDatabase();

        String[] params = new String[]{User_ID.toLowerCase(), Pwd.toLowerCase()};
        Cursor cur = db.rawQuery("SELECT * FROM BlockUserDetails where Blk_UserId=? AND Password=?", params);

        BlkUserDetails BlkUserDetails = new BlkUserDetails();
        if (cur.moveToNext()){

            BlkUserDetails.set_UserID(cur.getString(cur.getColumnIndex("Blk_UserId")));
            BlkUserDetails.set_Password(cur.getString(cur.getColumnIndex("Password")));
            BlkUserDetails.set_UserName(cur.getString(cur.getColumnIndex("Blk_UserName")));

            BlkUserDetails.set_UserRole(cur.getString(cur.getColumnIndex("UserRole")));
            BlkUserDetails.set_DistrictCode(cur.getString(cur.getColumnIndex("DistCode")));
            BlkUserDetails.set_DistrictName(cur.getString(cur.getColumnIndex("DistName")));
            BlkUserDetails.set_BlockCode(cur.getString(cur.getColumnIndex("BlkCode")));
            BlkUserDetails.set_BlockName(cur.getString(cur.getColumnIndex("BlkName")));
       //     BlkUserDetails.isAuthenticated(cur.getString(cur.getColumnIndex("BlockName")));
            //BlkUserDetails.set_mobileno(cur.getString(cur.getColumnIndex("BlockCode")));
//            BlkUserDetails.set_PanchayatCode(cur.getString(cur.getColumnIndex("PanchayatCode")));
//            BlkUserDetails.set_PanchayatName(cur.getString(cur.getColumnIndex("PanchayatName")));
//            // BlkUserDetails.set_IsLock(cur.getString(cur.getColumnIndex("IsLock")));
//            BlkUserDetails.set_IMEI(cur.getString(cur.getColumnIndex("IMEI")));
           // BlkUserDetails.isAuthenticated(true);
        }
        else {

           // BlkUserDetails.set_isAuthenticated(false);
        }
        cur.close();
        db.close();
        return BlkUserDetails;
    }
    public void insertinShared(BlkUserDetails result){


        String Userid=result.get_UserID().trim();
        //String Userrole=result.getUserRole();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER_ID", Userid ).commit();
        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER_ROLE", Userrole ).commit();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "कृपया एप्लिकेशन से बाहर निकलने के लिए डबल क्लिक करें", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() { doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}



package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import bih.nic.medhasoft.R;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.Get_usersalldetails;
import bih.nic.medhasoft.entity.UserDetails;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;


public class Login extends AppCompatActivity {

    EditText txtmobnum,txtdisecode,txtotp;
    private ProgressDialog dialog;
    // DataBaseHelper localDBHelper;
    String[] param;
    UserDetails userDetails;
    // String userpinno;
    DataBaseHelper placeData;
    String userotp,umobnum,udisecode;
    TextView txtversion,tv_block_Login;
    //Spinner spn_logintype;
    String searchitem[] = {"--- Select ---", "INDIVIDUAL", "NODEL"};
    ArrayAdapter psarkari;
    String logintype="0";
    String typevalue;
    String version;
    String _lang="en";
    Animation frombottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        placeData = new DataBaseHelper(Login.this);
        psarkari = new ArrayAdapter(this, R.layout.dropdownlist, searchitem);
        userDetails  = new UserDetails();

        txtdisecode = (EditText) findViewById(R.id.edt_disecode);
        txtmobnum = (EditText) findViewById(R.id.edt_mobnum);
        txtotp = (EditText) findViewById(R.id.edt_otp);

        txtversion = (TextView) findViewById(R.id.txtVersion);
        tv_block_Login = (TextView) findViewById(R.id.tv_block_Login);

        //DEFAULT VALUE
        //Animation
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
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

        try {

            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            txtversion.setText("App Version  "  +  version);
        } catch (PackageManager.NameNotFoundException e) {

        }
        placeData=new DataBaseHelper(Login.this);
        try {
            placeData.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            placeData.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;


        }
        //-

        if(placeData.isUserRegistered())
        {
            Intent intent=new Intent(Login.this, PREHomeActivity.class);
           //Intent intent=new Intent(Login.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        tv_block_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, BlockLogin.class);
                //Intent intent=new Intent(Login.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void SubmitOnclick(View view){

        if(Utiilties.isOnline(Login.this)) {
            Login();
        }
        else
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
    }

    public void Login() {

//        udisecode ="10280105518";// txtdisecode.getText().toString();
//        umobnum = "9507638140";  //txtmobnum.getText().toString();
//        userotp = "3376";  //txtotp.getText().toString();

        udisecode =txtdisecode.getText().toString();
        umobnum = txtmobnum.getText().toString();
        userotp = txtotp.getText().toString();

        boolean cancelRegistration = false;
        String isValied = "yes";
        View focusView = null;

        if (TextUtils.isEmpty(udisecode)) {
            txtdisecode.setError("Enter Dise Code");
            focusView = txtdisecode;
            cancelRegistration = true;
        }
        else if (TextUtils.isEmpty(umobnum)) {
            txtmobnum.setError("Enter Dise Code");
            focusView = txtmobnum;
            cancelRegistration = true;
        } else if (userotp.length() != 4) {
            txtotp.setError("Enter Valid OTP");
            focusView = txtotp;
            cancelRegistration = true;
        }


        if (cancelRegistration) {
            // error in login
            focusView.requestFocus();
        } else {
            //userDetails = new UserDetails();
            userDetails.setDiseCode(udisecode);
            userDetails.setMobile(userotp);
            // new RegistrationTask().execute(userDetails);
            new Getalldetails(udisecode,umobnum,userotp).execute();

        }
    }

    private class Getalldetails extends AsyncTask<String, Void, Get_usersalldetails> {

        private ProgressDialog dialog = new ProgressDialog(Login.this);

        String _dcode="";
        String _mobnum="";
        String _otp="";

        public Getalldetails(String dcode,String mnum, String userotp) {
            this._dcode=dcode;
            this._mobnum=mnum;
            this._otp=userotp;
        }

        protected void onPreExecute() {
            try {
                if(dialog!=null) {
                    dialog.setMessage("Verifying your credential.\nPlease wait...");

                    dialog.show();
                }
                else
                {
                    dialog = new ProgressDialog(Login.this);
                    dialog.setMessage("Verifying your credential.\nPlease wait...");

                    dialog.show();
                }
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Get_usersalldetails doInBackground(String... params) {
            return WebServiceHelper.GetUserallDetails(_dcode,_mobnum,_otp);
        }

        @Override
        protected void onPostExecute(Get_usersalldetails result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            long c =0;
            if (result != null) {
                if (result.get_IsActive().equalsIgnoreCase("Y")) {
                    try {
                        DataBaseHelper placeData = new DataBaseHelper(Login.this);
                        c = placeData.insertAllUserDetails(result);
                        if (c > 0) {
                            // new   DwnldProvisionalMarks().execute();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("DISECODE", _dcode).commit();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("OTP", _otp).commit();
                            Intent intent=new Intent(Login.this, Main2Activity.class);
                            //Intent intent=new Intent(Login.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(Login.this);
                    ab.setTitle("INVALID");
                    ab.setMessage("User is not active");
                    ab.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            });

                    ab.show();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"Response null",Toast.LENGTH_LONG).show();
            }
        }
    }
}


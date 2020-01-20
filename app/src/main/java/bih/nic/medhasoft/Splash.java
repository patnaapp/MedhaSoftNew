package bih.nic.medhasoft;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.Versioninfo;
import bih.nic.medhasoft.utility.CommonPref;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.Utiilties;

public class Splash extends Activity {

    Context context;
    MarshmallowPermission permission;
    long isDataDownloaded=-1;
    public static SharedPreferences prefs;
    @SuppressLint("NewApi")
    ActionBar actionBar;
    private static int SPLASH_TIME_OUT = 1000;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(getApplicationContext());
        setContentView(R.layout.activity_splash);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        context=this;

        checkAppUseMode();
    }


    private void checkAppUseMode()
    {
            boolean net = false;

            permission = new MarshmallowPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
            if (permission.result == -1 || permission.result == 0)
                net = Utiilties.isOnline(Splash.this);
            if (net) {


                    new CheckUpdate().execute();
                    //new downloadFyearData().execute("");

                    //start();

            }
            else if (!prefs.getBoolean("firstTime", false)) {

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        Splash.this).create();
                alertDialog.setTitle(getResources().getString(R.string.no_internet_connection));
                alertDialog.setMessage(getResources().getString(R.string.enable_internet_for_firsttime));
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed


                        GlobalVariables.isOffline = false;
                        Intent I = new Intent(
                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                        alertDialog.cancel();


                        //start();
                    }
                });

                alertDialog.show();
            }
            else {

                if (prefs.getBoolean("firstTime", false))
                    checkOnline();


            }

    }

        private void start() {
        final String check = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        final String checkblk = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USER_ID", "");



        if (!check.equals("")) {
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Splash .this, Main2Activity.class);
                   // Intent i = new Intent(Splash .this, PREHomeActivity.class);
                    startActivity(i);



                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else if (!checkblk.equals("")){
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Splash .this, BlockHomeActivity.class);
                    // Intent i = new Intent(Splash .this, PREHomeActivity.class);
                    startActivity(i);



                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else{
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Splash .this, Login.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    }



    private void showDailog(AlertDialog.Builder ab,
                            final Versioninfo versioninfo) {

        if (versioninfo.isVerUpdated()) {

            if (versioninfo.getPriority() == 0) {

                start();
            } else if (versioninfo.getPriority() == 1) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());

                ab.setPositiveButton(getResources().getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

								/*Intent myWebLink = new Intent(
										android.content.Intent.ACTION_VIEW);
								myWebLink.setData(Uri.parse(versioninfo
										.getAppUrl()));

								startActivity(myWebLink);

								dialog.dismiss();
								finish();
							}*/

                                Intent launchIntent = getPackageManager()
                                        .getLaunchIntentForPackage(
                                                "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
                                // name
                                // and
                                // activity
                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri
                                        .parse("market://details?id="
                                                + getApplicationContext()
                                                .getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                            }


                        });
                ab.setNegativeButton(getResources().getString(R.string.ignore),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                                start();
                            }

                        });

                ab.show();

            } else if (versioninfo.getPriority() == 2) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());
                ab.setPositiveButton(getResources().getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

								/*Intent myWebLink = new Intent(
										android.content.Intent.ACTION_VIEW);
								myWebLink.setData(Uri.parse(versioninfo
										.getAppUrl()));

								startActivity(myWebLink);

								dialog.dismiss();*/

                                Intent launchIntent = getPackageManager()
                                        .getLaunchIntentForPackage(
                                                "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
                                // name
                                // and
                                // activity
                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri
                                        .parse("market://details?id="
                                                + getApplicationContext()
                                                .getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                                // finish();
                            }
                        });
                ab.show();
            }
        } else {

            start();
        }

    }

    private class CheckUpdate extends AsyncTask<Void, Void, Versioninfo> {


        CheckUpdate() {

        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Versioninfo doInBackground(Void... Params) {

            TelephonyManager tm = null;
            String imei = null;

            permission=new MarshmallowPermission(Splash.this,Manifest.permission.READ_PHONE_STATE);
            if(permission.result==-1 || permission.result==0)
            {
                try
                {
                    tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    if(tm!=null) imei = tm.getDeviceId();
                }catch(Exception e){}
            }

            String version = null;
            try {
                version = getPackageManager().getPackageInfo(getPackageName(),
                        0).versionName;
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Versioninfo versioninfo = WebServiceHelper.CheckVersion(imei,version);

            return versioninfo;

        }

        @Override
        protected void onPostExecute(final Versioninfo versioninfo) {

            final AlertDialog.Builder ab = new AlertDialog.Builder(	Splash.this);
            ab.setCancelable(false);
            if (versioninfo != null && versioninfo.isValidDevice()) {

                CommonPref.setCheckUpdate(getApplicationContext(),	System.currentTimeMillis());

                if (versioninfo.getAdminMsg().trim().length() > 0
                        && !versioninfo.getAdminMsg().trim()
                        .equalsIgnoreCase("anyType{}")) {

                    ab.setTitle(versioninfo.getAdminTitle());
                    ab.setMessage(Html.fromHtml(versioninfo.getAdminMsg()));
                    ab.setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();

                                    showDailog(ab, versioninfo);

                                }
                            });
                    ab.show();
                } else {
                    showDailog(ab, versioninfo);
                }
            } else {
                if (versioninfo != null) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.wrong_device_text),
                            Toast.LENGTH_LONG).show();

                }
                else
                {
                    start();

                }
            }

        }
    }

    protected void checkOnline() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean net=false;

        MarshmallowPermission permission=new MarshmallowPermission(this,Manifest.permission.READ_PHONE_STATE);
        if(permission.result==-1 || permission.result==0)  net=Utiilties.isOnline(Splash.this);

        if (!net) {

            AlertDialog.Builder ab = new AlertDialog.Builder(	Splash.this);
            ab.setMessage(getResources().getString(R.string.no_internet_connection));
            ab.setPositiveButton(getResources().getString(R.string.turnon_internet),
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
            ab.setNegativeButton(getResources().getString(R.string.continue_offline),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            GlobalVariables.isOffline = true;
                            start();
                        }
                    });


            ab.show();

        } else {

            GlobalVariables.isOffline = false;
            new CheckUpdate().execute();

        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


    }




}









































//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.telephony.TelephonyManager;
//import android.text.Html;
//import android.util.Log;
//import android.widget.Toast;
//
//import bih.nic.medhasoft.R;
//import bih.nic.medhasoft.database.DataBaseHelper;
//import bih.nic.medhasoft.database.WebServiceHelper;
//import bih.nic.medhasoft.entity.Versioninfo;
//import bih.nic.medhasoft.utility.GlobalVariables;
//import bih.nic.medhasoft.utility.MarshmallowPermission;
//import bih.nic.medhasoft.utility.Utiilties;
//
//
//public class Splash  extends Activity {
//    private static int SPLASH_TIME_OUT = 1000;
//    DataBaseHelper databaseHelper;
//    MarshmallowPermission permission;
//    public static SharedPreferences prefs;
//    Context context;
//    String imei = "", version = null;
//    Context ctx;
//
//    LocationManager locationManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        ctx = this;
////        locationManager=(LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
////        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
////                2000,
////                10, locationListenerGPS);
////        isLocationEnabled();
//
////        if(Utiilties.isGPSEnabled(SplashActivity.this))
////        {
////            //ok no need to do any thing
////        }
////        else
////        {
////            AlertDialog.Builder alertDialog=new AlertDialog.Builder(ctx);
////            alertDialog.setTitle("Enable Location");
////            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
////            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
////                public void onClick(DialogInterface dialog, int which){
////                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                    startActivity(intent);
////                }
////            });
////            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
////                public void onClick(DialogInterface dialog, int which){
////                    dialog.cancel();
////                }
////            });
////            AlertDialog alert=alertDialog.create();
////            alert.show();
////        }
//
//    }
//    LocationListener locationListenerGPS=new LocationListener() {
//        @Override
//        public void onLocationChanged(android.location.Location location) {
//            double latitude=location.getLatitude();
//            double longitude=location.getLongitude();
//            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
//            Log.e("Date",""+location.getTime());
//            Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };
//
//    private void isLocationEnabled() {
//
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(ctx);
//            alertDialog.setTitle("इनेबल लोकेशन");
//            alertDialog.setMessage("आपके स्थान की सेटिंग इनेबल नहीं है। कृपया इसे सेटिंग मेनू में इनेबल करें.");
//            alertDialog.setPositiveButton("लोकेशन सेटिंग", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            });
//            alertDialog.setNegativeButton("कैंसिल", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert=alertDialog.create();
//            alert.show();
//        }
//        else{
//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(ctx);
//            alertDialog.setTitle("Confirm Location");
//            alertDialog.setMessage("Your Location is enabled, please enjoy");
//            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert=alertDialog.create();
//            alert.show();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);
//        if (permission.result == -1 || permission.result == 0) {
//            try {
//                imei = getimeinumber();
//                checkOnline();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (permission.result == 1) {
//            imei = getimeinumber();
//            checkOnline();
//        } else {
//            finish();
//        }
//        super.onResume();
//
//
//    }
//
//    public String getimeinumber() {
//        String identifier = null;
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm != null)
//            identifier = tm.getDeviceId();
//        if (identifier == null || identifier.length() == 0)
//            identifier = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
//        return identifier;
//    }
//
//    protected void checkOnline() {
//        // TODO Auto-generated method stub
//        super.onResume();
//
//
//        if (Utiilties.isOnline(Splash .this) == false) {
//
//            AlertDialog.Builder ab = new AlertDialog.Builder(
//                    Splash .this);
//            ab.setTitle("Alert Dialog !!!");
//            ab.setMessage(Html.fromHtml("<font color=#000000>इंटरनेट कनेक्शन उपलब्ध नहीं है|... \n कृपया नेटवर्क कनेक्शन चालू करें \n इंटरनेट कनेक्शन चालू करने के लिए हाँ बटन दबाये या ऑफलाइन बटन दबाये |.</font>"));
//            ab.setPositiveButton("हाँ",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//                            GlobalVariables.isOffline = false;
//                            Intent I = new Intent(
//                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                            startActivity(I);
//                        }
//                    });
//            ab.setNegativeButton("ऑफलाइन",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//
//                           /* GlobalVariables.isOffline = true;
//                            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
//                            startActivity(i);
//                            finish();*/
//                            start();
//                        }
//                    });
//
//
//            ab.show();
//
//        } else {
//
//            GlobalVariables.isOffline = false;
//            new CheckUpdate().execute();
//        }
//    }
//
//
//    private class CheckUpdate extends AsyncTask<Void, Void, Versioninfo> {
//
//
//        CheckUpdate() {
//
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            // this.dialog.setMessage("Checking for update...");
//            // this.dialog.setMessage("Loading...");
//            // this.dialog.show();
//        }
//
//        @Override
//        protected Versioninfo doInBackground(Void... Params) {
//
//
//            String version = null;
//            try {
//                version = getPackageManager().getPackageInfo(getPackageName(),
//                        0).versionName;
//            } catch (PackageManager.NameNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            Versioninfo versioninfo = WebServiceHelper.CheckVersion(imei,version);
//
//
//            return versioninfo;
//        }
//
//        @Override
//        protected void onPostExecute(final Versioninfo versioninfo) {
//
//            final AlertDialog.Builder ab = new AlertDialog.Builder(
//                    Splash .this);
//            ab.setCancelable(false);
//            if (versioninfo != null) {
//
////                CommonPref.setCheckUpdate(getApplicationContext(),
////                        System.currentTimeMillis());
//
//                if (versioninfo.getAdminMsg().trim().length() > 0
//                        && !versioninfo.getAdminMsg().trim()
//                        .equalsIgnoreCase("anyType{}")) {
//
//                    ab.setTitle(versioninfo.getAdminTitle());
//                    ab.setMessage(Html.fromHtml(versioninfo.getAdminMsg()));
//                    ab.setPositiveButton("ओके",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int whichButton) {
//                                    dialog.dismiss();
//
//                                    showDailog(ab, versioninfo);
//
//                                }
//                            });
//                    ab.show();
//                } else {
//                    showDailog(ab, versioninfo);
//                }
//            } else {
//                if (versioninfo != null) {
//                    Toast.makeText(getApplicationContext(), "आपका डिवाइस पंजीकृत नहीं है !",
//                            Toast.LENGTH_LONG).show();
//
//                }
//                /*Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                startActivity(i);
//                finish();*/
//                dothis();
//            }
//
//        }
//    }
//
////    public String getappversion() {
////        String versionCode = null;
////        PackageManager manager = this.getPackageManager();
////        try {
////            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
////            String packageName = info.packageName;
////            versionCode = String.valueOf(info.versionCode);
////            String versionName = info.versionName;
////        } catch (PackageManager.NameNotFoundException e) {
////            // TODO Auto-generated catch block
////        }
////
////
////        return versionCode;
////    }
//
//
//    private void showDailog(AlertDialog.Builder ab,
//                            final Versioninfo versioninfo) {
//
//        if (versioninfo.isVerUpdated()) {
//
//            if (versioninfo.getPriority() == 0) {
//
//                /*
//                 * Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                 * startActivity(i); finish();
//                 */
//                dothis();
//            } else if (versioninfo.getPriority() == 1) {
//
//                ab.setTitle(versioninfo.getUpdateTile());
//                ab.setMessage(versioninfo.getUpdateMsg());
//
//                // ab.setMessage("New version of App is available. Please update the App before proceeding. Do you want to update now?");
//
//                // ab.setMessage(Html
//                // .fromHtml("<font color=#000000>New Version of Application is available. Please update the application before proceeding. Do you want to update now?</font>"));
//                ab.setPositiveButton("अपडेट",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//                                /*
//                                 * Intent myWebLink = new Intent(
//                                 * android.content.Intent.ACTION_VIEW);
//                                 * myWebLink.setData(Uri.parse(versioninfo
//                                 * .getAppUrl()));
//                                 *
//                                 * startActivity(myWebLink);
//                                 */
//
//                                Intent launchIntent = getPackageManager()
//                                        .getLaunchIntentForPackage(
//                                                "com.android.vending");
//                                ComponentName comp = new ComponentName(
//                                        "com.android.vending",
//                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
//                                // name
//                                // and
//                                // activity
//                                launchIntent.setComponent(comp);
//                                launchIntent.setData(Uri
//                                        .parse("market://details?id="
//                                                + getApplicationContext()
//                                                .getPackageName()));
//
//                                try {
//                                    startActivity(launchIntent);
//                                    finish();
//                                } catch (android.content.ActivityNotFoundException anfe) {
//                                    startActivity(new Intent(
//                                            Intent.ACTION_VIEW, Uri
//                                            .parse(versioninfo
//                                                    .getAppUrl())));
//                                    finish();
//                                }
//
//                                dialog.dismiss();
//                            }
//                        });
//                ab.setNegativeButton("इगनोर",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                                // GlobalVariables.isOffline
//                                // = true;
//
//                                dialog.dismiss();
//
//                                /*Intent i = new Intent(getBaseContext(),
//                                        LoginActivity.class);
//                                startActivity(i);
//                                finish();*/
//                                dothis();
//                            }
//
//                        });
//
//                ab.show();
//
//            } else if (versioninfo.getPriority() == 2) {
//
//                ab.setTitle(versioninfo.getUpdateTile());
//                ab.setMessage(versioninfo.getUpdateMsg());
//                // ab.setMessage("Please update your App its required. Click on Update button");
//
//                ab.setPositiveButton("अपडेट",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//                                /*
//                                 * Intent myWebLink = new Intent(
//                                 * android.content.Intent.ACTION_VIEW);
//                                 * myWebLink.setData(Uri.parse(versioninfo
//                                 * .getAppUrl()));
//                                 *
//                                 * startActivity(myWebLink);
//                                 */
//                                Intent launchIntent = getPackageManager()
//                                        .getLaunchIntentForPackage(
//                                                "com.android.vending");
//                                ComponentName comp = new ComponentName(
//                                        "com.android.vending",
//                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
//                                // name
//                                // and
//                                // activity
//                                launchIntent.setComponent(comp);
//                                launchIntent.setData(Uri
//                                        .parse("market://details?id="
//                                                + getApplicationContext()
//                                                .getPackageName()));
//
//                                try {
//                                    startActivity(launchIntent);
//                                    finish();
//                                } catch (android.content.ActivityNotFoundException anfe) {
//                                    startActivity(new Intent(
//                                            Intent.ACTION_VIEW, Uri
//                                            .parse(versioninfo
//                                                    .getAppUrl())));
//                                    finish();
//                                }
//
//                                dialog.dismiss();
//                                // finish();
//                            }
//                        });
//                ab.show();
//            }
//        } else {
//
//            /*Intent i = new Intent(getBaseContext(), LoginActivity.class);
//            startActivity(i);
//            finish();*/
//
//            dothis();
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//
//    }
//
//    private void dothis() {
//
//        if (!Utiilties.isOnline(Splash .this)) {
//
//            AlertDialog.Builder ab = new AlertDialog.Builder(Splash .this);
//            ab.setMessage(Html.fromHtml("<font color=#000000>इंटरनेट कनेक्शन उपलब्ध नहीं है..कृपया नेटवर्क कनेक्शन चालू करें </font>"));
//            ab.setPositiveButton("नेटवर्क कनेक्शन चालू करें", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                    startActivity(I);
//                }
//            });
//
//            ab.create();
//            ab.show();
//
//        } else {
//
//
//          /*  Intent i = new Intent(SplashActivity.this, HomeActivity.class);
//            startActivity(i);
//            finish();*/
//            start();
//
//        }
//    }
//
//
//    private void start() {
//        final String check = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
//        if (!check.equals("")) {
//            new Handler().postDelayed(new Runnable() {
//
//                /*
//                 * Showing splash screen with a timer. This will be useful when you
//                 * want to show case your app logo / company
//                 */
//
//                @Override
//                public void run() {
//
//                    // This method will be executed once the timer is over
//                    // Start your app main activity
//                    Intent i = new Intent(Splash .this, PREHomeActivity.class);
//                    startActivity(i);
//
//
//
//                    // close this activity
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//        }else{
//            new Handler().postDelayed(new Runnable() {
//
//                /*
//                 * Showing splash screen with a timer. This will be useful when you
//                 * want to show case your app logo / company
//                 */
//
//                @Override
//                public void run() {
//                    // This method will be executed once the timer is over
//                    // Start your app main activity
//                    Intent i = new Intent(Splash .this, Login.class);
//                    startActivity(i);
//
//                    // close this activity
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//        }
//
//    }
//}

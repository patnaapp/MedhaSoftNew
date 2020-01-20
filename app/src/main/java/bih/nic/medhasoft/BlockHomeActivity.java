package bih.nic.medhasoft;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.utility.CommonPref;
import bih.nic.medhasoft.utility.MarshmallowPermission;

public class BlockHomeActivity extends AppCompatActivity {
    LinearLayout lin_VerifyQr;
    DataBaseHelper localDB ;
    SQLiteDatabase db;
    TextView user_name_blk,user_role_blk,user_dist,blkNm;
    MarshmallowPermission permission;
    boolean init;
    LinearLayout reject;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    ImageView menu_inflater_st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_home);

        localDB = new DataBaseHelper(this);
        //databaseHelper=new DataBaseHelper(getApplicationContext());
        db = localDB.getWritableDatabase();


        menu_inflater_st = findViewById(R.id.menu_inflater_stnew);
        menu_inflater_st.bringToFront();

        try {
            if (ActivityCompat.checkSelfPermission(BlockHomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(BlockHomeActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        CREATETABLEIFNOTEXIST1();
        CREATETABLEIFNOTEXIST2();
        initialization();
        localDB.deleteQRCODESERIALTable();
        lin_VerifyQr=findViewById(R.id.lin_VerifyQr);

        user_dist.setText("District:-"+CommonPref.getUserDetails(getApplicationContext()).get_DistrictName());
        blkNm.setText("Block Name:-"+CommonPref.getUserDetails(getApplicationContext()).get_BlockName());
        user_name_blk.setText("User Name:-"+CommonPref.getUserDetails(getApplicationContext()).get_UserName());
        user_role_blk.setText("User Role:-"+CommonPref.getUserDetails(getApplicationContext()).get_UserRole());

        lin_VerifyQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BlockHomeActivity.this,ScanQrCodeActivity1.class);
                startActivity(i);
                //finish();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SearchUniqueNoManual.class);
                startActivity(intent);
            }
        });


        menu_inflater_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BlockHomeActivity.this, menu_inflater_st);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_logout_st) {
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER_ID","").commit();
                            Intent i = new Intent(getBaseContext(), BlockLogin.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(i);

                            finish();
                            return true;
//
                        }
//                        if (id == R.id.hindi_key) {
//                           // CheckWhetherTheAppIsInstalledOrNot();
//
//
//                            return true;
////
//                        }
                        return true;


                    }
                });

                popup.show();
            }
        });


    }


    public void CREATETABLEIFNOTEXIST1() {

        db = localDB.getWritableDatabase();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS QRCodeTAble (id TEXT, blockCode TEXT, uniqueCode TEXT, status TEXT,message TEXT, benname TEXT, accno TEXT, ifsc TEXT, totaldata TEXT, totaldatareprt TEXT, totalrem TEXT, totalearlier TEXT)");
            localDB.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "QRCodeTAble");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CREATE Failed ", "QRCodeTAble");
        }
    }
    public void CREATETABLEIFNOTEXIST2() {

        db = localDB.getWritableDatabase();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS QRcodeSerialNo (blockcode TEXT, serialNo TEXT, uniqNo TEXT,status TEXT,benname TEXT,accno TEXT,ifsc TEXT)");
            localDB.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "QRcodeSerialNo");
            localDB.getWritableDatabase().close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CREATE Failed ", "QRcodeSerialNo");
        }
    }

    public void initialization(){

        user_name_blk=findViewById(R.id.user_name_blk);
        user_role_blk=findViewById(R.id.user_role_blk);
        user_dist=findViewById(R.id.user_dist);
        blkNm=findViewById(R.id.blkNm);
        reject=(LinearLayout)findViewById(R.id.reject);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CREATETABLEIFNOTEXIST1();
        localDB.deleteQRCODESERIALTable();
    }
}

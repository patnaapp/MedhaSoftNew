package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.BarcodeAdaptor;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.entity.UserDetails;
import bih.nic.medhasoft.utility.CommonPref;
import bih.nic.medhasoft.utility.GlobalVariables;

public class BarcodeScannerListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;

    ProgressDialog pd1;
    DataBaseHelper dataBaseHelper;
    ArrayList<BarcodeEntity> benfiLists = new ArrayList<>();
    ArrayList<BarcodeEntity> listrandom=new ArrayList<BarcodeEntity>();
    ArrayList<BarcodeEntity> listrandom1=new ArrayList<BarcodeEntity>();
    UserDetails userDetails;
    String Userid = "";
    String _varClass_Name="All",_varClass_Id="0";
    String _varClass_NameHn="सभी";
    TextView pdfno,pdftotcount,pdflink;
    String deleteRow="",scanintent="";
    Button btn;
    TextView tv_final;
    String QRCODE="",_UniqNO="";
    String BLOCKCODE="";
    TextView txtmessage,totalReprt,totalearlier,totalremaining,total;
    LinearLayout lin_main;
    int profile_counts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner_list);
        pdfno=(TextView)findViewById(R.id.pdfno);
        totalReprt=(TextView)findViewById(R.id.totalReprt);
        total=(TextView)findViewById(R.id.total);
        totalearlier=(TextView)findViewById(R.id.totalearlier);
        totalremaining=(TextView)findViewById(R.id.totalremaining);
        // pdftotcount=(TextView)findViewById(R.id.pdftotcount);
        pdflink=(TextView)findViewById(R.id.pdflink);
        tv_final=(TextView)findViewById(R.id.tv_final);
        btn=(Button) findViewById(R.id.btn);
        txtmessage=(TextView)findViewById(R.id.txtmessage);
        lin_main=(LinearLayout)findViewById(R.id.lin_main);
        btn.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        dataBaseHelper = new DataBaseHelper(BarcodeScannerListActivity.this);

        pd1 = new ProgressDialog(BarcodeScannerListActivity.this);
        pd1.setTitle("Data is downloading Wait");
        pd1.setCancelable(false);


        _UniqNO= PreferenceManager.getDefaultSharedPreferences(BarcodeScannerListActivity.this).getString("uniqueno", "");

        Userid = PreferenceManager.getDefaultSharedPreferences(BarcodeScannerListActivity.this).getString("UserId", "");

        scanintent=getIntent().getStringExtra("flag");

        if(getIntent().getStringExtra("flag")!=null){
            if(scanintent.equals("2") ){
                deleteRow=getIntent().getStringExtra("delete_row");
                long b= dataBaseHelper.UpdateQRCODESERIAL(deleteRow);

                listrandom1=dataBaseHelper.getQRCODESerial();


                for (int i=0;i<listrandom1.size();i++){
                    if(listrandom1.get(listrandom1.size()-1).getStatus().equalsIgnoreCase("Y")){
                        btn.setVisibility(View.VISIBLE);
                        tv_final.setVisibility(View.VISIBLE);
                    }
                }
                benfiLists=dataBaseHelper.getQRCODE();

                for (int i=0;i<benfiLists.size();i++){
                    pdfno.setText("PDF UNIQUE NO :- "+benfiLists.get(i).getUniqueNo());
                    totalReprt.setText("Total Data Report :- "+benfiLists.get(i).getTotalDataReprt());
                    total.setText("Total Data :- "+benfiLists.get(i).getTotalData());

                    totalremaining.setText("Total Data Remaining :- "+benfiLists.get(i).getTotalDataRemaning());
                    totalearlier.setText("Total Data Earlier :- "+benfiLists.get(i).getTotalDataEarlier());
                }

                recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(recylerViewLayoutManager);

                recyclerViewAdapter = new BarcodeAdaptor(BarcodeScannerListActivity.this, benfiLists,listrandom1);
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.notifyDataSetChanged();

            }else if(scanintent.equals("1")){
                // QRCODE=getIntent().getStringExtra("barcode");
                //  BLOCKCODE= CommonPref.getUserDetails(getApplicationContext()).get_BlockCode();
                //  new BarcodeRequest(BLOCKCODE,QRCODE).execute();

                listrandom1=dataBaseHelper.getQRCODESerial();


                for (int i=0;i<listrandom1.size();i++){
                    if(listrandom1.get(listrandom1.size()-1).getStatus().equalsIgnoreCase("Y")){
                        btn.setVisibility(View.VISIBLE);
                        tv_final.setVisibility(View.VISIBLE);
                    }
                }
                benfiLists=dataBaseHelper.getQRCODE();

                for (int i=0;i<benfiLists.size();i++){
                    pdfno.setText("PDF UNIQUE NO :- "+benfiLists.get(i).getUniqueNo());
                    totalReprt.setText("Total Data Report :- "+benfiLists.get(i).getTotalDataReprt());
                    totalremaining.setText("Total Data Remaining :- "+benfiLists.get(i).getTotalDataRemaning());
                    totalearlier.setText("Total Data Earlier :- "+benfiLists.get(i).getTotalDataEarlier());
                    total.setText("Total Data :- "+benfiLists.get(i).getTotalData());
                }

                recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(recylerViewLayoutManager);

                recyclerViewAdapter = new BarcodeAdaptor(BarcodeScannerListActivity.this, benfiLists,listrandom1);
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.notifyDataSetChanged();


            }
        }


        pdflink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BarcodeScannerListActivity.this,QrCodeWebViewActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private class BarcodeRequest extends AsyncTask<String, Void, BarcodeEntity> {
        String _intentdata,_distcode,_blkcode;

        BarcodeRequest(String intentdata,String dist)
        {
            this._intentdata=intentdata;
            this._distcode=dist;

        }

        private final ProgressDialog dialog = new ProgressDialog(BarcodeScannerListActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(BarcodeScannerListActivity.this).create();
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected BarcodeEntity doInBackground(String... strings) {
            return WebServiceHelper.UploadBarcodeResponse(BLOCKCODE,QRCODE);


        }



        @Override
        protected void onPostExecute( BarcodeEntity result) {

            if ((result != null) ) {

                if (result.getStatus().equalsIgnoreCase("Y")) {

                    Log.d("fuygfghf","cdhhdcbj"+result);
                    BarcodeEntity.barlist[] lectures1 = result.getStudentLecture();

                    for (int a=0;a<lectures1.length;a++){
                        String aaa= String.valueOf(lectures1[a].getName());
                        String benname= String.valueOf(lectures1[a].getBenId());
                        String acno= String.valueOf(lectures1[a].getAcNo());
                        String ifsc= String.valueOf(lectures1[a].getIFSC());
                        BarcodeEntity barcodeEntity=new BarcodeEntity();
                        barcodeEntity.setSerialNo(aaa);
                        barcodeEntity.setBenName(benname);
                        barcodeEntity.setAccountNo(acno);
                        barcodeEntity.setIFSC(ifsc);
                        barcodeEntity.setStatus("N");
                        barcodeEntity.setUniqueNo(result.getUniqueNo());
                        listrandom.add(barcodeEntity);
                        dataBaseHelper.insertSerialQR(result.getBlockCode(),result.getUniqueNo(),aaa,benname,acno,ifsc,"N");
                    }

                    benfiLists.add(result);
                    dataBaseHelper.insertQR(result);
                    pdfno.setText("PDF UNIQUE NO :- "+result.getUniqueNo());
                    recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
                    if (listrandom.size()==0){
                        btn.setVisibility(View.VISIBLE);

                        tv_final.setVisibility(View.VISIBLE);
                    }
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("uniqueno", result.getUniqueNo()).commit();
                    recyclerView.setLayoutManager(recylerViewLayoutManager);
                    recyclerViewAdapter = new BarcodeAdaptor(BarcodeScannerListActivity.this, benfiLists,listrandom);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();


                } else if(result.getStatus().equalsIgnoreCase("N")) {
                    lin_main.setVisibility(View.GONE);
                    txtmessage.setVisibility(View.VISIBLE);
                    txtmessage.setText(result.getMessage());
//                    alertDialog.setTitle("विफल ");
//                    alertDialog.setMessage("Wrong QR CODE");
//                    alertDialog.show();
                }else if(result.getStatus().equalsIgnoreCase("V")){
                    lin_main.setVisibility(View.GONE);
                    txtmessage.setVisibility(View.VISIBLE);
                    txtmessage.setText(result.getMessage());
                    alertDialog.setTitle("Message ");
                    alertDialog.setMessage("Already Verified Record ");
                    alertDialog.show();
                }

            }else {

                alertDialog.setTitle("wrong response null ");
                alertDialog.setMessage("Wrong QR CODE");
                alertDialog.show();

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),BlockHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        dataBaseHelper.deleteQRCODESERIALTable();

        startActivity(intent);
    }

    public void onclick_FinalSubmit(View view){

        android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(
                BarcodeScannerListActivity.this);
        ab.setTitle("Upload");
        ab.setMessage("Do You Want to Finalize Data");


        ab.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();


            }
        });

        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

                DataBaseHelper dbHelper = new DataBaseHelper(BarcodeScannerListActivity.this);
                profile_counts = dbHelper.gettotalCount(_UniqNO);
                ArrayList<BarcodeEntity> dataProgress = dbHelper.getQRCODESerial();
                if (dataProgress.size() > 0) {
                    GlobalVariables.listSize = dataProgress.size();

                   // for (BarcodeEntity data : dataProgress) {

                      //  new UPLOADDATA(data).execute();
                        new UPLOADDATA().execute();

                   // }

                } else {
                    Toast.makeText(getApplicationContext(), "No Data to Upload", Toast.LENGTH_SHORT).show();

                }


            }
        });

        ab.create().getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        ab.show();




    }



    private class UPLOADDATA extends AsyncTask<String, Void, BarcodeEntity> {
        BarcodeEntity data;
        String _uid;
        private final ProgressDialog dialog = new ProgressDialog(BarcodeScannerListActivity.this);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(BarcodeScannerListActivity.this).create();

//
//        UPLOADDATA(BarcodeEntity data) {
//            this.data = data;
//            this._uid = data.getUniqueNo();
//
//        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("UpLoading...");
            if (!BarcodeScannerListActivity.this.isFinishing()) {
                this.dialog.show();
            }
        }

        @Override
        protected BarcodeEntity doInBackground(String... param) {

//
//            String res = WebServiceHelper.UploadFinalData(data, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USERID", ""));
//            return res;

            return WebServiceHelper.UploadFinalData(data,CommonPref.getUserDetails(getApplicationContext()).get_UserID(),_UniqNO);

        }

        @Override
        protected void onPostExecute(BarcodeEntity result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null) {


                if (result.getSerialStatus().equals("Y")) {

                    long c = dataBaseHelper.deleteQRCODESERIAL(_uid);
                    if (c > 0) {

                        long c1 = dataBaseHelper.gettotalCount(_UniqNO);
                        if (c1 == 0) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BarcodeScannerListActivity.this);

                            builder.setTitle("Uploaded");
                            builder.setMessage(result.getSerialmessage());
                            //   builder.setIcon(R.drawable.ic_rcd);
                            builder.setPositiveButton("[OK]", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i=new Intent(BarcodeScannerListActivity.this,BlockHomeActivity.class);
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                    // showPending();

                                }
                            });
                            android.support.v7.app.AlertDialog dialog = builder.create();
                            if (!BarcodeScannerListActivity.this.isFinishing()) {
                                dialog.show();
                            }
                        }

                    }

                }
                else  if (result.getSerialStatus().equals("N")){
                    Toast.makeText(getApplicationContext(), "Uploading data failed ", Toast.LENGTH_SHORT).show();

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BarcodeScannerListActivity.this);

                    builder.setTitle("Uploading Failed");
                    builder.setMessage(result.getSerialmessage());
                    //   builder.setIcon(R.drawable.ic_rcd);
                    builder.setPositiveButton("[OK]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            // showPending();

                        }
                    });
                    android.support.v7.app.AlertDialog dialog = builder.create();
                    if (!BarcodeScannerListActivity.this.isFinishing()) {
                        dialog.show();
                    }


                }

            } else {
                //chk_msg_OK_networkdata("Uploading failed.Please Try Again Later");
                Toast.makeText(getApplicationContext(), "Uploading failed. Result Null..Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}

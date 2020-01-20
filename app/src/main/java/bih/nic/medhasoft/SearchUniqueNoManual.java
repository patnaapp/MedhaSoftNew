package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import bih.nic.medhasoft.Model.searchuniqModel;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.utility.CommonPref;

public class SearchUniqueNoManual extends AppCompatActivity {
    EditText edtsearch,edtremarks;
    ImageView btnsearch;
    LinearLayout lin_remraks,linear_rjct;
    Button btnreject;
    String remarks="";
    String _BlockCode="";
    ArrayList<BarcodeEntity> benfiLists = new ArrayList<>();
    ArrayList<BarcodeEntity> listrandom=new ArrayList<BarcodeEntity>();
    DataBaseHelper dataBaseHelper;
    TextView totalrjct,totalReprtrjct,totalearlierrjct,totalremainingrjct,pdflink_rjct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_unique_no_manual);

        dataBaseHelper=new DataBaseHelper(getApplicationContext());

        edtsearch=(EditText)findViewById(R.id.edtsearch);
        edtremarks=(EditText)findViewById(R.id.edtremarks);
        btnsearch=(ImageView) findViewById(R.id.btnsearch);
        lin_remraks=(LinearLayout) findViewById(R.id.lin_remraks);
        linear_rjct=(LinearLayout) findViewById(R.id.linear_rjct);
        btnreject=(Button) findViewById(R.id.btnreject);
        totalrjct=(TextView) findViewById(R.id.totalrjct);
        totalReprtrjct=(TextView) findViewById(R.id.totalReprtrjct);
        totalearlierrjct=(TextView) findViewById(R.id.totalearlierrjct);
        totalremainingrjct=(TextView) findViewById(R.id.totalremainingrjct);
        pdflink_rjct=(TextView) findViewById(R.id.pdflink_rjct);

        btnreject.setEnabled(false);



        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtsearch.getText()!=null){
                    if(edtsearch.getText().toString().equalsIgnoreCase("")) {
                        new SearchUniqNo(_BlockCode,edtsearch.getText().toString()).execute();
                    }
                }

            }
        });


        btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectuniqueno();
            }
        });

        pdflink_rjct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SearchUniqueNoManual.this,QrCodeWebViewActivity.class);
                startActivity(i);
            }
        });

        _BlockCode= CommonPref.getUserDetails(getApplicationContext()).get_BlockCode();

    }





//    private class SearchUniqNo extends AsyncTask<String, Void, searchuniqModel> {
//        String unqNo,serialNo,qrdata;
//
//        SearchUniqNo(String unino)
//        {
//            this.unqNo=unino;
//
//        }
//
//        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
//
//        private final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
//        @Override
//        protected void onPreExecute() {
//
//            //  this.dialog.setCanceledOnTouchOutside(false);
//            //  this.dialog.setMessage("प्रमाणित कर रहा है...");
//            // this.dialog.show();
//        }
//        @Override
//        protected searchuniqModel doInBackground(String... strings) {
//            return WebServiceHelper.DataAsPerUniqNo(unqNo);
//
//
//        }
//
//
//
//        @Override
//        protected void onPostExecute( searchuniqModel result) {
//
//
//            if ((result != null)) {
//                //   BarcodeAdaptor.count=_SerialNO;
//
//
//            }
//        }
//
//    }



    private class SearchUniqNo extends AsyncTask<String, Void, BarcodeEntity> {
        String blkCode,_intenydata;

        SearchUniqNo(String blkcode,String intdata)
        {
            this.blkCode=blkcode;
            this._intenydata=intdata;

        }

        private final ProgressDialog dialog = new ProgressDialog(SearchUniqueNoManual.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(SearchUniqueNoManual.this).create();
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected BarcodeEntity doInBackground(String... strings) {
            // return WebServiceHelper.UploadBarcodeResponse(blkCode,_intenydata);
            return WebServiceHelper.UploadBarcodeResponse(blkCode,"nijMYHv7bKzUWymVdQ/sVlKlGOhwsQl+");


        }



        @Override
        protected void onPostExecute( BarcodeEntity result) {

            if ((result != null) ) {

                if (result.getStatus().equalsIgnoreCase("Y")) {

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
                        barcodeEntity.setTotalData(result.getTotalData());
                        barcodeEntity.setTotalDataReprt(result.getTotalDataReprt());
                        barcodeEntity.setTotalDataRemaning(result.getTotalDataRemaning());
                        barcodeEntity.setTotalDataEarlier(result.getTotalDataEarlier());
                        listrandom.add(barcodeEntity);
                        dataBaseHelper.insertSerialQR(result.getBlockCode(),result.getUniqueNo(),aaa,benname,acno,ifsc,"N");
                    }
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("uniqueno", result.getUniqueNo()).commit();
                    benfiLists.add(result);
                    dataBaseHelper.insertQR(result);

                    benfiLists=dataBaseHelper.getQRCODE();

                    for (int i=0;i<benfiLists.size();i++){
//                        pdfno.setText("PDF UNIQUE NO :- "+benfiLists.get(i).getUniqueNo());
                        totalReprtrjct.setText("Total Data Report :- "+benfiLists.get(i).getTotalDataReprt());
                        totalremainingrjct.setText("Total Data Remaining :- "+benfiLists.get(i).getTotalDataRemaning());
                        totalearlierrjct.setText("Total Data Earlier :- "+benfiLists.get(i).getTotalDataEarlier());
                        totalrjct.setText("Total Data :- "+benfiLists.get(i).getTotalData());
                    }

                    lin_remraks.setVisibility(View.VISIBLE);
                    linear_rjct.setVisibility(View.VISIBLE);
                    btnreject.setEnabled(true);




                } else if(result.getStatus().equalsIgnoreCase("N")) {
                    Toast.makeText(getApplicationContext(),"Wrong Unique Number",Toast.LENGTH_LONG).show();

                }else if(result.getStatus().equalsIgnoreCase("V")) {
                    Toast.makeText(getApplicationContext(),"Unique Number Already Rejected",Toast.LENGTH_LONG).show();

                    alertDialog.setTitle("Already Rejected");
                    alertDialog.setMessage("Unique Number");
                    alertDialog.show();
                }

            }else {

                alertDialog.setTitle("wrong response null ");
                alertDialog.setMessage("null");
                alertDialog.show();

            }

        }
    }



    private class RejectData extends AsyncTask<String, Void, BarcodeEntity> {
        BarcodeEntity data;
        String _uid;
        private final ProgressDialog dialog = new ProgressDialog(SearchUniqueNoManual.this);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SearchUniqueNoManual.this).create();


        RejectData() {
//            this.data = data;
//            this._uid = data.getUniqueNo();

        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("UpLoading...");
            if (!SearchUniqueNoManual.this.isFinishing()) {
                this.dialog.show();
            }
        }

        @Override
        protected BarcodeEntity doInBackground(String... param) {

//
//            String res = WebServiceHelper.UploadFinalData(data, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USERID", ""));
//            return res;

            return WebServiceHelper.RejectFinalData("nijMYHv7bKzUWymVdQ/sVlKlGOhwsQl+",CommonPref.getUserDetails(getApplicationContext()).get_UserID(),remarks);

        }

        @Override
        protected void onPostExecute(BarcodeEntity result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null) {


                if (result.getSerialStatus().equals("Y")) {


                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SearchUniqueNoManual.this);

                    builder.setTitle("Rejected Successfully");
                    builder.setMessage(result.getSerialmessage());
                    //   builder.setIcon(R.drawable.ic_rcd);
                    builder.setPositiveButton("[OK]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(SearchUniqueNoManual.this,BlockHomeActivity.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                            // showPending();

                        }
                    });
                    android.support.v7.app.AlertDialog dialog = builder.create();
                    if (!SearchUniqueNoManual.this.isFinishing()) {
                        dialog.show();
                    }




                } else  if (result.getSerialStatus().equals("N")){
                    Toast.makeText(getApplicationContext(), "Rejection failed ", Toast.LENGTH_SHORT).show();

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SearchUniqueNoManual.this);

                    builder.setTitle("Rejection Failed");
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
                    if (!SearchUniqueNoManual.this.isFinishing()) {
                        dialog.show();
                    }


                }

            } else {
                //chk_msg_OK_networkdata("Uploading failed.Please Try Again Later");
                Toast.makeText(getApplicationContext(), "Rejection failed. Result null..Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void rejectuniqueno(){

        remarks=edtremarks.getText().toString();
        if(remarks.length()>0){
            new RejectData().execute();
        }

        else {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SearchUniqueNoManual.this);

            builder.setTitle("Enter Remarks");
            builder.setMessage("Please Enter Reason For Rejection");
            //   builder.setIcon(R.drawable.ic_rcd);
            builder.setPositiveButton("[OK]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    // showPending();

                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            if (!SearchUniqueNoManual.this.isFinishing()) {
                dialog.show();
            }
        }
    }

}

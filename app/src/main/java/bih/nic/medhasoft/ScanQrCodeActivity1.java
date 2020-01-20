package bih.nic.medhasoft;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.utility.CommonPref;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrCodeActivity1 extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    String BLOCKCODE="";
    DataBaseHelper dataBaseHelper;
    ArrayList<BarcodeEntity> benfiLists = new ArrayList<>();
    ArrayList<BarcodeEntity> listrandom=new ArrayList<BarcodeEntity>();
    String Intentdata="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code1);
        dataBaseHelper=new DataBaseHelper(getApplicationContext());

        ActivityCompat.requestPermissions(ScanQrCodeActivity1.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ScanQrCodeActivity1.this, "Permission denied to camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
      //  Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        Intentdata=result.getText();
        BLOCKCODE= CommonPref.getUserDetails(getApplicationContext()).get_BlockCode();
        new BarcodeRequest(BLOCKCODE,result.getText()).execute();
    }


    private class BarcodeRequest extends AsyncTask<String, Void, BarcodeEntity> {
        String blkCode,_intenydata;

        BarcodeRequest(String blkcode,String intdata)
        {
            this.blkCode=blkcode;
            this._intenydata=intdata;

        }

        private final ProgressDialog dialog = new ProgressDialog(ScanQrCodeActivity1.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(ScanQrCodeActivity1.this).create();
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected BarcodeEntity doInBackground(String... strings) {
            return WebServiceHelper.UploadBarcodeResponse(blkCode,_intenydata);


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


                    Intent intent=new Intent(getApplicationContext(),BarcodeScannerListActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("flag","1");

                    intent.putExtra("barcode",Intentdata);
                    startActivity(intent);

                    finish();


                } else if(result.getStatus().equalsIgnoreCase("N")) {
                    Toast.makeText(getApplicationContext(),"Wrong QR Code",Toast.LENGTH_LONG).show();

                }else if(result.getStatus().equalsIgnoreCase("V")) {
                    Toast.makeText(getApplicationContext(),"QR Code Already verified",Toast.LENGTH_LONG).show();

                    alertDialog.setTitle("Already Verified");
                    alertDialog.setMessage(" QR CODE");
                    alertDialog.show();
                }

            }else {

                alertDialog.setTitle("wrong response null ");
                alertDialog.setMessage("Wrong QR CODE");
                alertDialog.show();

            }

        }
    }
}

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

import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrCodeActivity2 extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    String _SerialNO="",_UniqNO="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code2);
        ActivityCompat.requestPermissions(ScanQrCodeActivity2.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        try {
            // _UniqNO=getIntent().getStringExtra("unqno");
            _UniqNO= PreferenceManager.getDefaultSharedPreferences(ScanQrCodeActivity2.this).getString("uniqueno", "");

            _SerialNO=getIntent().getStringExtra("SerialNo");
            Toast.makeText(getApplicationContext(),_UniqNO+_SerialNO,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ScanQrCodeActivity2.this, "Permission denied to camera", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        new BarcodeRequestSecond(_UniqNO,_SerialNO,result.getText()).execute();

    }



    private class BarcodeRequestSecond extends AsyncTask<String, Void, BarcodeEntity> {
        String unqNo,serialNo,qrdata;

        BarcodeRequestSecond(String unino,String serialNo,String intentdata)
        {
            this.unqNo=unino;
            this.serialNo=serialNo;
            this.qrdata=intentdata;
        }

        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        private final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        @Override
        protected void onPreExecute() {

            //  this.dialog.setCanceledOnTouchOutside(false);
            //  this.dialog.setMessage("प्रमाणित कर रहा है...");
            // this.dialog.show();
        }
        @Override
        protected BarcodeEntity doInBackground(String... strings) {
            return WebServiceHelper.UploadSerialBarcodeResponse(_UniqNO,_SerialNO,qrdata);


        }



        @Override
        protected void onPostExecute( BarcodeEntity result) {


            if ((result != null) ) {
                //   BarcodeAdaptor.count=_SerialNO;

                if(result.getSerialStatus().equalsIgnoreCase("Y")) {
                    Toast.makeText(getApplicationContext(),"QR Code Verified .",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BarcodeScannerListActivity.class);
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    intent.putExtra("flag", "2");
                    intent.putExtra("delete_row", _SerialNO);
                    startActivity(intent);
                    finish();
                }
                else if(result.getSerialStatus().equalsIgnoreCase("N")){

                    Toast.makeText(getApplicationContext(),"Wrong QR CODE",Toast.LENGTH_LONG).show();

                }


            }else {

                alertDialog.setTitle("wrong response null ");
                alertDialog.setMessage("Wrong QR Code");
                alertDialog.show();

            }

        }

    }
}

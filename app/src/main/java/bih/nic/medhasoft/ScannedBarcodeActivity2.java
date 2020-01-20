package bih.nic.medhasoft;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import bih.nic.medhasoft.Adapter.BarcodeAdaptor;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;

public class ScannedBarcodeActivity2 extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    String _SerialNO="",_UniqNO="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);


        try {
            // _UniqNO=getIntent().getStringExtra("unqno");
            _UniqNO= PreferenceManager.getDefaultSharedPreferences(ScannedBarcodeActivity2.this).getString("uniqueno", "");

            _SerialNO=getIntent().getStringExtra("SerialNo");
            Toast.makeText(getApplicationContext(),_UniqNO+_SerialNO,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        initViews();

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);


//        btnAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (intentData.length() > 0) {
//                    if (isEmail)
//                        startActivity(new Intent(ScannedBarcodeActivity2.this, Main2Activity.class).putExtra("email_address", intentData));
//                    else {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
//                    }
//                }
//
//
//            }
//        });
    }

    private void initialiseDetectorsAndSources() {

        //    Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity2.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity2.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                // WebServiceHelper.UploadBarcodeResponse(intentData);



                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                                new BarcodeRequestSecond(_UniqNO,_SerialNO,intentData).execute();
                                finish();

                                //WebServiceHelper.UploadBarcodeResponse(intentData);

                            }
                        }
                    });

                }
            }
        });
    }

    private class BarcodeRequestSecond extends AsyncTask<String, Void, BarcodeEntity> {
        String unqNo,serialNo,qrdata;

        BarcodeRequestSecond(String unino,String serialNo,String intentdata)
        {
            this.unqNo=unino;
            this.serialNo=serialNo;
            this.qrdata=intentData;
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


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }





}

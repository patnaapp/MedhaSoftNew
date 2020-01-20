package bih.nic.medhasoft;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.BarcodeAdaptor;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.utility.CommonPref;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.Utiilties;

public class ScannedBarcodeActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    MarshmallowPermission permission;
    boolean init;
    DataBaseHelper dataBaseHelper;
    ArrayList<BarcodeEntity> benfiLists = new ArrayList<>();
    ArrayList<BarcodeEntity> listrandom=new ArrayList<BarcodeEntity>();
    String BLOCKCODE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        dataBaseHelper=new DataBaseHelper(getApplicationContext());

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {
                    if (isEmail)
                        startActivity(new Intent(ScannedBarcodeActivity.this, Main2Activity.class).putExtra("email_address", intentData));
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    }
                }


            }
        });
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
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
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
                //    Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
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
                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                                BLOCKCODE= CommonPref.getUserDetails(getApplicationContext()).get_BlockCode();
                                new BarcodeRequest(BLOCKCODE,intentData).execute();



                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        initialiseDetectorsAndSources();
    }


    private class BarcodeRequest extends AsyncTask<String, Void, BarcodeEntity> {
        String blkCode,_intenydata;

        BarcodeRequest(String blkcode,String intdata)
        {
            this.blkCode=blkcode;
            this._intenydata=intdata;

        }

        private final ProgressDialog dialog = new ProgressDialog(ScannedBarcodeActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(ScannedBarcodeActivity.this).create();
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

                    intent.putExtra("barcode",intentData);
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

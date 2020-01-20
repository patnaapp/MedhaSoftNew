package bih.nic.medhasoft;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.webviewtopdf.PdfView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.utility.CommonPref;

public class QrCodeWebViewActivity extends AppCompatActivity {

    DataBaseHelper localDBHelper;
    public static ProgressDialog progressDialog1;
    WebView showpdfdataWV;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String _filePrefixName="MedhaSoft";
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_web_view);

        localDBHelper = new DataBaseHelper(this);

        showpdfdataWV=findViewById(R.id.showpdfdataWV); //-------------------------
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);


        checkAndRequestPermissions();
        // For url use below fun.

        printRegistrationForm();
    }


    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();


        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public void printRegistrationForm()
    {

        showpdfdataWV.getSettings().setLoadsImagesAutomatically(true);
        showpdfdataWV.getSettings().setJavaScriptEnabled(true);
        showpdfdataWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = showpdfdataWV.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");



        showpdfdataWV.getSettings().setLoadWithOverviewMode(true);
        showpdfdataWV.getSettings().setUseWideViewPort(true);
        showpdfdataWV.getSettings().setBuiltInZoomControls(true);
        showpdfdataWV.getSettings().setDomStorageEnabled(true);
        webSettings.setDomStorageEnabled(true);
        showpdfdataWV.getSettings().setAppCacheEnabled(true);
        // webSettings.setAllowFileAccessFromFileURLs(true);
        showpdfdataWV.getSettings().setLoadsImagesAutomatically(true);
        showpdfdataWV.getSettings().setJavaScriptEnabled(true);
        showpdfdataWV.setWebChromeClient(new WebChromeClient());
        //showpdfdataWV.getSettings().setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        //showpdfdataWV.loadUrl("http://edudbt.bih.nic.in/CDBT/Student/AppPrintMobile.aspx?BenId="+BENID);
        showpdfdataWV.loadUrl("http://aashakiran.bih.nic.in/BSAPKY/AppPrintMob.aspx?CUBY="+ CommonPref.getUserDetails(QrCodeWebViewActivity.this).get_UserID());

        showpdfdataWV.setWebViewClient(new myWebClient());


    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            Log.i("OnPageLoadFinished", url);

            if(progressDialog1.isShowing())
            {
                progressDialog1.dismiss();
            }
            //printOrCreatePdfFromWebview(view);
            generatePDFForHindiFont(view);
        }
    }


    public  void generatePDFForHindiFont(WebView wv)
    {
        //bih.nic.medhasoft
        try {
            Date date = new Date();
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
            final String fileName = _filePrefixName + timeStamp + ".pdf";

            Log.e("FNAME",fileName);
            final ProgressDialog progressDialog = new ProgressDialog(QrCodeWebViewActivity.this);
            progressDialog.setMessage("कृपया प्रतीक्षा करे");
            progressDialog.show();
            PdfView.createWebPrintJob(QrCodeWebViewActivity.this, wv, path, fileName, new PdfView.Callback() {

                @Override
                public void success(String path) {
                    path=path.replace("bih.nic.medhasoft","bih.nic.medhasoft");
                    progressDialog.dismiss();
                    //Toast.makeText(QrCodeWebViewActivity.this, "पीडीऍफ़ ", Toast.LENGTH_SHORT).show();
                    Log.e("PDF", "success");
                    // PdfView.openPdfFile(QrCodeWebViewActivity.this, getString(R.string.app_name), "Do you want to open the pdf file ?\n" + fileName, path);
                    PdfView.openPdfFile(QrCodeWebViewActivity.this, "पीडीऍफ़", "क्या आप पीडीऍफ़ फाइल खोलना चाहते है ?\n" + fileName, path);
                }

                @Override
                public void failure() {
                    progressDialog.dismiss();
                    Toast.makeText(QrCodeWebViewActivity.this, "PDF Generated failure", Toast.LENGTH_SHORT).show();
                    Log.e("PDF", "failure");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("PDF EXCE", e.getLocalizedMessage());
        }
    }
}

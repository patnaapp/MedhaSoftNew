package bih.nic.medhasoft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import bih.nic.medhasoft.utility.Utiilties;


public class SetUpActivity extends Activity {
Button btnlang;
    String _lang="en";
    TextView txtHeaderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
       final WebView wb=(WebView) findViewById(R.id.webview);
        txtHeaderName=findViewById(R.id.txtHeaderName);
        //btnlang=(Button) findViewById(R.id.btnhnen);
        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }

        wb.setWebViewClient(new myWebClient());

        if(_lang.equalsIgnoreCase("en")) {
            wb.loadUrl("file:///android_asset/setup.html");
        }
        else if(_lang.equalsIgnoreCase("hn")) {
            wb.loadUrl("file:///android_asset/setuphindi.html");
        }
        setLabelsTextAsPerLanguage();
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
           //printOrCreatePdfFromWebview(view,"myPDF");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    public  PrintJob printOrCreatePdfFromWebview(WebView webview, String jobName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Context c = webview.getContext();
            PrintDocumentAdapter printAdapter;
            PrintManager printManager = (PrintManager) c.getSystemService(Context.PRINT_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = webview.createPrintDocumentAdapter(jobName);
            } else {
                printAdapter = webview.createPrintDocumentAdapter();
            }
            if (printManager != null) {
                return printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
            }
        } else {
            Log.e(getClass().getName(), "ERROR: Method called on too low Android API version");
        }
        return null;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(SetUpActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void setLabelsTextAsPerLanguage()
    {
        if(_lang.equalsIgnoreCase("en")) {
            txtHeaderName.setText(R.string.app_name);
        }
        else
        {
            txtHeaderName.setText(R.string.app_namehn);
        }
    }
}

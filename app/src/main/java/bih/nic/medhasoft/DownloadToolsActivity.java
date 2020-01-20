package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import bih.nic.medhasoft.utility.GlobalVariables;

public class DownloadToolsActivity extends AppCompatActivity {
    String _lang="en";
    TextView tvSUbHeader;
    Button btnDownloadHindiKP,btnPDFReader,btnSetUpHindiKP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_tools);

        tvSUbHeader=findViewById(R.id.tvSUbHeader);
        btnDownloadHindiKP=findViewById(R.id.btnDownloadHindiKP);
        btnPDFReader=findViewById(R.id.btnPDFReader);
        btnSetUpHindiKP=findViewById(R.id.btnSetUpHindiKP);



        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }
        else
        {
            GlobalVariables.LANG=_lang;
        }

        setButtonsAndOtherLabels(_lang);
    }


    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void onCLick_DownloadHindiKeyBoard(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(DownloadToolsActivity.this);
        ab.setIcon(R.drawable.keyboard);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle("GOOGLE INDIC KEYBOARD");
            ab.setMessage("For typing in Hindi if this app 'GOOGLE INDIC KEYBOARD' is available in your device or if you have other Hindi Typing Keyboard then click on 'CANCEL' or If you want to install it then click on 'INSTALL'?");
            ab.setPositiveButton("[ INSTALL ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            try {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                //Copy App URL from Google Play Store.
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi"));

                                startActivity(intent);
                            } catch (android.content.ActivityNotFoundException anfe) {

                                finish();
                            }

                            dialog.dismiss();
                        }
                    });
            ab.setNegativeButton("[ CANCEL ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    });
            ab.show();
        }
        else
        {
            ab.setTitle("गूगल इंडिक कीबोर्ड");
            ab.setMessage("यदि हिंदी में टाइपिंग के लिए यह ऐप 'गूगल इंडिक कीबोर्ड' आपके डिवाइस में उपलब्ध है या यदि आपके पास अन्य हिंदी टाइपिंग कीबोर्ड है तो 'रद्द करें' पर क्लिक करें या यदि आप इसे इंस्टॉल करना चाहते हैं तो 'इंस्टॉल' पर क्लिक करें ?");
            ab.setPositiveButton("[ इंस्टॉल ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            try {
                                Intent  intent = new Intent(android.content.Intent.ACTION_VIEW);
                                //Copy App URL from Google Play Store.
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.inputmethod.hindi"));
                                // intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.jetstartgames.chess"));

                                startActivity(intent);
                            } catch (android.content.ActivityNotFoundException anfe) {

                                finish();
                            }

                            dialog.dismiss();
                        }
                    });
            ab.setNegativeButton("[ रद्द करें ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    });
            ab.show();
        }

    }

    public void onCLick_DownloadPDFReader(View v)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(DownloadToolsActivity.this);
        ab.setIcon(R.drawable.pdfimg);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle("PDF READER");
            ab.setMessage("If your device does not have PDF Reader, then click on 'INSTALL' to install it.");
            ab.setPositiveButton("[ INSTALL ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            try {
                                Intent  intent = new Intent(android.content.Intent.ACTION_VIEW);
                                //Copy App URL from Google Play Store.
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer"));

                                startActivity(intent);
                            } catch (android.content.ActivityNotFoundException anfe) {

                                finish();
                            }

                            dialog.dismiss();
                        }
                    });
            ab.setNegativeButton("[ CANCEL ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    });
            ab.show();
        }
        else
        {
            ab.setTitle("पीडीएफ़ रीडर");
            ab.setMessage("यदि आपके डिवाइस में पीडीएफ रीडर नहीं है, तो इसे डाउनलोड करने के लिए 'इंस्टॉल' पर क्लिक करें।");
            ab.setPositiveButton("[ इंस्टॉल ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            try {
                                Intent  intent = new Intent(android.content.Intent.ACTION_VIEW);
                                //Copy App URL from Google Play Store.
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer"));

                                startActivity(intent);
                            } catch (android.content.ActivityNotFoundException anfe) {

                                finish();
                            }

                            dialog.dismiss();
                        }
                    });
            ab.setNegativeButton("[ रद्द करें ]",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    });
            ab.show();
        }

    }

    public void onCLick_ShowSetUP(View v)
    {
        startActivity(new Intent(DownloadToolsActivity.this,SetUpActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            tvSUbHeader.setText("DOWNLOAD AND SETUP");
            btnDownloadHindiKP.setText("DOWNLOAD HINDI TYPING TOOL");
            btnPDFReader.setText("DOWNLOAD PDF READER");
            btnSetUpHindiKP.setText("SETUP HINDI TOOL");
        }
        else
        {
            tvSUbHeader.setText("डाउनलोड एंड सेटअप");
            btnDownloadHindiKP.setText("डाउनलोड हिंदी टूल");
            btnPDFReader.setText("डाउनलोड पीडीऍफ़ रीडर");
            btnSetUpHindiKP.setText("सेटअप हिंदी टूल");
        }

    }
}

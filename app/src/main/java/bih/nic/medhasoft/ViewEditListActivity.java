package bih.nic.medhasoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.VIewEditListAdapter;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.FYEAR;
import bih.nic.medhasoft.entity.Genderlist;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.categoryHINDI;
import bih.nic.medhasoft.utility.Utiilties;

//================

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.webviewtopdf.PdfView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;



public class ViewEditListActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog1;
    public static ProgressDialog pd2;

    VIewEditListAdapter studentListAdaptor;
    ListView list_student;

    ArrayList<bih.nic.medhasoft.Model.studentList> studentList=new ArrayList<>();

    Spinner spClass,spSession,spn_fyear;

    DataBaseHelper localDB ;
    SQLiteDatabase db;

    ArrayList<CLASSLIST> ClassList = new ArrayList<>();
    ArrayAdapter<String> ClassListadapter;
    String _varClass_Name="All",_varClass_Id="0";
    String _varClass_NameHn="सभी";

    ArrayList<SESSIONLIST> SessionList = new ArrayList<>();
    ArrayAdapter<String> SessionListadapter;
    String _varSession_Name="None",_varSession_Id="0";
    String _varSession_NameHn="कोई नहीं";


    ArrayAdapter<String> GenderListadapter;
    String _varGender_Name="All",_varGender_Id="0";
    String _varGender_NameHn="सभी";

    // String officer_code;
    String _diseCode="0";
    String _outp="0";

    ArrayList<FYEAR> FYearList = new ArrayList<>();
    ArrayAdapter<String> FYearListadapter;
    String _varFyear_Name="All",_varFYear_Id="0";
    String _varFyear_NameHn="सभी";

    //------------------
    TextView txtHeaderName;
    TextView txtSection;
    TextView txtFyear;
    TextView txtClass;
    TextView tvattendence;
    TextView txt_FName;
    TextView txt_MName;
    TextView txt_DoB;
    TextView txt_Name;
    TextView txt_ANum;
    TextView txt_ACNum, txt_IFSC, txt_AdmDt;
    TextView txt_AdNum, txt_AdName;

    String timeStamp;
    String _filePrefixName="medhasoft";

    TextView showpdfdata;
    TextView txtPDFHeader;
    WebView showpdfdataWV;
    String _schoolName="",_distBlock="";

    ImageView imgFilter;
    public static final String HINDI_FONT = "FreeSans.ttf";

    String _lang="en";

    ArrayList<String> benstatuslist;
    //--------------------------------
    ArrayList<Genderlist> genderlists = new ArrayList<>();
    String _varCategory_Name="all",_varCategory_NameHn="all",_varCategory_Id="0";

    ArrayList<categoryHINDI> catlists = new ArrayList<>();
    ArrayAdapter<String> catListadapter;
    ArrayList<String> minoritytlist;
    ArrayList<String> divyanglist;
    ArrayList<String> atnlist;
    ArrayList<String> havingnonevaluelist;
    ArrayList<String> bpllist;
    String str_minority="0",str_divyang="0",str_bpl="0",str_none_value="0",str_benstatus="0",str_atn="";;
    TextView txtStdCountVE;


    ImageView imgPDF;

    TextView txtStdCount;

    //-----FILTER------------------

    Spinner spnCategory;
    Spinner spnIsMinority;
    Spinner spnGender;
    Spinner spnIsBPL;
    Spinner spnIsHandicaped;
    Spinner spnHavingNonValue;
    Spinner spnAttendance;
    LinearLayout lin_background;
    EditText txtStdNameForSearch;
    //TextView txtAdnStdCount;
    String std_name;
    Spinner spn_benstatus;

    //TextView txtTotalStdLbl;
    TextView tvSUbHeader;
    LinearLayout lnFilterPDF;
    //------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieweditlist);

        localDB = new DataBaseHelper(this);

        db = localDB.getWritableDatabase();

        txtStdCount =(TextView)findViewById(R.id.txtStdCountVE);

        tvattendence =(TextView)findViewById(R.id.tvattendence);

        txtStdCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ViewEditListActivity.this, "Total Student: " + txtStdCount.getText(), Toast.LENGTH_SHORT).show();

            }
        });

        list_student=(ListView)findViewById(R.id.list_student);
        spClass=(Spinner)findViewById(R.id.spn_class);
        spSession=(Spinner)findViewById(R.id.spn_session);
        spn_fyear=(Spinner)findViewById(R.id.spn_fyear);

        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        txtSection = (TextView) findViewById(R.id.txtSection);
        txtFyear = (TextView) findViewById(R.id.txtFyear);
        txtClass = (TextView) findViewById(R.id.txtClass);

        txt_FName = (TextView) findViewById(R.id.txt_FName);
        txt_MName = (TextView) findViewById(R.id.txt_MName);
        txt_DoB = (TextView) findViewById(R.id.txt_Dob);
        txt_Name = (TextView) findViewById(R.id.txt_Name);
        txt_ANum = (TextView) findViewById(R.id.txt_ANum);
        //txt_ACNum, txt_IFSC, txt_AdmDt
        txt_ACNum = (TextView) findViewById(R.id.txt_ACNum);
        txt_IFSC = (TextView) findViewById(R.id.txt_IFSC);
        txt_AdmDt = (TextView) findViewById(R.id.txt_AdmDt);
        //txt_AdNum, txt_AdName;
        txt_AdNum = (TextView) findViewById(R.id.txt_AdNum);
        txt_AdName = (TextView) findViewById(R.id.txt_AdName);

        imgFilter =  findViewById(R.id.imgFilter);
        lnFilterPDF =  findViewById(R.id.lnFilterPDF);
        lin_background =  findViewById(R.id.lnFilterPDF);

        //lnFilterPDF.setVisibility(View.GONE);
        imgPDF =  findViewById(R.id.imgPDF);
        imgPDF.setVisibility(View.GONE);
        lin_background.setVisibility(View.GONE);
        showpdfdata=(TextView)findViewById(R.id.showpdfdata); //-------------------------
        txtPDFHeader=(TextView)findViewById(R.id.txtPDFHeader); //-------------------------
        showpdfdataWV=findViewById(R.id.showpdfdataWV);

        // btnUpload=findViewById(R.id.btnupload);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);
        _diseCode= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("DISECODE", "");
        _outp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("OTP", "");

        _varFYear_Id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("FYEARID", "");

        if(_varFYear_Id!=null)
        {
            if(_varFYear_Id.equalsIgnoreCase(""))
            {
                _varFYear_Id="0";
            }
        }
        else {
            _varFYear_Id="0";
        }


        _lang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG", "");

        if(_lang==null)
        {
            _lang= "en";
        }
        else if(_lang=="")
        {
            _lang= "en";
        }

        list_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentList country = (studentList) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Clicked : " + country.getStdname() + " Ben ID " + country.getStdbenid(), Toast.LENGTH_LONG).show();

            }
        });

        localDB = new DataBaseHelper(ViewEditListActivity.this);
        Cursor cur=localDB.getUserRegisteredDetails();
        if(cur.getCount()>0)
        {
            while(cur.moveToNext())
            {
                _schoolName = cur.getString(cur.getColumnIndex("School_Name"))+" SCHOOL";
                String ditsblok=cur.getString(cur.getColumnIndex("District_Name"))+  ", "+cur.getString(cur.getColumnIndex("Block_Name"));
                _distBlock = ditsblok;
            }
        }

        if(getIntent().hasExtra("RECORDTYPE"))
        {
            if(getIntent().getStringExtra("RECORDTYPE").equalsIgnoreCase("ALL"))
            {
                DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
                studentList = helper.getStudentDetaislForClassSectionFYear(_diseCode,_varClass_Id,_varSession_Id,_varFYear_Id);
                if(studentList.size()>0)
                {
                    studentListAdaptor = new VIewEditListAdapter(ViewEditListActivity.this,"E");
                    list_student.setAdapter(studentListAdaptor);
                    studentListAdaptor.notifyDataSetChanged();
                }
            }
        }


        setButtonsAndOtherLabels(_lang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //  MoreFilter();
        getAdditionalSearchResult();
    }

    public void loadGenderList(Spinner spn_gender)
    {
        localDB = new DataBaseHelper(ViewEditListActivity.this);
        genderlists=localDB.getGenderList();

        if(genderlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            // StringList.add("-चुने-");
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<genderlists.size();i++)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(genderlists.get(i).getGenderName());
                }
                else
                {
                    StringList.add(genderlists.get(i).getGenderNameHn());
                }
                if(_varGender_Id.equalsIgnoreCase(genderlists.get(i).getGenderID()))
                {
                    setID=(i+1);
                }
            }

            GenderListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_gender.setAdapter(GenderListadapter);
            spn_gender.setSelection(setID);
        }
    }
    public void loadCategoryHNList(Spinner spn_category)
    {
        localDB = new DataBaseHelper(ViewEditListActivity.this);
        catlists=localDB.getCategoryHNList();
        if(catlists.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            //StringList.add("-चुने-");
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<catlists.size();i++)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(catlists.get(i).getCategoryName());
                }
                else
                {
                    StringList.add(catlists.get(i).getCategoryNameHn());
                }
                if(_varCategory_Id.equalsIgnoreCase(catlists.get(i).getCategoryID()))
                {
                    setID=(i+1);
                }
            }

            catListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_category.setAdapter(catListadapter);
            catListadapter.notifyDataSetChanged();
            spn_category.setSelection(setID);
        }
    }

    public void addminority(Spinner spn_minority) {
        if(_lang.equalsIgnoreCase("en")) {
            minoritytlist.add("-All-");
            minoritytlist.add("Yes");
            minoritytlist.add("No");
        }
        else
        {
            minoritytlist.add("-सभी-");
            minoritytlist.add("हाँ");
            minoritytlist.add("नहीं");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, minoritytlist);
        spn_minority.setAdapter(dataAdapter);

        if(str_minority.equalsIgnoreCase("Y"))
        {
            spn_minority.setSelection(1);
        }
        else  if(str_minority.equalsIgnoreCase("N"))
        {
            spn_minority.setSelection(2);
        }

    }

    public void adddivyang(Spinner spn_divyang ) {
        if(_lang.equalsIgnoreCase("en")) {
            divyanglist.add("-All-");
            divyanglist.add("Yes");
            divyanglist.add("No");
        }
        else
        {
            divyanglist.add("-सभी-");
            divyanglist.add("हाँ");
            divyanglist.add("नहीं");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, divyanglist);
        spn_divyang.setAdapter(dataAdapter);
        if(str_divyang.equalsIgnoreCase("Y"))
        {
            spn_divyang.setSelection(1);
        }
        else  if(str_divyang.equalsIgnoreCase("N"))
        {
            spn_divyang.setSelection(2);
        }

    }
    public void addAttendance(Spinner spn_atten) {
        if(_lang.equalsIgnoreCase("en")) {
            atnlist.add("-All-");
            atnlist.add("< 75%"); //1
            atnlist.add("> 75%"); //0
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, atnlist);
        spn_atten.setAdapter(dataAdapter);
        if(str_atn.equalsIgnoreCase("1"))
        {
            spn_atten.setSelection(1);
        }
        else  if(str_atn.equalsIgnoreCase("0"))
        {
            spn_atten.setSelection(2);
        }
    }
    public void adddbpl(Spinner spn_bpl) {
        if(_lang.equalsIgnoreCase("en")) {
            bpllist.add("-All-");
            bpllist.add("Yes");
            bpllist.add("No");
        }
        else {
            bpllist.add("-सभी-");
            bpllist.add("हाँ");
            bpllist.add("नहीं");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, bpllist);
        spn_bpl.setAdapter(dataAdapter);
        if(str_bpl.equalsIgnoreCase("Y"))
        {
            spn_bpl.setSelection(1);
        }
        else  if(str_bpl.equalsIgnoreCase("N"))
        {
            spn_bpl.setSelection(2);
        }
    }
    public void addNoneValueField(Spinner spn_HavingNonValue ) {
        /*
        AadharCardNo
AccountNo
StdRegNum
         */
        if(_lang.equalsIgnoreCase("en")) {
            havingnonevaluelist.add("-None-");
            havingnonevaluelist.add("Aadhar Number");
            havingnonevaluelist.add("Account Number");
            havingnonevaluelist.add("Admission Number");
            havingnonevaluelist.add("IFSC Code");
        }
        else
        {
            havingnonevaluelist.add("-कोई नहीं-");
            havingnonevaluelist.add("आधार संख्या");
            havingnonevaluelist.add("खाता संख्या");
            havingnonevaluelist.add("प्रवेश संख्या");
            havingnonevaluelist.add("IFSC कोड");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, havingnonevaluelist);
        spn_HavingNonValue.setAdapter(dataAdapter);
        if(str_none_value.equalsIgnoreCase("AadharCardNo"))
        {
            spn_HavingNonValue.setSelection(1);
        }
        else  if(str_none_value.equalsIgnoreCase("AccountNo"))
        {
            spn_HavingNonValue.setSelection(2);
        }
        else  if(str_none_value.equalsIgnoreCase("StdRegNum"))
        {
            spn_HavingNonValue.setSelection(3);
        }
        else  if(str_none_value.equalsIgnoreCase("IFSC"))
        {
            spn_HavingNonValue.setSelection(4);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // getAdditionalSearchResult();
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
    public void onClick_GeneratePDF(View v)
    {

        AlertDialog.Builder ab = new AlertDialog.Builder(ViewEditListActivity.this);
        if(_lang.equalsIgnoreCase("en"))
        {
            ab.setTitle(R.string.report_title);
            ab.setMessage(R.string.report_msg);
        }
        else
        {
            ab.setTitle(R.string.report_titlehn);
            ab.setMessage(R.string.report_msghn);
        }
        ab.setPositiveButton("[ English ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        try {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            progressDialog1.show();
                            // printRegistrationForm("en");
                            dialog.dismiss();
                            createPdfviewlocal("en");


                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
        ab.setNegativeButton("[ हिंदी ]",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        try {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            progressDialog1.show();
                            dialog.dismiss();
                            createWebViewContaintsForPDF();
                        } catch (Exception e) {
                            if(progressDialog1.isShowing())
                            {
                                progressDialog1.dismiss();
                            }
                            e.printStackTrace();
                        }

                    }
                });
        ab.show();

    }

    private void createPdfviewlocal(String lang) throws IOException, DocumentException {

        if(lang.equalsIgnoreCase("en")) {
            progressDialog1.setMessage("Please wait. Genreating PDF file");
        }
        else
        {
            progressDialog1.setMessage("कृपया प्रतीक्षा करें। पीडीएफ फाइल जेनेरेट हो रहा है");
        }
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//        Font f;
//        writeFont();

        String targetPdf = "/sdcard/"+_filePrefixName+timeStamp+".pdf";
        File filePath;
        filePath = new File(targetPdf);

        OutputStream output = new FileOutputStream(filePath);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        // PdfContentByte canvas = writer.getDirectContent();
        Drawable d = getResources().getDrawable(R.drawable.title);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        //image.setAbsolutePosition(50f,750f);
        image.setAlignment(Element.ALIGN_CENTER);
        image.scaleToFit(550,50);
        document.add(image);
        //Step 4 Add content
        document.setPageSize(PageSize.A4);
        document.setMargins(0, 0, 75, 180);
        document.setMarginMirroring(true);

        document.add(new Chunk("\n"));

        Font f1= new Font (Font.FontFamily.UNDEFINED, 18, Font.BOLD, BaseColor.BLUE);
        Paragraph info= new Paragraph(_schoolName,f1);
        // info.setAlignment(Element.ALIGN_MIDDLE);

        Paragraph addr= new Paragraph("",f1);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        table.spacingAfter();
        PdfPCell cell = new PdfPCell(info);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        cell = new PdfPCell(addr);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        document.add(table);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.UNDERLINE, BaseColor.BLUE);
        Paragraph title1= new Paragraph(_distBlock,font1);
        title1.setAlignment(Element.ALIGN_CENTER);
        document.add(title1);
        document.add(new Chunk("\n"));

        document.add(new LineSeparator(2f,100,BaseColor.GRAY,Element.ALIGN_CENTER,-1f));
        // end

        //document.add(new Paragraph(showpdfdata.getText().toString()));
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLUE);
        String cDate=Utiilties.getDateString("dd-MMM-yyyy");

        if(_varClass_Name.contains("All"))
        {

        }
        else
        {
            _varClass_Name=_varClass_Id;
        }

        Paragraph createdDate= new Paragraph("CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate,font2 );
        //Paragraph createdDate= new Paragraph("Created On: "+ cDate,font2 );
        createdDate.setAlignment(Element.ALIGN_CENTER);
        document.add(createdDate);

        Font fonthdr = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE);

        document.add(new Chunk("\n"));
        PdfPTable tableC = new PdfPTable(new float[] { 5,5, 3, 3,3,3,4 });
        tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        if(lang.equalsIgnoreCase("en")) {

            tableC.addCell(new Phrase("ADM.NO.", fonthdr));
            tableC.addCell(new Phrase("BENEFICIARY", fonthdr));
            tableC.addCell(new Phrase("FATHER NAME", fonthdr));

            tableC.addCell(new Phrase("A.DATE", fonthdr));
            tableC.addCell(new Phrase("CLASS", fonthdr));
            tableC.addCell(new Phrase("DoB", fonthdr));
            tableC.addCell(new Phrase("MOBILE", fonthdr));
        }
//        else if(lang.equalsIgnoreCase("hn")) {
//            f = FontFactory.getFont(getFilesDir() + "/" + HINDI_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//
//
//            tableC.addCell(new Phrase("प्रवेश संख्या", f));
//            tableC.addCell(new Phrase("लाभार्थी", f));
//            tableC.addCell(new Phrase("पिता का नाम", f));
//            tableC.addCell(new Phrase("प्रवेश तिथि", f));
//            tableC.addCell(new Phrase("कक्षा", f));
//            tableC.addCell(new Phrase("जन्म तिथि", f));
//            tableC.addCell(new Phrase("मोबाइल", f));
//        }

        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);

        }
        if(lang.equalsIgnoreCase("en")) {
            if (studentList != null) {
                if (studentList.size() > 0) {
                    for (int x = 0; x < studentList.size(); x++) {



                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdadmnum()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdname()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdfname()));


                        String y, m, da, addate;
                        String admdate = studentList.get(x).getStdadmdate() != "anyType{}" ? studentList.get(x).getStdadmdate() : "";
                        addate = admdate;
                        try {
                            if (admdate != null) {
                                if (!admdate.equalsIgnoreCase("")) {

                                    y = admdate.substring(0, 4);
                                    m = admdate.substring(4, 6);
                                    da = admdate.substring(6, 8);
                                    addate = da + "-" + m + "-" + y;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }
                        tableC.addCell(addate);

                        if (studentList.get(x).getStdsession().equalsIgnoreCase("")) {
                            tableC.addCell(studentList.get(x).getStdclassid());
                        } else {
                            tableC.addCell(studentList.get(x).getStdclassid() + " (" + studentList.get(x).getStdsession() + ")");
                        }

                        String y1, m1, d1, tdate;
                        String dobBen = studentList.get(x).getStdDOB() != "anyType{}" ? studentList.get(x).getStdDOB() : "";
                        tdate = dobBen;
                        try {
                            if (dobBen != null) {
                                if (!dobBen.equalsIgnoreCase("")) {
                                    y1 = dobBen.substring(0, 4);
                                    m1 = dobBen.substring(4, 6);
                                    d1 = dobBen.substring(6, 8);
                                    tdate = d1 + "-" + m1 + "-" + y1;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }

                        tableC.addCell(tdate);
                        tableC.addCell(studentList.get(x).getStdmobile());
                    }
                }
            }
        }

        document.add(tableC);

        document.add(new Chunk("\n"));


        //Step 5: Close the document
        document.close();

        if(progressDialog1.isShowing())
        {
            progressDialog1.dismiss();
        }
        if(Build.VERSION.SDK_INT>=24) {
            openGeneratedPDF2();
        }
        else
        {
            openGeneratedPDF();
            // openGeneratedPDF2();
        }
    }

    private void createPdfviewlocalInnerStorage(String lang) throws IOException, DocumentException {

        if(lang.equalsIgnoreCase("en")) {
            progressDialog1.setMessage("Please wait. Genreating PDF file");
        }
        else
        {
            progressDialog1.setMessage("कृपया प्रतीक्षा करें। पीडीएफ फाइल जेनेरेट हो रहा है");
        }
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        //   Font f;
        //  writeFont();

        String targetPdf = "/sdcard/"+_filePrefixName+timeStamp+".pdf";
        File filePath;
        filePath = new File(targetPdf);

        //-------------------------

        String PATH = getApplicationContext().getFilesDir().getPath();

        File file = new File(Utiilties.cleanStringForVulnerability(PATH));



        String fileName=_filePrefixName+timeStamp+".pdf";
        File outputFile = new File(file, Utiilties.cleanStringForVulnerability(fileName));

        //----------------------

        OutputStream output = new FileOutputStream(outputFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        // PdfContentByte canvas = writer.getDirectContent();
        Drawable d = getResources().getDrawable(R.drawable.title);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        //image.setAbsolutePosition(50f,750f);
        image.setAlignment(Element.ALIGN_CENTER);
        image.scaleToFit(550,50);
        document.add(image);
        //Step 4 Add content
        document.setPageSize(PageSize.A4);
        document.setMargins(0, 0, 75, 180);
        document.setMarginMirroring(true);

        document.add(new Chunk("\n"));

        Font f1= new Font (Font.FontFamily.UNDEFINED, 18, Font.BOLD, BaseColor.BLUE);
        Paragraph info= new Paragraph(_schoolName,f1);
        // info.setAlignment(Element.ALIGN_MIDDLE);

        Paragraph addr= new Paragraph("",f1);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        table.spacingAfter();
        PdfPCell cell = new PdfPCell(info);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        cell = new PdfPCell(addr);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(Rectangle.BOX);
        cell.setExtraParagraphSpace(1.5f);
        table.addCell(cell);
        document.add(table);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.UNDERLINE, BaseColor.BLUE);
        Paragraph title1= new Paragraph(_distBlock,font1);
        title1.setAlignment(Element.ALIGN_CENTER);
        document.add(title1);
        document.add(new Chunk("\n"));

        document.add(new LineSeparator(2f,100,BaseColor.GRAY,Element.ALIGN_CENTER,-1f));
        // end

        //document.add(new Paragraph(showpdfdata.getText().toString()));
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLUE);
        String cDate=Utiilties.getDateString("dd-MMM-yyyy");

        if(_varClass_Name.contains("All"))
        {

        }
        else
        {
            _varClass_Name=_varClass_Id;
        }

        Paragraph createdDate= new Paragraph("CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate,font2 );
        //Paragraph createdDate= new Paragraph("Created On: "+ cDate,font2 );
        createdDate.setAlignment(Element.ALIGN_CENTER);
        document.add(createdDate);

        Font fonthdr = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE);

        document.add(new Chunk("\n"));
        PdfPTable tableC = new PdfPTable(new float[] { 5,5, 3, 3,3,3,4 });
        tableC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        if(lang.equalsIgnoreCase("en")) {

            tableC.addCell(new Phrase("ADM.NO.", fonthdr));
            tableC.addCell(new Phrase("BENEFICIARY", fonthdr));
            tableC.addCell(new Phrase("FATHER NAME", fonthdr));

            tableC.addCell(new Phrase("A.DATE", fonthdr));
            tableC.addCell(new Phrase("CLASS", fonthdr));
            tableC.addCell(new Phrase("DoB", fonthdr));
            tableC.addCell(new Phrase("MOBILE", fonthdr));
        }


        tableC.setHeaderRows(1);
        PdfPCell[] cells = tableC.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);

        }
        if(lang.equalsIgnoreCase("en")) {
            if (studentList != null) {
                if (studentList.size() > 0) {
                    for (int x = 0; x < studentList.size(); x++) {



                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdadmnum()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdname()));
                        tableC.addCell(Utiilties.getValue(studentList.get(x).getStdfname()));


                        String y, m, da, addate;
                        String admdate = studentList.get(x).getStdadmdate() != "anyType{}" ? studentList.get(x).getStdadmdate() : "";
                        addate = admdate;
                        try {
                            if (admdate != null) {
                                if (!admdate.equalsIgnoreCase("")) {

                                    y = admdate.substring(0, 4);
                                    m = admdate.substring(4, 6);
                                    da = admdate.substring(6, 8);
                                    addate = da + "-" + m + "-" + y;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }
                        tableC.addCell(addate);

                        if (studentList.get(x).getStdsession().equalsIgnoreCase("")) {
                            tableC.addCell(studentList.get(x).getStdclassid());
                        } else {
                            tableC.addCell(studentList.get(x).getStdclassid() + " (" + studentList.get(x).getStdsession() + ")");
                        }

                        String y1, m1, d1, tdate;
                        String dobBen = studentList.get(x).getStdDOB() != "anyType{}" ? studentList.get(x).getStdDOB() : "";
                        tdate = dobBen;
                        try {
                            if (dobBen != null) {
                                if (!dobBen.equalsIgnoreCase("")) {
                                    y1 = dobBen.substring(0, 4);
                                    m1 = dobBen.substring(4, 6);
                                    d1 = dobBen.substring(6, 8);
                                    tdate = d1 + "-" + m1 + "-" + y1;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("EXCEPTION", "date2 exception");
                        }

                        tableC.addCell(tdate);
                        tableC.addCell(studentList.get(x).getStdmobile());
                    }
                }
            }
        }

        document.add(tableC);

        document.add(new Chunk("\n"));


        //Step 5: Close the document
        document.close();

        if(progressDialog1.isShowing())
        {
            progressDialog1.dismiss();
        }
        if(Build.VERSION.SDK_INT>=24) {
            openGeneratedPDF2();
        }
        else
        {
            openGeneratedPDF();
        }
    }

    private void openGeneratedPDF(){
        //File file = new File("/sdcard/bedpost"+timeStamp+".pdf");
        File file = new File("/sdcard/"+_filePrefixName+timeStamp+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void openGeneratedPDF2(){
        //File file = new File("/sdcard/bedpost"+timeStamp+".pdf");
        File file = new File("/sdcard/"+_filePrefixName+timeStamp+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            //Uri uri = Uri.fromFile(file);
            Uri uri = FileProvider.getUriForFile(ViewEditListActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            try
            {
                startActivity(intent);

            }
            catch(ActivityNotFoundException e)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }

            }
        }
    }


    private void openGeneratedPDFInnerStorage(){
        String PATH = getApplicationContext().getFilesDir().getPath();

        String fileName=_filePrefixName+timeStamp+".pdf";
        File directory = getApplicationContext().getFilesDir();
        File pdfFile = new File(directory, fileName);

        File internalFile =new  File(directory,fileName);
//		Uri internal = Uri.fromFile(internalFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);

// set flag to give temporary permission to external app to use your FileProvider
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "bih.nic.medhasoft.fileprovider", internalFile);

// I am opening a PDF file so I give it a valid MIME type
        intent.setDataAndType(uri, "application/pdf");

// validate that the device can open your File!
        PackageManager pm = getApplicationContext().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
            finish();
        }

    }
    private void openGeneratedPDF2InnerStorage(){
        //File file = new File("/sdcard/bedpost"+timeStamp+".pdf");
        // File file = new File("/sdcard/"+_filePrefixName+timeStamp+".pdf");
        String PATH = getApplicationContext().getFilesDir().getPath();
        File file = new File(Utiilties.cleanStringForVulnerability(PATH));
        String fileName=_filePrefixName+timeStamp+".pdf";
        File pdfFile = new File(file+"/"+fileName);
        if (pdfFile.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            //   Uri uri = Uri.fromFile(pdfFile);
            Uri uri = FileProvider.getUriForFile(ViewEditListActivity.this,
                    "bih.nic.medhasoft.fileprovider",pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                if(_lang.equalsIgnoreCase("en"))
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewer, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewEditListActivity.this, R.string.pdfviewerhn, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void setButtonsAndOtherLabels(String lang)
    {
        if(lang.equalsIgnoreCase("en"))
        {

            txt_FName.setText("FATHER NAME");
            txt_MName.setText("MOTHER NAME");
            txt_DoB.setText("DoB");
            txt_Name.setText("NAME");
            txt_ANum.setText("ADM.NO");
            txtHeaderName.setText(R.string.app_name);
            tvattendence.setText("View-Edit Student Details");
            ////txt_ACNum, txt_IFSC, txt_AdmDt

            txt_ACNum.setText("Bank A/C #");
            txt_IFSC.setText("IFSC");
            txt_AdmDt.setText("ADM. DATE");

            txt_AdNum.setText("AADHAAR #");
            txt_AdName.setText("AADHAAR NAME");

        }
        else
        {


            txt_FName.setText("पिता का नाम");
            txt_Name.setText("नाम");
            txt_MName.setText("माता का नाम");
            txt_DoB.setText("जन्म तिथि");
            txt_ANum.setText("प्र. संख्या");

            txtHeaderName.setText(R.string.app_namehn);
            tvattendence.setText("छात्र विवरण देखें-संपादित करें");
            ////txt_ACNum, txt_IFSC, txt_AdmDt

            txt_ACNum.setText("बैंक खाता संख्या");
            txt_IFSC.setText("IFSC");
            txt_AdmDt.setText("प्रवेश तिथि");

            txt_AdNum.setText("आधार #");
            txt_AdName.setText("आधार नाम");

        }
    }

//    public void writeFont()
//    {
//        try {
//            File file = new File(getFilesDir(), HINDI_FONT);
//            if (file.length() == 0) {
//                InputStream fs = getAssets().open(HINDI_FONT);
//                FileOutputStream os = new FileOutputStream(file);
//                int i;
//                while ((i = fs.read()) != -1) {
//                    os.write(i);
//                }
//                os.flush();
//                os.close();
//                fs.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void createWebViewContaintsForPDF()
    {

        // style='background-color:#0000CC; height:4px;
        String htmlTableH="<table width='100%'>";
        htmlTableH+="<tr>";
        htmlTableH+="<th><img alt='Medha Soft' src='file:///android_asset/title.png' height='50' width='100%'/></th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr>";
        htmlTableH+="<th>" + _schoolName  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr>";
        htmlTableH+="<th>" + _distBlock  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="<tr><td><hr style='background-color: #0000FF; height:3'><br><br></td></tr>";

        String cDate=Utiilties.getDateString("dd-MMM-yyyy");

        if(_varClass_Name.contains("All"))
        {      }
        else
        {
            _varClass_Name=_varClass_Id;
        }
        String info="CLASS: " +_varClass_Name+", SECTION: " + _varSession_Name+"  DATE: "+ cDate;
        htmlTableH+="<tr>";
        htmlTableH+="<th style='color: #0000FF;'>" + info  + "</th>";
        htmlTableH+="</tr>";
        htmlTableH+="</table><br><br>";

        String htmlTable="<table width='100%' style='border: thin solid #000000;'><tr style='color: #FFFFFF; background-color: #666666'>";


        htmlTable+="<th style='border: thin solid #000000; width:11%'>प्रवेश संख्या</th>";
        htmlTable+="<th style='border: thin solid #000000; width:22%'>लाभार्थी</th>";
        htmlTable+="<th style='border: thin solid #000000; width:22%'>पिता का नाम</th>";
        htmlTable+="<th style='border: thin solid #000000; width:12%'>प्रवेश तिथि</th>";
        htmlTable+="<th style='border: thin solid #000000; width:10%'>कक्षा</th>";
        htmlTable+="<th style='border: thin solid #000000; width:11%'>जन्म तिथि</th>";
        htmlTable+="<th style='border: thin solid #000000; width:12%'>मोबाइल</th>";
        htmlTable+="</tr>";


        int countLess75Per=0;
        if(studentList!=null)
        {
            if(studentList.size()>0)
            {

                for(int x=0;x<studentList.size();x++) {
                    htmlTable+="<tr>";



                    htmlTable += "<th style='border: thin solid #000000; width:11%'>"+Utiilties.getValue(studentList.get(x).getStdadmnum())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+Utiilties.getValue(studentList.get(x).getStdnamehn())+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:22%'>"+Utiilties.getValue(studentList.get(x).getStdfnamehn())+"</th>";

                    String y, m, da, addate;
                    String admdate = studentList.get(x).getStdadmdate() != "anyType{}" ? studentList.get(x).getStdadmdate() : "";
                    addate = admdate;
                    try {
                        if (admdate != null) {
                            if (!admdate.equalsIgnoreCase("")) {

                                y = admdate.substring(0, 4);
                                m = admdate.substring(4, 6);
                                da = admdate.substring(6, 8);
                                addate = da + "-" + m + "-" + y;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EXCEPTION", "date2 exception");
                    }
                    htmlTable += "<th style='border: thin solid #000000; width:12%'>"+addate+"</th>";

                    if (studentList.get(x).getStdsession().equalsIgnoreCase("")) {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+Utiilties.getValue(studentList.get(x).getStdclassid())+"</th>";
                    } else {
                        htmlTable += "<th style='border: thin solid #000000; width:10%'>"+Utiilties.getValue(studentList.get(x).getStdclassid()) + " (" + Utiilties.getValue(studentList.get(x).getStdsession()) + ")"+"</th>";
                    }

                    String y1, m1, d1, tdate;
                    String dobBen = studentList.get(x).getStdDOB() != "anyType{}" ? studentList.get(x).getStdDOB() : "";
                    tdate = dobBen;
                    try {
                        if (dobBen != null) {
                            if (!dobBen.equalsIgnoreCase("")) {
                                y1 = dobBen.substring(0, 4);
                                m1 = dobBen.substring(4, 6);
                                d1 = dobBen.substring(6, 8);
                                tdate = d1 + "-" + m1 + "-" + y1;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("EXCEPTION", "date2 exception");
                    }

                    htmlTable += "<th style='border: thin solid #000000; width:11%'>"+tdate+"</th>";
                    htmlTable += "<th style='border: thin solid #000000; width:12%'>"+Utiilties.getValue(studentList.get(x).getStdmobile())+"</th>";

                    htmlTable+="</tr>";
                }
            }
        }
        htmlTable+="</table><br><br>";



        WebSettings webSettings = showpdfdataWV.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");

        showpdfdataWV.setWebViewClient(new myWebClient());


        String myHtmlString=htmlTableH+htmlTable;

        Log.e("HTML",myHtmlString);

        //showpdfdataWV.loadData(myHtmlString, "text/html; charset=UTF-8", null);
        showpdfdataWV.loadDataWithBaseURL(null,myHtmlString, "text/html; charset=UTF-8", null,null);
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
        //NOTE: make changes for file provider in [com.webviewtopdf.PdfView.java-in function fileChooser(Activity activity, String path) {
        //Replace package name by app pakage name

        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        final String fileName=_filePrefixName+timeStamp+".pdf";

        final ProgressDialog progressDialog=new ProgressDialog(ViewEditListActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ViewEditListActivity.this, wv, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                PdfView.openPdfFile(ViewEditListActivity.this,getString(R.string.app_name),"Do you want to open the pdf file ?\n"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();
            }
        });
    }

    public void onClick_MoreFilter(View v)
    {
        String msg="";
        MoreFilter();

    }

    public void resetAdditionSearchCriteria()
    {
        spnCategory.setSelection(0);
        _varCategory_Id="0";

        spnIsMinority.setSelection(0);
        str_minority="0";

        spnGender.setSelection(0);
        _varGender_Id="0";

        spnIsBPL.setSelection(0);
        str_bpl="0";

        spnIsHandicaped.setSelection(0);
        str_divyang="0";

        txtStdNameForSearch.setSelection(0);
        //txtAdnStdCount.setText("");
        std_name="";
        getAdditionalSearchResult();
    }
    public void getAdditionalSearchResult()
    {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

        String SQLQuery="SELECT * FROM StudentListForAttendance WHERE ";
        //studentList = helper.getStudentDetaislAsPerAdditionFilter(_diseCode, _varClass_Id, _varSession_Id, "",_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value);
        Cursor list_student_cursor = helper.getAdvanceCustomSearchResult(SQLQuery,_diseCode, _varClass_Id, _varSession_Id,_varFYear_Id, _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang,std_name,_lang,str_none_value,"RJCT",str_atn);

        studentList=getCustomRecords(list_student_cursor);
        studentListAdaptor = new VIewEditListAdapter(ViewEditListActivity.this,"E");
        studentListAdaptor.upDateEntries(studentList);
        list_student.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();

        txtStdCount.setText(String.valueOf(studentList.size()));
        //txtAdnStdCount.setText(String.valueOf(studentList.size()));
        // txtStdCountVE.setText(String.valueOf(studentList.size()));
        //txtAdnStdCount.setText(String.valueOf(studentList.size()));
//        if(_lang.equalsIgnoreCase("en")) {
//            txtTotalStdLbl.setText("TOTAL RECORD FOUND: ");
//        }
//        else
//        {
//            txtTotalStdLbl.setText("कुल रिकॉर्ड मिला: ");
//        }
    }
    public ArrayList<studentList> getCustomRecords(Cursor cursor)
    {
        ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();

        while (cursor.moveToNext()) {

            studentList stdData = new studentList();
            stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
            stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
            stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
            stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
            stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));
            stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
            stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
            stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));

            String sec;
            if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
            {
                sec="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
            {
                sec="";
            }
            else
            {
                sec=cursor.getString(cursor.getColumnIndex("StdSession"));
            }
            stdData.setStdsession(sec);

            stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
            /////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

            stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
            //////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
            stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
            stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
            stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

            String dob;
            //String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
            //dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

            if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
            {
                dob="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
            {
                dob="";
            }
            else
            {
                dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
            }

            stdData.setStdDOB(dob);

            String mobnum;
            //String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
            if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
            {
                mobnum="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
            {
                mobnum="";
            }
            else
            {
                mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
            }
            stdData.setStdmobile(mobnum);


            String admdate;
            if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
            {
                admdate="";
            }
            else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
            {
                admdate="";
            }
            else
            {
                admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
            }
            stdData.setStdadmdate(admdate);

            stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
            stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

            String aadharno;
            //=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
            if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
            {
                aadharno="";
            }
            else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
            {
                aadharno="";
            }
            else
            {
                aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
            }

            stdData.setStdaadharcardno(aadharno);

            stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

            String email;
            //=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
            if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
            {
                email="";
            }
            else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
            {
                email="";
            }
            else
            {
                email=cursor.getString(cursor.getColumnIndex("EmailId"));
            }
            stdData.setStdemailid(email);

            stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

            String ifsc;
            //=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
            if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
            {
                ifsc="";
            }
            else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
            {
                ifsc="";
            }
            else
            {
                ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
            }
            stdData.setStdifsc(ifsc);

            String acnum;
            //=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
            if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
            {
                acnum="";
            }
            else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
            {
                acnum="";
            }
            else
            {
                acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
            }
            stdData.setStdacnum(acnum);

            String acname;
            //=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
            if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
            {
                acname="";
            }
            else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
            {
                acname="";
            }
            else
            {
                acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
            }
            stdData.setStdacholdername(acname);


            String cat;
            if(cursor.getString(cursor.getColumnIndex("StdCategoryName"))==null)
            {
                cat="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdCategoryName")).equalsIgnoreCase("anyType{}"))
            {
                cat="";
            }
            else
            {
                cat=cursor.getString(cursor.getColumnIndex("StdCategoryName"));
            }
            stdData.setStdcatname(cat);

            String catid;
            if(cursor.getString(cursor.getColumnIndex("StdCategoryID"))==null)
            {
                catid="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdCategoryID")).equalsIgnoreCase("anyType{}"))
            {
                catid="";
            }
            else
            {
                catid=cursor.getString(cursor.getColumnIndex("StdCategoryID"));
            }
            stdData.setStdcatid(catid);

            String fnyear;
            if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
            {
                fnyear="";
            }
            else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
            {
                fnyear="";
            }
            else
            {
                fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
            }
            stdData.setStdfyearval(fnyear);



            String genid;
            if(cursor.getString(cursor.getColumnIndex("StdGenderID"))==null)
            {
                genid="";
            }
            else if(cursor.getString(cursor.getColumnIndex("StdGenderID")).equalsIgnoreCase("anyType{}"))
            {
                genid="";
            }
            else
            {
                genid=cursor.getString(cursor.getColumnIndex("StdGenderID"));
            }
            stdData.setStdgenid(genid);

            String wvill;
            if(cursor.getString(cursor.getColumnIndex("WardVillage"))==null)
            {
                wvill="";
            }
            else if(cursor.getString(cursor.getColumnIndex("WardVillage")).equalsIgnoreCase("anyType{}"))
            {
                wvill="";
            }
            else
            {
                wvill=cursor.getString(cursor.getColumnIndex("WardVillage"));
            }
            stdData.setWardVillage(wvill);

            String mino;
            if(cursor.getString(cursor.getColumnIndex("IsMinority"))==null)
            {
                mino="";
            }
            else if(cursor.getString(cursor.getColumnIndex("IsMinority")).equalsIgnoreCase("anyType{}"))
            {
                mino="";
            }
            else
            {
                mino=cursor.getString(cursor.getColumnIndex("IsMinority"));
            }
            stdData.setStdisminority(mino);

            stdDataEntities.add(stdData);

        }

        cursor.close();
        return stdDataEntities;
    }

    public void onClick_GoToHomeScreen(View v)
    {
        startActivity(new Intent(ViewEditListActivity.this,PREHomeActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    public void MoreFilter()
    {
        String msg="";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.additionaleditfiltermore, null);

        dialogBuilder.setView(dialogView);

        spn_benstatus=(Spinner) dialogView.findViewById(R.id.spn_benstatus);
        spClass=(Spinner) dialogView.findViewById(R.id.spn_class);
        spSession=(Spinner) dialogView.findViewById(R.id.spn_session);
        spn_fyear=(Spinner) dialogView.findViewById(R.id.spn_fyear);
        spnCategory = (Spinner) dialogView.findViewById(R.id.spnCategory);
        spnIsMinority = (Spinner) dialogView.findViewById(R.id.spnIsMinority);
        spnGender = (Spinner) dialogView.findViewById(R.id.spnGender);
        spnIsBPL = (Spinner) dialogView.findViewById(R.id.spnIsBPL);
        spnIsHandicaped = (Spinner) dialogView.findViewById(R.id.spnIsHandicaped);
        spnAttendance = (Spinner) dialogView.findViewById(R.id.spnAttendance);
        spnHavingNonValue = (Spinner) dialogView.findViewById(R.id.spnHavingNonValue);
        txtStdNameForSearch =  dialogView.findViewById(R.id.txtStdNameForSearch);
        //txtAdnStdCount =  dialogView.findViewById(R.id.txtAdnStdCount);
        //txtTotalStdLbl =  dialogView.findViewById(R.id.txtTotalStdLbl);

        benstatuslist=new ArrayList<String>();
        minoritytlist=new ArrayList<String>();
        divyanglist=new ArrayList<String>();
        havingnonevaluelist=new ArrayList<String>();
        bpllist=new ArrayList<String>();
        atnlist=new ArrayList<String>();

        addBenStatus(spn_benstatus);
        loadClassList(spClass);
        loadSessionList(spSession);
        loadFYearList(spn_fyear);
        loadGenderList(spnGender);
        loadCategoryHNList(spnCategory);
        addminority(spnIsMinority);
        adddivyang(spnIsHandicaped);
        adddbpl(spnIsBPL);
        addNoneValueField(spnHavingNonValue);
        addAttendance(spnAttendance);
        if(std_name!=null)
        {
            if(std_name.length()>0)
            {
                txtStdNameForSearch.setText(std_name);
            }
        }
        else
        {
            std_name="";
        }



        spn_benstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_benstatus =benstatuslist.get(arg2 );

                    if(str_benstatus.equalsIgnoreCase("New Entry/Edited") || str_benstatus.equalsIgnoreCase("नई प्रविष्टि / संपादित"))
                    {
                        str_benstatus="0";
                    }
                    else if(str_benstatus.equalsIgnoreCase("Sent To PFMS For Verification") || str_benstatus.equalsIgnoreCase("सत्यापन के लिए पीएफएमएस को भेजा गया"))
                    {
                        str_benstatus="1";
                    }
                    else if(str_benstatus.equalsIgnoreCase("Accepted At PFMS") || str_benstatus.equalsIgnoreCase("पीएफएमएस में स्वीकृत"))
                    {
                        str_benstatus="2";
                    }
                    else if(str_benstatus.equalsIgnoreCase("Rejected") || str_benstatus.equalsIgnoreCase("अस्वीकृत"))
                    {
                        str_benstatus="3";
                    }
                } else {
                    str_benstatus="0";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spn_fyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    FYEAR fyear = FYearList.get(arg2 - 1);
                    _varFYear_Id = fyear.getFYearID();
                    _varFyear_Name = fyear.getFYearValue();
                    _varFyear_NameHn = fyear.getFYearValue();

                } else {
                    _varFYear_Id = "0";
                    _varFyear_Name = "ALl";
                    _varFyear_NameHn = "All";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    CLASSLIST currentPhysical = ClassList.get(arg2 - 1);
                    _varClass_Id = currentPhysical.getClassID();
                    _varClass_Name = currentPhysical.getClassName();
                } else {
                    _varClass_Id = "0";
                    _varClass_Name = "All";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    SESSIONLIST currentPhysical = SessionList.get(arg2 - 1);
                    _varSession_Id = currentPhysical.getSessionID();
                    _varSession_Name = currentPhysical.getSessionName();


                } else {
                    _varSession_Id = "0";
                    _varSession_Name = "None";
                    _varSession_NameHn = "कोई नहीं";


                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    categoryHINDI currentPhysical = catlists.get(arg2 - 1);
                    _varCategory_Id = currentPhysical.getCategoryID();
                    _varCategory_Name = currentPhysical.getCategoryName();
                    _varCategory_NameHn = currentPhysical.getCategoryNameHn();

                } else {
                    _varCategory_Id = "0";
                    _varCategory_Name = "";
                    _varCategory_NameHn="";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnIsMinority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_minority = minoritytlist.get(arg2 );

                    if(str_minority.equalsIgnoreCase("हाँ") || str_minority.equalsIgnoreCase("Yes"))
                    {
                        str_minority="Y";
                    }
                    else
                    {
                        str_minority="N";
                    }

                } else {

                    str_minority="0";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    Genderlist currentPhysical = genderlists.get(arg2 - 1);
                    _varGender_Id = currentPhysical.getGenderID();
                    _varGender_Name = currentPhysical.getGenderName();
                    _varGender_NameHn = currentPhysical.getGenderNameHn();

                } else {
                    _varGender_Id = "0";
                    _varGender_Name = "";
                    _varGender_NameHn="";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnIsBPL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_bpl = bpllist.get(arg2 );

                    if(str_bpl.equalsIgnoreCase("हाँ") || str_bpl.equalsIgnoreCase("Yes"))
                    {
                        str_bpl="Y";
                    }
                    else
                    {
                        str_bpl="N";
                    }

                } else {
                    str_bpl="0";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnIsHandicaped.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_divyang = divyanglist.get(arg2 );

                    if(str_divyang.equalsIgnoreCase("हाँ") || str_divyang.equalsIgnoreCase("Yes"))
                    {
                        str_divyang="Y";
                    }
                    else
                    {
                        str_divyang="N";
                    }

                } else {
                    str_divyang="0";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spnHavingNonValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_none_value = havingnonevaluelist.get(arg2 );

                    if(str_none_value.equalsIgnoreCase("आधार संख्या") || str_none_value.equalsIgnoreCase("Aadhar Number"))
                    {
                        str_none_value="AadharCardNo";
                    }
                    else if(str_none_value.equalsIgnoreCase("खाता संख्या") || str_none_value.equalsIgnoreCase("Account Number"))
                    {
                        str_none_value="AccountNo";
                    }
                    else if(str_none_value.equalsIgnoreCase("प्रवेश संख्या") || str_none_value.equalsIgnoreCase("Admission Number"))
                    {
                        str_none_value="StdRegNum";
                    }
                    else if(str_none_value.equalsIgnoreCase("IFSC कोड") || str_none_value.equalsIgnoreCase("IFSC Code"))
                    {
                        str_none_value="IFSC";
                    }
                } else {
                    str_none_value="0";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spnAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    str_atn = atnlist.get(arg2 );

                    if(str_atn.equalsIgnoreCase("< 75%"))
                    {
                        str_atn="1";
                    }
                    else
                    {
                        str_atn="0";
                    }

                } else {
                    str_atn="";
                }
                getAdditionalSearchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        txtStdNameForSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>=1)
                {
                    Log.e("onTextChanged",s.toString());
                    std_name=s.toString();
                    getAdditionalSearchResult();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        // dialogBuilder.setIcon(R.drawable.filters);
        if(_lang.equalsIgnoreCase("en")) {
            txtStdNameForSearch.setHint("Enter Student Name in English");
            // dialogBuilder.setTitle("Additional Filter");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    std_name = txtStdNameForSearch.getText().toString();
                    dialog.dismiss();
                    getAdditionalSearchResult();

                }
            });
//            dialogBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    //resetAdditionSearchCriteria();
//                }
//            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    //pass
                }
            });
        }
        else
        {
            txtStdNameForSearch.setHint("हिंदी में छात्र का नाम दर्ज करें");
            //dialogBuilder.setTitle("अतिरिक्त फ़िल्टर");
            dialogBuilder.setPositiveButton(R.string.okhn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // if(isSelectedAnyValue( _varCategory_Id, str_minority, _varGender_Id, str_bpl, str_divyang)) {
                    std_name = txtStdNameForSearch.getText().toString();
                    dialog.dismiss();
                    getAdditionalSearchResult();

                }
            });

//            dialogBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    dialog.cancel();
//                    //resetAdditionSearchCriteria();
//                }
//            });

            dialogBuilder.setNegativeButton(R.string.cancehn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    //pass
                }
            });
        }
        AlertDialog b = dialogBuilder.create();
        b.show();
        // }

    }
    public void addBenStatus(Spinner spn_benS) {
        /*
        0-new enery/edited
1-pfms sent to for veryfication
2-accpted at pfms
3- rejected
         */
        if(_lang.equalsIgnoreCase("en")) {
            benstatuslist.add("New Entry/Edited");
            //  benstatuslist.add("Sent To PFMS For Verification");
            benstatuslist.add("Accepted");
            benstatuslist.add("Rejected");
        }
        else
        {
            benstatuslist.add("नई प्रविष्टि / संपादित");
            // benstatuslist.add("सत्यापन के लिए पीएफएमएस को भेजा गया");
            benstatuslist.add("स्वीकृत");
            benstatuslist.add("अस्वीकृत");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, benstatuslist);
        spn_benS.setAdapter(dataAdapter);

        if(str_benstatus.equalsIgnoreCase("0"))
        {
            spn_benS.setSelection(0);
        }
        else  if(str_benstatus.equalsIgnoreCase("ACCP"))
        {
            spn_benS.setSelection(1);
        }
        else  if(str_benstatus.equalsIgnoreCase("RJCT"))
        {
            spn_benS.setSelection(2);
        }

        spn_benS.setSelection(2);
        spn_benS.setEnabled(false);
//        else  if(str_benstatus.equalsIgnoreCase("1"))
//        {
//            spn_benS.setSelection(1);
//        }
//        else  if(str_benstatus.equalsIgnoreCase("2"))
//        {
//            spn_benS.setSelection(2);
//        }
//        else  if(str_benstatus.equalsIgnoreCase("3"))
//        {
//            spn_benS.setSelection(3);
//        }

    }
    public void loadClassList(Spinner spCls)
    {
        localDB = new DataBaseHelper(ViewEditListActivity.this);
        ClassList=localDB.getClassList();
        if(ClassList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }
            int setID=0;
            for(int i=0;i<ClassList.size();i++)
            {
                if(_lang.equalsIgnoreCase("en")) {
                    StringList.add(ClassList.get(i).getClassName());
                }
                else
                {
                    StringList.add(ClassList.get(i).getClassNamehn());
                }
                if(_varClass_Id.equalsIgnoreCase(ClassList.get(i).getClassID()))
                {
                    setID=(i+1);
                }
            }



            ClassListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spCls.setAdapter(ClassListadapter);
            ClassListadapter.notifyDataSetChanged();
            spCls.setSelection(setID);
        }
    }

    public void loadSessionList(Spinner spSess)
    {
        localDB = new DataBaseHelper(ViewEditListActivity.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("None");
            }
            else
            {
                StringList.add("कोई नहीं");
            }
            int setID=0;
            for(int i=0;i<SessionList.size();i++)
            {
                StringList.add(SessionList.get(i).getSessionName());
                if(_varSession_Id.equalsIgnoreCase(SessionList.get(i).getSessionID()))
                {
                    setID=(i+1);
                }
            }
            SessionListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spSess.setAdapter(SessionListadapter);
            SessionListadapter.notifyDataSetChanged();
            spSess.setSelection(setID);
        }
    }
    public void loadFYearList(Spinner spfyr)
    {
        localDB = new DataBaseHelper(ViewEditListActivity.this);
        FYearList=localDB.getFYearList();
        if(FYearList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();
            if(_lang.equalsIgnoreCase("en")) {
                StringList.add("-All-");
            }
            else
            {
                StringList.add("-सभी-");
            }

            FYearListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spfyr.setAdapter(FYearListadapter);

            int setID=0;
            for(int i=0;i<FYearList.size();i++)
            {
                StringList.add(FYearList.get(i).getFYearValue());
                if(_varFYear_Id.equalsIgnoreCase(FYearList.get(i).getFYearID()))
                {
                    setID=(i+1);
                }
                spfyr.setSelection(setID);
            }
        }
    }
}

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.medhasoft.Adapter.SerBList;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.CLASSLIST;
import bih.nic.medhasoft.entity.SESSIONLIST;
import bih.nic.medhasoft.entity.UserDetails;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;

public class NewEntryStudentList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;

    ProgressDialog pd1;
    DataBaseHelper dataBaseHelper;
    ArrayList<studentList> benfiLists = new ArrayList<>();
    UserDetails userDetails;
    String Userid = "";
    String blockcode="";
    String distcode="";
    String panchyatcode="";
    String blockName="";
    String DistName="";
    String PanchayatName="";
    String _varClass_Name="All",_varClass_Id="0";
    String _varClass_NameHn="सभी";

    ArrayList<CLASSLIST> ClassList = new ArrayList<>();
    ArrayAdapter<String> ClassListadapter;
    DataBaseHelper localDB ;

    ArrayList<SESSIONLIST> SessionList = new ArrayList<>();
    ArrayAdapter<String> SessionListadapter;
    String _varSession_Name="None",_varSession_Id="0";
    String _varSession_NameHn="कोई नहीं";
    // String officer_code;
    String _diseCode="0";
    String _mobilenum="0";

    EditText filterText;
    ImageView img_filter,img_search;
    private PopupWindow mPopupWindow;
    int filterid=0;
    TextView nodata;
    TextView txt_dist,txt_block,txt_pnchayat;
    Spinner spn_class,spn_session;
    public static ProgressDialog pd2;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_student_list);


        dataBaseHelper = new DataBaseHelper(NewEntryStudentList.this);




      
        pd1 = new ProgressDialog(NewEntryStudentList.this);
        pd1.setTitle("Data is downloading Wait");
        pd1.setCancelable(false);
//        filterText=(EditText)findViewById(R.id.flterText) ;
//        img_filter=(ImageView)findViewById(R.id.img_filter);
//        img_search=(ImageView)findViewById(R.id.img_search);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        spn_class = (Spinner) findViewById(R.id.spn_class);
        spn_session = (Spinner) findViewById(R.id.spn_session);
        nodata=(TextView)findViewById(R.id.nodata);
//        txt_dist=(TextView)findViewById(R.id.txt_dist);
//        txt_block=(TextView)findViewById(R.id.txt_block);
//        txt_pnchayat=(TextView)findViewById(R.id.txt_pnchayat);

        searchStdList();

        Userid = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("UserId", "");
        try {
            blockcode = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("Block", "");
            distcode = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("District", "");
            panchyatcode = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("PanchayatCode", "");
            DistName = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("DistrictName", "");
            blockName = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("BlockName", "");
            PanchayatName = PreferenceManager.getDefaultSharedPreferences(NewEntryStudentList.this).getString("PanchayatName", "");

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {
                if (arg2 > 0)
                {

                    CLASSLIST currentPhysical = ClassList.get(arg2 - 1);
                    _varClass_Id = currentPhysical.getClassID();
                    _varClass_Name = currentPhysical.getClassName();
                    _varClass_NameHn = currentPhysical.getClassNamehn();

                    searchStdList();
                    // benfiLists = dataBaseHelper.MatchedWithBankStudentList(_varClass_Id,_varSession_Id);
//                    new LoadAttendance(_diseCode,_varClass_Id).execute();
                }
                else
                {
                    _varClass_Id = "0";
                    _varClass_Name = "All";
                    _varClass_NameHn = "सभी";
                    searchStdList();
                    // benfiLists = dataBaseHelper.MatchedWithBankStudentList(_varClass_Id,_varSession_Id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        spn_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    SESSIONLIST currentPhysical = SessionList.get(arg2 - 1);
                    _varSession_Id = currentPhysical.getSessionID();
                    _varSession_Name = currentPhysical.getSessionName();
                    _varSession_NameHn = currentPhysical.getSessionNamehn();
                     searchStdList();//benfiLists = dataBaseHelper.MatchedWithBankStudentList(_varClass_Id,_varSession_Id);

                } else {
                    _varSession_Id = "0";
                    _varSession_Name = "None";
                    _varSession_NameHn = "कोई नहीं";
                     searchStdList();
                   // benfiLists = dataBaseHelper.MatchedWithBankStudentList(_varClass_Id,_varSession_Id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        loadClassList();
        loadSessionList();



    }

    public void onClick_Finish(View v)
    {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }



    public void loadClassList()
    {
        localDB = new DataBaseHelper(NewEntryStudentList.this);
        ClassList=localDB.getClassList();
        if(ClassList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();

            StringList.add("-All-");

            int setID=0;
            for(int i=0;i<ClassList.size();i++)
            {

                StringList.add(ClassList.get(i).getClassName());

                if(_varClass_Id.equalsIgnoreCase(ClassList.get(i).getClassID()))
                {
                    setID=(i+1);
                }
            }
            ClassListadapter=new ArrayAdapter(this,R.layout.dropdownlist,StringList);
            spn_class.setAdapter(ClassListadapter);
        }
        else {

            if (Utiilties.isOnline(NewEntryStudentList.this)) {
                Toast.makeText(this, "Please wait downloading class", Toast.LENGTH_SHORT).show();
                new NewEntryStudentList.LoadClassList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(NewEntryStudentList.this);
                ab.setMessage("Please Turn on Internet connection.");
                ab.setPositiveButton("Turn On",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                GlobalVariables.isOffline = false;
                                Intent I = new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(I);
                            }
                        });

                ab.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                GlobalVariables.isOffline = true;
                                dialog.dismiss();

                            }
                        });
                ab.show();

            }
        }
    }

    public void loadSessionList()
    {
        localDB = new DataBaseHelper(NewEntryStudentList.this);
        SessionList=localDB.getSessionList();
        if(SessionList.size()>0 ) {
            ArrayList<String> StringList=new ArrayList<String>();

            StringList.add("None");

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
            spn_session.setAdapter(SessionListadapter);
        }

        else {


            if (Utiilties.isOnline(NewEntryStudentList.this)) {
                Toast.makeText(this, "Please wait downloading sections", Toast.LENGTH_SHORT).show();
                new NewEntryStudentList.LoadSessionList().execute();

            } else {

                AlertDialog.Builder ab = new AlertDialog.Builder(NewEntryStudentList.this);
                ab.setMessage("Please Turn on Internet connection.");
                ab.setPositiveButton("Turn On",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                GlobalVariables.isOffline = false;
                                Intent I = new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(I);
                            }
                        });
                ab.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                GlobalVariables.isOffline = true;
                                dialog.dismiss();

                            }
                        });
                ab.show();

            }
        }
    }



    private class LoadClassList extends AsyncTask<String, Void, ArrayList<CLASSLIST>> {

        @Override
        protected void onPreExecute() {
            pd2 = new ProgressDialog(NewEntryStudentList.this);
            pd2.setTitle("Downloading Class...");
            pd2.setCancelable(false);
            pd2.show();
        }

        @Override
        protected ArrayList<CLASSLIST> doInBackground(String... param) {

            return WebServiceHelper.GetClassList();
        }

        @Override
        protected void onPostExecute(ArrayList<CLASSLIST> result) {
            if (pd2.isShowing()) {

                pd2.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt",""+result);
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setClassList(result);

                    if(i>0){
                        Toast.makeText(NewEntryStudentList.this, "Class Data downloaded", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadClassList();
                        }
                    }
                    else{
                        Toast.makeText(NewEntryStudentList.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class LoadSessionList extends AsyncTask<String, Void, ArrayList<SESSIONLIST>> {

        @Override
        protected void onPreExecute() {
            pd2 = new ProgressDialog(NewEntryStudentList.this);
            pd2.setTitle("Downloading Section...");
            pd2.setCancelable(false);
            pd2.show();
        }

        @Override
        protected ArrayList<SESSIONLIST> doInBackground(String... param) {

            return WebServiceHelper.GetSessionList();
        }

        @Override
        protected void onPostExecute(ArrayList<SESSIONLIST> result) {
            if (pd2.isShowing()) {

                pd2.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    Log.d("tttttttttt",""+result);
                    DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

                    long i= helper.setSessionList(result);

                    if(i>0){
                        Toast.makeText(NewEntryStudentList.this, "Section Data downloaded...", Toast.LENGTH_SHORT).show();
                        ClassList = helper.getClassList();
                        if(ClassList.size()> 0){
                            loadSessionList();
                        }
                    }
                    else{
                        Toast.makeText(NewEntryStudentList.this, "not inserted", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No record Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }




    public void searchStdList()
    {
        dataBaseHelper = new DataBaseHelper(getApplicationContext());



        benfiLists = dataBaseHelper.getNewEnteredStudent(_varClass_Id,_varSession_Id);


        if (benfiLists.size() == 0) {

        } else {
            recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView.setLayoutManager(recylerViewLayoutManager);
            recyclerViewAdapter = new SerBList(NewEntryStudentList.this, benfiLists);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

        }


    }
}

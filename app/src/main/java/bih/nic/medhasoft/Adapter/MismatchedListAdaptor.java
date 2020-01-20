package bih.nic.medhasoft.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;


public class MismatchedListAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    Context context;
    String Totclasses;
    int countChecked=0;
    LinearLayout lin_detail;

    ArrayList<studentList> data = new ArrayList<>();
    ArrayList<CheckUnCheck> benchek=new ArrayList<>();
    private HashMap<String, String> textValues = new HashMap<String, String>();
    //    final CheckUnCheck cn=new CheckUnCheck(); //--

    private RadioGroup lastCheckedRadioGroup = null;
    public MismatchedListAdaptor(Context activity, ArrayList<studentList> contact, String totnnoClasses) {
        this.context = activity;
        this.data = contact;
        this.Totclasses=totnnoClasses;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder = null;

        //if (view == null) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.mismatched_list_adaptor, null);
        holder = new ViewHolder();

        final DataBaseHelper localDB=new DataBaseHelper(context);
        // holder.txtBenID = (TextView) view.findViewById(R.id.txtBenID);
//        holder.txtfn = (TextView) view.findViewById(R.id.textView_fn);
//        holder.lin_detail=(LinearLayout)view.findViewById(R.id.lin_detail);
//        holder.txtname = (TextView) view.findViewById(R.id.textView_rollno);
//        holder.textView_Class = (TextView) view.findViewById(R.id.textView_Class);
//       // holder.txtadnum = (TextView) view.findViewById(R.id.textView);
//        holder.textViewSlNo = (TextView) view.findViewById(R.id.textViewSlNo);
//
//        holder.txtmn = (TextView) view.findViewById(R.id.textView_mn);
//        holder.tv_mob = (TextView) view.findViewById(R.id.tv_mob);
//        holder.tv_others = (TextView) view.findViewById(R.id.tv_others);
//        holder.tv_benNmAsBank = (TextView) view.findViewById(R.id.tv_benNmAsBank);
//        holder.tv_benacc = (TextView) view.findViewById(R.id.tv_benacc);
        // holder.txtdob = (TextView) view.findViewById(R.id.textView_dob);

        //  holder. checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        holder.tv_benName = (TextView) view.findViewById(R.id.tv_benName);
        holder.textView_fn = (TextView) view.findViewById(R.id.textView_fn);
        holder.textView_mn = (TextView) view.findViewById(R.id.textView_mn);
        // holder.tv_mob = (TextView) view.findViewById(R.id.tv_mob);
        holder.tv_others = (TextView) view.findViewById(R.id.tv_others);
        holder.tv_benNmAsBank = (TextView) view.findViewById(R.id.tv_benNmAsBank);
        holder.tv_benacc = (TextView) view.findViewById(R.id.tv_benacc);
        holder.textViewSlNo = (TextView) view.findViewById(R.id.textViewSlNo);
        holder.radioGroup = (RadioGroup) view.findViewById(R.id.radio);
        holder.radioButton_Y = (RadioButton)view.findViewById(R.id.radio_Y);
        holder.radioButton_N = (RadioButton)view.findViewById(R.id.radio_N);
        view.setTag(holder);
        countChecked=0;

        int pos = i+1;

        holder.radioGroup.setFocusable(false);

        final CheckUnCheck cn=new CheckUnCheck(); //--
        final ViewHolder finalHolder = holder;


        GlobalVariables.benIDArrayList=benchek;


        final studentList country = data.get(i);


      /*  holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                if (radioButton_Y != null) {
                    radioButton_Y.setChecked(false);
                }
                //store the clicked radiobutton
                radioButton_Y = checked_rb;
            }
        });*/

        final ViewHolder finalHolder1 = holder;
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Radio button

                if (finalHolder1.radioButton_Y.isChecked()){
                    Toast.makeText(context, "Y", Toast.LENGTH_LONG).show();
                    //  updateMismatchStatus(country.getStdbenid(),country.getaID(),country.getCUBy(),"Y");

                    localDB.updateMismatchStatus(country.getStdbenid(),country.getaID(),country.getCUBy(),"Y");
                }
                else{
                    Toast.makeText(context, "N", Toast.LENGTH_LONG).show();
                    // updateMismatchStatus(country.getStdbenid(),country.getaID(),country.getCUBy(),"N");
                    localDB.updateMismatchStatus(country.getStdbenid(),country.getaID(),country.getCUBy(),"N");

                }
            }
        });

        String  _lang= PreferenceManager.getDefaultSharedPreferences(context).getString("LANG", "");
        if(_lang.equalsIgnoreCase("en"))
        {

            String fn=country.getStdfname();
            if(fn!=null)
            {
                fn=!country.getStdfname().contains("anyType{}") ? country.getStdfname() : "";
            }
            holder.textView_fn.setText(fn);

//            String cls=country.getStdclassid();
//            if(cls!=null)
//            {
//                cls=!country.getStdclassid().contains("anyType{}") ? country.getStdclassid() : "";
//            }
            // holder.textView_Class.setText(cls);

            String mn=country.getStdmname();
            if(mn!=null)
            {
                mn=!country.getStdmname().contains("anyType{}") ? country.getStdmname() : "";
            }
            holder.textView_mn.setText(mn);


//            String dob=country.getStdDOB();
//            if(dob!=null)
//            {
//                dob=!country.getStdDOB().contains("anyType{}") ? country.getStdDOB() : "";
//                Log.e("DoB",dob);
//                if(dob.length()>7) {
//                    dob=dob;
//                }
//            }
            //  holder.txtdob.setText(dob);

            String sn=country.getStdname();
            if(sn!=null)
            {
                sn=!country.getStdname().contains("anyType{}") ? country.getStdname() : "";
            }
            holder.tv_benName.setText(sn);

            String bkNm=country.get_eupi_BenNameasPerBank();
            if(bkNm!=null)
            {
                bkNm=!country.get_eupi_BenNameasPerBank().contains("anyType{}") ? country.get_eupi_BenNameasPerBank() : "";
            }
            holder.tv_benNmAsBank.setText(bkNm);

            String acc=country.get_eupi_AccountNo();
            if(acc!=null)
            {
                acc=!country.get_eupi_AccountNo().contains("anyType{}") ? country.get_eupi_AccountNo() : "";
            }
            holder.tv_benacc.setText(acc);


            String other=country.getStdacholdername();
            if(other!=null)
            {
                other=!country.getStdacholdername().contains("anyType{}") ? country.getStdacholdername() : "";
            }
            holder.tv_others.setText(other);


//            String an=country.getStdadmnum();
//            if(an!=null)
//            {
//                an=!country.getStdadmnum().contains("anyType{}") ? country.getStdadmnum() : "";
//            }

            String idno=country.getId();
            if(idno!=null)
            {
                idno=!country.getId().contains("anyType{}") ? country.getId() : "";
            }
            // holder.textViewSlNo.setText(idno);

            // holder.txtadnum.setText(an);
            //   holder.txtBenID.setText(country.getStdbenid());
        }
        else
        {
            String fn=country.getStdfnamehn();
            if(fn!=null)
            {
                fn=!country.getStdfnamehn().contains("anyType{}") ? country.getStdfnamehn() : "";
            }
            holder.textView_fn.setText(fn);

//            String cls=country.getStdclassid();
//            if(cls!=null)
//            {
//                cls=!country.getStdclassid().contains("anyType{}") ? country.getStdclassid() : "";
//            }
//            holder.textView_Class.setText(cls);

            String mn=country.getStdmnamehn();
            if(mn!=null)
            {
                mn=!country.getStdmnamehn().contains("anyType{}") ? country.getStdmnamehn() : "";
            }
            holder.textView_mn.setText(mn);


//            String dob=country.getStdDOB();
//            if(dob!=null)
//            {
//                dob=!country.getStdDOB().contains("anyType{}") ? country.getStdDOB() : "";
//                Log.e("DoB",dob);
//                if(dob.length()>7)
//                {
//                    dob=dob;
//                }
//            }
//            holder.txtdob.setText(dob);


            String sn=country.getStdnamehn();
            if(sn!=null)
            {
                sn=!country.getStdnamehn().contains("anyType{}") ? country.getStdnamehn() : "";
            }
            holder.tv_benName.setText(sn);

//            String an=country.getStdadmnum();
//            if(an!=null)
//            {
//                an=!country.getStdadmnum().contains("anyType{}") ? country.getStdadmnum() : "";
//            }
            // holder.txtadnum.setText(an);
            //     holder.txtBenID.setText(country.getStdbenid());
        }

//        }
//        else {
//
//            holder = (ViewHolder) view.getTag();
//        }


        holder.textViewSlNo.setText(Integer.toString(pos));
        pos++;


        notifyDataSetChanged();
        return view;

    }

    public  void updateAttendance(String benid,String status)
    {
        DataBaseHelper localDB=new DataBaseHelper(context);
        ContentValues values = new ContentValues();
        // values.put("CreatedBy", diseCode);
        String cdate= Utiilties.getDateString("yyyy-MM-dd");
        cdate=cdate.replace("-","");
        values.put("CreatedDate", cdate);
        values.put("StdAttendanceLess", status);
        values.put("AttSeventyFivePercent",status);
        values.put("IsAttendanceUpdated","Y");

        String[] whereArgsss = new String[]{benid};
        SQLiteDatabase db = localDB.getWritableDatabase();
        long  c = db.update("StudentListForAttendance", values, "BenID=?", whereArgsss);
        Log.e("ISUPDATED",""+c);
    }
    public boolean checkMaxLimit()
    {
        int countermax = 0;
        for(studentList item : data)
        {
            if(item.isSelected())
            {
                countermax++;
            }
        }
        return countermax >= 5;
    }

    private class ViewHolder
    {
        // TextView txtname,txtfn,txtmn,txtdob,textView_Class,textViewSlNo,tv_mob,tv_others,tv_benNmAsBank,tv_benacc;

        TextView tv_benName,textView_fn,textView_mn,tv_mob,tv_others,tv_benNmAsBank,tv_benacc,textViewSlNo;

        CheckBox checkBox;
        RadioGroup radioGroup;
        RadioButton radioButton_Y,radioButton_N;
        LinearLayout lin_detail;
        // EditText editnoofclasses,editnoofclassesobtain;
    }



    public  void updateMismatchStatus(String benid,String aid,String dise,String status)
    {
        DataBaseHelper localDB=new DataBaseHelper(context);
        ContentValues values = new ContentValues();
        long c = -1;
        // values.put("CreatedBy", diseCode);

        values.put("a_Id", aid);
        values.put("matchstatus", status);
        values.put("disecode",dise);
        values.put("BenId",benid);

        String[] whereArgsss = new String[]{benid};
        SQLiteDatabase db = localDB.getWritableDatabase();


        long update = db.update("MismatchBenStatus", values, "BenId = ?", whereArgsss);

        if (!(update > 0))
        {
            c = db.insert("MismatchBenStatus", null, values);
        }


        Log.e("ISUPDATED",""+update);
    }
}

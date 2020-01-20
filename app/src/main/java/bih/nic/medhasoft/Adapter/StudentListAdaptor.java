package bih.nic.medhasoft.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import bih.nic.medhasoft.EditBendDetailsActivity;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class StudentListAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    Context context;
    String Totclasses;
    int countChecked=0;
    LinearLayout lin_detail;

    ArrayList<studentList> data = new ArrayList<>();
    ArrayList<CheckUnCheck> benchek=new ArrayList<>();
    private HashMap<String, String> textValues = new HashMap<String, String>();
    //    final CheckUnCheck cn=new CheckUnCheck(); //--
    public StudentListAdaptor(Context activity, ArrayList<studentList> contact, String totnnoClasses) {
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
        view = mInflater.inflate(R.layout.student_list_adaptor, null);
        holder = new ViewHolder();
        holder.txtBenID = (TextView) view.findViewById(R.id.txtBenID);
        holder.txtfn = (TextView) view.findViewById(R.id.textView_fn);
        holder.lin_detail=(LinearLayout)view.findViewById(R.id.lin_detail);
        holder.txtname = (TextView) view.findViewById(R.id.textView_rollno);
        holder.textView_Class = (TextView) view.findViewById(R.id.textView_Class);
        holder.txtadnum = (TextView) view.findViewById(R.id.textView);
        holder.textViewSlNo = (TextView) view.findViewById(R.id.textViewSlNo);

        holder.txtmn = (TextView) view.findViewById(R.id.textView_mn);
        holder.txtdob = (TextView) view.findViewById(R.id.textView_dob);

        holder.radioattn = (RadioGroup) view.findViewById(R.id.radioattn);
        holder.radio_Y_attn = (RadioButton)view.findViewById(R.id.radio_Y_attn);
        holder.radio_N_attn = (RadioButton)view.findViewById(R.id.radio_N_attn);

       // holder. checkBox = (CheckBox) view.findViewById(R.id.checkBox);
       // holder. checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        view.setTag(holder);
        countChecked=0;
//            final CheckUnCheck cn=new CheckUnCheck(); //------------------
//        holder.checkBox.setFocusable(false);
        holder.radioattn.setFocusable(false);
        holder.radio_Y_attn.setFocusable(false);
        holder.radio_N_attn.setFocusable(false);
        int pos = i+1;

        if(data!=null)

        {
            //  for ()
            Log.d("studentdatadetail",String.valueOf(data.get(i).getStdattseventyfiveper()));
            // Log.e("studentdatadetail",String.valueOf(data.get(i).getStdattseventyfiveper()));
            if(!(data.get(i).getStdattseventyfiveper().equalsIgnoreCase("anyType{}")))
            {
//
                if(data.get(i).getStdattseventyfiveper().equalsIgnoreCase("N")) // y changed to N
                {
                    //  holder.checkBox.setChecked(true);
                      holder.radio_N_attn.setChecked(true);

                   // holder.checkBox.setChecked(false);
                //    holder.checkBox.setTag(data.get(i));
                    holder.radioattn.setTag(data.get(i));
                    holder.txtname.setTextColor(context.getResources().getColor(R.color.holo_red_dark));

                    //country.setSelected(true);
                }
                else
                {
                  //  holder.checkBox.setChecked(true);
                    holder.radio_Y_attn.setChecked(true);
                 //   holder.checkBox.setTag(data.get(i));
                    holder.radioattn.setTag(data.get(i));
                    //country.setSelected(false);
                    holder.txtname.setTextColor(context.getResources().getColor(R.color.colortextgreen));
                }
            }
            else if(data.get(i).getStdattseventyfiveper().equalsIgnoreCase("anyType{}"))
            {
              //  holder.checkBox.setChecked(false);
                holder.radio_Y_attn.setChecked(false);
                holder.radio_N_attn.setChecked(false);

                holder.radioattn.setTag(data.get(i));
               // holder.radio_Y_attn.setTag(data.get(i));
                holder.txtname.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

      //  final CheckUnCheck cn=new CheckUnCheck(); //--
        final CheckUnCheck cn=new CheckUnCheck(); //--
        //final ViewHolder finalHolder1 = holder;
        final ViewHolder finalHolder = holder;
        holder.radioattn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Radio button
             //   int pos = (int) finalHolder.radioattn.getTag();
                studentList country = (studentList) finalHolder.radioattn.getTag();

                if (finalHolder.radio_Y_attn.isChecked()){
                    Toast.makeText(context, "Above 75 %", Toast.LENGTH_LONG).show();
                    //  updateMismatchStatus(country.getStdbenid(),country.getaID(),country.getCUBy(),"Y");
                    country.setSelected(true);
                    cn.setBenID(country.getStdbenid());  //------------
                    //cn.setIsChecked("0");   //----------------
                    // cn.setIsChecked("N");   //----------------
                    cn.setIsChecked("Y");   //----------------
                    //Log.e("BenID ",country.getStdbenid()+" Status " + "0");
                    finalHolder.txtname.setTextColor(context.getResources().getColor(R.color.colortextgreen));
                    Log.e("BenID ",country.getStdbenid()+" Status " + "Y");
                    updateAttendance(country.getStdbenid(),"Y");
                    Toast.makeText(context, country.getStdname()+" का रिकॉर्ड अपडेट हो  गया है", Toast.LENGTH_SHORT).show();

                }
                else if (finalHolder.radio_N_attn.isChecked()){
                    Toast.makeText(context, "Below 75%", Toast.LENGTH_LONG).show();
                    if (country!=null){
                        country.setSelected(false);
                        cn.setBenID(country.getStdbenid()); //----------
                        // cn.setIsChecked("1");  //------------
                        //cn.setIsChecked("Y");  //------------
                        cn.setIsChecked("N");
                        finalHolder.txtname.setTextColor(context.getResources().getColor(R.color.holo_red_dark));//------------
                        //    Log.e("BenID ",country.getStdbenid()+" Status " + "1");
                        Log.e("BenID ",country.getStdbenid()+" Status " + "N");
                        updateAttendance(country.getStdbenid(),"N");
                        Toast.makeText(context, country.getStdname()+" का रिकॉर्ड अपडेट हो  गया है", Toast.LENGTH_SHORT).show();
                    }

                }

                benchek.add(cn);
            }
        });






//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                studentList country = (studentList) cb.getTag();
//                // country.setSelected(cb.isChecked());
//                if(cb.isChecked())
//                {
////                        country.setSelected(true);
////                        cn.setBenID(country.getStdbenid());
////                        cn.setIsChecked("1");
////                        Log.e("BenID ",country.getStdbenid()+" Status " + "1");
//
//                    if (country!=null)
//                    {
//                        country.setSelected(true);
//                        cn.setBenID(country.getStdbenid());  //------------
//                        //cn.setIsChecked("0");   //----------------
//                        // cn.setIsChecked("N");   //----------------
//                        cn.setIsChecked("Y");   //----------------
//                        //Log.e("BenID ",country.getStdbenid()+" Status " + "0");
//                        finalHolder.txtname.setTextColor(context.getResources().getColor(R.color.colortextgreen));
//                        Log.e("BenID ",country.getStdbenid()+" Status " + "Y");
//                        updateAttendance(country.getStdbenid(),"Y");
//                        Toast.makeText(context, country.getStdname()+" का रिकॉर्ड अपडेट हो  गया है", Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//                    // updateAttendance(country.getStdbenid(),"0");
//
//                    // benchek.add(cn);
//                }
//                else
//                {
////                        country.setSelected(false);
////                        cn.setBenID(country.getStdbenid()); //----------
////                        cn.setIsChecked("0");  //------------
////                        Log.e("BenID ",country.getStdbenid()+" Status " + "0");
//                    if (country!=null){
//                        country.setSelected(false);
//                        cn.setBenID(country.getStdbenid()); //----------
//                        // cn.setIsChecked("1");  //------------
//                        //cn.setIsChecked("Y");  //------------
//                        cn.setIsChecked("N");
//                        finalHolder.txtname.setTextColor(context.getResources().getColor(R.color.holo_red_dark));//------------
//                        //    Log.e("BenID ",country.getStdbenid()+" Status " + "1");
//                        Log.e("BenID ",country.getStdbenid()+" Status " + "N");
//                        updateAttendance(country.getStdbenid(),"N");
//                        Toast.makeText(context, country.getStdname()+" का रिकॉर्ड अपडेट हो  गया है", Toast.LENGTH_SHORT).show();
//                    }
//
//                    // updateAttendance(country.getStdbenid(),"1");
//                    // benchek.add(cn);
//                }
//                benchek.add(cn);
//
//            }
//        });

        GlobalVariables.benIDArrayList=benchek;


        studentList country = data.get(i);

        if(benchek.size()>0)
        {
            for(int x=0;x<benchek.size();x++)
            {

                try {
                    if (country.getStdbenid().equalsIgnoreCase(benchek.get(x).getBenID()))
                    {
                        //  if (benchek.get(x).getIsChecked().equalsIgnoreCase("1")) {
                        if (benchek.get(x).getIsChecked().equalsIgnoreCase("Y"))
                        {
                            //holder.checkBox.setChecked(true);
                            holder.radio_Y_attn.setChecked(true);
                            // } else if (benchek.get(x).getIsChecked().equalsIgnoreCase("0")) {
                        }
                        else if (benchek.get(x).getIsChecked().equalsIgnoreCase("N"))
                        {
                           // holder.checkBox.setChecked(false);
                            holder.radio_N_attn.setChecked(true);
                        }
                    }
                }
                catch(Exception e)
                {
                    //some error
                }
            }

        }

        String  _lang= PreferenceManager.getDefaultSharedPreferences(context).getString("LANG", "");
        if(_lang.equalsIgnoreCase("en"))
        {

            String fn=country.getStdfname();
            if(fn!=null)
            {
                fn=!country.getStdfname().contains("anyType{}") ? country.getStdfname() : "";
            }
            holder.txtfn.setText(fn);

            String cls=country.getStdclassid();
            if(cls!=null)
            {
                cls=!country.getStdclassid().contains("anyType{}") ? country.getStdclassid() : "";
            }
            holder.textView_Class.setText(cls);

            String mn=country.getStdmname();
            if(mn!=null)
            {
                mn=!country.getStdmname().contains("anyType{}") ? country.getStdmname() : "";
            }
            holder.txtmn.setText(mn);


            String dob=country.getStdDOB();
            if(dob!=null)
            {
                dob=!country.getStdDOB().contains("anyType{}") ? country.getStdDOB() : "";
                Log.e("DoB",dob);
                if(dob.length()>7) {
                    dob=dob;
                }
            }
            holder.txtdob.setText(dob);

            String sn=country.getStdname();
            if(sn!=null)
            {
                sn=!country.getStdname().contains("anyType{}") ? country.getStdname() : "";
            }
            holder.txtname.setText(sn);


            String an=country.getStdadmnum();
            if(an!=null)
            {
                an=!country.getStdadmnum().contains("anyType{}") ? country.getStdadmnum() : "";
            }

            String idno=country.getId();
            if(idno!=null)
            {
                idno=!country.getId().contains("anyType{}") ? country.getId() : "";
            }
           // holder.textViewSlNo.setText(idno);

            holder.txtadnum.setText(an);
            holder.txtBenID.setText(country.getStdbenid());
        }
        else
        {
            String fn=country.getStdfnamehn();
            if(fn!=null)
            {
                fn=!country.getStdfnamehn().contains("anyType{}") ? country.getStdfnamehn() : "";
            }
            holder.txtfn.setText(fn);

            String cls=country.getStdclassid();
            if(cls!=null)
            {
                cls=!country.getStdclassid().contains("anyType{}") ? country.getStdclassid() : "";
            }
            holder.textView_Class.setText(cls);

            String mn=country.getStdmnamehn();
            if(mn!=null)
            {
                mn=!country.getStdmnamehn().contains("anyType{}") ? country.getStdmnamehn() : "";
            }
            holder.txtmn.setText(mn);


            String dob=country.getStdDOB();
            if(dob!=null)
            {
                dob=!country.getStdDOB().contains("anyType{}") ? country.getStdDOB() : "";
                Log.e("DoB",dob);
                if(dob.length()>7)
                {
                    dob=dob;
                }
            }
            holder.txtdob.setText(dob);


            String sn=country.getStdnamehn();
            if(sn!=null)
            {
                sn=!country.getStdnamehn().contains("anyType{}") ? country.getStdnamehn() : "";
            }
            holder.txtname.setText(sn);

            String an=country.getStdadmnum();
            if(an!=null)
            {
                an=!country.getStdadmnum().contains("anyType{}") ? country.getStdadmnum() : "";
            }
            holder.txtadnum.setText(an);
            holder.txtBenID.setText(country.getStdbenid());
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
        TextView txtname,txtadnum,txtfn,txtBenID,txtmn,txtdob,textView_Class,textViewSlNo;
        CheckBox checkBox;
        LinearLayout lin_detail;

        RadioGroup radioattn;
        RadioButton radio_Y_attn,radio_N_attn;
        // EditText editnoofclasses,editnoofclassesobtain;
    }
}

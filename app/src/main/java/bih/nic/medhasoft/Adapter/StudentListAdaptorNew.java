package bih.nic.medhasoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;


public class StudentListAdaptorNew extends BaseAdapter {

    LayoutInflater mInflater;
    Context context;
    String Totclasses;
    int countChecked=0;

    ArrayList<studentList> data = new ArrayList<>();

    private HashMap<String, String> textValues = new HashMap<String, String>();

      public StudentListAdaptorNew(Context activity, ArrayList<studentList> contact, String totnnoClasses) {
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
    public long getItemId(int i) {
    return 0;
}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder holder = null;

    if (view == null) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.student_list_adaptor, null);
        holder = new ViewHolder();
        holder.txtfn = (TextView) view.findViewById(R.id.textView_fn);
        holder.txtname = (TextView) view.findViewById(R.id.textView_rollno);
        holder.txtadnum = (TextView) view.findViewById(R.id.textView);


       // holder. checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        view.setTag(holder);
        countChecked=0;
        holder.checkBox.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                studentList country = (studentList) cb.getTag();
                // country.setSelected(cb.isChecked());
                if(cb.isChecked())
                {
                    country.setSelected(true);
                }
                else
                {
                    country.setSelected(false);
                }


            }
               // Toast.makeText(context,"yyyyyyyyyyyy",Toast.LENGTH_LONG).show();
        });

    }
    else {

        holder = (ViewHolder) view.getTag();
    }


    studentList country = data.get(i);
    holder.txtfn.setText(country.getStdfname());
    holder.txtname.setText(country.getStdname());
    holder.txtadnum.setText(country.getStdadmnum());
   // holder.checkBox.setText(country.getUserId());
    holder.checkBox.setChecked(country.isSelected());
    holder.checkBox.setTag(country);

        //if(country.getStdattseventyfiveper().equalsIgnoreCase("y"))

        //MARKED---N by default
        //UnMarked--Y means att < 75%
    if(country.getStdattseventyfiveper().equalsIgnoreCase("N")) // y changed to N
    {
        holder.checkBox.setChecked(true);
        holder.checkBox.setTag(country);
        country.setSelected(true);
    }
    else
    {
        holder.checkBox.setChecked(false);
        holder.checkBox.setTag(country);
        country.setSelected(false);
    }

    notifyDataSetChanged();
    return view;

}


    private class ViewHolder {
        TextView txtname,txtadnum,txtfn;
        CheckBox checkBox;
       // EditText editnoofclasses,editnoofclassesobtain;
    }


    public void updatePerAttandanceStatus(String benid)
    {


    }
    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :
            StudentListAdaptorNew.this.textValues.put(String.valueOf(view.getTag()), editable.toString());
        }
    }

    //you can implement a method like this one for each EditText with the list position as parameter :
    public String getValueFromFirstEditText(int position){
        //here you need to recreate the id for the first editText
        String result = textValues.get("theFirstEditTextAtPos:"+position);
        Log.d("ccccccccccc",""+result+1);
        if(result ==null)
            result = "";

        return result;
    }

    public String getValueFromSecondEditText(int position){
        //here you need to recreate the id for the second editText
        String result = textValues.get("theSecondEditTextAtPos:"+position);
        if(result ==null)
            result = "";

        return result;
    }


}

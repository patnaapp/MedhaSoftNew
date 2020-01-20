package bih.nic.medhasoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;

public class CustomStdListAdaptor extends ArrayAdapter<studentList> {

    studentList[] modelItems = null;
    Context context;

    public CustomStdListAdaptor(Context context, studentList[] resource) {
        super(context, R.layout.student_list_adaptor,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.student_list_adaptor, parent, false);
        TextView fname = (TextView) convertView.findViewById(R.id.textView_fn);
        TextView name = (TextView) convertView.findViewById(R.id.textView_rollno);
        TextView adm = (TextView) convertView.findViewById(R.id.textView);
       // CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        fname.setText(modelItems[position].getStdfname());
        name.setText(modelItems[position].getStdname());
        adm.setText(modelItems[position].getStdadmnum());
       // if(modelItems[position].getStdattseventyfiveper().equalsIgnoreCase("N"))
          //  cb.setChecked(true);
       // else
           // cb.setChecked(false);
        return convertView;
    }
}

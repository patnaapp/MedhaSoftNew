package bih.nic.medhasoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import bih.nic.medhasoft.BarcodeScannerListActivity;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.ScanQrCodeActivity2;
import bih.nic.medhasoft.ScannedBarcodeActivity;
import bih.nic.medhasoft.ScannedBarcodeActivity2;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.BarcodeEntity;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.utility.GlobalVariables;
import bih.nic.medhasoft.utility.Utiilties;


public class BarcodeAdaptor extends RecyclerView.Adapter<BarcodeAdaptor.ViewHolder> {


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ArrayList<BarcodeEntity> ListItem=new ArrayList<>();
    ArrayList<BarcodeEntity>djcj=new ArrayList<>();
    Boolean isShowDetail = false;
    String unqNo="",deleteRow="";
    public static String count="";

    public BarcodeAdaptor(BarcodeScannerListActivity barcodeScannerListActivity, ArrayList<BarcodeEntity> benfiLists, ArrayList<BarcodeEntity> listrandom) {
        this.context=barcodeScannerListActivity;
        this.ListItem=benfiLists;

        this.djcj=listrandom;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtserial,tv_benName,tv_acc,tv_ifsc;
        public Button btn_view;



        public ViewHolder(View v){

            super(v);

            txtserial=(TextView)v.findViewById(R.id.tv_slno);
            tv_benName=(TextView)v.findViewById(R.id.tv_benName);
            tv_acc=(TextView)v.findViewById(R.id.tv_acc);
            tv_ifsc=(TextView)v.findViewById(R.id.tv_ifsc);
            btn_view=(Button)v.findViewById(R.id.btn_view);



        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.barcode_list_adaptor,parent,false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {

        int pos = position+1;
      //  unqNo=ListItem.get(pos).getUniqueNo();

        unqNo= PreferenceManager.getDefaultSharedPreferences(context).getString("uniqueno", "");

        holder.txtserial.setText(djcj.get(position).getSerialNo().trim());
        holder.tv_benName.setText(djcj.get(position).getBenName().trim());
        holder.tv_acc.setText(djcj.get(position).getAccountNo().trim());
        holder.tv_ifsc.setText(djcj.get(position).getIFSC().trim());
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ScanQrCodeActivity2.class);
                intent.putExtra("unqno",unqNo);
                intent.putExtra("SerialNo",djcj.get(position).getSerialNo());
                context.startActivity(intent);

            }
        });



        if(djcj.get(position).getStatus().equalsIgnoreCase("Y")){
            holder.btn_view.setBackgroundResource(R.drawable.buttonshape);
            holder.btn_view.setText("Verified");
            holder.btn_view.setEnabled(false);

        }
//        holder.btn_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new BarcodeRequestSecond("","","").execute();
//            }
//        });


    }





    @Override
    public int getItemCount(){

        return djcj.size();
    }



}

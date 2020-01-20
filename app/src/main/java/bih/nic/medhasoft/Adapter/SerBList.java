package bih.nic.medhasoft.Adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.medhasoft.R;

public class SerBList extends RecyclerView.Adapter<SerBList.ViewHolder> {


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ArrayList<bih.nic.medhasoft.Model.studentList> ListItem=new ArrayList<>();
    Boolean isShowDetail = false;


    public SerBList(Context context1, ArrayList<bih.nic.medhasoft.Model.studentList> SubjectValues1){

        ListItem = SubjectValues1;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView bm_name,bf_name,bname,panchyat_name,wardname,tv_detail_btn,ben_id,tv_class_section,ben_dob,tv_account,tv_ifsc,slno;
        LinearLayout sblist,ll_detail;
        RelativeLayout rl_view_detail;


        public ViewHolder(View v){

            super(v);

            bf_name=(TextView)v.findViewById(R.id.bf_name);
            bname=(TextView)v.findViewById(R.id.bname);
            bm_name=(TextView)v.findViewById(R.id.bm_name);
            ben_id=(TextView)v.findViewById(R.id.ben_id);
            tv_class_section=(TextView)v.findViewById(R.id.tv_class_section);
            ben_dob=(TextView)v.findViewById(R.id.ben_dob);
            tv_account=(TextView)v.findViewById(R.id.tv_account);
            tv_ifsc=(TextView)v.findViewById(R.id.tv_ifsc);
            tv_detail_btn=(TextView)v.findViewById(R.id.tv_detail_btn);
            slno=(TextView)v.findViewById(R.id.slno);

            rl_view_detail=(RelativeLayout) v.findViewById(R.id.rl_view_detail);
            ll_detail=(LinearLayout) v.findViewById(R.id.ll_detail);
//            panchyat_name=(TextView)v.findViewById(R.id.panchyat_name);
//            wardname=(TextView)v.findViewById(R.id.wardname);
//            sblist=(LinearLayout)v.findViewById(R.id.sblist);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.adaptor_server_blist,parent,false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {

        int pos = position+1;

        holder.bname.setText(ListItem.get(position).getStdname().trim());
        holder.bf_name.setText(ListItem.get(position).getStdfname().trim());
        holder.bm_name.setText(ListItem.get(position).getStdmname().trim());
        holder.ben_id.setText(ListItem.get(position).getStdbenid().trim());
        //holder.tv_class_section.setText(ListItem.get(position).getStdclass().trim()+"/");
        holder.ben_dob.setText(ListItem.get(position).getStdDOB().trim());
        holder.tv_account.setText(ListItem.get(position).getStdacnum().trim());
        holder.tv_ifsc.setText(ListItem.get(position).getStdifsc().trim());
        holder.ll_detail.setVisibility(View.GONE);
        //String sourceString =ListItem.get(position).getStdmname();
        holder.rl_view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowDetail = !isShowDetail;

                if(isShowDetail){
                    holder.tv_detail_btn.setText("View Less");
                    holder.ll_detail.setVisibility(View.VISIBLE);
                }else{
                    holder.tv_detail_btn.setText("View More");
                    holder.ll_detail.setVisibility(View.GONE);
                }
                //Toast.makeText(context, "view detail", Toast.LENGTH_SHORT).show();
            }
        });

        holder.slno.setText(Integer.toString(pos)+").");
        pos++;

    }





    @Override
    public int getItemCount(){

        return ListItem.size();
    }
}
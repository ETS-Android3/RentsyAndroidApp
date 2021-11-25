package com.menlopark.rentsyuser.ui.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.HalpCata.Detail;

import java.util.ArrayList;

public class Help_Adapter extends RecyclerView.Adapter<Help_Adapter.MyViewHolder> {
    private Context mContext;
    ArrayList<Detail> HelpQueList;
    int i;
    String sSub_Cat_Id;

    public Help_Adapter(HelpActivity helpActivity, ArrayList<Detail> helpQueList) {
        this.mContext = helpActivity;
        this.HelpQueList = helpQueList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_main_cat;
        public LinearLayout myLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

            myLinearLayout = itemView.findViewById(R.id.myLinearLayout);
            tv_main_cat = itemView.findViewById(R.id.tv_main_cat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public Help_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_help, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(Help_Adapter.MyViewHolder holder, int position) {
        Detail helpQueList = HelpQueList.get(position);

        holder.tv_main_cat.setText(helpQueList.getTypeName());
//        for (i = 0; i < helpQueList.getSubCategory().size(); i++) {
//
//            TextView textView1 = new TextView(mContext);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 2, 0, 2);
//            textView1.setLayoutParams(params);
//          //  textView1.setId(Integer.parseInt(helpQueList.getSubCategory().get(i).getId().toString()));
//            textView1.setTextColor(mContext.getResources().getColor(R.color.black_overlay));
//           // textView1.setText(helpQueList.getSubCategory().get(i).getCategoryName());
//
//
//
//
//            View v = new View(mContext);
//            v.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    1
//            ));
//            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
//
//
//         //   sSub_Cat_Id = String.valueOf(helpQueList.getSubCategory().get(i).getId());
//            textView1.setPadding(50, 20, 30, 20);
//            textView1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent_qus = new Intent(mContext, Question_Activity.class);
//                    intent_qus.putExtra("cat_id", sSub_Cat_Id);
//                    mContext.startActivity(intent_qus);
//                }
//            });
//            holder.myLinearLayout.addView(textView1);
//            holder.myLinearLayout.addView(v);
//        }
    }

    @Override
    public int getItemCount() {
        return HelpQueList.size();
    }

}

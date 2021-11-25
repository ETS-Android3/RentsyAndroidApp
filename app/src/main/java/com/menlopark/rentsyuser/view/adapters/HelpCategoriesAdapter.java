package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.DoubleColumnBinding;
import com.menlopark.rentsyuser.databinding.SingleColumnBinding;
import com.menlopark.rentsyuser.model.HalpCata.Detail;

import java.util.ArrayList;

public class HelpCategoriesAdapter extends RecyclerView.Adapter<HelpCategoriesAdapter.MyViewHolder> {

    private ArrayList<com.menlopark.rentsyuser.model.HalpCata.Detail> mData;
    private LayoutInflater mInflater;
    private CatalogAdapter.ItemClickListener mClickListener;
    private Context context;



    public HelpCategoriesAdapter(FragmentActivity activity, ArrayList<com.menlopark.rentsyuser.model.HalpCata.Detail> helpQueList) {
        this.context = activity;
        this.mData = helpQueList;
    }

    @Override
    public HelpCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case Detail.SINGLE_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_column, parent, false);
                return new HelpCategoriesAdapter.MyViewHolder((SingleColumnBinding) DataBindingUtil.bind(v));

            case Detail.DOUBLE_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_column, parent, false);
                return new HelpCategoriesAdapter.MyViewHolder((DoubleColumnBinding) DataBindingUtil.bind(v));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(HelpCategoriesAdapter.MyViewHolder holder, int position) {

        switch (mData.get(position).typeid) {
            case 0:
                holder.binding.tvSingleHelpCat.setText(mData.get(position).getTypeName());
  //              return Detail.SINGLE_TYPE;
            case 1:
                holder.bindingd.tvDoubleHelpCat.setText(mData.get(position).getTypeName());
                //return Detail.DOUBLE_TYPE;
            default:
                //return -1;
        }
        //  holder.binding.tvSingleHelpCat.setText(mData.get(position).getTypeName());
//        holder.bindingd.tvDoubleHelpCat.setText("1");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SingleColumnBinding binding;
        DoubleColumnBinding bindingd;

        public MyViewHolder(SingleColumnBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public MyViewHolder(DoubleColumnBinding bind) {
            super(bind.getRoot());
            this.bindingd = bind;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mData.get(position).typeid) {
            case 0:
                return Detail.SINGLE_TYPE;
            case 1:
                return Detail.DOUBLE_TYPE;
            default:
                return -1;

        }

    }


}

package com.menlopark.rentsyuser.widget.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.menlopark.rentsyuser.R;


/**
 * Created by techiestown on 14/6/16.
 */
public class ProgressDialog extends DialogFragment {

    private int mProgressHit = 0;
    private static ProgressDialog sDialogProgress;
    private boolean mIsNew = true;

    @NonNull
    public static ProgressDialog newInstance(){
        if (sDialogProgress == null){
            sDialogProgress = new ProgressDialog();
        }else {
            sDialogProgress.mIsNew = false;
        }
        sDialogProgress.mProgressHit = 0;
        return sDialogProgress;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_progress, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressHit++;
                if (mProgressHit > 2){
                    dismiss();
                }
            }
        });

        return view;
    }

    public boolean isNew(){
        return mIsNew;
    }
}

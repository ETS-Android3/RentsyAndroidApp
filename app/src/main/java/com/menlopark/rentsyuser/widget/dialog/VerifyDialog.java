package com.menlopark.rentsyuser.widget.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;

import com.menlopark.rentsyuser.R;


public class VerifyDialog extends BaseDialog implements View.OnClickListener{
    VerifyDialog mBinding;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreateView(inflater, container, savedInstanceState);
        getComponent().inject(this);
       // mBinding = DataBindingUtil.inflate(inflater,R.layout.activity_verify_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setContentView(R.layout.activity_verify_dialog);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.white);
        getDialog().setCanceledOnTouchOutside(false);

     //   mBinding.content.verify.setOnClickListener(this);
        return container;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.verify_btnok:
                hideKeyboard();
                dismiss();
                break;*/

        }

    }

}

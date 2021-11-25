package com.menlopark.rentsyuser.widget.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.di.component.ActivityComponent;
import com.menlopark.rentsyuser.util.Utils;
import com.menlopark.rentsyuser.ui.BaseActivity;


/**
 * Created by dilan on 1/11/2016.
 */
public class BaseDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void hideKeyboard(){
        Utils.hideKeyboard(getActivity());
    }

    @Nullable
    public BaseActivity getBaseActivity(){
        Activity activity = getActivity();
        if (activity instanceof BaseActivity){
            BaseActivity baseActivity = (BaseActivity) activity;
            return baseActivity;
        }
        return null;
    }

    @Nullable
    public ActivityComponent getComponent() {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null){
            return baseActivity.getComponent();
        }
        return null;
    }

    public void showToast(@StringRes int msg) {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null){
            baseActivity.showToast(msg);
        }
    }

    public void showToast(String msg) {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null){
            baseActivity.showToast(msg);
        }
    }

    public void notYetImplemented(){
        showToast(R.string.not_yet_implemented);
    }
}

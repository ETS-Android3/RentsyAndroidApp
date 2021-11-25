package com.menlopark.rentsyuser.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.menlopark.rentsyuser.RenrsyUser;
import com.menlopark.rentsyuser.di.component.ActivityComponent;
import com.menlopark.rentsyuser.di.component.DaggerActivityComponent;
import com.menlopark.rentsyuser.util.L;
import com.menlopark.rentsyuser.util.UiUtil;
import com.menlopark.rentsyuser.widget.dialog.ProgressDialog;


/**
 * Created by techiestown on 14/6/16.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseA";
    private ActivityComponent mComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        mComponent = DaggerActivityComponent.builder().appComponent(getApp().getAppComponent()).build();
    }

    protected RenrsyUser getApp() {
        return (RenrsyUser) getApplicationContext();
    }

    public ActivityComponent getComponent() {
        return mComponent;
    }


    public void showToast(@StringRes int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

  /*  public void showNotYetImplemented(){
        showToast(R.string.not_yet_implemented);
    }
*/

    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog dialog = ProgressDialog.newInstance();
                if (dialog.isNew()) {
                    dialog.show(getSupportFragmentManager(), "progress_dialog");
                }
            }
        });
    }

    public void dismissProgress() {
        final ProgressDialog dialogProgress = ProgressDialog.newInstance();
        if (dialogProgress.getDialog() != null && dialogProgress.getDialog().isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogProgress.dismiss();
                }
            });
        }
    }

    public void hideKeyboard(){
        UiUtil.hideKeyboard(this);
    }

    public void showSwipeRefresh(@NonNull final SwipeRefreshLayout swipeRefreshLayout, final boolean show){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }


    public View.OnClickListener settingClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            L.d(TAG,"setting");
          //  startActivity(new Intent(BaseActivity.this, SettingActivity.class));
        }
    };

    public void setActionBarTitle(String s) {
        getSupportActionBar().setTitle(s);


     /* getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);


        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
      getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
      getSupportActionBar().setCustomView(textView);*/
        
        
    }
}

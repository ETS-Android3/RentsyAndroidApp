package com.menlopark.rentsyuser.util;


import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.widget.ImageView;

import com.menlopark.rentsyuser.R;




public class Dialog extends android.app.Dialog {
    ImageView imageLoader;
    public Dialog(Context context) {
        super(context, R.style.FullHeightDialog);

        this.setContentView(R.layout.dialog_progress);
        this.setCancelable(false);
    }
}

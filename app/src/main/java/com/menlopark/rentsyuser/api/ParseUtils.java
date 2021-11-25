package com.menlopark.rentsyuser.api;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ParseUtils {

    public static int card_count = 0;
    private static String date_selected;


    public static void CommonDialog(final Context context, String title, String btn_text, final String where) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_delete);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_done = (TextView) dialog.findViewById(R.id.txt_done);
        TextView txt_details_invite = (TextView) dialog.findViewById(R.id.txt_details_invite);
        TextView txt_details = (TextView) dialog.findViewById(R.id.txt_details);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        txt_title.setText(title);
        txt_done.setText(btn_text);
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (where.equalsIgnoreCase("cart")) {
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("cart", "cart");
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                }

            }
        });
        if (where.equalsIgnoreCase(context.getString(R.string.invite))) {
            txt_details_invite.setVisibility(View.VISIBLE);
            txt_details.setText(context.getString(R.string.earn_money));
        }
        dialog.show();
    }



    public static void Applicationrecieved(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_delete);
        dialog.show();
    }

    public static void callScheduleDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_schedule);

        TextView btn_done = (TextView) dialog.findViewById(R.id.btn_done);
        final TextView txtdate = (TextView) dialog.findViewById(R.id.date);
        final TextView txtend_date = (TextView) dialog.findViewById(R.id.btn_end_date);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(myCalendar, txtdate);
            }

        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, txtend_date);
            }

        };

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(context, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
        txtend_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtend_date.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        dialog.show();
    }


    private static void updateLabel(Calendar myCalendar, TextView txtdate) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtdate.setText(sdf.format(myCalendar.getTime()));
    }

    public static void CancelBooking(final Context context, final String type) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_booking);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_reason1 = (TextView) dialog.findViewById(R.id.txt_reason1);
        TextView btn_done = (TextView) dialog.findViewById(R.id.btn_done);


        if (type.equalsIgnoreCase("cancel")) {
            txt_title.setText("Cancel Booking");
            btn_done.setAllCaps(true);
        }
        txt_title.setVisibility(View.VISIBLE);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type.equalsIgnoreCase("cancel")) {
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                }
            }
        });
        dialog.show();
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("dummy_location.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

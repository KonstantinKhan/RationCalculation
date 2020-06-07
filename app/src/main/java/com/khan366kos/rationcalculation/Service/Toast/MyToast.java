package com.khan366kos.rationcalculation.Service.Toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

    public MyToast() {
    }

    public static void showToast(Context context, String s) {
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }
}

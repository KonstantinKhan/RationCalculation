package com.khan366kos.rationcalculation.Service.AppCursorAdapter;

import android.content.Context;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class CursorAdapterFactory {
    public SimpleCursorAdapter getCursorAdapter(Context context, CursorAdapterTypes types) {
        SimpleCursorAdapter toReturn = null;
        switch (types) {
            case PRODUCTS:
                toReturn = new CursorAdapterProduct(context);
                break;
            case DISHES:
                toReturn = new CursorAdapterDish(context);
                break;
            default:
        }
        return toReturn;
    }
}

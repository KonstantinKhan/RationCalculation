package com.khan366kos.rationcalculation.Service.AppCursorAdapter;

import android.content.Context;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.khan366kos.rationcalculation.R;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.*;

public class CursorAdapterDish extends SimpleCursorAdapter {
    public CursorAdapterDish(Context context) {
        super(context, R.layout.fragment_suggestions,
                null,
                new String[]{COLUMN_DISH_NAME,
                        COLUMN_DISH_CALORIES,
                        COLUMN_DISH_PROTEINS,
                        COLUMN_DISH_FATS,
                        COLUMN_DISH_CARBOHYDRATES},
                new int[]{R.id.tv_suggestion_product_name,
                        R.id.tv_suggestion_product_calories,
                        R.id.tv_suggestion_product_proteins,
                        R.id.tv_suggestion_product_fats,
                        R.id.tv_suggestion_product_carbohydrates},
                0);
    }
}

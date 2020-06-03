package com.khan366kos.rationcalculation.Service.AppCursorAdapter;

import android.content.Context;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.khan366kos.rationcalculation.R;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_CALORIES;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_FATS;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_PROTEINS;

public class CursorAdapterProduct extends SimpleCursorAdapter {
    public CursorAdapterProduct(Context context) {
        super(context, R.layout.fragment_suggestions,
                null,
                new String[]{COLUMN_PRODUCT_NAME,
                        COLUMN_PRODUCT_CALORIES,
                        COLUMN_PRODUCT_PROTEINS,
                        COLUMN_PRODUCT_FATS,
                        COLUMN_PRODUCT_CARBOHYDRATES},
                new int[]{R.id.tv_suggestion_product_name,
                        R.id.tv_suggestion_product_calories,
                        R.id.tv_suggestion_product_proteins,
                        R.id.tv_suggestion_product_fats,
                        R.id.tv_suggestion_product_carbohydrates},
                0);
    }
}

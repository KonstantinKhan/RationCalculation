package com.khan366kos.rationcalculation;

import android.provider.BaseColumns;

public final class ProductContract {
    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        public final static String _ID = BaseColumns._ID;

        public final static String TABLE_NAME_PRODUCTS = "products";
        public final static String COLUMN_PRODUCT_NAME = "productName";
        public final static String COLUMN_PRODUCT_CALORIES = "productCalories";
        public final static String COLUMN_PRODUCT_PROTEINS = "productProteins";
        public final static String COLUMN_PRODUCT_FATS = "productFats";
        public final static String COLUMN_PRODUCT_CARBOHYDRATES = "productCarbohydrates";

        public final static String TABLE_DISHES = "dishes";
        public final static String COLUMN_DISH_NAME = "dishName";
        public final static String COLUMN_DISH_CALORIES = "dishCalories";
        public final static String COLUMN_DISH_PROTEINS = "dishProteins";
        public final static String COLUMN_DISH_FATS = "dishFats";
        public final static String COLUMN_DISH_CARBOHYDRATES = "dishCarbohydrates";
        public final static String COLUMN_DISH_BLOB = "dishBlob";

        public final static String TAG = "MyLog";
    }
}

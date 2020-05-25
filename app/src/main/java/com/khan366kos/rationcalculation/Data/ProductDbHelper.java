package com.khan366kos.rationcalculation.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.khan366kos.Objects.Dish;
import com.khan366kos.rationcalculation.ProductContract.ProductEntry;

import java.io.ByteArrayOutputStream;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TABLE_DISHES;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TABLE_NAME_PRODUCTS;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

public class ProductDbHelper extends SQLiteOpenHelper {

    private volatile static ProductDbHelper instance;

    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;
    private Cursor cursor;

    private ProductDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static ProductDbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (ProductDbHelper.class) {
                if (instance == null) {
                    instance = new ProductDbHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Строка для создания таблицы для хранения данных о продуктах
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME_PRODUCTS + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, "
                + ProductEntry.COLUMN_PRODUCT_CALORIES + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PROTEINS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_FATS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES + " DOUBLE DEFAULT 0) ;";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        // Строка для создания таблицы для хранения данных о блюдах
        String SQL_CREATE_DISHES_TABLE = "CREATE TABLE " + ProductEntry.TABLE_DISHES + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + ProductEntry.COLUMN_DISH_NAME + " TEXT NOT NULL UNIQUE, "
                + ProductEntry.COLUMN_DISH_CALORIES + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_DISH_PROTEINS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_FATS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_CARBOHYDRATES + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_BLOB + " BLOB NOT NULL) ;";

        db.execSQL(SQL_CREATE_DISHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void setDb() {
        if (db == null) {
            //Log.d(TAG, "Подключаем базу данных");
            db = this.getReadableDatabase();
        }
    }

    public SQLiteDatabase getDb() {
        setDb();
        return db;
    }

    public void insertProduct(String productName,
                              String productCalories,
                              String productProteins,
                              String productFats,
                              String productCarbohydrates) {
        ContentValues cv = new ContentValues();
        cv.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        cv.put(ProductEntry.COLUMN_PRODUCT_CALORIES, Integer.parseInt(productCalories.replace(",", ".")));
        cv.put(ProductEntry.COLUMN_PRODUCT_PROTEINS, Double.parseDouble(productProteins.replace(",", ".")));
        cv.put(ProductEntry.COLUMN_PRODUCT_FATS, Double.parseDouble(productFats.replace(",", ".")));
        cv.put(ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES, Double.parseDouble(productCarbohydrates.replace(",", ".")));

        db.insert(ProductEntry.TABLE_NAME_PRODUCTS, null, cv);
    }

    // Метод для сохранения блюда в базу данных.
    public void insertDish(SQLiteDatabase db, Dish dish, ByteArrayOutputStream outputStream) {
        ContentValues cv = new ContentValues();
        cv.put(ProductEntry.COLUMN_DISH_BLOB, outputStream.toByteArray());
        cv.put(ProductEntry.COLUMN_DISH_NAME, dish.getName());
        cv.put(ProductEntry.COLUMN_DISH_CALORIES, (int) dish.getCaloriesCooked());
        cv.put(ProductEntry.COLUMN_DISH_PROTEINS, dish.getProteinsCooked());
        cv.put(ProductEntry.COLUMN_DISH_FATS, dish.getFatsCooked());

        try {
            cv.put(ProductEntry.COLUMN_DISH_CARBOHYDRATES, dish.getCarbohydratesCooked());
            db.insertOrThrow(ProductEntry.TABLE_DISHES, null, cv);
            Cursor c = db.query(TABLE_DISHES,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            Log.d(TAG, String.valueOf(c.getCount()));
        } catch (SQLException e) {
            Log.d(TAG, "Блюдо с таким названием уже есть в базе");
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(String tableName) {
        this.cursor = db.query(tableName,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    public void deleteProduct(String name) {
        db.delete(TABLE_NAME_PRODUCTS, COLUMN_PRODUCT_NAME + " = '" + name + "'", null);
    }
}

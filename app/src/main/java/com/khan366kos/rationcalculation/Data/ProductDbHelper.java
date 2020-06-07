package com.khan366kos.rationcalculation.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry;
import com.khan366kos.rationcalculation.Model.Ration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.*;

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
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_PRODUCTS + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, "
                + ProductEntry.COLUMN_PRODUCT_CALORIES + " DOUBLE NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PROTEINS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_FATS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES + " DOUBLE DEFAULT 0) ;";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        // Строка для создания таблицы для хранения данных о блюдах
        String SQL_CREATE_DISHES_TABLE = "CREATE TABLE " + ProductEntry.TABLE_DISHES + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + ProductEntry.COLUMN_DISH_NAME + " TEXT NOT NULL UNIQUE, "
                + ProductEntry.COLUMN_DISH_CALORIES + " DOUBLE NOT NULL, "
                + ProductEntry.COLUMN_DISH_PROTEINS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_FATS + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_CARBOHYDRATES + " DOUBLE DEFAULT 0, "
                + ProductEntry.COLUMN_DISH_BLOB + " BLOB NOT NULL) ;";

        db.execSQL(SQL_CREATE_DISHES_TABLE);

        // Строка для создания таблцы для хранения рационов
        String SQL_CREATE_RATIONS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_RATIONS + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + ProductEntry.COLUMN_RATION_DATE + " TEXT NOT NULL UNIQUE, "
                + ProductEntry.COLUMN_RATION_BLOB + " BLOB NOT NULL) ;";

        db.execSQL(SQL_CREATE_RATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void setDb() {
        if (db == null) {
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

        db.insert(ProductEntry.TABLE_PRODUCTS, null, cv);
    }

    // Метод для сохранения блюда в базу данных.
    public void insertDish(Dish dish) {

        // Сериализация блюда для хранения его в базе данных.
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(dish);
            outputStream.close();

            ContentValues cv = new ContentValues();
            cv.put(ProductEntry.COLUMN_DISH_BLOB, byteArrayOutputStream.toByteArray());
            cv.put(ProductEntry.COLUMN_DISH_NAME, dish.getName());
            cv.put(ProductEntry.COLUMN_DISH_CALORIES, dish.getCaloriesDefault());
            cv.put(ProductEntry.COLUMN_DISH_PROTEINS, dish.getProteinsDefault());
            cv.put(ProductEntry.COLUMN_DISH_FATS, dish.getFatsDefault());
            cv.put(ProductEntry.COLUMN_DISH_CARBOHYDRATES, dish.getCarbohydratesDefault());
            db.insertOrThrow(TABLE_DISHES, null, cv);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertRation(Ration ration) {
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(ration);
            outputStream.close();

            ContentValues cv = new ContentValues();
            cv.put(ProductEntry.COLUMN_RATION_DATE, ration.getData());
            cv.put(ProductEntry.COLUMN_RATION_BLOB, byteArrayOutputStream.toByteArray());
            db.insertOrThrow(TABLE_RATIONS, null, cv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCursor(TABLE_RATIONS);
        cursor.close();
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
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_NAME + " = '" + name + "'", null);
    }

    public void updateRation(Ration ration) {
        setDb();
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(ration);
            outputStream.close();

            ContentValues cv = new ContentValues();
            cv.put(ProductContract.ProductEntry.COLUMN_RATION_DATE, ration.getData());
            cv.put(ProductContract.ProductEntry.COLUMN_RATION_BLOB, byteArrayOutputStream.toByteArray());

            db.update(TABLE_RATIONS,
                    cv,
                    COLUMN_RATION_DATE + " = ?",
                    new String[]{String.valueOf(ration.getData())});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

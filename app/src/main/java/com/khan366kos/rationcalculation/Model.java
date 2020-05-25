package com.khan366kos.rationcalculation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khan366kos.Objects.Product;
import com.khan366kos.rationcalculation.Data.ProductDbHelper;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_FATS;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.COLUMN_PRODUCT_PROTEINS;

public class Model {

    private ProductDbHelper productDbHelper;

    public Model() {

        // Вызываем экземляр ProductDbHelper.
       // productDbHelper = ProductDbHelper.getInstance((Context) viewHolder);

        // Создаем и запускаем поток.
        // См. реализацию в ProductDbHelper.run()
        /*Thread t = new Thread(productDbHelper);
        t.start();

        try {
            // Ждем завершения работы нити.
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void deleteProduct(String name, String tableName) {

        // Удаляем продукт из базы данных по _id продукта в базе данных.
        productDbHelper.deleteProduct(name);

        // Обновляем курсор после удаления записи.
        productDbHelper.setCursor(tableName);
    }

    public void close() {
        productDbHelper.close();
    }

    public int itemCount(String tableName) {
        productDbHelper.setCursor(tableName);
        int count = productDbHelper.getCursor().getCount();
        productDbHelper.getCursor().close();
        return count;
    }

    public int countEntries(String tableName) {
        SQLiteDatabase db = productDbHelper.getDb();
        Cursor cursor = db.query(tableName,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor.getCount();
    }

    public Product loadProduct(String tableName, int position) {
        productDbHelper.setCursor(tableName);
        Cursor cursor = productDbHelper.getCursor();
        cursor.moveToPosition(position);
        Product product = new Product(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_PROTEINS)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PROTEINS)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_FATS)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_CARBOHYDRATES)));
        // Log.d(TAG, "Продукт загружен " + cursor.getInt(cursor.getColumnIndex(_ID)));
        return product;
    }
}

package com.khan366kos.rationcalculation.getway;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Data.ProductDbHelper;
import com.khan366kos.rationcalculation.ProductContract;
import com.khan366kos.rationcalculation.RationCalculationApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.*;

public class DataProvider {

    private int columnProductId;

    @Inject
    ProductDbHelper productDbHelper;

    public DataProvider() {
        RationCalculationApp.getComponent().injectDataProvider(this);
    }

    public Observable<List<Product>> getAllProducts() {
        return Observable.fromCallable(this::callGetAllProducts);
    }

    public Observable<Product> getProduct(String productName) {
        return Observable.fromCallable(() -> callGetProduct(productName));
    }

    public Observable<Void> addProduct(String productName, String productCalories,
                                       String productProteins, String productFats,
                                       String productCarbohydrates) {
        return Observable.fromCallable(() -> {
            callAddProduct(productName, productCalories, productProteins,
                    productFats, productCarbohydrates);
            return null;
        });
    }

    public Observable<Void> deleteProduct(String nameProduct) {
        return Observable.fromCallable(() -> {
            productDbHelper.setDb();
            productDbHelper.deleteProduct(nameProduct);
            return null;
        });
    }

    public Observable<Product> updateProduct(String productName, double productCalories,
                                             double productProteins, double productFats,
                                             double productCarbohydrates) {
        return Observable.fromCallable(() -> callUpdateProduct(productName, productCalories,
                productProteins, productFats, productCarbohydrates)
        );
    }

    public Observable<List<Product>> getCursor(String string, List<Product> items) {
        return Observable.fromCallable(() -> callQueryProduct(string, items));
    }

    private void callAddProduct(String productName, String productCalories, String productProteins,
                                String productFats, String productCarbohydrates) {
        productDbHelper.insertProduct(productName, productCalories, productProteins,
                productFats, productCarbohydrates);
    }

    private Product callGetProduct(String productName) {
        Product product;
        productDbHelper.setDb();

        Cursor cursor = productDbHelper.getDb().query(TABLE_NAME_PRODUCTS,
                null,
                COLUMN_PRODUCT_NAME + " = ?",
                new String[]{productName},
                null,
                null,
                null);

        int columnProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        int columnProductCalories = cursor.getColumnIndex(COLUMN_PRODUCT_CALORIES);
        int columnProductProteins = cursor.getColumnIndex(COLUMN_PRODUCT_PROTEINS);
        int columnProductFats = cursor.getColumnIndex(COLUMN_PRODUCT_FATS);
        int columnProductCarbohydrates = cursor.getColumnIndex(COLUMN_PRODUCT_CARBOHYDRATES);

        cursor.moveToFirst();

        product = new Product(cursor.getString(columnProductName),
                cursor.getInt(columnProductCalories),
                cursor.getDouble(columnProductProteins),
                cursor.getDouble(columnProductFats),
                cursor.getDouble(columnProductCarbohydrates));

        columnProductId = cursor.getInt(cursor.getColumnIndex(_ID));

        cursor.close();

        return product;
    }

    private List<Product> callGetAllProducts() {

        Product product;
        List<Product> products = new ArrayList<>();

        productDbHelper.setDb();

        productDbHelper.setCursor(TABLE_NAME_PRODUCTS);
        Cursor cursor = productDbHelper.getCursor();

        int columnProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        int columnProductCalories = cursor.getColumnIndex(COLUMN_PRODUCT_CALORIES);
        int columnProductProteins = cursor.getColumnIndex(COLUMN_PRODUCT_PROTEINS);
        int columnProductFats = cursor.getColumnIndex(COLUMN_PRODUCT_FATS);
        int columnProductCarbohydrates = cursor.getColumnIndex(COLUMN_PRODUCT_CARBOHYDRATES);

        if (cursor.moveToFirst()) {
            do {
                product = new Product(cursor.getString(columnProductName),
                        cursor.getDouble(columnProductCalories),
                        cursor.getDouble(columnProductProteins),
                        cursor.getDouble(columnProductFats),
                        cursor.getDouble(columnProductCarbohydrates));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /*for (int i = 0; i < 1000; i++) {
            product = new Product("Номер продукта: " + i, i, i, i, i);
            products.add(product);
        }*/

        return products;
    }

    private Product callUpdateProduct(String productName, double productCalories,
                                      double productProteins, double productFats,
                                      double productCarbohydrates) {
        ContentValues cv = new ContentValues();
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_CALORIES, productCalories);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PROTEINS, productProteins);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_FATS, productFats);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_CARBOHYDRATES, productCarbohydrates);

        productDbHelper.getDb().update(TABLE_NAME_PRODUCTS,
                cv,
                "_id = ?",
                new String[]{String.valueOf(columnProductId)});

        return callGetProduct(productName);
    }

    private List<Product> callQueryProduct(String s, List<Product> items) {
        productDbHelper.setDb();

        List<Product> queryProduct = new ArrayList<>();
        Product product;

        Cursor cursor = productDbHelper.getDb().query(TABLE_NAME_PRODUCTS,
                null,
                where(s, items).toString(),
                null,
                null,
                null,
                null,
                null);

        int columnProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        int columnProductCalories = cursor.getColumnIndex(COLUMN_PRODUCT_CALORIES);
        int columnProductProteins = cursor.getColumnIndex(COLUMN_PRODUCT_PROTEINS);
        int columnProductFats = cursor.getColumnIndex(COLUMN_PRODUCT_FATS);
        int columnProductCarbohydrates = cursor.getColumnIndex(COLUMN_PRODUCT_CARBOHYDRATES);

        if (cursor.moveToFirst()) {
            do {
                product = new Product(cursor.getString(columnProductName),
                        cursor.getDouble(columnProductCalories),
                        cursor.getDouble(columnProductProteins),
                        cursor.getDouble(columnProductFats),
                        cursor.getDouble(columnProductCarbohydrates));
                queryProduct.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return queryProduct;
    }

    // Метод для получения значений продуктов, которые уже использованы в блюде в виде строки,
    // которая передается в SQL-запрос.
    private StringBuilder where(String string, List<Product> items) {
        String selection = "";
        StringBuilder values = new StringBuilder();
        if (items.size() > 0) {
            selection = COLUMN_PRODUCT_NAME + " NOT LIKE ";
            for (int i = 0; i < items.size(); i++) {
                if (i == 0) {
                    values.append("'" + items.get(i).getName() + "'");
                } else {
                    values.append(" AND " + selection + "'" + items.get(i).getName() + "'");
                }
            }
        }

        StringBuilder val = new StringBuilder();
        if (items.size() > 0) {
            if (string.length() > 0) {
                val.append(" AND " + COLUMN_PRODUCT_NAME + " LIKE '%" + string + "%'");
            }
        } else {
            if (string.length() > 0) {
                val.append(COLUMN_PRODUCT_NAME + " LIKE '%" + string + "%'");
            }
        }
        return new StringBuilder(selection).append(values).append(val);
    }
}

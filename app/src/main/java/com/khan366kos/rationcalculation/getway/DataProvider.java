package com.khan366kos.rationcalculation.getway;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Data.ProductDbHelper;
import com.khan366kos.rationcalculation.Data.ProductContract;
import com.khan366kos.rationcalculation.Model.Ration;
import com.khan366kos.rationcalculation.RationCalculationApp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.*;

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

    public Observable<List<Product>> getCursorDish(String s, List<Product> components) {
        return Observable.fromCallable(() -> callQueryDishes(s, components));
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

    public Observable<List<Product>> getCursorProduct(String string, List<Product> items) {
        return Observable.fromCallable(() -> callQueryProducts(string, items));
    }

    public Observable<Void> addDish(Dish dish) {
        return Observable.fromCallable(() -> {
            callAddDish(dish);
            return null;
        });
    }

    public Observable<List<Dish>> getAllDishes() {
        return Observable.fromCallable(this::callGetAllDishes);
    }

    public Observable<Ration> getQueryRation(String date) {
        return Observable.fromCallable(() -> callQueryRation(date));
    }

    public Observable<Void> saveRation(Ration ration) {
        return Observable.fromCallable(() -> {
            callInsertRation(ration);
            return null;
        });
    }

    public Observable<Void> updateRation(Ration ration) {
        return Observable.fromCallable(() -> {
            callUpdateRation(ration);
            return null;
        });
    }

    public Observable<Dish> getDish(String name) {
        return Observable.fromCallable(() -> callGetDish(name));
    }

    private void callAddProduct(String productName, String productCalories, String productProteins,
                                String productFats, String productCarbohydrates) {

        productDbHelper.insertProduct(productName, productCalories, productProteins,
                productFats, productCarbohydrates);
    }

    private Product callGetProduct(String productName) {
        Product product;
        productDbHelper.setDb();

        Cursor cursor = productDbHelper.getDb().query(TABLE_PRODUCTS,
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

        productDbHelper.setCursor(TABLE_PRODUCTS);
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

        productDbHelper.getDb().update(TABLE_PRODUCTS,
                cv,
                "_id = ?",
                new String[]{String.valueOf(columnProductId)});

        return callGetProduct(productName);
    }

    private List<Product> callQueryProducts(String s, List<Product> items) {
        productDbHelper.setDb();

        List<Product> queryProduct = new ArrayList<>();
        Product product;

        Cursor cursor = productDbHelper.getDb().query(TABLE_PRODUCTS,
                null,
                where(s, items, COLUMN_PRODUCT_NAME).toString(),
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
    private StringBuilder where(String string, List<Product> items, String columnName) {
        String selection = "";
        StringBuilder values = new StringBuilder();
        if (items.size() > 0) {
            selection = columnName + " != ";
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
                val.append(" AND " + columnName + " LIKE '%" + string + "%'");
            }
        } else {
            if (string.length() > 0) {
                val.append(columnName + " LIKE '%" + string + "%'");
            }
        }
        return new StringBuilder(selection).append(values).append(val);
    }

    private void callAddDish(Dish dish) {
        productDbHelper.insertDish(dish);
    }

    private List<Dish> callGetAllDishes() {
        Dish dish;
        List<Dish> dishes = new ArrayList<>();

        productDbHelper.setDb();

        productDbHelper.setCursor(TABLE_DISHES);
        Cursor cursor = productDbHelper.getCursor();

        ByteArrayInputStream arrayInputStream;
        ObjectInputStream objectInputStream;

        if (cursor.moveToFirst()) {
            do {
                try {
                    arrayInputStream = new ByteArrayInputStream(cursor.getBlob(cursor
                            .getColumnIndex(COLUMN_DISH_BLOB)));
                    objectInputStream = new ObjectInputStream(arrayInputStream);
                    dish = (Dish) objectInputStream.readObject();
                    dishes.add(dish);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return dishes;
    }

    private List<Product> callQueryDishes(String s, List<Product> items) {
        productDbHelper.setDb();

        List<Product> queryProduct = new ArrayList<>();
        Dish dish;

        ByteArrayInputStream arrayInputStream;
        ObjectInputStream objectInputStream;

        Cursor cursor = productDbHelper.getDb().query(TABLE_DISHES,
                null,
                where(s, items, COLUMN_DISH_NAME).toString(),
                null,
                null,
                null,
                null);


        if (cursor.moveToFirst()) {
            do {
                try {
                    arrayInputStream = new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndex(COLUMN_DISH_BLOB)));
                    objectInputStream = new ObjectInputStream(arrayInputStream);
                    dish = (Dish) objectInputStream.readObject();
                    queryProduct.add(dish);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return queryProduct;
    }

    private Ration callQueryRation(String date) {
        Log.d(TAG, "callQueryRation: " + date);
        productDbHelper.setDb();
        Ration ration = null;

        ByteArrayInputStream arrayInputStream;
        ObjectInputStream objectInputStream;

        Cursor cursor = productDbHelper.getDb().query(TABLE_RATIONS,
                null,
                COLUMN_RATION_DATE + " = ?",
                new String[]{date},
                null,
                null,
                null);

        Log.d(TAG, "callQueryRation: " + cursor.getCount());

        cursor.moveToFirst();

        try {
            arrayInputStream = new ByteArrayInputStream(cursor.getBlob(cursor
                    .getColumnIndex(COLUMN_RATION_BLOB)));
            objectInputStream = new ObjectInputStream(arrayInputStream);
            ration = (Ration) objectInputStream.readObject();
            Log.d(TAG, "callQueryRation: " + ration.getComposition().size());
        } catch (IOException e) {
            Log.d(TAG, "callQueryRation: IOException " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "callQueryRation: ClassNotFoundException " + e);
            e.printStackTrace();
        }
        cursor.close();

        Log.d(TAG, "callQueryRation: " + ration.getComposition().size());

        return ration;
    }

    private void callInsertRation(Ration ration) {
        Log.d(TAG, "callInsertRation: " + ration.getComposition().size());
        productDbHelper.setDb();
        try {
            productDbHelper.insertRation(ration);
        } catch (SQLiteConstraintException e) {
            productDbHelper.updateRation(ration);
        }
    }

    private void callUpdateRation(Ration ration) {
        productDbHelper.setDb();
    }

    private Dish callGetDish(String name) {
        Dish dish = null;

        productDbHelper.setDb();

        ByteArrayInputStream arrayInputStream;
        ObjectInputStream objectInputStream;

        Cursor cursor = productDbHelper.getDb().query(TABLE_DISHES,
                null,
                COLUMN_DISH_NAME + " =? ",
                new String[]{name},
                null,
                null,
                null);

        cursor.moveToFirst();

        try {
            arrayInputStream = new ByteArrayInputStream(cursor.getBlob(cursor
                    .getColumnIndex(COLUMN_DISH_BLOB)));
            objectInputStream = new ObjectInputStream(arrayInputStream);
            dish = (Dish) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cursor.close();

        return dish;
    }
}
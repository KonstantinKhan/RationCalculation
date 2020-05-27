package com.khan366kos.rationcalculation.presentation.ProductsBase;

import com.khan366kos.rationcalculation.getway.DataProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.khan366kos.rationcalculation.presentation.ProductsBase.ContractBaseProducts.*;

public class BaseProductsPresenter implements ProductsPresenter {

    private ProductsView productsView;
    private DataProvider dataProvider;

    BaseProductsPresenter(ProductsView productsView) {
        this.productsView = productsView;
        dataProvider = new DataProvider();
    }

    @Override
    public void onBindViewHolder() {
        dataProvider.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> productsView.showProducts(products));
    }

    @Override
    public void onProductDelete(String nameProduct) {
        dataProvider.deleteProduct(nameProduct)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onAddProduct(String productName, String productCalories, String productProteins,
                             String productFats, String productCarbohydrates) {
        dataProvider.addProduct(productName, productCalories, productProteins,
                productFats, productCarbohydrates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        dataProvider.getProduct(productName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> productsView.updateAfterInsert(product));
    }

    @Override
    public void onProductEdit(String productName) {
        dataProvider.getProduct(productName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> productsView.transferProduct(product));
    }

    @Override
    public void onUpdateProduct(String productName, double productCalories, double productProteins,
                                double productFats, double productCarbohydrates) {
        dataProvider.updateProduct(productName, productCalories, productProteins,
                productFats, productCarbohydrates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> productsView.updateProduct(product),
                        throwable -> productsView.showErrorDuplicate());
    }
}

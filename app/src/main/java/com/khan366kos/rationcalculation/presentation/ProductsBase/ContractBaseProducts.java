package com.khan366kos.rationcalculation.presentation.ProductsBase;

import com.khan366kos.Objects.Product;

import java.util.List;

public interface ContractBaseProducts {

    interface ProductsView {
        void showProducts(List<Product> product);

        void updateAfterInsert(Product product);

        void transferProduct(Product product);

        void updateProduct(Product product);

        void showErrorDuplicate();
    }

    interface ProductsPresenter {
        void onBindViewHolder();

        void onProductDelete(String nameProduct);

        void onAddProduct(String productName, String productCalories, String productProteins,
                          String productFats, String productCarbohydrates);

        void onProductEdit(String productName);

        void onUpdateProduct(String productName, double calories, double proteins,
                             double fats, double carbohydrates);

    }
}

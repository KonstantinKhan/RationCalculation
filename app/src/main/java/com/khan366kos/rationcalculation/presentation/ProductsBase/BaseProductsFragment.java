package com.khan366kos.rationcalculation.presentation.ProductsBase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Service.Toast.MyToast;
import com.khan366kos.rationcalculation.R;

import java.util.List;

import static com.khan366kos.rationcalculation.presentation.ProductsBase.ContractBaseProducts.*;

public class BaseProductsFragment extends Fragment implements ProductsView {

    private ProductsPresenter presenter;

    private RecyclerView recyclerView;
    private BaseProductsAdapter adapter;
    private DialogFragmentAddProduct df;
    private FragmentManager fm;

    public BaseProductsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);

        fm = getParentFragmentManager();

        df = new DialogFragmentAddProduct(
                new DialogFragmentAddProduct.DialogFragmentStatus() {
                    @Override
                    public void onDismissCustom() {
                        if (adapter.getSwipeRevealLayout() != null) {
                            adapter.getSwipeRevealLayout().close(false);
                        }
                        df.clearValues();
                    }

                    @Override
                    public void onAddProduct(String productName, String productCalories,
                                             String productProteins, String productFats,
                                             String productCarbohydrates) {
                        presenter.onAddProduct(productName, productCalories, productProteins,
                                productFats, productCarbohydrates);
                    }

                    @Override
                    public void onEditProduct() {
                        presenter.onProductEdit(adapter.getProductName());
                    }

                    @Override
                    public void onUpdateProduct(String productName, double productCalories,
                                                double productProteins, double productFats,
                                                double productCarbohydrates) {
                        presenter.onUpdateProduct(productName, productCalories,
                                productProteins, productFats, productCarbohydrates);
                    }
                });

        adapter = new BaseProductsAdapter(new BaseProductsAdapter.OnProductClickListener() {
            @Override
            public void onProductClick() {
            }

            @Override
            public void onProductDelete(String productName) {
                presenter.onProductDelete(productName);
            }

            @Override
            public void onProductEdit() {
                df.setCreate(false);
                df.show(fm, "dialog add product");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_products, container, false);
        recyclerView = view.findViewById(R.id.rv_products_in_database);
        return view;
    }

    @Override
    public void onResume() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);

        presenter = new BaseProductsPresenter(this);
        presenter.onBindViewHolder();

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_component) {
            df.setCreate(true);
            df.show(fm, "dialog add product");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProducts(List<Product> products) {
        adapter.setItems(products);
    }

    @Override
    public void updateAfterInsert(Product product) {
        adapter.updateAfterInsert(product);
        recyclerView.scrollToPosition(adapter.getItems().size() - 1);
    }

    @Override
    public void transferProduct(Product product) {
        df.setValue(product.getName(),
                String.valueOf(product.getCaloriesDefault()),
                String.valueOf(product.getProteinsDefault()),
                String.valueOf(product.getFatsDefault()),
                String.valueOf(product.getCarbohydratesDefault()));
    }

    @Override
    public void updateProduct(Product product) {
        adapter.updateProduct(product);
        df.dismiss();
    }

    @Override
    public void showErrorDuplicate() {
        MyToast.showToast(this.getContext(), getString(R.string.error_duplicate_product));
    }
}

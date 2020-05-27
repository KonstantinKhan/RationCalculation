package com.khan366kos.rationcalculation.presentation.ProductsBase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.khan366kos.Objects.Product;
import com.khan366kos.rationcalculation.R;

import java.util.ArrayList;
import java.util.List;

public class BaseProductsAdapter extends RecyclerView.Adapter<BaseProductsAdapter.EntryViewHolder> {

    private List<Product> items;

    private String productName;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private int positionUpdate;

    //Интерфейс для реализации действий с выбранным пунктом в Recyclerview.
    private OnProductClickListener onProductClickListener;

    private SwipeRevealLayout swipeRevealLayout;

    public SwipeRevealLayout getSwipeRevealLayout() {
        return swipeRevealLayout;
    }

    public BaseProductsAdapter(OnProductClickListener onProductClickListener) {
        this.onProductClickListener = onProductClickListener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_product, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EntryViewHolder holder, int position) {

        holder.bind(items.get(position));

        //Запоминаем состояние SwipeRevealLayout;
        viewBinderHelper.bind(holder.srl, holder.tvProductName.getText().toString() +
                holder.tvProductCalories.getText().toString() +
                holder.tvProductProteins.getText().toString() +
                holder.tvProductFats.getText().toString() +
                holder.tvProductCarbohydrates.getText().toString());

        //В случае, если высота item менялась, то выравниваем второй лайаут SRL по первому.
        holder.srl.post(() -> {
            int heightFirst;
            int heightSecond;
            heightFirst = holder.ll_first.getHeight();
            heightSecond = holder.ll_secondary.getHeight();
            if (heightSecond != heightFirst) {
                ViewGroup.LayoutParams layoutParams = holder.ll_secondary.getLayoutParams();
                layoutParams.height = heightFirst;
                holder.ll_secondary.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setItems(List<Product> products) {
        items = products;
        notifyDataSetChanged();
    }

    public List<Product> getItems() {
        return items;
    }

    public void updateAfterInsert(Product product) {
        items.add(product);
        notifyItemInserted(items.size());
    }

    public void updateProduct(Product product) {
        items.set(positionUpdate, product);
        notifyItemChanged(positionUpdate);
    }

    public void setPositionUpdate(int positionUpdate) {
        this.positionUpdate = positionUpdate;
    }

    public interface OnProductClickListener {
        void onProductClick();

        void onProductDelete(String productName);

        void onProductEdit();
    }

    public String getProductName() {
        return productName;
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName; // имя продукта.
        private TextView tvProductCalories; // калорийность продукта.
        private TextView tvProductProteins; // количество белка в продукте
        private TextView tvProductFats; // количество жиров в продукте.
        private TextView tvProductCarbohydrates; // количество углеводов в продукте.

        private LinearLayout ll_first; // LinearLayout с параметрами продукта.
        private LinearLayout ll_secondary;
        private SwipeRevealLayout srl;

        private ImageButton btnDeleteProduct;
        private ImageButton btnEditProduct;


        public EntryViewHolder(@NonNull final View itemView) {
            super(itemView);

            // Инициализация View.
            tvProductName = itemView.findViewById(R.id.tv_search_product_name);
            tvProductCalories = itemView.findViewById(R.id.tv_search_product_calories);
            tvProductProteins = itemView.findViewById(R.id.tv_search_product_proteins);
            tvProductFats = itemView.findViewById(R.id.tv_search_product_fats);
            tvProductCarbohydrates = itemView.findViewById(R.id.tv_search_product_carbohydrates);
            btnDeleteProduct = itemView.findViewById(R.id.btn_delete_product);
            btnEditProduct = itemView.findViewById(R.id.btn_edit_product);
            ll_first = itemView.findViewById(R.id.ll_first);
            ll_secondary = itemView.findViewById(R.id.delete_layout);
            srl = itemView.findViewById(R.id.srl);

            tvProductName.setOnClickListener(view -> {
                //onProductClickListener.onProductClick();
            });

            btnDeleteProduct.setOnClickListener(view -> {
                onProductClickListener.onProductDelete(tvProductName.getText().toString());
                items.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

            btnEditProduct.setOnClickListener(view -> {
                swipeRevealLayout = srl;
                productName = tvProductName.getText().toString();
                onProductClickListener.onProductEdit();
                setPositionUpdate(getAdapterPosition());
            });
        }

        // Метод для отрисовки значений продукта.
        private void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductCalories.setText(String.valueOf(product.getCaloriesDefault()).replace(".", ","));
            tvProductProteins.setText(String.valueOf(product.getProteinsDefault()).replace(".", ","));
            tvProductFats.setText(String.valueOf(product.getFatsDefault()).replace(".", ","));
            tvProductCarbohydrates.setText(String.valueOf(product.getCarbohydratesDefault()).replace(".", ","));
        }
    }
}

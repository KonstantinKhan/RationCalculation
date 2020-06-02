package com.khan366kos.rationcalculation.presentation.DishesBase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.R;
import com.khan366kos.rationcalculation.presentation.ProductsBase.BaseProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseDishesAdapter extends RecyclerView.Adapter<BaseDishesAdapter.BaseDishesViewHolder> {

    private List<Dish> items;

    public BaseDishesAdapter() {
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseDishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_product_component, parent, false);
        return new BaseDishesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseDishesViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Dish> dishes) {
        items = dishes;
        notifyDataSetChanged();
    }

    public class BaseDishesViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;

        public BaseDishesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.tv_search_product_name_component);
        }

        private void bind(Dish dish) {
            tvDishName.setText(dish.getName());
        }
    }
}

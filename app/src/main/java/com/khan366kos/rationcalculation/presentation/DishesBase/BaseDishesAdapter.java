package com.khan366kos.rationcalculation.presentation.DishesBase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
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
                .inflate(R.layout.fragment_item_dish, parent, false);
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
        private TextView tvDishCalories;
        private TextView tvDishProteins;
        private TextView tvDishFats;
        private TextView tvDishCarbohydrates;
        private SwipeRevealLayout srl;
        private LinearLayout ll_first;
        private LinearLayout ll_secondary;

        public BaseDishesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.tv_search_dish_name);
            tvDishCalories = itemView.findViewById(R.id.tv_search_dish_calories);
            tvDishProteins = itemView.findViewById(R.id.tv_search_dish_proteins);
            tvDishFats = itemView.findViewById(R.id.tv_search_dish_fats);
            tvDishCarbohydrates = itemView.findViewById(R.id.tv_search_dish_carbohydrates);
            srl = itemView.findViewById(R.id.srl_item_dish);
            ll_first = itemView.findViewById(R.id.ll_first_dish_base);
            ll_secondary = itemView.findViewById(R.id.dishes_delete_layout);
        }

        private void bind(Dish dish) {
            tvDishName.setText(dish.getName());
            tvDishCalories.setText(dish.getCaloriesCookedStr());
            tvDishProteins.setText(dish.getProteinsCookedStr());
            tvDishFats.setText(dish.getFatsCookedStr());
            tvDishCarbohydrates.setText(dish.getCarbohydratesCookedStr());
        }
    }
}

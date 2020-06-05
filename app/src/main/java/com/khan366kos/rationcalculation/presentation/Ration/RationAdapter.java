package com.khan366kos.rationcalculation.presentation.Ration;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.R;

import java.util.ArrayList;
import java.util.List;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

public class RationAdapter extends RecyclerView.Adapter<RationAdapter.RationViewHolder> {

    private List<Product> components;

    public RationAdapter() {
        components = new ArrayList<>();
    }

    @NonNull
    @Override
    public RationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_component, parent, false);
        return new RationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RationViewHolder holder, int position) {
        holder.product = components.get(position);
        holder.bind(components.get(position));
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    public void setComponents(List<Product> components) {
        this.components = components;
        notifyDataSetChanged();
    }

    public List<Product> getComponents() {
        return components;
    }

    public class RationViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCalories;
        private TextView tvProteins;
        private TextView tvFats;
        private TextView tvCarbohydrates;
        private EditText etWeight;
        private Product product;

        public RationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_search_name_component);
            tvCalories = itemView.findViewById(R.id.tv_search_calories_component);
            tvProteins = itemView.findViewById(R.id.tv_search_proteins_component);
            tvFats = itemView.findViewById(R.id.tv_search_fats_component);
            tvCarbohydrates = itemView.findViewById(R.id.tv_search_carbohydrates_component);
            etWeight = itemView.findViewById(R.id.et_search_weight_component);

            etWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() > 0) {
                        product.setWeight(Integer.parseInt(charSequence.toString()));
                        tvCalories.setText(String.valueOf(product.getCaloriesDefault()));
                        tvProteins.setText(String.valueOf(product.getProteinsDefault()));
                        tvFats.setText(String.valueOf(product.getFatsDefault()));
                        tvCarbohydrates.setText(String.valueOf(product.getCarbohydratesDefault()));
                    } else {
                        tvCalories.setText("0,0");
                        tvProteins.setText("0,0");
                        tvFats.setText("0,0");
                        tvCarbohydrates.setText("0,0");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        private void bind(Product product) {
            tvName.setText(product.getName());
            tvCalories.setText(String.valueOf(product.getCalories()));
            tvProteins.setText(String.valueOf(product.getProteins()));
            tvFats.setText(String.valueOf(product.getFats()));
            tvCarbohydrates.setText(String.valueOf(product.getCarbohydrates()));
        }
    }
}


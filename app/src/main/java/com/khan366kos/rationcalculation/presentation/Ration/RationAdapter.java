package com.khan366kos.rationcalculation.presentation.Ration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.R;

import java.util.ArrayList;
import java.util.List;

public class RationAdapter extends RecyclerView.Adapter<RationAdapter.RationViewHolder> {

    private List<Product> components;

    public RationAdapter() {
        components = new ArrayList<>();
    }

    @NonNull
    @Override
    public RationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_dish, parent, false);
        return new RationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RationViewHolder holder, int position) {
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

        public RationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_search_dish_name);
        }

        private void bind(Product product) {
            tvName.setText(product.getName());
        }
    }
}

package com.khan366kos.rationcalculation.presentation.Ration;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Model.Ration;
import com.khan366kos.rationcalculation.R;

import java.util.List;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.TAG;

public class RationAdapter extends RecyclerView.Adapter<RationAdapter.RationViewHolder> {

    private Ration ration;
    private RationAdapter.OnMove onMove;
    private int editPosition;

    public RationAdapter(RationAdapter.OnMove onMove, Ration ration) {
        this.ration = ration;
        this.onMove = onMove;
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
        holder.product = ration.getComposition().get(position);
        holder.bind(ration.getComposition().get(position));
        holder.etWeight.setText(String.valueOf(ration.getComposition().get(position).getWeight()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return ration.getComposition().size();
    }

    public void setComponents(List<Product> components) {
        ration.setComposition(components);
        notifyDataSetChanged();
    }

    public interface OnMove {
        void onSetWeightComponent();

        void onClickBtnDelete();

        void onClickBtnEdit();

        void onClickItemComponent();
    }

    public List<Product> getComponents() {
        return ration.getComposition();
    }

    public Ration getRation() {
        return ration;
    }

    public int getEditPosition() {
        return editPosition;
    }

    public class RationViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCalories;
        private TextView tvProteins;
        private TextView tvFats;
        private TextView tvCarbohydrates;
        private EditText etWeight;
        private Product product;
        private ImageButton btnDelete;
        private ImageButton btnEdit;
        private int position;
        private SwipeRevealLayout srl;

        public RationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_search_name_component);
            tvCalories = itemView.findViewById(R.id.tv_search_calories_component);
            tvProteins = itemView.findViewById(R.id.tv_search_proteins_component);
            tvFats = itemView.findViewById(R.id.tv_search_fats_component);
            tvCarbohydrates = itemView.findViewById(R.id.tv_search_carbohydrates_component);
            etWeight = itemView.findViewById(R.id.et_search_weight_component);
            btnDelete = itemView.findViewById(R.id.btn_delete_component);
            btnEdit = itemView.findViewById(R.id.btn_edit_component);
            srl = itemView.findViewById(R.id.srl_item_component);

            srl.setOnClickListener(view -> {
                onMove.onClickItemComponent();
            });

            etWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() > 0) {
                        product.setWeight(Integer.parseInt(charSequence.toString()));
                    } else {
                        product.setWeight(0);
                    }
                    tvCalories.setText(String.valueOf(product.getCalories()).replace(".", ","));
                    tvProteins.setText(String.valueOf(product.getProteins()).replace(".", ","));
                    tvFats.setText(String.valueOf(product.getFats()).replace(".", ","));
                    tvCarbohydrates.setText(String.valueOf(product.getCarbohydrates()).replace(".", ","));
                    onMove.onSetWeightComponent();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            btnDelete.setOnClickListener(view -> {
                ration.remove(product);
                onMove.onClickBtnDelete();
                notifyItemRemoved(position);
                srl.close(false);
            });
            
            btnEdit.setOnClickListener(view -> {
                editPosition = position;
                onMove.onClickBtnEdit();
            });
        }

        private void bind(Product product) {
            tvName.setText(product.getName());
            tvCalories.setText(String.valueOf(product.getCalories()).replace(".", ","));
            tvProteins.setText(String.valueOf(product.getProteins()).replace(".", ","));
            tvFats.setText(String.valueOf(product.getFats()).replace(".", ","));
            tvCarbohydrates.setText(String.valueOf(product.getCarbohydrates()).replace(".", ","));
        }
    }
}


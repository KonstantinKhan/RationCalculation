package com.khan366kos.rationcalculation.presentation.Dish;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.R;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.SuggestionProductViewHolder> {

    private Dish dish; // Создаваемое блюдо
    private OnMove onMove; // Интерфейс для передачи событий в DishFragment;
    private List<SuggestionProductViewHolder> list = new ArrayList<>();

    public DishAdapter(OnMove onMove) {
        this.onMove = onMove;
        dish = new Dish();
    }

    @NonNull
    @Override
    public SuggestionProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_component, parent, false);
        return new SuggestionProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionProductViewHolder holder, int position) {

        list.add(holder);

        holder.product = dish.getComposition().get(position);

        holder.tvProductNameComponent.setText(holder.product.getName());

        makeValuesProduct(holder.product, holder.tvProductCaloriesComponent,
                holder.tvProductProteinsComponent, holder.tvProductFatsComponent,
                holder.tvProductCarbohydratesComponent);
    }

    @Override
    public int getItemCount() {
        return dish.getProductCount();
    }

    public Dish getDish() {
        return dish;
    }

    public interface OnMove {
        void makeValues();

        void deleteProductComponent();

        void collapseMenuItemSvComponent();
    }

    private void makeValuesProduct(Product product, TextView productCalories,
                                   TextView productProteins, TextView productFats,
                                   TextView productCarbohydrates) {
        productCalories.setText(String.valueOf((int) product.getCalories()));
        productProteins.setText(String.valueOf(product.getProteins())
                .replace(".", ","));
        productFats.setText(String.valueOf(product.getFats())
                .replace(".", ","));
        productCarbohydrates.setText(String.valueOf(product.getCarbohydrates())
                .replace(".", ","));
    }

    // Метод для проверки заполнения полей для значений веса продуктов.
    public boolean checkFillWeightProducts() {

        boolean checkFill = false; // Флаг, сигнализирующий, есть ли незаполненные поляж

        // Цвет для подчеркивания незаполненных полей.
        ColorStateList colorNonFill = ColorStateList.valueOf(Color.RED);

        // Проверяем поле для значений веса продукта на заполненность.
        // При первой встрече незаполненного поля прерываем цикл, присваем флагу checkFill false.
        for (SuggestionProductViewHolder holder : list) {
            if (holder.etProductWeightComponent.getText().toString().length() == 0) {
                checkFill = true;
                break;
            }
        }

        // Если есть незаполненные поля для значений веса продукта, то изменяем цвет подчеркивания
        // на значение colorNonFill во всех незаполненных полях.
        if (checkFill) {
            for (SuggestionProductViewHolder holder : list) {
                if (holder.etProductWeightComponent.getText().toString().length() == 0) {
                    holder.etProductWeightComponent.setBackgroundTintList(colorNonFill);
                }
            }
        }

        return checkFill;
    }

    public class SuggestionProductViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductNameComponent;
        private TextView tvProductCaloriesComponent;
        private TextView tvProductProteinsComponent;
        private TextView tvProductFatsComponent;
        private TextView tvProductCarbohydratesComponent;
        private EditText etProductWeightComponent;

        private ImageButton btnDeleteProductComponent;
        private ImageButton btnEditProductComponent;

        private ColorStateList colorFill;

        private Product product;

        private SwipeRevealLayout srl;

        public SuggestionProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Инициализируем View.
            tvProductNameComponent = itemView.findViewById(R.id.tv_search_name_component);
            tvProductCaloriesComponent =
                    itemView.findViewById(R.id.tv_search_calories_component);
            tvProductProteinsComponent =
                    itemView.findViewById(R.id.tv_search_proteins_component);
            tvProductFatsComponent =
                    itemView.findViewById(R.id.tv_search_fats_component);
            tvProductCarbohydratesComponent =
                    itemView.findViewById(R.id.tv_search_carbohydrates_component);
            etProductWeightComponent =
                    itemView.findViewById(R.id.et_search_weight_product_component);
            btnDeleteProductComponent =
                    itemView.findViewById(R.id.btn_delete_product_component);
            srl = itemView.findViewById(R.id.srl_item_component);

            colorFill = etProductWeightComponent.getBackgroundTintList();

            etProductWeightComponent.setOnFocusChangeListener((view, b) -> {
                if (b) {
                    onMove.collapseMenuItemSvComponent();
                }
            });

            etProductWeightComponent.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE)
                    etProductWeightComponent.clearFocus();
                return false;
            });

            // При внесении данных о весе продукта пересчитываем его параметры, а также параметры блюда.
            etProductWeightComponent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence.toString().length() > 0) {
                        // Присваиваем значение веса продукту.
                        product.setWeight(Integer.parseInt(charSequence.toString()));

                        // Цвет для подчеркивания незаполненных полей.
                        ColorStateList colorNonFill = ColorStateList.valueOf(Color.RED);
                        if (etProductWeightComponent.getBackgroundTintList() == colorNonFill) {

                            int[][] states = new int[][]{
                                    new int[]{android.R.attr.state_focused}, // focused
                                    new int[]{-android.R.attr.state_focused}, // unfocused
                            };

                            int[] colors = new int[]{
                                    Color.parseColor("#8FB399"),
                                    Color.parseColor("#616161")
                            };

                            ColorStateList myList = new ColorStateList(states, colors);

                            etProductWeightComponent.setBackgroundTintList(myList);
                        }

                    } else {
                        product.setWeight(0);
                    }

                    // Указываем значения продукта во View с учетом внесенного веса.
                    makeValuesProduct(product, tvProductCaloriesComponent,
                            tvProductProteinsComponent, tvProductFatsComponent,
                            tvProductCarbohydratesComponent);

                    dish.getComposition().set(getAdapterPosition(), product);
                    dish.setNutrients();

                    onMove.makeValues();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // Удаление продукта из состава блюда
            btnDeleteProductComponent.setOnClickListener(view -> {
                etProductWeightComponent.setText("");
                srl.close(false);
                dish.getComposition().remove(product);
                dish.setNutrients();
                notifyItemRemoved(getAdapterPosition());
                onMove.deleteProductComponent();
            });
        }
    }
}

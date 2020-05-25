package com.khan366kos.rationcalculation.Adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.Objects.Dish;
import com.khan366kos.Objects.Product;
import com.khan366kos.rationcalculation.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestionComponentAdapter extends RecyclerView.Adapter<SuggestionComponentAdapter.SuggestionProductViewHolder> {

    private Dish dish; // Создаваемое блюдо
    private OnSetWeightListener onSetWeightListener;
    private List<SuggestionProductViewHolder> list = new ArrayList<>();

    public SuggestionComponentAdapter(OnSetWeightListener onSetWeightListener) {
        this.onSetWeightListener = onSetWeightListener;
        dish = new Dish();
    }

    @NonNull
    @Override
    public SuggestionProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Создаем view для отображения данных о продукте, входящем в блюдо.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_product_component, parent, false);

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
        return dish.getComposition().size();
    }

    public Dish getDish() {
        return dish;
    }

    public interface OnSetWeightListener {
        void makeValues();

        void deleteProductComponent();

        void collapseMenuItemSvComponent();

        void clearFocusEt();
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
        // При первом встрече незаполненного поля прерываем цикл, присваем флагу checkFill false.
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

        public SuggestionProductViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setIsRecyclable(false);

            // Инициализируем View.
            tvProductNameComponent = itemView.findViewById(R.id.tv_search_product_name_component);
            tvProductCaloriesComponent =
                    itemView.findViewById(R.id.tv_search_product_calories_component);
            tvProductProteinsComponent =
                    itemView.findViewById(R.id.tv_search_product_proteins_component);
            tvProductFatsComponent =
                    itemView.findViewById(R.id.tv_search_product_fats_component);
            tvProductCarbohydratesComponent =
                    itemView.findViewById(R.id.tv_search_product_carbohydrates_component);
            etProductWeightComponent =
                    itemView.findViewById(R.id.et_search_weight_product_component);
            btnDeleteProductComponent =
                    itemView.findViewById(R.id.btn_delete_product_component);

            colorFill = etProductWeightComponent.getBackgroundTintList();

            etProductWeightComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        onSetWeightListener.collapseMenuItemSvComponent();
                    }
                }
            });

            etProductWeightComponent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    etProductWeightComponent.clearFocus();
                    onSetWeightListener.clearFocusEt();
                    return false;
                }
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

                    onSetWeightListener.makeValues();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // Удаление продукта из состава блюда
            btnDeleteProductComponent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dish.getComposition().remove(product);
                    dish.setNutrients();
                    notifyItemRemoved(getAdapterPosition());
                    onSetWeightListener.deleteProductComponent();
                }
            });
        }
    }
}

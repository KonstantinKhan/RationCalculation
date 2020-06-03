package com.khan366kos.rationcalculation.presentation.Dish;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.MainActivity;
import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.MyToast;
import com.khan366kos.rationcalculation.R;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterFactory;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterTypes;

import java.util.List;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.*;

public class DishFragment extends Fragment implements ContractDishFragment.DishView {

    private RecyclerView recyclerView;
    private Menu menu;
    private SearchView svComponent;

    private TextView tvDishCaloriesRaw;
    private TextView tvDishProteinsRaw;
    private TextView tvDishFatsRaw;
    private TextView tvDishCarbohydratesRaw;

    private EditText etDishWeightCooked;
    private TextView tvDishCaloriesCooked;
    private TextView tvDishProteinsCooked;
    private TextView tvDishFatsCooked;
    private TextView tvDishCarbohydratesCooked;

    private BottomNavigationView bnv;

    private MenuItem menuItemSvComponent;

    private DishPresenter presenter;
    private DishAdapter dishAdapter;

    private MatrixCursor cursor;
    private SimpleCursorAdapter simpleCursorAdapter;

    private EditText etDishName;
    private TextView tvHeading;

    private InputMethodManager imm;

    private CursorAdapterFactory cursorAdapterFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish, container, false);
        init(view);
        tvHeading.setText(getString(R.string.dish_name));
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true); // Позволяем фрагменту работать с меню.
        cursorAdapterFactory = new CursorAdapterFactory();

        simpleCursorAdapter = cursorAdapterFactory.getCursorAdapter(getContext(),
                CursorAdapterTypes.PRODUCTS);

        presenter = new DishPresenter(this); // Получаем экземпляр DishPresenter.

        dishAdapter = new DishAdapter(new DishAdapter.OnMove() {

            @Override
            public void makeValues() {

                setValuesRaw();
                setValuesCooked();

                // Обработчик событий при введении данных в EditText для хранения данных о весе
                // готового продукта.
                etDishWeightCooked.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence,
                                                  int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        setValuesCooked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }

            @Override
            public void deleteProductComponent() {
                if (!etDishWeightCooked.getText().toString().equals("-") &&
                        dishAdapter.getDish().getWeight() != 0) {
                    setValuesRaw();
                    setValuesCooked();
                }
            }

            @Override
            public void collapseMenuItemSvComponent() {
                // Если MenuItem, было развернуто в виде SearchView, то сворачиваем его.
                if (menuItemSvComponent.isActionViewExpanded()) {
                    menuItemSvComponent.collapseActionView();
                }
            }
        });
    }

    @Override
    public void onResume() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dishAdapter);

        etDishWeightCooked.setOnFocusChangeListener((view, b) -> {

            // Выполняем действия при наведении фокуса на EditText,
            // отвечающего за значение веса готового блюда.
            if (b) {

                // Заменяем значение "-" на "".
                if (((EditText) view).getText().toString().equals("-")) {
                    ((EditText) view).setText("");
                }

                // Закрываем SearchView.
                if (menuItemSvComponent != null) {
                    if (menuItemSvComponent.isActionViewExpanded()) {
                        menuItemSvComponent.collapseActionView();
                    }
                }
            }
        });

        bnv.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.add_product_in_dish:
                    menu.findItem(R.id.mi_sv_component).expandActionView();
                    return true;
                case R.id.clean_dish:
                    dishAdapter.getDish().getComposition().clear();
                    dishAdapter.getDish().setNutrients();
                    etDishWeightCooked.setText("-");
                    dishAdapter.notifyDataSetChanged();
                    setValuesCooked();
                    setValuesRaw();
                    return true;
                case R.id.save_dish:

                    // Проверяем, указано ли название продукта.
                    // Если да, то выводим сообщение и активируем поле для ввода названия блюда.
                    if (dishAdapter.getDish().getName() == null) {
                        Toast.makeText(getActivity(), "Укажите название блюда",
                                Toast.LENGTH_SHORT).show();
                        etDishName.setHint("Введите название продукта");
                        editDishName(true);
                        return true;
                    }

                    // Проверяем, есть ли в блюде добавленные продукты.
                    // Если нет, то выводим сообщение и активируем поле для выбора продукта.
                    else if (dishAdapter.getDish().getComposition().size() == 0) {
                        Toast.makeText(getActivity(), "Выберите продукт",
                                Toast.LENGTH_SHORT).show();
                        menu.findItem(R.id.mi_sv_component).expandActionView();
                        return true;
                    }

                    // Проверяем, у всех ли продуктов указан вес.
                    else if (dishAdapter.checkFillWeightProducts()) {
                        Toast.makeText(getActivity(), "Укажите вес продуктов",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    // Проверяем, заполнено ли поле с весом готового продукта.
                    else if (etDishWeightCooked.getText().toString().length() == 0 ||
                            etDishWeightCooked.getText().toString().equals("-")) {
                        Toast.makeText(getActivity(), "Укажите вес готового блюда",
                                Toast.LENGTH_SHORT).show();
                        etDishWeightCooked.requestFocus();
                        etDishWeightCooked.selectAll();
                        return true;
                    } else {
                        presenter.onSaveDish();
                        return true;
                    }
            }
            return false;
        });

        etDishName.setOnEditorActionListener((textView, i, keyEvent) -> {

            // Присваиваем значение из EditText TextView, если EditText не пустой.
            if (etDishName.getText().toString().length() > 0) {
                tvHeading.setText(etDishName.getText().toString());
                dishAdapter.getDish().setName(tvDishCaloriesCooked.getText().toString());
            }
            return true;
        });

        etDishName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // Запрещаем первым символом вводить пробел.
                if (charSequence.length() > 0) {
                    if (charSequence.charAt(0) == ' ') {
                        etDishName.setText(charSequence.subSequence(1, charSequence.length()));
                    }
                }
                dishAdapter.getDish().setName(etDishName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etDishName.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (dishAdapter.getDish().getName() == null) {
                    tvHeading.setText(R.string.dish_name);
                } else if (dishAdapter.getDish().getName().equals("")) {
                    tvHeading.setText(R.string.dish_name);
                } else {
                    dishAdapter.getDish().setName(etDishName.getText().toString());
                    tvHeading.setText(dishAdapter.getDish().getName());
                }
                editDishName(false);
            }
        });

        etDishName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                dishAdapter.getDish().setName(etDishName.getText().toString());
                tvHeading.setText(dishAdapter.getDish().getName());
                etDishName.clearFocus();
            }
            return false;
        });

        tvHeading.setOnClickListener(view -> {
            if (MainActivity.newFragmentId == R.id.dish) {
                if (tvHeading.getText().toString().equals(getString(R.string.dish_name))) {
                    etDishName.setHint("Введите название продукта");
                } else
                    etDishName.setText(dishAdapter.getDish().getName());
                editDishName(true);
            }
        });
        super.onResume();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        this.menu = menu;

        menuItemSvComponent = menu.findItem(R.id.mi_sv_component);
        svComponent = (SearchView) menuItemSvComponent.getActionView();

        // Устанавливаем вспомогательную надпись в поле поиска.
        svComponent.setQueryHint("Выберите продукт");

        svComponent.setSuggestionsAdapter(simpleCursorAdapter);

        // Устанавливаем максимальную ширину поля отображения вариантов поиска.
        svComponent.setMaxWidth(Integer.MAX_VALUE);

        svComponent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                svComponent.post(() -> presenter.onQueryTextChange(s,
                        dishAdapter.getDish().getComposition()));
                return false;
            }
        });

        svComponent.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                int i = dishAdapter.getItemCount(); // Количество продуктов в блюде.

                // Создаем курсор в выпадающем меню.
                Cursor cursor = svComponent.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);

                // Новый продукт с данными, выбранными из поиска.
                Product product = new Product(cursor.getString(1),
                        Double.parseDouble(cursor.getString(2).replace(",", ".")),
                        Double.parseDouble(cursor.getString(3).replace(",", ".")),
                        Double.parseDouble(cursor.getString(4).replace(",", ".")),
                        Double.parseDouble(cursor.getString(5).replace(",", ".")));

                // Добавляем продукт в блюдо
                dishAdapter.getDish().getComposition().add(product);

                // Прокручивает RecyclerView до добавленного блюда.
                recyclerView.smoothScrollToPosition(dishAdapter
                        .getDish().getComposition().size() - 1);

                // Обнуляем строку запроса.
                svComponent.setQuery("", false);
                dishAdapter.notifyItemInserted(i);

                return true;
            }
        });

        // Закрываем SearchView, если у него пропадает фокус.
        svComponent.setOnQueryTextFocusChangeListener((view, b) ->
                svComponent.post(() -> {
                    if (!b) {
                        if (menuItemSvComponent.isActionViewExpanded()) {
                            menuItemSvComponent.collapseActionView();
                        }
                    }
                }));
    }

    // Метод для инициализации View.
    private void init(View view) {

        // Блок View, отражающих параметры сырого блюда.
        tvDishCaloriesRaw = view.findViewById(R.id.tv_dish_calories_raw);
        tvDishProteinsRaw = view.findViewById(R.id.tv_dish_proteins_raw);
        tvDishFatsRaw = view.findViewById(R.id.tv_dish_fats_raw);
        tvDishCarbohydratesRaw = view.findViewById(R.id.tv_dish_carbohydrates_raw);

        // Блок View, отражающих параметры готового блюда.
        etDishWeightCooked = view.findViewById(R.id.et_dish_weight_cooked);
        tvDishCaloriesCooked = view.findViewById(R.id.tv_dish_calories_cooked);
        tvDishProteinsCooked = view.findViewById(R.id.tv_dish_proteins_cooked);
        tvDishFatsCooked = view.findViewById(R.id.tv_dish_fats_cooked);
        tvDishCarbohydratesCooked = view.findViewById(R.id.tv_dish_carbohydrates_cooked);

        bnv = view.findViewById(R.id.bnv_dish);

        recyclerView = view.findViewById(R.id.rv_products_in_database_component);
        recyclerView.setItemAnimator(null); // убираем анимацию элементов при изменениях.

        etDishName = getActivity().findViewById(R.id.et_dish_name);
        tvHeading = getActivity().findViewById(R.id.tv_heading);
    }

    // Устанавливаем зачения параметров блюда на 100 г. в готовом виде.
    private void setValuesCooked() {

        if (dishAdapter.getDish().getWeight() == 0) {
            tvDishCaloriesCooked.setText("-");
            tvDishProteinsCooked.setText("-");
            tvDishFatsCooked.setText("-");
            tvDishCarbohydratesCooked.setText("-");
        } else {
            if (!etDishWeightCooked.getText().toString().equals("-") &&
                    etDishWeightCooked.getText().toString().length() != 0) {
                dishAdapter.getDish().setWeightCooked(Integer.parseInt(
                        etDishWeightCooked.getText().toString()));
                dishAdapter.getDish().setNutrientsCookedStr();
                dishAdapter.getDish().setNutrientsCooked();
                tvDishCaloriesCooked.setText(dishAdapter.getDish().getCaloriesCookedStr());
                tvDishProteinsCooked.setText(dishAdapter.getDish().getProteinsCookedStr());
                tvDishFatsCooked.setText(dishAdapter.getDish().getFatsCookedStr());
                tvDishCarbohydratesCooked.setText(dishAdapter.getDish().getCarbohydratesCookedStr());
            } else {
                tvDishCaloriesCooked.setText("-");
                tvDishProteinsCooked.setText("-");
                tvDishFatsCooked.setText("-");
                tvDishCarbohydratesCooked.setText("-");
            }
        }
    }

    // Устанавливаем зачения параметров блюда на 100 г. в сыром виде.
    private void setValuesRaw() {
        if (dishAdapter.getDish().getWeight() != 0) {
            tvDishCaloriesRaw.setText(dishAdapter.getDish().getCaloriesDefaultStr());
            tvDishProteinsRaw.setText(dishAdapter.getDish().getProteinsDefaultStr());
            tvDishFatsRaw.setText(dishAdapter.getDish().getFatsDefaultStr());
            tvDishCarbohydratesRaw.setText(dishAdapter.getDish().getCarbohydratesDefaultStr());
        } else {
            tvDishCaloriesRaw.setText("-");
            tvDishProteinsRaw.setText("-");
            tvDishFatsRaw.setText("-");
            tvDishCarbohydratesRaw.setText("-");
        }
    }

    @Override
    public void notifyCursorAdapter(List<Product> products) {

        setCursor();
        String[] temp = new String[6];

        // Заполняем курсор данными из полученного списка продуктов.
        for (int i = 0; i < products.size(); i++) {
            temp[0] = String.valueOf(i);
            temp[1] = products.get(i).getName();
            temp[2] = String.valueOf(products.get(i).getCaloriesDefault()).replace(".", ",");
            temp[3] = String.valueOf(products.get(i).getProteinsDefault()).replace(".", ",");
            temp[4] = String.valueOf(products.get(i).getFatsDefault()).replace(".", ",");
            temp[5] = String.valueOf(products.get(i).getCarbohydratesDefault()).replace(".", ",");
            cursor.addRow(temp);
        }

        // Обновляем курсор.
        simpleCursorAdapter.changeCursor(cursor);
    }

    @Override
    public Dish saveDish() {
        return dishAdapter.getDish();
    }

    @Override
    public void showErrorDuplicate() {
        MyToast.showToast(this.getContext(), getString(R.string.error_duplicate_dish));
    }

    // Метод для получения курсора.
    private void setCursor() {
        String[] columnName = {_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_CALORIES,
                COLUMN_PRODUCT_PROTEINS, COLUMN_PRODUCT_FATS, COLUMN_PRODUCT_CARBOHYDRATES};
        cursor = new MatrixCursor(columnName);
    }

    // Метод для регулирования отображения полей, отвечающих за ввод наименования блюда
    // и его отображение в зависимости от значения переданного параметра.
    public void editDishName(boolean edit) {
        if (edit) {
            tvHeading.setVisibility(View.GONE);
            etDishName.setVisibility(View.VISIBLE);
            etDishName.requestFocus();
            imm.showSoftInput(etDishName, 0);
        } else {
            tvHeading.setVisibility(View.VISIBLE);
            etDishName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        etDishName.setText("");
    }
}

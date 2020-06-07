package com.khan366kos.rationcalculation.presentation.ProductsBase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.khan366kos.rationcalculation.Service.ViewExt.CheckContentET;
import com.khan366kos.rationcalculation.Service.Toast.MyToast;
import com.khan366kos.rationcalculation.R;

public class DialogFragmentAddProduct extends DialogFragment {

    private EditText etProductName; // поле ввода наименования продукта.
    private EditText etProductCalories; // поле ввода калорийности продукта.
    private EditText etProductProteins; // поле ввода количества белка в продукте.
    private EditText etProductFats; // поле ввода количества жиров в продукте.
    private EditText etProductCarbohydrates; // поле ввода количества углеводов в продукте.
    private Button btnInsertProduct; // кнопка для добавления продукта в базу.

    private boolean isCreate;
    private String productName;
    private double productCalories;
    private double productProteins;
    private double productFats;
    private double productCarbohydrate;

    // Интерфейс для реализации метода onDismissCustom при закрытии диалогового фрагмента
    // после добавления продукта в базу.
    private DialogFragmentStatus dialogFragmentStatus;

    public DialogFragmentAddProduct(DialogFragmentStatus dialogFragmentStatus) {
        //Log.d(TAG, "Создается экземляр df");
        this.dialogFragmentStatus = dialogFragmentStatus;
    }

    public interface DialogFragmentStatus {
        void onDismissCustom();

        void onAddProduct(String productName, String productCalories, String productProteins,
                          String productFats, String productCarbohydrates);

        void onEditProduct();

        void onUpdateProduct(String productName, double calories,
                             double proteins, double fats, double carbohydrates);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, null);
        init(view);

        etProductProteins.addTextChangedListener(new CheckContentET(etProductProteins));
        etProductFats.addTextChangedListener(new CheckContentET(etProductFats));
        etProductCarbohydrates.addTextChangedListener(new CheckContentET(etProductCarbohydrates));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Кусок кода, чтобы настроить ширину DialogFragment.
        Window window = getDialog().getWindow();
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getResources().getDimensionPixelOffset(R.dimen.fragment_dialog_width);
        window.setAttributes(params);

        if (!isCreate) {
            dialogFragmentStatus.onEditProduct();

            // Реализация обновления записи в базе данных на основе внесенных во View изменений.
            btnInsertProduct.setOnClickListener(view -> {

                if (checkExistEditText()) {
                    if (productName.equals(etProductName.getText().toString()) &&
                            productCalories == Double.parseDouble(etProductCalories.getText()
                                    .toString().replace(",", ".")) &&
                            productProteins == Double.parseDouble(etProductProteins.getText()
                                    .toString().replace(",", ".")) &&
                            productFats == Double.parseDouble(etProductFats.getText()
                                    .toString().replace(",", ".")) &&
                            productCarbohydrate == Double.parseDouble(etProductCarbohydrates.getText()
                                    .toString().replace(",", "."))) {
                        dismiss();
                    } else {
                        dialogFragmentStatus.onUpdateProduct(etProductName.getText().toString(),
                                commaToDot(etProductCalories),
                                commaToDot(etProductProteins),
                                commaToDot(etProductFats),
                                commaToDot(etProductCarbohydrates));
                    }
                    // Выводим сообщение, если одно или несколько полей продукта не заполнены.
                } else {
                    MyToast.showToast(getContext(),
                            getResources().getString(R.string.empty_fields));
                }
            });
        } else {
            btnInsertProduct.setOnClickListener(view -> {
                if (checkExistEditText()) {
                    dialogFragmentStatus.onAddProduct(etProductName.getText().toString(),
                            etProductCalories.getText().toString(),
                            etProductProteins.getText().toString(),
                            etProductFats.getText().toString(),
                            etProductCarbohydrates.getText().toString());
                    dismiss();

                } else {
                    MyToast.showToast(getContext(),
                            getResources().getString(R.string.empty_fields));
                }
            });
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        dialogFragmentStatus.onDismissCustom();
        super.onDismiss(dialog);
    }

    // Метод возвращает значение из EditText, пригодное для сохранения в базу данных.
    private double commaToDot(EditText editText) {
        return Double.parseDouble(editText.getText().toString().replace(",", "."));
    }

    private void init(View view) {
        etProductName = view.findViewById(R.id.et_product_name);
        etProductCalories = view.findViewById(R.id.et_product_calories);
        etProductProteins = view.findViewById(R.id.et_product_proteins);
        etProductFats = view.findViewById(R.id.et_product_fats);
        etProductCarbohydrates = view.findViewById(R.id.et_product_carbohydrates);
        btnInsertProduct = view.findViewById(R.id.btn_insert_product);
    }

    // Метод для очистки значений полей Views
    public void clearValues() {
        etProductName.setText("");
        etProductCalories.setText("");
        etProductProteins.setText("");
        etProductFats.setText("");
        etProductCarbohydrates.setText(" ");
    }

    // Метод для проверки EditText с параметрами продукта на заполнение.
    private boolean checkExistEditText() {
        if (etProductName.getText().toString().equals("") ||
                etProductCalories.getText().toString().equals("") ||
                etProductProteins.getText().toString().equals("") ||
                etProductFats.getText().toString().equals("") ||
                etProductCarbohydrates.getText().toString().equals("")) {
            return false;
        } else return true;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public void setValue(String productName, String productCalories, String productProteins,
                         String productFats, String productCarbohydrates) {

        // Меняем название кнопки на соответствующее контексту операции.
        btnInsertProduct.setText("Внести изменения в базу");
        etProductName.setText(productName);
        etProductCalories.setText(productCalories.replace(".", ","));
        etProductProteins.setText(productProteins.replace(".", ","));
        etProductFats.setText(productFats.replace(".", ","));
        etProductCarbohydrates.setText(productCarbohydrates.replace(".", ","));
        this.productName = productName;
        this.productCalories = Double.parseDouble(productCalories.replace(",", "."));
        this.productProteins = Double.parseDouble(productProteins.replace(",", "."));
        this.productFats = Double.parseDouble(productFats.replace(",", "."));
        this.productCarbohydrate = Double.parseDouble(productCarbohydrates.replace(",", "."));
    }
}

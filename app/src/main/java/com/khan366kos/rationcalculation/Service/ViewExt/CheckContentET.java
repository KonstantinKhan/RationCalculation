package com.khan366kos.rationcalculation.Service.ViewExt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Класс для проверки ввода данных в EditText
 * Выполняется проверка на точку.
 */
public class CheckContentET implements TextWatcher {

    private EditText editText;

    public CheckContentET(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        // Получаем последнмй введенный символ
        if (charSequence.length() > 0) {
            String s = String.valueOf(charSequence.charAt(charSequence.length() - 1));
            // Если введена точка, то заменяем ее на запятую.
            if (s.equals(".")) {
                String strAfter = charSequence.toString().replace(".", ",");
                editText.setText(strAfter);
                editText.setSelection(charSequence.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}

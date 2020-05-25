package com.khan366kos.rationcalculation.CustomViews;

import android.content.Context;
import android.view.KeyEvent;

import androidx.appcompat.widget.SearchView;


public class SearchViewEx extends SearchView {

    public SearchViewEx(Context context) {
        super(context);
    }

    // Переопределяем метод для снятия фокуса с SearchView после скрытия программной клавиатуры.
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}

package com.khan366kos.rationcalculation.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

public class EditTextEx extends androidx.appcompat.widget.AppCompatEditText {

    private InputMethodManager imm;

    public EditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    // Переопределяем метод для снятия фокуса с EditText после скрытия программной клавиатуры.
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    // Метод для имитации нажатия на EditText с вызовом клавиатуры.
    public void setFocusAndToggleSoftKey() {
        this.requestFocus();
        imm.toggleSoftInput(0, 0);
    }

    // Метод для скрытия клавиатуры и снятия фокуса с EditText.
    public void hideSoftKey() {
        this.clearFocus();
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }
}

package com.khan366kos.rationcalculation.Service.Animation;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.khan366kos.rationcalculation.R;

public class MyAnimation {

    private Context mContext;

    public MyAnimation(Context context) {
        this.mContext = context;
    }

    public Animation AnimBlink () {
        return AnimationUtils.loadAnimation(mContext, R.anim.blink);
    }

    public Animation AnimVanishingLeft() {
        return AnimationUtils.loadAnimation(mContext, R.anim.vanishing_left);
    }

    public Animation AnimEmergenceLeft() {
        return AnimationUtils.loadAnimation(mContext, R.anim.emergence_left);
    }

    public Animation AnimVanishingRight() {
        return AnimationUtils.loadAnimation(mContext, R.anim.emergence_right);
    }

    public Animation AnimEmergenceRight() {
        return AnimationUtils.loadAnimation(mContext, R.anim.emergence_right);
    }
}

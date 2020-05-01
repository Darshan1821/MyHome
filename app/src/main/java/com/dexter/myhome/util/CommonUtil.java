package com.dexter.myhome.util;

import android.view.Window;
import android.view.WindowManager;

public class CommonUtil {

    public static void setWindowNotClickable(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void setWindowClickable(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}

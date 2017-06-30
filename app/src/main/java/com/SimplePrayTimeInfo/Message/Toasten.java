package com.SimplePrayTimeInfo.Message;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by HassenKassim on 03.01.15.
 */
public class Toasten {
    public static void toasten(Context ctx, String txt) {
        Toast toast = Toast.makeText(ctx, txt, Toast.LENGTH_SHORT);
        toast.show();
    }
}

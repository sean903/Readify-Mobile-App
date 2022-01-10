package com.mobileapllication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MyView extends View {


    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}




//icon drawing - not using
//context.moveTo(379, 234 - 145);
//        context.bezierCurveTo(379 + (0.5522847498307936 * 148), 234 - 145,  379 + 148, 234 - (0.5522847498307936 * 145), 379 + 148, 234);
//        context.bezierCurveTo(379 + 148, 234 + (0.5522847498307936 * 145), 379 + (0.5522847498307936 * 148), 234 + 145, 379, 234 + 145);
//        context.bezierCurveTo(379 - (0.5522847498307936 * 148), 234 + 145, 379 - 148, 234 + (0.5522847498307936 * 145), 379 - 148, 234);
//        context.bezierCurveTo(379 - 148, 234 - (0.5522847498307936 * 145), 379 - (0.5522847498307936 * 148), 234 - 145, 379, 234 - 145);
//        context.rect(277, 175, 97, 107);
//        context.rect(374, 282, 12, -107);
//        context.rect(386, 175, 101, 107);
//        context.moveTo(302,195);
//        context.lineTo(348,195);
//        context.moveTo(303,220);
//        context.lineTo(346,219);
//        context.moveTo(302,244);
//        context.lineTo(346,244);
//        context.moveTo(417,194);
//        context.lineTo(463,194);
//        context.moveTo(415,217);
//        context.lineTo(461,217);
//        context.moveTo(414,242);
//        context.lineTo(463,243);
//        context.moveTo(302,263);
//        context.lineTo(346,262);
//        context.moveTo(415,261);
//        context.lineTo(465,261);
//        context.moveTo(399,283);
//        context.lineTo(399,283);


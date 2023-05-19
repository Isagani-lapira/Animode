package com.example.animode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;

public class BottomRoundedCardView extends CardView {
    private float radius = 0f;

    public BottomRoundedCardView(Context context) {
        super(context);
        init();
    }

    public BottomRoundedCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomRoundedCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        float density = getContext().getResources().getDisplayMetrics().density;
        radius = 25 * density; // Set the radius as per your requirement
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = canvas.save();
        final Path path = new Path();
        final RectF rect = new RectF(0, 0, getWidth(), getHeight());
        float[] radii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        path.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(count);
    }
}
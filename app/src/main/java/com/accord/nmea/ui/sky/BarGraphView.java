package com.accord.nmea.ui.sky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BarGraphView extends View {

        /* renamed from: b */
        public final RectF f1637b = new RectF();

        /* renamed from: c */
        public final Paint f1638c = new Paint(1);

        /* renamed from: d */
        public int f1639d = -256;

        /* renamed from: e */
        public int f1640e = -7829368;

        /* renamed from: f */
        public double f1641f = 100.0d;

        /* renamed from: g */
        public double f1642g = 50.0d;

        public BarGraphView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public final void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        public final void onDetachedFromWindow() {
            super.onDetachedFromWindow();
        }

        public final void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float height = ((float) getHeight()) * 0.25f;
            RectF rectF = this.f1637b;
            rectF.top = 0.0f;
            rectF.left = 0.0f;
            rectF.right = (float) getWidth();
            rectF.bottom = (float) getHeight();
            Paint paint = this.f1638c;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.f1640e);
            canvas.drawRoundRect(rectF, height, height, paint);
            double d = this.f1642g;
            if (d > 0.0d) {
                double min = Math.min(1.0d, d / this.f1641f);
                float f = rectF.left;
                double width = (double) getWidth();
                Double.isNaN(width);
                rectF.right = f + ((float) (width * min));
                paint.setColor(this.f1639d);
                canvas.drawRoundRect(rectF, height, height, paint);
            }
        }

        public final void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int mode = View.MeasureSpec.getMode(i);
            int i3 = 1;
            if (mode == Integer.MIN_VALUE) {
                size = Math.min(1, size);
            } else if (mode != 1073741824) {
                size = 1;
            }
            int size2 = View.MeasureSpec.getSize(i2);
            int mode2 = View.MeasureSpec.getMode(i2);
            if (mode2 == Integer.MIN_VALUE) {
                i3 = Math.min(1, size2);
            } else if (mode2 == 1073741824) {
                i3 = size2;
            }
            setMeasuredDimension(size, i3);
        }

        public void setBackColour(int i) {
            this.f1640e = i;
        }

        public void setForeColour(int i) {
            this.f1639d = i;
        }

        public void setMaxValue(double d) {
            this.f1641f = d;
        }

        public void setValue(double d) {
            this.f1642g = d;
        }
    }



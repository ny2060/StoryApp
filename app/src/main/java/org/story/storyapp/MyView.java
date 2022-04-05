package org.story.storyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class MyView extends View {

    private final Paint mPaintDstIn = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

    private final Paint mPaintColor = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint mPaintEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ArrayList<DrawOp> mDrawOps = new ArrayList<>();
    private final DrawOp mCurrentOp = new DrawOp();
    private final ArrayList<DrawOp> mUndoneOps = new ArrayList<>();
    private final ArrayList<DrawOp> mPencil = new ArrayList<>();
    Bitmap mLayerBitmap;
    Bitmap innerRe;
    Bitmap outerRe;
    Bitmap layerRe;
    private final Canvas mCanvas = new Canvas();
    private Bitmap mInnerShape;
    private Bitmap mOuterShape;
    private Boolean isResize = false;

    public MyView(Context context) {
        this(context, null, 0);


    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaintDstIn.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mPaintColor.setStyle(Paint.Style.STROKE);
        mPaintColor.setStrokeJoin(Paint.Join.ROUND);
        mPaintColor.setStrokeCap(Paint.Cap.ROUND);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaintEraser.set(mPaintColor);
        mPaintEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }


    public void setShape(File inner, File outer, File layer) {
        isResize = false;
        Bitmap inner1 = BitmapFactory.decodeFile(String.valueOf(inner));
        Bitmap inner2 = BitmapFactory.decodeFile(String.valueOf(outer));
        Bitmap inner3 = BitmapFactory.decodeFile(String.valueOf(layer));

        mCurrentOp.path.reset();
        mInnerShape = inner1;
        mOuterShape = inner2;
        mLayerBitmap = inner3;
        requestLayout();
        invalidate();
    }


    public void changeColor(String color) {

        mCurrentOp.reset();
        mCurrentOp.type = DrawOp.Type.PAINT;
        mCurrentOp.color = Color.parseColor(color);
    }

    public void setDrawingStroke(int stroke) {
        mCurrentOp.reset();
        mCurrentOp.type = DrawOp.Type.PAINT;
        mCurrentOp.stroke = stroke;
    }

    public void enableEraser() {
        mCurrentOp.reset();
        mCurrentOp.type = DrawOp.Type.ERASE;
    }

    public void clearDrawing() {
        mDrawOps.clear();
        mPencil.clear();
        mCurrentOp.reset();
        this.destroyDrawingCache();
        invalidate();
    }


    public Bitmap getCurrentCanvasColor() {

        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        //this.buildDrawingCache();
        //Bitmap bit = (Bitmap) this.getDrawingCache();
        //Canvas canvas = new Canvas(bit);

        this.draw(canvas);
        return bitmap;
    }


    private void drawOp(Canvas canvas, DrawOp op) {
        if (op.path.isEmpty()) {
            return;
        }
        final Paint paint;
        if (op.type == DrawOp.Type.PAINT) {
            paint = mPaintColor;
            paint.setColor(op.color);
            paint.setStrokeWidth(op.stroke);
        } else {
            paint = mPaintEraser;
            paint.setStrokeWidth(5);
        }
        canvas.drawPath(op.path, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mUndoneOps.clear();
                mPencil.clear();
                mCurrentOp.path.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getHistorySize(); i++) {
                    mCurrentOp.path.lineTo(event.getHistoricalX(i), event.getHistoricalY(i));
                }
                mCurrentOp.path.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentOp.path.lineTo(x, y);
                mDrawOps.add(new DrawOp(mCurrentOp));
                mPencil.add(new DrawOp(mCurrentOp));
                mCurrentOp.path.reset();
                break;
        }

        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isResize) {
            innerRe = Bitmap.createScaledBitmap(mInnerShape, this.getWidth(), this.getHeight(), true);
            outerRe = Bitmap.createScaledBitmap(mOuterShape, this.getWidth(), this.getHeight(), true);
            Log.d("qwerqwer", "aaaaaaaaaa");
            isResize = true;
        }

        if (isInEditMode()) {
            return;
        }

        if (mLayerBitmap == null) {
            layerRe = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);

        } else {
            layerRe = Bitmap.createScaledBitmap(mLayerBitmap, this.getWidth(), this.getHeight(), true);
            canvas.drawBitmap(layerRe, 0, 0, null);

        }

        // mLayerCanvas.setBitmap(mLayerBitmap);

        //  canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        for (DrawOp op : mDrawOps) {
            drawOp(canvas, op);
        }
        drawOp(canvas, mCurrentOp);

        mCanvas.drawBitmap(innerRe, 0, 0, mPaintDstIn);
        canvas.drawBitmap(outerRe, 0, 0, null);


        // canvas.drawBitmap(mLayerBitmap,0,0, null);

    }


    private static class DrawOp {
        public final Path path = new Path();
        public Type type;
        public int color;
        public int stroke;


        public DrawOp() {
            //
        }

        public DrawOp(DrawOp op) {
            this.path.set(op.path);
            this.type = op.type;
            this.color = op.color;
            this.stroke = op.stroke;
        }

        public void reset() {
            this.path.reset();
        }

        public enum Type {
            PAINT, ERASE
        }
    }


}
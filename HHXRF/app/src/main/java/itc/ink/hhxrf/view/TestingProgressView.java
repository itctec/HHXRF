package itc.ink.hhxrf.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestingProgressView extends View {
    private int timeLength=7000;
    Paint paint =new Paint();
    ProgressCallBack progressCallBack;
    long startTime=0;

    public TestingProgressView(Context context) {
        super(context);
    }

    public TestingProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setTotalTime(int timeLength,ProgressCallBack progressCallBack){
        this.timeLength=timeLength;
        this.progressCallBack=progressCallBack;
        startTime=System.currentTimeMillis();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient linearGradient =new LinearGradient(0,0,getMeasuredWidth()-110,0, Color.rgb(84,161,250), Color.rgb(176,232,88), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        long progressTime=System.currentTimeMillis()-startTime;
        if(progressTime<=timeLength){
            invalidate();
        }else {
            progressCallBack.progressEnd();
        }
        RectF oval = new RectF(0, 0, getMeasuredWidth()*((float)progressTime/timeLength), 63);
        canvas.drawRoundRect(oval, 7, 7, paint);
    }

    public interface ProgressCallBack{
        public void progressEnd();
    }
}

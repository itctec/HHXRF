package itc.ink.hhxrf.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class McaLineView extends View {
    private List<Integer> mcaDataList;
    private int maxNum=0;
    private int numCount=0;
    private Path mcaLinePath=new Path();
    private Paint mPaint=new Paint();
    public McaLineView(Context context) {
        super(context);
    }

    public McaLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public McaLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMcaData(List<Integer> mcaDataList){
        this.mcaDataList=mcaDataList;
        for(int temp:mcaDataList){
            if(temp>maxNum){
                maxNum=temp;
            }
        }
        numCount=mcaDataList.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("宽->"+this.getWidth()+"\t高->"+this.getHeight());
        float width=this.getWidth();
        float height=this.getHeight();
        canvas.drawColor(Color.WHITE);
        mcaLinePath.moveTo(0,height);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(1);
        for(int i=0;i<numCount;i++){
            mcaLinePath.lineTo((float)i/numCount*width,height-(float)mcaDataList.get(i)/maxNum*height);
        }
        canvas.drawPath(mcaLinePath,mPaint);
    }
}

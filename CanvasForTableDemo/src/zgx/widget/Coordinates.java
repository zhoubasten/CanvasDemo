package zgx.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ClassName : CoordinatesView
 * @Description : TODO
 * @author : ZGX zhangguoxiao_happy@163.com
 * @date : 2011-10-9 ����09:06:38
 * 
 */
public class Coordinates extends View {
	private static final String TAG = "Coordinates";
	/*
	 * ����
	 */
	private Paint mPaint;
	/*
	 * ���ݼ���
	 */
	private List<PointF[]> mPointsList;
	private List<Paint> mPaintList;
	/*
	 * ����
	 */
	private boolean mHasTitle;
	private String mTitle;
	private int mTitleHeight;
	private PointF mTitlePoint;
	/*
	 * �߾�
	 */
	private int mLeftPad, mRightPad, mBottomPad, mTopPad;
	
	/**
	 * ʱ�����ÿ��ĸ߶�
	 */
	private int mTableItemHeight; 
	
	
	/**
	 * ʱ�����ĸ߶�
	 */
	private int mTableDateHeight; 
	
	/**
	 * ������ȣ�Ĭ�Ͽ���Ϊ����ϵ���
	 */
	private int mTableDateWidth;
	
	/*
	 * ���������ܶȡ����Ⱥͱ�����
	 */
	private float mXValuePerPix, mYValuePerPix;
	private int mXLen, mYLen;
	private float mXScale, mYScale;

	/**
	 * �������������
	 */
	private int mXCount, mYCount;
	/**
	 * �ο�����������������
	 */
	private PointF mPointBase = new PointF();
	/**
	 * �ο������߼���������
	 */
	private PointF mPointBaseValue = new PointF();
	/*
	 * ������������ĵ�����ԭ�����������
	 */
	private PointF mPointOrigin = new PointF();

	/*
	 * �Զ���ؼ�һ��д�������췽�� CoordinatesView(Context context)����javaӲ���봴���ؼ�
	 * �����Ҫ���Լ��Ŀؼ��ܹ�ͨ��xml�������ͱ����е�2�����췽�� CoordinatesView(Context context,
	 * AttributeSet attrs) ��Ϊ��ܻ��Զ����þ���AttributeSet������������췽���������̳���View�Ŀؼ�
	 */
	public Coordinates(Context context) {
		super(context, null);
		init();
	}

	public Coordinates(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// ��������
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		// ���ñ߾�
		setCoordinatesPadding(0, 0, 0, 0);
		// ���ñ���
		setTitleHeight(20);
		setTitleName("����");

		setTableItemHeight(20);
		
		mHasTitle = true;
		mTitlePoint = new PointF(mLeftPad, mTitleHeight);
		// �����ܶ�
		mXValuePerPix = 0.5f;
		mYValuePerPix = 0.5f;
		// �������ű���
		mXScale = 1;
		mYScale = 1;
	}

	/**
	 * ���ñ���߶�
	 */
	public void setTitleHeight(int height) {
		mTitleHeight = height;
	}

	/**
	 * ����ͼ������
	 */
	public void setTitleName(String titleName) {
		mTitle = titleName;
	}

	/**
	 * ���÷Ŵ���С����
	 */
	public void setScaleXY(float xScale, float yScale) {
		mXScale = xScale;
		mYScale = yScale;
	}

	public void setTableItemHeight(int height){
		mTableItemHeight = height;
	}
	
	/**
	 * ��������ϵ�ı߾�
	 */
	public void setCoordinatesPadding(int leftPad, int rightPad, int topPad,
			int bottomPad) {
		mLeftPad = leftPad + 40;
		mRightPad = rightPad + 20;
		mTopPad = topPad + 10 ;
		mBottomPad = bottomPad + 40;
	}

	public void addTempreturePoints() {

	}

	/**
	 * ����һ������
	 */
	public void addPoints(PointF[] points, Paint paint) {
		if (points == null)
			return;
		if (mPointsList == null)
			mPointsList = new ArrayList<PointF[]>();
		mPointsList.add(points);
		if (mPaintList == null)
			mPaintList = new ArrayList<Paint>();
		if (paint != null)
			mPaintList.add(paint);
		else {
			mPaintList.add(mPaint);
		}
	}

	/**
	 * �����������ƺ͵�λ
	 */
	public void setAxisNamePrickleXY(String xName, String xPrickle,
			String yName, String yPrickle) {
	}

	/**
	 * �����ܶȣ�����xy������ʾ��������ȷ��xy������ܶ�
	 * 
	 * @param xCount
	 *            ����x������ĵ�ĸ���
	 * @param yCount
	 *            ����y������ĵ�ĸ���
	 */
	public void setPerpix(int xCount, int yCount) {
		Log.v(TAG, "setPerpix");
		mXCount = xCount;
		mYCount = yCount;

	}

	// private int centerX, centerY;
	/*
	 * �ؼ��������֮������ʾ֮ǰ������������������ʱ���Ի�ȡ�ؼ��Ĵ�С ���õ����������������Բ�����ڵĵ㡣
	 */
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		// centerX = w / 2;
		// centerY = h / 2;
		Log.v(TAG, "onSizeChanged W:" + w + " H:" + h);
		mXLen = w - mLeftPad - mRightPad;
		mYLen = h - mBottomPad - mTopPad - mTableDateHeight - mTitleHeight;
		
		mTableDateWidth = mXLen;
		
		mXValuePerPix = ((float) mXLen) / (float) mXCount;
		mYValuePerPix = ((float) mYLen) / (float) mYCount;
		Log.v(TAG, "onSizeChanged mXValuePerPix:" + mXValuePerPix
				+ " mYValuePerPix:" + mYValuePerPix);
		mPointOrigin.set(mLeftPad, h - mBottomPad);
		mPointBase.set(mXLen / 2 + mPointOrigin.x, mPointOrigin.y - mYLen / 2);
		mPointBaseValue.set(mXLen / 2 * mXValuePerPix / mXScale, mYLen / 2
				* mYValuePerPix / mYScale);
		// mPointZero.set(mLeftPad, h - mBottomPad);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/*
	 * �Զ���ؼ�һ�㶼������onDraw(Canvas canvas)�������������Լ���Ҫ��ͼ��
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (canvas == null) {
			return;
		}
		/*
		 * ������ɫ
		 */
		canvas.drawColor(Color.WHITE);
		// ��ֱ��
		canvas.drawLine(mPointOrigin.x, mPointOrigin.y, mPointOrigin.x + mXLen,
				mPointOrigin.y, mPaint);// ��X��
		canvas.drawLine(mPointOrigin.x, mPointOrigin.y, mPointOrigin.x,
				mPointOrigin.y - mYLen, mPaint);// ��Y��

		/*
		 * ������ �����ⲿ��Ҫ���������ϻ������ݣ�����������л���
		 */
		drawMultiLines(canvas, mPointsList, mPaintList);
		drawMultiDashLines(canvas, mXCount, mYCount);
		drawTable(canvas, mTable,mPaint ,true);
	}

	/**
	 * ��������
	 * @param canvas
	 * @param xCount ����������
	 * @param yCount ����������
	 */
	private void drawMultiDashLines(Canvas canvas, int xCount, int yCount) {
		// ��������
		for (int i = 0; i < xCount; i++) {
			PointF pointFrom = new PointF();
			pointFrom.set(i+1, 0);
			PointF pointTo = new PointF();
			pointTo.set(i+1, yCount);
			drawDashLines(canvas, point2Physical(pointFrom),
					point2Physical(pointTo));
		}
		// ��������
		for (int i = 0; i < yCount; i++) {
			PointF pointFrom = new PointF();
			pointFrom.set(0, i+1);
			PointF pointTo = new PointF();
			pointTo.set(xCount, i+1);
			drawDashLines(canvas, point2Physical(pointFrom),
					point2Physical(pointTo));
		}
	}

	/**
	 * ��������
	 * 
	 * @param canvas
	 */
	public void drawDashLines(Canvas canvas, PointF pointFrom, PointF pointTo) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		Path path = new Path();
		path.moveTo(pointFrom.x, pointFrom.y);
		path.lineTo(pointTo.x, pointTo.y);
		PathEffect effects = new DashPathEffect(new float[] { 1, 5, 1, 5 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}

	/**
	 * ����С��
	 */
	private float Round(float f) {
		return (float) (Math.round(f * 10)) / 10;
	}

	/**
	 * ����
	 */
	private void drawPoint(Canvas canvas, PointF p, Paint paint) {
		canvas.drawCircle(p.x, p.y, 2, paint);
	}

	/**
	 * ����
	 */
	private void drawLine(Canvas canvas, PointF pa, PointF pb, Paint paint) {
		canvas.drawLine(pa.x, pa.y, pb.x, pb.y, paint);
	}

	/**
	 * �߼�����ת��Ϊ��Ļ���� ���߼�����logPointF��ת��Ϊ��������
	 */
	private PointF point2Physical(PointF logPointF) {
		PointF physicalPointF = new PointF();
		physicalPointF.set(logPointF.x * mXValuePerPix + mPointOrigin.x,
				mPointOrigin.y - logPointF.y * mYValuePerPix);

		Log.v(TAG, "mPointOrigin x:" + mPointBase.x + " mPointOrigin y:"
				+ mPointBase.y);
		Log.v(TAG, "logit x:" + logPointF.x + " logit y:" + logPointF.y
				+ " phy x:" + physicalPointF.x + " phy y:" + physicalPointF.y);
		return physicalPointF;
	}

	/**
	 * ��������ת��Ϊ�߼����� ����������phyPointF��ת��Ϊ�߼�����
	 */
	private PointF point2Logical(PointF phyPointF) {
		float x = (phyPointF.x - mPointBase.x) * mXValuePerPix / mXScale
				+ mPointBaseValue.x;
		float y = (mPointBase.y - phyPointF.y) * mYValuePerPix / mYScale
				+ mPointBaseValue.y;
		PointF logicalPointF = new PointF(x, y);
		return logicalPointF;
	}

	/**
	 * �������� ���ڻ�������ļ�ͷ
	 */
	private void drawTriangle(Canvas canvas, PointF p1, PointF p2, PointF p3) {
		Path path = new Path();
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.close();

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		// ������������
		canvas.drawPath(path, paint);
	}

	/**
	 * ��������
	 */
	private void drawLines(Canvas canvas, PointF[] point, Paint paint) {
		int len = (point == null) ? 0 : point.length;
		if (len > 0) {
			PointF pa = point[0];
			PointF pb;
			drawPoint(canvas, point2Physical(pa), paint);
			for (int i = 1; i < len; i++) {
				pb = point[i];
				drawLine(canvas, point2Physical(pa), point2Physical(pb), paint);
				drawPoint(canvas, point2Physical(pb), paint);
				pa = pb;
			}
			pb = point[len - 1];
			drawLine(canvas, point2Physical(pa), point2Physical(pb), paint);
			drawPoint(canvas, point2Physical(pb), paint);
		}
	}

	/**
	 * ����������
	 */
	private void drawMultiLines(Canvas canvas, List<PointF[]> pointList,
			List<Paint> paintList) {
		int len = (pointList == null) ? 0 : pointList.size();
		for (int i = 0; i < len; i++) {
			drawLines(canvas, pointList.get(i), paintList.get(i));
		}
	}

	private String[][] mTable;
	
	private void drawTable(Canvas canvas, String[][] table, Paint paint, boolean isCenter){
		paint.setAntiAlias(true);
		
		PointF p0 = new PointF(); 	//�������Ϊ�������Ͻǵ�
		PointF p1 = new PointF();		//�������Ϊ�������½ǵ㣬�����������ȷ��������ο����״
		p0.set(mLeftPad, mTopPad  +mTitleHeight);
		p1.set(mLeftPad + mXLen, mTopPad+ mTableDateHeight +mTitleHeight);
		drawPoint(canvas, p0, paint);
		drawPoint(canvas, p1, paint);
		
		int horLinesCount = table.length;
		int verLinesCount = table[0].length;
		
		float tableItemHeight = mTableItemHeight;
		//���������
		for(int i = 0; i <= horLinesCount; i++){
			PointF from = new PointF(p0.x, p0.y+i*tableItemHeight);
			PointF to = new PointF(p1.x, from.y);
			drawLine(canvas, from, to, paint);
		}
		
		float tableItemWidth = (float)mXLen/(float)verLinesCount;
		//����������
		for(int i = 0; i <= verLinesCount; i++){
			PointF from = new PointF(p0.x + i*tableItemWidth , p0.y);
			PointF to = new PointF(from.x, p1.y);
			drawLine(canvas, from, to, paint);
		}
		if(isCenter){
			for(int i = 0; i<table.length; i ++){	//i��ʾ��i������
				for(int j = 0;j< table[i].length; j++ ){	//j��ʾ��i�еĵ�j������
					String tmp = table[i][j];
					float textWidth = getTextWidth(paint, tmp);
					float centerPointOffset = (tableItemWidth - textWidth)/2f;
					canvas.drawText(table[i][j], p0.x +j*tableItemWidth + centerPointOffset, p0.y + (i+1)*tableItemHeight, paint);
				}
			}
		}
	}
	
	public void addTable(String[][] table) {
		mTable = table;
		mTableDateHeight = mTableItemHeight*table.length;
	}
	
	
	/**
	 * �������ֿ���
	 * @param paint
	 * @param str
	 * @return
	 */
	public static float getTextWidth(Paint paint, String str) {  
        float iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
            float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (float) Math.ceil(widths[j]);  
            }  
        }  
        return iRet;  
    }
	
	/**
	 * ��ȡ����ߴ��Ӧ������߶�
	 * @param fontSize
	 * @return
	 */
	public float getFontHeight(float fontSize) 
	{ 
	   Paint paint = new Paint(); 
	   paint.setTextSize(fontSize); 
	   FontMetrics fm = paint.getFontMetrics(); 
	   return (float)Math.ceil(fm.descent - fm.top) + 2; 
	}
	
}
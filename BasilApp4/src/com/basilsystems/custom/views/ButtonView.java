package com.basilsystems.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.basilsystems.activities.R;

public class ButtonView extends View {

	//circle and text colors
	private int circleCol, labelCol;

	private boolean isChecked, isAuto;
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
//		if(isAuto){
//			setAuto(false);
//		}
		if(isChecked){
			setLabelCol(onColor);
		}else{
			setLabelCol(offColor);
		}
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
		if(isAuto){
			setOuterCirclePaint(autoColor);
		}else{
			setOuterCirclePaint(normalColor);
		}
	}

	private int offColor, onColor, autoColor, normalColor;

	private void setLabelCol(int labelCol) {
		this.labelCol = labelCol;
		invalidate();
	}

	public String getCircleText() {
		return circleText;
	}

	public void setCircleText(String circleText) {
		this.circleText = circleText;
		invalidate();
	}


	private void setOuterCirclePaint(int outerCirclePaint) {
		this.outerCirclePaint = outerCirclePaint;
		invalidate();
	}

	//label text
	private String circleText;
	//paint for drawing custom view
	private Paint circlePaint;

	private int outerCirclePaint;


	public ButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		//paint object for drawing in onDraw
		circlePaint = new Paint();

		//get the attributes specified in attrs.xml using the name we included
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ButtonView, 0, 0);

		try {
			//get the text and colors specified using the names in attrs.xml
			circleText = a.getString(R.styleable.ButtonView_circleLabel);
			circleCol = a.getInteger(R.styleable.ButtonView_circleColor, 0);//0 is default
			onColor = a.getInteger(R.styleable.ButtonView_onColor, 0);
			offColor = a.getInteger(R.styleable.ButtonView_offColor, 0);
			isAuto = a.getBoolean(R.styleable.ButtonView_isAuto, false);
			isChecked = a.getBoolean(R.styleable.ButtonView_isChecked, false);
			autoColor = a.getInteger(R.styleable.ButtonView_autoColor, 0);
			normalColor = a.getInteger(R.styleable.ButtonView_normalColor, 0);


			if(isChecked){
				labelCol = onColor;
			}else{
				labelCol = offColor;
			}
			if(isAuto){
				outerCirclePaint = autoColor;
			}else{
				outerCirclePaint = normalColor;
			}


		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//get half of the width and height as we are working with a circle
		int viewWidthHalf = this.getMeasuredWidth()/2;
		int viewHeightHalf = this.getMeasuredHeight()/2;

		//get the radius as half of the width or height, whichever is smaller
		//subtract ten so that it has some space around it
		int radius = 0;
		if(viewWidthHalf>viewHeightHalf)
			radius=viewHeightHalf-5;
		else
			radius=viewWidthHalf-5;

		circlePaint.setStyle(Style.FILL);
		circlePaint.setAntiAlias(true);

		//set the paint color using the circle color specified
		circlePaint.setColor(circleCol);

		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

		circlePaint.setStyle(Style.STROKE);
		circlePaint.setAntiAlias(true);
		circlePaint.setStrokeWidth(10);

		//set the paint color using the circle color specified
		circlePaint.setColor(outerCirclePaint);
		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);



		Typeface tf = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
		circlePaint.setTypeface(tf);

		//set the text color using the color specified
		circlePaint.setColor(labelCol);


		//set text properties
		circlePaint.setTextAlign(Paint.Align.CENTER);
		circlePaint.setStrokeWidth(0);
		circlePaint.setTextSize(20);
		
		if(circleText.length() > 10){
			circleText = circleText.substring(0, 10);
			circleText = circleText + "..";
		}
		//draw the text using the string attribute and chosen properties
		canvas.drawText(circleText, viewWidthHalf, viewHeightHalf, circlePaint);
	}
}
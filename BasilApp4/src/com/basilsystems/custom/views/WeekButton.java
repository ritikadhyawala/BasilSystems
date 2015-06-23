package com.basilsystems.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.basilsystems.activities.R;

public class WeekButton extends View {

	//circle and text colors
	private int circleCol, labelCol;

	private boolean isChecked;
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		if(isChecked){
		circleCol = onColor;
		}else{
			circleCol = offColor;
		}
		invalidate();
	}



	private int offColor, onColor;


	public String getCircleText() {
		return circleText;
	}

	public void setCircleText(String circleText) {
		this.circleText = circleText;
		invalidate();
	}


	//label text
	private String circleText;
	//paint for drawing custom view
	private Paint circlePaint;



	public WeekButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		//paint object for drawing in onDraw
		circlePaint = new Paint();

		//get the attributes specified in attrs.xml using the name we included
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.WeekButton, 0, 0);

		try {
			//get the text and colors specified using the names in attrs.xml
			circleText = a.getString(R.styleable.WeekButton_weekLabel);
			onColor = a.getInteger(R.styleable.WeekButton_selectedColor, 0);
			offColor = a.getInteger(R.styleable.WeekButton_unselectedColor, 0);
			labelCol = a.getInteger(R.styleable.WeekButton_weekLabelColor, 0);
			isChecked = a.getBoolean(R.styleable.WeekButton_weekButtonChecked, false);


			if(isChecked){
				circleCol = onColor;
			}else{
				circleCol = offColor;
			}

			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isChecked){
						setChecked(false);
					}else{
						setChecked(true);
					}
				}
			});

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
			radius=viewHeightHalf-1;
		else
			radius=viewWidthHalf-1;

		circlePaint.setStyle(Style.FILL);
		circlePaint.setAntiAlias(true);

		//set the paint color using the circle color specified
		circlePaint.setColor(circleCol);



		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

		//set the text color using the color specified
		circlePaint.setColor(labelCol);

		//set text properties
		circlePaint.setTextAlign(Paint.Align.CENTER);
		circlePaint.setTextSize(20);

		//draw the text using the string attribute and chosen properties
		canvas.drawText(circleText, viewWidthHalf, viewHeightHalf, circlePaint);
	}
}
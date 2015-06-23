package com.basilsystems.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.basilsystems.activities.R;

public class CustomRadioButton extends View {

	//circle and text colors
	private int outerGreenColor;

	private boolean isChecked;
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		invalidate();
	}

	//paint for drawing custom view
	private Paint circlePaint;



	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		//paint object for drawing in onDraw
		circlePaint = new Paint();

		//get the attributes specified in attrs.xml using the name we included
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.RadioButton, 0, 0);

		try {
			//get the text and colors specified using the names in attrs.xml
			isChecked = a.getBoolean(R.styleable.RadioButton_radioButtonChecked, false);
			outerGreenColor = a.getColor(R.styleable.RadioButton_outerGreenColor, Color.GREEN);

		} finally {
			a.recycle();
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
			radius=viewHeightHalf-10;
		else
			radius=viewWidthHalf-10;

		if(isChecked){
			circlePaint.setStyle(Style.FILL);
			circlePaint.setAntiAlias(true);
			//set the paint color using the circle color specified
			circlePaint.setColor(outerGreenColor);
			canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
		}

		radius = radius-20;

		circlePaint.setStyle(Style.STROKE);
		circlePaint.setAntiAlias(true);
		circlePaint.setStrokeWidth(5);

		//set the paint color using the circle color specified
		circlePaint.setColor(Color.GRAY);
		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

		circlePaint.setStyle(Style.FILL);
		circlePaint.setAntiAlias(true);

		//set the paint color using the circle color specified
		circlePaint.setColor(Color.WHITE);
		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

	}
}
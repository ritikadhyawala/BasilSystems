package com.basilsystems.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AllDevicesActivity extends Activity implements OnSeekBarChangeListener {
	 private static final int MAX_VALUE_SEEKBAR = 90;
	private static final int MIN_VALUE_SEEKBAR = 10;

	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.all_devices_layout);
	    
	    SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
	    seekBar.setOnSeekBarChangeListener(this);
	    
	    
	  }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(progress > MAX_VALUE_SEEKBAR){
			seekBar.setProgress(MAX_VALUE_SEEKBAR);
			seekBar.setThumb(getResources().getDrawable(R.drawable.custom_thumb_max_value));
		}else if(progress < MIN_VALUE_SEEKBAR){
			seekBar.setProgress(MIN_VALUE_SEEKBAR);
		}	
		
		if(progress<MAX_VALUE_SEEKBAR){
			seekBar.setThumb(getResources().getDrawable(R.drawable.custom_thumb_normal));
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

}

package com.basilsystems.util;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceManager {
	
	private static final String BASIL_PREF = "BasilPref";

	private static SharedPreferences sharedPreferences;
	
	private static Editor editor;
	
	private static SharedPreferenceManager instance = null;
	
	public static SharedPreferenceManager getInstance(Context context){
		if(instance == null){
				instance = new SharedPreferenceManager(context);
		}
		return instance;
	}

	public static final int INT = 1;

	public static final int FLOAT = 2;

	public static final int STRING = 3;

	public static final int LONG = 4;

	public static final int BOOLEAN = 5;

	public static final int STRING_SET = 6;
	
	private SharedPreferenceManager(Context context){
	   
		if(context != null){
			sharedPreferences = context.getSharedPreferences(BASIL_PREF, 0); // 0 - for private mode
		}
	}
	
	public void setPreference(String key, Object value, int type){
		if(key != null && value != null && !key.isEmpty() ){
			editor = sharedPreferences.edit();
			switch(type){
			case SharedPreferenceManager.INT :
				editor.putInt(key, (Integer)value);
				break;
				
			
			case SharedPreferenceManager.STRING_SET :
				editor.putStringSet(key, (Set<String>)value);
				break;
			
			case SharedPreferenceManager.FLOAT :
				editor.putFloat(key, (Float)value);
				break;
				
			case SharedPreferenceManager.LONG :
				editor.putLong(key, (Long)value);
				break;
				
			case SharedPreferenceManager.BOOLEAN :
				editor.putBoolean(key, (Boolean)value);
				break;
				
			case SharedPreferenceManager.STRING :
				editor.putString(key, (String)value);
				break;	
			    
			}
			
			editor.commit();
			
		}
	}
	
	public Object getPreferenceValue(String key, int type){
		
		Object value = null;
		if(key != null && !key.isEmpty()){
			if(sharedPreferences != null && sharedPreferences.contains(key)){
				switch(type){
				case SharedPreferenceManager.INT :
					value = sharedPreferences.getInt(key, 0);
					break;
					
				
				case SharedPreferenceManager.STRING_SET :
					value = sharedPreferences.getStringSet(key, null);
					break;
				
				case SharedPreferenceManager.FLOAT :
					value = sharedPreferences.getFloat(key, 0);
					break;
					
				case SharedPreferenceManager.LONG :
					value = sharedPreferences.getLong(key, 0);
					break;
					
				case SharedPreferenceManager.BOOLEAN :
					value = sharedPreferences.getBoolean(key, false);
					break;
					
				case SharedPreferenceManager.STRING :
					value = sharedPreferences.getString(key, "");
					break;	
				    
				}

			}
		}
		return value;
	}
	
	public void deletePreferences(String key){
		if(key != null && !key.isEmpty()){
			editor = sharedPreferences.edit();
			
			editor.remove(key);
			
			editor.commit();
		}
	}
 
}

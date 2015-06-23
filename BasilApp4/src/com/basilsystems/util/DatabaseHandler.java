package com.basilsystems.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.basilsystems.data.Appliance;
import com.basilsystems.data.Device;
import com.basilsystems.data.IrRemote;
import com.basilsystems.data.Mode;
import com.basilsystems.data.ModeAppliance;
import com.basilsystems.data.ModeDevice;
import com.basilsystems.data.Place;
import com.basilsystems.data.Schedular;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String REMOTE_BUTTON = "REMOTE_BUTTON";
	private static final String REMOTE = "REMOTE";
	private static final String IS_ON = "isOn";
	private static final String ICON = "icon";
	private static final String APPLIANCE_ID = "appliance_id";
	private static final String DEVICE_ID2 = "device_id";
	private static final String PLACE_ID = "place_id";
	private static final String SCHEDULE_ID = "schedule_id";
	private static final String MODE_APPLIANCES = "mode_appliances";
	private static final String MODE_DEVICES = "mode_devices";
	private static final String MODE_ID = "mode_id";
	private static final String MODES = "modes";
	private static final String WEEKDAYS = "weekdays";
	private static final String IS_REPEAT = "isRepeat";
	private static final String END_TIME = "end_time";
	private static final String START_TIME = "start_time";
	private static final String IS_TODAY = "isToday";
	private static final String SCHEDULE_APPLIANCES = "schedule_appliances";
	private static final String STATUS = "status";
	private static final String IS_SLIDER = "isSlider";
	private static final String IS_AUTO = "isAuto";
	private static final String DEVICE_ID = DEVICE_ID2;
	private static final String APPLIANCES = "appliances";
	private static final String ID = "id";
	private static final String PLACE_NAME = "placeName";
	private static final String NAME = "name";

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// places table name
	private static final String PLACES = "places";

	// devices table name
	private static final String DEVICES = "devices";


	private static DatabaseHandler instance = null;

	public SQLiteDatabase getDatabase(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db;
	}

	public static DatabaseHandler getInstance(Context context){
		if(instance == null){
			if(context != null){
				instance = new DatabaseHandler(context);
			}
		}
		return instance;    		
	}

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLACES_TABLE = "CREATE TABLE " + PLACES + "("
				+ NAME + " TEXT, "  + PLACE_ID + " TEXT)";
		String CREATE_DEVICES_TABLE = "CREATE TABLE " + DEVICES + "("
				+ NAME + " TEXT, "  + PLACE_ID + " TEXT, " + ID  + " TEXT, "+ ICON + " INT, " + "Appliances" + " TEXT, " + DEVICE_ID + " TEXT)";
		String CREATE_APPLIANCES_TABLE = "CREATE TABLE " + APPLIANCES + "(" + ID + " TEXT, " + NAME + " TEXT, " + DEVICE_ID + " TEXT, "
				+ IS_SLIDER + " BOOLEAN, " + IS_AUTO + " BOOLEAN, " +IS_ON + " BOOLEAN, " + STATUS + " INT, " + APPLIANCE_ID + " TEXT)";
		String CREATE_SCHEDULAR_TABLE = "CREATE TABLE " + SCHEDULE_APPLIANCES + "(" + ID + " TEXT, " + IS_TODAY +  " BOOLEAN, " + START_TIME 
				+ " TEXT, " + END_TIME + " TEXT, " + IS_REPEAT + " BOOLEAN, " + WEEKDAYS + " INT, " + IS_AUTO + " BOOLEAN, "
				+ STATUS + " INT, " + SCHEDULE_ID + " TEXT)";
		String CREATE_REMOTE_TABLE = "CREATE TABLE " + REMOTE + "("
				+ NAME + " TEXT, " + ID + " INT)";
		String CREATE_REMOTE_BUTTON = "CREATE TABLE " + REMOTE_BUTTON + "("
				+ NAME + " TEXT, " + REMOTE + " TEXT, " + ID + " INT)";
		db.execSQL(CREATE_PLACES_TABLE);
		db.execSQL(CREATE_DEVICES_TABLE);
		db.execSQL(CREATE_APPLIANCES_TABLE);
		db.execSQL(CREATE_SCHEDULAR_TABLE);
		db.execSQL(CREATE_REMOTE_BUTTON);

		//create modes table
		String CREATE_MODES_TABLE = "CREATE TABLE " + MODES + "("
				+ NAME + " TEXT, " + ID + " TEXT, "  + PLACE_ID + " TEXT" + ")";

		String CREATE_MODES_DEVICES_TABLE = "CREATE TABLE " + MODE_DEVICES + "("
				+ NAME + " TEXT, "+ ID + " TEXT, " + MODE_ID + " TEXT)";

		String CREATE_MODES_APPLIANCES_TABLE = "CREATE TABLE " + MODE_APPLIANCES +
				"(" + ID + " TEXT, " + DEVICE_ID + " TEXT, "
				+ IS_SLIDER + " BOOLEAN, " + IS_ON + " BOOLEAN, " +  IS_AUTO + " BOOLEAN, " + STATUS + " INT" + ")";

		db.execSQL(CREATE_MODES_TABLE);
		db.execSQL(CREATE_MODES_DEVICES_TABLE);
		db.execSQL(CREATE_MODES_APPLIANCES_TABLE);
		db.execSQL(CREATE_REMOTE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + PLACES);
		db.execSQL("DROP TABLE IF EXISTS " + DEVICES);
		db.execSQL("DROP TABLE IF EXISTS " + APPLIANCES);
		db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_APPLIANCES);

		db.execSQL("DROP TABLE IF EXISTS " + MODES);
		db.execSQL("DROP TABLE IF EXISTS " + MODE_DEVICES);
		db.execSQL("DROP TABLE IF EXISTS " + MODE_APPLIANCES);

		db.execSQL("DROP TABLE IF EXISTS " + REMOTE);
		db.execSQL("DROP TABLE IF EXISTS " + REMOTE_BUTTON);

		// Create tables again
		onCreate(db);
	}

	public void setPlaces(Place place){

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues cVal = new ContentValues();

		cVal.put(PLACE_ID, place.getID());

		Cursor cursor = db.query(PLACES, new String[] { PLACE_ID },  PLACE_ID  + " = ?", new String[]{place.getID()}, null, null, null, null);
		if(cursor != null && cursor.moveToNext()){
			db.update(PLACES, cVal, PLACE_ID  + " = ?", new String[]{place.getID()});
		}else{
			cVal.put(NAME, place.getName());
			db.insert(PLACES, null, cVal);
		}

		db.close();

	}

	public List<Place> getAllPlaces(){

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(PLACES, new String[] { NAME , PLACE_ID}, null, null, null, null, null, null);

		List<Place> places = new ArrayList<Place>();

		if(cursor.moveToFirst()){
			do{
				Place place = new Place(cursor.getString(0));
				place.setID(cursor.getString(1));
				places.add(place);	
			}while(cursor.moveToNext());
		}
		return places;
	}

	public void deletePlace(Place place){

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PLACES, NAME + " = ?", new String[]{place.getName()});

		db.close();
	}

	public void renamePlace(String oldName, String newName){

		SQLiteDatabase db = this.getWritableDatabase();
		if(oldName != null && newName != null && !oldName.isEmpty() && !newName.isEmpty()){

			ContentValues cVal = new ContentValues();
			cVal.put(NAME, newName);

			db.update(PLACES, cVal, NAME + " = ?", new String[]{oldName});
		}
	}

	public void setDevices(Device device){
		if(device != null){
			SQLiteDatabase db = this.getReadableDatabase();

			ContentValues cVal = new ContentValues();

			cVal.put(PLACE_ID, device.getPlace_id());
			cVal.put(DEVICE_ID, device.getDevice_id());
			cVal.put(ICON, device.getIcon());

			Cursor cursor = db.query(DEVICES, new String[] { DEVICE_ID },  DEVICE_ID  + " = ?", new String[]{device.getDevice_id()}, null, null, null, null);
			if(cursor != null && cursor.moveToNext()){
				db.update(DEVICES, cVal, DEVICE_ID  + " = ?", new String[]{device.getDevice_id()});
			}else{
				db.insert(DEVICES, null, cVal);
			}                                                                              

			db.close();
		}
	}

	public String getDeviceID(String id){
		if(id != null && !id.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(DEVICES, new String[] { DEVICE_ID },  DEVICE_ID + " = ?", new String[]{id}, null, null, null, null);

			cursor.moveToFirst();
			String device_id = cursor.getString(0);
			return device_id;
		}
		return null;
	}

	public List<Device> getAllDevices(String string){
		if(string != null){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(DEVICES, new String[] { NAME, PLACE_ID, DEVICE_ID, ICON },  PLACE_ID + " = ?", new String[]{string}, null, null, null, null);

			List<Device> devices = new ArrayList<Device>();

			if(cursor.moveToFirst()){
				do{
					Device device = new Device(cursor.getString(0), cursor.getString(1), cursor.getInt(3));
					device.setDevice_id(cursor.getString(2));
					devices.add(device);	
				}while(cursor.moveToNext());
			}
			return devices;
		}
		return null;
	}

	public void renameDevice(String newName, String oldName){

		SQLiteDatabase db = this.getWritableDatabase();
		if(oldName != null && newName != null && !oldName.isEmpty() && !newName.isEmpty()){

			ContentValues cVal = new ContentValues();
			cVal.put(NAME, newName);

			db.update(DEVICES, cVal, NAME + " = ?", new String[]{oldName});
		}

	}

	public void deleteDevice(Device device){
		if(device != null){
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(DEVICES, DEVICE_ID + " = ?", new String[]{device.getDevice_id()});

			db.close();
		}
	}

	public void addApplianceToDevice(Appliance appliance){
		if(appliance != null){
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cVal = new ContentValues();

			cVal.put(NAME, appliance.getApplianceName());
			cVal.put(ID, appliance.getID());
			cVal.put(IS_AUTO, appliance.getIsAuto());
			cVal.put(IS_SLIDER, appliance.getIsSlider());
			cVal.put(STATUS, appliance.getStatus());
			cVal.put(DEVICE_ID, appliance.getParentID());
			cVal.put(APPLIANCE_ID, appliance.getAppliance_id());
			cVal.put(IS_ON, appliance.getIsOn());
			db.insert(APPLIANCES, null, cVal);

			db.close();

		}
	}

	public List<Appliance> getAllAppliances(){
		List<Appliance> appliances = new ArrayList<Appliance>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(APPLIANCES, new String[] { NAME, DEVICE_ID, IS_AUTO, IS_SLIDER, STATUS , APPLIANCE_ID, IS_ON},  null, null, null, null, null, null);


		if(cursor.moveToFirst()){
			do{
				Appliance appliance = new Appliance(cursor.getString(1), cursor.getString(0), Integer.parseInt(cursor.getString(4)), cursor.getString(3).equals("1"), cursor.getString(2).equals("1"), cursor.getString(6).equals("1"));
				appliance.setAppliance_id(cursor.getString(5));
				appliances.add(appliance);	

			}while(cursor.moveToNext());
		}

		return appliances;
	}

	public List<Appliance> getAllAppliancesGivenDevice(String device_id){
		List<Appliance> appliances = new ArrayList<Appliance>();

		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.query(APPLIANCES, new String[] { NAME, DEVICE_ID, IS_AUTO, IS_SLIDER, STATUS, APPLIANCE_ID, IS_ON},   DEVICE_ID + " = ?", new String[]{device_id} , null, null, null, null);

		Cursor cursor = db.rawQuery("SELECT" +  " " + NAME + " ," + DEVICE_ID + " ," + IS_AUTO + " ," + IS_SLIDER+ " ," + STATUS+ " ," + APPLIANCE_ID+ " ," + IS_ON + " FROM " + APPLIANCES +  " WHERE " + DEVICE_ID+   " LIKE ?", 
                new String[]{"%" + device_id  + "%"});
		if(cursor.moveToFirst()){
			do{
				Appliance appliance = new Appliance(cursor.getString(1), cursor.getString(0), Integer.parseInt(cursor.getString(4)), cursor.getString(3).equals("1"), Boolean.parseBoolean(cursor.getString(2)), Boolean.parseBoolean(cursor.getString(6)));
				appliance.setAppliance_id(cursor.getString(5));
				appliances.add(appliance);	
			}while(cursor.moveToNext());
		}

		return appliances;
	}

	public void renameAppliance(String newName, Appliance appliance){

		SQLiteDatabase db = this.getWritableDatabase();
		if(appliance != null && newName != null && !newName.isEmpty()){

			ContentValues cVal = new ContentValues();
			cVal.put(NAME, newName);

			db.update(APPLIANCES, cVal, APPLIANCE_ID + " = ? ", new String[]{appliance.getID()});
		}

	}

	public void deleteAppliance(Appliance appliance){
		if(appliance != null){
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(APPLIANCES, ID + " = ?", new String[]{appliance.getID()});

			db.close();
		}
	}

	public void updateAppliance(String applianceID, JSONObject value){
		SQLiteDatabase db = this.getWritableDatabase();
		if(applianceID != null  && !applianceID.isEmpty() && value != null){

			ContentValues cVal = new ContentValues();
			try {
				if(value.has(NAME)){

					cVal.put(NAME, value.getString(NAME) );

				}else if(value.has(IS_AUTO)){
					cVal.put(IS_AUTO, value.getString(IS_AUTO) );
				}else if(value.has(IS_SLIDER)){
					cVal.put(IS_SLIDER, value.getString(IS_SLIDER) );
				}else if(value.has(STATUS)){
					cVal.put(STATUS, value.getString(STATUS) );
				}else if(value.has(IS_ON)){
					cVal.put(IS_ON, value.getString(IS_ON) );
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			Cursor cursor = db.query(APPLIANCES, new String[] {APPLIANCE_ID },  APPLIANCE_ID + " = ?", new String[]{"mac_addr1_" + String.valueOf(Integer.parseInt(applianceID) + 1)}, null, null, null, null);


			if(cursor != null && cursor.getCount()>0){
				db.update(APPLIANCES, cVal, APPLIANCE_ID + " = ? ", new String[]{"mac_addr1_" + String.valueOf(Integer.parseInt(applianceID) + 1)});
			}
		}
	}

	public void setScheduling(Schedular schedular){
		if(schedular != null){
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cVal = new ContentValues();

			cVal.put(ID, schedular.getAppliance_id());
			cVal.put(IS_TODAY, schedular.isToday());
			cVal.put(IS_REPEAT, schedular.isRepeat());
			cVal.put(WEEKDAYS, schedular.getWeekday());
			cVal.put(IS_AUTO, schedular.isAuto());
			cVal.put(END_TIME, schedular.getEndTime());
			cVal.put(START_TIME, schedular.getStartTime());
			cVal.put(STATUS, schedular.getStatus());
			cVal.put(SCHEDULE_ID, schedular.getSchedule_id());

			db.insert(SCHEDULE_APPLIANCES, null, cVal);

			db.close();
		}
	}

	public Schedular getSchedularForAppliance(String applianceId){
		if(applianceId != null && !applianceId.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(SCHEDULE_APPLIANCES, new String[] { IS_TODAY, IS_REPEAT, WEEKDAYS, IS_AUTO, END_TIME, START_TIME, STATUS, SCHEDULE_ID },   ID + " = ?", new String[]{applianceId} , null, null, null, null);


			if(cursor.moveToFirst()){
				do{
					Schedular schedular = new Schedular(applianceId, cursor.getString(0).equals("1"), cursor.getString(5), cursor.getString(4),cursor.getString(1).equals("1"), Integer.parseInt(cursor.getString(2)), cursor.getString(3).equals("1"), Integer.parseInt(cursor.getString(6)));	
					schedular.setSchedule_id(cursor.getString(7));
					return schedular;
				}while(cursor.moveToNext());
			}
		}

		return null;
	}

	public void addMode(Mode mode){

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues cVal = new ContentValues();

		cVal.put(ID, mode.getMode_id());
		cVal.put(NAME, mode.getMode_name());
		cVal.put(PLACE_ID, mode.getPlace_name());

		db.insert(MODES, null, cVal);


		db.close();

	}

	public List<Mode> getModesOfPlace(String placeID){

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(MODES, new String[] { NAME,  PLACE_ID, ID }, PLACE_ID + " = ?" , new String[]{placeID}, null, null, null, null);

		List<Mode> modes = new ArrayList<Mode>();

		if(cursor.moveToFirst()){
			do{
				modes.add(new Mode(cursor.getString(0), cursor.getString(1), cursor.getString(2)));	
			}while(cursor.moveToNext());
		}
		return modes;
	}

	public void deleteMode(Mode mode){

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(MODES, ID + " =?", new String[]{mode.getMode_id()});

		db.close();
	}

	public void renameMode(String newName, Mode mode){

		SQLiteDatabase db = this.getWritableDatabase();
		if(mode != null && newName != null && !newName.isEmpty()){

			ContentValues cVal = new ContentValues();
			cVal.put(NAME, newName);
			cVal.put(PLACE_NAME, mode.getPlace_name());

			db.update(MODES, cVal, ID + " =?", new String[]{mode.getMode_id()});
		}
	}

	public void setModeDevices(ModeDevice modeDevice){
		if(modeDevice != null){
			SQLiteDatabase db = this.getReadableDatabase();

			//			Cursor cursor = db.query(MODE_DEVICES, new String[] {MODE_ID, ID },  MODE_ID + " = ?", new String[]{modeDevice.getModeId()}, null, null, null, null);

			ContentValues cVal = new ContentValues();

			cVal.put(MODE_ID, modeDevice.getModeId());
			cVal.put(ID, modeDevice.getDeviceID());
			//			if(cursor != null && cursor.getCount()>0){
			//				db.update(MODE_DEVICES, cVal, MODE_ID+"="+modeDevice.getModeId(), null);
			//			}else{
			db.insert(MODE_DEVICES, null, cVal);
			//			}
			db.close();

		}
	}

	public List<ModeDevice> getAllModeDevices(String mode_id){
		if(mode_id != null){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(MODE_DEVICES, new String[] {  MODE_ID, ID },  MODE_ID + " = ?", new String[]{mode_id}, null, null, null, null);

			List<ModeDevice> modeDevices = new ArrayList<ModeDevice>();

			if(cursor.moveToFirst()){
				do{
					modeDevices.add(new ModeDevice(cursor.getString(1), cursor.getString(0)));	
				}while(cursor.moveToNext());
			}
			return modeDevices;	
		}
		return null;
	}

	public void deleteModeDevice(ModeDevice modeDevice){

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(MODE_DEVICES, ID + " =?", new String[]{modeDevice.getDeviceID()});

		db.close();
	}

	public void setModeAppliances(ModeAppliance modeAppliance){
		if(modeAppliance != null){
			SQLiteDatabase db = this.getReadableDatabase();

			ContentValues cVal = new ContentValues();
			cVal.put(ID, modeAppliance.getId());
			cVal.put(DEVICE_ID, modeAppliance.getMode_device_id());
			cVal.put(IS_AUTO, modeAppliance.isAuto());
			cVal.put(IS_SLIDER, modeAppliance.isSlider());
			cVal.put(STATUS, modeAppliance.getStatus());
			cVal.put(IS_ON, modeAppliance.isOn());

			Cursor cursor = db.query(MODE_APPLIANCES, new String[] {ID, DEVICE_ID, IS_AUTO, IS_SLIDER, STATUS }, ID + " = ?", new String[]{modeAppliance.getId()}, null, null, null, null);
			if(cursor != null && cursor.moveToNext()){
				db.update(MODE_APPLIANCES, cVal,  ID  + " = ?", new String[]{modeAppliance.getId()});
			}else{
				db.insert(MODE_APPLIANCES, null, cVal);
			}
			db.close();
		}
	}


	public List<Appliance> getModeAppliances(String device_id){
		if(device_id != null){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(MODE_APPLIANCES, new String[] { DEVICE_ID, ID, IS_AUTO, IS_SLIDER, STATUS, IS_ON },  DEVICE_ID + " = ?", new String[]{device_id}, null, null, null, null);

			List<Appliance> modeAppliances = new ArrayList<Appliance>();

			if(cursor.moveToFirst()){
				do{
					Appliance appliance = new Appliance(cursor.getString(0), getApplianceName(cursor.getString(1)) , Integer.parseInt(cursor.getString(4)), cursor.getString(3).equals("1") , cursor.getString(2).equals("1"), cursor.getString(5).equals("1"));
					appliance.setAppliance_id(cursor.getString(1));
					modeAppliances.add(appliance);	

				}while(cursor.moveToNext());
			}
			return modeAppliances;
		}
		return null;
	}

	public String getPlaceName(String placeID){
		if(placeID != null && !placeID.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(PLACES, new String[] { NAME},  PLACE_ID + " = ?", new String[]{placeID}, null, null, null, null);
			if(cursor != null && cursor.moveToNext()){
				return cursor.getString(0);
			}
		}
		return "";
	}

	public String getDeviceName(String deviceID){
		if(deviceID != null && !deviceID.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(DEVICES, new String[] { NAME},  DEVICE_ID + " = ?", new String[]{deviceID}, null, null, null, null);
			if(cursor != null && cursor.moveToNext()){
				return cursor.getString(0);
			}
		}
		return "";
	}

	public String getApplianceName(String applianceID){
		if(applianceID != null && !applianceID.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(APPLIANCES, new String[] { NAME},  APPLIANCE_ID + " = ?", new String[]{applianceID}, null, null, null, null);
			if(cursor != null && cursor.moveToNext()){
				return cursor.getString(0);
			}
		}
		return "";
	}

	public void updatePlaceName(String placeName, String placeID){
		if(placeName != null && placeID != null){
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues cVal = new ContentValues();
			cVal.put(NAME, placeName);
			db.update(PLACES, cVal,  PLACE_ID + " = ?", new String[]{placeID});
			addMode(new Mode("General", placeID, "general"));
			addMode(new Mode("Away", placeID, "away"));

		}

	}


	public void updateDeviceName(String deviceName, String deviceID){
		if(deviceName != null && deviceID != null){
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues cVal = new ContentValues();
			cVal.put(NAME, deviceName);
			db.update(DEVICES, cVal,  DEVICE_ID + " = ?", new String[]{deviceID});

		}

	}

	public void setRemote(IrRemote remote){
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cVal = new ContentValues();
		cVal.put(NAME, remote.getName());
		cVal.put(ID, remote.getId());
		db.insert(REMOTE, null, cVal);
		db.close();

	}

	public void setRemoteButtons(String remote, ArrayList<String> button_names){
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cVal = null;
		for(int i = 0 ; i <button_names.size() ; i++){
			cVal = new ContentValues();
			cVal.put(REMOTE, remote);
			cVal.put(ID, i);
			cVal.put(NAME, button_names.get(i));
		}
		db.insert(REMOTE, null, cVal);
		db.close();
	}
	
	public ArrayList<String> getRemoteNames(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(REMOTE, new String[] { NAME},  null, null, null, null, null, null);
		ArrayList<String> remoteNames = new ArrayList<String>();
	     while(cursor != null && cursor.moveToNext()){
		       remoteNames.add(cursor.getString(0));
		}
		return remoteNames;
	}

	public IrRemote getRemote(String remoteName){
		if(remoteName != null && !remoteName.isEmpty()){
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(REMOTE, new String[] { NAME, ID},  NAME + " = ?", new String[]{remoteName}, null, null, null, null);
			ArrayList<String> remoteButtons = new ArrayList<String>();
			if(cursor != null && cursor.moveToNext()){
				Cursor cursor1 = db.query(REMOTE_BUTTON, new String[] { NAME, ID, REMOTE},  REMOTE + " = ?", new String[]{remoteName}, null, null, null, null);
				if(cursor1 != null && cursor1.moveToNext()){
					remoteButtons.add(cursor1.getInt(1), cursor1.getString(0));
				}
			}
			IrRemote irRemote = new IrRemote(remoteName, cursor.getInt(1), remoteButtons);
			return irRemote;
		}
		return null;
	}

	public int getNewRemoteID(){
		SQLiteDatabase db = this.getReadableDatabase();
		int i = 0;
		Cursor cursor = db.query(REMOTE, new String[] { ID}, null, null, null, null, null, null);
		while(cursor != null && cursor.moveToNext()){
			if(i < cursor.getInt(0)){
				i = cursor.getInt(0);
			}
		}
		return i+1;
	}

	public void updateApplianceName(String applianceName, String applianceID){
		if(applianceName != null && applianceID != null){
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues cVal = new ContentValues();
			cVal.put(NAME, applianceName);
			db.update(APPLIANCES, cVal,  APPLIANCE_ID + " = ?", new String[]{applianceID});

		}

	}

}

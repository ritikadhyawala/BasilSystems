//package com.basilsystems.activities;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.ActionBar;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.basilsystems.util.SocketConnectionManager;
//import com.koushikdutta.async.http.socketio.Acknowledge;
//import com.koushikdutta.async.http.socketio.EventCallback;
//
//public class NewRegisterActivity extends Activity {
//
//	// constant for identifying the dialog
////	private static final int DIALOG_ALERT = 10;
//
//	private EditText username;
//	private EditText name;
//	
//	private boolean isUserExisting = false;
//	
//	private boolean showProgressDialog;
//	
//	private ProgressDialog progressDialog;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.i_am_new_here);
//
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();
//
//		name = (EditText)findViewById(R.id.name);
//		username = (EditText)findViewById(R.id.email);
//
//		Button register_button = (Button)findViewById(R.id.register);
//		register_button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//				showDialog(DIALOG_ALERT);
//				if(username.getText().toString().isEmpty()){
//					Toast.makeText(getApplicationContext(), "Username/email id ca not be empty", Toast.LENGTH_SHORT).show();
//				}
//				else if(name.getText().toString().isEmpty()){
//					Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
//				}else {
//					if(isSocketConnected()){
//						JSONArray jsonArray = new JSONArray();
//						JSONObject jsonMessage = new JSONObject();
//						try {
//							jsonMessage.put("name", name.getText().toString());
//							jsonMessage.put("email_id", username.getText().toString());
//							jsonArray.put(jsonMessage);
//							SocketConnectionManager.client.emit("isExistingUser", jsonArray);
//				        	
//				        	showProgressDialog = true;
//				        	progressDialog = ProgressDialog.show(NewRegisterActivity.this, "", "Waiting for the server", true);
//				        	new ShowProgressDialogThread().start();
//				        	
//				        	
//				        	SocketConnectionManager.client.on("isExistingUserReply", new EventCallback() {
//								
//								@Override
//								public void onEvent(String event, JSONArray argument,
//										Acknowledge acknowledge) {
//
//									if(argument != null && argument.length() > 0){
//										try {
//											String serverReply = argument.getString(0);
//											if(serverReply != null){
//										    			isUserExisting = Boolean.parseBoolean(serverReply);
//										    			if(isUserExisting){
//										    				createAlertBoxForExistingUser();
//										    				Toast.makeText(getApplicationContext(), "The user is already registered", Toast.LENGTH_SHORT).show();
//										    			}else{
//										    				JSONArray jsonArray = new JSONArray();
//										    				JSONObject jsonObject = new JSONObject();
//										    				jsonObject.put("email_id", username.getText().toString());
//										    				jsonArray.put(jsonObject);
//										    				SocketConnectionManager.client.emit("sendPasswordToEmail", jsonArray);
//										    				Toast.makeText(getApplicationContext(), "Password has been sent to your email id", Toast.LENGTH_SHORT).show();	
//										    			}
//										    }
//										} catch (JSONException e) {
//											e.printStackTrace();
//										}
//									    
//									}
//									showProgressDialog = false;
//								}
//							});
//				        
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
////						SocketConnectionManager.client.emit(jsonMessage);
//					}
//				}
//			}
//		});
//	}
//
//	private boolean isSocketConnected(){
//		if(SocketConnectionManager.client != null){
//			return true;
//		}else
//			if(SocketConnectionManager.client == null){
//				Toast.makeText(getApplicationContext(), "The connection is not established", Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(getApplicationContext(), "The connection is lost", Toast.LENGTH_SHORT).show();
//			}
//		return false;
//	}
//
//	
//
//	private class ShowProgressDialogThread extends Thread{
//		public void run(){
//			while(true){
//				if(!showProgressDialog){
//					progressDialog.cancel();
//					break;
//				} else
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//			}
//		}
//	}
//	
//	private void createAlertBoxForExistingUser(){
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
// 
//			// set title
//			alertDialogBuilder.setTitle("");
// 
//			// set dialog message
//			alertDialogBuilder
//				.setMessage("The user already exists")
//				.setCancelable(false)
//				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
//						dialog.cancel();
//					}
//				  });
// 
//				// create alert dialog
//				AlertDialog alertDialog = alertDialogBuilder.create();
// 
//				// show it
//				alertDialog.show();
//			
//	}
//
//
//
//}

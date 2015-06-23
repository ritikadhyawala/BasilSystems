//package com.basilsystems.activities;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.basilsystems.util.HttpClient;
//import com.basilsystems.util.SharedPreferenceManager;
//import com.basilsystems.util.Util;
//
//public class LoginActivity extends Activity {
//	private EditText username;
//	private EditText password;
//
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
//
//		username = (EditText)findViewById(R.id.username);
//		password = (EditText)findViewById(R.id.password);
//
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();
//		setSignUpButton();
//		setSignInButton();
//
//	}
//
//
//	private void setSignUpButton() {
//		Button sign_up = (Button)findViewById(R.id.sign_up_button);
//		sign_up.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent newRegisterIntent = new Intent(LoginActivity.this, NewRegisterActivity.class);
//				startActivity(newRegisterIntent);
//
//			}
//		});
//	}
//
//	private void setSignInButton() {
//		Button sign_in = (Button)findViewById(R.id.sign_in_button);
//		sign_in.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				new TokenManager().execute();
//			}
//		});
//	}
//
//
////	private String getTokenFromServer(String username, String password){
////
////		try {
////			Uri.Builder builder = new Uri.Builder();
////			builder.encodedAuthority(Util.URL).appendQueryParameter("email_id", username).appendQueryParameter("sender_type", "app");
////			Uri uri = builder.build();
////			String server_response = HttpClient.getResponseFromHTTPServer(new URI(Util.URL + "?email_id=" + username + "&password=" + password + "&sender_type=app"));
////			JSONObject json = new JSONObject(server_response);
////			String token = json.getString(Util.TOKEN);
////			return token;
////		} catch (URISyntaxException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (JSONException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		return null;
////	}
//
//	public class TokenManager extends AsyncTask<Void, Void, String>{
//
//
//		@Override
//		protected String doInBackground(Void... params) {
//			Uri.Builder builder = new Uri.Builder();
//			builder.encodedAuthority(Util.URL).appendQueryParameter("email_id", username.getText().toString()).appendQueryParameter("sender_type", "app");
//			String token = null;
//			String server_response;
//			try {
//				server_response = HttpClient.getResponseFromHTTPServer(new URI(Util.URL + "?email_id=" + username.getText().toString() + "&password=" + password.getText().toString() + "&sender_type=app"));
//				JSONObject json = new JSONObject(server_response);
//				token = json.getString(Util.TOKEN);
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(token != null && !token.isEmpty()){
//				SharedPreferenceManager.getInstance(getApplicationContext()).setPreference(Util.TOKEN, token, SharedPreferenceManager.STRING);
//			}else{
//				Util.showToast(getApplicationContext(), "The username/password is wrong!");
//			}
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(String result){
//			
//		}
//	}
//
//}

package com.basilsystems.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ListApplinacesActivity extends Activity  {
  // Unchanged
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
  }
  
  //NEW
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.home, menu);
    return true;
  }
  
  //NEW
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_refresh:
      Toast.makeText(this, "Action refresh selected", Toast.LENGTH_SHORT)
          .show();
      break;
    case R.id.action_settings:
      Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT)
          .show();
      break;

    default:
      break;
    }

    return true;
  }

  // Other methods which this class implements
} 
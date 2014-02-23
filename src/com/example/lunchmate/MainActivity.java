package com.example.lunchmate;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}
	
	public void button(View view) {
		Intent intent = new Intent(this, ConnectionActivity.class);
		startActivity(intent);
	}
}

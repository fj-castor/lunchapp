package com.example.lunchmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        Intent intent = getIntent();
		String[] listing = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
		Double lat = Double.parseDouble(listing[1]);
		Double lont = Double.parseDouble(listing[2]);
		
		TextView name = (TextView) findViewById(R.id.store_name);
		name.setText(listing[0]);
        
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.my_map)).getMap();
        LatLng area = new LatLng(lat, lont);
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                area, 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps. 
        
        //Push to separate activity window, passing through one selected 
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(area));
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	onDestroy();
    }
}
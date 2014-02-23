package com.example.lunchmate;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.lunchmate.MESSAGE";
	public final static String apiKey = "znrtvsefufdsvedzfuaxtsfc";
	public final static String apiURL = "http://api.sandbox.yellowapi.com/FindBusiness/?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}
	
    /* Called when api test button pressed */
    public void testApi(View view) {
    	EditText editText = (EditText) findViewById(R.id.edit_text_search);
    	String keywords = editText.getText().toString().trim().replace(' ', '+');
    	if (keywords != null && keywords.isEmpty() != true) {
    		String urlString = apiURL + "what=" + keywords + "restaurants+lunch+food&where=Toronto" 
    				+ "&pgLen=2&dist=5" + "&fmt=JSON" + "&apikey=" + apiKey + "&UID=1";
    		//Intent i = new Intent(this, MapActivity.class);
    		//i.putExtra("urlString", urlString);
    		new CallAPI().execute(urlString);
    	}
    }
    
    /* API call subclass */
    private class CallAPI extends AsyncTask<String, String, JSONObject> {
		
		@Override
		protected JSONObject doInBackground(String... params) {
			String urlString = params[0];
			JSONObject result = null;
			
			try {
				// Get Request stuff
				DefaultHttpClient defaultClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(urlString);
				HttpResponse httpResponse = defaultClient.execute(getRequest);
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
				// Build JSON
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				result = new JSONObject(sb.toString());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			Log.d("HELLLO", result.toString());
			return result;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			String[] listing = new String[3];
			
			// Extract elements of JSON
			try {
				JSONArray jArray = result.getJSONArray("listings");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject jListing = jArray.getJSONObject(i);
					String name = jListing.getString("name");
					JSONObject addressJSON = jListing.getJSONObject("geoCode");
					Log.d("SHsssssssIT", name);
					listing[i*3] = name;
					listing[i*3+1] = addressJSON.getString("latitude");
					listing[i*3+2] = addressJSON.getString("longitude");
				}
				
			} catch (Exception e) {
				Log.d("DSADSDAS", "");
				e.printStackTrace();
			}
			
			
			// Pass to new activity as String[]
			Intent intent = new Intent(getApplicationContext(), MapActivity.class);
			intent.putExtra(EXTRA_MESSAGE, listing);
			startActivity(intent);
		}
	}
}

package com.example.lunchmate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

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
    		String urlString = apiURL + "what=" + keywords + "+restaurants+lunch+food&where=Toronto" 
    				+ "&pgLen=9&dist=5" + "&fmt=JSON" + "&apikey=" + apiKey + "&UID=143432";
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
				// Get all the returned listings
				JSONArray jArray = result.getJSONArray("listings");	
				
				// Pick a random element from the listings
				Random randGen = new Random();
				int randomNum = randGen.nextInt(jArray.length());
				JSONObject jListing = jArray.getJSONObject(randomNum);
				
				// Get coordinates of chosen listing to map
				String name = jListing.getString("name");
				JSONObject addressJSON = jListing.getJSONObject("geoCode");
				listing[0] = name;
				listing[1] = addressJSON.getString("latitude");
				listing[2] = addressJSON.getString("longitude");
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			// Pass to new activity as String[]
			Intent intent = new Intent(getApplicationContext(), MapActivity.class);
			intent.putExtra(EXTRA_MESSAGE, listing);
			startActivity(intent);
		}
	}
}

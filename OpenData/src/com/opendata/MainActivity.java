package com.opendata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	
	ListView videoList;
	ArrayAdapter<String> videoAdapter;
	ArrayList<String> videoArrayList = new ArrayList<String>();
	Context context;
	String feedUrl ="http://data.taipei.gov.tw/opendata/apply/json/QjI5MURDOTgtOUU3OC00RTlELUE1MTAtRTIwMUVGNzNEN0Q3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		videoList =(ListView)findViewById(R.id.ListV1);
		context = this;		
		videoAdapter = new ArrayAdapter<String>(this,R.layout.video_list_item,videoArrayList);
		videoList.setAdapter(videoAdapter);
		
		VideoListTask loaderTask = new VideoListTask();
		loaderTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class VideoListTask extends AsyncTask<Void,Void,Void>{

		ProgressDialog dialog;
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			videoAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading Data");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(feedUrl);
			
			try {
				 HttpResponse response = client.execute(getRequest);
				 StatusLine statusLine = response.getStatusLine();
				 int statusCode =statusLine.getStatusCode();
				 
				 if(statusCode != 200){
					 return null;
				 }
				 
				 InputStream jsonStream = response.getEntity().getContent();
				 BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
				 StringBuilder builder = new StringBuilder();
				 String line;
				 while((line = reader.readLine()) != null){
					 builder.append(line);
				 }
				 String jsonData = builder.toString();
				 
				 JSONArray jsonArray = new JSONArray(jsonData);
				 
				 for(int i = 0;i<jsonArray.length();i++){
					 JSONObject jsonObj = jsonArray.getJSONObject(i);
					 videoArrayList.add(jsonObj.getString("E_Name"));
				 }
				 
				 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
}

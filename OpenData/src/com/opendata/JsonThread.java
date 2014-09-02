package com.opendata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonThread implements Runnable {
	JSONObject json_data;

	public void run() { // override Thread's run()
		System.out.println("Here is the starting point of Thread.");

		try {
			String result = "";
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						"http://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=1&keyword=2012");
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream webs = entity.getContent();
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(webs, "ios-8859"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					webs.close();
					result = sb.toString();
				} catch (Exception e) {
					Log.e("Log_Tag", "Error converting result " + e.toString());
				}

				try {
					JSONArray jArray = new JSONArray(result);
					for (int i = 0; i < jArray.length(); i++) {
						json_data = jArray.getJSONObject(i);
					}
				} catch (JSONException e) {
					Log.e("Log_Tag", "Error in Http connection " + e.toString());
				}
			} catch (Exception e) {
				Log.e("Log_Tag", "Error parsing data " + e.toString());
			}
		} catch (Exception e) {
			Log.e("ERROR", "ERROR IN CODE: " + e.toString());
			e.printStackTrace();

		}

	}

	public String get_data() {
		try {
			return json_data.getString("version")
					+ json_data.getString("categoryName")
					+ json_data.getString("total");
		} catch (JSONException e) {
			Log.e("ERROR", "ERROR IN GET_DATA: " + e.toString());
			return null;
		}

	}
}

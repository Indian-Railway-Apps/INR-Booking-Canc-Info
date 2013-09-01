/**
 * 
 */
package org.varunverma.inr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author varun
 *
 */
public class PendingQueryItems {

	
	public List<QueryItem> getPendingQueryItems() throws ClientProtocolException, IOException{
		
		List<QueryItem> list = new ArrayList<QueryItem>();
		
		Application app = Application.getInstance();
		
		String proxyURL = app.getApplicationProperties().getProperty("proxy_addr");
		int proxyPort = Integer.valueOf(app.getApplicationProperties().getProperty("proxy_port"));
		String proxyProtocol = app.getApplicationProperties().getProperty("proxy_protocol");
		String queryURL = app.getApplicationProperties().getProperty("app_url") + "/GetPendingItems.php";
		
		HttpHost proxy = new HttpHost(proxyURL, proxyPort, proxyProtocol);
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		
		HttpPost httppost = new HttpPost(queryURL);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		//Execute HTTP Post Request 
    	HttpResponse response = httpclient.execute(httppost);
    	
    	InputStream is;
		InputStreamReader isr;
		
		// Open Stream for Reading.
		is = response.getEntity().getContent();

		// Get Input Stream Reader.
		isr = new InputStreamReader(is);

		BufferedReader reader = new BufferedReader(isr);

		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		
		JSONArray pendingQueryItems = new JSONArray(builder.toString());
		
		for(int i=0; i<= pendingQueryItems.length(); i++){
			
			JSONObject qi = pendingQueryItems.getJSONObject(i);
			
			QueryItem queryItem = new QueryItem();
			
			queryItem.setTrainNo(qi.getString("train_no"));
			queryItem.setjClass(qi.getString("j_class"));
			queryItem.setTrainNo(qi.getString("travel_date"));
			queryItem.setSourceCode(qi.getString("source_code"));
			queryItem.setDestinationCode(qi.getString("dest_code"));
			
			list.add(queryItem);
			
		}

		return list;
	}
	
}
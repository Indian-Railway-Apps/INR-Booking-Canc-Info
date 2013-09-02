/**
 * 
 */
package org.varunverma.inr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

/**
 * @author varun
 *
 */
public class SaveQueryStatus {

	public void saveStatus(List<QueryItem> queryItems) throws ClientProtocolException, IOException{
		
		JSONArray availInfo = new JSONArray();
		
		Iterator<QueryItem> i = queryItems.iterator();
		
		while(i.hasNext()){
			
			Iterator<AvailabilityInfo> iterator = i.next().getAvailabilityInfo().iterator();
			
			availInfo.put(iterator.next().jsonify());

		}
		
		Application app = Application.getInstance();

		String proxyURL = app.getApplicationProperties().getProperty("proxy_addr");
		int proxyPort = Integer.valueOf(app.getApplicationProperties().getProperty("proxy_port"));
		String proxyProtocol = app.getApplicationProperties().getProperty("proxy_protocol");
		String saveURL = app.getApplicationProperties().getProperty("app_url") + "/SaveAvailability.php";

		HttpHost proxy = new HttpHost(proxyURL, proxyPort, proxyProtocol);
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
		
		HttpPost httppost = new HttpPost(saveURL);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		nameValuePairs.add(new BasicNameValuePair("avail_data", availInfo.toString()));
		
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
		
		
	}
	
}
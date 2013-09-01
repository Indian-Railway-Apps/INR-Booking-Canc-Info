/**
 * 
 */
package org.varunverma.inr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author varun
 * 
 */
public class QueryItem {

	private String trainNo, travelDate, jClass, sourceCode, destinationCode;
	private List<AvailabilityInfo> availInfo;

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	public String getjClass() {
		return jClass;
	}

	public void setjClass(String jClass) {
		this.jClass = jClass;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public void queryStatus() throws ClientProtocolException, IOException,
			ParserConfigurationException, SAXException {

		// Query the Status

		Application app = Application.getInstance();

		String proxyURL = app.getApplicationProperties().getProperty("proxy_addr");
		int proxyPort = Integer.valueOf(app.getApplicationProperties().getProperty("proxy_port"));
		String proxyProtocol = app.getApplicationProperties().getProperty("proxy_protocol");

		HttpHost proxy = new HttpHost(proxyURL, proxyPort, proxyProtocol);
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);

		HttpPost httppost = new HttpPost("http://www.indianrail.gov.in/cgi_bin/inet_accavl_cgi.cgi");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		if (response != null) {

			// Read
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			boolean read = false;
			String line = null, response_string = "", html_content = "";
			int to;
			while ((line = reader.readLine()) != null) {

				html_content = html_content.concat(line);
				html_content = html_content.concat("\n");

				if (line.contains("S.No.")) {
					read = true;
					to = line.indexOf(">");
					line = line.replaceFirst(line.substring(3, to), "");
					line = "<tbody>" + line;
				}

				if (line.contains("</tbody>")) {
					response_string = response_string + "</tbody>" + "\n";
					read = false;
				}

				if (line.contains("ALIGN = ")) {
					to = line.indexOf(">");
					line = line.replaceFirst(line.substring(3, to), "");
				}

				if (line.contains("class=")) {
					to = line.indexOf(">");
					line = line.replaceFirst(line.substring(3, to), "");
				}

				if (line.contentEquals("<TR>") || line.contentEquals("</TR>")) {
					line = "";
				}

				if (read) {
					response_string = response_string + line + "\n";
				}
			}

			in.close();

			ParseTrainAvailability my_handler = new ParseTrainAvailability();
			my_handler.setAdditionalInfo(trainNo, jClass, sourceCode, destinationCode);

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			InputSource is = new InputSource(new StringReader(response_string));
			saxParser.parse(is, my_handler);
			
			availInfo.addAll(my_handler.availInfo);

		}

	}

}
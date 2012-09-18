package malthusian.qr.net;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import malthusian.qr.Logger;
import malthusian.qr.MainActivity;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class NetClient extends Thread
{
	private static Logger logger = Logger.getLogger(NetClient.class);
	
	private HttpClient httpClient;
	private HttpHost httpHost;
	
	private String hostName = "horizab1.miniserver.com";
	private String pathPrefix = "/malthusian";
	private String pathSuffix = "/api/do_checkin.php";

	private RequestQueue requestQueue = new RequestQueue();
	
	private boolean running = true;
	
	public NetClient(MainActivity owner)
	{
		httpHost = new HttpHost(hostName, 80, "http");
		httpClient = new DefaultHttpClient();
	}
	
	public void requestStop()
	{
		this.running = false;
		requestQueue.add(null);
	}
	
	public void addRequest(Request req)
	{
		requestQueue.add(req);
	}
	
	public void run()
	{
		while(running)
		{
			Request req = requestQueue.get();
			
			if(req==null)
			{
				continue;
			}
			
			logger.debug("starting upload");

			HttpPost httpPost = new HttpPost();
			HttpResponse response = null;
			
			try
			{
				String s = "http://" + this.hostName + this.pathPrefix + this.pathSuffix;
				logger.debug(s);
				httpPost.setURI(new URI(s));
				httpPost.setHeader("Host", this.hostName);
				
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("post_player", req.getPlayer()));
				pairs.add(new BasicNameValuePair("post_location", req.getLocation()));

				httpPost.setEntity(new UrlEncodedFormEntity(pairs));
				response = httpClient.execute(httpPost);
			}
			catch(Exception e)
			{
				logger.error("Error doing upload post", e);
				continue;
			}

			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				logger.debug("NetPublishWorker Server did not return 200: " +
						response.getStatusLine().getReasonPhrase() + " " +
						response.getStatusLine().getStatusCode());
				httpPost.abort();
				continue;
			}
				
			httpPost.abort();
			requestQueue.remove();
		}
	}
}

package com.finoit.net;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * 
 * @author parashar.ankur
 */
public class HttpClientFactory {
	
	private HttpClient sClientInstance;
	private static HttpClientFactory sFactoryInstance = null;
	private boolean mTrustAllCerts = false;
	
	public void setmTrustAllCerts(boolean mTrustAllCerts) {
		this.mTrustAllCerts = mTrustAllCerts;
	}

	/*static{
		sClientInstance = null;
	}*/
	
	public static HttpClientFactory getInstance(){
		
		if(sFactoryInstance == null)
				sFactoryInstance = new HttpClientFactory();
		
		return sFactoryInstance;
	}
	
	public HttpClient getHttpClient(){
		
		if(sClientInstance == null)
				sClientInstance = getThreadSafeHtpClient(120000, this.mTrustAllCerts);
		
		return sClientInstance;
	}
	
	public HttpClient getThreadSafeHtpClient(int paramInt, boolean paramBoolean){
		
		 	Object localObject = new DefaultHttpClient();
		    HttpParams localHttpParams = ((DefaultHttpClient)localObject).getParams();
		    localObject = ((DefaultHttpClient)localObject).getConnectionManager().getSchemeRegistry();
		    
		    if (paramBoolean)
		    {
		    	((SchemeRegistry)localObject).unregister("https");
		    	((SchemeRegistry)localObject).register(new Scheme("https", new SSLSocketFactory(), 443));
		    }
		    
		    HttpConnectionParams.setStaleCheckingEnabled(localHttpParams, false);
		    HttpConnectionParams.setConnectionTimeout(localHttpParams, paramInt);
		    HttpConnectionParams.setSoTimeout(localHttpParams, paramInt);
		    HttpConnectionParams.setSocketBufferSize(localHttpParams, 8192);
		    HttpClientParams.setRedirecting(localHttpParams, true);
		    HttpProtocolParams.setUserAgent(localHttpParams, "DroidKit-HTTPClient-for-Android");
		    return (HttpClient)new DefaultHttpClient(new ThreadSafeClientConnManager(localHttpParams,
		    (SchemeRegistry)localObject), localHttpParams);
		    
	}

}

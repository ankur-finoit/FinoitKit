package com.finoit.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.finoitkit.FinoitKit;

import android.os.Bundle;
import android.util.Log;

/**
 * This Class contains Net Utility based functions
 * @author parashar.ankur
 *
 */
public class HttpUtils {
	
   public static final int METHOD_GET = 1;
   public static final int METHOD_POST = 2;
   public static final int METHOD_PUT = 3;
   public static final int METHOD_DELETE = 4;
   public static final int METHOD_SOAP = 5;
    
  /**
   * This function will convert Bundle Object into a of List<NameValuePair>
   * @param paramBundle : Input bundle 
   * @return : List<NameValuePair>
   */
   public static List<NameValuePair> bundleToList(Bundle paramBundle) {
	   
     ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
     
     Iterator<?> localIterator = paramBundle.keySet().iterator();
     while (localIterator.hasNext())
     {
    	 	String str = (String)localIterator.next();
    	 	localArrayList.add(new BasicNameValuePair(str, paramBundle.getString(str)));
     }
     
     return localArrayList;
     
   }
   
  /**
   * This function returns all the encoded parameters
   * from the String into bundle  
   * @param paramString
   * @return
   */
   @SuppressWarnings("deprecation")
   public static Bundle decodeParams(String paramString) {
	   
     Bundle localBundle = new Bundle();
     if(paramString != null)
     {
    	 String[] arrayOfString2 = paramString.split("&");
    	 int i = arrayOfString2.length;
    	 for (int j = 0; j < i; j++)
    	 {
    		 String[] arrayOfString1 = arrayOfString2[j].split("=");
    		 localBundle.putString(URLDecoder.decode(arrayOfString1[0]), URLDecoder.decode(arrayOfString1[1]));
    	 }
       
     }
     
     return localBundle;
     
   }
   
   /**
   * This function will returns a String of encoded values
   * @param params : Bundle needs to be encoded into the String
   * @return
   */
   public static String encodeParams(Bundle params) {
	   
       if (params == null || params.isEmpty()) {
    	   		return "";
       }
       
       StringBuilder sb = new StringBuilder();
       boolean first = true;

       for (String key : params.keySet()) {
    	   
           if (key == null || params.getString(key) == null)
               		continue;
           
           if (first) 
               	 first = false;
           else 
        	   	 sb.append("&");

           try {
               sb.append(URLEncoder.encode(key, HTTP.UTF_8)).append("=").append(URLEncoder.encode(params.getString(key), HTTP.UTF_8));
           }
           catch (UnsupportedEncodingException e) {
               return null;
           }
           
       }

       return sb.toString();
       
   }
   
   /**
   * Convert InputStream into Text
   * @param paramInputStream
   * @return
   * @throws IOException
   */
   public static String read(InputStream paramInputStream) throws IOException
   {
	   
		StringBuilder localStringBuilder = new StringBuilder();
		BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream), 1000);
		    
		for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine())
					localStringBuilder.append(str);
		    
		paramInputStream.close();
		return localStringBuilder.toString();
		
   }
   
   /**
    * This function will send the Http-Request to the Internet
    * @param client
    * @param url
    * @param method
    * @param data
    * @param extras
    * @param body
    * @param header
    * @return
    */
   public static HttpResponse openUrl( HttpClient client, String url, int method, Bundle header, Bundle data,
		   								ArrayList<byte[]> extras, ArrayList<String> body ) {

			HttpResponse response = null;
			
			try {
					client = HttpClientFactory.getInstance().getHttpClient();
					response = sendData(client, url, data, method, extras, body, header);
					if (response == null) {
							return null;
					}
			
				Log.d("Response: ", response.toString());
			}
			catch (IOException e) {
				Log.e("Error reading response: ", e.toString());
				return null;
			}
			catch (IllegalStateException e) {
				Log.e("Error In Params: ", e.toString());
				return null;
			}
			catch (HttpResponseException e) {
				Log.e("Error In response: ", e.toString());
				return null;
			}

			return response;
		
	}
   
	public static HttpResponse sendData(HttpClient client, String url,Bundle data,
			 int method, ArrayList<byte[]> extras,ArrayList<String> body, Bundle header
					) throws IllegalStateException, IOException,HttpResponseException {

		HttpResponse response = null;
		
		HttpRequestBase msg = null;

		HttpClient httpclient = client;
		
		MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		switch (method) {

			case METHOD_POST:
			
					HttpPost post = new HttpPost(url);				
			
					if (header != null) {
					
						for (String key : header.keySet()) {
								post.setHeader(key, header.getString(key));
						}
					
					}
			
					if (data != null){
						
						for (String key : data.keySet()) {
								multipart.addPart(key, new StringBody(data.getString(key)));
						}
							
					}

					if (extras != null) {
					
						int size = extras.size();
						for (int i = 0; i < size; i++) {
								ByteArrayBody bab = new ByteArrayBody(extras.get(i), "image/jpeg", System.currentTimeMillis() + ".jpg");	
								multipart.addPart(body.get(i), bab);
						}

					}
					post.setEntity(multipart);
					
					msg = post;
					
		break;
				
		case METHOD_GET:
			
					HttpGet get = new HttpGet(url);				
				
					if (header != null) {
						
						for (String key : header.keySet()) {
							get.setHeader(key, header.getString(key));
						}
						
					}
				
					msg = get;

		break;
				
		case METHOD_PUT:
			
					HttpPut put = new HttpPut(url);
			
					if (header != null) {
							for (String key : header.keySet()) {
									put.setHeader(key, header.getString(key));
							}
					}
			
					if (data != null)
						for (String key : data.keySet()) {
							multipart.addPart(key, new StringBody(data.getString(key)));
					}

					if (extras != null) {
						
						int size = extras.size();
						for (int i = 0; i < size; i++) {
							ByteArrayBody bab = new ByteArrayBody(extras.get(i),
									"image/jpeg", System.currentTimeMillis() + ".jpg");
							multipart.addPart(body.get(i), bab);
						}

					}		
			
					List<NameValuePair> pairs = bundleToList(data);
					put.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
			
					msg = put;
		
			break;
				
			case METHOD_DELETE:
			
					HttpDelete delete = new HttpDelete(url);
			
					if (header != null) {
						for (String key : header.keySet()) {
								delete.setHeader(key, header.getString(key));
						}
					}
				
					msg = delete;
				
			break;	
				
			case METHOD_SOAP :
				
				/*HttpPost spapPost = new HttpPost(url);
				
				if (header != null) {
					for (String key : header.keySet()) {
						spapPost.setHeader(key, header.getString(key));
					}
				}
				
				String data = getSoapString(data);
				
				post.setEntity(new StringEntity(str.toString(), HTTP.UTF_8));*/
				
			break; 
			
		}
		
					FinoitKit.getHttpConnectionMonitor().onRequestStart(msg);
				
					try {
		
							response = httpclient.execute(msg);

							int statusCode = response.getStatusLine().getStatusCode();
							System.out.println("Response Code: " + statusCode);

							if (statusCode > 400) {
								new HttpResponseException(statusCode, read(response.getEntity().getContent()));
								Log.d("Response Excpetion: ", statusCode + "");
								return null;
							}
						
							FinoitKit.getHttpConnectionMonitor().onRequestFinished(msg);

					}
					catch (ClientProtocolException e) {
							Log.e("Client error: ", e.toString());
							FinoitKit.getHttpConnectionMonitor().onRequestFinished(msg, e);
							return null;
					} 
					catch (IOException e) {
							Log.e("IO error: ", e.toString());
							FinoitKit.getHttpConnectionMonitor().onRequestFinished(msg, e);
							return null;
					}

			return response;

	}
   
}

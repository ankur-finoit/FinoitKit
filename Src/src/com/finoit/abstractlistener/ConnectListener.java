package com.finoit.abstractlistener;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.finoit.net.HttpUtils;

/**
 * This Class will be for Network and UI callback Listener
 * @author Ankur Parashar
 */
public class ConnectListener {

	Bundle mParams;
	Bundle mHeaders;
	String mRequest;
	String mBody[];
	int mRequestMethod;
	int localMsgID;
	String group_ID;
	String requestTag;

	RequestListener mListener;
	HttpClient mClient;
	String mResponse;
	int mResponseCode;

	ArrayList<byte[]> data;
	ArrayList<String> dataName;
	HashMap<String,String> responseHeaders = new HashMap<String,String>();
	
	public String getRequestTag() {
		return requestTag;
	}

	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}
	
	public ArrayList<String> getDataName() {
		return dataName;
	}

	public void setDataName() {
		this.dataName = new ArrayList<String>();
	}

	public ArrayList<byte[]> getData() {
		return data;
	}

	public void setData() {
		data = new ArrayList<byte[]>();
	}
	
	public String getResponseHeader(String key) {
		return responseHeaders.get(key);
	}
	
	public void setResponseHeader(String key, String value) {
		responseHeaders.put(key, value);
	}
	
	public int getResponseCode() {
		return mResponseCode;
	}
	
	public void setResponseCode(int code) {
		mResponseCode = code;
	}

	public void addData(byte extra[], String name) {
		data.add(extra);
		dataName.add(name);
	}

	public void setList(ArrayList<byte[]> data) {
		this.data.addAll(data);
	}

	public void setRequest(String request, int method) {
		mRequest = request;
		mRequestMethod = method;
	}

	public void setRequestListener(RequestListener listener) {
		mListener = listener;
	}

	public RequestListener getRequestListener() {
		return mListener;
	}

	public void setParams(Bundle params) {
		mParams = params;
	}
	
	public void setHeaders (Bundle headers) {
		mHeaders = headers;
	}

	protected void setResponse(String response) {
		mResponse = response;
	}

	public String getResponse() {
		return mResponse;
	}

	public String getRequest() {
		return mRequest;
	}

	public void setBody(String body[]) {
		mBody = body;
	}

	protected void setHttpClient(HttpClient client) {
		mClient = client;
	}

	public int getLocalMsgID() {
		return localMsgID;
	}

	public void setLocalMsgID(int localMsgID) {
		this.localMsgID = localMsgID;
	}

	public String getGroup_ID() {
		return group_ID;
	}

	public void setGroup_ID(String group_ID) {
		this.group_ID = group_ID;
	}

	public void start() {

		RequestTask task = new RequestTask(this);
		task.execute();
		if (mListener != null) {
				mListener.onRequestStarted(this);
		}

	}

	static class RequestTask extends AsyncTask<String, Void, String> {

		ConnectListener mRequestRef;

		public RequestTask(ConnectListener request) {
			mRequestRef = request;
		}

		@Override
		protected String doInBackground(String... params) {

			ConnectListener request = mRequestRef;
		
			if (request == null) {
					return null;
			}

			try {
				
				HttpResponse response =  HttpUtils.openUrl(request.mClient, request.mRequest, request.mRequestMethod,
											request.mHeaders, request.mParams, request.data, request.dataName);
				
				request.setResponseCode(response.getStatusLine().getStatusCode());
				
				Header headers[] = response.getAllHeaders();
				
				for (Header header : headers) {
							request.setResponseHeader(header.getName(), header.getValue());
				}
				
				return read(new FlushedInputStream(response.getEntity().getContent()));
				
			} 
			catch (Exception e) {
				Log.d("Response", "Invalid response from server: " + e.toString());
			}

			return null;

		}

		@Override
		protected void onPostExecute(String response) {

				ConnectListener request = mRequestRef;
				if (request == null) {
					return;
				}
				
				request.setResponse(response);
				if (response == null && request.mListener != null) {
						request.mListener.onRequestFailed(request);
				} 
				else if (request.mListener != null) {
						request.mListener.onRequestComplted(request);
				}
			
		}

	}
	
	/**
	 * @param in -InputStream        
	 * @return String Response of Server
	 * @throws IOException
	 */
	public static String read(InputStream in) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);

		for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
		}

		in.close();
		return sb.toString();
		
	}
	
	static class FlushedInputStream extends FilterInputStream {

		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

	}

	/**
	 * Listener interface that provides Callbacks on the state of the
	 * Request.
	 */
	public interface RequestListener {

		/**
		 * Callback Client Request Started
		 */
		public void onRequestStarted(ConnectListener listener);

		/**
		 * Callback Client Request Completed
		 */
		public void onRequestComplted(ConnectListener listener);

		/**
		 * Callback Client Request Failed
		 */
		public void onRequestFailed(ConnectListener listener);

	}

}

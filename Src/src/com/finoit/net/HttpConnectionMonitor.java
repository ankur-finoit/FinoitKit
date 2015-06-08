package com.finoit.net;

import java.net.HttpURLConnection;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.methods.HttpRequestBase;


/**
 * A static singleton monitoring object 
 * that keeps track of all connections being used using the FinoitKit
 * @author parashar.ankur
 *
 */
public class HttpConnectionMonitor {

	protected LinkedBlockingQueue<HttpURLConnection> mUrlConnections = new LinkedBlockingQueue<HttpURLConnection>();
	protected LinkedBlockingQueue<HttpRequestBase> mHttpRequests = new LinkedBlockingQueue<HttpRequestBase>();
	protected int mContiguousConnectionErrorCount = 0;
	
	public void onRequestStart(HttpRequestBase request) {
		mHttpRequests.add(request);
	}
	
	public void onRequestFinished(HttpRequestBase request) {
		mHttpRequests.remove(request);
		resetContiguousConnectionErrorCount();
	}
	
	public void onRequestFinished(HttpRequestBase request, Throwable t) {
		mHttpRequests.remove(request);
		if (t != null)
			incrementContiguousConnectionErrorCount();
		else
			resetContiguousConnectionErrorCount();
	}
	
	public void onRequestStart(HttpURLConnection connection) {
		mUrlConnections.add(connection);
	}
	
	public void onRequestFinished(HttpURLConnection connection) {
		mUrlConnections.remove(connection);
		resetContiguousConnectionErrorCount();
	}
	
	public void onRequestFinished(HttpURLConnection connection, Throwable t) {
		mUrlConnections.remove(connection);
		if (t != null)
			incrementContiguousConnectionErrorCount();
		else
			resetContiguousConnectionErrorCount();
	}	
	
	private synchronized void resetContiguousConnectionErrorCount() {
		mContiguousConnectionErrorCount = 0;
	}
	
	private synchronized void incrementContiguousConnectionErrorCount() {
		mContiguousConnectionErrorCount++;
	}
	
	public synchronized int getContiguousConnectionErrorCount() {
		return mContiguousConnectionErrorCount;
	}
	
	public int getCurrentConnectionCount() {
		return mUrlConnections.size() + mHttpRequests.size();
	}
	
}

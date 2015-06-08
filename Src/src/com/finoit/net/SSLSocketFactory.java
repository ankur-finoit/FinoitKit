package com.finoit.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 
 * 
 * @author parashar.ankur
 */
public class SSLSocketFactory implements SocketFactory , LayeredSocketFactory {
	
	private SSLContext sslcontext = null;
	
	private static SSLContext createEasySSLContext() throws IOException{
		
		try{
			
			 SSLContext context = SSLContext.getInstance("TLS");
             context.init(null, new TrustManager[] { new SSLTrustManager(
                             null) }, null);
				return context;
				
		}
		catch(Exception exception){
			throw new IOException(exception.getMessage());
		}
		
	}
	
	private SSLContext getSSLContext() throws IOException {
		
		if (this.sslcontext == null)
				this.sslcontext = createEasySSLContext();
		return this.sslcontext;
	}
	
	@Override
	public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean) 
			throws IOException, UnknownHostException {	
		return getSSLContext().getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
	}

	@Override
	public Socket createSocket() throws IOException {
		return getSSLContext().getSocketFactory().createSocket();
	}

	@Override
	public Socket connectSocket( Socket paramSocket, String host, int port,InetAddress paramInetAddress,
		 int localPort, HttpParams paramHttpParams ) throws IOException, UnknownHostException, ConnectTimeoutException {
		
		int i = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
	    int j = HttpConnectionParams.getSoTimeout(paramHttpParams);
	    
	    InetSocketAddress localInetSocketAddress = new InetSocketAddress(host, port);
	    SSLSocket sslsock = (SSLSocket) ((paramSocket != null) ? paramSocket : createSocket());
	    
	    Object localObject = (SSLSocket)sslsock;
	    if ((paramInetAddress != null) || (localPort > 0))
	    {
	    	if (localPort < 0)
	    			localPort = 0;
	    	((SSLSocket)localObject).bind(new InetSocketAddress(paramInetAddress, localPort));
	    }
	    
	    ((SSLSocket)localObject).connect(localInetSocketAddress, i);
	    ((SSLSocket)localObject).setSoTimeout(j);
	    
	    return (Socket)localObject;
		
	}

	@Override
	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		return true;
	}
	
	public boolean equals(Object obj){
        return ((obj != null) && obj.getClass().equals(SSLSocketFactory.class));
	}

	public int hashCode(){
        return SSLSocketFactory.class.hashCode();
	}

}

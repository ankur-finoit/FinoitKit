package com.finoit.net;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLTrustManager implements X509TrustManager{
	
	private X509TrustManager standardTrustManager = null;
	
	/**
     * Constructor for EasyX509TrustManager.
     */
    public SSLTrustManager( KeyStore keystore ) throws NoSuchAlgorithmException, KeyStoreException
    {
        super();
        
        TrustManagerFactory factory = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm() );
        factory.init( keystore );
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if ( trustmanagers.length == 0 ){
            	throw new NoSuchAlgorithmException( "no trust manager found" );
        }
        
        this.standardTrustManager = (X509TrustManager) trustmanagers[0];
    }
	
	@Override
	public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
		 standardTrustManager.checkClientTrusted( certificates, authType );
	}

	/**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
     */
	@Override
    public void checkServerTrusted( X509Certificate[] certificates, String authType )throws CertificateException {
		
        if ( ( certificates != null ) && ( certificates.length == 1 ) )
            	certificates[0].checkValidity();
        else
            	standardTrustManager.checkServerTrusted( certificates, authType );
        
    }

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return this.standardTrustManager.getAcceptedIssuers();
	}

}

/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;

/**
 * Ideally should be part of {@link HttpWebConnection},
 * but Google App Engine does not support {@link SSLContext}.
 *
 * @version $Revision: 6337 $
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 */
final class HttpWebConnectionInsecureSSL {

    private HttpWebConnectionInsecureSSL() { }

    static void setUseInsecureSSL(final AbstractHttpClient httpClient,
            final boolean useInsecureSSL) throws GeneralSecurityException {
        if (useInsecureSSL) {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
            final SSLSocketFactory factory = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier());
            final Scheme https = new Scheme("https", 443, factory);

            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(https);
        }
        else {
            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        }
    }
}

/**
 * A completely insecure (yet very easy to use) x509 trust manager. This manager trusts all servers
 * and all clients, regardless of credentials or lack thereof.
 *
 * @version $Revision: 6337 $
 * @author Daniel Gredler
 */
class InsecureTrustManager implements X509TrustManager {

    /**
     * {@inheritDoc}
     */
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

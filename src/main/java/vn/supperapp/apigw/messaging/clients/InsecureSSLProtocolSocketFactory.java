package vn.supperapp.apigw.messaging.clients;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

public class InsecureSSLProtocolSocketFactory implements SecureProtocolSocketFactory {
    private SSLContext context_;

    /**
     * Creates a new insecure SSL protocol socket factory.
     *
     * @throws GeneralSecurityException if a security error occurs
     */
    public InsecureSSLProtocolSocketFactory() throws GeneralSecurityException {
        context_ = SSLContext.getInstance("SSL");
        context_.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
            throws IOException {
        return context_.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException {
        return context_.getSocketFactory().createSocket(host, port);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort)
            throws IOException {
        return context_.getSocketFactory().createSocket(host, port, localAddress, localPort);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
                               final HttpConnectionParams params) throws IOException {
        return context_.getSocketFactory().createSocket(host, port, localAddress, localPort);
    }
}

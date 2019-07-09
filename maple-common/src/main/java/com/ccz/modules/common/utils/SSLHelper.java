package com.ccz.modules.common.utils;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;

@Slf4j
public class SSLHelper {

    public SSLContext makeSSL(String keystorePath, String keystorePassword) {
        try {
            InputStream in = getClass().getResourceAsStream(keystorePath);
            byte[] bytes = ByteStreams.toByteArray(in);
            final String keystoreData = Base64.getEncoder().encodeToString(bytes);

            SSLContext serverContext = SSLContext.getInstance("TLSv1.2");
            final KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new ByteArrayInputStream(Base64.getDecoder().decode(keystoreData)), keystorePassword.toCharArray());

            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keystorePassword.toCharArray());
            serverContext.init(kmf.getKeyManagers(), null, null);
            return serverContext;
        } catch (Exception e) {
            //log.error(e.getMessage());
        }
        return null;
    }
}

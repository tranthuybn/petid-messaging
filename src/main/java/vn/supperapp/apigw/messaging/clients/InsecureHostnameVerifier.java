/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.clients;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 *
 * @author luandv
 */
public class InsecureHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String string, SSLSession ssls) {
        return true;
    }
    
}

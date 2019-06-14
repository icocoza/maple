package com.ccz.modules.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class SysUtils {
    private static String localIp = null;
    private static String hostName = null;

    public static String getLinuxIp() {
        if (null != localIp) {
            return localIp;
        }
        try {
            boolean isLoopBack = true;
            Enumeration<NetworkInterface> en;
            en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                if (!ni.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress ia = inetAddresses.nextElement();
                        if (ia.getHostAddress() != null && ia.getHostAddress().indexOf('.') != -1) {
                            localIp = ia.getHostAddress();
                            isLoopBack = false;
                            break;
                        }
                    }
                }
                if (!isLoopBack) {
                    break;
                }
            }
            return localIp;
        } catch (SocketException e) {
            return null;
        }
    }

    public static String getHostname() {
        try {
            if (hostName != null)
                return hostName;
            return hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return getLinuxIp();
        }
    }
}

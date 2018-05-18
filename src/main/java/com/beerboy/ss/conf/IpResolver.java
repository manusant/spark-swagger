package com.beerboy.ss.conf;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

/**
 * @author manusant
 */
public class IpResolver {

    public static String resolvePublicIp() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .filter(networkInterface -> networkInterface.getName() != null && (networkInterface.getName().equals("eth0") || networkInterface.getName().equals("wlan0")))
                    .flatMap(networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
                    .findFirst()
                    .map(InetAddress::getHostAddress)
                    .orElseGet(() -> "localhost");
        } catch (Exception e) {
            return "localhost";
        }
    }
}

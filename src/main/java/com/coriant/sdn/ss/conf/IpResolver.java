package com.coriant.sdn.ss.conf;

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
                    .flatMap(networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
                    .filter(inetAddress -> inetAddress.getHostAddress() != null && !inetAddress.getHostAddress().equals("127.0.0.1") && !inetAddress.getHostAddress().equals("0:0:0:0:0:0:0:1"))
                    .map(InetAddress::getHostAddress)
                    .findFirst()
                    .orElseGet(() -> "localhost");
        }catch (Exception e){
            return "localhost";
        }
    }
}

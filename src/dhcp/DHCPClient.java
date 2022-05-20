/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhcp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ht
 */
public class DHCPClient {

    public static String getRandomMacAddress() {
        String mac = "";
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int n = r.nextInt(255);
            if (i == 5) {
                mac += String.format("%02x", n);
            } else {
                mac += String.format("%02x:", n);
            }
        }
        return mac.toUpperCase();
    }

    public static void main(String[] args) {

        try {

            DatagramSocket clientsocket = new DatagramSocket();

            String macAddress = getRandomMacAddress();

            InetAddress serverip = InetAddress.getByName("255.255.255.255");
            byte[] responsebyte = new byte[65535];
            byte[] requestbyte;

            //System.out.println("you are ready to communicate  ");
            int serverport = 1234;

            String s = "DHCPDISCOVER: source MAC address is " + macAddress
                    + "\ndestintation MAC address is ff:ff:ff:ff:ff:ff";

            requestbyte = s.getBytes();
            DatagramPacket myclientpacket = new DatagramPacket(requestbyte, requestbyte.length, serverip, serverport);
            clientsocket.send(myclientpacket);

            DatagramPacket serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
            clientsocket.receive(serverpacket);
            String ip = new String(serverpacket.getData()).trim();

            responsebyte = new byte[65535];

            serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
            clientsocket.receive(serverpacket);
            String response = new String(serverpacket.getData()).trim();
            System.out.println(response);
            
            requestbyte = new byte[65535];

            String requestip = "DHCPREQUEST: I request [" + ip + "] + [" + InetAddress.getLocalHost().toString() + "]   ";
            requestbyte = requestip.getBytes();
            myclientpacket = new DatagramPacket(requestbyte, requestbyte.length, serverip, serverport);
            clientsocket.send(myclientpacket);
            responsebyte = new byte[65535];

            serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
            clientsocket.receive(serverpacket);

            response = new String(serverpacket.getData()).trim();
            System.out.println(response);
            
            responsebyte = new byte[65535];

            serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
            clientsocket.receive(serverpacket);
            response = new String(serverpacket.getData()).trim();
            System.out.println(response);

            ip = null;

            System.out.println("If you want to request IP Enter Yes else NO");

            responsebyte = new byte[65535];
            
            Scanner sc = new Scanner(System.in);
            
            String input = sc.nextLine();
            responsebyte = input.getBytes();
            myclientpacket = new DatagramPacket(responsebyte, responsebyte.length, serverip, serverport);
            clientsocket.send(myclientpacket);

            responsebyte = new byte[65535];

            while (input.equalsIgnoreCase("Yes")) {

                s = "DHCPDISCOVER: source MAC address is " + macAddress
                        + "\ndestintation MAC address is ff:ff:ff:ff:ff:ff";

                requestbyte = s.getBytes();
                myclientpacket = new DatagramPacket(requestbyte, requestbyte.length, serverip, serverport);
                clientsocket.send(myclientpacket);

                serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
                clientsocket.receive(serverpacket);
                ip = new String(serverpacket.getData()).trim();

                responsebyte = new byte[65535];

                serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
                clientsocket.receive(serverpacket);
                response = new String(serverpacket.getData()).trim();
                System.out.println(response);
                requestbyte = new byte[65535];

                requestip = "DHCPREQUEST: I request [" + ip + "] + [" + InetAddress.getLocalHost().toString() + "]   ";
                requestbyte = requestip.getBytes();
                myclientpacket = new DatagramPacket(requestbyte, requestbyte.length, serverip, serverport);
                clientsocket.send(myclientpacket);
                responsebyte = new byte[65535];

                serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
                clientsocket.receive(serverpacket);

                response = new String(serverpacket.getData()).trim();
                System.out.println(response);
                responsebyte = new byte[65535];

                serverpacket = new DatagramPacket(responsebyte, responsebyte.length);
                clientsocket.receive(serverpacket);
                response = new String(serverpacket.getData()).trim();
                System.out.println(response);
                ip = null;
                
                System.out.println("If you want to request IP Enter Yes else NO");

            responsebyte = new byte[65535];
             input = sc.nextLine();
            responsebyte = input.getBytes();
            myclientpacket = new DatagramPacket(requestbyte, requestbyte.length, serverip, serverport);
            clientsocket.send(myclientpacket);

            responsebyte = new byte[65535];
                
                
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}

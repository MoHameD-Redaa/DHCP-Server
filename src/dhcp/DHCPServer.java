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
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.Scanner;

/**
 *
 * @author ht
 */
public class DHCPServer {

    public static void main(String[] args) throws IOException {
        

        DatagramSocket ds = new DatagramSocket(1234);
        clientConnection ClientConnection = new clientConnection(ds);
        ClientConnection.start();
        System.out.print("Server is up\n");
       

    }

    static class clientConnection extends Thread {

        DatagramSocket ds;

        clientConnection(DatagramSocket ds) {
            this.ds = ds;
        }

        public void run() {
            try {
                String[] IPs = {"192.168.0.100", "192.168.0.101", "192.168.0.102",
                    "192.168.0.103", "192.168.0.104", "192.168.0.105", "192.168.0.106",
                    "192.168.0.107", "192.168.0.108", "192.168.0.109"};

                boolean[] is_used = {false, false, false, false, false, false, false, false, false, false};

                String[] time = new String[10];

                byte[] recieved = new byte[65535];

                DatagramPacket dp = null;

                while (true) {
                    dp = new DatagramPacket(recieved, recieved.length);

                    ds.receive(dp);

                    String req = new String(dp.getData()).trim();

                    System.out.println(req);

                    for (int i = 0; i < 10; i++) {
                        System.out.print(IPs[i] + ' ');
                        if (is_used[i] == false) {
                            System.out.print("Available\n");
                        } else {
                            System.out.print("Reserved\n");
                        }
                    }

                    InetAddress ip = null;

                    int idx = 0;
                    for (int i = 0; i < 10; i++) {
                        if (is_used[i] == false) {
                            ip = InetAddress.getByName(IPs[i]);
                            idx = i;
                            break;
                        }
                    }

                    byte[] offer = new byte[65535];
                    offer = ip.toString().getBytes();
                    InetAddress clientIP = dp.getAddress();
                    int clientPort = dp.getPort();
                    DatagramPacket dp2 = new DatagramPacket(offer, offer.length, clientIP, clientPort);
                    ds.send(dp2);

                    String s = "DHCPOFFER: Your IP address can be " + ip.toString()
                            + "\nServer IP address is " + InetAddress.getLocalHost().toString()
                            + "\nRouter IP address is 127.164.0.0" + "\nSubnet Mask is 127.164.0.5\n"
                            + "the lease time is 3 minutes\nDNS servers are 127.0.0.13, 127.9.9.1\n";

                    offer = new byte[65535];
                    offer = s.getBytes();
                    dp2 = new DatagramPacket(offer, offer.length, clientIP, clientPort);
                    ds.send(dp2);

                    recieved = new byte[65535];
                    dp = new DatagramPacket(recieved, recieved.length);
                    ds.receive(dp);
                    
                    req = new String(dp.getData()).trim();
                    System.out.println(req);

                    is_used[idx] = true;
                    
                    time[idx] = LocalTime.now().plusMinutes(3).toString();
                    
                    s = "DHCPPACK: Your IP address is " + ip.toString()
                            + "\nServer IP address is " + InetAddress.getLocalHost().toString()
                            + "\nRouter IP address is 127.164.0.0" + "\nSubnet Mask is 127.164.0.5\n"
                            + "the lease time is 3 minutes\nDNS servers are 127.0.0.13, 127.9.9.1\n";

                    offer = new byte[65535];
                    offer = s.getBytes();
                    dp2 = new DatagramPacket(offer, offer.length, clientIP, clientPort);
                    ds.send(dp2);
                   
                    while (LocalTime.now().isAfter(LocalTime.parse(time[idx])) == false) {
                    }

                    is_used[idx] = false;
                    offer = new byte[65535];
                    offer = "My ip is expired\n".getBytes();
                    dp2 = new DatagramPacket(offer, offer.length, clientIP, clientPort);
                    ds.send(dp2);
                    
                    
                    recieved = new byte[65535];
                    dp = new DatagramPacket(recieved, recieved.length);
                    ds.receive(dp);
                    req = new String(dp.getData()).trim();
                    System.out.println(req);
                    
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

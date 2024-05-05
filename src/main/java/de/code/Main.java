package de.code;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter target Host: ");
        String targetHost = scanner.nextLine();
        System.out.println("Enter target Port: ");
        int targetPort = scanner.nextInt();
        System.out.println("Enter proxy Port: ");
        int proxyPort = scanner.nextInt();

        Blacklist blacklist = new Blacklist();
        blacklist.loadBlackList();

        ServerSocket serverSocket = new ServerSocket(proxyPort);
        System.out.println("Reverse-Proxy running " + proxyPort);

        Proxy proxy = new Proxy(targetHost, targetPort);
        Log log = new Log("logs.txt");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            log.log("REQ >> " + clientSocket.getInetAddress().toString());
            if (!blacklist.isIpOnBlacklist(clientSocket.getInetAddress().toString())) {
                Thread proxyThread = new Thread(() -> proxy.handleClient(clientSocket));
                proxyThread.start();
            } else log.log("BLOCK >> " + clientSocket.getInetAddress().toString());
        }
    }

}

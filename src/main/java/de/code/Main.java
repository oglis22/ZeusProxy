package de.code;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String targetHost = "minecraft-ip";
    private static final int targetPort = 25565;
    private static final int proxyPort = 25565;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(proxyPort);
        System.out.println("Reverse-Proxy running " + proxyPort);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread proxyThread = new Thread(() -> handleClient(clientSocket));
            proxyThread.start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                Socket targetSocket = new Socket(targetHost, targetPort);
                InputStream clientIn = clientSocket.getInputStream();
                OutputStream clientOut = clientSocket.getOutputStream();
                InputStream targetIn = targetSocket.getInputStream();
                OutputStream targetOut = targetSocket.getOutputStream()
        ) {
            Thread clientToTarget = new Thread(() -> transferData(clientIn, targetOut));
            Thread targetToClient = new Thread(() -> transferData(targetIn, clientOut));

            clientToTarget.start();
            targetToClient.start();

            clientToTarget.join();
            targetToClient.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transferData(InputStream in, OutputStream out) {
        byte[] buffer = new byte[4096];
        int bytesRead;

        try {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
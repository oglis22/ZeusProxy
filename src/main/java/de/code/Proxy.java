package de.code;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Proxy {
    private String targetHost;
    private int targetPort;

    public Proxy(String targetHost, int targetPort) {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }



    public void handleClient(Socket clientSocket) {
        try (
                Socket targetSocket = new Socket(this.targetHost, this.targetPort);
                InputStream clientIn = clientSocket.getInputStream();
                OutputStream clientOut = clientSocket.getOutputStream();
                InputStream targetIn = targetSocket.getInputStream();
                OutputStream targetOut = targetSocket.getOutputStream();
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

    public void transferData(InputStream in, OutputStream out) {
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
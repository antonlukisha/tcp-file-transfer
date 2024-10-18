package org.network.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final int port;
    private final InetAddress address;
    private final String path;

    public Client(int port, InetAddress address, String path) {
        this.port = port;
        this.address = address;
        this.path = path;
    }

    public void start() {
        try (Socket socket = new Socket(address, port);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             FileInputStream fileInputStream = new FileInputStream(path)) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeLong(file.length());
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, bytesRead);
                    dataOutputStream.flush();
                }
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String result = bufferedReader.readLine();
                    System.out.println("Server response: " + result);
                }
            }
        } catch (IOException exception) {
            System.err.println("Error from client: " + exception.getMessage());
        }
    }
}
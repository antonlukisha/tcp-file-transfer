package org.example.client2;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.IOException;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        ConsoleOptions options = new ConsoleOptions();
        JCommander commander = JCommander.newBuilder()
                .addObject(options)
                .build();

        try {
            commander.parse(args);
        } catch (ParameterException exception) {
            System.err.println("Error parsing arguments: " + exception.getMessage());
            commander.usage();
            return;
        }

        try {
            int port = options.port;
            InetAddress inetAddress = InetAddress.getByName(options.address);
            String path = System.getProperty("user.dir") + "/src/main/java/org/example/client2/src/" + options.path;
            Client client = new Client(port, inetAddress, path);
            client.start();
        } catch (IOException exception) {
            System.err.println("Error from client: " + exception.getMessage());
        }
    }
}

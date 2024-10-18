package org.example.server;

import com.beust.jcommander.*;

import java.io.IOException;

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
            Server server = new Server();
            server.start(port);
            server.stop();
        } catch (IOException exception) {
            System.err.println("Error from server: " + exception.getMessage());
        }
    }

}

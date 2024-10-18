package org.example.client1;

import com.beust.jcommander.Parameter;

public class ConsoleOptions {
    @Parameter(
            names = {"-p", "--port"},
            description = "The port",
            required = false
    )
    public int port = 8080;

    @Parameter(
            names = {"-a", "--address"},
            description = "The IP address",
            required = false
    )
    public String address = "192.168.56.1";

    @Parameter(
            names = {"-t", "--path"},
            description = "The path",
            required = false
    )
    public String path = "file1.txt";
}

package org.example.server;

import com.beust.jcommander.Parameter;

public class ConsoleOptions {
    @Parameter(
            names = {"-p", "--port"},
            description = "The port",
            required = false
    )
    public int port = 8080;
}

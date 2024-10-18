# TCP File Transfer with Speed Calculation


[About](#about) | [Description](#description) | [Requirements](#requirements) | [Algorithm](#algorithm) | [Bash Example](#bash-example) | [License](#license)


## :dart: About

This project implements a TCP-based file transfer system in Java. The system allows a client to send a file to a server while the server logs the transfer speed in real-time. The server can handle multiple clients simultaneously using threads and measures both instantaneous and average transfer speed during the session.

## :pencil2: Description

The goal is to develop a custom protocol for transferring files over TCP. The client sends the file name, size, and content to the server, and the server saves the file in its `uploads` directory. The server calculates the transfer speed and outputs it to the console every 3 seconds. Once the transfer is complete, the server verifies the file size and notifies the client of the success or failure.

## :open_file_folder: Requirements

1. **Client**
   - Sends the file name, file size, and its contents.
   - Receives confirmation from the server after successful/failed file transfer.

2. **Server**
   - Receives multiple client connections using threads.
   - Logs the transfer speed every 3 seconds for each client.
   - Verifies the file size and sends success/failure result to the client.

3. **General**
   - Uses TCP for communication.
   - Handles multiple clients concurrently.
   - Releases all system resources correctly after use.

## :triangular_ruler: Algorithm

### :handbag: Client
1. Connects to the server using a TCP socket.
2. Sends the file name, size, and its contents.
3. Waits for the server to confirm whether the transfer was successful or not.

### :office: Server
1. Waits for incoming connections from clients.
2. For each client:
   - Receives the file name, size, and content.
   - Saves the file in the `uploads` directory.
   - Logs the transfer speed every 3 seconds (or earlier if the transfer finishes).
   - Verifies the size and sends the result to the client.

## :paperclip: Bash Example

To compile and run the server and client using the command line:

```bash
# Compile the Java files
javac Server.java Client.java

# Run the server (example with port 8080)
java Server 8080

# Run the client (example with server IP 127.0.0.1 and file to send)
java Client.java 127.0.0.1 8080 file.txt
```

## :page_with_curl: License

   - Badges: Links have been updated to point to the GitHub repository for the Java project.
   - Bash Example: Provides command examples for compiling and running the client and server programs in Java.
   - Code: Example of Java client and server code implementing file transfer over TCP.
To configure the badges, replace your-username/tcp-file-transfer-java with the actual parameters of your repository.

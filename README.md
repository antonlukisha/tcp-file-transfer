# TCP File Transfer with Speed Calculation

<p align="center">
  <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/your-username/tcp-file-transfer?style=for-the-badge">
  <img alt="GitHub top language" src="https://img.shields.io/github/languages/top/your-username/tcp-file-transfer?style=for-the-badge">
  <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/your-username/tcp-file-transfer?style=for-the-badge">
  <img alt="GitHub stars" src="https://img.shields.io/github/stars/your-username/tcp-file-transfer?style=for-the-badge">
  <img alt="GitHub forks" src="https://img.shields.io/github/forks/your-username/tcp-file-transfer?style=for-the-badge">
</p>

---

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Task Description](#task-description)
3. [Requirements](#requirements)
4. [Algorithm](#algorithm)
5. [Bash Example](#bash-example)
6. [Client Code](#client-code)
7. [Server Code](#server-code)
8. [Technologies Used](#technologies-used)
9. [License](#license)

---

## Project Overview

The TCP File Transfer project aims to create a client-server system that transfers files from one computer to another, tracking the transfer speed in real-time. The server logs both instantaneous and average speed and supports multiple clients simultaneously.

## Task Description

The goal is to develop a custom file transfer protocol using TCP, where the server receives the file, saves it in the `uploads` directory, and logs the speed. Clients and servers communicate using this protocol, allowing for efficient data transmission and speed monitoring.

## Requirements

1. **Client**
   - Sends file metadata (name, size) and file content.
   - Receives confirmation from the server on successful/failed transfer.

2. **Server**
   - Receives multiple clients using threads.
   - Calculates the speed of data transfer every 3 seconds for each client.
   - Verifies the file size and notifies the client about the transfer result.

3. **Both**
   - Properly release all OS resources when no longer needed.

## Algorithm

### Client
1. Connects to the server using a TCP connection.
2. Sends file name, size, and content.
3. Waits for server confirmation.

### Server
1. Waits for incoming client connections.
2. For each client:
   - Receives file metadata and content.
   - Saves the file and logs the transfer speed.
   - Verifies the size and sends the result to the client.

## Bash Example

To compile and run the server and client:

```bash
# Compile both programs
g++ -o server server.cpp -pthread
g++ -o client client.cpp

# Start the server (example with port 8080)
./server 8080

# Start the client (example with server IP 127.0.0.1 and file to send)
./client 127.0.0.1 8080 path/to/file.txt

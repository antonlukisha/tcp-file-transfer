package org.network.server;

import java.io.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Server {
    private ServerSocket serverSocket;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        int id = 0;
        try (ExecutorService threadPool = Executors.newFixedThreadPool(100)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket, id, scheduler));
                id = (++id) % 100;
            }
        } finally {
            scheduler.shutdown();

        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final ScheduledExecutorService scheduler;
        private ScheduledFuture<?> speedReporter;
        private static final int REPORT_INTERVAL = 3;
        private AtomicLong bytesReceived;
        private AtomicLong lastBytesReceived;
        private final AtomicLong lastReportedTime = new AtomicLong(0);
        private final AtomicLong startTime = new AtomicLong(0);
        private AtomicBoolean speedReported;
        private final int clientId;

        public ClientHandler(Socket socket, int id, ScheduledExecutorService scheduler) {
            this.socket = socket;
            this.clientId = id;
            this.scheduler = scheduler;
        }

        @Override
        public void run() {
            System.out.println("New client connected: " + formatAddress(socket.getInetAddress()));
            try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                 PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                 socket) {
                String path = dataInputStream.readUTF();
                long size = dataInputStream.readLong();

                Path uploadsDir = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "org", "example", "server", "uploads");
                Path filePath = uploadsDir.resolve(path).normalize();

                if (!filePath.startsWith(uploadsDir)) {
                    System.err.println("Attempt to write outside upload directory by client " + clientId);
                    printWriter.println("Invalid file path.");
                    return;
                }

                if (!Files.exists(uploadsDir)) {
                    Files.createDirectories(uploadsDir);
                }

                lastBytesReceived = new AtomicLong(0);
                bytesReceived = new AtomicLong(0);
                speedReported = new AtomicBoolean(false);
                startTime.set(System.currentTimeMillis());
                lastReportedTime.set(startTime.get());
                speedReporter = scheduler.scheduleAtFixedRate(new SpeedReporter(), REPORT_INTERVAL, REPORT_INTERVAL, TimeUnit.SECONDS);

                try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toString())) {
                    transferFile(fileOutputStream, size, dataInputStream);
                    verify(printWriter, size);
                } catch (IOException exception) {
                    throw new IOException(exception);
                }
            } catch (IOException exception) {
                System.out.println("Server connection with client " + (clientId + 1) + " is broken.");
            } finally {
                if (speedReporter != null) {
                    speedReporter.cancel(true);
                }
            }
        }

        private String formatAddress(InetAddress address) {
            String hostAddress = address.getHostAddress();
            if (address instanceof Inet6Address) {
                hostAddress = hostAddress.split("%")[0];
            } else {
                hostAddress = hostAddress.split("/")[0];
            }
            return hostAddress;
        }

        private void transferFile(FileOutputStream fileOutputStream, long size, DataInputStream dataInputStream) throws IOException {
            int bytesRead;
            byte[] buffer = new byte[4096];

            while (bytesReceived.get() < size) {
                bytesRead = dataInputStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }

                fileOutputStream.write(buffer, 0, bytesRead);

                bytesReceived.addAndGet(bytesRead);
            }
        }

        private void verify(PrintWriter printWriter, long size) {
            if (speedReported.compareAndSet(false, true)) {
                long currentTime = System.currentTimeMillis();
                System.out.printf("Average Speed of Client " + (clientId + 1) + ": %.2f bytes/s%n", (double) bytesReceived.get() / ((currentTime - startTime.get()) / 1000.0));
            }

            if (bytesReceived.get() == size) {
                System.out.println("Successful file transfer to the client " + (clientId + 1));
                printWriter.println("Success");
            } else {
                printWriter.println("Failed");
            }
        }

        private class SpeedReporter implements Runnable {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastReportedTime.get();
                long bytesTransferred = bytesReceived.get() - lastBytesReceived.get();
                double speedInstantaneous = (double) bytesTransferred / (elapsedTime / 1000.0);
                double speedAverage = (double) bytesReceived.get() / ((currentTime - startTime.get()) / 1000.0);

                System.out.printf("(Client %d) Instantaneous Speed: %.2f bytes/s, Average Speed: %.2f bytes/s%n",
                        (clientId + 1),
                        speedInstantaneous,
                        speedAverage);
                speedReported.set(true);

                lastReportedTime.set(currentTime);
                lastBytesReceived.set(bytesReceived.get());
            }
        }
    }
}
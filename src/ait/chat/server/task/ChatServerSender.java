package ait.chat.server.task;

import ait.chat.server.model.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ChatServerSender implements Runnable {
    private final BlockingQueue<String> messageBox;
    private final Set<Client> clients;

    public ChatServerSender(BlockingQueue<String> messageBox) {
        this.messageBox = messageBox;
        clients = new HashSet<>();
    }

    public boolean addClient(Socket socket) throws IOException {
        return clients.add(
                new Client(
                        new PrintWriter(socket.getOutputStream(), true),
                        String.format("%s:%s", socket.getInetAddress(), socket.getPort())
                )
        );
    }

    @Override
    public void run() {
        try {
            while (true) {
                String fullMessage = messageBox.take();
                String[] parts = fullMessage.split("\\|", 2);
                String name = parts[1];
                String message = parts.length > 1 ? parts[1] : "";
                clients.stream()
                        .filter(client -> !client.getName().equals(name) )
                        .forEach(client -> client.getWriter().println(message));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

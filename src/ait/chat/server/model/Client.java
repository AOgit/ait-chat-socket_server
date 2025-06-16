package ait.chat.server.model;

import java.io.PrintWriter;

public class Client {
    private final PrintWriter writer;
    private final String name;

    public Client(PrintWriter writer, String name) {
        this.writer = writer;
        this.name = name;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public String getName() {
        return name;
    }

}

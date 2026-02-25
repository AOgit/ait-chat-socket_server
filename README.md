# 💬 Chat Server — Java Sockets

A **multi-client TCP chat server** built with Java Sockets and multithreading.

Homework project for the **Sockets & Multithreading** lesson at **AIT TR GmbH** (cohort 60).

---

## 📋 Task

Implement the `run()` method in `ChatServerReceiver` to get the main chat server logic working:
- Each connected client gets its own `ChatServerReceiver` thread that reads incoming messages
- Messages are prefixed with the sender's IP address and port, then placed into a shared `BlockingQueue`
- `ChatServerSender` broadcasts each message to all connected clients **except the sender**

---

## 🛠 Tech Stack

- Java
- Java Sockets (`ServerSocket`, `Socket`)
- Multithreading (`Runnable`, `Thread`)
- `BlockingQueue` (thread-safe message passing)
- `BufferedReader` / `PrintWriter` for stream I/O

---

## 🏗 Architecture

```
                    ┌─────────────────────────────┐
                    │        Chat Server           │
                    │                              │
  Client A ──────► │  ChatServerReceiver (Thread) │
                    │         ↓ messageBox         │
  Client B ──────► │  ChatServerReceiver (Thread) │ ──► ChatServerSender (Thread) ──► all clients except sender
                    │         ↓ messageBox         │
  Client C ──────► │  ChatServerReceiver (Thread) │
                    └─────────────────────────────┘
```

Each new client connection spawns:
- 1 `ChatServerReceiver` thread — reads messages from the client socket
- The shared `ChatServerSender` thread — broadcasts messages to all other clients

---

## 🔑 Key Implementation Details

**`ChatServerReceiver.run()`** — the core homework task:
```java
String message = socketReader.readLine();
message = String.format("%s:%s|%s", socket.getInetAddress(), socket.getPort(), message);
messageBox.add(message);
```
Messages are tagged with `ip:port` so the sender can be identified and excluded from broadcast.

**`ChatServerSender.run()`** — broadcasts to all except sender:
```java
String fullMessage = messageBox.take();       // blocking — waits for a message
String[] parts = fullMessage.split("\\|", 2);
String name = parts[0];                       // sender's ip:port
String message = parts[1];                   // actual message text
clients.stream()
    .filter(client -> !client.getName().equals(name))
    .forEach(client -> client.getWriter().println(message));
```

**`Client` model** — wraps a `PrintWriter` and the client's `ip:port` name for identification.

---

## 📁 Project Structure

```
src/ait/chat/server/
├── model/
│   └── Client.java              # Holds PrintWriter + client name (ip:port)
└── task/
    ├── ChatServerReceiver.java  # Runnable — reads messages from one client socket
    └── ChatServerSender.java    # Runnable — broadcasts messages to all other clients
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/AOgit/ait-chat-socket_server.git
cd ait-chat-socket_server
```

Open in **IntelliJ IDEA** and run the main server class.  
Connect multiple clients using `telnet` or a matching socket client:

```bash
telnet localhost <port>
```

package ru.sergeich;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;
    public final static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name + " подключился :)");
            broadcastMessage(name + " подключился :)");
        }
        catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClient();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " отключился :(");
        broadcastMessage( name + " отключился :(");
    }

    public void broadcastMessage(String massage) {
        for (ClientManager client : clients) {
            try {
                if (!client.name.equals(name)){
                    client.bufferedWriter.write(massage);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }
            catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    @Override
    public void run() {
        String massageFromClient;
        while (socket.isConnected()) {
            try {
                massageFromClient = bufferedReader.readLine();
                broadcastMessage(massageFromClient);
            }
            catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }
}

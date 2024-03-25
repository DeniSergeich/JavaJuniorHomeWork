package ru.sergeich;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите своё имя: ");
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost", 8189);
            Client client = new Client(socket, name);
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("Ваш адрес: " + inetAddress);
            String remoteIp = inetAddress.getHostAddress();
            System.out.println("Ваш IP: " + remoteIp);
            System.out.println("Локальный порт: " + socket.getLocalPort());

            client.listenForMessage();
            client.sendMessage();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
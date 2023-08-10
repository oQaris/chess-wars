package io.deeplay.server;

import io.deeplay.domain.Color;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    private Server server;
    private BufferedWriter out;
    private BufferedReader in;
    private Color color;
    private final int playerNumber = 2;


    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
//        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String inputLine;
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            
            while ((inputLine = in.readLine()) != null) {
            }

            Scanner in = new Scanner(clientSocket.getInputStream());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Scanner send = new Scanner(System.in);

            while (in.hasNext()) {
                System.out.println("Data came from client: ");
                System.out.println(in.next());
                System.out.println("Write to send client: ");
                String string = send.nextLine();
                System.out.println(string);
                out.flush();
            }
        } catch (Exception e) {

        }
    }

    public void sendMessage(String message) {
    }
}

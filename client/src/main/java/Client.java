import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private boolean isMyTurn = false;
    private ObjectMapper objectMapper = new ObjectMapper();
    // private ChessController controller;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        // out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private void handleMove(String jsonMove) {
//        try {
//            // получить передвижение в Json и прочитать его
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        }
    }

    public void sendMove(Move move) {
        try {
            String jsonMove = objectMapper.writeValueAsString(move);
            out.write(jsonMove + "\n");
            out.flush();
            isMyTurn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 8080);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server");

            String message = in.readLine();
            while (message != null) {
                handleMessage(message);
                message = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(String message) {
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 6070);
        client.connectToServer();

//        Socket socket = new Socket("localhost", 6070);
//        socket.setSoTimeout(5000);
//        InputStream inputStream = socket.getInputStream();
//        OutputStream outputStream = socket.getOutputStream();
//
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
//
//        PrintWriter out = new PrintWriter(outputStream);
//        Scanner sender = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("Send something to server");
//            String words = sender.nextLine();
//            out.println(words);
//            out.flush();
//            String string = sender.nextLine();
//            System.out.println("Data from server: " + string);
//        }
//        bufferedWriter.write("hello");
//        bufferedWriter.newLine();
//        bufferedWriter.flush();
//        System.out.println(bufferedReader.readLine());
//        bufferedWriter.write("bye");
//        bufferedWriter.newLine();
//        bufferedWriter.flush();
//        System.out.println(bufferedReader.readLine());
//        System.out.println(bufferedReader.readLine());
    }
}
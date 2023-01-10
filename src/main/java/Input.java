import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Input {
    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket("127.0.0.1", 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();
            out.println(input);
            String word = in.readLine();
            System.out.println(word);
        }
    }
}
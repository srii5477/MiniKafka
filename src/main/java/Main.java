import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 9092;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            //serverSocket.setSoTimeout(10);
            // Wait for connection from client.

                clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                byte[] inputBytes = new byte[23];
            while (true) {
                try {
                    int n = in.read(inputBytes);
                    System.out.println(Arrays.toString(inputBytes));
                    int intVal = ByteBuffer.allocate(2).put(Arrays.copyOfRange(inputBytes, 6, 8)).getShort(0);

                    DataOutputStream dout = getDataOutputStream(clientSocket, inputBytes, intVal);
                    dout.close();
                } catch (IOException ex) {
                    break;
                }
            }
        }
         catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static DataOutputStream getDataOutputStream(Socket clientSocket, byte[] inputBytes, int intVal) throws IOException {
        DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
        dout.writeInt(19); // msg size
        dout.writeByte(inputBytes[8]); //corr id
        dout.writeByte(inputBytes[9]);
        dout.writeByte(inputBytes[10]);
        dout.writeByte(inputBytes[11]);
        if (intVal >= 0 && intVal <= 4) {
            dout.writeShort(0);
        } else {
            dout.writeShort(35);
        }
        dout.writeByte(2); // api_keys array length
        dout.writeShort(18); //api_key
        dout.writeShort(0); //min version
        dout.writeShort(4); // max version
        dout.writeByte(0); //emtpy tag buffer
        dout.writeInt(0); //throttle_time_ms
        dout.writeByte(0); //emtpy tag buffer
        dout.flush();
        return dout;
    }
}

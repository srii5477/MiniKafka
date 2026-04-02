import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

    // TODO: Uncomment the code below to pass the first stage

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     int port = 9092;
     try {
       serverSocket = new ServerSocket(port);
       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);
       // Wait for connection from client.
       clientSocket = serverSocket.accept();
       DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
       byte[] inputBytes = new byte[12];

       int n = in.read(inputBytes);
       System.out.println(Arrays.toString(inputBytes));
       int intVal = ByteBuffer.allocate(2).put(Arrays.copyOfRange(inputBytes, 6, 8)).getInt(0);

       DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
         dout.writeInt(0);
         dout.writeByte(inputBytes[8]);
         dout.writeByte(inputBytes[9]);
         dout.writeByte(inputBytes[10]);
         dout.writeByte(inputBytes[11]);
         if(intVal>=0 && intVal<=4) {
             dout.writeByte(0); dout.writeByte(0);
         } else {
             dout.writeShort(35);
         }
       dout.flush();
       dout.close();
     } catch (IOException e) {
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
}

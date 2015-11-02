import java.io.*;
import java.net.*;

public class TCP
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 1234);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Hello from JAVA TCP Client");

        out.close();
        socket.close();
    }
}

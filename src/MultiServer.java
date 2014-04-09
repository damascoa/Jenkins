import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MultiServer {

	public static void main(String args[]) {
		
		int portNumber = 112;
		int count = 1;
		System.out.println("Inicjacja serwera, port: "+ portNumber);
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber);
			)	{
			System.out.println("Nas³uchiwanie portu..");
			while(true) {
				//new Thread(new ManageSocket(serverSocket.accept())).start();;
				Socket incoming = serverSocket.accept();
				ManageSocket manage = new ManageSocket(incoming, count++);
				Thread th = new Thread(manage);
				th.start();
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		
	}
}

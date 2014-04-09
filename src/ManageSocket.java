import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ManageSocket implements Runnable {

	private Socket clientSocket = null;
	private String path = "D:\\Drive (2011)\\RecivedFiles\\";
	private String name = null;
	
	public ManageSocket(Socket socket, int number) {
		super();
		this.clientSocket = socket;
	}
	@Override
	public void run() {
		try {
			//wczytanie nazwy przesylanego pliku
			InputStream is = clientSocket.getInputStream();
			byte[] buffer = new byte[50];
			is.read(buffer);
			name = new String(buffer).trim();//czyszczenie ze spacji
			path+=name;
			System.out.println("Pobieranie pliku "+name+" w toku.");
			
			Path assumedPath = Paths.get(path);
			//if(Files.deleteIfExists(assumedPath))
			//	System.out.println("Poprzedni plik o nazwie " +assumedPath.getFileName()+" zosta³ skasowany.");
			Files.createFile(assumedPath);
		} catch (IOException e1) {
			System.out.println("Mamy problem.");
			e1.printStackTrace();
		}
		
		try(
				//Strumien zapisu do pliku
				FileOutputStream fos = new FileOutputStream(path);
				//Strumien odczytu danych z socketu
				InputStream is = clientSocket.getInputStream();
		) {
			byte[] buffer = new byte[1024];
			
			while(is.read(buffer) > -1) {
				fos.write(buffer); // zapis do pliku
				fos.flush();
			}
			//proces zamkniecia socketu
			AutoCloseable acsock = clientSocket;
			AutoCloseable acfos = fos;
			AutoCloseable acis = is;
			try {
				acsock.close();
				acfos.close();
				acis.close();
				
				System.out.println("Zakonczono przesylanie pliku: "+name);
			} catch (Exception e) {
				System.out.println("Nie mo¿na zamkn¹æ socketu!");
				e.printStackTrace();
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

}

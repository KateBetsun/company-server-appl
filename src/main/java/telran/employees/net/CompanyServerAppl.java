package telran.employees.net;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.*;
import java.util.Scanner;
import static telran.net.TcpConfigurationProperties.*;

public class CompanyServerAppl {
	
	private static final String FILE_NAME = "employeesTests.data";
	private static final int PORT = 5000;
	
	private static final Object mutex = new Object();

	public static void main(String[] args) {
		
		Company company = new CompanyMapsImpl();
		Persistable persistable = null;
		if(company instanceof Persistable) {
			persistable = (Persistable)company;
		}
		if(persistable != null) {
			try {
			persistable.restore(FILE_NAME);
			} catch (Exception e) {
			
			}
		}		
		Protocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(protocol, PORT);
		//need to start TCPServer as a thread
		Thread serverThread = new Thread(tcpServer);
		serverThread.start();
		boolean running = true;
		//cycle with asking a user to enter shut down for exit from the server
		//regular while cycle with no using cli-view
		//by entering "shutdown" you should call method shutdown of the TcpServer
		//after shutdown you should perform saving the data into the file
		try(Scanner scanner = new Scanner(System.in)){
			while(running) {
				System.out.println(String.format("Enter %s for the server shutdown", 
									SHUTDOWN));
				String line = scanner.nextLine();
				if(line !=null && line.equals(SHUTDOWN)) {
					running = false;
					tcpServer.shutdown();
					if(persistable != null) {
						synchronized (mutex) {
                            try {
                                persistable.save(FILE_NAME);
                            } catch (Exception e) {
                                System.out.println("Error saving data: " + e.getMessage());
                            }
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

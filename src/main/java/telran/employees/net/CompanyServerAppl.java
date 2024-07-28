package telran.employees.net;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.*;

public class CompanyServerAppl {
	
	private static final String FILE_NAME = "employeesTests.data";
	private static final int PORT = 5000;

	public static void main(String[] args) {
		Company company = new CompanyMapsImpl();
		try {
			((Persistable)company).restore(FILE_NAME);
		} catch (Exception e) {
			
		}
		Protocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(protocol, PORT);
		tcpServer.run();

	}

}

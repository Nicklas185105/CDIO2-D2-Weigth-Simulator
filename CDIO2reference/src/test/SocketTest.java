package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import socket.ISocketController;

public class SocketTest {

	@Test
	public void test() throws InterruptedException {
		try {			
			Socket socket = new Socket("localhost",ISocketController.Port);
			OutputStream sos = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(sos);
			pw.println("RM20 4 GED");
			pw.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}

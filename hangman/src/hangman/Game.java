package hangman;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Game implements Runnable{
	private Socket socket;
	private int totalScore=0;
	
	Game(Socket socket)
	{
		this.socket=socket;
	}
	// sends command to client
	private void sendCommand(String command)
	{
		try {
			DataOutputStream out= new DataOutputStream(socket.getOutputStream());
			out.writeBytes(command);
			System.out.println("Command:" + command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	
// main game threat
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Threat started");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(!in.ready())
			{
				System.out.println("Waiting for request");
				Thread.currentThread().yield();
			}
			String cmd = in.readLine();
			System.out.println("Command request received");
			//client can send 3 commands to the server
			while(!cmd.isEmpty())
			{
				 
				switch (cmd) {
				case "Game Start":
					sendCommand(Server.getWord()+"\n");
					cmd = in.readLine();
					break;
				case "Game Over":
					sendCommand("" + totalScore +"\n");
					cmd = in.readLine();
					break;
					
				case"Game Win":
					totalScore ++;
					sendCommand("" + totalScore +"\n");
					cmd = in.readLine();
					break;

				default:
					break;
				}
			}
			Thread.currentThread().yield();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */

	
}

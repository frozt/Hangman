package hangman;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server extends Thread {

	private JFrame frmHangmanServer;
	private JTextField textPort;
	private static int maxConn = 10;
	private static int activeConn = 0;
	private final int defaultPort=9593;
	private static ServerSocket listener;
	private static ArrayList <String> wordList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frmHangmanServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
// loads dictionary at the start of the server
	private static void loadDict()
	{
		  try{
			  // Open the file that is the first 
			  FileInputStream fstream = new FileInputStream("dict.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  wordList = new ArrayList<String>();
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
			  // Add each word to array list
			  wordList.add(strLine);
			  }
			  System.out.println("" + wordList.size());
			  //Close the input stream
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}
// randomly selects a word from the list
	public static String getWord()
	{
		Random generator = new Random();
		int index=generator.nextInt(wordList.size());
		return wordList.get(index);
	}
//listens port and if any connection made start a thread for each connection until the max connection limit
	private static void listen(int Port) throws IOException 
	{
		try
		{
		
		 System.out.println("Port " + Port +" opened.");
		Socket server;
		listener = new ServerSocket(Port);
		loadDict();
//counts the active connections and dont let more conn then the defined maxConn
		while((activeConn++ < maxConn) || (maxConn == 0)){
	        server = listener.accept();
	        System.out.println("Connection accepted");
	        Game game= new Game(server);
	        Thread t = new Thread(game);
	        t.start();
	      }
	    } catch (IOException ioe) {
	      System.out.println("IOException on socket listen: " + ioe);
	      ioe.printStackTrace();
	    }
		
	}
	
	private void initialize() {
		frmHangmanServer = new JFrame();
		frmHangmanServer.setTitle("Hangman Server");
		frmHangmanServer.setBounds(100, 100, 254, 198);
		frmHangmanServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblPortNumber = new JLabel("Port Number:");
		
		textPort = new JTextField();
		textPort.setColumns(10);
		
// Server starts with this action
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
//if any parameter given for port it is selected and if not default port is used for connection
			public void actionPerformed(ActionEvent e) {
				try {
				if(textPort.getText().isEmpty())
					
						listen(defaultPort);					
				else
					listen(Integer.parseInt(textPort.getText()));
				}
				catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}	
			}	
		}
		);
		// not completely implemented yet
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					listener.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnStop.setVisible(false);
		GroupLayout groupLayout = new GroupLayout(frmHangmanServer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnStart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblPortNumber, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(textPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(304, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPortNumber)
						.addComponent(textPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnStop)
						.addComponent(btnStart))
					.addContainerGap(253, Short.MAX_VALUE))
		);
		frmHangmanServer.getContentPane().setLayout(groupLayout);
	}

}

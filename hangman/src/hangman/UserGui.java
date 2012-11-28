package hangman;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.JPanel;

public class UserGui {

	private JFrame frmHangman;
	private JTextField textLeft;
	private JTextField textWord;
	private JTextField textGuess;
	private JTextField textTotalScore;
	private JTextField TextIP;
	private JTextField textPort;
	private JTextArea txtMisses;
	private final int defaultPort=9593;
	private static String word;
	private static String realWord;
	private static Socket socket;
	private final int maxLife=10;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserGui window = new UserGui();
					window.frmHangman.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHangman = new JFrame();
		frmHangman.setTitle("Hangman");
		frmHangman.setBounds(100, 100, 450, 300);
		frmHangman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblLifeLeft = new JLabel("Life Left:");
		
		textLeft = new JTextField();
		textLeft.setText(""+maxLife);
		textLeft.setEditable(false);
		textLeft.setColumns(10);
		
		JLabel lblWord = new JLabel("Word:");
		
		textWord = new JTextField();
		textWord.setEditable(false);
		textWord.setColumns(10);
		
		textGuess = new JTextField();
		textGuess.setColumns(10);
		
//Guess button event 
		final JButton btnGuessLetter = new JButton("Guess");
		btnGuessLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String result = checkGuess(textGuess.getText());
				if(result.equals("Game Over"))
				{
					gameEnd("Game Over");
					btnGuessLetter.setEnabled(false);
					textWord.setText(realWord);
				}
				else if(result.equalsIgnoreCase(realWord))
				{
					gameEnd("Game Win");
					btnGuessLetter.setEnabled(false);
					textWord.setText(result);
				}
				else
					textWord.setText(result);
				textGuess.setText("");
			}
		});
		
		txtMisses = new JTextArea();
		
		JLabel lblTotalScore = new JLabel("Total Score:");
		
		textTotalScore = new JTextField();
		textTotalScore.setEditable(false);
		textTotalScore.setColumns(10);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnGuessLetter.setEnabled(true);
				newGame();
			}
		});
		
		final JPanel ConnectionPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmHangman.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblWord)
													.addGap(23)
													.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
														.addComponent(textGuess, 247, 247, 247)
														.addGroup(groupLayout.createSequentialGroup()
															.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
																.addComponent(textWord, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
																.addGroup(groupLayout.createSequentialGroup()
																	.addGap(94)
																	.addComponent(lblTotalScore)
																	.addPreferredGap(ComponentPlacement.UNRELATED)
																	.addComponent(textTotalScore, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
															.addPreferredGap(ComponentPlacement.RELATED))))
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblLifeLeft)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(textLeft, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
											.addGap(18))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(btnGuessLetter, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
											.addGap(76)))
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnNewGame, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
									.addGap(75)))
							.addGap(18)
							.addComponent(txtMisses, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
							.addGap(28))
						.addComponent(ConnectionPanel, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ConnectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textGuess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(btnGuessLetter)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblWord)
								.addComponent(textWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(16)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblLifeLeft)
								.addComponent(textLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textTotalScore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotalScore))
							.addGap(18)
							.addComponent(btnNewGame))
						.addComponent(txtMisses, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
					.addGap(58))
		);
		
		JLabel lblIP = new JLabel("IP address:");
		
		TextIP = new JTextField();
		TextIP.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		
		textPort = new JTextField();
		textPort.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		
//start button event. First connects to the server and if word is received this panel disables to avoid unnecessary reconnect attempts 
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InetAddress IP;
				int port;
				try {
					if(TextIP.getText().isEmpty())
						IP= InetAddress.getLocalHost();
					else
						IP= InetAddress.getByName(TextIP.getText());
					
					if(textPort.getText().isEmpty())
						port=defaultPort;
					else
						port= Integer.parseInt(textPort.getText());
					
					socket = new Socket(IP,port);
					System.out.println("Connection requested");					
					sendRequest("Game Start\n");
					realWord = waitAnswer().toLowerCase();
					System.out.println(realWord);
					if(!realWord.isEmpty())
						ConnectionPanel.setVisible(false);
					word = "";
					for(int i=0; i<realWord.length();i++)
						word = word + "*";
					textWord.setText(word);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		GroupLayout gl_ConnectionPanel = new GroupLayout(ConnectionPanel);
		gl_ConnectionPanel.setHorizontalGroup(
			gl_ConnectionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ConnectionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblIP)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(TextIP, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPort)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textPort, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnStart)
					.addContainerGap(46, Short.MAX_VALUE))
		);
		gl_ConnectionPanel.setVerticalGroup(
			gl_ConnectionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ConnectionPanel.createSequentialGroup()
					.addGroup(gl_ConnectionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIP)
						.addComponent(TextIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort)
						.addComponent(textPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStart))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		ConnectionPanel.setLayout(gl_ConnectionPanel);
		frmHangman.getContentPane().setLayout(groupLayout);
	}

// send request to the server with the given command. Commands are start of the game or the end of the game
	private void sendRequest(String command)
	{
		try {
			
			DataOutputStream out= new DataOutputStream(socket.getOutputStream());
			out.writeBytes(command);
			System.out.println("Command request sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Connection Error!");
			
		}
	}
// waits for an answer from the server and returns the answer from the server. Answers are the words or total score
	private String waitAnswer()
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Connection Error!");
			return null;
		}
	}
//after each guess this function is called. It check for letter guess and word guess.
	private String checkGuess(String guess)
	{
		guess = guess.toLowerCase();
		StringBuilder tempWord= new StringBuilder(word);
		if(guess.length()==1)
		{
			if(realWord.contains(guess))
			{
				char[] tempRealWord = realWord.toCharArray();
				for(int i=0;i<tempRealWord.length;i++)
				{
					if(tempRealWord[i] == guess.charAt(0))
						tempWord.setCharAt(i, guess.charAt(0));
				}
				word = tempWord.toString();
			}
			else
			{
				int left = Integer.parseInt(textLeft.getText()) -1;
				textLeft.setText("" + left);
				if(txtMisses.getText().isEmpty())
					txtMisses.setText(guess);
				else
					txtMisses.setText(txtMisses.getText() + "\n" + guess);
				if(left == 0)
					return "Game Over";
			}
		}
		else if(guess.length() == realWord.length())
		{
			if(realWord.equalsIgnoreCase(guess))
				word = realWord;
			else
			{
				int left = Integer.parseInt(textLeft.getText()) -1;
				textLeft.setText("" + left);
				if(left == 0)
					return "Game Over";
			}
		}
		else
			JOptionPane.showMessageDialog(null,"Please enter 1 character or guess word with "+realWord.length()+" characters");	
		
		return word;
	}
// when the game ends this function give suited message and handle end game communication to server and display result
	private void gameEnd(String command)
	{
		if(command.equals("Game Win"))
			JOptionPane.showMessageDialog(null,"You won");
		else
			JOptionPane.showMessageDialog(null,"Game Over");
		sendRequest(command+"\n");
		textTotalScore.setText(waitAnswer());
	}
// whenever user want to start a new game this function is called. Request a new game from server and initialize UI
	private void newGame()
	{
		sendRequest("Game Start\n");
		realWord = waitAnswer().toLowerCase();
		System.out.println(realWord);
		word = "";
		for(int i=0; i<realWord.length();i++)
			word = word + "*";
		textWord.setText(word);
		txtMisses.setText("");
		textLeft.setText(""+ maxLife);
	}
}

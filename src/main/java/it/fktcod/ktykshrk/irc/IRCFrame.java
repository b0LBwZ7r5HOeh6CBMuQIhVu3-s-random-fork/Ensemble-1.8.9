package it.fktcod.ktykshrk.irc;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import it.fktcod.ktykshrk.managers.CommandManager;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class IRCFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IRCFrame frame = new IRCFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IRCFrame() {
		setTitle("IRC");
	
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("踢出");
		btnNewButton.setBounds(0, 59, 103, 25);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("在线列表");
		button.setBounds(0, 95, 103, 25);
		contentPane.add(button);
		
		JTextArea txtrName = new JTextArea();
		txtrName.setText("name");
		txtrName.setToolTipText("name");
		txtrName.setBounds(0, 24, 103, 24);
		contentPane.add(txtrName);
		
		
		 btnNewButton.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    CommandManager.getInstance().runCommands("." +"irc"+" kick"+" "+txtrName.getText());
	            

	                } catch (Exception var5) {
	                    var5.printStackTrace();

	                }


	            }
	        });
		 
		 
		 
		 button.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    CommandManager.getInstance().runCommands("." +"irc"+" list");


	                } catch (Exception var5) {
	                    var5.printStackTrace();

	                }


	            }
	        });
	}
}

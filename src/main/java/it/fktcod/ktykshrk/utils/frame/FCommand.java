package it.fktcod.ktykshrk.utils.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
public class FCommand extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FCommand frame = new FCommand();
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
    public FCommand() {
        setTitle("Command Frame Fix By Zenwix");
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("Fill in commands");
        textArea.setBounds(0, 39, 434, 24);
        contentPane.add(textArea);

        JButton btnNewButton = new JButton("Run");
        btnNewButton.setBounds(10, 74, 103, 25);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ////System.out.println("Command system has been opened");
                try {
                    String message = textArea.getText();
                    CommandManager.getInstance().runCommands("." + message);
                    ChatUtils.message("run...");


                } catch (Exception var5) {
                    var5.printStackTrace();

                }


            }
        });
    }
}



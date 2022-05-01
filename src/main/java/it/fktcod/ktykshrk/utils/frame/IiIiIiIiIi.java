package it.fktcod.ktykshrk.utils.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import it.fktcod.ktykshrk.utils.inject.IIIIIIIIII;
import it.fktcod.ktykshrk.utils.system.ClipBoardUtils;

import javax.swing.JTextField;

public class IiIiIiIiIi extends JFrame {

	private JPanel contentPane;
	private JTextField txthwid;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IiIiIiIiIi frame = new IiIiIiIiIi();
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
	public IiIiIiIiIi() {
		setTitle("ENSEMBLE\u9A8C\u8BC1\u7CFB\u7EDF");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txthwid = new JTextField();
		txthwid.setText("\u60A8\u5C1A\u672A\u83B7\u53D6\u51ED\u8BC1\uFF0C\u8BF7\u52A0\u7FA4975302793\uFF0C\u60A8\u7684HWID\u4E3A\uFF1A");
		txthwid.setBounds(27, 79, 378, 59);
		contentPane.add(txthwid);
		txthwid.setColumns(10);
		
		textField = new JTextField();
		try {
			textField.setText(IIIIIIIIII.getHWID());
			ClipBoardUtils.setSysClipboardText(IIIIIIIIII.getHWID());
		} catch (NoSuchAlgorithmException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		textField.setBounds(27, 153, 378, 29);
		contentPane.add(textField);
		textField.setColumns(10);
	}
}

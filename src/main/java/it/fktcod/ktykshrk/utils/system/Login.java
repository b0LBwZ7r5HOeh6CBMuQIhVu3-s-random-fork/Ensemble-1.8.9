package it.fktcod.ktykshrk.utils.system;

import com.google.gson.JsonParser;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.EventsEngine;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.managers.HackManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Login extends JFrame {

    public static String Cloudurl;
    public static String ReturnData;

    private static JsonParser jsonParser = new JsonParser();

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void Client(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
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
    public Login() {
        setTitle("Ensemble Login");
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("Fill With Your UserName");
        textArea.setBounds(0, 10, 434, 24);
        contentPane.add(textArea);

        JTextArea textArea2 = new JTextArea();
        textArea2.setBackground(Color.WHITE);
        textArea2.setToolTipText("Fill With Your Password");
        textArea2.setBounds(0, 40, 434, 24);
        contentPane.add(textArea2);

        JButton btnNewButton = new JButton("Login");
        btnNewButton.setBounds(10, 74, 103, 25);
        contentPane.add(btnNewButton);


        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String UserName = textArea.getText();
                String Password = textArea2.getText();
                String Hwid = getHdSerialInfo();
                String QQ = getQQ();
                String UserInfo = "LOG="+UserName+"="+Password+"="+getHdSerialInfo()+"="+getQQ();

                ////System.out.println(UserName);
                ////System.out.println(Password);
                ////System.out.println(QQ);
                ////System.out.println(Hwid);
                ////System.out.println(UserInfo);

                String ReturnInfo = Send("101.132.134.120",27321,UserInfo);

                String[] RSZ = ReturnInfo.split("=");

                switch (RSZ.length) {
                    case 2:{
                        ////System.out.println("case 2");
                        ////System.out.println(ReturnInfo);
                        ////System.out.println(RSZ[1].hashCode());
                        switch (RSZ[1].hashCode()){
                            case 3541570:{
                                JOptionPane.showMessageDialog(null,"Logged Successfully","Ensemble",JOptionPane.INFORMATION_MESSAGE);

                                try {
									Core.fontManager=new FontManager();
								} catch (Exception e1) {
									// TODO �Զ����ɵ� catch ��
									e1.printStackTrace();
								}
                                Core.hackManager = new HackManager();
                                Core.fileManager = new FileManager();
                                Core.eventsEngine = new EventsEngine();
                                Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Core.eventsEngine);
                                Nan0EventRegister.register(FMLCommonHandler.instance().bus(), Core.eventsEngine);

                            }
                            case 88481237:{
                                JOptionPane.showMessageDialog(null,RSZ[1],"Ensemble",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }

                        if (RSZ[1].hashCode() == 3541570){}
                        else{JOptionPane.showMessageDialog(null,RSZ[1],"Ensemble",JOptionPane.INFORMATION_MESSAGE);}

                        break;
                    }
                    case 3:{
                        ////System.out.println("case 3");
                        ////System.out.println(ReturnInfo);
                        ////System.out.println(RSZ[2].hashCode());

                        if(RSZ[2].hashCode() == 1201167282){
                            JOptionPane.showMessageDialog(null,"Password Failed(0x86FF)","Ensemble",JOptionPane.ERROR_MESSAGE);
                        }
                        else if(RSZ[2].hashCode() == 2049842967){
                            JOptionPane.showMessageDialog(null,"Account Time is Out(0x82FF)","Ensemble",JOptionPane.ERROR_MESSAGE);
                        }
                        else if(RSZ[2].hashCode() == -1880301345){
                            JOptionPane.showMessageDialog(null,"Account Time is Out(0x82FF)","Ensemble",JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"Account Has Been Locked(0x73FF)","Ensemble",JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }

            }
            });
    }

    public static String getQQ() {
        String R2 = "}";
        String ZZ = new String(String.valueOf(QQUtils.getLoginQQList()));
        String Z[] = ZZ.split("=",2);
        String QQDT = Z[1];
        String QQNum = QQDT.replaceAll(R2,"");
        return QQNum;
    }

    public static String getHdSerialInfo() {

        String line = "";
        String HdSerial = "";
        try {
            Process proces = Runtime.getRuntime().exec("cmd /c dir c:");
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(proces.getInputStream(),"gbk"));

            while ((line = buffreader.readLine()) != null) {
                if (line.indexOf("������к��� ") != -1) { 

                    HdSerial = line.substring(line.indexOf("������к��� ") + "������к��� ".length(), line.length());
                    HdSerial=HdSerial.replaceAll("-","");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return HdSerial;
    }

    public static String Send(String IP, int Port, String Message) {

        try{
            Socket socket = new Socket(IP, Port);

            OutputStream ops = socket.getOutputStream();

            OutputStreamWriter opsw = new OutputStreamWriter(ops);

            BufferedWriter bw = new BufferedWriter(opsw);

            bw.write(Message);

            bw.flush();

            InputStream ips = socket.getInputStream();

            InputStreamReader ipsr = new InputStreamReader(ips);

            BufferedReader br = new BufferedReader(ipsr);

            String s = null;

            while((s = br.readLine()) != null){
                return s;
            }

            socket.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed Connect to The Server(0x66FF)","Ensemble",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

}


package it.fktcod.ktykshrk.irc;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.click.theme.Theme;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.AvaritiaColorUtil;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.EnumChatFormatting;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import static it.fktcod.ktykshrk.Core.getSubString;
import scala.reflect.internal.Trees.This;

import static it.fktcod.ktykshrk.utils.inject.IIIIIIIIII.getHWID;

public class IRCChat extends Module {

    public static boolean isdev = false;
    public static OutputStream out;
    public BufferedReader reader;
    public static Socket socket;
    public static PrintWriter pw;
    static InputStreamReader in;
    public static int checkid = 3;
    private static String prefix = "";
    public static List<String> banned;
    public static String FGF = "r@safucku@uuense@";
    public static Thread thread;
    public TimerUtils timerUtils = new TimerUtils();
    public static TimerUtils reconTimerUtils = new TimerUtils();

    public IRCChat() {
        super("IRCChat", HackCategory.ANOTHER);
        this.setToggled(false);
        //this.onEnable();

    }

    @Override
    public void onEnable() {
        if(thread==null) {
            thread = new IRCTheard();
            thread.start();
        }
        super.onEnable();
    }



    private static int sec = 1;

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }

    public static void send(String TEXT) {
        try {
            // ////System.out.println(TEXT);
            out.write(TEXT.getBytes("GBK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPacket(Object packet, Side side) {

        boolean send = true;
        if (side == Side.OUT) {
            if (packet instanceof C01PacketChatMessage) {

                Field field = ReflectionHelper.findField(C01PacketChatMessage.class,
                        new String[] { "message", "field_149440_a" });

                try {

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    if (packet instanceof C01PacketChatMessage) {
                        C01PacketChatMessage p = (C01PacketChatMessage) packet;

                        if (p.getMessage().subSequence(0, 1).equals("+")) {

                            System.out.println(BuildMessageString(p.getMessage().substring(1, p.getMessage().length())));
                            out.write((BuildMessageString(p.getMessage().substring(1, p.getMessage().length()))).getBytes("GBK"));
                            send = false;
                            return send;
                        } else {
                            send = true;
                        }
                    }

                } catch (Exception e) { // e.printStackTrace();

                }

            }
        }
        return send;
    }

    @Override
    public void onDisable() {
        thread.interrupt();
        thread.stop();
        thread=null;
        ChatUtils.error("You have no permission to close this module");
    }

    public static void connect() {

        ChatUtils.warning("Try to connect to the server...");
        try {

            String Text = BuildConnectString(Core.UID,Core.UN,Core.UP);
            socket = new Socket("124.222.57.187", 19888);
            //in = socket.getInputStream();
            in = new InputStreamReader(socket.getInputStream(), "GBK");

            out = socket.getOutputStream();
            pw = new PrintWriter(socket.getOutputStream(), true);
            // ////System.out.println(socket);
            // ////System.out.println(in);
            // ////System.out.println(out);
            // ////System.out.println(pw);

            // ////System.out.println(Text);
            //ChatUtils.message("Connection is Successful!");
            //ChatUtils.message("+号 消息 发送irc消息");
            checkid = 9999;
            out.write((Text).getBytes("GBK"));



        } catch (IOException  e) {
            thread = null;
            ChatUtils.error("Disconnect from IRC!");
            checkid = checkid+1;
            //HackManager.getHack("IRCChat").setToggled(false);
            //HackManager.getHack("IRCChat").onDisable();
            e.printStackTrace();
        }
    }
    public static String BuildConnectString(String UID,String Username,String Password){
        try {
            String UIDW = "[USERID]["+UID+"][UserName][";
            String UsernameW =  Username+"][Password][";
            String PasswordW = Password+"][USERHWID][";
            String HWIDW = Core.getHWID(true)+"][GameID][";
            String GAMENAME = mc.thePlayer.getName()+"]";
            String BuildString = "[Target][CONNECT]"+UIDW+UsernameW+PasswordW+HWIDW;
            return BuildString;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String BuildMessageString(String Message){

        String MessageW = "[String]["+Message+"]";
        String BuildString = "[Target][MESSAGE]"+MessageW;
        return BuildString;

    }
    public static void handleInput() {
        char[] data = new char[1024];
        try {
            int len = in.read(data);
            String ircmessage = new String(data, 0, len);
            ircmessage = ircmessage.replaceAll("\n", "");
            ircmessage = ircmessage.replaceAll("\r", "");
            ircmessage = ircmessage.replaceAll("\t", "");

            System.out.println(ircmessage);
            if (checkid != 1){
                String result = getSubString(ircmessage,"[result]","[");

                //System.out.println(result);
                ////System.out.println(userid);
                // [鍚堝]璁稿彲璇佷笉瀛樺湪 -964859282
                // [鍚堝]璁稿彲璇侀獙璇佹垚鍔� 749547859
                // [鍚堝]鑷姩鎹㈢粦鎴愬姛 -407416329
                // 鎹㈢粦澶辫触 789885443
                //System.out.println(result.hashCode());
                switch (result.hashCode()) {
                    case 883338842:
                        ChatUtils.message("Connection is Successful!");
                        ChatUtils.message("+号 消息 发送irc消息");
                        checkid = 1;
                        break;
								/*
								String CNX = WebUtils.get("http://121.43.230.232/1.html");
								//System.out.println(CNX);
								CNX = CNX.replaceAll("<meta charset=\"utf-8\">","");
								Translate_CN = CNX.split("=");
								 */
                    default:
                        String reason = getSubString(result,"[reason]","");
                        //System.out.println("Minecraft 1.8.9 ×" + reason);
                        //new Cr4sh();
                        break;

                }

            }



            if (ircmessage.equals("Crash")) {
                mc.shutdown();
            }

            if (ircmessage.equals("HeartBeat Packet")) {
                out.write(("[Packet][HeartBeat]"+"[hwid]["+Core.getHWID(true)+"]").getBytes("GBK"));
            }
            // ////System.out.println(ircmessage);
            // ////System.out.println(sz1.length);w
            // ////System.out.println(sz1);

            if (ircmessage.contains("[UserName]") && ircmessage.contains("[String]")) {
                String Userrank = getSubString(ircmessage,"[UserRank][","][UserName][");
                String Username = getSubString(ircmessage,"[UserName][","][String][");
                String message = getSubString(ircmessage,"[String][","]");
                //System.out.println(Username);
                //System.out.println(message);

                Wrapper.INSTANCE.mc().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                        EnumChatFormatting.LIGHT_PURPLE + "[IRC] " +EnumChatFormatting.GOLD + "[" + EnumChatFormatting.AQUA + Userrank + EnumChatFormatting.GOLD
                                + "]" + EnumChatFormatting.DARK_AQUA + Username+": " + EnumChatFormatting.WHITE + message ));

            }

        } catch (Exception e) {

            /*
             * if (reconTimerUtils.hasReached(5000)) { thread.interrupt();
             * HackManager.getHack("IRCChat").setToggled(false);
             * ChatUtils.warning("reconnected!"); reconTimerUtils.reset(); }
             */
        }
    }

    public static String gbk_bytes_to_string(byte[] data) {
        int i;
        String s = "";

        for (i = 0; i < data.length; ++i) {
            if (data[i] == 0) {
                break;
            }
        }

        byte[] temp = new byte[i];
        System.arraycopy(data, 0, temp, 0, temp.length);

        try {
            s = new String(temp, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s;
    }

}

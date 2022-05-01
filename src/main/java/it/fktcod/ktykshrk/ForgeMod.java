
package it.fktcod.ktykshrk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.LogManager;

import javax.swing.JOptionPane;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.managers.NotificationManager;
import it.fktcod.ktykshrk.module.Notification;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.frame.FCommand;
import it.fktcod.ktykshrk.utils.frame.IiIiIiIiIi;
import it.fktcod.ktykshrk.utils.inject.Agent;
import it.fktcod.ktykshrk.utils.inject.IIIIIIIIII;
import it.fktcod.ktykshrk.utils.math.fps.FPSCore;

import it.fktcod.ktykshrk.utils.system.Login;
import it.fktcod.ktykshrk.utils.system.Nan0EventRegister;
import it.fktcod.ktykshrk.utils.system.QQUtils;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static it.fktcod.ktykshrk.LoadClient.RLoad;

@Mod(modid = ForgeMod.MODID, name = ForgeMod.NAME, version = ForgeMod.VERSION, acceptableRemoteVersions = "*")
public class ForgeMod {

	public static ForgeMod forgeMod;
	public static final String MODID = "u143156";
	//Margele L AntiCheat
	public static final String NAME = "Netease";
	public static final String VERSION = "";
	public static final String MCVERSION = "1.8.9";
	public static final String DATE = "#210708";
	public static int initCount = 0;
	public static HackManager hackManager;
	public static FileManager fileManager;
	public static EventsEngine eventsEngine;
	public static FontManager fontManager;
	public static String Pass;
	
	

	public static int iloveu = 250;
	public static boolean init = false;
	public static boolean obf;
	public static boolean chinese;
	public static NotificationManager notificationManager;
	public static String user;
	public static FPSCore fpsCore;

	public static boolean state = false;

	public ForgeMod() throws Exception {
		init(null);
	}
	
	@Mod.EventHandler
	private void preInit(FMLPreInitializationEvent E) {
	}

	@Mod.EventHandler
	private static void init(FMLInitializationEvent E)  {
		if (init){return;}
		init = true;
		RLoad("","");
	}

	public static void fuck(){
		System.out.println("RNM");
	}
	public static ForgeMod instance() {

		//net.minecraftforge.fml.common.Loader.loadMods();
		return forgeMod;
	}

	/*
	 * @Mod.EventHandler private static void init(FMLInitializationEvent E) throws
	 * IOException, NoSuchAlgorithmException {
	 * 
	 * if (initCount > 0) { return; } init = true; iloveu = 666;
	 * 
	 * user="Ensemble Tester";
	 * 
	 * fontManager = new FontManager(); notificationManager=new
	 * NotificationManager(); // fontManager2 =new FontManager2();
	 * 
	 * hackManager = new HackManager(); fileManager = new FileManager();
	 * eventsHandler = new EventsHandler();
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Main.eventsHandler);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(),
	 * Main.eventsHandler); Display.setTitle("| Ensemble |"+Main.VERSION+
	 * " Welcome!");
	 * 
	 * initCount++;
	 * 
	 * }
	 */



	/*
	 * @Mod.EventHandler public static void init(FMLInitializationEvent E) throws
	 * IOException, NoSuchAlgorithmException {
	 * 
	 * if (initCount > 0) { return; } init = true;
	 * 
	 * iloveu = 666; JOptionPane.showMessageDialog(null, "Logged");
	 * Display.setTitle("| Ensemble | Loading..."); fontManager = new FontManager();
	 * hackManager = new HackManager(); Send("Stage=5"); fileManager = new
	 * FileManager(); Send("Stage=6"); eventsHandler = new EventsHandler();
	 * Send("Stage=7"); combo = new Combo(); Send("Stage=8");
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsHandler);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsHandler);
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, combo);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), combo);
	 * Send("Stage=9"); Display.setTitle("| Ensemble | Loaded |"); Send("Stage=10");
	 * Send("Stage=1"); Send("Stage=2"); String UserName = "LOG=" +
	 * JOptionPane.showInputDialog(null, " Please Type Your UserName:\n",
	 * "Ensemble", JOptionPane.PLAIN_MESSAGE); String Password =
	 * JOptionPane.showInputDialog(null, " Please Type Your Password:\n",
	 * "Ensemble", JOptionPane.PLAIN_MESSAGE);
	 * 
	 * try { Display.setTitle("| Ensemble | Connecting...");
	 * 
	 * Socket socket = new Socket("101.132.134.120", 13330);
	 * 
	 * OutputStream os = socket.getOutputStream(); PrintWriter pw = new
	 * PrintWriter(os);
	 * 
	 * InputStream is = socket.getInputStream(); BufferedReader br = new
	 * BufferedReader(new InputStreamReader(is));
	 * 
	 * String info = UserName + "=" + Password + "=" + getHWID() + "=" + getQQ();
	 * Send("Stage=3"); pw.write(info); pw.flush(); socket.shutdownOutput();
	 * 
	 * String reply = null; while (!((reply = br.readLine()) == null)) {
	 * ////System.out.println("CQEU23E9WDUHWDUHWD8UWHGD8UWHDFIUWHFUEWHFUIEAHF8UEAH:" +
	 * reply); switch (reply) { case "PASSED": Send("Stage=4"); iloveu = 666;
	 * JOptionPane.showMessageDialog(null, "Logged");
	 * Display.setTitle("| Ensemble | Loading..."); hackManager = new HackManager();
	 * Send("Stage=5"); fileManager = new FileManager(); Send("Stage=6");
	 * eventsHandler = new EventsHandler(); Send("Stage=7"); combo = new Combo();
	 * Send("Stage=8"); Nan0EventRegister.register(MinecraftForge.EVENT_BUS,
	 * eventsHandler); Nan0EventRegister.register(FMLCommonHandler.instance().bus(),
	 * eventsHandler); Nan0EventRegister.register(MinecraftForge.EVENT_BUS, combo);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), combo);
	 * Send("Stage=9"); Display.setTitle("| Ensemble | Loaded |"); Send("Stage=10");
	 * break; case "DIS": iloveu = 666;
	 * Display.setTitle("| Ensemble | Checked Failed | " + getHWID()); break; } }
	 * 
	 * br.close(); is.close(); pw.close(); os.close(); socket.close(); } catch
	 * (UnknownHostException e) { e.printStackTrace(); } catch (IOException e) {
	 * e.printStackTrace(); } initCount++;
	 * 
	 * }
	 */
	@Mod.EventHandler
	private void postInit(FMLPostInitializationEvent E) {
	}

	/*
	 * public static void login2() throws NoSuchAlgorithmException {
	 * 
	 * //bypass1
	 * 
	 * 
	 * Send("Stage=2"); String UserName = "LOG=" + JOptionPane.showInputDialog(null,
	 * " Please Type Your UserName:\n", "Ensemble", JOptionPane.PLAIN_MESSAGE);
	 * String Password = JOptionPane.showInputDialog(null,
	 * " Please Type Your Password:\n", "Ensemble", JOptionPane.PLAIN_MESSAGE);
	 * 
	 * try { Display.setTitle("| Ensemble | Connecting...");
	 * 
	 * Socket socket = new Socket("101.132.134.120", 13330);
	 * 
	 * OutputStream os = socket.getOutputStream(); PrintWriter pw = new
	 * PrintWriter(os);
	 * 
	 * InputStream is = socket.getInputStream(); BufferedReader br = new
	 * BufferedReader(new InputStreamReader(is));
	 * 
	 * String info = UserName + "=" + Password + "=" + getHWID()+"="+getQQ();
	 * Send("Stage=3"); Display.setTitle("| Ensemble | Checking...");
	 * pw.write(info); pw.flush(); socket.shutdownOutput();
	 * 
	 * String reply = null; while (!((reply = br.readLine()) == null)) {
	 * ////System.out.println("CQEU23E9WDUHWDUHWD8UWHGD8UWHDFIUWHFUEWHFUIEAHF8UEAH:" +
	 * reply); switch (reply) { case "PASSED": Send("Stage=4"); iloveu=666;
	 * JOptionPane.showMessageDialog(null, "Logged");
	 * Display.setTitle("| Ensemble | Loading..."); hackManager = new HackManager();
	 * Send("Stage=5"); fileManager = new FileManager(); Send("Stage=6");
	 * eventsHandler = new EventsHandler(); Send("Stage=7"); combo = new Combo();
	 * Send("Stage=8"); Nan0EventRegister.register(MinecraftForge.EVENT_BUS,
	 * eventsHandler); Nan0EventRegister.register(FMLCommonHandler.instance().bus(),
	 * eventsHandler); Nan0EventRegister.register(MinecraftForge.EVENT_BUS, combo);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), combo);
	 * Send("Stage=9"); Display.setTitle("| Ensemble | Loaded |"); Send("Stage=10");
	 * break; case "DIS": iloveu=666;
	 * Display.setTitle("| Ensemble | Checked Failed | " + getHWID()); break; } }
	 * 
	 * br.close(); is.close(); pw.close(); os.close(); socket.close(); } catch
	 * (UnknownHostException e) { e.printStackTrace(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * public static void login() throws IOException, NoSuchAlgorithmException {
	 * String username = JOptionPane.showInputDialog("Type Your UserName"); String
	 * password = JOptionPane.showInputDialog("Type Your Password"); String HWID =
	 * getHWID();
	 * 
	 * if (username == null) { JOptionPane.showMessageDialog(null,
	 * "The UserName cannot be empty!", "Checker", JOptionPane.ERROR_MESSAGE); }
	 * else { if (password == null) { JOptionPane.showMessageDialog(null,
	 * "The UserName cannot be empty!", "Checker", JOptionPane.ERROR_MESSAGE); }
	 * 
	 * else { String Data = username + "=" + password + "=" + HWID;
	 * 
	 * String BACK = WebUtils.get("http://checker.dpro.site/");
	 * ////System.out.println(BACK);
	 * 
	 * if (BACK.contains(username + "=" + password + "=" + HWID)) {
	 * JOptionPane.showMessageDialog(null, "Logged Successfully", "Checker",
	 * JOptionPane.INFORMATION_MESSAGE); hackManager = new HackManager();
	 * Send("Stage=5"); fileManager = new FileManager(); Send("Stage=6");
	 * eventsHandler = new EventsHandler(); Send("Stage=7");
	 * 
	 * combo = new Combo();
	 * 
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsHandler);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsHandler);
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, combo);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), combo);
	 * Send("Stage=8");
	 * 
	 * } else { JOptionPane.showMessageDialog(null, "Logged Failed", "Checker",
	 * JOptionPane.ERROR_MESSAGE); Utils.copy(HWID);
	 * JOptionPane.showMessageDialog(null,
	 * "Already Copied Your HWID to Your Clipboard", "Checker",
	 * JOptionPane.ERROR_MESSAGE); } } }
	 * 
	 * }
	 */

	/*
	 * public void Inject() throws NoSuchAlgorithmException, IOException { if
	 * (initCount > 0) { return; } init = true; main = this; try {
	 * Minecraft.getMinecraft().getClass().getField("func_71410_x"); obf = true; }
	 * catch (NoSuchFieldException e) { obf = false; } Main.iloveu = 666;
	 * this.fontManager = new FontManager(); this.hackManager = new HackManager();
	 * this.fileManager = new FileManager(); this.eventsHandler = new
	 * EventsHandler(); Display.setTitle("| Ensemble | GHOST");
	 * Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsHandler);
	 * Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsHandler);
	 * 
	 * initCount++; }
	 */
	



	public static String Send(String IP, int Port, String Message) {

		try {
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

			while ((s = br.readLine()) != null) {
				return s;
			}

			socket.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed Connect to The Server(0x66FF)", "Ensemble",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}

}

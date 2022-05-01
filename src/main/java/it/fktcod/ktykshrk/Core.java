package it.fktcod.ktykshrk;

import it.fktcod.ktykshrk.managers.*;
import it.fktcod.ktykshrk.utils.Cr4sh;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import it.fktcod.ktykshrk.ui.font.FontLoaders;
import it.fktcod.ktykshrk.utils.system.Nan0EventRegister;
import it.fktcod.ktykshrk.utils.system.QQUtils;

public class Core {
	public static Core instance;
	public static boolean state = false;
	public String IP = "NULL";
	public static int Verify ;
	public static final String MODID = "u143156";
	public static String NAME = "NetEase";
	public static final String VERSION = "4.0.8";
	public static final String MCVERSION = "1.8.9";
	public static String[] Translate_CN;
	public static HackManager hackManager;
	public static FileManager fileManager;
	public static EventsEngine eventsEngine;
	public static FontManager fontManager;
	public static FontLoaders fontLoaders;
	public static NotificationManager notificationManager;
	public static ScriptManager scriptManager;
	public static Logger logger;
	public static int iloveu = 250;
	public static boolean init = false;
	public static boolean inject = false;

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
			Display.setTitle("Failed Connect to The Server(0x66FF)");
			e.printStackTrace();
		}
		return null;
	}

	public Core() { NativeLoaderX(); }

	public static String LicenceCodeX;
	public static String UID;
	public static String UN;
	public static String UP;
	public static String TrueTitle;

	public void NativeLoaderX() {

		if (state)
			return;

		if (!LoadClient.isCheck){
			new Cr4sh();
			return;
		}

		//File file = new File("C:\\Windows\\exs_server_ip.rsa");

		state = true;
		instance = this;
		String pathname = "C:\\user.ensecode";
		String LicenceCode = null;

		try (FileReader reader = new FileReader(pathname); BufferedReader bfr = new BufferedReader(reader)) {
			while ((LicenceCode = bfr.readLine()) != null) {

				Socket socket = new Socket("124.222.57.187", 19730);

				OutputStream ops = socket.getOutputStream();

				OutputStreamWriter opsw = new OutputStreamWriter(ops);

				BufferedWriter bw = new BufferedWriter(opsw);

				LicenceCodeX = LicenceCode;

				UID = getSubString(LicenceCode, "[USERID][", "][UserName][");
				UN =getSubString(LicenceCode,"[UserName][","][Password][");
				UP =getSubString(LicenceCode,"][Password][","]");
				String BuildS = BuildLoginString(UID,UN,UP);
				bw.write(BuildS);
				bw.flush();

				InputStream ips = socket.getInputStream();

				InputStreamReader ipsr = new InputStreamReader(ips, "GBK");

				BufferedReader br = new BufferedReader(ipsr);

				String s = null;


				while ((s = br.readLine()) != null) {

					//System.out.println(s);

					String result = getSubString(s,"[result]","[");

					//System.out.println(result);
					Verify = result.hashCode();
					//System.out.println(String.valueOf(String.valueOf(String.valueOf(Verify).hashCode()).hashCode()).hashCode());
					switch (result.hashCode()) {
						case 883338842:

							setOBF();
							String hwid = getSubString(s,"[hwid]","[");
							//System.out.println(hwid);
							if (hwid.contains("rnmEXSV406"+getHWID(true))){
								//NAME = "Ensemble";
								String CNX = WebUtils.get("http://2018k.cn/api/getExample?id=4e4826a9127a46b0b9558dfef3b7d8a4&data=notice");
								////System.out.println(CNX);
								CNX = CNX.replaceAll("<meta charset=\"utf-8\">","");
								Translate_CN = CNX.split("=");

								init = true;
								iloveu = 666;
								logger = LogManager.getLogger();
								fontManager = new FontManager();
								fontLoaders = new FontLoaders();
								hackManager = new HackManager();
								fileManager = new FileManager();
								eventsEngine = new EventsEngine();
								notificationManager = new NotificationManager();

								Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsEngine);
								Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsEngine);
								//user = getQQ();
								//scriptManager = new ScriptManager();

								TrueTitle = Display.getTitle();
								Display.setTitle("Minecraft 1.8.9 √" + UN);


								//Class MAC = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");


								//Testers

								//ClassLoader.getSystemClassLoader().loadClass(EntityPlayerSP.class.getName());
								//final Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
								//declaredMethod.invoke()

								break;
							}

							String reasonX = getSubString(s,"[reason]","");
							//System.out.println("Minecraft 1.8.9 ×" + reasonX);
							new Cr4sh();






							break;
						default:
							String reason = getSubString(s,"[reason]","");
							//System.out.println("Minecraft 1.8.9 ×" + reason);
							new Cr4sh();
							break;

					}
				}

				socket.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public static byte[] getClassByteCode(String className) {
		String jarname = "/" + className.replace('.', '/') + ".class";
		InputStream is = Core.class.getResourceAsStream(jarname);

		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		byte imgdata[] = null;
		try {
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			imgdata = bytestream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bytestream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imgdata;
	}


	private static String readtxt(String fileName) {
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			////System.out.println("Reading text file using FileReader");
			while((line = br.readLine()) != null){
				return line;
			}
			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/java/java-read-text-file.html

	public static Core INSTANCE() {
		return instance;
	}

	/*
	 * public static void inject() throws Exception { if (INSTANCE() == null) { try
	 * { instance = new Client(); } catch (NoSuchAlgorithmException |
	 * UnsupportedEncodingException e) { e.printStackTrace(); } } }
	 */

	public void ReCheck() {
		String pathname = "C:\\user.ensecode";
		String LicenceCode = null;

		try (FileReader reader = new FileReader(pathname); BufferedReader bfr = new BufferedReader(reader)) {
			while ((LicenceCode = bfr.readLine()) != null) {

				try {
					Socket socket = new Socket("124.222.57.187", 19730);

					OutputStream ops = socket.getOutputStream();

					OutputStreamWriter opsw = new OutputStreamWriter(ops);

					BufferedWriter bw = new BufferedWriter(opsw);

					bw.write("LOG" + "r@safucku@uuense@" + LicenceCode + "r@safucku@uuense@" + Core.getHWID(true));
					bw.flush();

					InputStream ips = socket.getInputStream();

					InputStreamReader ipsr = new InputStreamReader(ips, "GBK");

					BufferedReader br = new BufferedReader(ipsr);

					String s = null;

					////System.out.println("s = " + s);
					while ((s = br.readLine()) != null) {
						String[] sz = s.split("r@safucku@uuense@");

						// 楠岃瘉鎴愬姛 1213832196
						// 楠岃瘉澶辫触 1213776777
						// 鎹㈢粦鎴愬姛 789940862
						// 鎹㈢粦澶辫触 789885443

						switch (sz[0].hashCode()) {
						case 1213776777:
							new Cr4sh();;
							break;
						case 789940862:
							new Cr4sh();
							break;
						case 789885443:
							break;
						case 1213832196:
							break;
						}
					}

					socket.close();
				} catch (Exception e) {
					Display.setTitle("Failed Connect to The Server(0x66FF)");
					e.printStackTrace();
				}

			}
		} catch (IOException e) {

			Minecraft.getMinecraft().shutdown();
			e.printStackTrace();
		}
	}

	public static String getHWID(boolean isMD5) throws IOException, NoSuchAlgorithmException {
		String property = System.getProperty("os.name").toLowerCase();
		String cpuSerialNumber = getCPUSerialNumber();
		String hardDiskSerialNumber = InetAddress.getLocalHost().getHostName().toString();
		////System.out.println(hardDiskSerialNumber);
		String serial = cpuSerialNumber+hardDiskSerialNumber;
		if (isMD5) {
			MessageDigest messageDigest = null;
			messageDigest = MessageDigest.getInstance("MD5");
			byte[] ciphertext = messageDigest.digest(serial.getBytes());
			return Hex.encodeHexString(ciphertext);
		} else {
			return serial;
		}

	}

	public static String getCPUSerialNumber() {
		String serial;
		try {
			Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
			process.getOutputStream().close();
			Scanner sc = new Scanner(process.getInputStream());
			serial = sc.next();
			serial = sc.next();
		} catch (IOException e) {
			throw new RuntimeException("获取CPU序列号失败");
		}
		return serial;
	}

	public static String getQQ() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String R2 = "}";
		String ZZ = new String(String.valueOf(QQUtils.getLoginQQList()));
		String Z[] = ZZ.split("=", 2);
		String QQDT = Z[1];
		//////System.out.println(QQDT);
		String QQNum = QQDT.replaceAll(R2, "");
		////System.out.println("USERQQ:" + QQNum);
		return QQNum;
	}

	public static String BuildLoginString(String UID,String Username,String Password){
		try {
			String UIDW = "[USERID]["+UID+"][UserName][";
			String UsernameW =  Username+"][Password][";
			String PasswordW = Password+"][USERHWID][";
			String HWIDW = getHWID(true)+"]";
			String BuildString = "[Target][LOGIN]"+UIDW+UsernameW+PasswordW+HWIDW;
			return BuildString;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String BuildGetClassString(String Type){
		try {
			String TypeW = "[TYPE]["+Type+"][USERHWID][";
			String HWIDW = getHWID(true)+"]";
			String BuildString = "[Target][GETCLASS]"+TypeW+HWIDW;
			return BuildString;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}
	public static boolean isObfuscate=false;
	public void setOBF(){
		instance = this;
		Field F;
		try {
			F= S18PacketEntityTeleport.class.getDeclaredField("field_149456_b");
			isObfuscate=true;
		} catch (NoSuchFieldException ex) {
			try {
				F= S18PacketEntityTeleport.class.getDeclaredField("posX");
				isObfuscate=false;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}
}
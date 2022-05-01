package it.fktcod.ktykshrk.command;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.Notification;
import it.fktcod.ktykshrk.module.Notification.Type;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;
import org.lwjgl.opengl.Display;

public class Config extends Command {
	public static File configs;
	public static URL configurl;
	private static JsonParser jsonParser = new JsonParser();
	private static Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

	public Config() {
		super("config");
	}

	@Override
	public void runCommand(String s, String[] args) {
		if (args[0].equals("reload")) {
			Core.fileManager = new FileManager();
		}
		if (args[0].equals("frame")) {
			//cn.zenwix.ensemble.utils.frame.Config.main(null);
		} else if (args[0].equals("save") && args[1] != null) {
			if (!FileManager.CONFIG.exists())
				FileManager.CONFIG.mkdir();
			configs = new File(FileManager.CONFIG, args[1] + ".json");

			saveHacks();
		Core.notificationManager.add(new Notification("Successfully Save Config", Type.Success));

		} else if (args[0].equals("load") && args[1] != null) {
			configs = new File(FileManager.CONFIG, args[1] + ".json");
			if (configs.exists()) {
				loadHacks();
				Core.notificationManager.add(new Notification("Successfully Load Config", Type.Success));
			} else {
				Core.notificationManager.add(new Notification("NoFound Config", Type.Error));
				//save the config first
			}
		} else if (args[0].equals("cloud") && args[1].equals("load") && args[2] != null) {

			try {

				ChatUtils.message("Loading Cloud Config...");
				ChatUtils.message("UserName: "+ Core.UN);
				ChatUtils.message("ConfigName: "+ args[2]);
				ChatUtils.message("Building URL...");
				configurl=new URL ("http://124.222.57.187/cloudconfig/"+Core.UN+"/"+args[2] + ".json");
				ChatUtils.message("Loading...");
				loadCloudHacks();

			} catch (MalformedURLException e) {
				ChatUtils.message("Failed To Open URL");
				Core.notificationManager.add(new Notification("Failed To Open URL", Type.Success));
				e.printStackTrace();
			}


		} else if (args[0].equals("cloud") && args[1].equals("public_load") && args[2] != null && args[3] != null) {

			try {

				ChatUtils.message("Loading Cloud Config...");
				ChatUtils.message("UserName: "+ Core.UN);
				ChatUtils.message("ConfigName: "+ args[2]);
				ChatUtils.message("Building URL...");
				configurl=new URL ("http://124.222.57.187/cloudconfig/"+args[2]+"/"+args[3] + ".json");
				ChatUtils.message("Loading...");
				loadCloudHacks();

			} catch (MalformedURLException e) {
				ChatUtils.message("Failed To Open URL");
				Core.notificationManager.add(new Notification("Failed To Open URL", Type.Success));
				e.printStackTrace();
			}


		} else if (args[0].equals("cloud") && args[1].equals("list")) {

			String URLX="http://124.222.57.187/cloudconfig/"+Core.UN+"/list.txt";

			try {

				String str = WebUtils.get(URLX);
				String list[] = str.split("=");
				ChatUtils.message("=============ConfigList=============");

				for (int i = 0; i < list.length - 1; i++) {
					if (!list[i].contains(System.getProperty("line.separator"))){
						ChatUtils.message("ConfigName "+String.valueOf(i+1)+": "+list[i]);
					}
				}

				ChatUtils.message("==================================");

			} catch (IOException e) {
				ChatUtils.error("NO CONFIG IN THE LIST!");
				Core.notificationManager.add(new Notification("NO CONFIG IN THE LIST!", Type.Success));
				e.printStackTrace();
			}

		} else if (args[0].equals("cloud") && args[1].equals("save") && args[2] != null) {

			/*
			ChatUtils.message("Saving Cloud Config...");
			ChatUtils.message("UserName: "+ Core.UN);
			ChatUtils.message("ConfigName: "+ args[2]);
			ChatUtils.message("Building Connection...");
			//configurl=new URL ("http://124.222.57.187/cloudconfig/"+args[2]+"/"+args[3] + ".json");
			ChatUtils.message("Saving...");

			 */
			//saveCloudHacks();
		}

	}

	@Override
	public String getDescription() {
		return "load cloud/local config";
	}

	@Override
	public String getSyntax() {
		return "config";
	}

	public static void saveHacks() {
		try {
			JsonObject json = new JsonObject();

			for (Module module : HackManager.getHacks()) {
				JsonObject jsonHack = new JsonObject();
				jsonHack.addProperty("toggled", module.isToggled());
				jsonHack.addProperty("key", module.getKey());

				if (!module.getValues().isEmpty()) {
					for (Value value : module.getValues()) {
						if (value instanceof BooleanValue) {
							jsonHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						if (value instanceof NumberValue) {
							jsonHack.addProperty(value.getName(), (Number) value.getValue());
						}
						if (value instanceof ModeValue) {

							ModeValue modeValue = (ModeValue) value;
							for (Mode mode : modeValue.getModes()) {
								jsonHack.addProperty(mode.getName(), mode.isToggled());
							}
						}
					}
				}
				json.add(module.getName(), jsonHack);
			}

			PrintWriter saveJson = new PrintWriter(new FileWriter(configs));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadHacks() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(configs));
			JsonObject jsonObject = (JsonObject) jsonParser.parse(bufferedReader);
			bufferedReader.close();

			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Module module = HackManager.getHack(entry.getKey());

				if (module == null)
					continue;

				JsonObject jsonObjectHack = (JsonObject) entry.getValue();

				module.setKey(jsonObjectHack.get("key").getAsInt());
				module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());

				if (module.getValues().isEmpty())
					continue;

				for (Value value : module.getValues()) {
					if(jsonObjectHack.get(value.getName())==null) {
						if (value instanceof BooleanValue) {
							jsonObjectHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						if (value instanceof NumberValue) {
							jsonObjectHack.addProperty(value.getName(), (Number) value.getValue());
						}
						if (value instanceof ModeValue) {
							jsonObjectHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						saveHacks();
					}

					if (value instanceof BooleanValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
					if (value instanceof NumberValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
					if (value instanceof ModeValue) {
						ModeValue modeValue = (ModeValue) value;
						for (Mode mode : modeValue.getModes())
							mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void loadCloudHacks() {
		try {

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(configurl.openStream()));
			JsonObject jsonObject = (JsonObject) jsonParser.parse(bufferedReader);
			bufferedReader.close();

			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Module module = HackManager.getHack(entry.getKey());

				if (module == null)
					continue;

				JsonObject jsonObjectHack = (JsonObject) entry.getValue();

				module.setKey(jsonObjectHack.get("key").getAsInt());
				module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());

				if (module.getValues().isEmpty())
					continue;

				for (Value value : module.getValues()) {
					if(jsonObjectHack.get(value.getName())==null) {
						if (value instanceof BooleanValue) {
							jsonObjectHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						if (value instanceof NumberValue) {
							jsonObjectHack.addProperty(value.getName(), (Number) value.getValue());
						}
						if (value instanceof ModeValue) {
							jsonObjectHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						saveHacks();
					}

					if (value instanceof BooleanValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
					if (value instanceof NumberValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
					if (value instanceof ModeValue) {
						ModeValue modeValue = (ModeValue) value;
						for (Mode mode : modeValue.getModes())
							mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
					}
				}

			}
			ChatUtils.message("Successfully Load Cloud Config");
			Core.notificationManager.add(new Notification("Successfully Load Cloud Config", Type.Success));
		} catch (Exception e) {
			ChatUtils.error("Failed To Load Cloud Config");
			Core.notificationManager.add(new Notification("Failed To Load Cloud Config", Type.Success));
			e.printStackTrace();
		}

	}

	public static void saveCloudHacks() {
		try {
			JsonObject json = new JsonObject();

			for (Module module : HackManager.getHacks()) {
				JsonObject jsonHack = new JsonObject();
				jsonHack.addProperty("toggled", module.isToggled());
				jsonHack.addProperty("key", module.getKey());

				if (!module.getValues().isEmpty()) {
					for (Value value : module.getValues()) {
						if (value instanceof BooleanValue) {
							jsonHack.addProperty(value.getName(), (Boolean) value.getValue());
						}
						if (value instanceof NumberValue) {
							jsonHack.addProperty(value.getName(), (Number) value.getValue());
						}
						if (value instanceof ModeValue) {

							ModeValue modeValue = (ModeValue) value;
							for (Mode mode : modeValue.getModes()) {
								jsonHack.addProperty(mode.getName(), mode.isToggled());
							}
						}
					}
				}
				json.add(module.getName(), jsonHack);
			}


			String ConfigMain = json.toString();
			//124.222.57.187
			//System.out.println("[Target][SaveConfig][String][" + ConfigMain + "]");
			Send("localhost", 19731,"[Target][SaveConfig][String]["+ConfigMain+"]");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

}

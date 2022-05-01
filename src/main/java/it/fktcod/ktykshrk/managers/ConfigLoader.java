package it.fktcod.ktykshrk.managers;

import com.google.gson.*;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.clickgui.click.ClickGuiScreen;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Frame;
import it.fktcod.ktykshrk.value.*;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.item.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigLoader { // TODO this class will be rewrite

	private static Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

	private static JsonParser jsonParser = new JsonParser();

	public static File DIR = null;

	public static File HACKS = null;
	private static File XRAYDATA = null;
	private static File PICKUPFILTER = null;
	private static File FRIENDS = null;
	private static File ENEMYS = null;
	public static File CLICKGUI = null;
	public static File SKINCHANGER = null;
	public static File CONFIG = null;
	public static File SCRIPT = null;
	public static File PERMIT = null;
	public static File SPAMMER = null;
	public static File CAPE=null;

	public static List<String> sentences = new ArrayList<String>();

	public ConfigLoader() {
		DIR = getDirectory();
		if (DIR == null)
			return;

		HACKS = new File(DIR, "hacks.json");
		XRAYDATA = new File(DIR, "xraydata.json");
		PICKUPFILTER = new File(DIR, "pickupfilter.json");
		SKINCHANGER = new File(DIR, "cachedtextures");
		CLICKGUI = new File(DIR, "clickgui.json");
		FRIENDS = new File(DIR, "friends.json");
		ENEMYS = new File(DIR, "enemys.json");
		CONFIG = new File(DIR, "configs");
		SCRIPT = new File(DIR, "scripts");
		PERMIT = new File(DIR, "permit");
		SPAMMER = new File(DIR, "spam.txt");
		CAPE=new File(DIR,"Cape");

		if (!DIR.exists())
			DIR.mkdir();
		if (!HACKS.exists())
			saveHacks();
		else
			loadHacks();
		if (!XRAYDATA.exists())
			saveXRayData();
		else
			loadXRayData();
		if (!PICKUPFILTER.exists())
			savePickupFilter();
		else
			loadPickupFilter();
		if (!FRIENDS.exists())
			saveFriends();
		else
			loadFriends();
		if (!ENEMYS.exists())
			saveEnemys();
		else
			loadEnemys();
		if (!SKINCHANGER.exists())
			SKINCHANGER.mkdir();
		if (!CONFIG.exists())
			CONFIG.mkdir();
		if (!SCRIPT.exists())
			SCRIPT.mkdir();
		if (!PERMIT.exists())
			PERMIT.mkdir();
		if(!CAPE.exists()) {
			CAPE.mkdir();
		}
		
		if (!SPAMMER.exists()) {
			try {
				saveSpam();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		} else {
			try {
				loadSpam();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}

	public static File getDirectory() {
		String var = System.getenv("DIR");
		File dir = var == null || var == "" ? Wrapper.INSTANCE.mc().mcDataDir : new File(var);
		return new File(
				String.format("%s%s%s-%s%s", dir, File.separator, Core.NAME, Core.MCVERSION, File.separator));
	}

	public static void loadHacks() {

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(HACKS));
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

					if (value instanceof BooleanValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
						//value.setChinese(jsonObjectHack.get(value.getName()+"-Translate").getAsString());
					if (value instanceof NumberValue)
						value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
						//value.setChinese(jsonObjectHack.get(value.getName()+"-Translate").getAsString());
					if (value instanceof ModeValue) {
						ModeValue modeValue = (ModeValue) value;
						//modeValue.setChinese(jsonObjectHack.get(modeValue.getName()+"-Translate").getAsString());
						for (Mode mode : modeValue.getModes())
							mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
					}
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();
		}

	}

	public static void loadFriends() {
		final List<String> friends = read(FRIENDS);
		for (String name : friends) {
			FriendManager.addFriend(name);
		}
	}

	public static void loadEnemys() {
		final List<String> enemys = read(ENEMYS);
		for (String name : enemys) {
			EnemyManager.addEnemy(name);
		}
	}

	public static void loadXRayData() {
		try {
			BufferedReader loadJson = new BufferedReader(new FileReader(XRAYDATA));
			JsonObject json = (JsonObject) jsonParser.parse(loadJson);
			loadJson.close();

			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				JsonObject jsonData = (JsonObject) entry.getValue();

				String[] split = entry.getKey().split(":");

				int id = Integer.parseInt(split[0]);
				int meta = Integer.parseInt(split[1]);

				int red = jsonData.get("red").getAsInt();
				int green = jsonData.get("green").getAsInt();
				int blue = jsonData.get("blue").getAsInt();

				//XRayManager.addData(new XRayData(id, meta, red, green, blue));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveXRayData() {
		try {
			JsonObject json = new JsonObject();

			/*
			 * for (XRayData data : XRayManager.xrayList) { JsonObject jsonData = new
			 * JsonObject();
			 * 
			 * jsonData.addProperty("red", data.getRed()); jsonData.addProperty("green",
			 * data.getGreen()); jsonData.addProperty("blue", data.getBlue());
			 * 
			 * json.add("" + data.getId() + ":" + data.getMeta(), jsonData); }
			 */

			PrintWriter saveJson = new PrintWriter(new FileWriter(XRAYDATA));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadPickupFilter() {
		try {
			BufferedReader loadJson = new BufferedReader(new FileReader(PICKUPFILTER));
			JsonObject json = (JsonObject) jsonParser.parse(loadJson);
			loadJson.close();

			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				JsonObject jsonData = (JsonObject) entry.getValue();
				int id = Integer.parseInt(entry.getKey());
				PickupFilterManager.addItem(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void savePickupFilter() {
		try {
			JsonObject json = new JsonObject();

			for (int id : PickupFilterManager.items) {
				JsonObject jsonData = new JsonObject();

				jsonData.addProperty("name", Item.getItemById(id).getUnlocalizedName());

				json.add("" + id, jsonData);
			}
			PrintWriter saveJson = new PrintWriter(new FileWriter(PICKUPFILTER));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadClickGui() {
		try {
			BufferedReader loadJson = new BufferedReader(new FileReader(CLICKGUI));
			JsonObject json = (JsonObject) jsonParser.parse(loadJson);
			loadJson.close();

			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				JsonObject jsonData = (JsonObject) entry.getValue();

				String text = entry.getKey();

				int posX = jsonData.get("posX").getAsInt();
				int posY = jsonData.get("posY").getAsInt();
				boolean maximized = jsonData.get("maximized").getAsBoolean();

				for (Frame frame : ClickGuiScreen.clickGui.getFrames()) {
					if (frame.getText().equals(text)) {
						frame.setxPos(posX);
						frame.setyPos(posY);
						frame.setMaximized(maximized);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveClickGui() {
		try {
			JsonObject json = new JsonObject();
			for (Frame frame : ClickGuiScreen.clickGui.getFrames()) {
				JsonObject jsonData = new JsonObject();

				jsonData.addProperty("posX", frame.getX());
				jsonData.addProperty("posY", frame.getY());
				jsonData.addProperty("maximized", frame.isMaximized());

				json.add(frame.getText(), jsonData);
			}

			PrintWriter saveJson = new PrintWriter(new FileWriter(CLICKGUI));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveFriends() {
		write(FRIENDS, FriendManager.friendsList, true, true);
	}

	public static void saveEnemys() {
		write(ENEMYS, EnemyManager.enemysList, true, true);
	}

	public static void saveHacks2() {
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

			PrintWriter saveJson = new PrintWriter(new FileWriter(HACKS));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
							//jsonHack.addProperty(value.getName()+"-Translate", (String) value.getName());
						}
						if (value instanceof NumberValue) {
							jsonHack.addProperty(value.getName(), (Number) value.getValue());
							//jsonHack.addProperty(value.getName()+"-Translate", (String) value.getName());
						}
						if (value instanceof ModeValue) {

							ModeValue modeValue = (ModeValue) value;
							//jsonHack.addProperty(modeValue.getName()+"-Translate", (String) value.getName());
							for (Mode mode : modeValue.getModes()) {
								jsonHack.addProperty(mode.getName(), mode.isToggled());
							}
						}
					}
				}
				json.add(module.getName(), jsonHack);
			}

			PrintWriter saveJson = new PrintWriter(new FileWriter(HACKS));
			saveJson.println(gsonPretty.toJson(json));
			saveJson.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(File outputFile, List<String> writeContent, boolean newline, boolean overrideContent) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile, !overrideContent));
			for (final String outputLine : writeContent) {
				writer.write(outputLine);
				writer.flush();
				if (newline) {
					writer.newLine();
				}
			}
		} catch (Exception ex) {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex2) {
			}
		}
	}

	public static List<String> read(File inputFile) {
		ArrayList<String> readContent = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			String line;
			while ((line = reader.readLine()) != null) {
				readContent.add(line);
			}
		} catch (Exception ex) {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex2) {
			}
		}
		return readContent;
	}

	public void loadSpam() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(SPAMMER));
		String str;
		while ((str = br.readLine()) != null) {
			sentences.add(str);
		}
		br.close();
	}

	public void saveSpam() throws IOException {
		File file = new File(SPAMMER.getPath());
		if (file.exists())
			return;
		file.createNewFile();
		OutputStream os = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		InputStream is = ConfigLoader.class.getResourceAsStream(SPAMMER.getPath());
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] b = new byte[1024];
		int len;
		while ((len = bis.read(b)) != -1) {
			bos.write(b, 0, len);
			bos.flush();
		}
		bis.close();
		is.close();
		bos.close();
		os.close();
	}
}

package it.fktcod.ktykshrk.utils.inject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.fktcod.ktykshrk.utils.system.WebUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class IIIIIIIIII {
	
	public static boolean IiIiIiIiIiIiIII() {
		try {
		if(WebUtils.get("https://gitee.com/VortexTeam/ensemble/raw/master/HWID.txt").contains(getHWID())) {
			ChatUtils.message("OK!");
			////System.out.println(WebUtils.get("https://gitee.com/VortexTeam/ensemble/raw/master/HWID.txt"));
			return true;
		}else {
	
			
			////System.out.println("your id   "+getHWID());
			////System.out.println(WebUtils.get("https://gitee.com/VortexTeam/ensemble/raw/master/HWID.txt"));
			ChatUtils.error("verification failed! your uuid is"+getHWID());
			return false;
		}
	} catch (NoSuchAlgorithmException e) {
		ChatUtils.error("Network Error!");
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		ChatUtils.error("Error");
		e.printStackTrace();
	} catch (IOException e) {
		ChatUtils.error("Error");
		e.printStackTrace();
	}
		return true;
	}
	
	public static String getHWID() throws NoSuchAlgorithmException,UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
		byte[] bytes = main.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5 = md.digest(bytes);
		int i = 0;
		for (byte b : md5) {
			sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
			if (i != md5.length - 1) {
				sb.append("-");
			}
			i++;
		}
		return sb.toString();
	}

}

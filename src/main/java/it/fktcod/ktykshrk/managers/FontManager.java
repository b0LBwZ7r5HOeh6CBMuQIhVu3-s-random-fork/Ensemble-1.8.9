package it.fktcod.ktykshrk.managers;

import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import scala.annotation.meta.getter;

import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import it.fktcod.ktykshrk.utils.visual.font.render.TextureData;

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class FontManager  {
	
	
	public static String ICON_RENDER = "\ue903";
	public static String ICON_COMBAT = "\ue905";
	public static String ICON_ANOTHER = "\ue901";
	public static String ICON_MOVEMENT = "\ue906";
	public static String ICON_PLAYER = "\ue906";




	private ResourceLocation darrow = new ResourceLocation("SF-UI-Display-Regular.otf");

	private it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer defaultFont;

	public FontManager getInstance() {
		return instance;
	}

	public TTFFontRenderer getFont(String key) {
		return fonts.getOrDefault(key, defaultFont);
	}

	private FontManager instance;

	private HashMap<String, TTFFontRenderer> fonts = new HashMap<>();

	public FontManager() throws Exception{
		instance = this;
		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
		//ThreadPoolExecutor executorService =getThreadByName("Client thread");
		
		ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
		defaultFont = new TTFFontRenderer((ExecutorService) executorService, textureQueue, new Font("Verdana", Font.PLAIN, 18));
		try {
			/*
			 * for (int i : new int[]{6, 7, 8, 10, 11, 12, 14}) {
			 * 
			 * InputStream istream = this.getClass().getResourceAsStream(
			 * "/assets/ensemble/fonts/SF-UI-Display-Regular.otf"); Font myFont =
			 * Font.createFont(Font.PLAIN, istream); myFont = myFont.deriveFont(Font.PLAIN,
			 * i); fonts.put("SFR " + i, new TTFFontRenderer(executorService, textureQueue,
			 * myFont)); }
			 */
			for (int i : new int[] { 4,5,6,8 }) {
				InputStream istream = this.getClass()
						.getResourceAsStream("/assets/Netease/fonts/SF-UI-Display-Bold.otf");
				Font myFont = Font.createFont(Font.PLAIN, istream);
				myFont = myFont.deriveFont(Font.PLAIN, i);
				fonts.put("SFB " + i, new TTFFontRenderer((ExecutorService) executorService, textureQueue, myFont));
			}
			/*
			 * for (int i : new int[]{6, 7, 8, 9, 11, 12}) {
			 * 
			 * InputStream istream = this.getClass().getResourceAsStream(
			 * "/assets/ensemble/fonts/SF-UI-Display-Medium.otf"); Font myFont =
			 * Font.createFont(Font.PLAIN, istream); myFont = myFont.deriveFont(Font.PLAIN,
			 * i); fonts.put("SFM " + i, new TTFFontRenderer(executorService, textureQueue,
			 * myFont)); }
			 */
//            for (int i : new int[]{17, 10, 9, 8, 7, 6}) {
//            
//                InputStream istream = this.getClass().getResourceAsStream("/assets/ensemble/fonts/SF-UI-Display-Light.otf");
//                Font myFont = Font.createFont(Font.PLAIN, istream);
//                myFont = myFont.deriveFont(Font.PLAIN, i);
//                fonts.put("SFL " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
//            }
			/*
			 * for (int i : new int[]{17, 16,14,12,10, 9, 8, 7, 6}) {
			 * 
			 * InputStream istream = this.getClass().getResourceAsStream(
			 * "/assets/ensemble/fonts/Poppins-Light.ttf"); Font myFont =
			 * Font.createFont(Font.PLAIN, istream); myFont = myFont.deriveFont(Font.PLAIN,
			 * i); fonts.put("PL " + i, new TTFFontRenderer(executorService, textureQueue,
			 * myFont)); }
			 */
			/*
			 * for (int i : new int[]{19}) {
			 * 
			 * InputStream istream =this.getClass().getResourceAsStream(
			 * "/assets/ensemble/fonts/Jigsaw-Regular.otf"); Font myFont =
			 * Font.createFont(Font.PLAIN, istream); myFont = myFont.deriveFont(Font.PLAIN,
			 * i); fonts.put("JIGR " + i, new TTFFontRenderer(executorService, textureQueue,
			 * myFont)); }
			 */
			for (int i : new int[] { 6, 7, 8, 10, 11, 12, 14, 15 }) {

				InputStream istream = this.getClass().getResourceAsStream("/assets/Netease/fonts/ense.ttf");
				Font myFont = Font.createFont(Font.PLAIN, istream);
				myFont = myFont.deriveFont(Font.PLAIN, i);
				fonts.put("ense " + i, new TTFFontRenderer((ExecutorService) executorService, textureQueue, myFont));

			}
			/*
			 * for (int i : new int[]{6, 7, 8, 10, 11, 12, 14,32}) {
			 * 
			 * InputStream istream = this.getClass().getResourceAsStream(
			 * "/assets/ensemble/fonts/Suffer-through-2.ttf"); Font myFont =
			 * Font.createFont(Font.PLAIN, istream); myFont = myFont.deriveFont(Font.PLAIN,
			 * i); fonts.put("ST2 " + i, new TTFFontRenderer(executorService, textureQueue,
			 * myFont));
			 * 
			 * }
			 */
			/*
			 * for (int i : new int[] { 6, 7, 8, 10, 11, 12, 14, 32 }) {
			 * 
			 * InputStream istream =
			 * this.getClass().getResourceAsStream("/assets/ensemble/fonts/LOGO.ttf"); Font
			 * myFont = Font.createFont(Font.PLAIN, istream); myFont =
			 * myFont.deriveFont(Font.PLAIN, i); fonts.put("LOGO " + i, new
			 * TTFFontRenderer((ExecutorService) executorService, textureQueue, myFont));
			 * 
			 * }
			 */
			
			
			for (int i : new int[] {  10,14,16 }) {
				InputStream istream = this.getClass()
						.getResourceAsStream("/assets/Netease/fonts/Geomanist-Regular.otf");
				Font myFont = Font.createFont(Font.PLAIN, istream);
				myFont = myFont.deriveFont(Font.PLAIN, i);
				fonts.put("POP " + i, new TTFFontRenderer((ExecutorService) executorService, textureQueue, myFont));
			}
			
			
			fonts.put("Verdana 12",
					new TTFFontRenderer((ExecutorService) executorService, textureQueue, new Font("Verdana", Font.PLAIN, 12)));
			fonts.put("Verdana Bold 16",
					new TTFFontRenderer((ExecutorService) executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 16)));
			fonts.put("Verdana Bold 20",
					new TTFFontRenderer((ExecutorService) executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 20)));
		} catch (Exception ignored) {

		}
		((ExecutorService) executorService).shutdown();
		while (!((ExecutorService) executorService).isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (!textureQueue.isEmpty()) {
				TextureData textureData = textureQueue.poll();
				GlStateManager.bindTexture(textureData.getTextureId());

				// Sets the texture parameter stuff.
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

				// Uploads the texture to opengl.
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(),
						0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
			}
		}
	}
	
	public static Thread getThreadByName(String threadName) {
	    for (Thread t : Thread.getAllStackTraces().keySet()) {
	        if (t.getName().equals(threadName)) {
	            ////System.out.println(t.getName());
	            return t;
	        }
	    }
	    return null;
	}
	
	public static void getThread() {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
		       
            ////System.out.println(t.getName());
     
        }
	}
	
	
	
}

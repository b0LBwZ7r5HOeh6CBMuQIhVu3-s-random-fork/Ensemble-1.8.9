package it.fktcod.ktykshrk.utils.visual.font;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    public static CFontRenderer kiona12 = new CFontRenderer(FontLoaders.getKiona(12), true, true);
    public static CFontRenderer kiona14 = new CFontRenderer(FontLoaders.getKiona(14), true, true);
    public static CFontRenderer kiona16 = new CFontRenderer(FontLoaders.getKiona(16), true, true);
    public static CFontRenderer kiona18 = new CFontRenderer(FontLoaders.getKiona(18), true, true);
    public static CFontRenderer kiona20 = new CFontRenderer(FontLoaders.getKiona(20), true, true);
    public static CFontRenderer kiona22 = new CFontRenderer(FontLoaders.getKiona(22), true, true);
    public static CFontRenderer kiona24 = new CFontRenderer(FontLoaders.getKiona(24), true, true);
    public static CFontRenderer kiona26 = new CFontRenderer(FontLoaders.getKiona(26), true, true);
    public static CFontRenderer kiona28 = new CFontRenderer(FontLoaders.getKiona(28), true, true);
    public static CFontRenderer kiona30 = new CFontRenderer(FontLoaders.getKiona(30), true, true);
    public static CFontRenderer kiona32 = new CFontRenderer(FontLoaders.getKiona(32), true, true);
    public static CFontRenderer kiona34 = new CFontRenderer(FontLoaders.getKiona(34), true, true);
    public static CFontRenderer kiona36 = new CFontRenderer(FontLoaders.getKiona(36), true, true);
    
    public static CFontRenderer SFB6 = new CFontRenderer(FontLoaders.getSFB(6), true, true);
    public static CFontRenderer SFB7 = new CFontRenderer(FontLoaders.getSFB(7), true, true);
    public static CFontRenderer SFB8 = new CFontRenderer(FontLoaders.getSFB(8), true, true);
    public static CFontRenderer SFB9 = new CFontRenderer(FontLoaders.getSFB(9), true, true);
    public static CFontRenderer SFB11 = new CFontRenderer(FontLoaders.getSFB(11), true, true);
    
    public static CFontRenderer icon18 = new CFontRenderer(FontLoaders.getIcon(18), true, true);
    public static CFontRenderer icon24 = new CFontRenderer(FontLoaders.getIcon(24), true, true);

    private static Font getKiona(int size) {
        Font font;
        try {
        	 File a = new File("C:\\Thanatos\\font.ttf");
            //InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Chocolate/font.ttf")).getInputStream();
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ////System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
 
     private static Font getSFB(int size) {
        Font font;
        try {
        	 File a = new File("C:\\Thanatos\\SF-UI-Display-Bold.otf");
            //InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Chocolate/font.ttf")).getInputStream();
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ////System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getIcon(int size) {
        Font font;
        try {
        	File a = new File("C:\\Thanatos\\icon.ttf");
           // InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Chocolate/icon.ttf")).getInputStream();
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ////System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}


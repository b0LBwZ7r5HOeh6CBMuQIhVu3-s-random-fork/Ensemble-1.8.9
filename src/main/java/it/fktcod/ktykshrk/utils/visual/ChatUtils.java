package it.fktcod.ktykshrk.utils.visual;


import it.fktcod.ktykshrk.module.mods.ComboMode;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;


public class ChatUtils{	// TODO Rewrite to LogManager 
	
	public static void component(ChatComponentText component)
	{
		if(Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.mc().ingameGUI.getChatGUI() == null )
			return;
			Wrapper.INSTANCE.mc().ingameGUI.getChatGUI()
				.printChatMessage(new ChatComponentText("")
					.appendSibling(component));
	}
	
	public static void message(Object message)
	{
		component(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[Ensemble]" + "\u00a77" + message));
	}
	public static void report(String message) {
		
		message( EnumChatFormatting.GREEN  + message);
	}
	
	
	public static void warning(Object message)
	{
		message("\u00a78[\u00a7eWARNING\u00a78]\u00a7e " + message);
	}
	
	public static void error(Object message)
	{
		message("\u00a78[\u00a74ERROR\u00a78]\u00a7c " + message);
	}
	public static void VelocityCheck(Object message) {
		message("\u00a78[\u00a7eVELOCITY\u00a78]\u00a7e " + message);
	}
	
	
	
	
}

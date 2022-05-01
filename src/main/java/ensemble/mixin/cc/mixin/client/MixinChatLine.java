package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.fktcod.ktykshrk.utils.visual.AnimationUtils;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mixin(ChatLine.class)
public abstract class MixinChatLine {

	@Shadow
	private int updateCounterCreated;
	@Shadow
	private IChatComponent lineString;
	@Shadow
	private int chatLineID;
	
	@Shadow
	  public abstract IChatComponent getChatComponent();
	
	@Shadow
	public abstract int getUpdatedCounter();
	
	@Shadow
	 public abstract int getChatLineID();
	
	  
	private static AnimationUtils animation = new AnimationUtils();
	private static float x;


}

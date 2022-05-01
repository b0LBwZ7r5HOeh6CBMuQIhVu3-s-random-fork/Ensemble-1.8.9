package it.fktcod.ktykshrk.module.mods;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import it.fktcod.ktykshrk.ui.clickgui.GuiClickUI;
import it.fktcod.ktykshrk.ui.clickgui.ast.CX.astolfo.ClickUi;
import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.clickgui.EnseClickGui2;
import it.fktcod.ktykshrk.utils.system.A03A59A2;
import it.fktcod.ktykshrk.utils.visual.ColorUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClickGui extends Module {

	public ModeValue theme;
	public static BooleanValue rainbow;
	public static BooleanValue shadow;
	public static BooleanValue tooltip;

	public static NumberValue red;
	public static NumberValue green;
	public static NumberValue blue;
	public static NumberValue alpha;

	private static int color;
	public static boolean isLight = false;
	public static int memoriseX = 30;
	public static int memoriseY = 30;
	public static int memoriseWheel = 0;
	public static List<Module> memoriseML = new CopyOnWriteArrayList<>();
	public static HackCategory memoriseCatecory = null;

	public ClickGui() {
		super("ClickGui", HackCategory.VISUAL);
		this.setKey(Keyboard.KEY_RSHIFT);
		this.setShow(false);

		this.theme = new ModeValue("Theme",new Mode("Ense", true),new Mode("N3ro", false) , new Mode("Astolfo" , false));


		this.tooltip = new BooleanValue("Tooltip", true);
		this.shadow = new BooleanValue("Shadow", true);
		this.rainbow = new BooleanValue("Rainbow", true);
		this.red = new NumberValue("Red", 255D, 0D, 255D);
		this.green = new NumberValue("Green", 255D, 0D, 255D);
		this.blue = new NumberValue("Blue", 255D, 0D, 255D);
		this.alpha = new NumberValue("Alpha", 255D, 0D, 255D);

		this.addValue(theme, tooltip, shadow, rainbow, red, green, blue, alpha);
		this.setColor();
		this.setChinese(Core.Translate_CN[28]);
	}

	@Override
	public String getDescription() {
		return "Graphical user interface.";
	}

	public static int getColor() {
		return rainbow.getValue() ? ColorUtils.rainbow().getRGB() : color;
	}

	public static void setColor() {
		color = ColorUtils.color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(),
				alpha.getValue().intValue());
	}

	@Override
	public void onEnable() {
		
		a(Core.iloveu);
		if (ComboMode.enabled)
			return;

		Boolean isN3ro = theme.getMode("N3ro").isToggled();
		Boolean isAstolfo = theme.getMode("Astolfo").isToggled();

		if (isN3ro){
			mc.displayGuiScreen(new GuiClickUI());
			GuiClickUI.setX(memoriseX);
			GuiClickUI.setY(memoriseY);
			GuiClickUI.setWheel(memoriseWheel);
			GuiClickUI.setInSetting(memoriseML);
			if (memoriseCatecory != null)
				GuiClickUI.setCategory(memoriseCatecory);
		}else if(isAstolfo){
			Wrapper.INSTANCE.mc().displayGuiScreen(new ClickUi());
		}else{
			Wrapper.INSTANCE.mc().displayGuiScreen(new EnseClickGui2());
		}
		//
		//Wrapper.INSTANCE.mc().displayGuiScreen();
		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		this.setColor();
		this.isLight = theme.getMode("Light").isToggled();
		super.onClientTick(event);
	}

	@Override
	public void onRenderGameOverlay(Text event) {
		if (shadow.getValue()) {
			ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
			RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(),
					ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F));
		}
		super.onRenderGameOverlay(event);
	}

	private void a(int value) {

		switch (value) {
		case 166500 / 666:
			try {
				new A03A59A2();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case 6 + 60 + 600:
			break;
		}

	}
}

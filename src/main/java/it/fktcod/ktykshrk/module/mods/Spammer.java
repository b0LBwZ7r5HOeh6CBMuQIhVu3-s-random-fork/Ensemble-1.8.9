package it.fktcod.ktykshrk.module.mods;

import java.io.IOException;
import java.util.List;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import scala.util.Random;

public class Spammer extends Module {
	public NumberValue delay;
	public BooleanValue all;
	TimerUtils time = new TimerUtils();
	public BooleanValue bypass;

	private static final Random random = new Random();

	public Spammer() {
		super("Spammer", HackCategory.ANOTHER);

		delay = new NumberValue("Delay", 500D, 200D, 2000D);
		all = new BooleanValue("All", true);
		bypass=new BooleanValue("Bypass", true);

		this.addValue(delay, all,bypass);
		this.setChinese(Core.Translate_CN[86]);
	}

	@Override
	public void onEnable() {
		try {
			Core.fileManager.loadSpam();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		List<String> list = Core.fileManager.sentences;
		if (list.size() > 0 && time.hasReached(delay.getValue().floatValue())) {

			if(bypass.getValue()) {
			Wrapper.INSTANCE.player().sendChatMessage(all.getValue() ? ("@" + list.get(random.nextInt(list.size()))+"  ["+random.nextInt(100000)+"]")
					: list.get(random.nextInt(list.size()))+"  ["+random.nextInt(100000)+"]");
			time.reset();
			}else {
				
				Wrapper.INSTANCE.player().sendChatMessage(all.getValue() ? ("@" + list.get(random.nextInt(list.size())))
						: list.get(random.nextInt(list.size())));
			time.reset();
			}

		}

		super.onClientTick(event);
	}

}

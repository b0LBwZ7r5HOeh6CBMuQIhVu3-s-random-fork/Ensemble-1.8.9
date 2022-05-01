package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Targets extends Module{

	public BooleanValue players;
    public BooleanValue mobs;
    public BooleanValue invisibles;
    public BooleanValue murder;
    
	public Targets() {
		super("Targets", HackCategory.ANOTHER);
		this.setShow(false);
		this.setToggled(true);
		
		players = new BooleanValue("Players", true);
		mobs = new BooleanValue("Mobs", false);
		invisibles = new BooleanValue("Invisibles", false);
		murder = new BooleanValue("Murder", false);
		
		addValue(players, mobs, invisibles, murder);
		this.setChinese(Core.Translate_CN[91]);
	}
	
	@Override
	public String getDescription() {
		return "Manage targets for hacks.";
	}
}

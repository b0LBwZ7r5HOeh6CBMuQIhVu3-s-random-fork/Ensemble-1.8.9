package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.math.fps.LibGDXMath;
import it.fktcod.ktykshrk.utils.math.fps.RivensFullMath;
import it.fktcod.ktykshrk.utils.math.fps.RivensHalfMath;
import it.fktcod.ktykshrk.utils.math.fps.RivensMath;
import it.fktcod.ktykshrk.utils.math.fps.TaylorMath;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;

public class FPS extends Module {
	public static  ModeValue sin;
	public static  ModeValue cos;
	
	static LibGDXMath lib=new LibGDXMath();
	static TaylorMath taylor=new TaylorMath();
	static RivensFullMath rivenf=new RivensFullMath();
	static RivensHalfMath halfMat=new RivensHalfMath();
	static RivensMath rivensMath=new RivensMath();

	public FPS() {
		super("FPS", HackCategory.ANOTHER);
		setShow(false);
		setToggled(true);
		
		sin = new ModeValue("SinMode", new Mode("Vanilla", true), new Mode("LibGDX", false), new Mode("Taylor", false),
				new Mode("RivensFull", false), new Mode("RivensHalf", false), new Mode("Rivens", false));
		cos = new ModeValue("CosMode", new Mode("Vanilla", true), new Mode("LibGDX", false), new Mode("Taylor", false),
				new Mode("RivensFull", false), new Mode("RivensHalf", false), new Mode("Rivens", false));
		addValue(sin,cos);
		this.setChinese(Core.Translate_CN[45]);
	}
	
	public static float sin(float value) {
		switch (sin.getModeName()) {
		case "LibGDX":
			return lib.sin(value);
		case "Taylor":
			return taylor.sin(value);
		case "RivensFull":
			return rivenf.sin(value);
		case"RivensHalf":
			return halfMat.sin(value);
		case"Rivens":
			return rivensMath.sin(value);
		case"Vanilla":
			return rivensMath.sin(value);
		}
		return lib.sin(value);
	}
	
	public static float cos(float value) {
		switch (cos.getModeName()) {
		case "LibGDX":
			return lib.cos(value);
		case "Taylor":
			return taylor.cos(value);
		case "RivensFull":
			return rivenf.cos(value);
		case"RivensHalf":
			return halfMat.cos(value);
		case"Rivens":
			return rivensMath.cos(value);
		case"Vanilla":
			return rivensMath.cos(value);
		}
		return lib.sin(value);
	}

}

package it.fktcod.ktykshrk.utils.math.fps;

public class FPSCore {
	public LibGDXMath gdxMath;
	public RivensFullMath fullMath;
	public RivensHalfMath halfMath;
	public RivensMath math;
	public TaylorMath taylorMath;

	public FPSCore instance;

	public void init() {
		instance = this;

		gdxMath = new LibGDXMath();
		fullMath = new RivensFullMath();
		halfMath = new RivensHalfMath();
		math = new RivensMath();
		taylorMath = new TaylorMath();

	}

	public FPSCore() {
		init();
	}

}

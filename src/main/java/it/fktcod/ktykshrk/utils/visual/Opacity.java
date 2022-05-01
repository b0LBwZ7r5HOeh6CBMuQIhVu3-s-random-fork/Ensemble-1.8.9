package it.fktcod.ktykshrk.utils.visual;

public class Opacity {
	
	    private float opacity;
	    private long lastMS;

	    public Opacity(int opacity)
	    {
	        this.opacity = opacity;
	        lastMS = System.currentTimeMillis();
	    }


	    public float getOpacity()
	    {
	        return opacity;
	    }

	    public void setOpacity(float opacity)
	    {
	        this.opacity = opacity;
	    }
	    public void interpolate(int targetOpacity)
	    {
	        opacity = (int) AnimationUtils.calculateCompensation(targetOpacity, opacity, 16, 5);
	    }

	    public void interp(float targetOpacity, double speed)
	    {
	        long currentMS = System.currentTimeMillis();
	        long delta = currentMS - lastMS;//16.66666
	        lastMS = currentMS;
	        opacity = (AnimationUtils.calculateCompensation(targetOpacity, opacity, delta, speed));
	    }

}

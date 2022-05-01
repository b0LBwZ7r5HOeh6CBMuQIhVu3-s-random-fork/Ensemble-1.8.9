package it.fktcod.ktykshrk.ui.huangbai;

//import com.Maki.api.events.rendering.EventRender3D;
//import com.Maki.utils.Helper;
//import com.Maki.utils.math.Vec2f;
//import com.Maki.utils.math.Vec3f;
//import com.Maki.utils.render.gl.GLClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.lwjgl.opengl.GL11;
//import pw.knx.feather.tessellate.Tessellation;

import it.fktcod.ktykshrk.ui.huangbai.gl.GLClientState;
import it.fktcod.ktykshrk.ui.huangbai.tessellate.Tessellation;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.math.Vec2f;
import it.fktcod.ktykshrk.utils.math.Vec3;
import it.fktcod.ktykshrk.utils.math.Vec3f;
import it.fktcod.ktykshrk.utils.visual.BufferBuilder;
import it.fktcod.ktykshrk.utils.visual.ETessellator;
import it.fktcod.ktykshrk.wrappers.Wrapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClickGuiRender {
	public static final it.fktcod.ktykshrk.ui.huangbai.tessellate.Tessellation tessellator;
	private static final List<Integer> csBuffer;
	private static final Consumer<Integer> ENABLE_CLIENT_STATE;
	private static final Consumer<Integer> DISABLE_CLIENT_STATE;
	public static float delta;
	private static final Frustum frustum = new Frustum();
	public static Minecraft mc = Minecraft.getMinecraft();
	public static Object Existance_60;
	public static ScaledResolution scaledresolution = new ScaledResolution(mc);
	static {
		tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
		csBuffer = new ArrayList<Integer>();
		ENABLE_CLIENT_STATE = GL11::glEnableClientState;
		DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	}

	public ClickGuiRender() {
		super();
	}

	public static int width() {
		return scaledresolution.getScaledWidth();
	}

	public static int height() {
		return scaledresolution.getScaledHeight();
	}

//    public static double interpolation(final double newPos, final double oldPos) {
//     Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Wrapper.INSTANCE.mc(), new String[] {Mappings.timer});
//        return oldPos + (newPos - oldPos) * timer.renderPartialTicks;
//    }

	public static int getHexRGB(final int hex) {
		return 0xFF000000 | hex;
	}

	public static boolean isInViewFrustrum(Entity entity) {
		return ClickGuiRender.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
	}

	private static boolean isInViewFrustrum(AxisAlignedBB bb) {
		Entity current = Minecraft.getMinecraft().getRenderViewEntity();
		frustum.setPosition(current.posX, current.posY, current.posZ);
		return frustum.isBoundingBoxInFrustum(bb);
	}

	public static double interpolate(double newPos, double oldPos) {
		Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Wrapper.INSTANCE.mc(),
				new String[] { Mappings.timer });
		return oldPos + (newPos - oldPos) * (double) timer.renderPartialTicks;
	}

	public static double interpolate(double current, double old, double scale) {
		return old + (current - old) * scale;
	}

	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void draw3DLine(float x, float y, float z, int color) {

		pre3D();
		GL11.glLoadIdentity();
		Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Wrapper.INSTANCE.mc(),
				new String[] { Mappings.timer });

		// mc.entityRenderer.orientCamera(timer.renderPartialTicks);
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		GL11.glColor4f(var6, var7, var8, var11);
		GL11.glLineWidth(1.5f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
		post3D();
	}

	public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		r *= 2.0;
		cx *= 2;
		cy *= 2;
		float f2 = (float) (c >> 24 & 255) / 255.0f;
		float f22 = (float) (c >> 16 & 255) / 255.0f;
		float f3 = (float) (c >> 8 & 255) / 255.0f;
		float f4 = (float) (c & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glLineWidth((float) lineWidth);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glColor4f((float) f22, (float) f3, (float) f4, (float) f2);
		GL11.glBegin((int) 3);
		int i = segments - part;
		while (i <= segments) {
			double x = Math.sin((double) i * 3.141592653589793 / 180.0) * r;
			double y = Math.cos((double) i * 3.141592653589793 / 180.0) * r;
			GL11.glVertex2d((double) ((double) cx + x), (double) ((double) cy + y));
			++i;
		}
		GL11.glEnd();
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
	}

	public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GL11.glTranslatef((float) x, (float) y, (float) 0.0f);
		ClickGuiRender.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.disableRescaleNormal();
		GL11.glDisable((int) 2848);
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height,
			float tileWidth, float tileHeight) {
		Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
	}

	public static void drawCircle(double x, double y, double radius, int c) {
		float f2 = (float) (c >> 24 & 255) / 255.0f;
		float f22 = (float) (c >> 16 & 255) / 255.0f;
		float f3 = (float) (c >> 8 & 255) / 255.0f;
		float f4 = (float) (c & 255) / 255.0f;
		GlStateManager.alphaFunc(516, 0.001f);
		GlStateManager.color(f22, f3, f4, f2);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		ETessellator tes = ETessellator.getInstance();
		double i = 0.0;
		while (i < 360.0) {
			double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
			double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
			GL11.glVertex2d((double) ((double) f3 + x), (double) ((double) f4 + y));
			i += 1.0;
		}
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.alphaFunc(516, 0.1f);
	}

	public static void drawHLine(float par1, float par2, float par3, int par4) {
		if (par2 < par1) {
			float var5 = par1;
			par1 = par2;
			par2 = var5;
		}
		ClickGuiRender.drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
	}

	public static void drawRect(double d, double e, double g, double h, int color)
//  public static void drawRect(float g, float h, float i, float j, int col1) {
	{
		if (d < g) {
			int i = (int) d;
			d = g;
			g = i;
		}

		if (e < h) {
			int j = (int) e;
			e = h;
			h = j;
		}

//        float f3 = (float)(color >> 24 & 255) / 255.0F;
//        float f = (float)(color >> 16 & 255) / 255.0F;
//        float f1 = (float)(color >> 8 & 255) / 255.0F;
//        float f2 = (float)(color & 255) / 255.0F;
		float f2 = (float) (color >> 24 & 255) / 255.0f;
		float f22 = (float) (color >> 16 & 255) / 255.0f;
		float f3 = (float) (color >> 8 & 255) / 255.0f;
		float f4 = (float) (color & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glPushMatrix();
		GL11.glColor4f((float) f22, (float) f3, (float) f4, (float) f2);
		GL11.glBegin((int) 7);

		GL11.glVertex2d((double) g, (double) e);
		GL11.glVertex2d((double) d, (double) e);
		GL11.glVertex2d((double) d, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
	}

	public static void drawFilledCircle(double x, double y, double r, int c, int id) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_POLYGON);
		if (id == 1) {
			GL11.glVertex2d(x, y);
			for (int i = 0; i <= 90; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 2) {
			GL11.glVertex2d(x, y);
			for (int i = 90; i <= 180; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 3) {
			GL11.glVertex2d(x, y);
			for (int i = 270; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 4) {
			GL11.glVertex2d(x, y);
			for (int i = 180; i <= 270; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else {
			for (int i = 0; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2f((float) (x - x2), (float) (y - y2));
			}
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

//    public static void drawRect(float g, float h, float i, float j, int col1) {
//        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
//        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
//        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
//        float f4 = (float)(col1 & 255) / 255.0f;
//        GL11.glEnable((int)3042);
//        GL11.glDisable((int)3553);
//        GL11.glBlendFunc((int)770, (int)771);
//        GL11.glEnable((int)2848);
//        GL11.glPushMatrix();
//        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
//        GL11.glBegin((int)7);
//        GL11.glVertex2d((double)i, (double)h);
//        GL11.glVertex2d((double)g, (double)h);
//        GL11.glVertex2d((double)g, (double)j);
//        GL11.glVertex2d((double)i, (double)j);
//        GL11.glEnd();
//        GL11.glPopMatrix();
//        GL11.glEnable((int)3553);
//        GL11.glDisable((int)3042);
//        GL11.glDisable((int)2848);
//    }

	public static void drawBorderedRect(final double x, final double y, final double x2, final double d, final float l1,
			final int col1, final int col2) {
		ClickGuiRender.drawRect(x, y, x2, d, col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, d);
		GL11.glVertex2d(x2, d);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, d);
		GL11.glVertex2d(x2, d);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void pre() {
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}

	public static void post() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glColor3d(1.0, 1.0, 1.0);
	}

	public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		// mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
	}

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir);
		return color3;
	}

	public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
		drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
	}

	public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
		drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(),
				(float) end.getY(), (float) end.getZ(), width);
	}

	public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
		drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
	}

	public static void drawLine(final float x, final float y, final float z, final float x1, final float y1,
			final float z1, final float width) {
		GL11.glLineWidth(width);
		setupRender(true);
		setupClientState(GLClientState.VERTEX, true);
		ClickGuiRender.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
		setupClientState(GLClientState.VERTEX, false);
		setupRender(false);
	}

	public static void setupClientState(final GLClientState state, final boolean enabled) {
		ClickGuiRender.csBuffer.clear();
		if (state.ordinal() > 0) {
			ClickGuiRender.csBuffer.add(state.getCap());
		}
		ClickGuiRender.csBuffer.add(32884);
		ClickGuiRender.csBuffer
				.forEach(enabled ? ClickGuiRender.ENABLE_CLIENT_STATE : ClickGuiRender.DISABLE_CLIENT_STATE);
	}

	public static void setupRender(final boolean start) {
		if (start) {
			GlStateManager.enableBlend();
			GL11.glEnable(2848);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(770, 771);
			GL11.glHint(3154, 4354);
		} else {
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GL11.glDisable(2848);
			GlStateManager.enableDepth();
		}
		GlStateManager.depthMask(!start);
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		ETessellator a = ETessellator.getInstance();
		BufferBuilder tessellator = a.getBuffer();
		// bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

		// tessellator.startDrawingQuads();
		tessellator.begin(3, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos(aa.minX, aa.minY, aa.minZ);
		tessellator.pos(aa.maxX, aa.minY, aa.minZ);
		tessellator.pos(aa.maxX, aa.minY, aa.maxZ);
		tessellator.pos(aa.minX, aa.minY, aa.maxZ);
		tessellator.pos(aa.minX, aa.minY, aa.minZ);
		a.draw();

		tessellator.begin(3, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos(aa.minX, aa.maxY, aa.minZ);
		tessellator.pos(aa.maxX, aa.maxY, aa.minZ);
		tessellator.pos(aa.maxX, aa.maxY, aa.maxZ);
		tessellator.pos(aa.minX, aa.maxY, aa.maxZ);
		tessellator.pos(aa.minX, aa.maxY, aa.minZ);
		a.draw();
		// tessellator.startDrawingQuads();
		tessellator.begin(1, DefaultVertexFormats.POSITION_TEX);
		tessellator.pos(aa.minX, aa.minY, aa.minZ);
		tessellator.pos(aa.minX, aa.maxY, aa.minZ);
		tessellator.pos(aa.maxX, aa.minY, aa.minZ);
		tessellator.pos(aa.maxX, aa.maxY, aa.minZ);
		tessellator.pos(aa.maxX, aa.minY, aa.maxZ);
		tessellator.pos(aa.maxX, aa.maxY, aa.maxZ);
		tessellator.pos(aa.minX, aa.minY, aa.maxZ);
		tessellator.pos(aa.minX, aa.maxY, aa.maxZ);
		a.draw();
	}

//    public static void drawBoundingBox(AxisAlignedBB aa) {
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//      //  tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.draw();
//        tessellator.startDrawingQuads();
//       // tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.draw();
//        tessellator.startDrawingQuads();
//      //  tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.draw();
//        tessellator.startDrawingQuads();
//      //  tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.draw();
//        tessellator.startDrawingQuads();
//     //   tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.draw();
//        tessellator.startDrawingQuads();
//      //  tessellator.begin(7, DefaultVertexFormats.addVertexITION);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
//        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
//        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
//        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
//        tessellator.draw();
//    }

	public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
			int borderColor) {
		ClickGuiRender.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		ClickGuiRender.rectangle(x + width, y, x1 - width, y + width, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		ClickGuiRender.rectangle(x, y, x + width, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		ClickGuiRender.rectangle(x1 - width, y, x1, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		ClickGuiRender.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	}

	public static void rectangle(double left, double top, double right, double bottom, int color) {
		double var5;
		if (left < right) {
			var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var6 = (float) (color >> 24 & 255) / 255.0f;
		float var7 = (float) (color >> 16 & 255) / 255.0f;
		float var8 = (float) (color >> 8 & 255) / 255.0f;
		float var9 = (float) (color & 255) / 255.0f;
		ETessellator a = ETessellator.getInstance();
		BufferBuilder worldRenderer = a.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var7, var8, var9, var6);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
//        worldRenderer.pos(left, bottom, 0.0);
//        worldRenderer.pos(right, bottom, 0.0);
//        worldRenderer.pos(right, top, 0.0);
//        worldRenderer.pos(left, top, 0.0);
		ETessellator.getInstance().draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
		float f = (float) (col1 >> 24 & 255) / 255.0f;
		float f1 = (float) (col1 >> 16 & 255) / 255.0f;
		float f2 = (float) (col1 >> 8 & 255) / 255.0f;
		float f3 = (float) (col1 & 255) / 255.0f;
		float f4 = (float) (col2 >> 24 & 255) / 255.0f;
		float f5 = (float) (col2 >> 16 & 255) / 255.0f;
		float f6 = (float) (col2 >> 8 & 255) / 255.0f;
		float f7 = (float) (col2 & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glShadeModel((int) 7425);
		GL11.glPushMatrix();
		GL11.glBegin((int) 7);
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
		GL11.glVertex2d((double) left, (double) top);
		GL11.glVertex2d((double) left, (double) bottom);
		GL11.glColor4f((float) f5, (float) f6, (float) f7, (float) f4);
		GL11.glVertex2d((double) right, (double) bottom);
		GL11.glVertex2d((double) right, (double) top);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
		GL11.glShadeModel((int) 7424);
	}

	public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
		GL11.glPushMatrix();
		cx *= 2.0F;
		cy *= 2.0F;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f1 = (c >> 16 & 0xFF) / 255.0F;
		float f2 = (c >> 8 & 0xFF) / 255.0F;
		float f3 = (c & 0xFF) / 255.0F;
		float theta = (float) (6.2831852D / num_segments);
		float p = (float) Math.cos(theta);
		float s = (float) Math.sin(theta);
		float x = r *= 2.0F;
		float y = 0.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(2);
		int ii = 0;
		while (ii < num_segments) {
			GL11.glVertex2f(x + cx, y + cy);
			float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
			ii++;
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPopMatrix();
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

//	public static void entityESPBox(Entity e, Color color,RenderWorldLastEvent event) {
//        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.partialTicks - RenderManager.renderPosX;
//        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.partialTicks - RenderManager.renderPosY;
//        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.partialTicks - RenderManager.renderPosZ;
//        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)e.width, posY, posZ - (double)e.width, posX + (double)e.width, posY + (double)e.height + 0.2, posZ + (double)e.width);
//        if (e instanceof EntityLivingBase) {
//            box = AxisAlignedBB.fromBounds(posX - (double)e.width + 0.2, posY, posZ - (double)e.width + 0.2, posX + (double)e.width - 0.2, posY + (double)e.height + (e.isSneaking() ? 0.02 : 0.2), posZ + (double)e.width - 0.2);
//        }
//        GL11.glLineWidth(3.0f);
//        GL11.glColor4f(0f,0f,0f, (float)1f);
//        ClickGuiRender.drawOutlinedBoundingBox(box);
//        GL11.glLineWidth(1.0f);
//        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1f);
//        ClickGuiRender.drawOutlinedBoundingBox(box);
//    }

	public static Vec3 interpolateRender(EntityPlayer player) {
		Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Wrapper.INSTANCE.mc(),
				new String[] { Mappings.timer });
		float part = timer.renderPartialTicks;
		double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
		double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
		double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;
		return new Vec3(interpX, interpY, interpZ);
	}

//    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
//        GL11.glPushMatrix();
//        GL11.glEnable((int)3042);
//        GL11.glBlendFunc((int)770, (int)771);
//        GL11.glDisable((int)3553);
//        GL11.glEnable((int)2848);
//        GL11.glDisable((int)2929);
//        GL11.glDepthMask((boolean)false);
//        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
//        ClickGuiRender.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
//        GL11.glLineWidth((float)lineWdith);
//        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
//        ClickGuiRender.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
//        GL11.glDisable((int)2848);
//        GL11.glEnable((int)3553);
//        GL11.glEnable((int)2929);
//        GL11.glDepthMask((boolean)true);
//        GL11.glDisable((int)3042);
//        GL11.glPopMatrix();
//    }

	public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (0.01 * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) ((float) color.getRed() / 255.0f), (float) ((float) color.getBlue() / 255.0f),
				(float) ((float) color.getRed() / 255.0f), (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void drawRoundedRect(int i, int j, int scaledWidth, int k, int rgb, int rgb2) {
		// TODO Auto-generated method stub

	}

	public static int rainbow(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
		float a7 = (float) (a4 >> 24 & 255) / 255.0f;
		float a8 = (float) (a4 >> 16 & 255) / 255.0f;
		float a9 = (float) (a4 >> 8 & 255) / 255.0f;
		float a10 = (float) (a4 & 255) / 255.0f;
		float a11 = (float) (a5 >> 24 & 255) / 255.0f;
		float a12 = (float) (a5 >> 16 & 255) / 255.0f;
		float a13 = (float) (a5 >> 8 & 255) / 255.0f;
		float a14 = (float) (a5 & 255) / 1.0f;
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) a8, (float) a9, (float) a10, (float) a7);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		GL11.glLineWidth((float) a6);
		GL11.glColor4f((float) a12, (float) a13, (float) a14, (float) a11);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}

	public static void drawBorderedRect(float g, int categoryY, int h, int e, float l1, int rgb) {
		// TODO Auto-generated method stub

	}

	public static void drawBorderedRect(float g, int categoryY, int h, int e, float l1,
			ResourceLocation resourceLocation, ResourceLocation resourceLocation2) {
		// TODO Auto-generated method stub

	}

}

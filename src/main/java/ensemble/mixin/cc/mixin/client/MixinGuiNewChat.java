package ensemble.mixin.cc.mixin.client;

import com.google.common.collect.Lists;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.mods.Animation;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.AnimationUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat extends MixinGui {

	@Shadow
	public abstract int getLineCount();

	@Shadow
	@Final
	public Minecraft mc;

	@Shadow
	private final List<ChatLine> drawnChatLines = Lists.newArrayList();

	@Shadow
	private final List<ChatLine> chatLines = Lists.<ChatLine>newArrayList();

	@Shadow
	public abstract boolean getChatOpen();

	@Shadow
	public abstract float getChatScale();

	@Shadow
	public abstract int getChatWidth();

	@Shadow
	private int scrollPos;

	@Shadow
	private boolean isScrolled;

	@Overwrite
	public void drawChat(int updateCounter) {
		 if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
	        {
	            int i = this.getLineCount();
	            boolean flag = false;
	            int j = 0;
	            int k = this.drawnChatLines.size();
	            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

	            if (k > 0)
	            {
	                if (this.getChatOpen())
	                {
	                    flag = true;
	                }

	                float f1 = this.getChatScale();
	                int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
	                GlStateManager.pushMatrix();
	                GlStateManager.translate(2.0F, 20.0F, 0.0F);
	                GlStateManager.scale(f1, f1, 1.0F);

	                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1)
	                {
	                    ChatLine chatline = (ChatLine)this.drawnChatLines.get(i1 + this.scrollPos);

	                    if (chatline != null)
	                    {
	                        int j1 = updateCounter - chatline.getUpdatedCounter();

	                        if (j1 < 200 || flag)
	                        {
	                            double d0 = (double)j1 / 200.0D;
	                            d0 = 1.0D - d0;
	                            d0 = d0 * 10.0D;
	                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
	                            d0 = d0 * d0;
	                            int l1 = (int)(255.0D * d0);

	                            if (flag)
	                            {
	                                l1 = 255;
	                            }

	                            l1 = (int)((float)l1 * f);
	                            ++j;

	                            if (l1 > 3) {
									GL11.glPushMatrix();

									int i2 = 0;
									int j2 = -i1 * 9;

									if (HackManager.getHack("Animation").isToggled() && Animation.chat.getValue()
											&& !flag) {
										if (j1 <= 20) {
											GL11.glTranslatef((float) (-(l + 4) * easeInQuart(1 - (j1 / 20.0))), 0F, 0F);
										}
										if (j1 >= 180) {
											GL11.glTranslatef((float) (-(l + 4) * easeInQuart((j1 - 180) / 20.0)), 0F, 0F);
										}
									}

									if (!HackManager.getHack("ChatRect").isToggled()) {
										 Utils.rect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
									}

									GlStateManager.enableBlend();
									String s = chatline.getChatComponent().getFormattedText();
	                          
									this.mc.fontRendererObj.drawStringWithShadow(
											s, (float) i2, (float) (j2 - 8),
											16777215 + (l1 << 24));
									GlStateManager.disableAlpha();
									GlStateManager.disableBlend();

									GL11.glPopMatrix();
								}
	                        }
	                    }
	                }

	                if (flag)
	                {
	                    int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
	                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
	                    int l2 = k * k2 + k;
	                    int i3 = j * k2 + j;
	                    int j3 = this.scrollPos * i3 / k;
	                    int k1 = i3 * i3 / l2;

	                    if (l2 != i3)
	                    {
	                        int k3 = j3 > 0 ? 170 : 96;
	                        int l3 = this.isScrolled ? 13382451 : 3355562;
	                        Gui.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
	                        Gui.drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
	                    }
	                }

	                GlStateManager.popMatrix();
	            }
	        }
	}

	double easeInQuart(double x) {
		return x * x * x * x;
	}

}

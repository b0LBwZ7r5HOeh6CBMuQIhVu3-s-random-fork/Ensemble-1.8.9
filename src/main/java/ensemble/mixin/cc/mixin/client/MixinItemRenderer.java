package ensemble.mixin.cc.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.mods.Animation;
import it.fktcod.ktykshrk.module.mods.Aura;
import it.fktcod.ktykshrk.module.mods.KillAura;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    ///////// FLUX
    public float rotateDirection = 0;
    public float delta;
    public float shaderDelta;
    @Shadow
    private Minecraft mc;
    @Shadow
    private float equippedProgress;
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private ItemStack itemToRender;
    @Shadow
    private RenderManager renderManager;

    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {

        float f = 1.0F
                - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch
                + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw
                + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;

        float var16 = MathHelper.sin(f1 * f1 * (float) Math.PI);

        final float f16 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {
            final KillAura killAura = (KillAura) HackManager.getHack("KillAura");

            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0
                    || (itemToRender.getItem() instanceof ItemSword && killAura.getBlockState())) {
                EnumAction enumaction = killAura.getBlockState() ? EnumAction.BLOCK
                        : this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f1);
                        break;
                    case BLOCK:
                        if (Animation.modeValue.getMode("Hanabi").isToggled()) {
                            // Hanabi
                            this.transformFirstPersonItem(0.1f, equippedProgress);
                            this.doBlockTransformations();
                            float var15 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
                            GlStateManager.translate(-0.0f, -0.3f, 0.4f);
                            GlStateManager.rotate((-var15) * 22.5f, -9.0f, -0.0f, 9.0f);
                            GlStateManager.rotate((-var15) * 10.0f, 1.0f, -0.4f, -0.5f);
                        } else if (Animation.modeValue.getMode("Jello").isToggled()) {
                            GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                            GlStateManager.translate(0.0F, 0 * -0.6F, 0.0F);
                            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                            float var3 = MathHelper.sin(0.0F * 0.0F * 3.1415927F);
                            float var4 = MathHelper.sin(MathHelper.sqrt_float(0.0F) * 3.1415927F);
                            GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
                            GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.scale(0.4F, 0.4F, 0.4F);

                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                            GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                            // ////System.out.println(var4 > 0.5 ? 1-var4 : var4);
                            int alpha = (int) Math.min(255,
                                    ((System.currentTimeMillis() % 255) > 255 / 2
                                            ? (Math.abs(Math.abs(System.currentTimeMillis()) % 255 - 255))
                                            : System.currentTimeMillis() % 255) * 2);
                            // float f5 = (var4 > 0.5 ? 1 - var4 : var4);
                            // ////System.out.println(alpha/6);
                            GlStateManager.translate(0.3f, -0.0f, 0.40f);
                            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                            GlStateManager.translate(0, 0.5f, 0);

                            GlStateManager.rotate(90, 1.0f, 0.0f, -1.0f);
                            GlStateManager.translate(0.6f, 0.5f, 0);
                            GlStateManager.rotate(-90, 1.0f, 0.0f, -1.0f);

                            GlStateManager.rotate(-10, 1.0f, 0.0f, -1.0f);
                            /// GlStateManager.rotate((- f5) * 10.0f, 10.0f, 10.0f, -9.0f);
                            // GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);

                            // GlStateManager.translate(0, 0, -0.5);
                            GlStateManager.rotate(mc.thePlayer.isSwingInProgress ? -alpha / 5f : 1, 1.0f, -0.0f, 1.0f);
                        } else if (Animation.modeValue.getMode("Tap").isToggled()) {
                            float smooth = (f1 * 0.8f - (f1 * f1) * 0.8f);
                            GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                            GlStateManager.translate(0.0F, f * -0.15F, 0.0F);
                            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                            float var3 = MathHelper.sin(f1 * f1 * (float) Math.PI);
                            float var4 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float) Math.PI);
                            GlStateManager.rotate(smooth * -90.0F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.scale(0.37F, 0.37F, 0.37F);
                            this.func_178103_d();

                        } else if (Animation.modeValue.getMode("Slide").isToggled()) {

                            float smooth = (f1 * 0.8f - (f1 * f1) * 0.8f);
                            GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                            GlStateManager.translate(0.0F, f * -0.6F, 0.0F);
                            GlStateManager.rotate(45.0F, 0.0F, 2 + smooth * 0.5f, smooth * 3);
                            float var3 = MathHelper.sin(f1 * f1 * (float) Math.PI);
                            float var4 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float) Math.PI);
                            GlStateManager.rotate(0f, 0.0F, 1.0F, 0.0F);
                            GlStateManager.scale(0.37F, 0.37F, 0.37F);
                            this.func_178103_d();

                        } else if (Animation.modeValue.getMode("Push").isToggled()) {
                            this.transformFirstPersonItem(f, 0.0F);
                            this.func_178103_d();
                            GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F) * 35.0F, -8.0F, -0.0F, 9.0F);
                            GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F) * 10.0F, 1.0F, -0.4F, -0.5F);
                        } else if (Animation.modeValue.getMode("Butter").isToggled()) {
                            this.transformFirstPersonItem(f * 0.5f, 0.0f);
                            GlStateManager.rotate(-f16 * -74.0f / 4.0f, -8.0f, -0.0f, 9.0f);
                            GlStateManager.rotate(-f16 * 15.0f, 1.0f, f16 / 2.0f, -0.0f);
                            this.func_178103_d();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
                        } else if (Animation.modeValue.getMode("Sigma").isToggled()) {
                            this.func_178096_b(f * 0.5f, 0);
                            GlStateManager.rotate(-var16 * 55 / 2.0F, -8.0F, -0.0F, 9.0F);
                            GlStateManager.rotate(-var16 * 45, 1.0F, var16 / 2, -0.0F);
                            this.func_178103_d();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                        } else if(Animation.modeValue.getMode("NONE").isToggled()){
							transformFirstPersonItem(0F,0F);
							doBlockTransformations();
						}else if(Animation.modeValue.getMode("Swank").isToggled()){
							GL11.glTranslated(-0.1, 0.15, 0.0);
							this.transformFirstPersonItem(f / 0.15f, f1);
							final float rot = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
							GlStateManager.rotate(rot * 30.0f, 2.0f, -rot, 9.0f);
							GlStateManager.rotate(rot * 35.0f, 1.0f, -rot, -0.0f);
							this.doBlockTransformations();
							break;
						}

                        else {
                            this.transformFirstPersonItem(f + 0.1F, f1);
                            this.doBlockTransformations();
                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);

                        }
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, f1);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            } else {
                if (!HackManager.getHack("Animation").isToggled())
                    this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }


    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.42F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    /**
     * @author
     */

    /*
     * public void renderItemInFirstPerson_1(float partialTicks) { float f = 1.0F -
     * (this.prevEquippedProgress + (this.equippedProgress -
     * this.prevEquippedProgress) * partialTicks); AbstractClientPlayer
     * entityplayersp = this.mc.thePlayer; float f1 =
     * entityplayersp.getSwingProgress(partialTicks); float f2 =
     * entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch -
     * entityplayersp.prevRotationPitch) * partialTicks; float f3 =
     * entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw -
     * entityplayersp.prevRotationYaw) * partialTicks; float var2 = 1.0F -
     * (this.prevEquippedProgress + (this.equippedProgress -
     * this.prevEquippedProgress) * partialTicks); EntityPlayerSP var3 =
     * this.mc.thePlayer; float var4 = var3.getSwingProgress(partialTicks);
     *
     * this.rotateArroundXAndY(f2, f3); this.setLightMapFromPlayer(entityplayersp);
     * this.rotateWithPlayerRotations((EntityPlayerSP) entityplayersp,
     * partialTicks); GlStateManager.enableRescaleNormal();
     * GlStateManager.pushMatrix();
     *
     * if (this.itemToRender != null) { if (this.itemToRender.getItem() ==
     * Items.filled_map) { this.renderItemMap(entityplayersp, f2, f, f1); } else if
     * (entityplayersp.getItemInUseCount() > 0) { EnumAction enumaction =
     * this.itemToRender.getItemUseAction();
     *
     * switch (enumaction) { case NONE: this.transformFirstPersonItem(f, 0.0F);
     * break;
     *
     * case EAT: case DRINK: this.performDrinking(entityplayersp, partialTicks);
     * this.transformFirstPersonItem(f, f1); break;
     *
     * case BLOCK: renderblock(f, f1); if
     * (Animation.modeValue.getMode("Slide").isToggled()) {
     * this.transformFirstPersonItem(0, 0.0f); this.func_178103_d(); float var9 =
     * MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
     * GlStateManager.translate(-0.05f, -0.0f, 0.35f); GlStateManager.rotate(-var9 *
     * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f); GlStateManager.rotate(-var9 *
     * (float) 70.0, 1.0f, -0.4f, -0.0f); } else if
     * (Animation.modeValue.getMode("Lucky").isToggled()) {
     * this.transformFirstPersonItem(0, 0.0f); this.func_178103_d(); float var9 =
     * MathHelper.sin(MathHelper.sqrt_float(f1) * 0.3215927f);
     * GlStateManager.translate(-0.05f, -0.0f, 0.3f); GlStateManager.rotate(-var9 *
     * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f); GlStateManager.rotate(-var9 *
     * (float) 70.0, 1.0f, -0.4f, -0.0f); } else if
     * (Animation.modeValue.getMode("Ohare").isToggled()) { // Ohare float f6 =
     * MathHelper.sin((MathHelper.sqrt_float(f1) * 3.1415927f));
     * GL11.glTranslated(-0.05D, 0.0D, -0.25); this.transformFirstPersonItem(f / 2,
     * 0.0f); GlStateManager.rotate(-f6 * 60.0F, 2.0F, -f6 * 2, -0.0f);
     * this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Wizzard").isToggled()) { // Wizzard float f6 =
     * MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
     * this.transformFirstPersonItem(f / 3, 0.0f); GlStateManager.rotate(f6 * 30.0F
     * / 1.0F, f6 / -1.0F, 1.0F, 0.0F); GlStateManager.rotate(f6 * 10.0F / 10.0F,
     * -f6 / -1.0F, 1.0F, 0.0F); GL11.glTranslated(0.0D, 0.4D, 0.0D);
     * this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Lennox").isToggled()) { // Lennox float f6 =
     * MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
     * GL11.glTranslated(0.0D, 0.125D, -0.1D); this.transformFirstPersonItem(f / 3,
     * 0.0F); GlStateManager.rotate(-f6 * 75.0F / 4.5F, f6 / 3.0F, -2.4F, 5.0F);
     * GlStateManager.rotate(-f6 * 75.0F, 1.5F, f6 / 3.0F, -0.0F);
     * GlStateManager.rotate(f6 * 72.5F / 2.25F, f6 / 3.0F, -2.7F, 5.0F);
     * this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Leaked").isToggled()) { // Leaked
     * this.transformFirstPersonItem(f, 0); this.func_178103_d();
     * GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) *
     * 3.141592653589793F) * 30.0F, 0.5F, 0.5F, 0); } else if
     * (Animation.modeValue.getMode("Avatar").isToggled()) { // Avatar
     * this.avatar(f, f1); this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Push").isToggled()) {
     * this.transformFirstPersonItem(f, 0.0F); this.func_178103_d();
     * GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F)
     * * 35.0F, -8.0F, -0.0F, 9.0F);
     * GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F)
     * * 10.0F, 1.0F, -0.4F, -0.5F); } break;
     *
     * case BOW: this.transformFirstPersonItem(f, f1);
     * this.doBowTransformations(partialTicks, entityplayersp); } } else { if
     * (((KillAura.autoblockValue.getValue() && KillAura.target != null) ||
     * this.mc.gameSettings.keyBindUseItem.isKeyDown()) && Animation.all.getValue())
     * { renderblock(f, f1); if (Animation.modeValue.getMode("Slide").isToggled()) {
     * // Slide this.transformFirstPersonItem(0, 0.0f); this.func_178103_d(); float
     * var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
     * GlStateManager.translate(-0.05f, -0.0f, 0.35f); GlStateManager.rotate(-var9 *
     * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f); GlStateManager.rotate(-var9 *
     * (float) 70.0, 1.0f, -0.4f, -0.0f); } else if
     * (Animation.modeValue.getMode("Lucky").isToggled()) { // Lucky
     * this.transformFirstPersonItem(0, 0.0f); this.func_178103_d(); float var9 =
     * MathHelper.sin(MathHelper.sqrt_float(f1) * 0.3215927f);
     * GlStateManager.translate(-0.05f, -0.0f, 0.3f); GlStateManager.rotate(-var9 *
     * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f); GlStateManager.rotate(-var9 *
     * (float) 70.0, 1.0f, -0.4f, -0.0f); } else if
     * (Animation.modeValue.getMode("Ohare").isToggled()) { // Ohare float f6 =
     * MathHelper.sin((MathHelper.sqrt_float(f1) * 3.1415927f));
     * GL11.glTranslated(-0.05D, 0.0D, -0.25); this.transformFirstPersonItem(f / 2,
     * 0.0f); GlStateManager.rotate(-f6 * 60.0F, 2.0F, -f6 * 2, -0.0f);
     * this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Wizzard").isToggled()) { // Wizzard float f6 =
     * MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
     * this.transformFirstPersonItem(f / 3, 0.0f); GlStateManager.rotate(f6 * 30.0F
     * / 1.0F, f6 / -1.0F, 1.0F, 0.0F); GlStateManager.rotate(f6 * 10.0F / 10.0F,
     * -f6 / -1.0F, 1.0F, 0.0F); GL11.glTranslated(0.0D, 0.4D, 0.0D);
     * this.func_178103_d(); } else if
     * (Animation.modeValue.getMode("Lennox").isToggled()) { // Lennox float f6 =
     * MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
     * GL11.glTranslated(0.0D, 0.125D, -0.1D); this.transformFirstPersonItem(f / 3,
     * 0.0F); GlStateManager.rotate(-f6 * 75.0F / 4.5F, f6 / 3.0F, -2.4F, 5.0F);
     * GlStateManager.rotate(-f6 * 75.0F, 1.5F, f6 / 3.0F, -0.0F);
     * GlStateManager.rotate(f6 * 72.5F / 2.25F, f6 / 3.0F, -2.7F, 5.0F);
     * this.func_178103_d(); } } else { this.doItemUsedTransformations(f1);
     * this.transformFirstPersonItem(f, f1); }
     *
     * }
     *
     * this.renderItem(entityplayersp, this.itemToRender,
     * ItemCameraTransforms.TransformType.FIRST_PERSON); } else if
     * (!entityplayersp.isInvisible()) { this.renderPlayerArm(entityplayersp, f,
     * f1); }
     *
     * GlStateManager.popMatrix(); GlStateManager.disableRescaleNormal();
     * RenderHelper.disableStandardItemLighting(); }
     */
    private void avatar(float equipProgress, float swingProgress) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -40.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    private void renderblock(float swingProgress, float equippedProgress) {
        final float f16 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);

        if (!HackManager.getHack("Animation").isToggled()) {
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.doBlockTransformations();
            return;
        }
        if (Animation.modeValue.getMode("Sigma").isToggled()) {
            // Sigma

            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.doBlockTransformations();
            float var14 = MathHelper.sin(equippedProgress * equippedProgress * 3.1415927F);
            float var15 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927F);
            GlStateManager.translate(-0F, 0.4F, 1.0F);
            GlStateManager.rotate(-var15 * 22.5F, -9.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-var15 * 10.0F, 1.0F, -0.4F, -0.5F);
        } else if (Animation.modeValue.getMode("Debug").isToggled()) {
            // Debug
            this.transformFirstPersonItem(0.2f, equippedProgress);
            this.doBlockTransformations();
            GlStateManager.translate(-0.5, 0.2, 0.0);
        } else if (Animation.modeValue.getMode("Vanilla").isToggled()) {
            // Vanilla
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.doBlockTransformations();
        } else if (Animation.modeValue.getMode("Luna").isToggled()) {
            // Luna
            this.transformFirstPersonItem(swingProgress, 0.0F);
            this.doBlockTransformations();
            final float sin = MathHelper.sin(equippedProgress * equippedProgress * 3.1415927f);
            final float sin2 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.translate(-0.2f, 0.45f, 0.25f);
            GlStateManager.rotate(-sin2 * 20.0f, -5.0f, -5.0f, 9.0f);
        } else if (Animation.modeValue.getMode("1.7").isToggled()) {
            this.transformFirstPersonItem(swingProgress - 0.3F, equippedProgress);
            this.func_178103_d();
        } else if (Animation.modeValue.getMode("Swang").isToggled()) {
            // Swang
            this.transformFirstPersonItem(swingProgress / 2.0F, equippedProgress);
            float var15;
            var15 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927F);
            GlStateManager.rotate(var15 * 30.0F / 2.0F, -var15, -0.0F, 9.0F);
            GlStateManager.rotate(var15 * 40.0F, 1.0F, -var15 / 2.0F, -0.0F);

            this.doBlockTransformations();
        } else if (Animation.modeValue.getMode("Swank").isToggled()) {
            // Swank
            this.transformFirstPersonItem(swingProgress / 2.0F, equippedProgress);
            float var15;
            var15 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
            GlStateManager.rotate(var15 * 30.0F, -var15, -0.0F, 9.0F);
            GlStateManager.rotate(var15 * 40.0F, 1.0F, -var15, -0.0F);

            this.doBlockTransformations();
        } else if (Animation.modeValue.getMode("Swong").isToggled()) {
            // Swong
            this.transformFirstPersonItem(swingProgress / 2.0F, 0.0F);
            float var15;
            var15 = MathHelper.sin(equippedProgress * equippedProgress * 3.1415927F);
            float var151 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927F);
            GlStateManager.rotate(-var151 * 40.0F / 2.0F, var151 / 2.0F, -0.0F, 9.0F);
            GlStateManager.rotate(-var151 * 30.0F, 1.0F, var151 / 2.0F, -0.0F);

            this.doBlockTransformations();
        } else if (Animation.modeValue.getMode("Jigsaw").isToggled()) {
            // Jigsaw
            this.transformFirstPersonItem(0.1f, equippedProgress);
            this.doBlockTransformations();
            GlStateManager.translate(-0.5, 0, 0);
        } else if (Animation.modeValue.getMode("Hanabi").isToggled()) {
            // Hanabi
            this.transformFirstPersonItem(0.1f, equippedProgress);
            this.doBlockTransformations();
            float var15 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
            GlStateManager.translate(-0.0f, -0.3f, 0.4f);
            GlStateManager.rotate((-var15) * 22.5f, -9.0f, -0.0f, 9.0f);
            GlStateManager.rotate((-var15) * 10.0f, 1.0f, -0.4f, -0.5f);
        } else if (Animation.modeValue.getMode("Jello").isToggled()) {
            // Jello
            GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
            GlStateManager.translate(0.0F, 0 * -0.6F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            float var3 = MathHelper.sin(0.0F * 0.0F * 3.1415927F);
            float var4 = MathHelper.sin(MathHelper.sqrt_float(0.0F) * 3.1415927F);
            GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(0.4F, 0.4F, 0.4F);

            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
            GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
            // ////System.out.println(var4 > 0.5 ? 1-var4 : var4);
            int alpha = (int) Math.min(255,
                    ((System.currentTimeMillis() % 255) > 255 / 2
                            ? (Math.abs(Math.abs(System.currentTimeMillis()) % 255 - 255))
                            : System.currentTimeMillis() % 255) * 2);
            // float f5 = (var4 > 0.5 ? 1 - var4 : var4);
            // ////System.out.println(alpha/6);
            GlStateManager.translate(0.3f, -0.0f, 0.40f);
            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0, 0.5f, 0);

            GlStateManager.rotate(90, 1.0f, 0.0f, -1.0f);
            GlStateManager.translate(0.6f, 0.5f, 0);
            GlStateManager.rotate(-90, 1.0f, 0.0f, -1.0f);

            GlStateManager.rotate(-10, 1.0f, 0.0f, -1.0f);
            /// GlStateManager.rotate((- f5) * 10.0f, 10.0f, 10.0f, -9.0f);
            // GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);

            // GlStateManager.translate(0, 0, -0.5);
            GlStateManager.rotate(mc.thePlayer.isSwingInProgress ? -alpha / 5f : 1, 1.0f, -0.0f, 1.0f);
        } else if (Animation.modeValue.getMode("Chill").isToggled()) {
            // Chill
            this.transformFirstPersonItem(swingProgress / 2.0f - 0.18f, 0.0f);
            GL11.glRotatef(f16 * 60.0f / 2.0f, -f16 / 2.0f, -0.0f, -16.0f);
            GL11.glRotatef(-f16 * 30.0f, 1.0f, f16 / 2.0f, -1.0f);
            this.func_178103_d();
        } else if (Animation.modeValue.getMode("Tiny Whack").isToggled()) {
            // Tiny Whack
            this.transformFirstPersonItem(swingProgress / 2.0f - 0.18f, 0.0f);
            GL11.glRotatef(-f16 * 40.0f / 2.0f, f16 / 2.0f, -0.0f, 9.0f);
            GL11.glRotatef(-f16 * 30.0f, 1.0f, f16 / 2.0f, -0.0f);
            this.func_178103_d();
        } else if (Animation.modeValue.getMode("Long Hit").isToggled()) {
            // Long Hit
            this.transformFirstPersonItem(swingProgress, 0.0f);
            this.func_178103_d();
            final float var19 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
            GlStateManager.translate(-0.05f, 0.6f, 0.3f);
            GlStateManager.rotate(-var19 * 70.0f / 2.0f, -8.0f, -0.0f, 9.0f);
            GlStateManager.rotate(-var19 * 70.0f, 1.5f, -0.4f, -0.0f);
        } else if (Animation.modeValue.getMode("Long Hit").isToggled()) {
            this.transformFirstPersonItem(swingProgress, 0.0f);
            this.func_178103_d();
            final float var19 = MathHelper.sin(MathHelper.sqrt_float(equippedProgress) * 3.1415927f);
            GlStateManager.translate(-0.05f, 0.6f, 0.3f);
            GlStateManager.rotate(-var19 * 70.0f / 2.0f, -8.0f, -0.0f, 9.0f);
            GlStateManager.rotate(-var19 * 70.0f, 1.5f, -0.4f, -0.0f);
        } else if (Animation.modeValue.getMode("Butter").isToggled()) {
            // Butter
            this.transformFirstPersonItem(swingProgress * 0.5f, 0.0f);
            GlStateManager.rotate(-f16 * -74.0f / 4.0f, -8.0f, -0.0f, 9.0f);
            GlStateManager.rotate(-f16 * 15.0f, 1.0f, f16 / 2.0f, -0.0f);
            this.func_178103_d();
            GL11.glTranslated(1.2, 0.3, 0.5);
            GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
        }

    }

    public float getRotateDirection() {// AllitemRotate->Rotate
        rotateDirection = rotateDirection + delta;
        if (rotateDirection > 360)
            rotateDirection = 0;
        return rotateDirection;
    }
    ///// FLUX

    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178096_1_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927f);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927f);
        GlStateManager.rotate(var3 * -34.0f, 0.0f, 1.0f, 0.2f);
        GlStateManager.rotate(var4 * -20.7f, 0.2f, 0.1f, 1.0f);
        GlStateManager.rotate(var4 * -68.6f, 1.3f, 0.1f, 0.2f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private boolean canBlockItem() {
        try {
            return this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void func_178103_d() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack,
                                    ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void rotateArroundXAndY(float f2, float f3);

    @Shadow
    public abstract void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_,
                                       float p_178097_4_);



    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer var1);

    @Shadow
    protected abstract void doItemUsedTransformations(float f1);

    @Shadow
    protected abstract void doBowTransformations(float p_178098_1_, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayersp, float partialTicks);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float p_178104_2_);


    private void doItemRenderGLTranslate() {
        GlStateManager.translate(Animation.posx.getValue(), Animation.posy.getValue(), Animation.posz.getValue());
    }

    private void doItemRenderGLScale() {
        GlStateManager.scale(Animation.scalevalue.getValue(), Animation.scalevalue.getValue(), Animation.scalevalue.getValue());
    }

	@Overwrite
	private void transformFirstPersonItem(float equipProgress, float swingProgress) {
		doItemRenderGLTranslate();
		GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
		GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
		float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
		float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
		GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
		doItemRenderGLScale();
	}
}

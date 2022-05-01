package it.fktcod.ktykshrk.utils;


import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Mappings
{
    public static String timer = isMCP() ? "timer" : "field_71428_T";
    public static String anti = isMCP() ? "MovementInput" : "field_71158_b";
    public static String isInWeb = isMCP() ? "isInWeb" : "field_70134_J";
    public static String registerReloadListener = isMCP() ? "registerReloadListener" : "func_110542_a";
	public static String session = isNotObfuscated() ? "session" : "field_71449_j";
	public static String yaw = isNotObfuscated() ? "yaw" : "field_149476_e";
	public static String pitch = isNotObfuscated() ? "pitch" : "field_149473_f";
	public static String rightClickDelayTimer = isNotObfuscated() ? "rightClickDelayTimer" : "field_71467_ac";
	public static String getPlayerInfo = isNotObfuscated() ? "getPlayerInfo" : "func_175155_b";
	public static String playerTextures = isNotObfuscated() ? "playerTextures" : "field_187107_a";
	public static String currentGameType = isNotObfuscated() ? "currentGameType" : "field_78779_k";
	public static String connection = isNotObfuscated() ? "connection" : "field_78774_b";
	public static String blockHitDelay = isNotObfuscated() ? "blockHitDelay" : "field_78781_i";
	public static String curBlockDamageMP = isNotObfuscated() ? "curBlockDamageMP" : "field_78770_f";
	public static String isHittingBlock = isNotObfuscated() ? "isHittingBlock" : "field_78778_j";
	public static String onUpdateWalkingPlayer = isNotObfuscated() ? "onUpdateWalkingPlayer" : "func_175161_p";
	
	public static final Field delayTimer = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");
    public static final Field running = ReflectionHelper.findField(Minecraft.class, "field_71425_J", "running");

    public static final Field pressed = ReflectionHelper.findField(KeyBinding.class, "field_74513_e", "pressed");

    public static final Field theShaderGroup = ReflectionHelper.findField(EntityRenderer.class, "field_147707_d", "theShaderGroup");

    public static final Field listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");


    public Mappings()
    {
    }
    public static boolean isNotObfuscated() {
        try { return Minecraft.class.getDeclaredField("instance") != null;
        } catch (Exception ex) { return false; }
    }
    private static boolean isMCP()
    {
        try
        {
            return ReflectionHelper.findField(Minecraft.class, new String[] {"theMinecraft"}) != null;
        }
        catch (Exception var1)
        {
            return false;
        }
    }
    
    
}

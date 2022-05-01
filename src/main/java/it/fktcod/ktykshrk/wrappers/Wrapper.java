package it.fktcod.ktykshrk.wrappers;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.utils.Mappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class Wrapper {

	public static volatile Wrapper INSTANCE = new Wrapper();
	public static boolean canSendMotionPacket = true;
	public static Timer timer=ReflectionHelper.getPrivateValue(Minecraft.class, Wrapper.INSTANCE.mc(), new String[]{Mappings.timer});

    public static double getRenderPosX() {
        return (Double)getField("renderPosX", "field_78725_b", Minecraft.getMinecraft().getRenderManager());
    }

    public static double getRenderPosY() {
        return (Double)getField("renderPosY", "field_78726_c", Minecraft.getMinecraft().getRenderManager());
    }

    public static double getRenderPosZ() {
        return (Double)getField("renderPosZ", "field_78723_d", Minecraft.getMinecraft().getRenderManager());
    }

    public static void rightClickMouse(final Minecraft mc) {
        invoke(mc, "rightClickMouse" , "func_147121_ag", new Class[]{}, new Object[] {});
    }
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().thePlayer;
    }
    public WorldClient world() {
        return Wrapper.INSTANCE.mc().theWorld;
    }

    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }

    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRendererObj;
    }
    
    public void sendPacket(Packet packet) {
        this.player().sendQueue.addToSendQueue(packet);
    }
    
    public InventoryPlayer inventory() { 
		return this.player().inventory; 
	}
	
	public PlayerControllerMP controller() { 
		return Wrapper.INSTANCE.mc().playerController; 
	}

	 public int r(int init) 
	 {
	    	return new Random().nextInt(init);
	 }

    public static float getCurBlockDamageMP(Minecraft mc){
        try {
            float curv;
            Field m = PlayerControllerMP.class.getDeclaredField(Core.isObfuscate ?"field_78770_f":"curBlockDamageMP");
            m.setAccessible(true);
            curv= (float) m.get(mc.playerController);
            m.setAccessible(false);
            return curv;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0f;
    }
    public static void setCurBlockDamageMP(Minecraft mc,float damage){
        try {
            float curv;
            Field m = PlayerControllerMP.class.getDeclaredField(Core.isObfuscate ?"field_78770_f":"curBlockDamageMP");
            m.setAccessible(true);
            m.set(mc.playerController,damage);
            m.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void setBlockHitDelay(Minecraft mc,int delay){
        try {
            Field m = PlayerControllerMP.class.getDeclaredField(Core.isObfuscate?"field_78781_i":"blockHitDelay");
            m.setAccessible(true);
            m.set(mc.playerController,delay);
            m.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getField(final String field, final String obfName, final Object instance) {
        final Field fField = ReflectionHelper.findField((Class)instance.getClass(), Core.isObfuscate? new String[] { obfName }: new String[] { field }

        );
        fField.setAccessible(true);
        try {
            return fField.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(final Object target, final String methodName, final String obfName, final Class[] methodArgs, final Object[] args) {
        final Class clazz = target.getClass();
        final Method method = ReflectionHelper.findMethod(clazz, target,
                Core.isObfuscate?new String[] {  obfName }:new String[] { methodName }
                , methodArgs);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

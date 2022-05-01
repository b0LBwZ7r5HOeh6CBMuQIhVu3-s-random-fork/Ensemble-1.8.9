package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.util.Vector;

import static org.objectweb.asm.Opcodes.*;

public class Disablers extends Module{
	public ClassVisitor cv;
	public Disablers() {
		super("Disablers", HackCategory.ANOTHER);
	}
	
	@Override
	public String getDescription() {
		return "Disablers For AntiCheats.";
	}
	
	@Override
	public void onEnable() {
		ClassLoader contextClassLoader = null;
		for (final Thread thread : Thread.getAllStackTraces().keySet()) {
			//System.out.println("=======================================================================");
			//System.out.println("thread.getName().toLowerCase() = " + thread.getName().toLowerCase());

			contextClassLoader = thread.getContextClassLoader();

			try {

				Field f = contextClassLoader.getClass().getDeclaredField("classes");
				f.setAccessible(true);
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				Vector<Class> classes =  (Vector<Class>) f.get(classLoader);

				for (Class<?> clazz: classes) {
					String className = clazz.getName();
					//System.out.println("className = " + className);
				}

			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}


		}

		if (contextClassLoader == null) {
			//System.out.println("NONE");
		}



	}

	@Override
	public void onClientTick(TickEvent.ClientTickEvent event) {


	}





	
	
}

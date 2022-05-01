package it.fktcod.ktykshrk;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import it.fktcod.ktykshrk.utils.Cr4sh;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.ForgeMod;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import sun.management.VMManagement;
import sun.misc.Unsafe;

import static it.fktcod.ktykshrk.Core.getHWID;
import static org.objectweb.asm.Opcodes.*;

public class LoadClient {


	public static boolean isCheck =false;
	/*
	 * public static void L(String string, String string2) { Logger logger =
	 * LogManager.getLogger(); logger.info("Try Inject!"); try {
	 * logger.info("Inject!"); Core.class.newInstance(); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (InstantiationException e) { e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */
	private static final Unsafe unsafe;
	private static Method findNative;
	private static ClassLoader classLoader;

	private static boolean ENABLE;

	private static final String[] naughtyFlags = {
			"-XBootclasspath",
			"-javaagent",
			"-Xdebug",
			"-agentlib",
			"-Xrunjdwp",
			"-Xnoagent",
			"-verbose",
			"-DproxySet",
			"-DproxyHost",
			"-DproxyPort",
			"-Djavax.net.ssl.trustStore",
			"-Djavax.net.ssl.trustStorePassword"
	};

	/* UnsafeProvider */
	static {
		Unsafe ref;
		try {
			Class<?> clazz = Class.forName("sun.misc.Unsafe");
			Field theUnsafe = clazz.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			ref = (Unsafe) theUnsafe.get(null);
		} catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			ref = null;
		}

		unsafe = ref;
	}

	/* CookieFuckery */
	public static void check() {
		if (!ENABLE) return;
		try {
			Field jvmField = ManagementFactory.getRuntimeMXBean().getClass().getDeclaredField("jvm");
			jvmField.setAccessible(true);
			VMManagement jvm = (VMManagement) jvmField.get(ManagementFactory.getRuntimeMXBean());
			List<String> inputArguments = jvm.getVmArguments();

			for (String arg : naughtyFlags) {
				for (String inputArgument : inputArguments) {
					if (inputArgument.contains(arg)) {
						//System.out.println("Found illegal program arguments!");
						dumpDetected();
					}
				}
			}
			try {
				byte[] bytes = createDummyClass("java/lang/instrument/Instrumentation");
				unsafe.defineClass("java.lang.instrument.Instrumentation", bytes, 0, bytes.length, null, null);
			} catch (Throwable e) {
				e.printStackTrace();
				dumpDetected();
			}
			if (isClassLoaded("sun.instrument.InstrumentationImpl")) {
				//System.out.println("Found sun.instrument.InstrumentationImpl!");
				dumpDetected();
			}

			byte[] bytes = createDummyClass("dummy/class/path/MaliciousClassFilter");
			unsafe.defineClass("dummy.class.path.MaliciousClassFilter", bytes, 0, bytes.length, null, null); // Change this.
			System.setProperty("sun.jvm.hotspot.tools.jcore.filter", "dummy.class.path.MaliciousClassFilter"); // Change this.

			disassembleStruct();

		} catch (Throwable e) {
			e.printStackTrace();
			dumpDetected();
		}
	}

	private static boolean isClassLoaded(@SuppressWarnings("SameParameterValue") String clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method m = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
		m.setAccessible(true);
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		ClassLoader scl = ClassLoader.getSystemClassLoader();
		return m.invoke(cl, clazz) != null || m.invoke(scl, clazz) != null;
	}


	/* DummyClassProvider */
	private static byte[] createDummyClass(String name) {
		ClassNode classNode = new ClassNode();
		classNode.name = name.replace('.', '/');
		classNode.access = ACC_PUBLIC;
		classNode.version = V1_8;
		classNode.superName = "java/lang/Object";

		List<MethodNode> methods = new ArrayList<>();
		MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);

		InsnList insn = new InsnList();
		insn.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insn.add(new LdcInsnNode("Nice try"));
		insn.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
		insn.add(new TypeInsnNode(NEW, "java/lang/Throwable"));
		insn.add(new InsnNode(DUP));
		insn.add(new LdcInsnNode("owned"));
		insn.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Throwable", "<init>", "(Ljava/lang/String;)V", false));
		insn.add(new InsnNode(ATHROW));

		methodNode.instructions = insn;

		methods.add(methodNode);
		classNode.methods = methods;

		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}

	private static void dumpDetected() {
		try {
			unsafe.putAddress(0, 0);
		} catch (Exception e) {}
		FMLCommonHandler.instance().exitJava(0, false); // Shutdown.
		Error error = new Error();
		error.setStackTrace(new StackTraceElement[]{});
		throw error;
	}

	/* StructDissasembler */
	private static void resolveClassLoader() throws NoSuchMethodException {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows")) {
			String vmName = System.getProperty("java.vm.name");
			String dll = vmName.contains("Client VM") ? "/bin/client/jvm.dll" : "/bin/server/jvm.dll";
			try {
				System.load(System.getProperty("java.home") + dll);
			} catch (UnsatisfiedLinkError e) {
				throw new RuntimeException(e);
			}
			classLoader = LoadClient.class.getClassLoader();
		} else {
			classLoader = null;
		}

		findNative = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);

		try {
			Class<?> cls = ClassLoader.getSystemClassLoader().loadClass("jdk.internal.module.IllegalAccessLogger");
			Field logger = cls.getDeclaredField("logger");
			unsafe.putObjectVolatile(cls, unsafe.staticFieldOffset(logger), null);
		} catch (Throwable t) {}

		findNative.setAccessible(true);
	}

	private static void setupIntrospection() throws Throwable {
		resolveClassLoader();
	}

	public static void disassembleStruct() {
		try {
			setupIntrospection();
			long entry = getSymbol("gHotSpotVMStructs");
			unsafe.putLong(entry, 0);
		} catch (Throwable t) {
			t.printStackTrace();
			dumpDetected();
		}
	}

	private static long getSymbol(String symbol) throws InvocationTargetException, IllegalAccessException {
		long address = (Long) findNative.invoke(null, classLoader, symbol);
		if (address == 0)
			throw new NoSuchElementException(symbol);

		return unsafe.getLong(address);
	}

	private static String getString(long addr) {
		if (addr == 0) {
			return null;
		}

		char[] chars = new char[40];
		int offset = 0;
		for (byte b; (b = unsafe.getByte(addr + offset)) != 0; ) {
			if (offset >= chars.length) chars = Arrays.copyOf(chars, offset * 2);
			chars[offset++] = (char) b;
		}

		return new String(chars, 0, offset);
	}

	private static void readStructs(Map<String, Set<Object[]>> structs) throws InvocationTargetException, IllegalAccessException {
		long entry = getSymbol("gHotSpotVMStructs");
		long typeNameOffset = getSymbol("gHotSpotVMStructEntryTypeNameOffset");
		long fieldNameOffset = getSymbol("gHotSpotVMStructEntryFieldNameOffset");
		long typeStringOffset = getSymbol("gHotSpotVMStructEntryTypeStringOffset");
		long isStaticOffset = getSymbol("gHotSpotVMStructEntryIsStaticOffset");
		long offsetOffset = getSymbol("gHotSpotVMStructEntryOffsetOffset");
		long addressOffset = getSymbol("gHotSpotVMStructEntryAddressOffset");
		long arrayStride = getSymbol("gHotSpotVMStructEntryArrayStride");

		for (; ; entry += arrayStride) {
			String typeName = getString(unsafe.getLong(entry + typeNameOffset));
			String fieldName = getString(unsafe.getLong(entry + fieldNameOffset));
			if (fieldName == null) break;

			String typeString = getString(unsafe.getLong(entry + typeStringOffset));
			boolean isStatic = unsafe.getInt(entry + isStaticOffset) != 0;
			long offset = unsafe.getLong(entry + (isStatic ? addressOffset : offsetOffset));

			Set<Object[]> fields = structs.get(typeName);
			if (fields == null) structs.put(typeName, fields = new HashSet<>());
			fields.add(new Object[]{fieldName, typeString, offset, isStatic});
		}
		long address = (Long) findNative.invoke(null, classLoader, 2);
		if (address == 0)
			throw new NoSuchElementException("");

		unsafe.getLong(address);
	}

	private static void readTypes(Map<String, Object[]> types, Map<String, Set<Object[]>> structs) throws InvocationTargetException, IllegalAccessException {
		long entry = getSymbol("gHotSpotVMTypes");
		long typeNameOffset = getSymbol("gHotSpotVMTypeEntryTypeNameOffset");
		long superclassNameOffset = getSymbol("gHotSpotVMTypeEntrySuperclassNameOffset");
		long isOopTypeOffset = getSymbol("gHotSpotVMTypeEntryIsOopTypeOffset");
		long isIntegerTypeOffset = getSymbol("gHotSpotVMTypeEntryIsIntegerTypeOffset");
		long isUnsignedOffset = getSymbol("gHotSpotVMTypeEntryIsUnsignedOffset");
		long sizeOffset = getSymbol("gHotSpotVMTypeEntrySizeOffset");
		long arrayStride = getSymbol("gHotSpotVMTypeEntryArrayStride");

		for (; ; entry += arrayStride) {
			String typeName = getString(unsafe.getLong(entry + typeNameOffset));
			if (typeName == null) break;

			String superclassName = getString(unsafe.getLong(entry + superclassNameOffset));
			boolean isOop = unsafe.getInt(entry + isOopTypeOffset) != 0;
			boolean isInt = unsafe.getInt(entry + isIntegerTypeOffset) != 0;
			boolean isUnsigned = unsafe.getInt(entry + isUnsignedOffset) != 0;
			int size = unsafe.getInt(entry + sizeOffset);

			Set<Object[]> fields = structs.get(typeName);
			types.put(typeName, new Object[]{typeName, superclassName, size, isOop, isInt, isUnsigned, fields});
		}
	}

	public static String BuildGetClassString(String Type){
		try {
			String TypeW = "[TYPE]["+Type+"][USERHWID][";
			String HWIDW = getHWID(true)+"]";
			String BuildString = "[Target][GETCLASS]"+TypeW+HWIDW;
			return BuildString;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		////System.out.println(result);
		return result;
	}

	public static void RLoad(String string, String string2) {
		check();

		try {

			String Send = BuildGetClassString("DC");
			//System.out.println(Send);
			String className = Core.Send("124.222.57.187", 19730,Send);
			className = getSubString(className,"[classname]","");
			//System.out.println(className);
			isCheck = true;
			Class Main = Class.forName(className);
			Main.newInstance();

		} catch (IllegalAccessException e) {
			new Cr4sh();
		} catch (InstantiationException e) {
			new Cr4sh();
		}  catch (ClassNotFoundException e) {
			isCheck = false;
			Class Main = null;
			try {

				String Send = BuildGetClassString("RC");
				String className = Core.Send("124.222.57.187", 19730,Send);
				////System.out.println(className);
				isCheck = true;
				className = getSubString(className,"[classname]","");
				Main = Class.forName(className);
				Main.newInstance();

			} catch (ClassNotFoundException ex) {
				isCheck = false;
				new Cr4sh();
			} catch (IllegalAccessException ex) {
				isCheck = false;
				new Cr4sh();
			} catch (InstantiationException ex) {
				isCheck = false;
				new Cr4sh();
			}
		}
	}
}

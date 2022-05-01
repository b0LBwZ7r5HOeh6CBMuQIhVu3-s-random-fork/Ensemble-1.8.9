package exs;

import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.*;
import java.io.*;
import java.security.*;
import java.util.*;
import java.lang.reflect.*;

public class EXS extends Thread{
    private final byte[][] classes;
    private final String user;
    private final String password;
    //public String IP = "121.43.230.232";
    public EXS(final byte[][] classes, final String user, final String password) {
        this.classes = classes;
        this.user = user;
        this.password = password;
        ////System.out.println("EXS LOADING");
    }

    public static String getCMDreturn(final String command) {
        String value = "";
        try {
            String line;
            while ((line = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream(), Charset.forName("GBK"))).readLine()) != null) {
                value = String.valueOf(new StringBuilder(String.valueOf(value)).append(line).append("\r\n"));
            }
        }
        catch (IOException ex) {
            return "null";
        }
        return value;
    }
    public static String Send(String IP, int Port, String Message) {

        try {
            Socket socket = new Socket(IP, Port);
            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            InputStream ips = socket.getInputStream();
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            String s = null;

            while ((s = br.readLine()) != null) {
                return s;
            }

            socket.close();
        } catch (Exception e) {
            //Display.setTitle("Failed Connect to The Server(0x66FF)");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void run() {


        try {
            ClassLoader contextClassLoader = null;
            for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread.getName().toLowerCase().equals("client thread")) {
                    contextClassLoader = thread.getContextClassLoader();
                }
            }
            if (contextClassLoader == null) {
                return;
            }
            this.setContextClassLoader(contextClassLoader);
            final Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
            declaredMethod.setAccessible(true);

            Class clazz = null;
            String Send = BuildGetClassString("RL");
            //System.out.println(Send);
            String className = Send("124.222.57.187", 19730,Send);
            //System.out.println(className);
            className = getSubString(className,"[classname]","");
            //System.out.println(className);
            Thread.sleep(1000);

            for (final byte[] array : this.classes) {
                final Class clazz2 = (Class)declaredMethod.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                if (clazz2 != null && clazz2.getName().contains(className)) {
                    clazz = clazz2;
                }
            }
            if (clazz != null) {
                clazz.getDeclaredMethod("RLoad", String.class, String.class).invoke(null, this.user, this.password);
            }

        }
        catch (Exception ex) {}
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
        return result;
    }
    public static int a(final byte[][] array, final String s, final String s2) {
        try {
            new EXS(array, s, s2).run();
        }
        catch (Exception ex) {}
        return 0;
    }

    public static String BuildGetClassString(String Type){
        try {
            String TypeW = "[TYPE]["+Type+"][USERHWID][";
            String HWIDW = getHWID(true)+"]";
            String BuildString =  "[Target][GETCLASS]"+TypeW+HWIDW;
            return BuildString;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[][] a(final int n) {
        return new byte[n][];
    }

    public static String getHWID(boolean isMD5) throws IOException, NoSuchAlgorithmException {
        String property = System.getProperty("os.name").toLowerCase();
        String cpuSerialNumber = getCPUSerialNumber();
        String hardDiskSerialNumber = InetAddress.getLocalHost().getHostName().toString();
        //System.out.println(hardDiskSerialNumber);
        String serial = cpuSerialNumber+hardDiskSerialNumber;
        if (isMD5) {
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] ciphertext = messageDigest.digest(serial.getBytes());
            return encodeHexString(ciphertext);
        } else {
            return serial;
        }

    }

    public static String getCPUSerialNumber() {
        String serial;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = sc.next();
        } catch (IOException e) {
            throw new RuntimeException("获取CPU序列号失败");
        }
        return serial;
    }
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

}

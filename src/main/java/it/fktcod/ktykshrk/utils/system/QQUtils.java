package it.fktcod.ktykshrk.utils.system;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

import java.util.HashMap;
import java.util.Map;

public class QQUtils {
    private static final String QQ_WINDOW_TEXT_PRE = "qqexchangewnd_shortcut_prefix_";
    private static final QQUtils.User32 user32;

    public QQUtils() { }

    public static Map<String, String> getLoginQQList() {
        final Map<String, String> map = new HashMap(5);
        user32.EnumWindows(new QQUtils.User32.WNDENUMPROC() {
            public boolean callback(Pointer hWnd, Pointer userData) {
                byte[] windowText = new byte[512];
                QQUtils.user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);
                if (QQUtils._filterQQInfo(wText)) {
                    map.put(hWnd.toString(), wText.substring(wText.indexOf("qqexchangewnd_shortcut_prefix_") + "qqexchangewnd_shortcut_prefix_".length()));
                }

                return true;
            }
        }, (Pointer)null);
        return map;
    }

    private static boolean _filterQQInfo(String windowText) {
        return windowText.startsWith("qqexchangewnd_shortcut_prefix_");
    }

    static {
        user32 = QQUtils.User32.INSTANCE;
    }

    public interface User32 extends StdCallLibrary {
        QQUtils.User32 INSTANCE = (QQUtils.User32)Native.loadLibrary("user32", QQUtils.User32.class);

        boolean EnumWindows(QQUtils.User32.WNDENUMPROC var1, Pointer var2);

        int GetWindowTextA(Pointer var1, byte[] var2, int var3);

        public interface WNDENUMPROC extends StdCallCallback {
            boolean callback(Pointer var1, Pointer var2);
        }
    }


}

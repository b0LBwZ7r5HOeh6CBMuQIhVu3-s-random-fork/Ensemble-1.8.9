package exs;

import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

//Ensemble Jar Loader
public class EJL {

    public static void load(){
        //外部jar所在位置
        String path = "file:D:\\Program File\\IDEA\\WorkSpase\\Test20191015\\out\\artifacts\\test191015\\test191015.jar";
        URLClassLoader urlClassLoader =null;
        Class<?> MyTest = null;
        try {
            //通过URLClassLoader加载外部jar
            urlClassLoader = new URLClassLoader(new URL[]{new URL(path)});
            //获取外部jar里面的具体类对象
            MyTest = urlClassLoader.loadClass("cn.hjljy.MyTest");
            //创建对象实例
            Object instance = MyTest.newInstance();
            //获取实例当中的方法名为show，参数只有一个且类型为string的public方法
            Method method = MyTest.getMethod("show", String.class);
            //传入实例以及方法参数信息执行这个方法
            Object ada = method.invoke(instance, "ada");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //卸载关闭外部jar
            try {
                urlClassLoader.close();
            } catch (IOException e) {
                //System.out.println("关闭外部jar失败："+e.getMessage());
            }
        }
    }
}

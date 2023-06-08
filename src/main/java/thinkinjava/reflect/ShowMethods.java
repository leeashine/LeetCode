package thinkinjava.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ShowMethods {
    public static void main(String[] args) {
        String className = "thinkinjava.reflect.ShowMethods";
        try {
            Class<?> c = Class.forName(className);
            Method[] methods = c.getMethods();
            Constructor<?>[] constructors = c.getConstructors();
            Field[] fields = c.getFields();
            for (Method method : methods) {
                System.out.println(method.toString());
            }
            for (Constructor<?> constructor : constructors) {
                System.out.println(constructor);
            }
            for (Field field : fields) {
                System.out.println(field);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

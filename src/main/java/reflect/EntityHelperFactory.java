package reflect;

import javassist.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * javassist动态生成代码
 * @author: Lee
 * @create: 2024/06/30 14:37
 **/
public class EntityHelperFactory {


    /**
     * 获取实体助手
     * @param clazz
     * @return
     */
    public static AbstractEntityHelper getEntityHelper(Class<?> clazz) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {

        if (null == clazz) {
            return null;
        }

        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();
        // import java.sql.ResultSet;
        pool.importPackage(ResultSet.class.getName());
        // import org.lee.xx.clzz;
        pool.importPackage(clazz.getName());

        // 拿到AbstractEntityHelper的类
        CtClass ctClass = pool.getCtClass(AbstractEntityHelper.class.getName());
        // 要创建的助手类名称
        String name = ctClass.getName() + "_Helper";

        // 动态创建类 这个类继承于AbstractEntityHelper
        CtClass cc = pool.makeClass(name, ctClass);

        // 创建默认构造器
        // 生成如下代码 public XX_Helper(){}
        CtConstructor ctConstructor = new CtConstructor(new CtClass[0], cc);
        ctConstructor.setBody("{}");
        cc.addConstructor(ctConstructor);

        StringBuilder sb = new StringBuilder();
        sb.append("pubic Object create() throws Exception {\n");
        sb.append(clazz.getName())
                .append(" obj = new ").append(clazz.getName()).append("();\n");

        Field[] fileds = clazz.getFields();
        for (Field f : fileds) {
            Column annotation = f.getAnnotation(Column.class);
            if(null == annotation) {
                continue;
            }
            if (f.getType() == Integer.TYPE) {
                sb.append("obj.")
                        .append(f.getName()).append(" = rs.getInt(\"").append(name).append("\");\n");
            }
        }


        sb.append("return null;");
        sb.append("}");

        // 创建方法
        CtMethod cm = CtNewMethod.make(sb.toString(), cc);
        cc.addMethod(cm);

        Class<?> javaClass = cc.toClass();
        Object o = javaClass.newInstance();
        if (o instanceof AbstractEntityHelper) {
            return (AbstractEntityHelper) o;
        }
        return null;
    }

}

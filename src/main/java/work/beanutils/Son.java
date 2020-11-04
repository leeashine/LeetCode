package work.beanutils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

/**
 BeanUtils.copyProperties(Object source, Object target)方法，source对象和target对象相应属性的名称和类型必须都一样才可以成功拷贝属性值
<p>
 BeanUtils.copyProperties只对bean属性进行复制，这里的复制属于浅复制。BeanUtils.copyProperties<b>利用反射，直接将对象的引用set进去，并不是深拷贝<b/>。

 */
public class Son extends Father{
    private Life life;

    @Override
    public Life getLife() {
        return life;
    }

    @Override
    public void setLife(Life life) {
        this.life = life;
    }

    public static void main(String[] args) {
        Father cuishan = new Father();
        cuishan.setFace("handsome");
        cuishan.setHeight("180");
        Life cuishanLife = new Life();
        cuishanLife.setStatus("alive");
        cuishan.setLife(cuishanLife);
        Son wuji=new Son();
        BeanUtils.copyProperties(cuishan,wuji);

        // 无忌用个新对象 不受翠山影响了
//        Life wujiLife = new Life();
        Life wujiLife = wuji.getLife();
        wujiLife.setStatus("alive");
        wuji.setLife(wujiLife);
        cuishanLife.setStatus("dead"); // 翠山后来自刎了

        System.out.println(JSON.toJSONString(cuishan));
        System.out.println(JSON.toJSONString(wuji));
    }
}

package designpattern.factorypattern.factorymethodpattern.practise;

public class JPGPicture implements Picture{
    @Override
    public void load() {
        System.out.println("JPG图片读取。。。。。。");
    }
}

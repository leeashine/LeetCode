package designpattern.factorypattern.factorymethodpattern.practise;

public class Client {
    public static void main(String[] args) {

        PictureFactory factory=new JPGPicFactory();
        factory.LoadPicture();

    }
}

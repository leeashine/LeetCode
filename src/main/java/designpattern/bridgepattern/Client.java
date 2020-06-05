package designpattern.bridgepattern;

public class Client {
    public static void main(String[] args) {
        Image image=new JPGImage();
        ImageImp imageImp=new WindowsImp();
        image.setImageImp(imageImp);
        image.parseFile("天龙八部");
    }
}

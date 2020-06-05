package designpattern.bridgepattern;

public class JPGImage extends Image{
    @Override
    public void parseFile(String fileName) {
        Matrix matrix=new Matrix();
        imageImp.doPaint(matrix);
        System.out.println(fileName + "，格式为JPG。");
    }
}

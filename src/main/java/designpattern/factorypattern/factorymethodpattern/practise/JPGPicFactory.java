package designpattern.factorypattern.factorymethodpattern.practise;

public class JPGPicFactory extends PictureFactory{
    @Override
    public Picture createPic() {
        Picture picture=new JPGPicture();
        return picture;
    }
}

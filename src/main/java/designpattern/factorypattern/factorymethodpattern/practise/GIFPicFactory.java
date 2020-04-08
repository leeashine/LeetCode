package designpattern.factorypattern.factorymethodpattern.practise;

public class GIFPicFactory extends PictureFactory{
    @Override
    public Picture createPic() {
        return new GIFPicture();
    }
}

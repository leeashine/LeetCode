package designpattern.factorypattern.factorymethodpattern.practise;

abstract class PictureFactory {
    protected abstract Picture createPic();
    public void LoadPicture(){
        Picture picture=this.createPic();
        picture.load();
    }
}

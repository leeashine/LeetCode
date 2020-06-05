package designpattern.bridgepattern.practise;

public abstract class File {
    protected DataBase dataBase;
    public void setDataBase(DataBase dataBase){
        this.dataBase=dataBase;
    }
    public abstract void exportFile(String path);
}

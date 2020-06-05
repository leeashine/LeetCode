package designpattern.bridgepattern.practise;

public class Client {
    public static void main(String[] args) {
        File file=new PdfFile();
        DataBase dataBase=new MySqlDataBaseImp();
        file.setDataBase(dataBase);
        file.exportFile("d:/");
    }
}

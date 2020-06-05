package designpattern.bridgepattern.practise;

public class PdfFile extends File{
    @Override
    public void exportFile(String path) {
        dataBase.connect();
        System.out.println("从数据库中生成pdf文件");
    }
}

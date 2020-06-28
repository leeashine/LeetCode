package designpattern.facadepattern.sample;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//文件读取类，充当子系统类。
public class FileReader {
    public String Read(String fileNameSrc)
    {
        System.out.println("读取文件，获取明文：");
        FileInputStream fs = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            fs = new FileInputStream(fileNameSrc);
            int data;
            while((data = fs.read())!= -1)
            {
                sb = sb.append((char)data);
            }
            fs.close();
            System.out.println(sb.toString());
        }
        catch(FileNotFoundException e)
        {
            System.out.println("文件不存在！");
        }
        catch(IOException e)
        {
            System.out.println("文件操作错误！");
        }
        return sb.toString();
    }

}

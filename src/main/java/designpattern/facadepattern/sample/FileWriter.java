package designpattern.facadepattern.sample;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

//文件保存类，充当子系统类。
public class FileWriter {
    public void Write(String encryptStr,String fileNameDes)
    {
        System.out.println("保存密文，写入文件。");
        FileOutputStream fs = null;
        try
        {
            fs = new FileOutputStream(fileNameDes);
            byte[] str =encryptStr.getBytes(StandardCharsets.UTF_8);
            fs.write(str,0,str.length);
            fs.flush();
            fs.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("文件不存在！");
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.out.println("文件操作错误！");
        }
    }
}

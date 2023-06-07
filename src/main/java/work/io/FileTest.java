package work.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTest {
    public static void main(String[] args) throws IOException {

        mergeVideoIntoOne();


    }

    /**
     * 多个二进制文件合并成一个文件
     * 如多个ts文件合并成一个ts视频文件
     *
     * @throws IOException
     */
    private static void mergeVideoIntoOne() throws IOException {
        long start = System.currentTimeMillis();
        File dir = new File("E:\\IDM下载\\新建文件夹");
        File[] files = dir.listFiles();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = FileUtils.openOutputStream(new File("C:\\Users\\Lee\\Desktop\\abc\\眼睛妹.ts"));
            for (File file : files) {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                fileOutputStream.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
        long end = System.currentTimeMillis();
        System.out.println("cost " + (end - start) + "ms");
    }
}

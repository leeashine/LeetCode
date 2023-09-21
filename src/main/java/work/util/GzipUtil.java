package work.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩算法在处理文本文件或二进制文件时通常具有较好的性能。
 * 压缩和解压缩的速度相对较快，适用于实时或需要快速处理的场景。
 * 压缩速度和压缩比之间存在一定的权衡，更高的压缩比可能会导致压缩和解压缩的速度略有降低。
 */
public class GzipUtil {

    public static byte[] compressString(String text) throws IOException {
        byte[] compressedData = null;
        ByteArrayOutputStream baos = null;
        GZIPOutputStream gzip = null;
        try {
            baos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(baos);
            gzip.write(text.getBytes("UTF-8"));
            compressedData = baos.toByteArray();
        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(baos);
        }


        return compressedData;
    }

    public static String decompressString(byte[] compressedData) {
        ByteArrayInputStream bais = null;
        GZIPInputStream gzip = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        ;
        try {
            bais = new ByteArrayInputStream(compressedData);
            gzip = new GZIPInputStream(bais);
            reader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(bais);
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        try {
            String originalText = "This is a sample string to be compressed using GZIP.";
            System.out.println("originalText: " + originalText.getBytes().length + " bytes");

            // 压缩字符串
            byte[] compressedData = compressString(originalText);
            System.out.println("Compressed data: " + compressedData.length + " bytes");

            // 解压缩字符串
            String decompressedText = decompressString(compressedData);
            System.out.println("Decompressed text: " + decompressedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

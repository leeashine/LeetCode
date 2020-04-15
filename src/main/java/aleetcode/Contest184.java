package aleetcode;

public class Contest184 {
    public static void main(String[] args) {

        String x=  "&amp; is an HTML entity but &ambassador; is not.&quot;".replaceAll("&quot;","\\\"");
        System.out.println(x);
    }
    public String entityParser(String text) {

        return  text.replaceAll("&quot;","\\\"").replaceAll("&apos;","'").replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&frasl;","/").replaceAll("&amp;","&");

    }
}

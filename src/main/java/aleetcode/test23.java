package aleetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test23 {

	
	public static void main(String[] args) {
		String sz = "打靶位置1:打L靶和Z靶 总孔数:5 钻径:3.175 打靶位置2:打ABCD靶 总孔数:4 钻径:3.175 A0靶距标准值: X=102mm Y=486mm Z=383mm ABCD靶标准值: AB=374mm CD=374mm BD=477mm AC=474mm";
		
		String s="2019/11/08";
		System.out.println(s.substring(8,10));
		
		 // 按指定模式在字符串查找
	      String line = "This order was placed for QT3000! OK?";
	      String pattern = ".*?(\\D*)(\\d+)(.*)";
	 
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	 
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(line);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         System.out.println("Found value: " + m.group(1) );
	         System.out.println("Found value: " + m.group(2) );
	         System.out.println("Found value: " + m.group(3) ); 
	      } else {
	         System.out.println("NO MATCH");
	      }
		
	}
	
	
}

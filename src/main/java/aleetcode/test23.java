package aleetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test23 {

	
	public static void main(String[] args) {
		String sz = "���λ��1:��L�к�Z�� �ܿ���:5 �꾶:3.175 ���λ��2:��ABCD�� �ܿ���:4 �꾶:3.175 A0�о��׼ֵ: X=102mm Y=486mm Z=383mm ABCD�б�׼ֵ: AB=374mm CD=374mm BD=477mm AC=474mm";
		
		String s="2019/11/08";
		System.out.println(s.substring(8,10));
		
		 // ��ָ��ģʽ���ַ�������
	      String line = "This order was placed for QT3000! OK?";
	      String pattern = ".*?(\\D*)(\\d+)(.*)";
	 
	      // ���� Pattern ����
	      Pattern r = Pattern.compile(pattern);
	 
	      // ���ڴ��� matcher ����
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
package aleetcode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IDGenerator {
	private static int STATIC_SEQ = (int)(9999*Math.random());
	private final static  IdWorker idWorker = new IdWorker(0);
	
	/**
	 * 获得32位随机流水号，采用TWRITER算法获取
	 * */
	public final static String getID32()
	{
		return idWorker.nextId();
	}

	
	/**
	 * 获得25位随机流水号，设计规则
	 * <br>13位时间（精确到毫秒）+2位随机数+3位随机数+2位随机数+4位内存顺序号=25位流水号
	 * */
	public final static String getID25()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateTime = sdf.format(new Date());
		
		return dateTime+random(2)+random(4)+random(2)+getStaticSeq();
	}
	
	/**
	 * 获得25位随机流水号，设计规则
	 * <br>13位时间（精确到毫秒）+3位随机数+2位随机数+3位随机数=25位流水号
	 * */
	public final static String getID21()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateTime = sdf.format(new Date());
		
		return dateTime+random(3)+random(2)+random(3);
	}

	
	/**
	 * 获得响应位数的随机数
	 * */
	private static String random(int length)
	{
		double maxvalue = Math.pow(10.00, length*1.00);
		int iValue = (int)(maxvalue*Math.random());
		String sValue = lpad(String.valueOf(iValue),length,"0");
		return sValue;
		
	}
	
	private synchronized static String getStaticSeq()
	{
		STATIC_SEQ++;
		if(STATIC_SEQ>9999)
		{
			STATIC_SEQ = 0;
		}
		
		return lpad(String.valueOf(STATIC_SEQ),4,"0");
	}
	/**
	 * 左填充字符
	 * */
	public static final String lpad(String value,int len,String reg)
	{
		if(value==null) value="";
		
		char[] valueArray = value.toCharArray();
		if(valueArray.length>len)
			 return value;

		 char[] returnArray = new char[len]; 
		 char[] regArray = reg.toCharArray();
		 int curLength = 0;
		 while(curLength<len-valueArray.length)
		 {
			 System.arraycopy(regArray,0,returnArray, curLength, regArray.length);
			 curLength += regArray.length;
		 }
		 
		 System.arraycopy(valueArray, 0, returnArray, len-valueArray.length, valueArray.length);
		 return new String(returnArray);
	}
	
	/**
	 * 获得UUID流水号
	 * */
	public static final String UUID(boolean upperflag)
	{
		String UUID = java.util.UUID.randomUUID().toString();
		if(upperflag) UUID = UUID.toUpperCase();
		return UUID;
	}

}

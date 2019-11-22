package aleetcode;

import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class JsonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String jsonStr="{\"aa\":\"123456789.1234\",\"bb\":\"123456789.1234\",\"cc\":\"1.0E+7\"}";
		JSONObject json = (JSONObject) JSONObject.parse(jsonStr);
		//空指针
//		JSONObject json = (JSONObject) JSONObject.parse("");  
		System.out.println(json.get("aa"));
		String dd=String.valueOf(json.get("dd"));
		System.out.println(dd);
//		String--->JSON
		String jsonStr2="{\"body\":{\"aa\":\"1000000000\",\"cd\":\"10000000000.00\"}}";
		JSONObject json2 = (JSONObject) JSONObject.parse(jsonStr2);
		JSONObject json3 = (JSONObject) JSONObject.parse(json2.getString("body"));
		System.out.println( json3.getBigDecimal("cd").setScale(2) );
//		JSON--->String
		JSONObject bankInfo = new JSONObject();
		JSONObject sdk_version = new JSONObject();
		sdk_version.put("sdk_version", "2.0");
		bankInfo.put("bank_encrypted_meta", sdk_version);
		System.out.println(bankInfo.toJSONString());
		
//		map---->jsonString
		//JSONObject.toJSONString(paramMap)
		
		String jsonStr3="{\"list\":{\"map\":{\"name\":\"2\",\"age\":\"12\"},\"mali\":{\"name\":\"3\",\"age\":\"18\"}}}";

		JSONObject list=(JSONObject)JSONObject.parse(jsonStr3);
		
		Map<String, Object> map =(JSONObject)list.get("list");  
        for (Entry<String, Object> entry : map.entrySet()) {
                        //getKey获取Entry集合中的key、getValue获取value
//                        System.out.println(entry.getKey()+"="+entry.getValue()); 
                        Map<String, Object> map2=(JSONObject)entry.getValue();
                        for (Entry<String, Object> entry2 : map2.entrySet()) {
                        	System.out.println(entry2.getKey()+"="+entry2.getValue()); 
                        }
            }

        
        String jsonStr4="{\"SpouseCustomerName\": \"配偶\", 	\"loanAmount\": \"10000.0\", 	\"customerAddressCode\": \"999999\", 	\"customerSex\": \"2\", 	\"customerNumber\": \"WD2019073100000001\", 	\"customerPhone\": \"18271414002\", 	\"organization\": \"中国科技园q\", 	\"eventId\": \"netloan_taxSecondsLoan\", 	\"customerProperty\": \"\", 	\"EAccountID\": \"1122334455\", 	\"applicationNumber\": \"WD2019073100000001\", 	\"transTime\": \"2019-07-31 10:28:36\", 	\"partnerCode\": \"kratos\", 	\"ChannelNumber\": \"AH\", 	\"UnifiedSocialCredit\": \"666\", 	\"registered_address\": \"上海市教育路11号\", 	\"SpouseCertID\": \"320922197905138447\", 	\"loanTerm\": \"12\", 	\"ECustomerID\": \"1122334455\", 	\"pboPostAddress\": \"上海市教育路11号\", 	\"SpouseCertType\": \"Ind01\", 	\"customerName\": \"庾美\", 	\"customerType\": \"03\", 	\"SpousePhoneNo\": \"13131231234\", 	\"id_number\": \"522701199111237641\", 	\"productCode\": \"\", 	\"customerBirthday\": \"1991-11-23\", 	\"marriage\": \"10\", 	\"account_name\": \"庾美\", 	\"productId\": \"3076\", 	\"appName\": \"netloan\", 	\"secretKey\": \"2597b083866c4b419ea12dede2ed03eb\", 	\"transId\": \"WD2019073100000001\", 	\"customerAddress\": \"上海市教育路11号\", 	\"pouseCode\": \"522701199111237641\", 	\"pouseCerttype\": \"Ind01\" }";
        JSONObject json4 = (JSONObject) JSONObject.parse(jsonStr4);
		System.out.println(json4.getString("SpouseCustomerName"));
		
		
//		String listStr="<?xmlversion=\"1.0\"encoding=\"UTF-8\"?><list><map><name>2</name><age>12</age></map><mali><name>3</name><age>18</age></mali></list>";
//		Object xmlJSONObj = XML.toJSONObject(listStr); 
//		com.amarsoft.are.util.json.JSONObject list=JSONDecoder.decode(xmlJSONObj.toString());
//		Map<String,Object> map = ServiceTransUtils.toHashMap(list);
//		
//        for (Entry<String, Object> entry : map.entrySet()) {
//                        //getKey获取Entry集合中的key、getValue获取value
////                        System.out.println(entry.getKey()+"="+entry.getValue()); 
//                        Map<String, Object> map2=(Map)entry.getValue();
//                        for (Entry<String, Object> entry2 : map2.entrySet()) {
//                        	System.out.println(entry2.getKey()+"="+entry2.getValue());
//                        	
//                        }
//                        
//            }
		
		
		//异常情况  根本不是json字符串！
//		String jsonStrs="杭州市";
//		JSONObject jsons =  JSONObject.parseObject(jsonStrs);
//		String prov=jsons.getString("prov");
//		System.out.println(prov);
//		String area="";
//		if(!(prov==null||prov.equals(""))&&!"{}".equals(prov)){
//			JSONObject addessjson = JSON.parseObject(prov);
//			area = addessjson.getString("area");
//		}
//
//		System.out.println(area);

	}

}

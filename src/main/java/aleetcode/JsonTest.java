package aleetcode;

import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import designpattern.commandpattern.sample2.XMLUtil;
import util.XmlTool;


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

//		map->json
//		JSONObject是继承的map！所以不用转换 直接拿来用就行

//		jsonString---->map
//		String jsonStr3="{\"list\":{\"map\":{\"name\":\"2\",\"age\":\"12\"},\"mali\":{\"name\":\"3\",\"age\":\"18\"}}}";
		String jsonStr3="{\n" +
				"\t\"creditScore\": \"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201703273000000922900001916809\\\",\\\"zm_score\\\":\\\"750\\\"}\",\n" +
				"\t\"watchListii\": \"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201705033000000032300002194518\\\",\\\"details\\\":[{\\\"biz_code\\\":\\\"AA\\\",\\\"code\\\":\\\"AA001001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"逾期金额（元）\\\",\\\"key\\\":\\\"event_max_amt_code\\\",\\\"value\\\":\\\"M01\\\"},{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"test201601025zf000003266bb5b12ac8d42602e862d8fd\\\"},{\\\"description\\\":\\\"最近一次违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-10\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-10-25 00:00:00\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AA001\\\"},{\\\"biz_code\\\":\\\"AC\\\",\\\"code\\\":\\\"AC002001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"0f33237afc66907a01c6d64ff79a3a30\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-06-16 00:00:00\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AC002\\\"},{\\\"biz_code\\\":\\\"AC\\\",\\\"code\\\":\\\"AD001005\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"e4773507-1985-425b-b3ec-cca9102787ee\\\"},{\\\"description\\\":\\\"违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 14:19:17\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AG001\\\"},{\\\"biz_code\\\":\\\"AB\\\",\\\"code\\\":\\\"AB001001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"d378c378-8164-4d8a-8b74-294673d51d7e\\\"},{\\\"description\\\":\\\"案件发布时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 15:22:08\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AB001\\\"},{\\\"biz_code\\\":\\\"AA\\\",\\\"code\\\":\\\"AE001008\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"92e139d6-378f-4fb4-bcd5-b021e42bc14f\\\"},{\\\"description\\\":\\\"最近一次违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 15:22:09\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AA001\\\"},{\\\"biz_code\\\":\\\"AH\\\",\\\"code\\\":\\\"AI003007\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"fe42b908-6f05-4ffe-a9c1-ba9766656bff\\\"},{\\\"description\\\":\\\"欠费停机时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 16:13:52\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AH001\\\"}],\\\"is_matched\\\":true}\",\n" +
				"\t\"das\": \"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201708233000000158000638578460\\\",\\\"vars\\\":[{\\\"key\\\":\\\"have_car_flag\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"have_fang_flag\\\",\\\"value\\\":\\\"02\\\"},{\\\"key\\\":\\\"auth_fin_last_6m_cnt\\\",\\\"value\\\":\\\"03\\\"},{\\\"key\\\":\\\"auth_fin_last_3m_cnt\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"auth_fin_last_1m_cnt\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"positive_biz_cnt_1y\\\",\\\"value\\\":\\\"05\\\"},{\\\"key\\\":\\\"last_6m_avg_asset_total\\\",\\\"value\\\":\\\"05\\\"},{\\\"key\\\":\\\"tot_pay_amt_6m\\\",\\\"value\\\":\\\"04\\\"},{\\\"key\\\":\\\"xfdc_index\\\",\\\"value\\\":\\\"04\\\"},{\\\"key\\\":\\\"ovd_order_cnt_6m\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"ovd_order_amt_6m\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"occupation\\\",\\\"value\\\":\\\"白领(模型预测)\\\"},{\\\"key\\\":\\\"company_name\\\",\\\"value\\\":\\\"无法识别\\\"},{\\\"key\\\":\\\"mobile_fixed_days\\\",\\\"value\\\":\\\"06\\\"},{\\\"key\\\":\\\"adr_stability_days\\\",\\\"value\\\":\\\"05\\\"}]}\"\n" +
				"}";

		JSONObject list=(JSONObject)JSONObject.parse(jsonStr3);
		
		Map<String, Object> map =(JSONObject)list.getJSONObject("creditScore");
		for (Entry<String, Object> entry : map.entrySet()) {
			//getKey获取Entry集合中的key、getValue获取value
//                        System.out.println(entry.getKey()+"="+entry.getValue()); 

			System.out.println(entry.getKey() + "=" + entry.getValue());

		}

        
        String jsonStr4="{\"SpouseCustomerName\": \"配偶\", 	\"loanAmount\": \"10000.0\", 	\"customerAddressCode\": \"999999\", 	\"customerSex\": \"2\", 	\"customerNumber\": \"WD2019073100000001\", 	\"customerPhone\": \"18271414002\", 	\"organization\": \"中国科技园q\", 	\"eventId\": \"netloan_taxSecondsLoan\", 	\"customerProperty\": \"\", 	\"EAccountID\": \"1122334455\", 	\"applicationNumber\": \"WD2019073100000001\", 	\"transTime\": \"2019-07-31 10:28:36\", 	\"partnerCode\": \"kratos\", 	\"ChannelNumber\": \"AH\", 	\"UnifiedSocialCredit\": \"666\", 	\"registered_address\": \"上海市教育路11号\", 	\"SpouseCertID\": \"320922197905138447\", 	\"loanTerm\": \"12\", 	\"ECustomerID\": \"1122334455\", 	\"pboPostAddress\": \"上海市教育路11号\", 	\"SpouseCertType\": \"Ind01\", 	\"customerName\": \"庾美\", 	\"customerType\": \"03\", 	\"SpousePhoneNo\": \"13131231234\", 	\"id_number\": \"522701199111237641\", 	\"productCode\": \"\", 	\"customerBirthday\": \"1991-11-23\", 	\"marriage\": \"10\", 	\"account_name\": \"庾美\", 	\"productId\": \"3076\", 	\"appName\": \"netloan\", 	\"secretKey\": \"2597b083866c4b419ea12dede2ed03eb\", 	\"transId\": \"WD2019073100000001\", 	\"customerAddress\": \"上海市教育路11号\", 	\"pouseCode\": \"522701199111237641\", 	\"pouseCerttype\": \"Ind01\" }";
        JSONObject json4 = (JSONObject) JSONObject.parse(jsonStr4);
		System.out.println(json4.getString("SpouseCustomerName"));
		
//		xml------->json
		String listStr="<document><request><head><version>1.0.0</version><appId>ALIPAY</appId><function>ant.jiebei.institution.cooperation.promote.apply</function><reqTime>20191218143003</reqTime><reqTimeZone>UTC+8</reqTimeZone><reqMsgId>20191218143001622000000000255680</reqMsgId><reserve></reserve><signType>RSA</signType><inputCharset>UTF-8</inputCharset></head><body><applyNo>20191218085263015010A</applyNo><name>张三</name><certType>01</certType><certNo>324342197609084532</certNo><mobileNo>13323456789</mobileNo><promoteType>INCREASE_APPLY</promoteType><promoteReason>优质用户提额</promoteReason><zmInfo><![CDATA[{\"creditScore\":\"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201703273000000922900001916809\\\",\\\"zm_score\\\":\\\"750\\\"}\",\"watchListii\":\"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201705033000000032300002194518\\\",\\\"details\\\":[{\\\"biz_code\\\":\\\"AA\\\",\\\"code\\\":\\\"AA001001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"逾期金额（元）\\\",\\\"key\\\":\\\"event_max_amt_code\\\",\\\"value\\\":\\\"M01\\\"},{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"test201601025zf000003266bb5b12ac8d42602e862d8fd\\\"},{\\\"description\\\":\\\"最近一次违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-10\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-10-25 00:00:00\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AA001\\\"},{\\\"biz_code\\\":\\\"AC\\\",\\\"code\\\":\\\"AC002001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"0f33237afc66907a01c6d64ff79a3a30\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-06-16 00:00:00\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AC002\\\"},{\\\"biz_code\\\":\\\"AC\\\",\\\"code\\\":\\\"AD001005\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"e4773507-1985-425b-b3ec-cca9102787ee\\\"},{\\\"description\\\":\\\"违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 14:19:17\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AG001\\\"},{\\\"biz_code\\\":\\\"AB\\\",\\\"code\\\":\\\"AB001001\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"d378c378-8164-4d8a-8b74-294673d51d7e\\\"},{\\\"description\\\":\\\"案件发布时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 15:22:08\\\",\\\"settlement\\\":false,\\\"type\\\":\\\"AB001\\\"},{\\\"biz_code\\\":\\\"AA\\\",\\\"code\\\":\\\"AE001008\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"92e139d6-378f-4fb4-bcd5-b021e42bc14f\\\"},{\\\"description\\\":\\\"最近一次违约时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 15:22:09\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AA001\\\"},{\\\"biz_code\\\":\\\"AH\\\",\\\"code\\\":\\\"AI003007\\\",\\\"extend_info\\\":[{\\\"description\\\":\\\"编号\\\",\\\"key\\\":\\\"id\\\",\\\"value\\\":\\\"fe42b908-6f05-4ffe-a9c1-ba9766656bff\\\"},{\\\"description\\\":\\\"欠费停机时间\\\",\\\"key\\\":\\\"event_end_time_desc\\\",\\\"value\\\":\\\"2016-12\\\"}],\\\"level\\\":0,\\\"refresh_time\\\":\\\"2016-12-14 16:13:52\\\",\\\"settlement\\\":true,\\\"type\\\":\\\"AH001\\\"}],\\\"is_matched\\\":true}\",\"das\":\"{\\\"success\\\":true,\\\"biz_no\\\":\\\"ZM201708233000000158000638578460\\\",\\\"vars\\\":[{\\\"key\\\":\\\"have_car_flag\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"have_fang_flag\\\",\\\"value\\\":\\\"02\\\"},{\\\"key\\\":\\\"auth_fin_last_6m_cnt\\\",\\\"value\\\":\\\"03\\\"},{\\\"key\\\":\\\"auth_fin_last_3m_cnt\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"auth_fin_last_1m_cnt\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"positive_biz_cnt_1y\\\",\\\"value\\\":\\\"05\\\"},{\\\"key\\\":\\\"last_6m_avg_asset_total\\\",\\\"value\\\":\\\"05\\\"},{\\\"key\\\":\\\"tot_pay_amt_6m\\\",\\\"value\\\":\\\"04\\\"},{\\\"key\\\":\\\"xfdc_index\\\",\\\"value\\\":\\\"04\\\"},{\\\"key\\\":\\\"ovd_order_cnt_6m\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"ovd_order_amt_6m\\\",\\\"value\\\":\\\"01\\\"},{\\\"key\\\":\\\"occupation\\\",\\\"value\\\":\\\"白领(模型预测)\\\"},{\\\"key\\\":\\\"company_name\\\",\\\"value\\\":\\\"无法识别\\\"},{\\\"key\\\":\\\"mobile_fixed_days\\\",\\\"value\\\":\\\"06\\\"},{\\\"key\\\":\\\"adr_stability_days\\\",\\\"value\\\":\\\"05\\\"}]}\"}]]></zmInfo><apolloInfo><![CDATA[{\"have_car_prob_grade\":\"02\",\"have_fang_prob_grade\":\"01\",\"xfdc_index\":\"07\",\"mobile_fixed_grade\":\"08\",\"adr_stability_grade\":\"09\",\"occupation\":\"办事人员和有关人员\",\"tot_pay_amt_6m_grade\":\"04\",\"last_6m_avg_asset_total_grade\":\"06\",\"ovd_order_cnt_6m_grade\":\"03\",\"ovd_order_amt_6m_grade\":\"04\",\"positive_biz_cnt_1y_grade\":\"10\",\"risk_score\":\"03\",\"cust_seg\":\"01\",\"dev_stability_grade\":\"02\",\"ovd_order_days_6m_grade\":\"04\",\"first_loan_length_grade\":\"05\",\"repay_amt_6m_grade\":\"07\"}]]></apolloInfo><extInfo><![CDATA[]]></extInfo></body></request><signature>GueZQ1ibAbTkuvPRNPmhqwBGVYSso3AMG4+GD28wtR7zHykApb2gVIocAHWa8415ihytN9vGfcJAOPh/Gk6lYrtLYW/jxZIuqxpliv++WosFZ617CjX/LA/Y3KVAxjQK5sCPTeaEs9ifZI0dG89v29v1vhQlrA5hF6ZCl5eE3fUMvWXOcxM2HdDAzfYbRFw9zhsfooc3xk5o3NhJCpHpoOMIADRp1Lt9tMHOMHrI1RDjn8e/5gOc1+xzkmuJ5jDmhSb0RKwmzCbM5a2iRSQEudc6TJV23rBl2JvMdgpLlFNyEm1m4fWGOwJEJ5QQ5yaJMzOuuzFmrP97B6MsRYl59Q==</signature></document>";
		JSONObject xmltojsonObj=XmlTool.documentToJSONObject(listStr);
		System.out.println(xmltojsonObj.toJSONString());
		JSONObject bodyobj =xmltojsonObj.getJSONArray("document").getJSONObject(0).getJSONArray("request").getJSONObject(0).getJSONArray("body").getJSONObject(0);
		JSONObject zmInfo=bodyobj.getJSONObject("zmInfo");
		JSONObject creditScore=zmInfo.getJSONObject("creditScore");
		String zm_score=creditScore.getString("zm_score");

		System.out.println(zm_score);

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

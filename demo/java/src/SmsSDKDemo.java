import com.qcloud.sms.SmsVoice;
import com.qcloud.sms.SmsVoiceResult;

public class SmsSDKDemo {
    public static void main(String[] args) {
    	try {
    		// 请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
    		int appid = 1400025207;
    		String appkey = "163ad12e87775731cfdb5f47e424bd3f";
    		
    		String phoneNumber1 = "12345678901";
    		String phoneNumber2 = "15356059310";
    		String phoneNumber3 = "12345678903";
    		int tmplId = 10852;
			int tmplIdVoice= 10851;

    		// 初始化单发
	    	/*SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
	    	SmsSingleSenderResult singleSenderResult;*/
	
	    	// 普通单发
	    	/*singleSenderResult = singleSender.send(0, "86", phoneNumber2, "4578为您的登录验证码，请于2分钟内填写。如非本人操作，请忽略本短信。", "", "");
	    	System.out.println(singleSenderResult);*/


			// 初始化语音
			SmsVoice smsVoice = new SmsVoice(appid, appkey);
			SmsVoiceResult smsVoiceResult;

			// 普通语音单发
			smsVoiceResult = smsVoice.send(2, "86", phoneNumber2, "尊敬的客户测试您好！您在淘宝上的订单已生成，请在3分钟内完成支付，否则该订单将自动取消，感谢合作和支持", "");
			System.out.println(smsVoiceResult);
	
	    	// 指定模板单发
	    	// 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
	    	/*ArrayList<String> params = new ArrayList<>();
	    	params.add("5489");
	    	params.add("1");
	    	singleSenderResult = singleSender.sendWithParam("86", phoneNumber2, tmplId, params, "", "", "");
	    	System.out.println(singleSenderResult);*/

            //语音通知类 带参
			/*ArrayList<String> params = new ArrayList<>();
			params.add("测试");
			params.add("淘宝");
			params.add("1");
			smsVoiceResult = smsVoice.sendWithParam("86", phoneNumber2, tmplIdVoice, params, "测试001", "");
			System.out.println(smsVoiceResult);*/
	    	
	    	// 初始化群发
	    	/*SmsMultiSender multiSender = new SmsMultiSender(appid, appkey);
	    	SmsMultiSenderResult multiSenderResult;*/
	
	    	// 普通群发
	    	// 下面是 3 个假设的号码
	    	/*ArrayList<String> phoneNumbers = new ArrayList<>();
	    	phoneNumbers.add(phoneNumber1);
	    	phoneNumbers.add(phoneNumber2);
	    	phoneNumbers.add(phoneNumber3);
	    	multiSenderResult = multiSender.send(0, "86", phoneNumbers, "测试短信，普通群发，深圳，小明，上学。", "", "");
	    	System.out.println(multiSenderResult);*/

	    	// 指定模板群发
	    	// 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
	    	/*params = new ArrayList<>();
	    	params.add("指定模板群发");
	    	params.add("深圳");
	    	params.add("小明");
	    	multiSenderResult = multiSender.sendWithParam("86", phoneNumbers, tmplId, params, "", "", "");
	    	System.out.println(multiSenderResult);*/
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

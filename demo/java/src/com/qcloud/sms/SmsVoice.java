package com.qcloud.sms;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

// org.json 第三方库请自行下载编译，或者在以下链接下载使用 jdk 1.7 的版本
// http://share.weiyun.com/630a8c65e9fd497f3687b3546d0b839e

public class SmsVoice {
	int appid;
	String appkey;
    String url = "https://yun.tim.qq.com/v5/tlsvoicesvr/sendvoiceprompt";

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsVoice(int appid, String appkey) throws Exception {
		this.appid = appid;
		this.appkey = appkey;
	}

	/**
	 * 普通单发短信接口，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
	 * @param prompttype 短信类型，0 为普通短信，1 营销短信
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param promptfile 信息内容，必须与申请的模板格式一致，否则将返回错误
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	public SmsVoiceResult send(
			int prompttype,
			String nationCode,
			String phoneNumber,
			String promptfile,
			String ext) throws Exception {
/*
请求包体
{
    "tel": {
        "nationcode": "86", //国家码
        "mobile": "13788888888" //手机号码
    },
    "prompttype": 2, //语音类型，目前固定为2
    "promptfile": "语音内容文本", //通知内容，utf8编码，支持中文、英文字母、数字及组合
    "playtimes": 2, //播放次数，可选，最多3次，默认2次
    "sig": "30db206bfd3fea7ef0db929998642c8ea54cc7042a779c5a0d9897358f6e9505", //app凭证，具体计算方式见下注
    "time": 1457336869, //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会返回失败
    "ext": "" //用户的session内容，腾讯server回包中会原样返回，可选字段，不需要就填空。
}

应答包体
{
    "result": 0,
    "errmsg": "OK",
    "ext": "",
    "sid": "xxxxxxx",
    "fee": 1
}


{
    "result": 0, //0表示成功，非0表示失败
    "errmsg": "OK", //result非0时的具体错误信息
    "ext": "", //用户的session内容，腾讯server回包中会原样返回
    "callid": "xxxx" //标识本次发送id，标识一次下发记录
}

*/
		// 校验 type 类型
//		if (0 != prompttype && 1 != prompttype) {
//			throw new Exception("prompttype " + prompttype + " error");
//		}

		if (null == ext) {
			ext = "";
		}

		// 按照协议组织 post 请求包体
        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;

		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("prompttype", prompttype);
        data.put("promptfile", promptfile);
        data.put("sig", util.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumber)));
        data.put("tel", tel);
        data.put("time", curTime);
        data.put("ext", ext);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();
        
        System.out.println(data.toString());

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
		SmsVoiceResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsVoiceResult(json);
        } else {
        	result = new SmsVoiceResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        
        return result;
	}

	/**
	 * 指定模板单发
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param templId 信息内容
	 * @param params 模板参数列表，如模板 {1}...{2}...{3}，那么需要带三个参数
	 * @param sign 签名，如果填空，系统会使用默认签名
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	public SmsVoiceResult sendWithParam(
			String nationCode,
			String phoneNumber,
			int templId,
			ArrayList<String> params,
			String sign,
			String ext) throws Exception {
/*
请求包体
{
    "tel": {
        "nationcode": "86", 
        "mobile": "13788888888"
    }, 
    "sign": "腾讯云", 
    "tpl_id": 19, 
    "params": [
        "验证码", 
        "1234", 
        "4"
    ], 
    "sig": "fdba654e05bc0d15796713a1a1a2318c",
    "time": 1479888540,
    "extend": "", 
    "ext": ""
}
应答包体
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
*/
		if (null == nationCode || 0 == nationCode.length()) {
			nationCode = "86";
		}
		if (null == params) {
			params = new ArrayList<>();
		}
		if (null == sign) {
			sign = "";
		}
		if (null == ext) {
			ext = "";
		}
		
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;

		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("tel", tel);
        data.put("sig", util.calculateSigForTempl(appkey, random, curTime, phoneNumber));
        data.put("tpl_id", templId);
        data.put("params", util.smsParamsToJSONArray(params));
        data.put("sign", sign);
        data.put("time", curTime);
        data.put("ext", ext);

		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
		SmsVoiceResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsVoiceResult(json);
        } else {
        	result = new SmsVoiceResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        
        return result;
	}
}

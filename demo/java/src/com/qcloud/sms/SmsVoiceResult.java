package com.qcloud.sms;

public class SmsVoiceResult {
/*
{
    "result": 0, //0表示成功，非0表示失败
    "errmsg": "OK", //result非0时的具体错误信息
    "ext": "", //用户的session内容，腾讯server回包中会原样返回
    "callid": "xxxx" //标识本次发送id，标识一次下发记录
}
*/
	public int result;
	public String errMsg = "";
	public String ext = "";
	public String callid = "";
	
	public String toString() {
		return String.format(
				"SmsVoiceResult\nresult %d\nerrMsg %s\next %s\ncallid %s",
				result, errMsg, ext, callid);
	}
}

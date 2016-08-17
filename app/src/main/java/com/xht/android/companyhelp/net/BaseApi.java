package com.xht.android.companyhelp.net;

public class BaseApi {
	
	public static final String VERCODE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/seadVerifyController/seadVerify";
	public static final String REGIST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/registController/regist";
	public static final String LOGIN_URL = "http://www.xiaohoutai.com.cn:8888/XHT/loginController/login";
	public static final String ARTICLE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/getArticle";

	public static final String ZhuCeComp_URL = "http://www.xiaohoutai.com.cn:8888/XHT/priceinfoController/loadCompanyRegistPrice";
	public static final String JiZhangBaoShui_URL = "http://www.xiaohoutai.com.cn:8888/XHT/priceinfoController/loadAccountPrice";
	public static final String[] JiaGeofYeWu = {
			ZhuCeComp_URL, JiZhangBaoShui_URL
	};

	public static final String SaveRegistOrderInfo_URL = "http://www.xiaohoutai.com.cn:8888/XHT/" +
			"companyregistorderController/saveRegistOrderInfo";
	public static final String JiZhangBS_SROrderI_URL = "http://www.xiaohoutai.com.cn:8888/XHT/" +
			"accountorderController/saveAccountOrderInfo";

	public static final String WEIXI_LJZF_URL = "http://www.xiaohoutai.com.cn:8888/XHT/appwxzf/appwxyuzhifu";

}

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
	public static final String SHEBAO_GET_JIAGE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/sclsecurityorderController/loadPriceAndCompanyNameOfSocialSecurity";
	public static final String SHEBAO_BOOKLIST_POST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/sclsecurityorderController/saveSclsecurityorder";
	public static final String FAPIAO_GET_JIAGE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/invoiceorderController/loadCompanyNameAndPrice";
	public static final String FAPIAO_POST_LEIXING1 = "http://www.xiaohoutai.com.cn:8888/XHT/invoiceorderController/saveTaxInvoiceOrderInfo";
	public static final String FAPIAO_POST_LEIXING2 = "http://www.xiaohoutai.com.cn:8888/XHT/invoiceorderController/saveBusinessInvoiceOrderInfo";
	public static final String FAPIAO_POST_LEIXING3 = "http://www.xiaohoutai.com.cn:8888/XHT/invoiceorderController/saveVerifyInvoiceOrderInfo";

	public static final String WEIXI_LJZF_URL = "http://www.xiaohoutai.com.cn:8888/XHT/appwxzf/appwxyuzhifu";

}

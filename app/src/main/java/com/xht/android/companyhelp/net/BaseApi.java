package com.xht.android.companyhelp.net;

public class BaseApi {
	
	public static final String VERCODE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/seadVerifyController/seadVerify";
	public static final String REGIST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/registController/regist";
	public static final String LOGIN_URL = "http://www.xiaohoutai.com.cn:8888/XHT/loginController/login";
	public static final String ARTICLE_URL = "http://www.xiaohoutai.com.cn:8888/XHT/articleinfoController/loadAppArticleListInfo";

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
	public static final String RegiTrademask_Post_Url = "http://www.xiaohoutai.com.cn:8888/XHT/regtrademarkController/saveRegtrademarkOrderInfo";
	public static final String RegiTrademask_Get_Url = "http://www.xiaohoutai.com.cn:8888/XHT/regtrademarkController/loadCompanyNameAndPrice";
	public static final String FWKB_D_Get_Url = "http://www.xiaohoutai.com.cn:8888/XHT/serverBoardController/findOnServerBoard";
	public static final String FWKB_DF_Get_Url = "http://www.xiaohoutai.com.cn:8888/XHT/serverBoardController/findAllyue";

	public static final String WEIXI_LJZF_URL = "http://1291115210.tunnel.2bdata.com/XHT/appwxzf/appwxyuzhifu";

	//变更后提交的服务器
	public static final String BIANGENG_LIST_POST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/saveBizchangeorderInfo";

	//注销要提交的服务器
	public static final String ZHUXIAO_LIST_POST_URL ="http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/saveCancelCompanyOrderInfo";// TODO

	//获取变更资金的公司和ID
	public static final String ZHUCE_GET_BIAN_GENG_URL="http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/loadCompanyNameAndPriceOfChgRgtCapital";

	//获取注销公司的公司名和ID
	public static final String ZHUXIAO_GET_BIAN_GENG_URL="http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/loadCompanyNameAndPriceOfCancelCompany";

	//获取变更服务的公司名和价格
	public static final String FUWU_GET_BIAN_GENG_URL="http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/loadCompanyNameAndPriceOfChgBusiness";
	//变更服务提交的服务器
	public static final String BIANGENG_SERVICE_LIST_POST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/bizchangeorderController/savaChangeBusinessOrderInfo";

	public static final String FUWU_GET_ZHANGHU_URL="http://www.xiaohoutai.com.cn:8888/XHT/ordercontactController/loadUserInfo";

	//获取账户参数信息
	public static final String FUWU_POST_ZHANGHU_URL="http://www.xiaohoutai.com.cn:8888/XHT/ordercontactController/modifyContactInfo";

	//完善信息--提交
	public static final String FUWU_POST_COMPLETE_URL="";

}

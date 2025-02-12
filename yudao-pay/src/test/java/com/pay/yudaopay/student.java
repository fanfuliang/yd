package com.pay.yudaopay;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.pay.yudaopay.util.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2020/03/18 11:49 <br>
 * @see com.pay.yudaopay <br>
 */
public class student{


    public static String GetSignKey() throws Exception {
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("mch_id", "1580751521");//需要真实商户号
//        param.put("appid", "wx9f93c62828812af5");
//        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符
//        String sign = WXPayUtil.generateSignature(param,"2ab9071n06b9k739b950ddb41db2790d", WXPayConstants.SignType.MD5);//通过SDK生成签名其中API_KEY为商户对应的真实密钥
//        param.put("sign", sign);
//        String xml = WXPayUtil.mapToXml(param);//将map转换为xml格式
//        System.out.println(xml);
//        String url = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";//沙箱密钥获取api
//        String SignKey = HttpUtils.sendPost(url, xml);//
//        System.out.println("signkey+"+SignKey);
//        Map<String, String> param1 = new HashMap<String, String>();
//        param1 = WXPayUtil.xmlToMap(SignKey);
//        String key = param1.get("sandbox_signkey");
//
//        System.out.println(key);

//        WXPayConfig config = new WXPayConfig();
//
//        WXPay wxPay = new WXPay(config);
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("mch_id", "1580751521");
//        params.put("nonce_str", WXPayUtil.generateNonceStr());
//        String signSX = WXPayUtil.generateSignature(params, "2ab9071n06b9k739b950ddb41db2790d");
//        params.put("sign", signSX);
//        String strXML = wxPay.requestWithoutCert("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey",
//                params, this.getHttpConnectTimeoutMs(), this.getHttpReadTimeoutMs());
//
//        Map<String, String> result = WXPayUtil.xmlToMap(strXML);
//        System.out.println("retrieveSandboxSignKey:" + result);
//        if ("SUCCESS".equals(result.get("return_code"))) {
//
//            return result.get("sandbox_signkey");
//        }
//
//        //得到sandbox_signkey之后。替换正式的密钥key.然后调用统一下单接口。
//
//        data.put("sign",  WXPayUtil.generateSignature(params, iWxPayConfig.getKey()));
//        System.out.println("发起微信支付下单接口, request={}"+ data.toString());
//        Map<String, String> response = wxpay.unifiedOrder(data);


        return null;
    }

    public static void main(String[] args) {
        try {
            GetSignKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

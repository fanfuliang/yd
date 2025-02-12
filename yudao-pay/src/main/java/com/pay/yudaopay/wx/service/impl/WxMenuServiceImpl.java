package com.pay.yudaopay.wx.service.impl;

import com.pay.yudaopay.util.wxUtil.HttpsClient;
import com.pay.yudaopay.util.wxUtil.WxConfig;
import com.pay.yudaopay.util.wxUtil.WxConstants;
import com.pay.yudaopay.util.wxUtil.WxUtil;
import com.pay.yudaopay.wx.model.Order;
import com.pay.yudaopay.wx.service.WxMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangye
 * @version 1.0
 * @description 微信支付实现类
 * @date 2019/12/19
 */
@Service("wxMenuService")
@Slf4j
public class WxMenuServiceImpl implements WxMenuService {

    @Override
    public String wxPayUrl(Order order, String signType) throws Exception {
        HashMap<String, String> data = new HashMap<>();
        //公众账号ID
        data.put("appid", WxConfig.appID);
        //商户号
        data.put("mch_id", WxConfig.mchID);
        //随机字符串
        data.put("nonce_str", WxUtil.getNonceStr());
        //商品描述
        data.put("body", order.getSubject());
        //商户订单号
        data.put("out_trade_no", order.getOrderNo());
        //标价币种
        data.put("fee_type", "CNY");
        //标价金额
        data.put("total_fee", String.valueOf(order.getTotalFee()));
        //用户的IP
        data.put("spbill_create_ip", order.getClintIp());
        //通知地址
        data.put("notify_url", WxConfig.unifiedorderNotifyUrl);
        //交易类型
        data.put("trade_type", "NATIVE");
        //签名类型
        data.put("sign_type", signType);
        //商品id
        data.put("product_id", order.getProductId());
        //签名 签名中加入key
        data.put("sign", WxUtil.getSignature(data, WxConfig.key, signType));

        String requestXML = WxUtil.mapToXml(data);
        String responseString = HttpsClient.httpsRequestReturnString(WxConstants.PAY_UNIFIEDORDER, HttpsClient.METHOD_POST, requestXML);
        //解析返回的xml
        Map<String, String> resultMap = WxUtil.processResponseXml(responseString, signType);
        if (resultMap.get(WxConstants.RESULT_CODE).equals("SUCCESS")) {
            return resultMap.get("code_url");
        }
        log.error(responseString);
        return null;
    }

    @Override
    public String wxOrderQuery(String orderNo, String signType) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        //公众账号ID
        data.put("appid", WxConfig.appID);
        //商户号
        data.put("mch_id", WxConfig.mchID);
        //随机字符串
        data.put("nonce_str", WxUtil.getNonceStr());
        //商户订单号
        data.put("out_trade_no", orderNo);
        //签名类型
        data.put("sign_type", signType);
        //签名 签名中加入key
        data.put("sign", WxUtil.getSignature(data, WxConfig.key, signType));
        String requestXML = WxUtil.mapToXml(data);
        String responseString = HttpsClient.httpsRequestReturnString(WxConstants.PAY_ORDERQUERY, HttpsClient.METHOD_POST, requestXML);
        //解析返回的xml
        Map<String, String> resultMap = WxUtil.processResponseXml(responseString, signType);
        if (resultMap.get(WxConstants.RESULT_CODE).equals("SUCCESS")) {
            /**
             * 订单支付状态
             * SUCCESS—支付成功
             * REFUND—转入退款
             * NOTPAY—未支付
             * CLOSED—已关闭
             * REVOKED—已撤销（刷卡支付）
             * USERPAYING--用户支付中
             * PAYERROR--支付失败(其他原因，如银行返回失败)
             */
            return resultMap.get("transaction_id");
        }
        return null;
    }

    @Override
    public String wxCloseOrder(String orderNo, String signType) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        //公众账号ID
        data.put("appid", WxConfig.appID);
        //商户号
        data.put("mch_id", WxConfig.mchID);
        //随机字符串
        data.put("nonce_str", WxUtil.getNonceStr());
        //商户订单号
        data.put("out_trade_no", orderNo);
        //签名类型
        data.put("sign_type", signType);
        //签名 签名中加入key
        data.put("sign", WxUtil.getSignature(data, WxConfig.key, signType));
        String requestXML = WxUtil.mapToXml(data);
        String responseString = HttpsClient.httpsRequestReturnString(WxConstants.PAY_CLOSEORDER, HttpsClient.METHOD_POST, requestXML);
        //解析返回的xml
        Map<String, String> resultMap = WxUtil.processResponseXml(responseString, signType);
        if (resultMap.get(WxConstants.RETURN_CODE).equals("SUCCESS")) {
            /**
             * 关闭订单状态
             * SUCCESS—关闭成功
             * FAIL—关闭失败
             */
            return resultMap.get("result_code");
        }
        return null;
    }

}

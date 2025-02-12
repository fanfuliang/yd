package com.pay.yudaopay.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝-当面付 控制器.
 * <p>
 * https://openclub.alipay.com/read.php?tid=1720&fid=40
 *
 * https://docs.open.alipay.com/203/105910
 *
 * @author Mengday Zhang
 * @version 1.0
 * @since 2018/6/4
 */
@Slf4j
@RestController
@RequestMapping("/alipay/f2fpay")
public class AlipayF2FPayController {

    @Autowired
    private AlipayClient alipayClient;

    /**
     * 生成二维码
     *
     * @param outTradeNo  商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     * @param totalAmount 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。
     * @param subject     订单标题
     * @param body        订单描述
     * @return
     */
    @RequestMapping(value = "/getQrcode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String getQrcode(String outTradeNo, String totalAmount, String subject, String body) {
        try {
            AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest(); // 创建API对应的request类
            //设置回调地址
//            alipayTradePrecreateRequest.setNotifyUrl(notifyUrl);
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalAmount);
            model.setSubject(subject);
//            model.setBody(body);
            model.setTimeoutExpress("15m");
//            model.setQrcodeWidth(5L); // 自定义二维码宽度
//            // 商品明细信息，按需传入
//            List<GoodsDetail> goodsDetailList = new ArrayList<>();
//            GoodsDetail goods1 = new GoodsDetail();
//            goods1.setGoodsId("");
//            goods1.setGoodsName("");
//            goods1.setQuantity(1L);
//            goods1.setPrice("0.01");
//            goodsDetailList.add(goods1);
//            model.setGoodsDetail(goodsDetailList);
            alipayTradePrecreateRequest.setBizModel(model);
            AlipayTradePrecreateResponse alipayTradePrecreateResponse = alipayClient.execute(alipayTradePrecreateRequest);
            String responseBody = alipayTradePrecreateResponse.getBody();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        } catch (AlipayApiException e) {
            log.error("", e);
            return "ERROR";
        }
    }
}

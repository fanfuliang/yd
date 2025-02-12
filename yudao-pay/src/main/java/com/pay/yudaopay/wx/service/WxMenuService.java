package com.pay.yudaopay.wx.service;

import com.pay.yudaopay.wx.model.Order;

public interface WxMenuService {


    /**
     * 生成支付二维码URL
     *
     * @param order    订单类
     * @param signType 签名类型
     * @throws Exception
     */
    String wxPayUrl(Order order, String signType) throws Exception;

    /**
     * 查询微信订单
     *
     * @param orderNo  订单号
     * @param signType 签名类型
     * @return
     */
    String wxOrderQuery(String orderNo, String signType) throws Exception;

    /**
     * 关闭微信支付订单
     *
     * @param orderNo  订单号
     * @param signType 签名类型
     * @return
     */
    String wxCloseOrder(String orderNo, String signType) throws Exception;
}

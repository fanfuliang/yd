package com.pay.yudaopay.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.pay.yudaopay.configuration.AlipayProperties;
import com.pay.yudaopay.feign.OrderService;
import com.pay.yudaopay.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @program: yudao
 * @description: 电脑网站支付 https://docs.open.alipay.com/270/105898/
 * @author: liudong
 * @create: 2020-07-31 10:24:51
 */
@Slf4j
@Controller
@RequestMapping("/alipay/page")
public class AlipayTradePagePayController {

    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayTradeService alipayTradeService;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AlipayController alipayController;
    @Autowired
    private AlipayProperties aliPayProperties;

    private String jumpAddr;

    @ResponseBody
    @GetMapping("/getUrl")
    public String getUrl() {
        return jumpAddr;
    }

    @GetMapping("/gotoPayPage")
    public void gotoPayPage(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 订单模型
            String productCode = "FAST_INSTANT_TRADE_PAY";
            String type = request.getParameter("type");
            String totalAmount = request.getParameter("totalAmount");
            String accountMoneyId = request.getParameter("accountMoneyId");
            String payType = request.getParameter("payType");
            String chargeType = request.getParameter("chargeType");
            jumpAddr = request.getParameter("jumpAddr");
            BigDecimal money = new BigDecimal(totalAmount);
            String accountBusinessId = null;
            //1 充值，2消费
            if ("1".equals(type)) {
                Result result = orderService.submitCharge(accountMoneyId, money, null, payType, chargeType);
                if (null == result.getStatus() || 200 != result.getStatus()) {
                    return;
                }
                accountBusinessId = result.getData().toString();
            }
            else if ("2".equals(type)) {
                String orderId = request.getParameter("orderId");
                String consumeType = request.getParameter("consumeType");
                Result result = orderService.submitConsume(accountMoneyId, money, null, payType, consumeType, orderId);
                if (null == result.getStatus() || 200 != result.getStatus()) {
                    return;
                }
                accountBusinessId = result.getData().toString();
            }

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(accountBusinessId);
            model.setProductCode(productCode);
            model.setTotalAmount(totalAmount);
            model.setSubject("支付测试");
            model.setBody("支付测试，共" + totalAmount + "元");

            AlipayTradePagePayRequest pagePayRequest = new AlipayTradePagePayRequest();
            pagePayRequest.setReturnUrl(alipayProperties.getReturnUrl());
            pagePayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
            pagePayRequest.setBizModel(model);

            // 调用SDK生成表单, 并直接将完整的表单html输出到页面
            String form = alipayClient.pageExecute(pagePayRequest).getBody();  //调用SDK生成表单

            response.setContentType("text/html;charset=" + alipayProperties.getCharset());
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     *
     * @param request
     * @param response
     * @throws AlipayApiException
     */
    @RequestMapping(value = "/alipayQrcode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String alipayQrcode(HttpServletRequest request, HttpServletResponse response) {
        try {
            String qr_code;
            String outTradeNo = request.getParameter("outTradeNo");
            String totalAmount = request.getParameter("totalAmount");

            AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest();//创建API对应的request类
            //设置回调地址
            alipayTradePrecreateRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalAmount);
            model.setSubject("支付测试");
            model.setBody("支付测试，共" + totalAmount + "元");
            alipayTradePrecreateRequest.setBizModel(model);
            AlipayTradePrecreateResponse alipayTradePrecreateResponse = alipayClient.execute(alipayTradePrecreateRequest);
            String body = alipayTradePrecreateResponse.getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);
            qr_code = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
            return qr_code;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    @GetMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        try {
            response.setContentType("text/html;charset=" + alipayProperties.getCharset());
            //商户订单号
            boolean verifyResult = alipayController.rsaCheckV2(request);
            if (verifyResult) {
                //验证成功
                //请在这里加上商户的业务逻辑程序代码，如保存支付宝交易号
                //支护宝的商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                Result result = orderService.businessSuccess(out_trade_no, trade_no);
                if (null != result.getStatus() && 200 == result.getStatus()) {
                    return "pagePaySuccess2";
                }
            }
            return "pagePayUnknown";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "pagePayUnknown";
        }
    }

    /**
     * 支付结果异步通知 https://opendocs.alipay.com/open/270/105902#s7
     * 支付宝会根据 API 中商户传入的 notify_url，通过 POST 请求的形式将支付结果作为参数通知到商户系统
     * 第一次交易状态改变（即时到账中此时交易状态是交易完成）时，不仅会返回同步处理结果，而且服务器异步通知页面也会收到支付宝发来的处理结果通知；
     * 程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是 success 这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String notify(HttpServletRequest request) throws UnsupportedEncodingException {
        // 一定要验签，防止黑客篡改参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder notifyBuild = new StringBuilder();
        parameterMap.forEach((key, value) -> notifyBuild.append(key + "=" + value[0] + "\n"));
        log.info(notifyBuild.toString());

        boolean flag = alipayController.rsaCheckV1(request);
        if (flag) {
            //交易状态
            // TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
            // TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
            String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            if (tradeStatus.equals("TRADE_SUCCESS")) {
                //注意：
                //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                Result result = orderService.businessSuccess(out_trade_no, trade_no);
                if (null != result.getStatus() && 200 == result.getStatus()) {
                    return "success";
                }
            }
        }
        return "fail";
    }

    /**
     * 订单查询(最主要用于查询订单的支付状态)
     *
     * @param outTradeNo 支付宝商户的订单号
     * @return
     */
    @GetMapping("/query")
    @ResponseBody
    public String query(String outTradeNo) throws Exception {

        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeNo);
        alipayTradeQueryRequest.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequest);

        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(outTradeNo);
        AlipayF2FQueryResult result = alipayTradeService.queryTradeResult(builder);
        log.info(result.getResponse().getBody());
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("查询返回该订单支付成功");
                Result businessResult = orderService.businessSuccess(outTradeNo, response.getTradeNo());
                if (null == businessResult.getStatus() || 200 != businessResult.getStatus()) {
                    log.error("更改商户业务状态失败");
                    return "ERROR";
                }
                break;
            case FAILED:
                log.error("查询返回该订单支付失败");
                break;
            case UNKNOWN:
                log.error("系统异常，订单支付状态未知");
                break;
            default:
                log.error("不支持的交易状态，交易返回异常");
                break;
        }
        return result.getTradeStatus().name();
    }


    /**
     * 订单查询 https://opendocs.alipay.com/apis/api_1/alipay.trade.query
     *
     * @param orderNo 商户订单号
     * @return
     */
    @GetMapping("/query2")
    @ResponseBody
    public String query2(String orderNo) throws Exception {
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderNo);
        alipayTradeQueryRequest.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequest);
        return response.getBody();
    }

    /**
     * 关闭交易
     *
     * @param orderNo
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("/close")
    @ResponseBody
    public String close(String orderNo) throws AlipayApiException {
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(orderNo);
        alipayRequest.setBizModel(model);

        AlipayTradeCloseResponse alipayResponse = alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }

    /**
     * 退款
     *
     * @param orderNo 商户订单号
     * @return
     */
    @PostMapping("/refund")
    @ResponseBody
    public String refund(String orderNo) throws AlipayApiException {
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount("0.01");
        // 退款原因
        model.setRefundReason("无理由退货");
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
//        model.setOutRequestNo(UUID.randomUUID().toString());
        alipayRequest.setBizModel(model);

        AlipayTradeRefundResponse alipayResponse = alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }

    /**
     * 退款查询
     *
     * @param orderNo       商户订单号
     * @param refundOrderNo 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部订单号
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("/refundQuery")
    @ResponseBody
    public String refundQuery(String orderNo, String refundOrderNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(orderNo);
        model.setOutRequestNo(refundOrderNo);
        alipayRequest.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse alipayResponse = alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }

    /**
     * billDate : 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * 查询对账单下载地址: https://docs.open.alipay.com/api_15/alipay.data.dataservice.bill.downloadurl.query/
     *
     * @param billDate
     */
    @GetMapping("/bill")
    @ResponseBody
    public void queryBill(String billDate) {
        // 1. 查询对账单下载地址
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillType("trade");
        model.setBillDate(billDate);
        request.setBizModel(model);
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                String billDownloadUrl = response.getBillDownloadUrl();
                System.out.println(billDownloadUrl);

                // 2. 下载对账单
                List<String> orderList = this.downloadBill(billDownloadUrl);
                System.out.println(orderList);
                // 3. 先比较支付宝的交易合计/退款合计笔数/实收金额是否和自己数据库中的数据一致，如果不一致证明有异常，再具体找出那些订单有异常
                // 查找支付宝支付成功而自己支付失败的记录和支付宝支付失败而自己认为支付成功的异常订单记录到数据库

            }
            else {
                // 失败
                String code = response.getCode();
                String msg = response.getMsg();
                String subCode = response.getSubCode();
                String subMsg = response.getSubMsg();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载下来的是一个【账号_日期.csv.zip】文件（zip压缩文件名，里面有多个.csv文件）
     * 账号_日期_业务明细 ： 支付宝业务明细查询
     * 账号_日期_业务明细(汇总)：支付宝业务汇总查询
     * <p>
     * 注意：如果数据量比较大，该方法可能需要更长的执行时间
     *
     * @param billDownLoadUrl
     * @return
     * @throws IOException
     */
    private List<String> downloadBill(String billDownLoadUrl) throws IOException {
        String ordersStr = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .build();
        HttpGet httpRequest = new HttpGet(billDownLoadUrl);
        httpRequest.setConfig(config);
        CloseableHttpResponse response = null;
        byte[] data = null;
        try {
            response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toByteArray(entity);
        } finally {
            response.close();
            httpClient.close();
        }
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data), Charset.forName("GBK"));
        ZipEntry zipEntry = null;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    String name = zipEntry.getName();
                    // 只要明细不要汇总
                    if (name.contains("汇总")) {
                        continue;
                    }
                    byte[] byteBuff = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipInputStream.read(byteBuff)) != -1) {
                        byteArrayOutputStream.write(byteBuff, 0, bytesRead);
                    }
                    ordersStr = byteArrayOutputStream.toString("GBK");
                } finally {
                    byteArrayOutputStream.close();
                    zipInputStream.closeEntry();
                }
            }
        } finally {
            zipInputStream.close();
        }

        if (ordersStr.equals("")) {
            return null;
        }
        String[] bills = ordersStr.split("\r\n");
        List<String> billList = Arrays.asList(bills);
        billList = billList.parallelStream().map(item -> item.replace("\t", "")).collect(Collectors.toList());

        return billList;
    }
}

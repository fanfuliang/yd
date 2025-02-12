package com.pay.yudaopay.feign;

import com.pay.yudaopay.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2019/07/09 10:55 <br>
 * @see com.yudao.datamine.slave.v1.feign <br>
 */
@FeignClient(value = "yudao-baseservices-compose")
public interface OrderService{
    /**
     * 提交充值
     * @param accountMoneyId 资金账户id
     * @param money 消费金额
     * @param outPayTradeNo 其他支付平台交易号
     * @param payType 支付类型
     * @param chargeType 充值类型
     * @return
     */
    @PostMapping(value = "/accountMoney/submitCharge")
    Result submitCharge(@RequestParam("accountMoneyId") String accountMoneyId,
                        @RequestParam("money") BigDecimal money,
                        @RequestParam("outPayTradeNo") String outPayTradeNo,
                        @RequestParam("payType") String payType,
                        @RequestParam("chargeType") String chargeType);


    /**
     * 提交消费
     * @param accountMoneyId 资金账户id
     * @param money 消费金额
     * @param outPayTradeNo 其他支付平台交易号
     * @param payType 支付类型
     * @param consumeType 消费类型
     * @param orderId 订单id
     * @return
     */
    @PostMapping(value = "/accountMoney/submitConsume")
    Result submitConsume(@RequestParam("accountMoneyId") String accountMoneyId,
                         @RequestParam("money") BigDecimal money,
                         @RequestParam("outPayTradeNo") String outPayTradeNo,
                         @RequestParam("payType") String payType,
                         @RequestParam("consumeType") String consumeType,
                         @RequestParam("orderId")String orderId);


    /**
     * 业务成功
     * @param accountBusinessId 业务id
     * @param outPayTradeNo 其他支付平台交易号
     * @return
     */
    @PostMapping(value = "/accountMoney/businessSuccess")
    Result businessSuccess(@RequestParam("accountBusinessId") String accountBusinessId,
                           @RequestParam("outPayTradeNo")String outPayTradeNo);

    /**
     * 业务成功
     * @param accountBusinessId 业务id
     * @return
     */
    @PostMapping(value = "/accountMoney/businessFail")
    Result businessFail(@RequestParam("accountBusinessId")String accountBusinessId);
}

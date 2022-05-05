package com.controller;

import com.business.PayBusinessImpl;
import com.dto.CancelBillListDTO;
import com.dto.PaymentDTO;
import com.entity.Bill;
import com.utils.Response;
import com.utils.ResponseCode;
import com.vo.BillStatusVO;
import com.vo.NewBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author alexzhengzzz
 * @date 5/5/22 06:39
 */
@RestController()
@RequestMapping("/")
@Slf4j
@Api("PayController")
public class PayController {

    @Autowired
    private PayBusinessImpl payBusiness;

    @ApiOperation("only make a payment third party")
    @PostMapping("/payment")
    public Response<NewBillVO> pay(@RequestBody PaymentDTO paymentDTO) {
        NewBillVO bill = payBusiness.pay(paymentDTO);
        return new Response<>(ResponseCode.SUCCESS, bill);
    }

    @ApiOperation("make payment")
    @GetMapping("/bill/{billId}")
    public Response<BillStatusVO> getBillStatus(@PathVariable("billId") Long billId) {
        BillStatusVO billStatusVO = payBusiness.getBillStatus(billId);
        return new Response<>(ResponseCode.SUCCESS, billStatusVO);
    }

    @ApiOperation("make payment")
    @PostMapping("/bill/u/{billId}")
    public Response<BillStatusVO> changeBillStatus(@PathVariable("billId") Long billId) {
        BillStatusVO billStatusVO = payBusiness.changeBillStatus(billId);
        return new Response<>(ResponseCode.SUCCESS, billStatusVO);
    }
    // only success
    @ApiOperation("cancel all the payments")
    @PostMapping("/bills")
    public Response<BillStatusVO> cancelPayments(@RequestBody CancelBillListDTO cancelBillListDTO) {
        payBusiness.cancelPayments(cancelBillListDTO);
        return new Response<>(ResponseCode.SUCCESS);
    }

    //@ApiOperation("make payment")
    //@GetMapping("/refund")
    //public String refund() {
    //    log.info("connection test success");
    //    return "succes111231321s";
    //}




}
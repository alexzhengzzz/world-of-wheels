package com.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dto.CancelBillListDTO;
import com.dto.PaymentDTO;
import com.entity.Bill;
import com.entity.CardInfo;
import com.service.IBillService;
import com.service.ICardInfoService;
import com.utils.ErrorCode;
import com.utils.GeneralExceptionFactory;
import com.vo.BillStatusVO;
import com.vo.NewBillVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author alexzhengzzz
 * @date 5/5/22 12:08
 */
@Service
public class PayBusinessImpl implements PayBusiness {
    @Autowired
    private ICardInfoService cardInfoService;

    @Autowired
    private IBillService billService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewBillVO pay(PaymentDTO paymentDTO) {
        // check card info
        CardInfo cardInfo = checkAndGetCardInfo(paymentDTO);
        // update balance and insert bill record
        Bill bill = getBill(paymentDTO);

        //cardInfo.setBalance(cardInfo.getBalance().subtract(paymentDTO.getAmount()));
        //Boolean isSuccess = cardInfoService.updateById(cardInfo);
        //if (!isSuccess) {
        //    throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "update card info failed");
        //}
        Boolean isSuccess = billService.save(bill);
        if (!isSuccess) {
            throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "insert bill failed");
        }

        return convertToNewBillVO(bill);
    }

    public static NewBillVO convertToNewBillVO(Bill item) {
        NewBillVO result = new NewBillVO();
        result.setBillId(item.getBillId());
        return result;
    }

    @Override
    public BillStatusVO getBillStatus(Long billId) {
        Bill bill = billService.getById(billId);
        if (Objects.isNull(bill)) {
            throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "bill not found");
        }
        return convertToBillStatusVO(bill);
    }

    public static BillStatusVO convertToBillStatusVO(Bill item) {
        BillStatusVO result = new BillStatusVO();
        result.setBillId(item.getBillId());
        result.setStatus(item.getStatus());
        return result;
    }

    private Bill getBill(PaymentDTO paymentDTO) {
        Bill bill = new Bill();
        bill.setCardNum(paymentDTO.getCardNum());
        bill.setBillAmount(paymentDTO.getAmount());
        return bill;
    }

    private CardInfo checkAndGetCardInfo(PaymentDTO paymentDTO) {
        CardInfo cardInfo = cardInfoService.getOne(new LambdaQueryWrapper<CardInfo>().
                eq(CardInfo::getCardNum, paymentDTO.getCardNum())
                .eq(CardInfo::getCvv, paymentDTO.getCvv())
                .eq(CardInfo::getExpiredDate, paymentDTO.getExpiredDate()));
        if (Objects.isNull(cardInfo)) {
            throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "card info not found");
        }
        if (paymentDTO.getAmount().compareTo(cardInfo.getBalance()) > 0) {
            throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "balance not enough");
        }
        return cardInfo;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BillStatusVO changeBillStatus(Long billId) {
        Bill bill = billService.getById(billId);
        if (bill.getStatus() == 1) {
            return convertToBillStatusVO(bill);
        } else {
            CardInfo cardInfo = cardInfoService.getOne(new LambdaQueryWrapper<CardInfo>().eq(CardInfo::getCardNum, bill.getCardNum()));
            if (cardInfo == null) {
                throw GeneralExceptionFactory.create(ErrorCode.PAY_ERROR, "card info not found");
            }
            bill.setStatus(1);
            cardInfo.setBalance(cardInfo.getBalance().subtract(bill.getBillAmount()));
            cardInfoService.updateById(cardInfo);
            billService.updateById(bill);
            return convertToBillStatusVO(bill);
        }
    }
    @Override
    public void cancelPayments(CancelBillListDTO cancelBillListDTO) {
        cancelBillListDTO.getBillIds().forEach(billId -> {
            Bill bill = billService.getById(billId);
            if (bill != null && bill.getStatus() == 1) {
                bill.setStatus(0);
                billService.updateById(bill);
                String cardNum = bill.getCardNum();
                CardInfo cardInfo = cardInfoService.getOne(new LambdaQueryWrapper<CardInfo>().eq(CardInfo::getCardNum, cardNum));
                cardInfo.setBalance(cardInfo.getBalance().add(bill.getBillAmount()));
                cardInfoService.updateById(cardInfo);
            }
        });
    }
}

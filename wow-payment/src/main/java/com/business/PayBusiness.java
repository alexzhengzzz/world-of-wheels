package com.business;

import com.dto.CancelBillListDTO;
import com.dto.PaymentDTO;
import com.vo.BillStatusVO;
import com.vo.NewBillVO;

/**
 * @author alexzhengzzz
 * @date 5/5/22 12:08
 */
public interface PayBusiness {
    NewBillVO pay(PaymentDTO paymentDTO);

    BillStatusVO getBillStatus(Long billId);

    BillStatusVO changeBillStatus(Long billId);

    void cancelPayments(CancelBillListDTO cancelBillListDTO);
}

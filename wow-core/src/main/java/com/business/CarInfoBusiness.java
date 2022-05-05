package com.business;

import com.bo.CarInfoBO;
import com.vo.CarInfoVO;

import java.util.List;

public interface CarInfoBusiness {
    // 0 entire, 1 valid, 2 out stock
    public List<CarInfoBO> getCarList(int type);
}

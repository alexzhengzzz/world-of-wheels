package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Office;
import com.vo.OfficeVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zmh
 * @since 2022-05-02
 */
public interface OfficeService extends IService<Office> {
    List<OfficeVO> getOfficeInfo();
}

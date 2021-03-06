package com.business.impl;

import com.annotation.PermissionChecker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.business.CorporationBusiness;
import com.dto.CorpEmployeeDTO;
import com.dto.CorporationDTO;
import com.entity.CorpEmployee;
import com.entity.Corporation;
import com.entity.User;
import com.enums.Role;
import com.enums.RoleType;
import com.exception.ErrorCode;
import com.exception.GeneralExceptionFactory;
import com.service.ICorpEmployeeService;
import com.service.ICorporationService;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CorporationBusinessImpl implements CorporationBusiness {

    @Autowired
    private ICorporationService corporationService;

    @Autowired
    private ICorpEmployeeService corpEmployeeService;

    @Autowired
    private UserService userService;


    @Override
    @PermissionChecker(requiredRole = Role.ADMIN)
    public void createCorporation(CorporationDTO corporationDTO) {
        // check if exist
        Corporation corporation = corporationService.getOne(new LambdaQueryWrapper<Corporation>().eq(Corporation::getCompanyName, corporationDTO.getCompanyName()));
        if (!Objects.isNull(corporation)) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_QUERY_EXISTED_ERROR, "corporation existed");
        }
        corporation = getCorporation(corporationDTO);
        Boolean isSuccess = corporationService.save(corporation);
        if (!isSuccess) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_INSERT_ERROR);
        }
    }

    @Override
    @PermissionChecker(requiredRole = Role.ADMIN)
    public void deleteCorporation(CorporationDTO corporationDTO) {
        //  delete corporation according to company name
        String companyName = corporationDTO.getCompanyName();
        Corporation corporation = corporationService.getOne(new LambdaQueryWrapper<Corporation>().eq(Corporation::getCompanyName, companyName));
        if (corporation == null) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_QUERY_NOT_EXISTED_ERROR, "no such company in database");
        }
        boolean isSuccess = corporationService.remove(new LambdaQueryWrapper<>(corporation).eq(Corporation::getCompanyName, companyName));
        if (!isSuccess) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_DELETE_ERROR);
        }
    }

    @Override
    @PermissionChecker(requiredRole = Role.USER, requiredRoleType = RoleType.CORPORATION)
    public void addEmployeeToCorporation(CorpEmployeeDTO corpEmployeeDTO) {
        String companyName = corpEmployeeDTO.getCompanyName();
        String employeeId = corpEmployeeDTO.getEmployeeId();

        Corporation co = corporationService.getOne(new LambdaQueryWrapper<Corporation>().eq(Corporation::getCompanyName, companyName));
        if (co == null) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_QUERY_EXISTED_ERROR, "no such company in database");
        }
        Long corpId = co.getCorpId();
        // check if exist in corp_employee
        CorpEmployee ce = corpEmployeeService.getOne(new LambdaQueryWrapper<CorpEmployee>().eq(CorpEmployee::getCorpId, corpId).eq(CorpEmployee::getEmployeeId, employeeId));
        if (ce != null) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_QUERY_EXISTED_ERROR);
        }
        CorpEmployee corpEmployee = new CorpEmployee();
        corpEmployee.setEmployeeId(employeeId);
        corpEmployee.setCorpId(corpId);
        Boolean isSuccess = corpEmployeeService.save(corpEmployee);
        if (!isSuccess) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_INSERT_ERROR);
        }
    }

    @Override
    public List<User> getEmployeeList(String companyName) {
        Corporation corporation = corporationService.getOne(new LambdaQueryWrapper<Corporation>().eq(Corporation::getCompanyName, companyName));
        if (corporation == null) {
            throw GeneralExceptionFactory.create(ErrorCode.DB_QUERY_NOT_EXISTED_ERROR);
        }
        Long corpId = corporation.getCorpId();
        List<CorpEmployee> corpEmployees = corpEmployeeService.list(new LambdaQueryWrapper<CorpEmployee>().eq(CorpEmployee::getCorpId, corpId));
        List<User> users = new ArrayList<>();
        for (CorpEmployee ce : corpEmployees) {
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmployeeId, ce.getEmployeeId()));
            if (user!= null) {
                users.add(user);
            }
        }
        return users;
    }


    private Corporation getCorporation(CorporationDTO corporationDTO) {
        Corporation corporation = new Corporation();
        corporation.setCompanyName(corporationDTO.getCompanyName());
        corporation.setRegisterCode(corporationDTO.getRegisterCode());
        return corporation;
    }





}

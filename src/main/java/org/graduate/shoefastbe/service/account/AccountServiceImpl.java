package org.graduate.shoefastbe.service.account;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.base.error_success_handle.SuccessHandle;
import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.common.enums.RoleEnums;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.graduate.shoefastbe.entity.AccountEntity;
import org.graduate.shoefastbe.mapper.AccountDetailMapper;
import org.graduate.shoefastbe.mapper.AccountMapper;
import org.graduate.shoefastbe.repository.AccountDetailRepository;
import org.graduate.shoefastbe.repository.AccountRepository;
import org.graduate.shoefastbe.validation.AccountValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountValidationHelper accountValidationHelper;
    private final AccountDetailRepository accountDetailRepository;
    private final AccountDetailMapper accountDetailMapper;
    private final AccountMapper accountMapper;
    @Override
    @Transactional
    public SuccessResponse singUp(AccountCreateRequest account) {
        accountValidationHelper.signUpValidate(account);
        AccountEntity accountEntity = accountMapper.getEntityFromRequest(account);
        accountEntity.setIsActive(Boolean.TRUE);
        accountEntity.setRole(RoleEnums.CUSTOMER.name());
        accountEntity.setCreateDate(LocalDate.now());
        accountEntity.setModifyDate(LocalDate.now());
        accountRepository.saveAndFlush(accountEntity);
        AccountDetailEntity accountDetailEntity = accountDetailMapper.getEntityFromRequest(account);
        accountDetailEntity.setAccountId(accountEntity.getId());
        accountDetailRepository.save(accountDetailEntity);
        return SuccessHandle.success(CodeAndMessage.ME106);
    }
}

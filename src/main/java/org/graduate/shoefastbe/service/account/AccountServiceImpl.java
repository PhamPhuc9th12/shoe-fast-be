package org.graduate.shoefastbe.service.account;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.authen.TokenHelper;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.base.error_success_handle.SuccessHandle;
import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.common.enums.RoleEnums;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.*;
import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.graduate.shoefastbe.entity.AccountEntity;
import org.graduate.shoefastbe.mapper.AccountDetailMapper;
import org.graduate.shoefastbe.mapper.AccountMapper;
import org.graduate.shoefastbe.repository.AccountDetailRepository;
import org.graduate.shoefastbe.repository.AccountRepository;
import org.graduate.shoefastbe.util.MailUtil;
import org.graduate.shoefastbe.validation.AccountValidationHelper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

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
        accountEntity.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
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

    @Override
    public TokenAndRole login(LoginRequest loginRequest) {
        AccountEntity userEntity = accountRepository.findByUsername(loginRequest.getUsername());
        if (Objects.isNull(userEntity) || Boolean.FALSE.equals(userEntity.getIsActive())){
            throw new RuntimeException(CodeAndMessage.ERR4);
        }
        String currentHashedPassword = userEntity.getPassword();
        AccountDetailEntity accountDetailEntity = accountDetailRepository.findByAccountId(userEntity.getId());
        if (BCrypt.checkpw(loginRequest.getPassword(), currentHashedPassword)){
            String accessToken = TokenHelper.generateToken(userEntity);
            return new TokenAndRole(accessToken, userEntity.getRole(), accountDetailEntity.getFullName());
        }
        throw new RuntimeException(CodeAndMessage.ERR1);
    }

    @Override
    public AccountResponse findByUsername(String accessToken) {
        String username = TokenHelper.getUsernameFromToken(accessToken);
        AccountEntity accountEntity = accountRepository.findByUsername(username);
       return getAccountDetail(accountEntity, accountEntity.getId());
    }

    @Override
    @Transactional
    public SuccessResponse forgotPassword(ForgotPassRequest forgotPassRequest) throws MessagingException {
        AccountEntity accountEntity = accountRepository.findByUsername(forgotPassRequest.getUsername());
        if(Objects.isNull(accountEntity)) throw new RuntimeException(CodeAndMessage.ERR2);
        String newPassword = String.valueOf(UUID.randomUUID());
        accountEntity.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        accountRepository.saveAndFlush(accountEntity);
        //gửi mail
        AccountDetailEntity accountDetail = accountDetailRepository.findByAccountId(accountEntity.getId());
        MailUtil.sendmailForgotPassword(accountDetail.getEmail(), newPassword);
        return SuccessHandle.success(CodeAndMessage.ME100);
    }

    @Override
    public AccountResponse getDetailById(Long id) {
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR2)
        );
        return getAccountDetail(accountEntity, id);
    }
    @Override
    @Transactional
    public AccountDetailEntity updateProfile(AccountUpdateRequest accountUpdateRequest) {
        AccountDetailEntity accountDetailEntity = accountDetailRepository.findByAccountId(accountUpdateRequest.getId());
        accountDetailMapper.updateEntityByUpdateAccount(accountDetailEntity, accountUpdateRequest);
        return accountDetailEntity;
    }

    private AccountResponse getAccountDetail(AccountEntity accountEntity, Long id){
        AccountDetailEntity accountDetailEntity = accountDetailRepository.findByAccountId(id);
        return AccountResponse.builder()
                .id(accountEntity.getId())
                .address(accountDetailEntity.getAddress())
                .email(accountDetailEntity.getEmail())
                .phone(accountDetailEntity.getPhone())
                .gender(accountDetailEntity.getGender())
                .username(accountEntity.getUsername())
                .birthDate(accountDetailEntity.getBirthdate())
                .fullName(accountDetailEntity.getFullName())
                .roleName(accountEntity.getRole())
                .isActive(accountEntity.getIsActive())
                .createDate(accountEntity.getCreateDate())
                .modifyDate(accountEntity.getModifyDate())
                .build();
    }
}
